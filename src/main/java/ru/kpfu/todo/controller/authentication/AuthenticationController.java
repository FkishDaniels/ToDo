package ru.kpfu.todo.controller.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.todo.controller.authentication.payload.AuthenticationRequest;
import ru.kpfu.todo.controller.authentication.payload.RegisterRequest;
import ru.kpfu.todo.exception.already_exist.UserAlreadyExistsException;
import ru.kpfu.todo.service.ApplicationUserService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {
    private final ApplicationUserService userService;

    @GetMapping("/login")
    public String loadLoginPage(Model model){
        model.addAttribute("authenticationRequest", new AuthenticationRequest());
        return "loginPage";
    }


    @GetMapping("/register")
    public String loadRegisterPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "registerPage";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute RegisterRequest request, Model model) {
        try {
            userService.registerUser(request);
            return "redirect:/authenticate/login"; // Успешная регистрация
        } catch (UserAlreadyExistsException ex) {
            model.addAttribute("error", ex.getMessage());
            return "registerPage"; // Ошибка регистрации
        }
    }

}
