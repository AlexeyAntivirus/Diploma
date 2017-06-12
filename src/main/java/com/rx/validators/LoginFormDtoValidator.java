package com.rx.validators;

import com.rx.dto.forms.LoginFormDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoginFormDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return LoginFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginFormDto dto = (LoginFormDto) target;

        validateLogin(dto.getLogin(), errors);
        validatePassword(dto.getPassword(), errors);
    }

    private void validatePassword(String password, Errors errors) {
        if (password == null || password.isEmpty()) {
            errors.rejectValue("password", "field.not.specified");
        } else if (password.length() > 128 || password.length() < 6) {
            errors.rejectValue("password", "invalid.field.size.range");
        }
    }

    private void validateLogin(String login, Errors errors) {
        if (login == null || login.isEmpty()) {
            errors.rejectValue("login", "field.not.specified");
        } else if (login.length() > 128 || login.length() < 6) {
            errors.rejectValue("login", "invalid.field.size.range");
        }
    }
}