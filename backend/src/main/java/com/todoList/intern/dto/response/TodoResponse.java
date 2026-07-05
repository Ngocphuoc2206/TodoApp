package com.todoList.intern.dto.response;

import com.todoList.intern.domain.TodoStatus;

import java.time.Instant;

public record TodoResponse(
        Long id,
        String title,
        String description,
        TodoStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
