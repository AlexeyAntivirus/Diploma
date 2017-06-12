package com.rx.controllers;

import com.rx.dto.UserUpdatingResultDto;
import com.rx.dto.forms.UserFormDto;
import com.rx.services.DocumentStorageService;
import com.rx.services.UserService;
import com.rx.validators.UserFormDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserFormDtoValidator userFormDtoValidator;
    private DocumentStorageService documentStorageService;
    private UserService userService;

    @Autowired
    public UserController(UserService userService,
                          DocumentStorageService documentStorageService,
                          UserFormDtoValidator userFormDtoValidator) {
        this.userService = userService;
        this.userFormDtoValidator = userFormDtoValidator;
        this.documentStorageService = documentStorageService;
    }

    @InitBinder("userFormDto")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(userFormDtoValidator);
    }

    @GetMapping(name = "/profile", value = "/profile")
    public ModelAndView getUserProfile(@RequestParam("userId") Long id) {

        ModelAndView modelAndView = userForm();
        modelAndView.getModel().put("user", userService.getUserById(id));
        modelAndView.getModel().put("id", id);

        return modelAndView;
    }

    @PostMapping(name = "/profile", value = "/profile")
    public String updateUser(@RequestParam("userId") Long userId,
                             @Valid UserFormDto userFormDto,
                             BindingResult bindingResult,
                             Model model) {

        model.addAttribute("attribute", "redirectWithRedirectPrefix");
        if (bindingResult.hasErrors()) {
            model.addAttribute("userFormDto", userFormDto);
            model.addAttribute("id", userId);
            model.addAttribute("user", userService.getUserById(userId));
            return "user";
        }

        UserUpdatingResultDto userUpdatingResultDto = userService.updateUser(userId, userFormDto);
        String errorMessage = userUpdatingResultDto.getErrorMessage();

        if (errorMessage != null) {
            bindingResult.rejectValue(userUpdatingResultDto.getErrorField(), errorMessage);
            model.addAttribute("userFormDto", userFormDto);
            model.addAttribute("id", userId);
            model.addAttribute("user", userService.getUserById(userId));
            return "user";
        }

        return "redirect:/user/profile?userId=" + userId;
    }

    @GetMapping(name = "/syllabuses", value = "/syllabuses")
    public String getSyllabuses(@RequestParam("userId") Long userId,
                                Model model) {
        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("syllabuses", documentStorageService.getAllSyllabuses());
        return "syllabuses";
    }

    @GetMapping(name = "/acts", value = "/acts")
    public String getActs(@RequestParam("userId") Long userId,
                                Model model) {
        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("acts", documentStorageService.getAllNormativeActs());
        return "acts";
    }

    private ModelAndView userForm() {
        return new ModelAndView("user", "userFormDto", new UserFormDto());
    }
}
