package com.rx.dto;


public class DisciplineAddingResultDto {

    private Long disciplineId;

    private String errorField;

    private String errorMessage;

    protected DisciplineAddingResultDto(DisciplineAddingResultDtoBuilder builder) {
        this.disciplineId = builder.disciplineId;
        this.errorField = builder.errorField;
        this.errorMessage = builder.errorMessage;
    }

    public String getErrorField() {
        return errorField;
    }

    public Long getDisciplineId() {
        return disciplineId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static DisciplineAddingResultDtoBuilder builder() {
        return new DisciplineAddingResultDtoBuilder();
    }

    public static class DisciplineAddingResultDtoBuilder {

        private Long disciplineId;

        private String errorField;

        private String errorMessage;

        private DisciplineAddingResultDtoBuilder() {
        }


        public DisciplineAddingResultDtoBuilder withDisciplineId(Long disciplineId) {
            this.disciplineId = disciplineId;
            return this;
        }

        public DisciplineAddingResultDtoBuilder withErrorField(String errorField) {
            this.errorField = errorField;
            return this;
        }

        public DisciplineAddingResultDtoBuilder withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public DisciplineAddingResultDto build() {
            return new DisciplineAddingResultDto(this);
        }
    }
}
