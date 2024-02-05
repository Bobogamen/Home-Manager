package com.home_manager.web;

import com.home_manager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final HttpServletRequest request;


    public AdminController(UserService userService, HttpServletRequest request) {
        this.userService = userService;
        this.request = request;
    }

    @GetMapping("")
    public ModelAndView admin() {

        if (this.request.isUserInRole("ADMIN")) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("admin/admin");
            modelAndView.addObject("allUser", this.userService.getAllUsers());

            return modelAndView;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
