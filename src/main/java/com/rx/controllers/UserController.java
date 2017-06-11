package com.rx.controllers;

import com.rx.dao.User;
import com.rx.dto.UserUpdatingResultDto;
import com.rx.dto.forms.FullUserFormDto;
import com.rx.dto.forms.UserFormDto;
import com.rx.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(name = "/profile", value = "/profile")
    public ModelAndView getUserProfile(@RequestParam("userId") Long id) {

        ModelAndView modelAndView = userForm();
        modelAndView.getModel().put("user", userService.getUserById(id));
        modelAndView.getModel().put("page", "profile");
        modelAndView.getModel().put("id", id);

        return modelAndView;
    }

    @PostMapping(name = "/profile", value = "/profile")
    public String updateUser(@RequestParam("userId") Long userId,
                             @Valid UserFormDto userFormDto,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("userFormDto", userFormDto);
        }

        UserUpdatingResultDto userUpdatingResultDto = userService.updateUser(userId, userFormDto);
        String errorMessage = userUpdatingResultDto.getErrorMessage();

        if (errorMessage != null) {
            bindingResult.rejectValue(userUpdatingResultDto.getErrorField(), errorMessage);
            model.addAttribute("userFormDto", userFormDto);
        }

        model.addAttribute("user", userUpdatingResultDto.getUpdatedUser());
        model.addAttribute("attribute", "redirectWithRedirectPrefix");
        return "redirect:/user/profile?userId=" + userId;
    }

    private ModelAndView userForm() {
        return new ModelAndView("index", "userFormDto", new FullUserFormDto());
    }
}
