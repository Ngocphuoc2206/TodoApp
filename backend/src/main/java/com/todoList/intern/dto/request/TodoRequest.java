package com.todoList.intern.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoRequest(
        @NotBlank(message = "Tiêu đề không được để trống")
        @Size(max = 150, message = "Tiêu đề tối đa 150 ký tự")
        String title,

        @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
        String description
) {
}
