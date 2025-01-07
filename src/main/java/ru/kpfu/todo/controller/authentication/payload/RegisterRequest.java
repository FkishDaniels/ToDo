package ru.kpfu.todo.controller.authentication.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
        @Pattern(message = "email is invalid", regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        @NotBlank(message = "email shouldn't be a blank")
        private String email;

        @NotBlank(message = "username shouldn't be a blank")
        @Size(max = 50, message = "max size is 50")
        private String userName;

        @Size(min = 8, max = 32, message = "invalid size of password min = 8 max = 32")
        @NotBlank(message = "password shouldn't be a blank")
        private String password;
}
