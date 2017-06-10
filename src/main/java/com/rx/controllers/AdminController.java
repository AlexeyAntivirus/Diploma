package com.rx.controllers;


import com.rx.dao.Discipline;
import com.rx.dao.User;
import com.rx.dao.UserRole;
import com.rx.dto.DisciplineAddingResultDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
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
    public ModelAndView getUser(@PathVariable("id") Long id,
                                @RequestParam("userId") Long userId) {

        ModelAndView modelAndView = fullUserForm();
        modelAndView.getModel().put("teacher", userService.getUserById(id));
        modelAndView.getModel().put("userId", userId);
        modelAndView.getModel().put("user", userService.getUserById(userId));
        modelAndView.getModel().put("page", "admin-user");
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
        }

        UserUpdatingResultDto userUpdatingResultDto = userService.updateUserFully(id, fullUserFormDto);
        User user = userUpdatingResultDto.getUpdatedUser();

        if (user == null) {
            bindingResult.rejectValue(userUpdatingResultDto.getErrorField(), userUpdatingResultDto.getErrorMessage());
            model.addAttribute("fullUserFormDto", fullUserFormDto);
        }

        model.addAttribute("attribute", "redirectWithRedirectPrefix");

        return "redirect:/admin/get-user/" + id + "?userId=" + userId;
    }

    @GetMapping(name = "/add-user", value = "/add-user")
    public ModelAndView getAddingUserForm(@RequestParam("userId") Long userId) {
        ModelAndView modelAndView = addUserForm();

        modelAndView.getModel().put("user", userService.getUserById(userId));
        modelAndView.getModel().put("userId", userId);
        modelAndView.getModel().put("page", "admin-add-user");
        return modelAndView;
    }

    @PostMapping(name = "/add-user", value = "/add-user")
    public String addUser(@Valid FullUserFormDto fullUserFormDto,
                          @RequestParam("userId") Long userId,
                          BindingResult bindingResult,
                          Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("userFormDto", fullUserFormDto);
            return "main";
        }

        UserAddingResultDto userAddingResultDto = userService.addUser(fullUserFormDto);
        Long newUserId = userAddingResultDto.getUserId();

        if (newUserId == null) {
            bindingResult.rejectValue(userAddingResultDto.getErrorField(), userAddingResultDto.getErrorMessage());
            model.addAttribute("fullUserFormDto", fullUserFormDto);
            return "main";
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
        model.addAttribute("page", "admin-users");
        return "redirect:/admin/users?userId=" + userId;
    }

    @GetMapping(name = "/users", value = "/users")
    public String getUsers(Model model,
                           @RequestParam("userId") Long userId) {

        Iterable<User> users = userService.getAllUsers();

        model.addAttribute("users", users);
        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("page", "admin-users");
        return "main";
    }

    @GetMapping(name = "/add-discipline", value = "/add-discipline")
    public ModelAndView getAddingDisciplineForm(@RequestParam("userId") Long userId) {
        ModelAndView modelAndView = addDisciplineForm();
        modelAndView.getModel().put("user", userService.getUserById(userId));
        modelAndView.getModel().put("userId", userId);
        modelAndView.getModel().put("page", "admin-add-discipline");

        return modelAndView;
    }

    @PostMapping(name = "/add-discipline", value = "/add-discipline")
    public String addDiscipline(@Valid AddDisciplineFormDto addDisciplineFormDto,
                                @RequestParam("userId") Long userId,
                                BindingResult bindingResult,
                                Model model) {
        model.addAttribute("userId", userId);
        if (bindingResult.hasErrors()) {
            return "main";
        }

        DisciplineAddingResultDto disciplineAddingResultDto = disciplineService.addDiscipline(addDisciplineFormDto);
        Long disciplineId = disciplineAddingResultDto.getDisciplineId();

        if (disciplineId == null) {
            bindingResult.rejectValue(disciplineAddingResultDto.getErrorField(), disciplineAddingResultDto.getErrorMessage());
            return "main";
        } else {
            model.addAttribute("attribute", "redirectWithRedirectPrefix");
            return "redirect:/admin/get-discipline/" + disciplineId + "?userId=" + userId;
        }
    }

    @GetMapping(name = "/get-discipline/{id}", value = "/get-discipline/{id}")
    public ModelAndView getDiscipline(@PathVariable("id") Long id,
                                      @RequestParam("userId") Long userId) {
        ModelAndView modelAndView = fullDisciplineForm();
        modelAndView.getModel().put("id", id);
        modelAndView.getModel().put("user", userService.getUserById(userId));
        modelAndView.getModel().put("page", "admin-discipline");

        Discipline disciplineById = disciplineService.getDisciplineById(id);

        if (disciplineById != null) {
            List<User> notTeachingThisDisciplineUsers = StreamSupport.stream(userService.getAllUsers().spliterator(), false)
                    .filter(user -> !disciplineById.getUsers().contains(user) && user.getUserRole() != UserRole.ADMINISTRATOR)
                    .collect(Collectors.toList());
            modelAndView.getModel().put("users", notTeachingThisDisciplineUsers);
        }

        modelAndView.getModel().put("discipline", disciplineById);

        return modelAndView;
    }

    @PostMapping(name = "/get-discipline/{id}", value = "/get-discipline/{id}")
    public String updateDisciplineFully(@PathVariable("id") Long id,
                                        @RequestParam("userId") Long userId,
                                        @Valid FullDisciplineFormDto fullDisciplineFormDto,
                                        BindingResult bindingResult,
                                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("fullDisciplineFormDto", fullDisciplineFormDto);
        }

        model.addAttribute("attribute", "redirectWithRedirectPrefix");
        model.addAttribute("discipline", disciplineService.updateDiscipline(id, fullDisciplineFormDto));

        return "redirect:/admin/get-discipline/" + id + "?userId=" + userId;
    }

    @GetMapping(name = "/delete-discipline/{id}", value = "/delete-discipline/{id}")
    public String deleteDiscipline(@PathVariable("id") Long id,
                                   @RequestParam("userId") Long userId,
                                   Model model) {
        disciplineService.deleteById(id);
        model.addAttribute("attribute", "redirectWithRedirectPrefix");
        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("page", "admin-disciplines");

        return "redirect:/admin/disciplines?userId=" + userId;
    }

    @GetMapping(name = "/detach", value = "/detach")
    public String detachUserFromDiscipline(@RequestParam("userId") Long userId,
                                           @RequestParam("disciplineId") Long disciplineId,
                                           @RequestParam("teacherId") Long teacherId,
                                           Model model) {
        disciplineService.detachUserFromDiscipline(disciplineId, teacherId);
        model.addAttribute("attribute", "redirectWithRedirectPrefix");

        return "redirect:/admin/get-discipline/" + disciplineId + "?userId=" + userId;
    }

    @GetMapping(name = "/disciplines", value = "/disciplines")
    public String getAllDisciplines(Model model,
                                    @RequestParam("userId") Long userId) {
        Iterable<Discipline> disciplines = disciplineService.getAllDisciplines();

        model.addAttribute("disciplines", disciplines);
        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("page", "admin-disciplines");
        return "main";
    }

    private ModelAndView fullUserForm() {
        return new ModelAndView("main", "fullUserFormDto", new FullUserFormDto());
    }

    private ModelAndView addUserForm() {
        return new ModelAndView("main", "fullUserFormDto", new FullUserFormDto());
    }

    private ModelAndView fullDisciplineForm() {
        return new ModelAndView("main", "fullDisciplineFormDto", new FullDisciplineFormDto());
    }

    private ModelAndView addDisciplineForm() {
        return new ModelAndView("main", "addDisciplineFormDto", new AddDisciplineFormDto());
    }
}
