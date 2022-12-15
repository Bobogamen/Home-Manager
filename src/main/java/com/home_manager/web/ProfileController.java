package com.home_manager.web;

import com.home_manager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserService userService;


    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    private ModelAndView profile(RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");

        redirectAttributes.addFlashAttribute("success", "TESTING success attribute");


        return modelAndView;
    }
}
