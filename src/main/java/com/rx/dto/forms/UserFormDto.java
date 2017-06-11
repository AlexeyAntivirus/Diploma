package com.rx.dto.forms;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserFormDto {

    @NotNull(message = "{field.not.specified}")
    @NotEmpty(message = "{field.not.specified}")
    @Email(message = "{email.invalid}")
    private String email;

    @NotNull(message = "{field.not.specified}")
    @NotEmpty(message = "{field.not.specified}")
    @Size(min = 6, max = 255, message = "{invalid.field.size.range}")
    private String password;

    @NotNull(message = "{field.not.specified}")
    @NotEmpty(message = "{field.not.specified}")
    @Size(max = 255, message = "{invalid.field.size}")
    private String lastName;

    @NotNull(message = "{field.not.specified}")
    @NotEmpty(message = "{field.not.specified}")
    @Size(max = 255, message = "{invalid.field.size}")
    private String firstName;

    @NotNull(message = "{field.not.specified}")
    @NotEmpty(message = "{field.not.specified}")
    @Size(max = 255, message = "{invalid.field.size}")
    private String middleName;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
}
