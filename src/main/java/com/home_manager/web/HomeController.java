package com.home_manager.web;

import com.home_manager.model.dto.AddResidentDTO;
import com.home_manager.model.entities.Home;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.enums.Notifications;
import com.home_manager.model.user.HomeManagerUserDetails;
import com.home_manager.service.HomeService;
import com.home_manager.service.HomesGroupService;
import com.home_manager.service.ResidentService;
import com.home_manager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/profile/homesGroup{homesGroupId}/home{homeId}")
public class HomeController {

    private final UserService userService;
    private final HomesGroupService homesGroupService;
    private final HomeService homeService;
    private final ResidentService residentService;

    public HomeController(UserService userService, HomesGroupService homesGroupService, HomeService homeService, ResidentService residentService) {
        this.userService = userService;
        this.homesGroupService = homesGroupService;
        this.homeService = homeService;
        this.residentService = residentService;
    }

    private boolean isAuthorized(long homeGroupId, long userId) {
        return this.userService.isOwner(homeGroupId, userId);
    }

    @GetMapping("")
    public ModelAndView home(@PathVariable long homesGroupId, @PathVariable long homeId, @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {
            return getModelAndView(homesGroupId, homeId, "current");
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/next")
    public ModelAndView nextHome(@PathVariable long homesGroupId, @PathVariable long homeId, @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {
            return getModelAndView(homesGroupId, homeId, "next");
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/previous")
    public ModelAndView previousHome(@PathVariable long homesGroupId, @PathVariable long homeId, @AuthenticationPrincipal HomeManagerUserDetails user) {
        if (isAuthorized(homesGroupId, user.getId())) {
            return getModelAndView(homesGroupId, homeId, "previous");
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private ModelAndView getModelAndView(long homesGroupId, long homeId, String state) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("manager/home");

        HomesGroup homesGroup = this.homesGroupService.getHomesGroupById(homesGroupId);
        modelAndView.addObject("homesGroup", homesGroup);

        if (state.equals("current")) {
            modelAndView.addObject("home", this.homeService.getHomeById(homeId));

            return modelAndView;
        } else {
            if (state.equals("next")) {
                modelAndView.addObject("home", nextPreviousHomeFinder(homesGroup.getHomes(), homeId, true));
            } else if (state.equals("previous")) {
                modelAndView.addObject("home", nextPreviousHomeFinder(homesGroup.getHomes(), homeId, false));
            }
        }

        return modelAndView;
    }

    private Home nextPreviousHomeFinder(List<Home> homes, long currentHomeId, boolean next) {
        int nextIndex = 0;

        for (int i = 0; i < homes.size(); i++) {
            if (homes.get(i).getId() == currentHomeId) {
                nextIndex = i;
                break;
            }
        }

        if (next) {
            return nextIndex == homes.size() - 1 ? homes.get(0) : homes.get(nextIndex + 1);
        } else {
            return nextIndex == 0 ? homes.get(homes.size() - 1) : homes.get(nextIndex - 1);
        }
    }

    @PostMapping("/add-resident")
    public String addResident(@PathVariable long homesGroupId,
                              @PathVariable long homeId,
                              @AuthenticationPrincipal HomeManagerUserDetails user,
                              AddResidentDTO addResidentDTO, RedirectAttributes redirectAttributes) {

        if (isAuthorized(homesGroupId, user.getId())) {

            this.residentService.addResident(addResidentDTO, this.homeService.getHomeById(homeId));
            redirectAttributes.addFlashAttribute("success", Notifications.RESIDENT_ADDED_SUCCESSFULLY.getValue());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return "redirect:/profile/homesGroup{homesGroupId}";
    }

    @GetMapping("/edit-owner{residentId}")
    public ModelAndView editOwner(@PathVariable long homesGroupId, @PathVariable long homeId,
                                  @PathVariable long residentId, @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("manager/edit_owner");
            modelAndView.addObject("owner", this.residentService.getResidentById(residentId));
            modelAndView.addObject("home", this.homeService.getHomeById(homeId));

            return modelAndView;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/edit-resident{residentId}")
    public String editResident(@PathVariable long homesGroupId, @PathVariable long residentId, @PathVariable long homeId,
                               @RequestParam(name = "resident", required = false) String resident, @AuthenticationPrincipal HomeManagerUserDetails user,
                               AddResidentDTO addResidentDTO, RedirectAttributes redirectAttributes) {

        if (isAuthorized(homesGroupId, user.getId())) {
            Home home = this.homeService.getHomeById(homeId);

            this.residentService.editResident(addResidentDTO, residentId, home, resident != null || home.getOwner().getId() != residentId || home.getResidents().size() <= 1);

            redirectAttributes.addFlashAttribute("success", Notifications.UPDATED_SUCCESSFULLY.getValue());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return "redirect:/profile/homesGroup{homesGroupId}/home{homeId}";
    }

    @GetMapping("/delete-resident{residentId}")
    public ModelAndView deleteResident(@PathVariable long homesGroupId, @PathVariable long homeId, @PathVariable long residentId,
                                       @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("manager/delete_resident");
            modelAndView.addObject("resident", this.residentService.getResidentById(residentId));

            return modelAndView;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/delete-resident{residentId}")
    public String deleteResident(@PathVariable long homesGroupId, @PathVariable long homeId, @PathVariable long residentId,
                                 @AuthenticationPrincipal HomeManagerUserDetails user, RedirectAttributes redirectAttributes) {

        if (isAuthorized(homesGroupId, user.getId())) {

            this.residentService.deleteResidentById(residentId);

            redirectAttributes.addFlashAttribute("fail", Notifications.RESIDENT_DELETED_SUCCESSFULLY.getValue());

            return "redirect:/profile/homesGroup{homesGroupId}/home{homeId}";

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

    }

    @PutMapping("/edit-fee-times{feeId}")
    public String editFee(@PathVariable long homesGroupId, @PathVariable long homeId, @PathVariable long feeId,
                          @RequestParam("times") int times, @AuthenticationPrincipal HomeManagerUserDetails user, RedirectAttributes redirectAttributes) {
        if (isAuthorized(homesGroupId, user.getId())) {
            this.homeService.changeTimesOfFeeById(homeId, feeId, times);

            redirectAttributes.addFlashAttribute("success", Notifications.UPDATED_SUCCESSFULLY.getValue());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return "redirect:/profile/homesGroup{homesGroupId}/home{homeId}";
    }
}
