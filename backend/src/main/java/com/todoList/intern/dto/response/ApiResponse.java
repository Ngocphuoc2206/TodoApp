package com.todoList.intern.dto.response;

import com.todoList.intern.exception.ErrorCode;

import java.time.Instant;
import java.util.Map;

public record ApiResponse<T>(
        boolean success,
        String code,
        String message,
        T data,
        Map<String, String> errors,
        Instant timestamp
) {
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                true,
                ErrorCode.SUCCESS.getCode(),
                message,
                data,
                Map.of(),
                Instant.now()
        );
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, Map<String, String> errors) {
        return error(errorCode, errorCode.getMessage(), errors);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message, Map<String, String> errors) {
        return new ApiResponse<>(
                false,
                errorCode.getCode(),
                message,
                null,
                errors,
                Instant.now()
        );
    }
}
