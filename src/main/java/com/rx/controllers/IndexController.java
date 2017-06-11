package com.rx.controllers;


import com.rx.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/", "/index"})
public class IndexController {


    private UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUserDashboard(@RequestParam("userId") Long id,
                                   Model model) {

        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("page", "dashboard");
        model.addAttribute("id", id);

        return "index";
    }
}
