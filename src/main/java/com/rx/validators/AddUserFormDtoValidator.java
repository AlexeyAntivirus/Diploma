package com.rx.validators;


import com.rx.dao.UserRole;
import com.rx.dto.forms.AddUserFormDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class AddUserFormDtoValidator implements Validator {

    private final Pattern emailPattern;

    @Autowired
    public AddUserFormDtoValidator(@Value("${app.email.pattern}") String emailPattern) {
        this.emailPattern = Pattern.compile(emailPattern);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AddUserFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddUserFormDto dto = (AddUserFormDto) target;

        validateUsername(dto.getUsername(), errors);
        validatePassword(dto.getPassword(), errors);
        validateEmail(dto.getEmail(), errors);
        validateFirstName(dto.getFirstName(), errors);
        validateLastName(dto.getLastName(), errors);
        validateMiddleName(dto.getMiddleName(), errors);
        validateUserRole(dto.getUserRole(), errors);
    }

    private void validateUsername(String username, Errors errors) {
        if (username == null || username.isEmpty()) {
            errors.rejectValue("username", "field.not.specified");
        } else if (username.length() > 128 || username.length() < 6) {
            errors.rejectValue("username", "invalid.field.size.range");
        }
    }

    private void validatePassword(String password, Errors errors) {
        if (password == null || password.isEmpty()) {
            errors.rejectValue("password", "field.not.specified");
        } else if (password.length() > 128 || password.length() < 6) {
            errors.rejectValue("password", "invalid.field.size.range");
        }
    }

    private void validateEmail(String email, Errors errors) {
        if (email != null && !email.isEmpty()) {
            if (!emailPattern.matcher(email).matches()) {
                errors.rejectValue("email", "invalid.email");
            }
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

    private void validateUserRole(UserRole userRole, Errors errors) {
        if (userRole == null) {
            errors.rejectValue("userRole", "field.not.specified");
        }
    }
}
