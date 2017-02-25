package com.rx.validators;

import com.rx.dto.FileUploadFormDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

@Component
public class FileUploadFormDtoValidator implements Validator {
    private Pattern filenamePattern;

    @Autowired
    public FileUploadFormDtoValidator(@Value("${app.storage.allowed.filename}") String filenamePattern) {
        this.filenamePattern = Pattern.compile(filenamePattern);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FileUploadFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FileUploadFormDto uploadFormDto = (FileUploadFormDto) target;
        MultipartFile multipartFile = uploadFormDto.getMultipartFile();

        if (multipartFile.isEmpty()) {
            errors.rejectValue("multipartFile", "upload.file.empty");

            return;
        }

        if (!filenamePattern.matcher(multipartFile.getOriginalFilename()).matches()) {
            errors.rejectValue("multipartFile", "upload.file.invalid.name");
        }
    }
}
