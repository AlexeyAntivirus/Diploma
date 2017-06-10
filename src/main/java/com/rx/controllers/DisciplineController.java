package com.rx.controllers;

import com.rx.dao.Discipline;
import com.rx.dto.forms.AddDisciplineFormDto;
import com.rx.services.DisciplineService;
import com.rx.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView getDiscipline(@PathVariable("id") Long id,
                                      @RequestParam("userId") Long userId) {
        ModelAndView modelAndView = disciplineForm();
        modelAndView.getModel().put("id", id);
        modelAndView.getModel().put("user", userService.getUserById(userId));
        modelAndView.getModel().put("discipline", disciplineService.getDisciplineById(id));
        modelAndView.getModel().put("page", "discipline");

        return modelAndView;
    }

    @GetMapping(name = "disciplines", value = "disciplines")
    public String getDisciplines(Model model,
                                 @RequestParam("userId") Long userId) {
        Iterable<Discipline> disciplines = disciplineService.getTeacherDisciplines(userId);

        model.addAttribute("disciplines", disciplines);
        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("page", "disciplines");
        return "main";
    }

    private ModelAndView disciplineForm() {
        return new ModelAndView("main", "disciplineFormDto", new AddDisciplineFormDto());
    }


}
