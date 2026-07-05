package com.todoList.intern.controller;

import com.todoList.intern.domain.TodoStatus;
import com.todoList.intern.dto.request.TodoRequest;
import com.todoList.intern.dto.response.ApiResponse;
import com.todoList.intern.dto.response.PageResponse;
import com.todoList.intern.dto.response.TodoResponse;
import com.todoList.intern.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TodoResponse>>> getTodos(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) TodoStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        PageResponse<TodoResponse> response = todoService.getTodos(keyword, status, page, size, sort);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách công việc thành công", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoResponse>> getTodoById(@PathVariable Long id) {
        TodoResponse response = todoService.getTodoById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy công việc thành công", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TodoResponse>> createTodo(
            @Valid @RequestBody TodoRequest request
    ) {
        TodoResponse response = todoService.createTodo(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo công việc thành công", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoResponse>> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoRequest request
    ) {
        TodoResponse response = todoService.updateTodo(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật công việc thành công", response));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<TodoResponse>> toggleTodoStatus(@PathVariable Long id) {
        TodoResponse response = todoService.toggleTodoStatus(id);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái công việc thành công", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa công việc thành công", null));
    }
}
