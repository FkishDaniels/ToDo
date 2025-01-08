package ru.kpfu.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.todo.controller.cabinet.payload.TodoResponse;
import ru.kpfu.todo.entity.ApplicationUser;
import ru.kpfu.todo.entity.Todo;
import ru.kpfu.todo.repository.TodoRepository;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;


    public TodoResponse toDto(Todo todo) {
        TodoResponse dto = new TodoResponse();
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setCreatedDate(todo.getCreatedDate());
        dto.setPriority(todo.getPriority());
        dto.setIsCompleted(todo.getIsCompleted() ? "Завершено" : "В процессе");
        dto.setDueDate(todo.getDueDate());
        dto.setDaysUntilDue(ChronoUnit.DAYS.between(todo.getCreatedDate(), todo.getDueDate()));
        return dto;
    }

    public List<TodoResponse> getFilteredAll(ApplicationUser user, String priority) {

        List<TodoResponse> filteredTodos = user.getTodoList()
                .stream()
                .map(this::toDto)
                .toList();

        if (priority != null && !priority.isEmpty()) {
            filteredTodos = filteredTodos.stream()
                    .filter(todo -> todo.getPriority().name().equalsIgnoreCase(priority))
                    .toList();
        }

        return filteredTodos;
    }
}
