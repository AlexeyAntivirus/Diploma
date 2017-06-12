package com.rx.validators;


import com.rx.dto.forms.AddDisciplineFormDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AddDisciplineFormDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AddDisciplineFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddDisciplineFormDto disciplineFormDto = (AddDisciplineFormDto) target;

        validateName(disciplineFormDto.getDisciplineName(), errors);
    }

    private void validateName(String name, Errors errors) {
        if (name == null || name.isEmpty()) {
            errors.rejectValue("disciplineName", "field.not.specified");
        }
    }

}