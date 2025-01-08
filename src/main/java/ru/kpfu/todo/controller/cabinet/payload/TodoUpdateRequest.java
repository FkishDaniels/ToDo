package ru.kpfu.todo.controller.cabinet.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.kpfu.todo.enumiration.Priority;

import java.time.LocalDateTime;

@Data
public class TodoUpdateRequest {
    private Long id;
    @NotBlank(message = "title shouldn't be a blank")
    private String title;
    @NotBlank(message = "description shouldn't be a blank")
    private String description;
    @NotNull(message = "due date shouldn't be null'")
    private LocalDateTime dueDate;
    @NotNull(message = "priority shouldn't be null'")
    private Priority priority;
    @NotBlank(message = "status shouldn't be a blank")
    private String status;
}
