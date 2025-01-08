package ru.kpfu.todo.controller.cabinet.payload;

import lombok.Data;
import ru.kpfu.todo.enumiration.Priority;
import ru.kpfu.todo.enumiration.Status;

import java.time.LocalDateTime;

@Data
public class TodoResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime dueDate;
    private Priority priority;
    private Status status;
    private long daysUntilDue;
}
