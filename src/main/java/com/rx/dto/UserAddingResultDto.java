package com.rx.dto;


public class UserAddingResultDto {

    private Long userId;

    private String errorField;

    private String errorMessage;

    protected UserAddingResultDto(UserAddingResultDtoBuilder builder) {
        this.errorMessage = builder.errorMessage;
        this.userId = builder.userId;
        this.errorField = builder.errorField;
    }

    public String getErrorField() {
        return errorField;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Long getUserId() {
        return userId;
    }

    public static UserAddingResultDtoBuilder builder() {
        return new UserAddingResultDtoBuilder();
    }

    public static class UserAddingResultDtoBuilder {

        private String errorField;

        private String errorMessage;

        private Long userId;


        private UserAddingResultDtoBuilder() {
        }

        public UserAddingResultDtoBuilder withErrorField(String errorField) {
            this.errorField = errorField;
            return this;
        }

        public UserAddingResultDtoBuilder withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public UserAddingResultDtoBuilder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserAddingResultDto build() {
            return new UserAddingResultDto(this);
        }
    }
}
