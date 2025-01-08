package ru.kpfu.todo.controller.cabinet.payload;

import lombok.Data;
import ru.kpfu.todo.enumiration.Priority;

import java.time.LocalDateTime;

@Data
public class TodoResponse {
    private String title;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime dueDate;
    private Priority priority;
    private String isCompleted;
    private long daysUntilDue;
}
