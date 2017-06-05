package com.rx.dto;


import com.rx.dao.User;

public class UserUpdatingResultDto {

    private String errorField;

    private String errorMessage;

    private User updatedUser;

    protected UserUpdatingResultDto(UserUpdatingResultDtoBuilder builder) {
        this.errorField = builder.errorField;
        this.errorMessage = builder.errorMessage;
        this.updatedUser = builder.updatedUser;
    }

    public String getErrorField() {
        return errorField;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public User getUpdatedUser() {
        return updatedUser;
    }

    public static UserUpdatingResultDtoBuilder builder() {
        return new UserUpdatingResultDtoBuilder();
    }


    public static class UserUpdatingResultDtoBuilder {

        private String errorField;

        private String errorMessage;

        private User updatedUser;

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

        public UserUpdatingResultDtoBuilder withUpdatedUser(User updatedUser) {
            this.updatedUser = updatedUser;
            return this;
        }

        public UserUpdatingResultDto build() {
            return new UserUpdatingResultDto(this);
        }
    }
}
