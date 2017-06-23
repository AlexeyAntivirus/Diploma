package com.rx.controllers;

import com.rx.dao.Discipline;
import com.rx.helpers.AuthenticatedUser;
import com.rx.services.DisciplineService;
import com.rx.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/discipline")
public class DisciplineController {

    private DisciplineService disciplineService;

    @Autowired
    public DisciplineController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    @GetMapping(name = "/{id}", value = "/{id}")
    public String getDiscipline(@PathVariable("id") Long id,
                                Model model) {
        model.addAttribute("id", id);
        model.addAttribute("discipline", disciplineService.getDisciplineById(id));

        return "discipline";
    }

    @GetMapping(name = "disciplines", value = "disciplines")
    public String getDisciplines(Model model) {
        Long userId = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Iterable<Discipline> disciplines = disciplineService.getTeacherDisciplines(userId);

        model.addAttribute("disciplines", disciplines);
        return "disciplines";
    }

}
