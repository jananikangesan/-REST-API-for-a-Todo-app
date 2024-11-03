package com.example.demo.controller;

import com.example.demo.model.TodoItem;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.JwtService;
import com.example.demo.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todos")
public class TodoController {

  @Autowired
  private TodoService todoService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtService jwtService;

  private Logger logger = LoggerFactory.getLogger(TodoController.class);

  @PostMapping
  public ResponseEntity<TodoItem> createTodo(@RequestBody TodoItem todoItem,
      HttpServletRequest request) {

    logger.info("Received request to create Todo item: {}", todoItem);

    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      logger.warn("Unauthorized attempt to create Todo item");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    String token = authHeader.substring(7);

    String userEmail = jwtService.extractEmail(token);

    Optional<User> user = userRepository.findByEmail(userEmail);

    if (user.isEmpty()) {
      logger.warn("User not found for email: {}", userEmail);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    todoItem.setUser(user.get());
    TodoItem savedTodo = todoService.save(todoItem);

    logger.info("Successfully created Todo item: {}", savedTodo);

    return ResponseEntity.ok(savedTodo);
  }

  @GetMapping
  public ResponseEntity<Page<TodoItem>> getTodos(
      @RequestParam(required = false, defaultValue = "id") String sortBy,
      @RequestParam(required = false, defaultValue = "asc") String sortOrder,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      HttpServletRequest request) {

    logger.info("Fetching all todos for the user, sorted by: {} in {} order", sortBy, sortOrder);

    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      logger.warn("Unauthorized attempt to fetch Todo item");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    String token = authHeader.substring(7);
    String userEmail = jwtService.extractEmail(token);
    Optional<User> user = userRepository.findByEmail(userEmail);

    if (user.isEmpty()) {
      logger.warn("User not found for email: {}", userEmail);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
    Pageable pageableRequest = PageRequest.of(page, size, sort);

    Page<TodoItem> todos = todoService.getTodosByUser(user.get().getId(), pageableRequest);
    logger.info("Fetched {} todos for user: {}", todos.getContent().size(), user.get().getEmail());
    return ResponseEntity.ok(todos);
  }

  @PutMapping("/edit/{id}")
  public ResponseEntity<TodoItem> updateTodo(@PathVariable Long id,
      @RequestBody TodoItem todoDetails) {

    logger.info("Received request to update Todo item with ID: {}", id);

    Optional<TodoItem> todoItemOptional = todoService.getTodoById(id);
    if (todoItemOptional.isPresent()) {
      TodoItem todoItem = todoItemOptional.get();
      todoItem.setDescription(todoDetails.getDescription());
      todoItem.setIsComplete(todoDetails.getIsComplete());

      TodoItem updatedTodo = todoService.save(todoItem);
      logger.info("Successfully updated Todo item: {}", updatedTodo);
      return ResponseEntity.ok(updatedTodo);
    }
    logger.warn("Todo item with ID: {} not found", id);
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteTodo(@PathVariable Long id) {
    logger.info("Received request to delete Todo item with ID: {}", id);

    todoService.delete(id);

    logger.info("Todo item with ID: {} deleted successfully", id);
    return ResponseEntity.ok("Todo item deleted successfully");
  }


  @GetMapping("/search")
  public ResponseEntity<List<TodoItem>> searchTodoItems(
      @RequestParam(required = false) String description,
      @RequestParam(required = false) String priority,
      @RequestParam(required = false) Boolean isComplete) {

    logger.info("Searching Todo items with filters - Description: {}, Priority: {}, IsComplete: {}",
        description, priority, isComplete);

    List<TodoItem> items = todoService.search(description, priority, isComplete);

    logger.info("Found {} Todo items matching the search criteria", items.size());

    return ResponseEntity.ok(items);
  }
}

