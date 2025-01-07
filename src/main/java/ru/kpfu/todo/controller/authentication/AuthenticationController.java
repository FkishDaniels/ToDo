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

    @PostMapping("/login")
    public String authenticateUser(@ModelAttribute AuthenticationRequest authenticationRequest,
                                   Model model){
        try {
            userService.authenticate(authenticationRequest);
            return "redirect:/home";
        }catch (BadCredentialsException e){
            // Добавление ошибки в модель для отображения на странице
            model.addAttribute("error", "Неверный email или пароль");
            return "loginPage"; // Возврат к странице логина
        }
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
