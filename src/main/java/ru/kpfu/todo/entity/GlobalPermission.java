package ru.kpfu.todo.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.kpfu.todo.enumiration.GlobalPermissionName;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "global_permission")
public class GlobalPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private GlobalPermissionName name;
}
