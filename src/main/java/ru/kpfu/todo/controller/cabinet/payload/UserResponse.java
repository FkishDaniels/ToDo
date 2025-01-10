package ru.kpfu.todo.controller.cabinet.payload;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private List<String> permissions;
    private List<Long> todoIds;
}
