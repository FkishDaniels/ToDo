package ru.kpfu.todo.exception.not_found;

import lombok.EqualsAndHashCode;
import ru.kpfu.todo.exception.base.ApplicationNotFoundException;

@EqualsAndHashCode(callSuper = true)
public class TodoNotFoundException extends ApplicationNotFoundException {
    public TodoNotFoundException(Long id) {
        super("Todo not found", new Object[]{id});
    }
}