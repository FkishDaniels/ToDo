package ru.kpfu.todo.entity;

import lombok.*;
import ru.kpfu.todo.enumiration.GlobalPermissionName;


@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class GlobalPermission {
    private Long id;
    private GlobalPermissionName name;
}
