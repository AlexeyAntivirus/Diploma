package com.rx;

import com.rx.dto.FileUploadFormDto;
import com.rx.validators.FileUploadFormDtoValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class FileUploadFormDtoValidatorTests {

    @Value("${app.storage.allowed.filename}")
    String allowedFilenamePattern;

    private FileUploadFormDtoValidator validator;

    @Before
    public void init() {
        validator = new FileUploadFormDtoValidator(allowedFilenamePattern);
    }

    @Test(expected = PatternSyntaxException.class)
    public void testConstructorWhenAllowedFilenamePatternIsInvalid() {
        new FileUploadFormDtoValidator("^[a-zA-Z0-9.()\\-_\\pInCyrillic}]+\\.(doc|docx|xls|xlsx|pdf|txt|rtf)$");
    }

    @Test
    public void testConstructorWhenAllIsWell() {
        Pattern initializedAllowedFilenamePattern = (Pattern) Whitebox.getInternalState(validator, "filenamePattern");
        Assert.assertEquals(allowedFilenamePattern, initializedAllowedFilenamePattern.toString());
    }

    @Test
    public void testSupportsWhenAllIsWell() {
        Assert.assertTrue(validator.supports(FileUploadFormDto.class));
    }

    @Test
    public void testSupportsWhenClassIsNotAssignableFrom() {
        Assert.assertFalse(validator.supports(Thread.class));
    }

    @Test
    public void testValidateWhenAllIsWell() {
        FileUploadFormDto dto = new FileUploadFormDto();
        dto.setMultipartFile(new MockMultipartFile("multipartFile", "text.txt", "text/plain", "This is a test".getBytes()));
        Errors errors = new BeanPropertyBindingResult(dto, "fileUploadFormDto");

        validator.validate(dto, errors);

        Assert.assertNull(errors.getFieldError("multipartFile"));
    }

    @Test
    public void testValidateMultipartFileIsEmpty() {
        FileUploadFormDto dto = new FileUploadFormDto();
        dto.setMultipartFile(new MockMultipartFile("multipartFile", "text.txt", "text/plain", (byte[]) null));
        Errors errors = new BeanPropertyBindingResult(dto, "fileUploadFormDto");

        validator.validate(dto, errors);
        FieldError multipartFileError = errors.getFieldError("multipartFile");
        Assert.assertNotNull(multipartFileError);
        Assert.assertEquals(multipartFileError.getCode(), "upload.file.empty");
    }

    @Test
    public void testValidateMultipartFileOriginalNameIsInvalid() {
        FileUploadFormDto dto = new FileUploadFormDto();
        dto.setMultipartFile(new MockMultipartFile("multipartFile", "text.sh", "text/plain", "echo \"Hello!\"".getBytes()));
        Errors errors = new BeanPropertyBindingResult(dto, "fileUploadFormDto");

        validator.validate(dto, errors);
        FieldError multipartFileError = errors.getFieldError("multipartFile");
        Assert.assertNotNull(multipartFileError);
        Assert.assertEquals(multipartFileError.getCode(), "upload.file.invalid.name");
    }
}
