package ru.kpfu.todo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.kpfu.todo.entity.ApplicationUser;
import ru.kpfu.todo.exception.not_found.UserNotFoundException;
import ru.kpfu.todo.repository.ApplicationUserRepository;

@Service
@RequiredArgsConstructor
public class UserUtilService {
    private final ApplicationUserRepository userRepository;

    public ApplicationUser findUserByAuthentication(Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
    }
}
