package com.home_manager.web;

import com.home_manager.model.dto.AddFeeDTO;
import com.home_manager.model.entities.Fee;
import com.home_manager.model.entities.Home;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.enums.Notifications;
import com.home_manager.model.user.HomeManagerUserDetails;
import com.home_manager.service.FeeService;
import com.home_manager.service.HomeService;
import com.home_manager.service.HomesGroupService;
import com.home_manager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        modelAndView.setViewName("manager/add-fee");

        modelAndView.addObject("homesGroup", this.homesGroupService.getHomesGroupById(homesGroupId));

        return modelAndView;
    }

    @ModelAttribute("addFeeDTO")
    private AddFeeDTO addFeeDTO() {
        return new AddFeeDTO();
    }

    @PostMapping("/add-fee")
    private String addFee(@PathVariable long homesGroupId, @Valid AddFeeDTO addFeeDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                          @RequestParam Map<String, String> inputs, @AuthenticationPrincipal HomeManagerUserDetails user) {

        inputs.remove("_csrf");
        String name = inputs.remove("name");
        double value = Double.parseDouble(inputs.remove("value"));

        if (isAuthorized(homesGroupId, user.getId())) {
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("addFeeDTO", addFeeDTO);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addFeeDTO", bindingResult);

                return "redirect:/profile/homesGroup{homesGroupId}/add-fee";
            } else if (inputs.size() == 0) {
                redirectAttributes.addFlashAttribute("addFeeDTO", addFeeDTO);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addFeeDTO", bindingResult);
                redirectAttributes.addFlashAttribute("fail", Notifications.CHOOSE_AT_LEAST_ONE_HOME.getValue());

                return "redirect:/profile/homesGroup{homesGroupId}/add-fee";
            }

            List<Long> homes = inputs.entrySet().stream().map(entry -> {
                if (entry.getValue().equals("true")) {
                    return Long.parseLong(entry.getKey());
                }

                return null;
            }).filter(Objects::nonNull).toList();

            Fee fee = this.feeService.addFee(name, value, this.homesGroupService.getHomesGroupById(homesGroupId));
            homes.forEach(h -> this.homeService.setFeeToHome(h, fee));

            redirectAttributes.addFlashAttribute("success",
                    inputs.size() == 1 ? Notifications.FEE_ADD_FOR_HOME.getValue() : Notifications.FEE_ADD_FOR_HOMES.getValue());
            return "redirect:/profile/homesGroup{homesGroupId}";
        } else {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

}


    @GetMapping("/edit-fee{feeId}")
    public ModelAndView editFee(@PathVariable long homesGroupId, @PathVariable long feeId, @AuthenticationPrincipal HomeManagerUserDetails user) {
        if (isAuthorized(homesGroupId, user.getId())) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("manager/edit-fee");

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
                          @RequestParam Map<String, String> inputs, RedirectAttributes redirectAttributes) {

        if (isAuthorized(homesGroupId, user.getId())) {

            inputs.remove("_csrf");
            String name = inputs.remove("name");
            double value = Double.parseDouble(inputs.remove("value"));

            Fee fee = this.feeService.changeFee(feeId, name, value);

            Map<Long, Boolean> homesMap = new LinkedHashMap<>();
            inputs.forEach((key, value1) ->
                    homesMap.put(Long.parseLong(key),
                            Boolean.parseBoolean(value1)));

            this.homeService.changeHomesFee(homesMap, fee);

            redirectAttributes.addFlashAttribute("success", Notifications.UPDATED_SUCCESSFULLY.getValue());
            return "redirect:/profile/homesGroup{homesGroupId}";
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
