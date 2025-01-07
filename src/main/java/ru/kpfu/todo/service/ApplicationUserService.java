package ru.kpfu.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.todo.controller.authentication.payload.AuthenticationRequest;
import ru.kpfu.todo.controller.authentication.payload.RegisterRequest;
import ru.kpfu.todo.entity.ApplicationUser;
import ru.kpfu.todo.exception.already_exist.UserAlreadyExistsException;
import ru.kpfu.todo.repository.ApplicationUserRepository;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegisterRequest request) {
        checkEmail(request.getEmail());

        ApplicationUser newUser = new ApplicationUser();
        newUser.setUsername(request.getUserName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        applicationUserRepository.save(newUser);
    }
    public void authenticate(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
    }


    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void checkEmail(String email) {
        var applicationUser = applicationUserRepository.findByEmail(email);
        if (applicationUser.isPresent()) {
            throw new UserAlreadyExistsException(email);
        }
    }

}
