package com.home_manager.web;

import com.home_manager.model.dto.AddHomesGroupDTO;
import com.home_manager.model.dto.RegistrationDTO;
import com.home_manager.model.enums.Notifications;
import com.home_manager.model.user.HomeManagerUserDetails;
import com.home_manager.service.EmailService;
import com.home_manager.service.HomesGroupService;
import com.home_manager.service.UserService;
import com.home_manager.utility.MailUtility;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final HomesGroupService homesGroupService;
    private final HttpServletRequest request;
    private final EmailService emailService;

    public ProfileController(UserService userService, HomesGroupService homesGroupService, EmailService emailService, HttpServletRequest request) {
        this.userService = userService;
        this.homesGroupService = homesGroupService;
        this.emailService = emailService;
        this.request = request;
    }

    @GetMapping("")
    private ModelAndView profile(@AuthenticationPrincipal HomeManagerUserDetails user) {

        if (this.request.isUserInRole("MANAGER")) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("profile");

            modelAndView.addObject("homeGroups", this.userService.getUserById(user.getId()).getHomesGroup());

            return modelAndView;
        } else {
            return new ModelAndView("redirect:/cashier");
        }
    }

    @GetMapping("/add-homes_group")
    private ModelAndView addHomesGroup() {

        if (this.request.isUserInRole("MANAGER")) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("manager/add-homes_group");
            return modelAndView;

        } else {
            return new ModelAndView("redirect:/cashier");
        }
    }

    @ModelAttribute("addHomesGroupDTO")
    public AddHomesGroupDTO addHomesGroupDTO() {
        return new AddHomesGroupDTO();
    }

    @PostMapping("/add-homes_group")
    private String addHomesGroup(@AuthenticationPrincipal HomeManagerUserDetails user,
                                 @Valid AddHomesGroupDTO addHomesGroupDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (this.request.isUserInRole("MANAGER")) {
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("addHomesGroupDTO", addHomesGroupDTO);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addHomesGroupDTO", bindingResult);

                return "redirect:/profile/add-homes_group";
            }

            this.homesGroupService.addHomesGroup(addHomesGroupDTO, this.userService.getUserById(user.getId()));

            redirectAttributes.addFlashAttribute("success", "Група " + addHomesGroupDTO.getName() + " e добавена");

            return "redirect:/profile";
        } else {
            return "redirect:/cashier";
        }
    }

    @GetMapping("/register-cashier")
    public ModelAndView registerCashier() {
        if (this.request.isUserInRole("MANAGER")) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("register");
            return modelAndView;

        } else {
            return new ModelAndView("redirect:/cashier");
        }
    }

    @ModelAttribute("registrationDTO")
    public RegistrationDTO registrationDTO() {
        return new RegistrationDTO();
    }

    @PostMapping("/register-cashier")
    public String register(@AuthenticationPrincipal HomeManagerUserDetails user, @Valid RegistrationDTO registrationDTO, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (this.request.isUserInRole("MANAGER")) {
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("registrationDTO", registrationDTO);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registrationDTO", bindingResult);

                return "redirect:/profile/register-cashier";
            }

            this.userService.registerCashier(registrationDTO, user.getId());
            this.emailService.sendCashierRegistrationEmail(registrationDTO.getEmail(), user, MailUtility.appUrl(request));

            redirectAttributes.addFlashAttribute("success", Notifications.CASHIER_REGISTRATION_SUCCESSFULLY.getValue());
        }

        return "redirect:/cashier";
    }

    @GetMapping("/assign")
    public ModelAndView assign(@AuthenticationPrincipal HomeManagerUserDetails user) {

        if (this.request.isUserInRole("MANAGER")) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("manager/assign");
            modelAndView.addObject("manager", this.userService.getUserById(user.getId()));

            return modelAndView;
        } else {
            return new ModelAndView("redirect:/cashier");
        }
    }

    @PostMapping("/assign{cashierId}")
    public String assign(@PathVariable long cashierId, @AuthenticationPrincipal HomeManagerUserDetails user,
                         @RequestParam(name = "homesGroup", required = false) List<Long> homesGroups, RedirectAttributes redirectAttributes) {

        if (this.request.isUserInRole("MANAGER")) {

            this.homesGroupService.homesGroupAssignment(user.getHomesGroup(), this.userService.getUserById(cashierId), homesGroups);

            if (homesGroups == null) {
                redirectAttributes.addFlashAttribute("fail", Notifications.CASHIER_NO_HOMES_GROUP.getValue());
            } else {
                redirectAttributes.addFlashAttribute("success", Notifications.ASSIGN_SUCCESSFULLY.getValue());
            }

            return "redirect:/profile/assign";

        } else {
            return "redirect:/cashier";
        }
    }

    @GetMapping("/edit-name")
    public ModelAndView editName(@AuthenticationPrincipal HomeManagerUserDetails user) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", this.userService.getUserById(user.getId()));
        modelAndView.setViewName("edit_name");

        return modelAndView;
    }

    @PostMapping("/edit-name")
    public String editName(@Valid RegistrationDTO registrationDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (bindingResult.hasFieldErrors("name")) {
            redirectAttributes.addFlashAttribute("registrationDTO", registrationDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registrationDTO", bindingResult);

            return "redirect:/profile/edit-name";
        }

        this.userService.editUserName(user.getId(), registrationDTO.getName());
        user.setName(registrationDTO.getName());

        redirectAttributes.addFlashAttribute("success", Notifications.UPDATED_SUCCESSFULLY.getValue());

        if (user.isCashier()) {
            return "redirect:/cashier";
        }

        return "redirect:/profile";
    }

    @GetMapping("/change-password")
    public ModelAndView changePassword(@AuthenticationPrincipal HomeManagerUserDetails user) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", this.userService.getUserById(user.getId()));
        modelAndView.setViewName("change_password");

        return modelAndView;
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid RegistrationDTO registrationDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                 @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (bindingResult.hasFieldErrors("password") || bindingResult.hasFieldErrors("confirmPassword")) {
            redirectAttributes.addFlashAttribute("registrationDTO", registrationDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registrationDTO", bindingResult);

            return "redirect:/profile/change-password";
        }

        this.userService.changePasswordByUserId(user.getId(), registrationDTO.getPassword());
        redirectAttributes.addFlashAttribute("success", Notifications.PASSWORD_CHANGED_SUCCESSFULLY.getValue());

        if (user.isCashier()) {
            return "redirect:/cashier";
        }

        return "redirect:/profile";
    }
}
