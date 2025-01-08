package ru.kpfu.todo.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.kpfu.todo.enumiration.Priority;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "todo")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @ManyToMany(mappedBy = "todoList")
    private List<ApplicationUser> userList;
}
