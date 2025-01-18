package ru.kpfu.todo.controller.todo.payload;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import ru.kpfu.todo.enumiration.Priority;
import ru.kpfu.todo.enumiration.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
public class TodoRequest {
    private Long id;
    @NotBlank(message = "title shouldn't be a empty")
    private String title;
    @NotBlank(message = "description shouldn't be a empty")
    private String description;
    @Future(message = "due date should be in the future")
    private LocalDate dueDate;
    @NotNull(message = "priority shouldn't be null'")
    private Priority priority;
    @NotNull(message = "status shouldn't be a blank")
    private Status status;
    @NotBlank(message = "Name shouldn't be a blank")
    private String name;
}
