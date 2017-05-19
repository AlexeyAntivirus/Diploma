package com.rx.controllers;

import com.rx.dao.User;
import com.rx.dto.UpdatingUserFormDto;
import com.rx.services.UserService;
import com.rx.validators.UpdatingUserFormDtoValidator;
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
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile/{id}")
public class ProfileController {

    private UserService userService;
    private UpdatingUserFormDtoValidator validator;

    @Autowired
    public ProfileController(UserService userService, UpdatingUserFormDtoValidator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @InitBinder("updatingUserFormDto")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping
    public ModelAndView getProfile(@PathVariable("id") Long id) {

        ModelAndView modelAndView = profileForm();
        modelAndView.getModel().put("user", userService.getUserById(id));
        modelAndView.getModel().put("id", id);

        return modelAndView;
    }

    @PostMapping
    public String updateUser(@PathVariable("id") Long id,
                             @Valid UpdatingUserFormDto updatingUserFormDto,
                             BindingResult bindingResult,
                             Model model) {
        User user = userService.getUserById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("updatingUserFormDto", updatingUserFormDto);
        } else if (!updatingUserFormDto.getEmail().equals(user.getEmail()) &&
                userService.existsByEmail(updatingUserFormDto.getEmail())) {
            bindingResult.rejectValue("email", "email.isBusy");
            model.addAttribute("updatingUserFormDto", updatingUserFormDto);
        } else {
            user.setEmail(updatingUserFormDto.getEmail());
            user.setPassword(updatingUserFormDto.getPassword());
            user.setLastName(updatingUserFormDto.getLastName());
            user.setFirstName(updatingUserFormDto.getFirstName());
            user.setMiddleName(updatingUserFormDto.getMiddleName());
            userService.insertOrUpdateUser(user);
        }

        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping(name = "/profiles", value = "/profiles")
    public String getProfiles(Model model) {

        Iterable<User> users = userService.getAllUsers();

        model.addAttribute("users", users);
        return "profiles";
    }

    private ModelAndView profileForm() {
        return new ModelAndView("profile", "updatingUserFormDto", new UpdatingUserFormDto());
    }
}
