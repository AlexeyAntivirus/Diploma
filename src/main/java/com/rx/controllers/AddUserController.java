package com.rx.controllers;

import com.rx.dao.User;
import com.rx.dao.UserRole;
import com.rx.dao.repositories.UserRepository;
import com.rx.dto.AddingUserFormDto;
import com.rx.validators.AddingUserFormDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AddUserController {

    private UserRepository repository;
    private AddingUserFormDtoValidator validator;

    @Autowired
    public AddUserController(UserRepository repository, AddingUserFormDtoValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @InitBinder("addingUserFormDto")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping(name = "/add-user", value = "/add-user")
    public ModelAndView getAddingUserForm() {
        return addingUserForm();
    }

    @PostMapping(name = "/add-user", value = "/add-user")
    public String addUser(@Valid AddingUserFormDto addingUserFormDto,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-user";
        } else {
            User user = User.builder()
                    .withLogin(addingUserFormDto.getLogin())
                    .withEmail(addingUserFormDto.getEmail())
                    .withPassword(addingUserFormDto.getPassword())
                    .withLastName(addingUserFormDto.getLastName())
                    .withFirstName(addingUserFormDto.getFirstName())
                    .withMiddleName(addingUserFormDto.getMiddleName())
                    .withUserRole(addingUserFormDto.getUserRole())
                    .build();

            Long id = repository.save(user).getId();
            return "redirect:/profile/" + id;
        }
    }


    @Bean
    public CommandLineRunner runner(UserRepository repository) {
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

    private ModelAndView addingUserForm() {
        return new ModelAndView("add-user", "addingUserFormDto", new AddingUserFormDto());
    }
}
