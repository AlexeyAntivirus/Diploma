package com.rx.dto;


import com.rx.dao.Discipline;

public class DisciplineUpdatingResultDto {

    private Discipline updatedDiscipline;

    private String errorField;

    private String errorMessage;

    protected DisciplineUpdatingResultDto(DisciplineUpdatingResultDtoBuilder builder) {
        this.updatedDiscipline = builder.updatedDiscipline;
        this.errorField = builder.errorField;
        this.errorMessage = builder.errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorField() {
        return errorField;
    }

    public Discipline getUpdatedDiscipline() {
        return updatedDiscipline;
    }

    public static DisciplineUpdatingResultDtoBuilder builder() {
        return new DisciplineUpdatingResultDtoBuilder();
    }

    public static class DisciplineUpdatingResultDtoBuilder {
        private Discipline updatedDiscipline;
        private String errorField;
        private String errorMessage;

        private DisciplineUpdatingResultDtoBuilder() {
        }

        public DisciplineUpdatingResultDtoBuilder withUpdatedDiscipline(Discipline updatedDiscipline) {
            this.updatedDiscipline = updatedDiscipline;
            return this;
        }

        public DisciplineUpdatingResultDtoBuilder withErrorField(String errorField) {
            this.errorField = errorField;
            return this;
        }

        public DisciplineUpdatingResultDtoBuilder withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public DisciplineUpdatingResultDto build() {
            return new DisciplineUpdatingResultDto(this);
        }
    }
}
