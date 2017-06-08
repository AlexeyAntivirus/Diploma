package com.rx.controllers;


import com.rx.dao.Discipline;
import com.rx.dao.User;
import com.rx.dao.UserRole;
import com.rx.dto.DisciplineUpdatingResultDto;
import com.rx.dto.UserAddingResultDto;
import com.rx.dto.UserUpdatingResultDto;
import com.rx.dto.forms.AddDisciplineFormDto;
import com.rx.dto.forms.FullDisciplineFormDto;
import com.rx.dto.forms.FullUserFormDto;
import com.rx.services.DisciplineService;
import com.rx.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    private DisciplineService disciplineService;

    @Autowired
    public AdminController(UserService userService, DisciplineService disciplineService) {
        this.userService = userService;
        this.disciplineService = disciplineService;
    }

    @GetMapping(name = "/get-user/{id}", value = "/get-user/{id}")
    public ModelAndView getUser(@PathVariable("id") Long id) {

        ModelAndView modelAndView = fullUserForm();
        modelAndView.getModel().put("user", userService.getUserById(id));
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
        }

        UserUpdatingResultDto userUpdatingResultDto = userService.updateUserFully(id, fullUserFormDto);
        String errorMessage = userUpdatingResultDto.getErrorMessage();

        if (errorMessage != null) {
            bindingResult.rejectValue(userUpdatingResultDto.getErrorField(), errorMessage);
            model.addAttribute("fullUserFormDto", fullUserFormDto);
        }

        model.addAttribute("user", userUpdatingResultDto.getUpdatedUser());
        return "user";
    }

    @GetMapping(name = "/add-user", value = "/add-user")
    public ModelAndView getAddingUserForm() {
        return addUserForm();
    }

    @PostMapping(name = "/add-user", value = "/add-user")
    public String addUser(@Valid FullUserFormDto fullUserFormDto,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userFormDto", fullUserFormDto);
            return "add-user";
        }

        UserAddingResultDto userAddingResultDto = userService.addUser(fullUserFormDto);
        String errorMessage = userAddingResultDto.getErrorMessage();

        if (errorMessage != null) {
            bindingResult.rejectValue(userAddingResultDto.getErrorField(), errorMessage);
            model.addAttribute("fullUserFormDto", fullUserFormDto);
            return "add-user";
        } else {
            model.addAttribute("resultText", "user.add.message");
            model.addAttribute("linkText", "user.add.link.text");
            model.addAttribute("link", "/user/get/" + userAddingResultDto.getUserId());
            return "add-result";
        }
    }

    @GetMapping(name = "/delete-user/{id}", value = "/delete-user/{id}")
    public String deleteUser(@PathVariable("id") Long id,
                             Model model) {
        userService.deleteById(id);
        model.addAttribute("resultText", "user.delete.message");
        return "admin/delete-result";
    }

    @GetMapping(name = "/add-discipline", value = "/add-discipline")
    public ModelAndView getAddingDisciplineForm() {
        return addDisciplineForm();
    }

    @PostMapping(name = "/add-discipline", value = "/add-discipline")
    public String addDiscipline(@Valid AddDisciplineFormDto addDisciplineFormDto,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/add-discipline";
        } else if (disciplineService.existsByName(addDisciplineFormDto.getName())) {
            bindingResult.rejectValue("name", "discipline.isPresent");
            return "admin/add-discipline";
        } else {
            Long id = disciplineService.addDiscipline(Discipline.builder()
                    .withName(addDisciplineFormDto.getName())
                    .build());
            model.addAttribute("resultText", "discipline.add.message");
            model.addAttribute("linkText", "discipline.add.link.text");
            model.addAttribute("link", "/admin/get-discipline/" + id);
            return "admin/add-result";
        }
    }

    @GetMapping(name = "/get-discipline/{id}", value = "/get-discipline/{id}")
    public ModelAndView getDiscipline(@PathVariable("id") Long id) {
        ModelAndView modelAndView = fullDisciplineForm();
        modelAndView.getModel().put("id", id);
        Discipline disciplineById = disciplineService.getDisciplineById(id);

        List<User> notTeachingThisDisciplineUsers = StreamSupport.stream(userService.getAllUsers().spliterator(), false)
                .filter(user -> !disciplineById.getUsers().contains(user) && user.getUserRole() != UserRole.ADMINISTRATOR)
                .collect(Collectors.toList());

        modelAndView.getModel().put("discipline", disciplineById);
        modelAndView.getModel().put("users", notTeachingThisDisciplineUsers);

        return modelAndView;
    }

    @PostMapping(name = "/get-discipline/{id}", value = "/get-discipline/{id}")
    public String updateDisciplineFully(@PathVariable("id") Long id,
                                        @Valid FullDisciplineFormDto fullDisciplineFormDto,
                                        BindingResult bindingResult,
                                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("fullDisciplineFormDto", fullDisciplineFormDto);
        }

        model.addAttribute("discipline", disciplineService.updateDiscipline(id, fullDisciplineFormDto));
        return "admin/discipline";
    }

    @GetMapping(name = "/delete-discipline/{id}", value = "/delete-discipline/{id}")
    public String deleteDiscipline(@PathVariable("id") Long id,
                                   Model model) {
        disciplineService.deleteById(id);
        model.addAttribute("resultText", "discipline.delete.message");
        return "admin/delete-result";
    }

    private ModelAndView fullUserForm() {
        return new ModelAndView("admin/user", "fullUserFormDto", new FullUserFormDto());
    }

    private ModelAndView addUserForm() {
        return new ModelAndView("admin/add-user", "userFormDto", new FullUserFormDto());
    }

    private ModelAndView fullDisciplineForm() {
        return new ModelAndView("admin/discipline", "fullDisciplineFormDto", new FullDisciplineFormDto());
    }

    private ModelAndView addDisciplineForm() {
        return new ModelAndView("admin/add-discipline", "addDisciplineFormDto", new AddDisciplineFormDto());
    }


}
