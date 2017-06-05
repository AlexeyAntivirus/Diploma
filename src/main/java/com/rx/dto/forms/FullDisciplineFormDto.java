package com.rx.dto.forms;


import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class FullDisciplineFormDto {

    @NotNull(message = "{field.not.specified}")
    @NotEmpty(message = "{field.not.specified}")
    private String name;

    private Long userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
