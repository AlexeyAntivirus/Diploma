package com.rx.controllers;


import com.rx.dao.Discipline;
import com.rx.dao.User;
import com.rx.dao.UserRole;
import com.rx.dto.DisciplineAddingResultDto;
import com.rx.dto.DisciplineUpdatingResultDto;
import com.rx.dto.forms.AddDisciplineFormDto;
import com.rx.dto.forms.FullDisciplineFormDto;
import com.rx.services.DisciplineService;
import com.rx.services.UserService;
import com.rx.validators.AddDisciplineFormDtoValidator;
import com.rx.validators.FullDisciplineFormDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/admin")
public class AdminDisciplineController {

    private UserService userService;

    private DisciplineService disciplineService;

    private FullDisciplineFormDtoValidator fullDisciplineFormDtoValidator;

    private AddDisciplineFormDtoValidator addDisciplineFormDtoValidator;


    @Autowired
    public AdminDisciplineController(UserService userService,
                                     DisciplineService disciplineService,
                                     FullDisciplineFormDtoValidator fullDisciplineFormDtoValidator,
                                     AddDisciplineFormDtoValidator addDisciplineFormDtoValidator) {
        this.userService = userService;
        this.disciplineService = disciplineService;
        this.fullDisciplineFormDtoValidator = fullDisciplineFormDtoValidator;
        this.addDisciplineFormDtoValidator = addDisciplineFormDtoValidator;
    }

    @InitBinder("addDisciplineFormDto")
    public void addDisciplineFormDtoInitBinder(WebDataBinder binder) {
        binder.setValidator(addDisciplineFormDtoValidator);
    }

    @InitBinder("fullDisciplineFormDto")
    public void fullDisciplineFormDtoInitBinder(WebDataBinder binder) {
        binder.setValidator(fullDisciplineFormDtoValidator);
    }


    @GetMapping(name = "/add-discipline", value = "/add-discipline")
    public ModelAndView getAddingDisciplineForm() {
        ModelAndView modelAndView = addDisciplineForm();

        return modelAndView;
    }

    @PostMapping(name = "/add-discipline", value = "/add-discipline")
    public String addDiscipline(@Valid AddDisciplineFormDto addDisciplineFormDto,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) {
            return "admin-add-discipline";
        }

        DisciplineAddingResultDto disciplineAddingResultDto = disciplineService.addDiscipline(addDisciplineFormDto);
        Long disciplineId = disciplineAddingResultDto.getDisciplineId();

        if (disciplineId == null) {
            bindingResult.rejectValue(disciplineAddingResultDto.getErrorField(), disciplineAddingResultDto.getErrorMessage());
            return "admin-add-discipline";
        }

        model.addAttribute("attribute", "redirectWithRedirectPrefix");
        return "redirect:/admin/get-discipline/" + disciplineId;
    }

    @GetMapping(name = "/get-discipline/{id}", value = "/get-discipline/{id}")
    public ModelAndView getDiscipline(@PathVariable("id") Long id) {
        ModelAndView modelAndView = fullDisciplineForm();
        modelAndView.getModel().put("id", id);

        Discipline disciplineById = disciplineService.getDisciplineById(id);

        if (disciplineById != null) {
            List<User> notTeachingThisDisciplineUsers = StreamSupport.stream(userService.getAllUsers().spliterator(), false)
                    .filter(user -> !disciplineById.getUsers().contains(user) && user.getUserRole() != UserRole.ADMINISTRATOR)
                    .collect(Collectors.toList());
            modelAndView.getModel().put("users", notTeachingThisDisciplineUsers);
        }

        modelAndView.getModel().put("discipline", disciplineById);

        return modelAndView;
    }

    @PostMapping(name = "/get-discipline/{id}", value = "/get-discipline/{id}")
    public String updateDisciplineFully(@PathVariable("id") Long id,
                                        @Valid FullDisciplineFormDto fullDisciplineFormDto,
                                        BindingResult bindingResult,
                                        Model model) {
        model.addAttribute("attribute", "redirectWithRedirectPrefix");

        if (bindingResult.hasErrors()) {
            model.addAttribute("fullDisciplineFormDto", fullDisciplineFormDto);
            model.addAttribute("success", false);
            return "redirect:/admin/get-discipline/" + id;
        }

        DisciplineUpdatingResultDto disciplineUpdatingResultDto = disciplineService.updateDiscipline(id, fullDisciplineFormDto);

        if (!disciplineUpdatingResultDto.isUpdated()) {
            bindingResult.rejectValue(disciplineUpdatingResultDto.getErrorField(),
                    disciplineUpdatingResultDto.getErrorMessage());
            model.addAttribute("fullDisciplineFormDto", fullDisciplineFormDto);
            model.addAttribute("success", false);
            return "redirect:/admin/get-discipline/" + id;
        }

        model.addAttribute("success", true);

        return "redirect:/admin/get-discipline/" + id;
    }

    @GetMapping(name = "/delete-discipline/{id}", value = "/delete-discipline/{id}")
    public String deleteDiscipline(@PathVariable("id") Long id,
                                   Model model) {
        disciplineService.deleteById(id);
        model.addAttribute("attribute", "redirectWithRedirectPrefix");

        return "redirect:/admin/disciplines";
    }

    @GetMapping(name = "/detach", value = "/detach")
    public String detachUserFromDiscipline(@RequestParam("disciplineId") Long disciplineId,
                                           @RequestParam("teacherId") Long teacherId,
                                           Model model) {
        disciplineService.detachUserFromDiscipline(disciplineId, teacherId);
        model.addAttribute("attribute", "redirectWithRedirectPrefix");

        return "redirect:/admin/get-discipline/" + disciplineId;
    }

    @GetMapping(name = "/disciplines", value = "/disciplines")
    public String getAllDisciplines(Model model) {
        Iterable<Discipline> disciplines = disciplineService.getAllDisciplines();

        model.addAttribute("disciplines", disciplines);
        return "admin-disciplines";
    }

    private ModelAndView fullDisciplineForm() {
        return new ModelAndView("admin-discipline", "fullDisciplineFormDto", new FullDisciplineFormDto());
    }

    private ModelAndView addDisciplineForm() {
        return new ModelAndView("admin-add-discipline", "addDisciplineFormDto", new AddDisciplineFormDto());
    }
}
