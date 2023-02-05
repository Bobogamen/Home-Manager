package com.home_manager.web;

import com.home_manager.model.entities.Home;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.user.HomeManagerUserDetails;
import com.home_manager.service.HomesGroupService;
import com.home_manager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.text.DecimalFormat;

@Controller
@RequestMapping("/cashier")

public class CashierController {

    private final UserService userService;
    private final HomesGroupService homesGroupService;

    public CashierController(UserService userService, HomesGroupService homesGroupService) {
        this.userService = userService;
        this.homesGroupService = homesGroupService;
    }

    private boolean isAuthorized(long homesGroupId, long userId) {
        return this.userService.isOwner(homesGroupId, userId);
    }

    @GetMapping("")
    public ModelAndView cashier(@AuthenticationPrincipal HomeManagerUserDetails user) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cashier");


        modelAndView.addObject("user", this.userService.getUserById(user.getId()));


        return modelAndView;
    }

    @GetMapping("/homesGroup{homesGroupId}")
    public ModelAndView cashierHomesGroup(@PathVariable long homesGroupId, @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("cashier/cashier_homes_group");

            HomesGroup homesGroup = this.homesGroupService.getHomesGroupById(homesGroupId);
            modelAndView.addObject("homesGroup", homesGroup);


            double total = homesGroup.getHomes().stream().mapToDouble(Home::getTotalForMonth).sum();
            modelAndView.addObject("total", new DecimalFormat("#.##").format(total));

            return modelAndView;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
