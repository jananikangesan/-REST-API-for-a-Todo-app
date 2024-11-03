package com.example.demo.service.impl;

import com.example.demo.model.TodoItem;
import com.example.demo.repository.TodoRepository;
import com.example.demo.service.TodoService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TodoServiceImpl implements TodoService {

  @Autowired
  private TodoRepository todoRepository;

  @Override
  public Optional<TodoItem> getTodoById(Long id) {
    return todoRepository.findById(id);
  }

  @Override
  public Page<TodoItem> getTodosByUser(Long userId, Pageable pageable) {
    return todoRepository.findByUserId(userId, pageable);
  }

  @Override
  public TodoItem save(TodoItem todoItem) {
    if(todoItem.getId() == null){
      todoItem.setCreatedAt(Instant.now());
    }
    todoItem.setUpdatedAt(Instant.now());
    return todoRepository.save(todoItem);
  }

  @Override
  public void delete(Long todoId) {
    todoRepository.deleteById(todoId);
  }

  @Override
  public List<TodoItem> search(String description, String priority, Boolean isComplete) {
    return todoRepository.search(description, priority, isComplete);
  }
}
