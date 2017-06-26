package com.rx.controllers;


import com.rx.dao.User;
import com.rx.dto.UserAddingResultDto;
import com.rx.dto.UserUpdatingResultDto;
import com.rx.dto.forms.AddUserFormDto;
import com.rx.dto.forms.FullUserFormDto;
import com.rx.services.UserService;
import com.rx.validators.AddUserFormDtoValidator;
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
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private UserService userService;

    private FullUserFormDtoValidator fullUserFormDtoValidator;

    private AddUserFormDtoValidator addDisciplineFormDtoValidator;

    @Autowired
    public AdminUserController(UserService userService,
                               AddUserFormDtoValidator addUserFormDtoValidator,
                               FullUserFormDtoValidator fullUserFormDtoValidator) {
        this.userService = userService;
        this.fullUserFormDtoValidator = fullUserFormDtoValidator;
        this.addDisciplineFormDtoValidator = addUserFormDtoValidator;
    }

    @InitBinder("fullUserFormDto")
    public void fullUserFormDtoInitBinder(WebDataBinder binder) {
        binder.setValidator(fullUserFormDtoValidator);
    }

    @InitBinder("addUserFormDto")
    public void addUserFormDtoInitBinder(WebDataBinder binder) {
        binder.setValidator(addDisciplineFormDtoValidator);
    }

    @GetMapping(name = "/get-user/{id}", value = "/get-user/{id}")
    public ModelAndView getUser(@PathVariable("id") Long id) {

        ModelAndView modelAndView = fullUserForm();
        modelAndView.getModel().put("teacher", userService.getUserById(id));
        modelAndView.getModel().put("id", id);

        return modelAndView;
    }

    @PostMapping(name = "/get-user/{id}", value = "/get-user/{id}")
    public String updateUserFully(@PathVariable("id") Long id,
                                  @Valid FullUserFormDto fullUserFormDto,
                                  BindingResult bindingResult,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("fullUserFormDto", fullUserFormDto);
            model.addAttribute("success", false);
            model.addAttribute("teacher", userService.getUserById(id));
            model.addAttribute("id", id);

            return "admin-user";
        }

        UserUpdatingResultDto userUpdatingResultDto = userService.updateUserFully(id, fullUserFormDto);

        if (!userUpdatingResultDto.isUpdated()) {
            bindingResult.rejectValue(userUpdatingResultDto.getErrorField(), userUpdatingResultDto.getErrorMessage());
            model.addAttribute("fullUserFormDto", fullUserFormDto);
            model.addAttribute("success", false);
            model.addAttribute("teacher", userService.getUserById(id));
            model.addAttribute("id", id);

            return "admin-user";
        }

        model.addAttribute("attribute", "redirectWithRedirectPrefix");
        model.addAttribute("success", true);
        model.addAttribute("teacher", userService.getUserById(id));
        model.addAttribute("id", id);

        return "admin-user";
    }

    @GetMapping(name = "/add-user", value = "/add-user")
    public ModelAndView getAddingUserForm() {
        return addUserForm();
    }

    @PostMapping(name = "/add-user", value = "/add-user")
    public String addUser(
                          @Valid AddUserFormDto addUserFormDto,
                          BindingResult bindingResult,
                          Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("addUserFormDto", addUserFormDto);
            return "admin-add-user";
        }

        UserAddingResultDto userAddingResultDto = userService.addUser(addUserFormDto);
        Long newUserId = userAddingResultDto.getUserId();

        if (newUserId == null) {
            bindingResult.rejectValue(userAddingResultDto.getErrorField(), userAddingResultDto.getErrorMessage());
            model.addAttribute("addUserFormDto", addUserFormDto);
            return "admin-add-user";
        } else {
            model.addAttribute("attribute", "redirectWithRedirectPrefix");
            return "redirect:/admin/get-user/" + newUserId;
        }
    }

    @GetMapping(name = "/delete-user/{id}", value = "/delete-user/{id}")
    public String deleteUser(@PathVariable("id") Long id,
                             Model model) {
        userService.deleteById(id);

        model.addAttribute("attribute", "redirectWithRedirectPrefix");
        return "redirect:/admin/users";
    }

    @GetMapping(name = "/users", value = "/users")
    public String getUsers(Model model) {

        Iterable<User> users = userService.getAllUsers();

        model.addAttribute("users", users);
        return "admin-users";
    }

    private ModelAndView fullUserForm() {
        return new ModelAndView("admin-user", "fullUserFormDto", new FullUserFormDto());
    }

    private ModelAndView addUserForm() {
        return new ModelAndView("admin-add-user", "addUserFormDto", new AddUserFormDto());
    }
}
