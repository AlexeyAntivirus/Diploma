package com.rx.controllers;

import com.rx.controllers.exceptions.FileUploadIOException;
import com.rx.controllers.exceptions.FileUploadInvalidPathException;
import com.rx.dao.DocumentType;
import com.rx.dto.forms.FileUploadFormDto;
import com.rx.dto.FileUploadResultDto;
import com.rx.services.FileStorageService;
import com.rx.validators.FileUploadFormDtoValidator;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;

@Controller
@RequestMapping("/upload")
public class FileUploadController {

    private static final Logger LOGGER = LogManager.getLogger(FileUploadController.class);

    private FileStorageService fileStorageService;
    private FileUploadFormDtoValidator validator;

    @InitBinder("fileUploadFormDto")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @Autowired
    public FileUploadController(FileStorageService storageService, FileUploadFormDtoValidator validator) {
        this.fileStorageService = storageService;
        this.validator = validator;
    }

    @GetMapping
    public ModelAndView getUploadForm() {
        return uploadModelAndView();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String handleUpload(@Valid FileUploadFormDto fileUploadFormDto,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            return "upload";
        }

        FileUploadResultDto result = this.fileStorageService.
                saveFileInStorage(fileUploadFormDto.getMultipartFile(),
                        DocumentType.COURSE_WORK_GUIDELINES,
                        Date.valueOf(LocalDate.now()));

        model.addAttribute("uploadedFileUUID", result.getFileId());

        return "upload-result";
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
        return new ModelAndView("upload", "fileUploadFormDto", new FileUploadFormDto());
    }
}
