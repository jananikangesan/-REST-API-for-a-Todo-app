package com.example.demo.repository;

import com.example.demo.model.TodoItem;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository  extends JpaRepository<TodoItem,Long> {

  Page<TodoItem> findByUserId(Long userId, Pageable pageable);

  @Query("SELECT t FROM TodoItem t WHERE " +
      "(:description IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
      "(:priority IS NULL OR t.priority = :priority) AND " +
      "(:isComplete IS NULL OR t.isComplete = :isComplete)")
  List<TodoItem> search(@Param("description") String description,
      @Param("priority") String priority,
      @Param("isComplete") Boolean isComplete);

}
