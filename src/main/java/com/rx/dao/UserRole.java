package com.rx.dao;


public enum UserRole {
    SENIOR_LECTURER,
    ASSISTANT_LECTURER,
    HEAD_OF_DEPARTMENT,
    METHODOLOGIST,
    ADMINISTRATOR;

    @Override
    public String toString() {
        switch (this) {
            case SENIOR_LECTURER:
                return "user.role.senior-lecturer";
            case ASSISTANT_LECTURER:
                return "user.role.assistant-lecturer";
            case HEAD_OF_DEPARTMENT:
                return "user.role.head-of-department";
            case METHODOLOGIST:
                return "user.role.methodologist";
            case ADMINISTRATOR:
                return "user.role.administrator";
            default: throw new IllegalStateException();
        }
    }
}
