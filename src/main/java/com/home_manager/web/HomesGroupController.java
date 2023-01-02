package com.home_manager.web;

import com.home_manager.model.dto.AddHomeDTO;
import com.home_manager.model.dto.AddHomesGroupDTO;
import com.home_manager.model.entities.Home;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.entities.Resident;
import com.home_manager.model.enums.HomesGroupEnum;
import com.home_manager.model.user.HomeManagerUserDetails;
import com.home_manager.service.HomeService;
import com.home_manager.service.HomesGroupService;
import com.home_manager.service.ResidentService;
import com.home_manager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(value = "/profile")
public class HomesGroupController {

    private final UserService userService;
    private final HomesGroupService homesGroupService;
    private final HomeService homeService;
    private final ResidentService residentService;

    public HomesGroupController(UserService userService, HomesGroupService homesGroupService, HomeService homeService, ResidentService residentService) {
        this.userService = userService;
        this.homesGroupService = homesGroupService;
        this.homeService = homeService;
        this.residentService = residentService;
    }

    private boolean isAuthorized(long homeGroupId, HomeManagerUserDetails user) {
        return this.userService.getUserById(user.getId()).
                getHomesGroup().stream().anyMatch(hg -> hg.getId() == homeGroupId);
    }

    @GetMapping("/homesGroup{id}")
    ModelAndView viewHomesGroup(@PathVariable long id, @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(id, user)) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("homes_group");
            modelAndView.addObject("homesGroup", this.homesGroupService.getHomesGroupById(id));

            return modelAndView;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/homesGroup{id}/add-home")
    public ModelAndView addHome(@PathVariable long id, @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(id, user)) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("add_home");
            modelAndView.addObject("homesGroup", this.homesGroupService.getHomesGroupById(id));
            return modelAndView;

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ModelAttribute("addHomeDTO")
    public AddHomeDTO addHomeDTO() {
        return new AddHomeDTO();
    }

    @PostMapping("/homesGroup{id}/add-home")
    public String addHome(@PathVariable long id, @AuthenticationPrincipal HomeManagerUserDetails user,
                          @Valid AddHomeDTO addHomeDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (isAuthorized(id, user)) {

            boolean isResident = addHomeDTO.isResident();

            if (isResident) {
                boolean floor = bindingResult.hasFieldErrors("floor");
                boolean name = bindingResult.hasFieldErrors("name");
                boolean ownerFirstName = bindingResult.hasFieldErrors("ownerFirstName");

                if (floor || name || ownerFirstName) {
                    redirectAttributes.addFlashAttribute("addHomeDTO", addHomeDTO);
                    redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addHomeDTO", bindingResult);

                    return "redirect:/profile/homesGroup{id}/add-home";
                }

            } else {
                if (bindingResult.hasErrors()) {
                    redirectAttributes.addFlashAttribute("addHomeDTO", addHomeDTO);
                    redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addHomeDTO", bindingResult);
                    redirectAttributes.addFlashAttribute("residentValue", false);

                    return "redirect:/profile/homesGroup{id}/add-home";
                }
            }

            HomesGroup homesGroup = this.homesGroupService.getHomesGroupById(id);
            Resident owner = this.residentService.addOwner(addHomeDTO);
            Home home = this.homeService.addHome(addHomeDTO, homesGroup);

            if (isResident) {
                this.homeService.setOwnerToHome(home, owner);
                this.homeService.addResidentToHome(home, owner);
                this.residentService.setResidentHome(home, owner);
            } else {
                Resident resident = this.residentService.addResident(addHomeDTO);
                this.homeService.setOwnerToHome(home, owner);
                this.homeService.addResidentToHome(home, resident);
                this.residentService.setResidentHome(home, resident);
            }

            redirectAttributes.addFlashAttribute("success", "Домът е добавен успешно");
            return "redirect:/profile/homesGroup{id}";

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/edit/homesGroup{id}")
    public ModelAndView editHomesGroup(@PathVariable long id, @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(id, user)) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("edit-homes_group");
            modelAndView.addObject("homesGroup", this.homesGroupService.getHomesGroupById(id));

            return modelAndView;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ModelAttribute("addHomesGroupDTO")
    public AddHomesGroupDTO addHomesGroupDTO() {
        return new AddHomesGroupDTO();
    }

    @ModelAttribute("allTypes")
    public List<String> allTypes() {
        return Arrays.stream(HomesGroupEnum.values()).map(HomesGroupEnum::getName).collect(Collectors.toList());
    }

    @PostMapping("/edit/homesGroup{id}")
    public String editHomesGroup(@PathVariable long id, @AuthenticationPrincipal HomeManagerUserDetails user,
                                 @Valid AddHomesGroupDTO addHomesGroupDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (isAuthorized(id, user)) {
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("addHomesGroupDTO", addHomesGroupDTO);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addHomesGroupDTO", bindingResult);

                return "redirect:/edit/homesGroups{id}";
            }

            this.homesGroupService.editHomesGroup(addHomesGroupDTO, id);

            redirectAttributes.addFlashAttribute("success", "Група " + addHomesGroupDTO.getName() + " e редактирана");
            return "redirect:/profile/homesGroup{id}";
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
