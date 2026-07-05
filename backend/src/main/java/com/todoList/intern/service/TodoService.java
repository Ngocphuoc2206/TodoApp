package com.todoList.intern.service;

import com.todoList.intern.domain.Todo;
import com.todoList.intern.domain.TodoStatus;
import com.todoList.intern.dto.request.TodoRequest;
import com.todoList.intern.dto.response.PageResponse;
import com.todoList.intern.dto.response.TodoResponse;
import com.todoList.intern.exception.AppException;
import com.todoList.intern.exception.ErrorCode;
import com.todoList.intern.exception.ResourceNotFoundException;
import com.todoList.intern.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TodoService {
    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("id", "title", "status", "createdAt", "updatedAt");

    private final TodoRepository todoRepository;

    @Transactional(readOnly = true)
    public PageResponse<TodoResponse> getTodos(String keyword, TodoStatus todoStatus, int page, int size, String sort) {
        if (page < 0) {
            throw new AppException(ErrorCode.INVALID_PAGE);
        }

        if (size < 1 || size > 100) {
            throw new AppException(ErrorCode.INVALID_SIZE);
        }

        String normalizedKeyword = normalizeKeyword(keyword);
        Sort sorting = buildSort(sort);

        Page<TodoResponse> todoPage = todoRepository
                .search(normalizedKeyword, todoStatus, PageRequest.of(page, size, sorting))
                .map(this::toResponse);

        return PageResponse.from(todoPage);
    }

    @Transactional(readOnly = true)
    public TodoResponse getTodoById(Long id) {
        return toResponse(findTodoById(id));
    }

    @Transactional
    public TodoResponse createTodo(TodoRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.title().strip());
        todo.setDescription(normalizeDescription(request.description()));
        todo.setStatus(TodoStatus.PENDING);

        Todo savedTodo = todoRepository.save(todo);
        return toResponse(savedTodo);
    }

    @Transactional
    public TodoResponse updateTodo(Long id, TodoRequest request) {
        Todo todo = findTodoById(id);

        todo.setTitle(request.title().strip());
        todo.setDescription(normalizeDescription(request.description()));

        Todo updatedTodo = todoRepository.save(todo);
        return toResponse(updatedTodo);
    }

    @Transactional
    public TodoResponse toggleTodoStatus(Long id) {
        Todo todo = findTodoById(id);

        if (todo.getStatus() == TodoStatus.PENDING) {
            todo.setStatus(TodoStatus.COMPLETED);
        } else {
            todo.setStatus(TodoStatus.PENDING);
        }

        Todo updatedTodo = todoRepository.save(todo);
        return toResponse(updatedTodo);
    }

    @Transactional
    public void deleteTodo(Long id) {
        Todo todo = findTodoById(id);
        todoRepository.delete(todo);
    }

    private Todo findTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorCode.TODO_NOT_FOUND,
                                ErrorCode.TODO_NOT_FOUND.getMessage() + " có id = " + id
                        )
                );
    }

    private TodoResponse toResponse(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getStatus(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return keyword.strip();
    }

    private String normalizeDescription(String description) {
        if (description == null || description.isBlank()) {
            return null;
        }

        return description.strip();
    }

    private Sort buildSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        String[] parts = sort.split(",");
        if (parts.length > 2) {
            throw new AppException(ErrorCode.INVALID_SORT_FORMAT);
        }

        String field = parts[0].strip();
        if (!ALLOWED_SORT_FIELDS.contains(field)) {
            String message = ErrorCode.INVALID_SORT_FIELD.getMessage()
                    + ". Chỉ hỗ trợ: " + ALLOWED_SORT_FIELDS;
            throw new AppException(ErrorCode.INVALID_SORT_FIELD, message);
        }

        Sort.Direction direction = Sort.Direction.DESC;

        if (parts.length > 1) {
            try {
                direction = Sort.Direction.fromString(parts[1].strip());
            } catch (IllegalArgumentException exception) {
                throw new AppException(ErrorCode.INVALID_SORT_DIRECTION);
            }
        }

        return Sort.by(direction, field);
    }
}
