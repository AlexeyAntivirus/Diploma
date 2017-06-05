package com.rx.controllers;

import com.rx.dao.Discipline;
import com.rx.dto.forms.AddDisciplineFormDto;
import com.rx.services.DisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/discipline")
public class DisciplineController {

    private DisciplineService disciplineService;

    @Autowired
    public DisciplineController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }



    @GetMapping(name = "get/{id}", value = "get/{id}")
    public ModelAndView getDiscipline(@PathVariable("id") Long id) {
        ModelAndView modelAndView = disciplineForm();
        modelAndView.getModel().put("id", id);
        modelAndView.getModel().put("discipline", disciplineService.getDisciplineById(id));

        return modelAndView;
    }



    @GetMapping(name = "disciplines", value = "disciplines")
    public String getDisciplines(Model model) {
        Iterable<Discipline> disciplines = disciplineService.getAllDisciplines();

        model.addAttribute("disciplines", disciplines);
        return "disciplines";
    }

    private ModelAndView disciplineForm() {
        return new ModelAndView("discipline", "disciplineFormDto", new AddDisciplineFormDto());
    }


}
