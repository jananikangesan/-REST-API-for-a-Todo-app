package com.example.demo.service;

import com.example.demo.model.TodoItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoService {

  public Optional<TodoItem> getTodoById(Long id);
  public Page<TodoItem> getTodosByUser(Long userId, Pageable pageable);
  public TodoItem save(TodoItem todoItem);
  public void delete(Long todoId);
  public List<TodoItem> search(String description, String priority, Boolean isComplete);
}
