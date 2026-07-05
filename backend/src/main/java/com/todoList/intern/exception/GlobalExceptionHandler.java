package com.todoList.intern.exception;

import com.todoList.intern.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return buildResponse(errorCode, exception.getMessage(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return buildResponse(
                ErrorCode.VALIDATION_FAILED,
                ErrorCode.VALIDATION_FAILED.getMessage(),
                errors
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        String message = "Giá trị '" + exception.getValue()
                + "' không hợp lệ cho tham số '" + exception.getName() + "'";

        return buildResponse(ErrorCode.INVALID_REQUEST_PARAMETER, message, Map.of());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidJson(HttpMessageNotReadableException exception) {
        return buildResponse(
                ErrorCode.INVALID_JSON,
                ErrorCode.INVALID_JSON.getMessage(),
                Map.of()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(IllegalArgumentException exception) {
        return buildResponse(
                ErrorCode.INVALID_REQUEST,
                exception.getMessage(),
                Map.of()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception exception) {
        return buildResponse(
                ErrorCode.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                Map.of()
        );
    }

    private ResponseEntity<ApiResponse<Void>> buildResponse(
            ErrorCode errorCode,
            String message,
            Map<String, String> errors
    ) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode, message, errors));
    }
}
