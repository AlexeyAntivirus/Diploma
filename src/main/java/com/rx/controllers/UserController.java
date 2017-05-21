package com.rx.controllers;

import com.rx.dao.User;
import com.rx.dao.UserRole;
import com.rx.dao.repositories.UserRepository;
import com.rx.dto.UserFormDto;
import com.rx.services.UserService;
import com.rx.validators.UserFormDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
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
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private UserFormDtoValidator validator;

    @Autowired
    public UserController(UserService userService, UserFormDtoValidator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @InitBinder("userFormDto")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping(name = "{id}", value = "{id}")
    public ModelAndView getUser(@PathVariable("id") Long id) {

        ModelAndView modelAndView = userForm();
        modelAndView.getModel().put("user", userService.getUserById(id));
        modelAndView.getModel().put("id", id);

        return modelAndView;
    }

    @PostMapping(name = "{id}", value = "{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @Valid UserFormDto userFormDto,
                             BindingResult bindingResult,
                             Model model) {
        User user = userService.getUserById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("userFormDto", userFormDto);
        } else if (!userFormDto.getEmail().equals(user.getEmail()) &&
                userService.existsByEmail(userFormDto.getEmail())) {
            bindingResult.rejectValue("email", "email.isBusy");
            model.addAttribute("userFormDto", userFormDto);
        } else {
            user.setEmail(userFormDto.getEmail());
            user.setPassword(userFormDto.getPassword());
            user.setLastName(userFormDto.getLastName());
            user.setFirstName(userFormDto.getFirstName());
            user.setMiddleName(userFormDto.getMiddleName());
            userService.insertOrUpdateUser(user);
        }

        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping(name = "{id}/admin", value = "{id}/admin")
    public ModelAndView getUserAdmin(@PathVariable("id") Long id) {

        ModelAndView modelAndView = userAdminForm();
        modelAndView.getModel().put("user", userService.getUserById(id));
        modelAndView.getModel().put("id", id);

        return modelAndView;
    }

    @PostMapping(name = "{id}/admin", value = "{id}/admin")
    public String updateUserAdmin(@PathVariable("id") Long id,
                             @Valid UserFormDto userFormDto,
                             BindingResult bindingResult,
                             Model model) {
        User user = userService.getUserById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("userFormDto", userFormDto);
        } else if (!userFormDto.getEmail().equals(user.getEmail()) &&
                userService.existsByEmail(userFormDto.getEmail())) {
            bindingResult.rejectValue("email", "email.isBusy");
            model.addAttribute("userFormDto", userFormDto);
        } else {
            user.setLogin(userFormDto.getLogin());
            user.setEmail(userFormDto.getEmail());
            user.setPassword(userFormDto.getPassword());
            user.setLastName(userFormDto.getLastName());
            user.setFirstName(userFormDto.getFirstName());
            user.setMiddleName(userFormDto.getMiddleName());
            user.setUserRole(userFormDto.getUserRole());
            userService.insertOrUpdateUser(user);
        }

        model.addAttribute("user", user);
        return "user-admin";
    }

    @GetMapping(name = "/add-user", value = "/add-user")
    public ModelAndView getAddingUserForm() {
        return addUserForm();
    }

    @PostMapping(name = "/add-user", value = "/add-user")
    public String addUser(@Valid UserFormDto userFormDto,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-user";
        } else if (userService.existsByLogin(userFormDto.getLogin())) {
            bindingResult.rejectValue("login", "user.isPresent");
            return "add-user";
        } else if (userService.existsByEmail(userFormDto.getEmail())) {
            bindingResult.rejectValue("email", "email.isBusy");
            return "add-user";
        } else {
            User user = User.builder()
                    .withLogin(userFormDto.getLogin())
                    .withEmail(userFormDto.getEmail())
                    .withPassword(userFormDto.getPassword())
                    .withLastName(userFormDto.getLastName())
                    .withFirstName(userFormDto.getFirstName())
                    .withMiddleName(userFormDto.getMiddleName())
                    .withUserRole(userFormDto.getUserRole())
                    .build();
            Long id = userService.insertOrUpdateUser(user);
            return "redirect:/user/" + id + "/admin";
        }
    }

    @GetMapping(name = "/users", value = "/users")
    public String getUsers(Model model) {

        Iterable<User> users = userService.getAllUsers();

        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping(name = "/delete-user/{id}", value = "/delete-user/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/user/" + id + "/admin";
    }

    private ModelAndView userForm() {
        return new ModelAndView("user", "userFormDto", new UserFormDto());
    }

    private ModelAndView userAdminForm() {
        return new ModelAndView("user-admin", "userFormDto", new UserFormDto());
    }

    private ModelAndView addUserForm() {
        return new ModelAndView("add-user", "userFormDto", new UserFormDto());
    }

    @Bean
    private CommandLineRunner runner(UserRepository repository) {
        return args -> {
            repository.save(User.builder()
                    .withLogin("admin")
                    .withPassword("admin")
                    .withEmail("blabla@gmail.com")
                    .withPassword("14ph38")
                    .withLastName("")
                    .withFirstName("")
                    .withMiddleName("")
                    .withUserRole(UserRole.ADMINISTRATOR)
                    .build());
            repository.save(User.builder()
                    .withLogin("nshvec60")
                    .withPassword("nshvec60")
                    .withEmail("shvetsnatalya@rambler.ru")
                    .withLastName("Швець")
                    .withFirstName("Наталя")
                    .withMiddleName("Василівна")
                    .withUserRole(UserRole.METHODOLOGIST)
                    .build());
            repository.save(User.builder()
                    .withLogin("plotnikov")
                    .withPassword("plotnikov")
                    .withEmail("plotnikov@ukr.net")
                    .withLastName("Плотников")
                    .withFirstName("Валерій")
                    .withMiddleName("Михайлович")
                    .withUserRole(UserRole.HEAD_OF_DEPARTMENT)
                    .build());
            repository.save(User.builder()
                    .withLogin("popkovdn")
                    .withPassword("popkovdn")
                    .withEmail("popkovdn@ukr.net")
                    .withLastName("Попков")
                    .withFirstName("Денис")
                    .withMiddleName("Миколайович")
                    .withUserRole(UserRole.SENIOR_LECTURER)
                    .build());
            repository.save(User.builder()
                    .withLogin("proziumod")
                    .withPassword("proziumod")
                    .withEmail("proziumod@gmail.com")
                    .withLastName("Мітрофанова")
                    .withFirstName("Наталя")
                    .withMiddleName("Федорівна")
                    .withUserRole(UserRole.ASSISTANT_LECTURER)
                    .build());
        };
    }
}
