package com.todoList.intern.controller;

import com.todoList.intern.domain.TodoStatus;
import com.todoList.intern.dto.response.TodoResponse;
import com.todoList.intern.exception.GlobalExceptionHandler;
import com.todoList.intern.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {
    @Mock
    private TodoService todoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        TodoController todoController = new TodoController(todoService);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(todoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void getTodoById_shouldReturnApiResponse() throws Exception {
        TodoResponse response = new TodoResponse(
                1L,
                "Learn controller test",
                "MockMvc response",
                TodoStatus.PENDING,
                Instant.now(),
                Instant.now()
        );
        when(todoService.getTodoById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Learn controller test"));
    }

    @Test
    void createTodo_shouldReturnCreatedApiResponse() throws Exception {
        TodoResponse response = new TodoResponse(
                1L,
                "New todo",
                "Created from API",
                TodoStatus.PENDING,
                Instant.now(),
                Instant.now()
        );
        when(todoService.createTodo(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "New todo",
                                  "description": "Created from API"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data.status").value("PENDING"));
    }

    @Test
    void createTodo_shouldReturnValidationFailed_whenTitleIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "",
                                  "description": "Missing title"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.errors.title").value("Tiêu đề không được để trống"));
    }
}
