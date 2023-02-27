package com.home_manager.web;

import com.home_manager.model.dto.RegistrationDTO;
import com.home_manager.model.enums.Notifications;
import com.home_manager.service.EmailService;
import com.home_manager.service.UserService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.validation.Valid;

@Controller
public class LoginRegisterController {

    private final UserService userService;
    private final EmailService emailService;

    public LoginRegisterController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    //-------------------- LOGIN FAIL SECTION START --------------------
    @PostMapping("/login-fail")
    public String loginFail(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String userName,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, userName);
        redirectAttributes.addFlashAttribute("fail", Notifications.INVALID_LOGIN.getValue());

        return "redirect:/";
    }
    //-------------------- LOGIN FAIL SECTION END --------------------

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @ModelAttribute("registrationDTO")
    public RegistrationDTO registrationDTO() {
        return new RegistrationDTO();
    }

    @PostMapping("/register")
    public String register(@Valid RegistrationDTO registrationDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws ServletException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registrationDTO", registrationDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registrationDTO", bindingResult);

            return "redirect:/register";
        }

        this.userService.register(registrationDTO);
        this.emailService.sendRegistrationEmail(registrationDTO.getEmail());

        redirectAttributes.addFlashAttribute("success", Notifications.REGISTRATION_SUCCESSFULLY.getValue());

        return "redirect:/profile";
    }
}
