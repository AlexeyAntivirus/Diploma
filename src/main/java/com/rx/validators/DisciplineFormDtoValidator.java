package com.rx.validators;

import com.rx.dto.DisciplineFormDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DisciplineFormDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return DisciplineFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DisciplineFormDto disciplineFormDto = (DisciplineFormDto) target;

        validateName(disciplineFormDto.getName(), errors);
    }

    private void validateName(String name, Errors errors) {
        if (name == null || name.isEmpty()) {
            errors.rejectValue("name", "field.not.specified");
        }
    }

}
