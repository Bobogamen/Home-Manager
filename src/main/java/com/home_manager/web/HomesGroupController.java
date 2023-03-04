package com.home_manager.web;

import com.home_manager.model.dto.AddHomeDTO;
import com.home_manager.model.dto.AddHomesGroupDTO;
import com.home_manager.model.dto.AddResidentDTO;
import com.home_manager.model.entities.Fee;
import com.home_manager.model.entities.Home;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.entities.Resident;
import com.home_manager.model.enums.HomesGroupEnums;
import com.home_manager.model.enums.Notifications;
import com.home_manager.model.user.HomeManagerUserDetails;
import com.home_manager.service.HomeService;
import com.home_manager.service.HomesGroupService;
import com.home_manager.service.ResidentService;
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
import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping(value = "/profile")
public class HomesGroupController {

    private final UserService userService;
    private final HomesGroupService homesGroupService;
    private final HomeService homeService;
    private final ResidentService residentService;

    public HomesGroupController(UserService userService, HomesGroupService homesGroupService, HomeService homeService,
                                ResidentService residentService) {
        this.userService = userService;
        this.homesGroupService = homesGroupService;
        this.homeService = homeService;
        this.residentService = residentService;
    }

    private boolean isAuthorized(long homesGroupId, long userId) {
        return this.userService.isOwner(homesGroupId, userId);
    }

    @GetMapping("/homesGroup{homesGroupId}")
    ModelAndView viewHomesGroup(@PathVariable long homesGroupId, @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("manager/homes_group");

            HomesGroup homesGroup = this.homesGroupService.getHomesGroupById(homesGroupId);
            modelAndView.addObject("homesGroup", homesGroup);

            List<Fee> fees = homesGroup.getFees();
            modelAndView.addObject("fees", fees);

            double total = homesGroup.getHomes().stream().mapToDouble(Home::getTotalForMonth).sum();
            modelAndView.addObject("total", total);
//            modelAndView.addObject("total", new DecimalFormat("#.##").format(total));

            return modelAndView;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/homesGroup{homesGroupId}/add-home")
    public ModelAndView addHome(@PathVariable long homesGroupId, @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("manager/add_home");
            modelAndView.addObject("homesGroup", this.homesGroupService.getHomesGroupById(homesGroupId));
            return modelAndView;

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ModelAttribute("addHomeDTO")
    public AddHomeDTO addHomeDTO() {
        return new AddHomeDTO();
    }

    @ModelAttribute("addResidentDTO")
    public AddResidentDTO addResidentDTO() {
        return new AddResidentDTO();
    }

    @PostMapping("/homesGroup{homesGroupId}/add-home")
    public String addHome(@PathVariable long homesGroupId, @AuthenticationPrincipal HomeManagerUserDetails user,
                          @Valid AddHomeDTO addHomeDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (isAuthorized(homesGroupId, user.getId())) {

            boolean isResident = addHomeDTO.isResident();

            if (isResident) {
                boolean floor = bindingResult.hasFieldErrors("floor");
                boolean name = bindingResult.hasFieldErrors("name");
                boolean ownerFirstName = bindingResult.hasFieldErrors("ownerFirstName");

                if (floor || name || ownerFirstName) {
                    redirectAttributes.addFlashAttribute("addHomeDTO", addHomeDTO);
                    redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addHomeDTO", bindingResult);

                    return "redirect:/profile/homesGroup{homesGroupId}/add-home";
                }

            } else {
                if (bindingResult.hasErrors()) {
                    redirectAttributes.addFlashAttribute("addHomeDTO", addHomeDTO);
                    redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addHomeDTO", bindingResult);
                    redirectAttributes.addFlashAttribute("residentValue", false);

                    return "redirect:/profile/homesGroup{homesGroupId}/add-home";
                }
            }

            HomesGroup homesGroup = this.homesGroupService.getHomesGroupById(homesGroupId);
            Resident owner = this.residentService.addOwner(addHomeDTO);
            Home home = this.homeService.addHome(addHomeDTO, homesGroup);

            if (isResident) {
                this.homeService.setOwnerToHome(home.getId(), owner);
                this.homeService.addResidentToHome(home.getId(), owner);
                this.residentService.setResidentHome(home, owner);
            } else {
                Resident resident = this.residentService.addFirstResident(addHomeDTO);
                this.homeService.setOwnerToHome(home.getId(), owner);
                this.homeService.addResidentToHome(home.getId(), resident);
                this.residentService.setResidentHome(home, resident);
            }

            redirectAttributes.addFlashAttribute("success", Notifications.HOME_ADDED_SUCCESSFULLY.getValue());
            return "redirect:/profile/homesGroup{homesGroupId}";

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ModelAttribute("allTypes")
    public List<String> allTypes() {
        return Arrays.stream(HomesGroupEnums.values()).map(HomesGroupEnums::getValue).toList();
    }


    @GetMapping("/homesGroup{homesGroupId}/edit")
    public ModelAndView editHomesGroup(@PathVariable long homesGroupId, @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("manager/edit-homes_group");
            modelAndView.addObject("homesGroup", this.homesGroupService.getHomesGroupById(homesGroupId));

            return modelAndView;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ModelAttribute("addHomesGroupDTO")
    public AddHomesGroupDTO addHomesGroupDTO() {
        return new AddHomesGroupDTO();
    }

    @PutMapping("/homesGroup{homesGroupId}/edit")
    public String editHomesGroup(@PathVariable long homesGroupId, @AuthenticationPrincipal HomeManagerUserDetails user,
                                 @Valid AddHomesGroupDTO addHomesGroupDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (isAuthorized(homesGroupId, user.getId())) {
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("addHomesGroupDTO", addHomesGroupDTO);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addHomesGroupDTO", bindingResult);

                return "redirect:/edit/homesGroups{homesGroupId}";
            }

            this.homesGroupService.editHomesGroup(addHomesGroupDTO, homesGroupId);

            redirectAttributes.addFlashAttribute("success", Notifications.UPDATED_SUCCESSFULLY.getValue());
            return "redirect:/profile/homesGroup{homesGroupId}";
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
