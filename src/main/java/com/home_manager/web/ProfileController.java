package com.home_manager.web;

import com.home_manager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileController {

    private UserService userService;


    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    private ModelAndView profile() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");

        return modelAndView;
    }
}
