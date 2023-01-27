package com.home_manager.web;

import com.home_manager.model.user.HomeManagerUserDetails;
import com.home_manager.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cashier")

public class CashierController {

    private final UserService userService;

    public CashierController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ModelAndView cashier(@AuthenticationPrincipal HomeManagerUserDetails user) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cashier");

        modelAndView.addObject("cashiers", this.userService.getUserById(user.getId()).getCashiers());

        return modelAndView;
    }
}
