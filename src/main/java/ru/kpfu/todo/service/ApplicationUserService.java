package ru.kpfu.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.todo.controller.authentication.payload.AuthenticationRequest;
import ru.kpfu.todo.controller.authentication.payload.RegisterRequest;
import ru.kpfu.todo.controller.cabinet.payload.UserResponse;
import ru.kpfu.todo.controller.todo.payload.TodoResponse;
import ru.kpfu.todo.entity.ApplicationUser;
import ru.kpfu.todo.entity.Todo;
import ru.kpfu.todo.exception.already_exist.UserAlreadyExistsException;
import ru.kpfu.todo.repository.ApplicationUserRepository;
import ru.kpfu.todo.util.UserUtilService;

import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserUtilService userUtilService;

    @Transactional
    public void registerUser(RegisterRequest request) {
        checkEmail(request.getEmail());

        ApplicationUser newUser = new ApplicationUser();
        newUser.setUsername(request.getUserName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        applicationUserRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public ApplicationUser authenticate(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var user = applicationUserRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow();

        return user;
    }


    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Transactional(readOnly = true)
    public void checkEmail(String email) {
        var applicationUser = applicationUserRepository.findByEmail(email);
        if (applicationUser.isPresent()) {
            throw new UserAlreadyExistsException(email);
        }
    }

    @Transactional
    public UserResponse updateUserTaskList(Todo todo, Authentication authentication) {
        var userToUpdate = userUtilService.findUserByAuthentication(authentication);

        userToUpdate.getTodoList().add(todo);
        return toDto(applicationUserRepository.save(userToUpdate));
    }

    public UserResponse toDto(ApplicationUser applicationUser) {
        return UserResponse.builder()
                .id(applicationUser.getId())
                .username(applicationUser.getUsername())
                .email(applicationUser.getEmail())
                .permissions(applicationUser.getGlobalPermissions()
                        .stream()
                        .map(permission -> permission.getName().name())
                        .collect(Collectors.toList()))
                .todoIds(applicationUser.getTodoList()
                        .stream()
                        .map(Todo::getId)
                        .collect(Collectors.toList()))
                .build();
    }
}
