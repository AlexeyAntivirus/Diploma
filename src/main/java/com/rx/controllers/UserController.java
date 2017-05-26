package com.rx.controllers;

import com.rx.dao.Discipline;
import com.rx.dao.Document;
import com.rx.dao.DocumentType;
import com.rx.dao.User;
import com.rx.dao.UserRole;
import com.rx.dao.repositories.DisciplineRepository;
import com.rx.dao.repositories.UserRepository;
import com.rx.dto.forms.UserFormDto;
import com.rx.services.UserService;
import com.rx.validators.UserFormDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private UserFormDtoValidator validator;

    @Autowired
    public UserController(UserService userService, UserFormDtoValidator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @InitBinder("userFormDto")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping(name = "/get/{id}", value = "/get/{id}")
    public ModelAndView getUser(@PathVariable("id") Long id) {

        ModelAndView modelAndView = userForm();
        modelAndView.getModel().put("user", userService.getUserById(id));
        modelAndView.getModel().put("id", id);

        return modelAndView;
    }

    @PostMapping(name = "/get/{id}", value = "/get/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @Valid UserFormDto userFormDto,
                             BindingResult bindingResult,
                             Model model) {
        User user = userService.getUserById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("userFormDto", userFormDto);
        } else if (!userFormDto.getEmail().equals(user.getEmail()) &&
                userService.existsByEmail(userFormDto.getEmail())) {
            bindingResult.rejectValue("email", "email.isBusy");
            model.addAttribute("userFormDto", userFormDto);
        } else {
            user.setLogin(userFormDto.getLogin());
            user.setEmail(userFormDto.getEmail());
            user.setPassword(userFormDto.getPassword());
            user.setLastName(userFormDto.getLastName());
            user.setFirstName(userFormDto.getFirstName());
            user.setMiddleName(userFormDto.getMiddleName());
            user.setUserRole(userFormDto.getUserRole());
            userService.insertOrUpdateUser(user);
        }

        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping(name = "/add-user", value = "/add-user")
    public ModelAndView getAddingUserForm() {
        return addUserForm();
    }

    @PostMapping(name = "/add-user", value = "/add-user")
    public String addUser(@Valid UserFormDto userFormDto,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            return "add-user";
        } else if (userService.existsByLogin(userFormDto.getLogin())) {
            bindingResult.rejectValue("login", "user.isPresent");
            return "add-user";
        } else if (userService.existsByEmail(userFormDto.getEmail())) {
            bindingResult.rejectValue("email", "email.isBusy");
            return "add-user";
        } else {
            User user = User.builder()
                    .withLogin(userFormDto.getLogin())
                    .withEmail(userFormDto.getEmail())
                    .withPassword(userFormDto.getPassword())
                    .withLastName(userFormDto.getLastName())
                    .withFirstName(userFormDto.getFirstName())
                    .withMiddleName(userFormDto.getMiddleName())
                    .withUserRole(userFormDto.getUserRole())
                    .build();
            Long id = userService.insertOrUpdateUser(user);
            model.addAttribute("resultText", "user.add.message");
            model.addAttribute("linkText", "user.add.link.text");
            model.addAttribute("link", "/user/get/" + id);
            return "add-result";
        }
    }

    @GetMapping(name = "/users", value = "/users")
    public String getUsers(Model model) {

        Iterable<User> users = userService.getAllUsers();

        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping(name = "/delete-user/{id}", value = "/delete-user/{id}")
    public String deleteUser(@PathVariable("id") Long id,
                             Model model) {
        userService.deleteById(id);
        model.addAttribute("resultText", "user.delete.message");
        return "delete-result";
    }

    private ModelAndView userForm() {
        return new ModelAndView("user", "userFormDto", new UserFormDto());
    }

    private ModelAndView addUserForm() {
        return new ModelAndView("add-user", "userFormDto", new UserFormDto());
    }

    @Bean
    private CommandLineRunner runner(UserRepository userRepository,
                                     DisciplineRepository disciplineRepository) {
        return args -> {
            Document document1 = Document.builder()
                    .withDocumentFilename("Швець Н.В. Педагогічна нагрузка 2016-2017.xlsx")
                    .withDocumentType(DocumentType.TEACHING_LOAD)
                    .withUploadingDate(Date.valueOf(LocalDate.now()))
                    .build();
            Document document2 = Document.builder()
                    .withDocumentFilename("Швець Н.В. Педагогічна нагрузка 2017-2018.xlsx")
                    .withDocumentType(DocumentType.TEACHING_LOAD)
                    .withUploadingDate(Date.valueOf(LocalDate.now()))
                    .build();
            Document document3 = Document.builder()
                    .withDocumentFilename("Мітрофанова Н.Ф. Педагогічна нагрузка 2016-2017.xlsx")
                    .withDocumentType(DocumentType.TEACHING_LOAD)
                    .withUploadingDate(Date.valueOf(LocalDate.now()))
                    .build();
            Document document4 = Document.builder()
                    .withDocumentFilename("Мітрофанова Н.Ф. Педагогічна нагрузка 2017-2018.xlsx")
                    .withDocumentType(DocumentType.TEACHING_LOAD)
                    .withUploadingDate(Date.valueOf(LocalDate.now()))
                    .build();
            Document document5 = Document.builder()
                    .withDocumentFilename("ПтаППБ. Конспект лекцій 2013.docx")
                    .withDocumentType(DocumentType.THEORETICAL_PART_LECTURE_NOTES)
                    .withUploadingDate(Date.valueOf(LocalDate.now()))
                    .build();
            Document document6 = Document.builder()
                    .withDocumentFilename("ПтаППБ. Посібник до лабораторних робіт 2013.docx")
                    .withDocumentType(DocumentType.LABORATORY_WORK_TUTORIALS)
                    .withUploadingDate(Date.valueOf(LocalDate.now()))
                    .build();
            Document document7 = Document.builder()
                    .withDocumentFilename("ПтаППБ. Посібник до лабораторних робіт 2014.docx")
                    .withDocumentType(DocumentType.LABORATORY_WORK_TUTORIALS)
                    .withUploadingDate(Date.valueOf(LocalDate.now()))
                    .build();
            Document document8 = Document.builder()
                    .withDocumentFilename("КПП. Конспект лекцій 2013.docx")
                    .withDocumentType(DocumentType.THEORETICAL_PART_LECTURE_NOTES)
                    .withUploadingDate(Date.valueOf(LocalDate.now()))
                    .build();
            Document document9 = Document.builder()
                    .withDocumentFilename("КПП. Посібник до лабораторних робіт 2013.docx")
                    .withDocumentType(DocumentType.LABORATORY_WORK_TUTORIALS)
                    .withUploadingDate(Date.valueOf(LocalDate.now()))
                    .build();
            Document document10 = Document.builder()
                    .withDocumentFilename("КПП. Посібник до лабораторних робіт 2014.docx")
                    .withDocumentType(DocumentType.LABORATORY_WORK_TUTORIALS)
                    .withUploadingDate(Date.valueOf(LocalDate.now()))
                    .build();
            Document document11 = Document.builder()
                    .withDocumentFilename("КПП. Методичні матеріали для діагностики рівня засвоення програми модулів 2015.docx")
                    .withDocumentType(DocumentType.PROGRAM_MODULES_ADOPTION_LEVEL_DIAGNOSIS_METHODOLOGICAL_MATERIALS)
                    .withUploadingDate(Date.valueOf(LocalDate.now()))
                    .build();

            Discipline discipline1 = Discipline.builder()
                    .withName("Паралельне та Багатопоточне Програмування")
                    .withCik(document5)
                    .withCik(document6)
                    .withCik(document7)
                    .withCik(document11)
                    .build();
            Discipline discipline2 = Discipline.builder()
                    .withName("Крос-Платформне Програмування")
                    .withCik(document8)
                    .withCik(document9)
                    .withCik(document10)
                    .build();
            disciplineRepository.save(discipline1);
            disciplineRepository.save(discipline2);
            User user1 = User.builder()
                    .withLogin("nshvec60")
                    .withPassword("nshvec60")
                    .withEmail("shvetsnatalya@rambler.ru")
                    .withLastName("Швець")
                    .withFirstName("Наталя")
                    .withMiddleName("Василівна")
                    .withUserRole(UserRole.METHODOLOGIST)
                    .build();
            User user2 = User.builder()
                    .withLogin("proziumod")
                    .withPassword("proziumod")
                    .withEmail("proziumod@gmail.com")
                    .withLastName("Мітрофанова")
                    .withFirstName("Наталя")
                    .withMiddleName("Федорівна")
                    .withUserRole(UserRole.ASSISTANT_LECTURER)
                    .build();
            HashSet<Discipline> disciplines = new HashSet<Discipline>() {{
                add(discipline1);
                add(discipline2);
            }};
            user1.setTeachingLoads(new HashSet<Document>() {{
                add(document1);
                add(document2);
            }});
            user1.setDisciplines(disciplines);
            user2.setTeachingLoads(new HashSet<Document>() {{
                add(document3);
                add(document4);
            }});
            user2.setDisciplines(disciplines);

            userRepository.save(User.builder()
                    .withLogin("admin")
                    .withPassword("admin")
                    .withEmail("blabla@gmail.com")
                    .withPassword("14ph38")
                    .withLastName("")
                    .withFirstName("")
                    .withMiddleName("")
                    .withUserRole(UserRole.ADMINISTRATOR)
                    .build());
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(User.builder()
                    .withLogin("plotnikov")
                    .withPassword("plotnikov")
                    .withEmail("plotnikov@ukr.net")
                    .withLastName("Плотников")
                    .withFirstName("Валерій")
                    .withMiddleName("Михайлович")
                    .withUserRole(UserRole.HEAD_OF_DEPARTMENT)
                    .build());
            userRepository.save(User.builder()
                    .withLogin("popkovdn")
                    .withPassword("popkovdn")
                    .withEmail("popkovdn@ukr.net")
                    .withLastName("Попков")
                    .withFirstName("Денис")
                    .withMiddleName("Миколайович")
                    .withUserRole(UserRole.SENIOR_LECTURER)
                    .build());
        };
    }
}
