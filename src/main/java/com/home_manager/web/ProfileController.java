package com.home_manager.web;

import com.home_manager.model.dto.AddHomesGroupDTO;
import com.home_manager.model.dto.RegistrationDTO;
import com.home_manager.model.user.HomeManagerUserDetails;
import com.home_manager.service.EmailService;
import com.home_manager.service.FeeService;
import com.home_manager.service.HomesGroupService;
import com.home_manager.service.UserService;
import com.home_manager.utility.MailUtility;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final HomesGroupService homesGroupService;
    private final HttpServletRequest request;
    private final EmailService emailService;

    public ProfileController(UserService userService, HomesGroupService homesGroupService, FeeService feeService, HttpServletRequest request, EmailService emailService) {
        this.userService = userService;
        this.homesGroupService = homesGroupService;
        this.request = request;
        this.emailService = emailService;
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
            modelAndView.setViewName("add-homes_group");
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
    public String register(@AuthenticationPrincipal HomeManagerUserDetails user,  @Valid RegistrationDTO registrationDTO, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (this.request.isUserInRole("MANAGER")) {
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("registrationDTO", registrationDTO);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registrationDTO", bindingResult);

                return "redirect:/profile/register-cashier";
            }

            this.userService.registerCashier(registrationDTO, user.getId());
            this.emailService.sendCashierRegistrationEmail(registrationDTO.getEmail(), user, MailUtility.appUrl(request));

            redirectAttributes.addFlashAttribute("success", "Успешна регистрация на касиер!");
        }

        return "redirect:/cashier";
    }

    @GetMapping("/assign")
    public ModelAndView assign() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("assign");

        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView edit(@AuthenticationPrincipal HomeManagerUserDetails user) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user",this.userService.getUserById(user.getId()));
        modelAndView.setViewName("edit_profile");

        return modelAndView;
    }

    @PostMapping("/edit-name")
    public String editName(@AuthenticationPrincipal HomeManagerUserDetails user, String name, RedirectAttributes redirectAttributes) {

        this.userService.editUserName(user.getId(), name);
        user.setName(name);

        redirectAttributes.addFlashAttribute("success", "Успешна промяна!");

        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid RegistrationDTO registrationDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                 @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (bindingResult.hasFieldErrors("password") || bindingResult.hasFieldErrors("confirmPassword")) {
            redirectAttributes.addFlashAttribute("registrationDTO", registrationDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registrationDTO", bindingResult);

            return "redirect:/profile/edit";
        }

        this.userService.changePasswordByUserId(user.getId(), registrationDTO.getPassword());
        redirectAttributes.addFlashAttribute("success", "Успешна промяна!");
        return "redirect:/profile";
    }
}
