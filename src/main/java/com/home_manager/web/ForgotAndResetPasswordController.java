package com.home_manager.web;

import com.home_manager.model.dto.PasswordResetDTO;
import com.home_manager.model.enums.Notifications;
import com.home_manager.service.EmailService;
import com.home_manager.service.UserService;
import com.home_manager.utility.MailUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class ForgotAndResetPasswordController {

    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    public ForgotAndResetPasswordController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(HttpServletRequest request, String email, RedirectAttributes redirectAttributes) {
        boolean found = this.userService.findEmail(email);

        if (found) {
            this.userService.updateResetPasswordToken(MailUtility.getToken(), email);

            try {
                this.emailService.sendRecoveryPasswordEmail(email, request);
                System.out.printf("%s request a password reset%n", email);
            } catch (Exception exception) {
                System.out.println("Fail to sent password reset email");
                throw new RuntimeException(exception.getMessage());
            }
        }

        redirectAttributes.addFlashAttribute("foundEmail", found);
        redirectAttributes.addFlashAttribute("email", email);
        redirectAttributes.addFlashAttribute("show", true);

        return "redirect:/forgot-password";
    }


    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "/reset_password";
    }

    @ModelAttribute("passwordResetDTO")
    public PasswordResetDTO passwordResetDTO() {
        return new PasswordResetDTO();
    }

    @PostMapping("/reset-password")
    // !!! BindingResult MUST BE IMMEDIATELY AFTER @Valid ELEMENT OR DOESN'T WORK !!! --------------------
    public String resetPassword(@RequestParam("token") String token,
                                @Valid PasswordResetDTO passwordResetDTO,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        boolean foundEmailByToken = this.userService.findEmailByToken(token);

        if (!foundEmailByToken) {
            redirectAttributes.addFlashAttribute("fail", Notifications.INVALID_EMAIL.getValue());
            return "redirect:/";

        } else if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("passwordResetDTO", passwordResetDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordResetDTO", bindingResult);
            return "redirect:/reset-password?token=" + token;
        }

        String password = passwordResetDTO.getPassword();
        this.userService.changePasswordOnEmailByToken(token, password);

        redirectAttributes.addFlashAttribute("success", Notifications.PASSWORD_CHANGED_SUCCESSFULLY.getValue());
        return "redirect:/";
    }
}
