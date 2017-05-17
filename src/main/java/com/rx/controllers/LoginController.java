package com.rx.controllers;

import com.rx.dao.User;
import com.rx.dao.UserRole;
import com.rx.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserRepository repository;

    @Autowired
    public LoginController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping(name = "/profile", value = "/profile/{id}")
    public String getProfile(@PathVariable("id") Long id, Model model) {

        User user = repository.findOne(id);

        model.addAttribute("id", id);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping(name = "/profiles", value = "/profiles")
    public String getProfiles(Model model) {

        Iterable<User> users = repository.findAll();

        model.addAttribute("users", users);
        return "profiles";
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
}
