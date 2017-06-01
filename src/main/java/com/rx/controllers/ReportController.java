package com.rx.controllers;


import com.rx.services.DisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/report")
public class ReportController {

    private DisciplineService service;

    @Autowired
    public ReportController(DisciplineService service) {
        this.service = service;
    }

    @GetMapping
    public String getReport(Model model) {

        model.addAttribute("state", service.getCurriculumsStateOfAllDisciplines());
        return "report";
    }
}
