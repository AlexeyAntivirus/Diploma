package com.rx.validators;


import com.rx.dto.forms.UserFormDto;
import com.rx.helpers.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sun.plugin.liveconnect.SecurityContextHelper;

import java.util.regex.Pattern;

@Component
public class UserFormDtoValidator implements Validator {

    private final Pattern emailPattern;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserFormDtoValidator(@Value("${app.email.pattern}") String emailPattern, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.emailPattern = Pattern.compile(emailPattern);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserFormDto dto = (UserFormDto) target;

        validateEmail(dto.getEmail(), errors);
        validatePassword(dto.getCurrentPassword(), dto.getNewPassword(), errors);
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

    private void validatePassword(String currentPassword, String newPassword, Errors errors) {

        if ((currentPassword == null || currentPassword.isEmpty()) && (newPassword != null && !newPassword.isEmpty())) {
            errors.rejectValue("currentPassword", "field.not.specified");
            return;
        } else if ((newPassword == null || newPassword.isEmpty()) && (currentPassword != null && !currentPassword.isEmpty())) {
            errors.rejectValue("newPassword", "field.not.specified");
            return;
        } else if ((currentPassword == null || currentPassword.isEmpty()) && (newPassword == null || newPassword.isEmpty())) {
            return;
        }

        if (currentPassword.length() > 128 || currentPassword.length() < 6) {
            errors.rejectValue("currentPassword", "invalid.field.size.range");
            return;
        }

        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!bCryptPasswordEncoder.matches(currentPassword, principal.getPassword())) {
            errors.rejectValue("currentPassword", "user.password.notConfirmed");
            return;
        }

        if (newPassword.length() > 128 || newPassword.length() < 6) {
            errors.rejectValue("newPassword", "invalid.field.size.range");
            return;
        }

        if (currentPassword.equals(newPassword)) {
            errors.rejectValue("newPassword", "user.password.notConfirmed");
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