package com.rx.controllers;

import com.rx.dao.Discipline;
import com.rx.services.DisciplineService;
import com.rx.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/discipline")
public class DisciplineController {

    private UserService userService;
    private DisciplineService disciplineService;

    @Autowired
    public DisciplineController(DisciplineService disciplineService, UserService userService) {
        this.disciplineService = disciplineService;
        this.userService = userService;
    }

    @GetMapping(name = "/{id}", value = "/{id}")
    public String getDiscipline(@PathVariable("id") Long id,
                                      @RequestParam("userId") Long userId,
                                      Model model) {
        model.addAttribute("id", id);
        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("discipline", disciplineService.getDisciplineById(id));

        return "discipline";
    }

    @GetMapping(name = "disciplines", value = "disciplines")
    public String getDisciplines(Model model,
                                 @RequestParam("userId") Long userId) {
        Iterable<Discipline> disciplines = disciplineService.getTeacherDisciplines(userId);

        model.addAttribute("disciplines", disciplines);
        model.addAttribute("user", userService.getUserById(userId));
        return "disciplines";
    }

}
