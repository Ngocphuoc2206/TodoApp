package com.todoList.intern.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    SUCCESS(HttpStatus.OK, "SUCCESS", "Thành công"),
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO_NOT_FOUND", "Không tìm thấy công việc"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "Dữ liệu đầu vào không hợp lệ"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "Yêu cầu không hợp lệ"),
    INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "INVALID_REQUEST_PARAMETER", "Tham số không hợp lệ"),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "INVALID_JSON", "JSON gửi lên không hợp lệ"),
    INVALID_PAGE(HttpStatus.BAD_REQUEST, "INVALID_PAGE", "Page phải lớn hơn hoặc bằng 0"),
    INVALID_SIZE(HttpStatus.BAD_REQUEST, "INVALID_SIZE", "Size phải nằm trong khoảng từ 1 đến 100"),
    INVALID_SORT_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_SORT_FORMAT", "Sort phải có định dạng field,direction"),
    INVALID_SORT_FIELD(HttpStatus.BAD_REQUEST, "INVALID_SORT_FIELD", "Trường sort không hợp lệ"),
    INVALID_SORT_DIRECTION(HttpStatus.BAD_REQUEST, "INVALID_SORT_DIRECTION", "Hướng sort chỉ có thể là asc hoặc desc"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Đã xảy ra lỗi hệ thống");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
