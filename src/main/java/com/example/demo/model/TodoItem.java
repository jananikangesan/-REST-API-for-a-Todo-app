package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "todo_items")
@NoArgsConstructor
public class TodoItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String description;
  private Boolean isComplete = false;
  private Instant dueDate;
  private String priority;

  @ManyToOne(fetch = FetchType.EAGER)
  private User user;

  private Instant createdAt;
  private Instant updatedAt;

  @Override
  public String toString() {
    return "TodoItem{" +
        "id=" + id +
        ", description='" + description + '\'' +
        ", isComplete=" + isComplete +
        ", dueDate=" + dueDate +
        ", priority='" + priority + '\'' +
        ", user=" + user +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
  }
}
