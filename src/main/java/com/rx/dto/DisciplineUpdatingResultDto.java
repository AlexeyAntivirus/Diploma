package com.rx.dto;


import com.rx.dao.Discipline;

public class DisciplineUpdatingResultDto {

    private String errorField;

    private String errorMessage;

    private boolean isUpdated;

    private DisciplineUpdatingResultDto(DisciplineUpdatingResultDtoBuilder builder) {
        this.errorField = builder.errorField;
        this.errorMessage = builder.errorMessage;
        this.isUpdated = builder.isUpdated;
    }

    public String getErrorField() {
        return errorField;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public static DisciplineUpdatingResultDtoBuilder builder() {
        return new DisciplineUpdatingResultDtoBuilder();
    }

    public static class DisciplineUpdatingResultDtoBuilder {
        private String errorField;

        private String errorMessage;

        private boolean isUpdated;

        public DisciplineUpdatingResultDtoBuilder withErrorField(String errorField) {
            this.errorField = errorField;
            return this;
        }

        public DisciplineUpdatingResultDtoBuilder withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public DisciplineUpdatingResultDtoBuilder isUpdated(boolean isUpdated) {
            this.isUpdated = isUpdated;
            return this;
        }

        public DisciplineUpdatingResultDto build() {
            return new DisciplineUpdatingResultDto(this);
        }
    }
}
