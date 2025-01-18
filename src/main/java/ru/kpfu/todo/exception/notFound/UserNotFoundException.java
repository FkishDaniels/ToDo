package ru.kpfu.todo.exception.notFound;

import lombok.EqualsAndHashCode;
import ru.kpfu.todo.exception.base.ApplicationNotFoundException;

@EqualsAndHashCode(callSuper = true)
public final class UserNotFoundException extends ApplicationNotFoundException {
    public UserNotFoundException(String email) {
        super("User with this email doesn't exist.", new Object[]{email});
    }
}
