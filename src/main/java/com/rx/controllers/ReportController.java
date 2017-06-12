package com.rx.controllers;


import com.rx.services.DisciplineService;
import com.rx.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/report")
public class ReportController {

    private UserService userService;
    private DisciplineService service;

    @Autowired
    public ReportController(UserService userService, DisciplineService service) {
        this.userService = userService;
        this.service = service;
    }

    @GetMapping
    public String getReport(@RequestParam("userId") Long id,
                            Model model) {

        model.addAttribute("curriculumStates", service.getCurriculumsStateOfAllDisciplines());
        model.addAttribute("user", userService.getUserById(id));
        return "report";
    }
}
