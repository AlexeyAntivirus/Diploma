package com.rx.validators;

import com.rx.dto.forms.DocumentUploadFormDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

@Component
public class DocumentUploadFormDtoValidator implements Validator {
    private Pattern filenamePattern;

    @Autowired
    public DocumentUploadFormDtoValidator(@Value("${app.storage.allowed.filename}") String filenamePattern) {
        this.filenamePattern = Pattern.compile(filenamePattern);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return DocumentUploadFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DocumentUploadFormDto uploadFormDto = (DocumentUploadFormDto) target;
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
