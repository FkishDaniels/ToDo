package ru.kpfu.todo.controller.authentication.payload;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}
