package com.rx.dto.forms;


import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginFormDto {

    @NotNull(message = "{field.not.specified}")
    @NotEmpty(message = "{field.not.specified}")
    @Size(min = 6, max = 255, message = "{invalid.field.size.range}")
    private String login;

    @NotNull(message = "{field.not.specified}")
    @NotEmpty(message = "{field.not.specified}")
    @Size(min = 6, max = 255, message = "{invalid.field.size.range}")
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
