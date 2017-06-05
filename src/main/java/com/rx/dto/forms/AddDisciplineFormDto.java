package com.rx.dto.forms;


import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class AddDisciplineFormDto {

    @NotNull(message = "{field.not.specified}")
    @NotEmpty(message = "{field.not.specified}")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
