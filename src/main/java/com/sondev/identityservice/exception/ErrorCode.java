package com.sondev.identityservice.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION (999, "Uncategorized exception error"),
    INVALID_KEY (10001, "Invaid message key"),
    USER_EXISTED (1002, "User existed"),
    USERNAME_INVALID(1003, "Username must be at least 3 character"),
    INVALID_PASSWORD(1004, "Password must be at least 8 chacracter"),
    USER_NOT_EXISTED (1005, "User not existed")
    ;


    //Field
    private int code;
    private String message;

    // Constructor
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getter and Setter
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
