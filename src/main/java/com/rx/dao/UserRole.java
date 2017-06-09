package com.rx.dao;


public enum UserRole {
    LECTURER,
    ASSISTANT_LECTURER,
    HEAD_OF_DEPARTMENT,
    METHODOLOGIST,
    ADMINISTRATOR;

    public String getString() {
        switch (this) {
            case LECTURER:
                return "user.role.lecturer";
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
