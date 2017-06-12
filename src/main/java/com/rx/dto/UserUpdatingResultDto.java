package com.rx.dto;


public class UserUpdatingResultDto {

    private String errorField;

    private String errorMessage;

    private boolean isUpdated;

    protected UserUpdatingResultDto(UserUpdatingResultDtoBuilder builder) {
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

    public static UserUpdatingResultDtoBuilder builder() {
        return new UserUpdatingResultDtoBuilder();
    }


    public static class UserUpdatingResultDtoBuilder {

        private String errorField;

        private String errorMessage;

        private boolean isUpdated;

        private UserUpdatingResultDtoBuilder() {
        }

        public UserUpdatingResultDtoBuilder withErrorField(String errorField) {
            this.errorField = errorField;
            return this;
        }

        public UserUpdatingResultDtoBuilder withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public UserUpdatingResultDtoBuilder isUpdated(boolean isUpdated) {
            this.isUpdated = isUpdated;
            return this;
        }

        public UserUpdatingResultDto build() {
            return new UserUpdatingResultDto(this);
        }
    }
}
