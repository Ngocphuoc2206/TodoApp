package com.todoList.intern.exception;

public class ResourceNotFoundException extends AppException {
    public ResourceNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
