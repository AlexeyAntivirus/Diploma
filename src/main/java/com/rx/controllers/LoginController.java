package com.rx.controllers;


import com.rx.dao.User;
import com.rx.dto.forms.LoginFormDto;
import com.rx.services.UserService;
import com.rx.validators.LoginFormDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    private UserService userService;

    private LoginFormDtoValidator validator;

    @Autowired
    public LoginController(UserService userService, LoginFormDtoValidator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @InitBinder("loginFormDto")
    public void loginFormDtoInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(validator);
    }

    @GetMapping
    public ModelAndView getLoginFormDto() {
        ModelAndView modelAndView = loginFormDto();
        modelAndView.getModel().put("authorizationError", "user.login.greetings");
        return modelAndView;
    }

    @PostMapping
    public String login(@Valid LoginFormDto loginFormDto,
                        BindingResult bindingResult,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authorizationError", "user.login.errors");
            return "login";
        }
        User user = userService.getUserByLoginAndPassword(loginFormDto.getLogin(), loginFormDto.getPassword());

        if (user == null) {
            model.addAttribute("authorizationError", "user.login.not-authorize");
            return "login";
        } else {
            return "redirect:/index?userId=" + user.getId();
        }
    }

    private ModelAndView loginFormDto() {
        return new ModelAndView("login", "loginFormDto", new LoginFormDto());
    }
}
