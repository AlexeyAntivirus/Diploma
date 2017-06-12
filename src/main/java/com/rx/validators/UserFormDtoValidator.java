package com.rx.validators;


import com.rx.dao.UserRole;
import com.rx.dto.forms.UserFormDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserFormDtoValidator implements Validator {

    private final Pattern emailPattern;

    @Autowired
    public UserFormDtoValidator(@Value("${app.email.pattern}") String emailPattern) {
        this.emailPattern = Pattern.compile(emailPattern);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserFormDto dto = (UserFormDto) target;

        validateEmail(dto.getEmail(), errors);
        validatePassword(dto.getPassword(), errors);
        validateFirstName(dto.getFirstName(), errors);
        validateLastName(dto.getLastName(), errors);
        validateMiddleName(dto.getMiddleName(), errors);
    }

    private void validateEmail(String email, Errors errors) {
        if (email == null || email.isEmpty()) {
            errors.rejectValue("email", "field.not.specified");
        } else if (!emailPattern.matcher(email).matches()) {
            errors.rejectValue("email", "invalid.email");
        }
    }

    private void validatePassword(String password, Errors errors) {
        if (password == null || password.isEmpty()) {
            errors.rejectValue("password", "field.not.specified");
        } else if (password.length() > 128 || password.length() < 6) {
            errors.rejectValue("password", "invalid.field.size.range");
        }
    }

    private void validateLastName(String lastName, Errors errors) {
        if (lastName == null || lastName.isEmpty()) {
            errors.rejectValue("lastName", "field.not.specified");
        } else if (lastName.length() > 128) {
            errors.rejectValue("lastName", "invalid.field.size");
        }
    }

    private void validateFirstName(String firstName, Errors errors) {
        if (firstName == null || firstName.isEmpty()) {
            errors.rejectValue("firstName", "field.not.specified");
        } else if (firstName.length() > 128) {
            errors.rejectValue("firstName", "invalid.field.size");
        }
    }

    private void validateMiddleName(String middleName, Errors errors) {
        if (middleName == null || middleName.isEmpty()) {
            errors.rejectValue("middleName", "field.not.specified");
        } else if (middleName.length() > 128) {
            errors.rejectValue("middleName", "invalid.field.size");
        }
    }


}