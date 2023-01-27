package com.home_manager.web;

import com.home_manager.model.entities.Fee;
import com.home_manager.model.entities.Home;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.user.HomeManagerUserDetails;
import com.home_manager.service.FeeService;
import com.home_manager.service.HomeService;
import com.home_manager.service.HomesGroupService;
import com.home_manager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/profile/homesGroup{homesGroupId}")
public class FeeController {

    private final UserService userService;
    private final HomesGroupService homesGroupService;
    private final FeeService feeService;
    private final HomeService homeService;

    public FeeController(UserService userService, HomesGroupService homesGroupService, FeeService feeService, HomeService homeService) {
        this.userService = userService;
        this.homesGroupService = homesGroupService;
        this.feeService = feeService;
        this.homeService = homeService;
    }

    private boolean isAuthorized(long homeGroupId, long userId) {
        return this.userService.isOwner(homeGroupId, userId);
    }

    @GetMapping("/add-fee")
    private ModelAndView addFee(@PathVariable long homesGroupId) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-fee");

        modelAndView.addObject("homesGroup", this.homesGroupService.getHomesGroupById(homesGroupId));

        return modelAndView;
    }

    @PostMapping("/add-fee")
    private String addFee(@PathVariable long homesGroupId, String name, double value,
                          @RequestParam(name = "homes", required = false) List<Long> homes, RedirectAttributes redirectAttributes,
                          @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (homes == null) {
            redirectAttributes.addFlashAttribute("fail", "Изберете поне един дом!");
            return "redirect:/profile/homesGroup{homesGroupId}/add-fee";
        }

        if (isAuthorized(homesGroupId, user.getId())) {
            Fee fee = this.feeService.addFee(name, value, this.homesGroupService.getHomesGroupById(homesGroupId));
            homes.forEach(h -> this.homeService.setFeeToHome(h, fee));

            redirectAttributes.addFlashAttribute("success", "Такса " + name + " e добавена за избраните домове");
            return "redirect:/profile/homesGroup{homesGroupId}";
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/edit-fee{feeId}")
    public ModelAndView editFee(@PathVariable long homesGroupId, @PathVariable long feeId, @AuthenticationPrincipal HomeManagerUserDetails user) {
        if (isAuthorized(homesGroupId, user.getId())) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("edit-fee");

            HomesGroup homesGroup = this.homesGroupService.getHomesGroupById(homesGroupId);
            modelAndView.addObject("homesGroup", homesGroup);

            Map<Home, Boolean> homes = new LinkedHashMap<>(homesGroup.getHomes().size());
            homesGroup.getHomes().
                    forEach(h -> homes.put(h, h.getFees().stream().anyMatch(f -> f.getFee().getId() == feeId)));
            modelAndView.addObject("homes", homes);

            modelAndView.addObject("fee", this.feeService.getFeeById(feeId));

            return modelAndView;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/edit-fee{feeId}")
    public String editFee(@PathVariable long homesGroupId, @PathVariable long feeId, @AuthenticationPrincipal HomeManagerUserDetails user,
                          @RequestParam Map<String, String> inputs,
                          RedirectAttributes redirectAttributes) {

        inputs.remove("_csrf");
        String name = inputs.remove("name");
        double value  = Double.parseDouble(inputs.remove("value"));

        if (isAuthorized(homesGroupId, user.getId())) {

            Fee fee = this.feeService.changeFee(feeId, name, value);

            Map<Long, Boolean> homesMap = new LinkedHashMap<>();
            inputs.forEach((key, value1) ->
                    homesMap.put(Long.parseLong(key),
                            Boolean.parseBoolean(value1)));

            this.homeService.changeHomesFee(homesMap, fee);

            redirectAttributes.addFlashAttribute("success", "Таксата е редактирана успешно");
            return "redirect:/profile/homesGroup{homesGroupId}";
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }


    }
}
