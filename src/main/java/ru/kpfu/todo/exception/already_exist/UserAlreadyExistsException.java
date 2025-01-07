package ru.kpfu.todo.exception.already_exist;

import lombok.EqualsAndHashCode;
import ru.kpfu.todo.exception.base.ApplicationConflictException;

@EqualsAndHashCode(callSuper = true)
public final class UserAlreadyExistsException extends ApplicationConflictException {

    public UserAlreadyExistsException(String email) {
        super("User with this email already registered", new Object[]{email});
    }
}