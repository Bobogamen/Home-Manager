package com.home_manager.web;

import com.home_manager.model.dto.AddHomesGroupDTO;
import com.home_manager.model.user.HomeManagerUserDetails;
import com.home_manager.service.HomesGroupService;
import com.home_manager.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final HomesGroupService homesGroupService;

    public ProfileController(UserService userService, HomesGroupService homesGroupService) {
        this.userService = userService;
        this.homesGroupService = homesGroupService;
    }

    @GetMapping("")
    private ModelAndView profile(@AuthenticationPrincipal HomeManagerUserDetails user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");

        modelAndView.addObject("homeGroups", this.userService.getUserById(user.getId()).getHomesGroup());

        return modelAndView;
    }

    @GetMapping("/add-homes_group")
    private ModelAndView addHomesGroup() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-homes_group");
        return modelAndView;
    }

    @ModelAttribute("addHomesGroupDTO")
    public AddHomesGroupDTO addHomesGroupDTO() {
        return new AddHomesGroupDTO();
    }

    @PostMapping("/add-homes_group")
    private String addHomesGroup(@AuthenticationPrincipal HomeManagerUserDetails user,
                                 @Valid AddHomesGroupDTO addHomesGroupDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addHomesGroupDTO", addHomesGroupDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addHomesGroupDTO", bindingResult);

            return "redirect:/profile/add-homes_group";
        }

        this.homesGroupService.addHomesGroup(addHomesGroupDTO, this.userService.getUserById(user.getId()));

        redirectAttributes.addFlashAttribute("success", "Група " + addHomesGroupDTO.getName() + " e добавена");

        return "redirect:/profile";
    }
}
