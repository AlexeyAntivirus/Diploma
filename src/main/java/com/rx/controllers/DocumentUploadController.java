package com.rx.controllers;

import com.rx.controllers.exceptions.FileUploadIOException;
import com.rx.controllers.exceptions.FileUploadInvalidPathException;
import com.rx.dao.Discipline;
import com.rx.dao.User;
import com.rx.dto.forms.CurriculumUploadFormDto;
import com.rx.dto.forms.DocumentUploadFormDto;
import com.rx.dto.DocumentUploadResultDto;
import com.rx.helpers.AuthenticatedUser;
import com.rx.services.DisciplineService;
import com.rx.services.DocumentStorageService;
import com.rx.services.UserService;
import com.rx.validators.CurriculumUploadFormDtoValidator;
import com.rx.validators.DocumentUploadFormDtoValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;

@Controller
@RequestMapping("/upload")
public class DocumentUploadController {

    private static final Logger LOGGER = LogManager.getLogger(DocumentUploadController.class);

    private DocumentStorageService documentStorageService;
    private DocumentUploadFormDtoValidator documentUploadFormDtoValidator;
    private CurriculumUploadFormDtoValidator curriculumUploadFormDtoValidator;
    private UserService userService;
    private DisciplineService disciplineService;

    @InitBinder("documentUploadFormDto")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(documentUploadFormDtoValidator);
    }

    @InitBinder("curriculumUploadFormDto")
    private void curriculumInitBinder(WebDataBinder binder) {
        binder.setValidator(curriculumUploadFormDtoValidator);
    }

    @Autowired
    public DocumentUploadController(UserService userService,
                                    DisciplineService disciplineService,
                                    DocumentStorageService storageService,
                                    DocumentUploadFormDtoValidator documentUploadFormDtoValidator,
                                    CurriculumUploadFormDtoValidator curriculumUploadFormDtoValidator) {
        this.documentStorageService = storageService;
        this.disciplineService = disciplineService;
        this.userService = userService;
        this.documentUploadFormDtoValidator = documentUploadFormDtoValidator;
        this.curriculumUploadFormDtoValidator = curriculumUploadFormDtoValidator;
    }

    @GetMapping(name = "/syllabus", value = "/syllabus")
    public ModelAndView getSyllabusUploadForm() {
        ModelAndView modelAndView = uploadModelAndView();

        modelAndView.getModel().put("type", "syllabus");

        return modelAndView;
    }

    @PostMapping(name = "/syllabus", value = "/syllabus", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadSyllabus(@Valid DocumentUploadFormDto documentUploadFormDto,
                                 BindingResult bindingResult,
                                 Model model) {
        model.addAttribute("attribute", "redirectWithRedirectPrefix");

        if (bindingResult.hasErrors()) {
            model.addAttribute("type", "syllabus");
            return "upload";
        }

        if (documentStorageService.isFileExists(documentUploadFormDto.getMultipartFile().getOriginalFilename())) {
            model.addAttribute("type", "syllabus");
            bindingResult.rejectValue("multipartFile", "upload.file.exists");
            return "upload";
        }

        DocumentUploadResultDto result = this.documentStorageService.
                saveSyllabusInStorage(documentUploadFormDto.getMultipartFile(), Date.valueOf(LocalDate.now()));

        model.addAttribute("uploadedFileId", result.getDocumentId());


        return "redirect:/user/syllabuses";
    }

    @GetMapping(name = "/act", value = "/act")
    public ModelAndView getNormativeActUploadForm() {
        ModelAndView modelAndView = uploadModelAndView();

        modelAndView.getModel().put("type", "act");

        return modelAndView;
    }

    @PostMapping(name = "/act", value = "/act", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadNormativeAct(
                                 @Valid DocumentUploadFormDto documentUploadFormDto,
                                 BindingResult bindingResult,
                                 Model model) {
        model.addAttribute("attribute", "redirectWithRedirectPrefix");
        if (bindingResult.hasErrors()) {
            model.addAttribute("type", "act");
            return "upload";
        }

        if (documentStorageService.isFileExists(documentUploadFormDto.getMultipartFile().getOriginalFilename())) {
            bindingResult.rejectValue("multipartFile", "upload.file.exists");
            model.addAttribute("type", "act");
            return "upload";
        }

        DocumentUploadResultDto result = this.documentStorageService.
                saveNormativeActInStorage(documentUploadFormDto.getMultipartFile(), Date.valueOf(LocalDate.now()));

        model.addAttribute("uploadedFileId", result.getDocumentId());


        return "redirect:/user/acts";
    }

    @GetMapping(name = "/teaching-load", value = "/teaching-load")
    public ModelAndView getTeachingLoadUploadForm() {
        ModelAndView modelAndView = uploadModelAndView();

        modelAndView.getModel().put("type", "teaching-load");

        return modelAndView;
    }

    @PostMapping(name = "/teaching-load", value = "/teaching-load", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadTeachingLoad(
                                     @Valid DocumentUploadFormDto documentUploadFormDto,
                                     BindingResult bindingResult,
                                     Model model) {

        Long userId = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User userById = userService.getUserById(userId);
        model.addAttribute("attribute", "redirectWithRedirectPrefix");

        if (bindingResult.hasErrors()) {
            model.addAttribute("type", "teaching-load");
            return "upload";
        }

        if (documentStorageService.isFileExists(documentUploadFormDto.getMultipartFile().getOriginalFilename())) {
            bindingResult.rejectValue("multipartFile", "upload.file.exists");
            model.addAttribute("type", "teaching-load");
            return "upload";
        }

        DocumentUploadResultDto result = this.documentStorageService.
                saveTeachingLoadInStorage(userById, documentUploadFormDto.getMultipartFile(), Date.valueOf(LocalDate.now()));

        model.addAttribute("uploadedFileId", result.getDocumentId());


        return "redirect:/user/profile";
    }

    @GetMapping(name = "/curriculum", value = "/curriculum")
    public ModelAndView getCurriculumUploadForm() {
        ModelAndView modelAndView = curriculumUploadModelAndView();
        Long userId = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        modelAndView.addObject("user", userService.getUserById(userId));

        return modelAndView;
    }

    @PostMapping(name = "/curriculum", value = "/curriculum", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadCurriculum(@Valid CurriculumUploadFormDto curriculumUploadFormDto,
                                     BindingResult bindingResult,
                                     Model model) {
        Long userId = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        model.addAttribute("attribute", "redirectWithRedirectPrefix");

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.getUserById(userId));
            return "upload-curriculum";
        }

        if (documentStorageService.isFileExists(curriculumUploadFormDto.getMultipartFile().getOriginalFilename())) {
            bindingResult.rejectValue("multipartFile", "upload.file.exists");
            model.addAttribute("user", userService.getUserById(userId));
            return "upload-curriculum";
        }
        Discipline discipline = disciplineService.getDisciplineById(curriculumUploadFormDto.getDisciplineId());
        DocumentUploadResultDto result = this.documentStorageService.
                saveCurriculumInStorage(discipline, curriculumUploadFormDto.getMultipartFile(),
                        curriculumUploadFormDto.getDocumentType(), Date.valueOf(LocalDate.now()));

        model.addAttribute("uploadedFileId", result.getDocumentId());

        return "redirect:/discipline/" + curriculumUploadFormDto.getDisciplineId();
    }

    @GetMapping(name = "/delete-syllabus/{id}", value = "/delete-syllabus/{id}")
    public String deleteSyllabus(@PathVariable("id") Long id,
                                 Model model) {
        documentStorageService.deleteDocument(id);
        model.addAttribute("attribute", "redirectWithRedirectPrefix");

        return "redirect:/user/syllabuses";
    }

    @GetMapping(name = "/delete-act/{id}", value = "/delete-act/{id}")
    public String deleteAct(@PathVariable("id") Long id,
                            Model model) {
        documentStorageService.deleteDocument(id);
        model.addAttribute("attribute", "redirectWithRedirectPrefix");

        return "redirect:/user/acts";
    }

    @GetMapping(name = "/delete-curriculum/{id}", value = "/delete-curriculum/{id}")
    public String deleteCurriculum(@PathVariable("id") Long id,
                                   @RequestParam("disciplineId") Long disciplineId,
                                   Model model) {
        documentStorageService.deleteCurriculum(disciplineId, id);
        model.addAttribute("attribute", "redirectWithRedirectPrefix");

        return "redirect:/discipline/" + disciplineId;
    }

    @GetMapping(name = "/delete-teaching-load/{id}", value = "/delete-teaching-load/{id}")
    public String deleteTeachingLoad(@PathVariable("id") Long id, Model model) {
        Long userId = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        documentStorageService.deleteTeachingLoad(userId, id);

        model.addAttribute("attribute", "redirectWithRedirectPrefix");

        return "redirect:/user/profile";
    }


    @ExceptionHandler(value = FileUploadInvalidPathException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView fileUploadInvalidPathExceptionHandler(FileUploadInvalidPathException e) {
        return doHandleException(e);
    }

    @ExceptionHandler(value = FileUploadIOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView fileUploadIOExceptionHandler(FileUploadIOException e) {
        return doHandleException(e);
    }


    private ModelAndView doHandleException(RuntimeException e) {
        LOGGER.warn(e.getMessage(), e);

        ModelAndView modelAndView = uploadModelAndView();
        ModelAndView curriculumModelAndView = curriculumUploadModelAndView();

        modelAndView.addObject("exception", e);
        curriculumModelAndView.addObject("exception", e);

        return modelAndView;
    }

    private ModelAndView uploadModelAndView() {
        return new ModelAndView("upload", "documentUploadFormDto", new DocumentUploadFormDto());
    }

    private ModelAndView curriculumUploadModelAndView() {
        return new ModelAndView("upload-curriculum", "curriculumUploadFormDto", new CurriculumUploadFormDto());
    }
}
