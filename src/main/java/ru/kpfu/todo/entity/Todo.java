package ru.kpfu.todo.entity;

import lombok.*;
import ru.kpfu.todo.enumiration.Priority;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Todo {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime dueDate;
    private Boolean isCompleted;
    private Priority name;
    private List<ApplicationUser> userList;
}
