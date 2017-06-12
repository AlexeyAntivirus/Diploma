package com.rx.controllers;

import com.rx.controllers.exceptions.FileUploadIOException;
import com.rx.controllers.exceptions.FileUploadInvalidPathException;
import com.rx.dto.forms.DocumentUploadFormDto;
import com.rx.dto.DocumentUploadResultDto;
import com.rx.services.DocumentStorageService;
import com.rx.services.UserService;
import com.rx.validators.DocumentUploadFormDtoValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
    private DocumentUploadFormDtoValidator validator;
    private UserService userService;

    @InitBinder("fileUploadFormDto")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @Autowired
    public DocumentUploadController(UserService userService,
                                    DocumentStorageService storageService,
                                    DocumentUploadFormDtoValidator validator) {
        this.documentStorageService = storageService;
        this.userService = userService;
        this.validator = validator;
    }

    @GetMapping(value = "/syllabus")
    public ModelAndView getSyllabusUploadForm(@RequestParam("userId") Long userId) {
        ModelAndView modelAndView = uploadModelAndView();

        modelAndView.getModel().put("user", userService.getUserById(userId));

        return modelAndView;
    }

    @PostMapping(name = "/syllabus", value = "/syllabus", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadSyllabus(@RequestParam("userId") Long userId,
                                 @Valid DocumentUploadFormDto documentUploadFormDto,
                                 BindingResult bindingResult,
                                 Model model) {
        model.addAttribute("user", userService.getUserById(userId));
        if (bindingResult.hasErrors()) {
            return "upload";
        }

        DocumentUploadResultDto result = this.documentStorageService.
                saveSyllabusInStorage(documentUploadFormDto.getMultipartFile(), Date.valueOf(LocalDate.now()));

        model.addAttribute("uploadedFileId", result.getDocumentId());
        model.addAttribute("attribute", "redirectWithRedirectPrefix");

        return "redirect:/syllabuses";
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

        modelAndView.addObject("exception", e);

        return modelAndView;
    }

    private ModelAndView uploadModelAndView() {
        return new ModelAndView("upload", "documentUploadFormDto", new DocumentUploadFormDto());
    }
}
