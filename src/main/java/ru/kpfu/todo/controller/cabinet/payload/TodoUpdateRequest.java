package ru.kpfu.todo.controller.cabinet.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import ru.kpfu.todo.enumiration.Priority;
import ru.kpfu.todo.enumiration.Status;

import java.time.LocalDateTime;

@Data
@ToString
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
    @NotNull(message = "status shouldn't be a blank")
    private Status status;
}
