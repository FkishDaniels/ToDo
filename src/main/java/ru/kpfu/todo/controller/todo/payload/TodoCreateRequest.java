package ru.kpfu.todo.controller.todo.payload;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import ru.kpfu.todo.enumiration.Priority;
import ru.kpfu.todo.enumiration.Status;

import java.time.LocalDate;

@Data
@ToString
public class TodoCreateRequest {
    private Long id;
    @NotBlank(message = "title shouldn't be a empty")
    private String title;
    @NotBlank(message = "description shouldn't be a empty")
    private String description;
    @Future(message = "due date should be in the future")
    private LocalDate dueDate;
    @NotNull(message = "priority shouldn't be null'")
    private Priority priority;
    @NotNull(message = "name shouldn;t be null")
    private String name;
}
