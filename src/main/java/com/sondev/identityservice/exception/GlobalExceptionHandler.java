package com.sondev.identityservice.exception;

import com.sondev.identityservice.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Khai báo cho Spring biết khi có 1 Exception xảy ra thì class này sẽ chịu trách nhiệm
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class) // Khai báo Exception chúng ta muốn bắt
    ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception){

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
            String enumKey = exception.getFieldError().getDefaultMessage();

            // Gán cho nó 1 Enum Error mặc định
            ErrorCode errorCode = ErrorCode.INVALID_KEY;


            // Bắt trường hợp Key enum không đúng
            try{
                errorCode = ErrorCode.valueOf(enumKey);
            }
            catch (IllegalArgumentException e) {
                // Xử lý lỗi tại đây
            }

            // Nếu không có lỗi xảy ra
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setCode(errorCode.getCode());
            apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }


}
