package com.sondev.identityservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION (999, "Uncategorized exception error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY (10001, "Invaid message key", HttpStatus.BAD_REQUEST),
    USER_EXISTED (1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 3 character", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least 8 character", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED (1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007,"You do not have permission", HttpStatus.FORBIDDEN)
    ;
// HttpStatus.INTERNAL_SERVER_ERROR: 500
// HttpStatus.BAD_REQUEST: 400 Lỗi liên quan input user
// HttpStatus.NOT_FOUND:   Lỗi k tìm thấy
// HttpStatus.UNAUTHORIZED: 401 Lỗi không đăng nhập được
    //Field
    private int code;
    private String message;
    private HttpStatusCode statusCode;

    // Constructor
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }


}
