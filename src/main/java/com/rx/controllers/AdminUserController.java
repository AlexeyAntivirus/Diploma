package com.rx.controllers;


import com.rx.dao.User;
import com.rx.dto.UserAddingResultDto;
import com.rx.dto.UserUpdatingResultDto;
import com.rx.dto.forms.FullUserFormDto;
import com.rx.services.UserService;
import com.rx.validators.FullUserFormDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private UserService userService;

    private FullUserFormDtoValidator fullUserFormDtoValidator;

    @Autowired
    public AdminUserController(UserService userService,
                               FullUserFormDtoValidator fullUserFormDtoValidator) {
        this.userService = userService;
        this.fullUserFormDtoValidator = fullUserFormDtoValidator;
    }

    @InitBinder("fullUserFormDto")
    public void fullUserFormDtoInitBinder(WebDataBinder binder) {
        binder.setValidator(fullUserFormDtoValidator);
    }

    @GetMapping(name = "/get-user/{id}", value = "/get-user/{id}")
    public ModelAndView getUser(@PathVariable("id") Long id,
                                @RequestParam("userId") Long userId) {

        ModelAndView modelAndView = fullUserForm();
        modelAndView.getModel().put("teacher", userService.getUserById(id));
        modelAndView.getModel().put("userId", userId);
        modelAndView.getModel().put("user", userService.getUserById(userId));
        modelAndView.getModel().put("id", id);

        return modelAndView;
    }

    @PostMapping(name = "/get-user/{id}", value = "/get-user/{id}")
    public String updateUserFully(@PathVariable("id") Long id,
                                  @RequestParam("userId") Long userId,
                                  @Valid FullUserFormDto fullUserFormDto,
                                  BindingResult bindingResult,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("fullUserFormDto", fullUserFormDto);
            model.addAttribute("teacher", userService.getUserById(id));
            model.addAttribute("userId", userId);
            model.addAttribute("user", userService.getUserById(userId));
            model.addAttribute("id", id);

            return "admin-user";
        }

        UserUpdatingResultDto userUpdatingResultDto = userService.updateUserFully(id, fullUserFormDto);

        if (!userUpdatingResultDto.isUpdated()) {
            bindingResult.rejectValue(userUpdatingResultDto.getErrorField(), userUpdatingResultDto.getErrorMessage());
            model.addAttribute("fullUserFormDto", fullUserFormDto);
            model.addAttribute("teacher", userService.getUserById(id));
            model.addAttribute("userId", userId);
            model.addAttribute("user", userService.getUserById(userId));
            model.addAttribute("id", id);

            return "admin-user";
        }

        model.addAttribute("attribute", "redirectWithRedirectPrefix");

        return "redirect:/admin/get-user/" + id + "?userId=" + userId;
    }

    @GetMapping(name = "/add-user", value = "/add-user")
    public ModelAndView getAddingUserForm(@RequestParam("userId") Long userId) {
        ModelAndView modelAndView = addUserForm();

        modelAndView.getModel().put("user", userService.getUserById(userId));
        modelAndView.getModel().put("userId", userId);
        return modelAndView;
    }

    @PostMapping(name = "/add-user", value = "/add-user")
    public String addUser(@RequestParam("userId") Long userId,
                          @Valid FullUserFormDto fullUserFormDto,
                          BindingResult bindingResult,
                          Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("fullUserFormDto", fullUserFormDto);
            model.addAttribute("userId", userId);
            model.addAttribute("user", userService.getUserById(userId));
            return "admin-add-user";
        }

        UserAddingResultDto userAddingResultDto = userService.addUser(fullUserFormDto);
        Long newUserId = userAddingResultDto.getUserId();

        if (newUserId == null) {
            bindingResult.rejectValue(userAddingResultDto.getErrorField(), userAddingResultDto.getErrorMessage());
            model.addAttribute("fullUserFormDto", fullUserFormDto);
            model.addAttribute("userId", userId);
            model.addAttribute("user", userService.getUserById(userId));
            return "admin-add-user";
        } else {
            model.addAttribute("attribute", "redirectWithRedirectPrefix");
            return "redirect:/admin/get-user/" + newUserId + "?userId=" + userId;
        }
    }

    @GetMapping(name = "/delete-user/{id}", value = "/delete-user/{id}")
    public String deleteUser(@PathVariable("id") Long id,
                             @RequestParam("userId") Long userId,
                             Model model) {
        userService.deleteById(id);

        model.addAttribute("attribute", "redirectWithRedirectPrefix");
        model.addAttribute("user", userService.getUserById(userId));
        return "redirect:/admin/users?userId=" + userId;
    }

    @GetMapping(name = "/users", value = "/users")
    public String getUsers(Model model,
                           @RequestParam("userId") Long userId) {

        Iterable<User> users = userService.getAllUsers();

        model.addAttribute("users", users);
        model.addAttribute("user", userService.getUserById(userId));
        return "admin-users";
    }

    private ModelAndView fullUserForm() {
        return new ModelAndView("admin-user", "fullUserFormDto", new FullUserFormDto());
    }

    private ModelAndView addUserForm() {
        return new ModelAndView("admin-add-user", "fullUserFormDto", new FullUserFormDto());
    }
}
