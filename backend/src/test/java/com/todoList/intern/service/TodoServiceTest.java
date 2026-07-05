package com.todoList.intern.service;

import com.todoList.intern.domain.Todo;
import com.todoList.intern.domain.TodoStatus;
import com.todoList.intern.dto.request.TodoRequest;
import com.todoList.intern.dto.response.TodoResponse;
import com.todoList.intern.exception.AppException;
import com.todoList.intern.exception.ErrorCode;
import com.todoList.intern.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void getTodoById_shouldReturnTodo_whenTodoExists() {
        Todo todo = todo(1L, "Learn unit test", "Write service test", TodoStatus.PENDING);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        TodoResponse response = todoService.getTodoById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Learn unit test");
        assertThat(response.status()).isEqualTo(TodoStatus.PENDING);
    }

    @Test
    void getTodoById_shouldThrowTodoNotFound_whenTodoDoesNotExist() {
        when(todoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.getTodoById(99L))
                .isInstanceOf(AppException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.TODO_NOT_FOUND);
    }

    @Test
    void createTodo_shouldCreateTodoWithPendingStatus() {
        TodoRequest request = new TodoRequest(" New todo ", " Description ");
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> {
            Todo todo = invocation.getArgument(0);
            todo.setId(1L);
            todo.setCreatedAt(Instant.now());
            todo.setUpdatedAt(Instant.now());
            return todo;
        });

        TodoResponse response = todoService.createTodo(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("New todo");
        assertThat(response.description()).isEqualTo("Description");
        assertThat(response.status()).isEqualTo(TodoStatus.PENDING);
    }

    @Test
    void updateTodo_shouldUpdateTitleAndDescription_whenTodoExists() {
        Todo existingTodo = todo(1L, "Old title", "Old description", TodoStatus.PENDING);
        TodoRequest request = new TodoRequest(" Updated title ", " Updated description ");
        when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TodoResponse response = todoService.updateTodo(1L, request);

        assertThat(response.title()).isEqualTo("Updated title");
        assertThat(response.description()).isEqualTo("Updated description");
        assertThat(response.status()).isEqualTo(TodoStatus.PENDING);
    }

    @Test
    void toggleTodoStatus_shouldChangePendingToCompleted() {
        Todo todo = todo(1L, "Test", null, TodoStatus.PENDING);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TodoResponse response = todoService.toggleTodoStatus(1L);

        assertThat(response.status()).isEqualTo(TodoStatus.COMPLETED);
    }

    @Test
    void toggleTodoStatus_shouldChangeCompletedToPending() {
        Todo todo = todo(1L, "Test", null, TodoStatus.COMPLETED);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TodoResponse response = todoService.toggleTodoStatus(1L);

        assertThat(response.status()).isEqualTo(TodoStatus.PENDING);
    }

    @Test
    void deleteTodo_shouldDeleteTodo_whenTodoExists() {
        Todo todo = todo(1L, "Delete me", null, TodoStatus.PENDING);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        todoService.deleteTodo(1L);

        verify(todoRepository).delete(todo);
    }

    @Test
    void getTodos_shouldThrowInvalidPage_whenPageIsNegative() {
        assertThatThrownBy(() -> todoService.getTodos(null, null, -1, 10, "createdAt,desc"))
                .isInstanceOf(AppException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_PAGE);
    }

    @Test
    void getTodos_shouldThrowInvalidSize_whenSizeIsOutOfRange() {
        assertThatThrownBy(() -> todoService.getTodos(null, null, 0, 101, "createdAt,desc"))
                .isInstanceOf(AppException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_SIZE);
    }

    @Test
    void getTodos_shouldThrowInvalidSortFormat_whenSortHasTooManyParts() {
        assertThatThrownBy(() -> todoService.getTodos(null, null, 0, 10, "createdAt,desc,extra"))
                .isInstanceOf(AppException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_SORT_FORMAT);
    }

    @Test
    void getTodos_shouldThrowInvalidSortField_whenSortFieldIsNotAllowed() {
        assertThatThrownBy(() -> todoService.getTodos(null, null, 0, 10, "unknown,desc"))
                .isInstanceOf(AppException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_SORT_FIELD);
    }

    @Test
    void getTodos_shouldThrowInvalidSortDirection_whenSortDirectionIsNotAllowed() {
        assertThatThrownBy(() -> todoService.getTodos(null, null, 0, 10, "createdAt,wrong"))
                .isInstanceOf(AppException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_SORT_DIRECTION);
    }

    private Todo todo(Long id, String title, String description, TodoStatus status) {
        Todo todo = new Todo();
        todo.setId(id);
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setStatus(status);
        todo.setCreatedAt(Instant.now());
        todo.setUpdatedAt(Instant.now());
        return todo;
    }
}
