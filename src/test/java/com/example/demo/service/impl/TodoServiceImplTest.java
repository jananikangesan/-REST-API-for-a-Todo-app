package com.example.demo.service.impl;

import com.example.demo.model.TodoItem;
import com.example.demo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TodoServiceImplTest {

  @Mock
  private TodoRepository todoRepository;

  @InjectMocks
  private TodoServiceImpl todoService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getTodoById() {
    TodoItem todoItem = new TodoItem();
    todoItem.setId(1L);
    when(todoRepository.findById(1L)).thenReturn(Optional.of(todoItem));

    Optional<TodoItem> result = todoService.getTodoById(1L);

    assertTrue(result.isPresent());
    assertEquals(1L, result.get().getId());
  }

  @Test
  void save() {
    TodoItem todoItem = new TodoItem();
    todoItem.setId(1L);
    when(todoRepository.save(any(TodoItem.class))).thenReturn(todoItem);

    TodoItem result = todoService.save(todoItem);

    assertNotNull(result);
    assertNotNull(todoItem.getUpdatedAt());
    verify(todoRepository, times(1)).save(todoItem);
  }

  @Test
  void delete() {
    doNothing().when(todoRepository).deleteById(1L);

    todoService.delete(1L);

    verify(todoRepository, times(1)).deleteById(1L);
  }

  @Test
  void search() {
    TodoItem todoItem1 = new TodoItem();
    todoItem1.setDescription("Sample description");
    todoItem1.setPriority("High");
    todoItem1.setIsComplete(true);

    List<TodoItem> todos = Arrays.asList(todoItem1);
    when(todoRepository.search("description", "High", true)).thenReturn(todos);

    List<TodoItem> result = todoService.search("description", "High", true);

    assertEquals(1, result.size());
    assertEquals("Sample description", result.get(0).getDescription());
  }
}
