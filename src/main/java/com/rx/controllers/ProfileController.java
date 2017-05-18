package com.rx.controllers;

import com.rx.dao.User;
import com.rx.dao.UserRole;
import com.rx.dao.repositories.UserRepository;
import com.rx.dto.AddingUserFormDto;
import com.rx.dto.UpdatingUserFormDto;
import com.rx.validators.AddingUserFormDtoValidator;
import com.rx.validators.UpdatingUserFormDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class ProfileController {

    private UserRepository repository;
    private UpdatingUserFormDtoValidator validator;

    @Autowired
    public ProfileController(UserRepository repository, UpdatingUserFormDtoValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @InitBinder("updatingUserFormDto")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping(name = "/profile", value = "/profile/{id}")
    public ModelAndView getProfile(@PathVariable("id") Long id) {

        ModelAndView modelAndView = profileForm();
        modelAndView.getModel().put("user", repository.findOne(id));
        modelAndView.getModel().put("id", id);

        return modelAndView;
    }

    @PostMapping(name = "/profile", value = "/profile/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @Valid UpdatingUserFormDto updatingUserFormDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/profile/" + id;
        } else {
            User user = repository.findOne(id);

            user.setEmail(updatingUserFormDto.getEmail());
            user.setPassword(updatingUserFormDto.getPassword());
            user.setLastName(updatingUserFormDto.getLastName());
            user.setFirstName(updatingUserFormDto.getFirstName());
            user.setMiddleName(updatingUserFormDto.getMiddleName());
            Long newId = repository.save(user).getId();
            return "redirect:/profile/" + newId;
        }
    }

    @GetMapping(name = "/profiles", value = "/profiles")
    public String getProfiles(Model model) {

        Iterable<User> users = repository.findAll();

        model.addAttribute("users", users);
        return "profiles";
    }

    private ModelAndView profileForm() {
        return new ModelAndView("profile", "updatingUserFormDto", new UpdatingUserFormDto());
    }
}
