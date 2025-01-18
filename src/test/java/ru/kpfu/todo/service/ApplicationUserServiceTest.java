package ru.kpfu.todo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kpfu.todo.controller.authentication.payload.AuthenticationRequest;
import ru.kpfu.todo.controller.authentication.payload.RegisterRequest;
import ru.kpfu.todo.controller.cabinet.payload.UserResponse;
import ru.kpfu.todo.entity.ApplicationUser;
import ru.kpfu.todo.entity.GlobalPermission;
import ru.kpfu.todo.entity.Todo;
import ru.kpfu.todo.enumiration.GlobalPermissionName;
import ru.kpfu.todo.exception.alreadyExist.UserAlreadyExistsException;
import ru.kpfu.todo.repository.ApplicationUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationUserServiceTest {

    @Mock
    private ApplicationUserRepository applicationUserRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ApplicationUserService applicationUserService;



    @Test
    @DisplayName("Успешная регистрация пользователя")
    void registerUser_Success() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName("testUser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        when(applicationUserRepository.findByEmail(registerRequest.getEmail()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword()))
                .thenReturn("encodedPassword");

        applicationUserService.registerUser(registerRequest);

        verify(applicationUserRepository).save(any(ApplicationUser.class));
    }

    @Test
    @DisplayName("Ошибка регистрации: email уже существует")
    void registerUser_EmailAlreadyExists() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");

        when(applicationUserRepository.findByEmail(registerRequest.getEmail()))
                .thenReturn(Optional.of(new ApplicationUser()));

        assertThrows(UserAlreadyExistsException.class,
                () -> applicationUserService.registerUser(registerRequest));
    }

    @Test
    @DisplayName("Успешная аутентификация пользователя")
    void authenticate_Success() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test@example.com");
        authenticationRequest.setPassword("password");

        ApplicationUser mockUser = new ApplicationUser();
        mockUser.setEmail(authenticationRequest.getEmail());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(applicationUserRepository.findByEmail(authenticationRequest.getEmail()))
                .thenReturn(Optional.of(mockUser));

        ApplicationUser authenticatedUser = applicationUserService.authenticate(authenticationRequest);

        assertNotNull(authenticatedUser);
        assertEquals(authenticationRequest.getEmail(), authenticatedUser.getEmail());
    }

    @Test
    @DisplayName("Успешное обновление списка задач пользователя")
    void updateUserTaskList_Success() {
        Todo todo = new Todo();
        todo.setId(1L);

        Authentication mockAuthentication = mock(Authentication.class);
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("daniilka2003super@mail.ru");
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);

        ApplicationUser user = new ApplicationUser();
        user.setId(1L);
        user.setEmail("daniilka2003super@mail.ru");
        user.setTodoList(new ArrayList<>());

        GlobalPermission permission = new GlobalPermission(GlobalPermissionName.USER);
        user.setGlobalPermissions(List.of(permission));

        when(applicationUserRepository.findByEmail("daniilka2003super@mail.ru"))
                .thenReturn(Optional.of(user));
        when(applicationUserRepository.save(user)).thenReturn(user);

        UserResponse response = applicationUserService.updateUserTaskList(todo, mockAuthentication);

        assertNotNull(response);
        assertTrue(user.getTodoList().contains(todo));
        verify(applicationUserRepository).save(user);
    }


    @Test
    @DisplayName("Проверка: email уже существует")
    void checkEmail_EmailExists() {
        when(applicationUserRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(new ApplicationUser()));

        assertThrows(UserAlreadyExistsException.class,
                () -> applicationUserService.checkEmail("test@example.com"));
    }

    @Test
    @DisplayName("Проверка: email не существует")
    void checkEmail_EmailDoesNotExist() {
        when(applicationUserRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> applicationUserService.checkEmail("test@example.com"));
    }
}
