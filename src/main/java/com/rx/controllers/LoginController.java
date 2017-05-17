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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserRepository repository;

    @Autowired
    public LoginController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping(name = "/profile", value = "/profile")
    public String getProfile(@RequestParam("id") Long id, Model model) {

        User user = repository.findOne(id);

        model.addAttribute("id", id);
        model.addAttribute("user", user);
        return "profile";
    }

    @Bean
    public CommandLineRunner runner(UserRepository repository) {
        return args -> {

            repository.save(User.builder()
                    .withLogin("admin")
                    .withPassword("14ph38")
                    .withLastName("admin")
                    .withFirstName("")
                    .withMiddleName("")
                    .withUserRole(UserRole.ADMINISTRATOR)
                    .build());
            repository.save(User.builder()
                    .withLogin("shvets")
                    .withPassword("shvets")
                    .withLastName("Швець")
                    .withFirstName("Наталя")
                    .withMiddleName("Василівна")
                    .withUserRole(UserRole.METHODOLOGIST)
                    .build());
        };
    }
}
