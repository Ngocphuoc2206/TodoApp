package com.todoList.intern.repository;

import com.todoList.intern.domain.Todo;
import com.todoList.intern.domain.TodoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("""
        SELECT t
        FROM Todo t
        WHERE (:status IS NULL OR t.status = :status)
            AND(
                :keyword IS NULL
                OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(COALESCE(t.description, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
    """)
    Page<Todo> search(
            @Param("keyword") String keyword,
            @Param("status")TodoStatus status,
            Pageable pageable
    );
}
