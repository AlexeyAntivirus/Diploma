package com.rx.validators;


import com.rx.dto.forms.FullDisciplineFormDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FullDisciplineFormDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return FullDisciplineFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FullDisciplineFormDto disciplineFormDto = (FullDisciplineFormDto) target;

        validateName(disciplineFormDto.getName(), errors);
    }

    private void validateName(String name, Errors errors) {
        if (name == null || name.isEmpty()) {
            errors.rejectValue("name", "field.not.specified");
        }
    }

}