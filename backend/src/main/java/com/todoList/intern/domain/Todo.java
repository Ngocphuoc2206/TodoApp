package com.todoList.intern.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status = TodoStatus.PENDING;

    @Column(name = "is_done", nullable = false)
    private boolean isDone = false;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        syncDoneFlag();
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
        syncDoneFlag();
    }

    private void syncDoneFlag() {
        isDone = status == TodoStatus.COMPLETED;
    }
}
