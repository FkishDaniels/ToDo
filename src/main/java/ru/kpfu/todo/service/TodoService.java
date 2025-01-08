package ru.kpfu.todo.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.todo.controller.cabinet.payload.TodoResponse;
import ru.kpfu.todo.controller.cabinet.payload.TodoUpdateRequest;
import ru.kpfu.todo.entity.ApplicationUser;
import ru.kpfu.todo.entity.Todo;
import ru.kpfu.todo.enumiration.Status;
import ru.kpfu.todo.repository.TodoRepository;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final static  Map<String,String> STATUS_MAPPING = Map.of(
            "TODO", "К выполнению",
            "COMPLETED", "Выполнена",
            "IN_PROGRESS", "В процессе"
    );
    private static final Map<String, String> REVERSE_STATUS_MAPPING = STATUS_MAPPING.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

    private final TodoRepository todoRepository;


    public TodoResponse toDto(Todo todo) {


        TodoResponse dto = new TodoResponse();
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setCreatedDate(todo.getCreatedDate());
        dto.setPriority(todo.getPriority());
        dto.setStatus(STATUS_MAPPING.getOrDefault(todo.getStatus().name(),"Неизвестный статус"));
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

    public void updateTodo(TodoUpdateRequest todoUpdateRequest) {
        Todo todo = todoRepository.findById(todoUpdateRequest.getId()).orElseThrow();
        todo.setTitle(todoUpdateRequest.getTitle());
        todo.setDescription(todoUpdateRequest.getDescription());
        todo.setDueDate(todoUpdateRequest.getDueDate());
        todo.setPriority(todoUpdateRequest.getPriority());
        todo.setStatus(Status.valueOf(REVERSE_STATUS_MAPPING.get(todoUpdateRequest.getStatus())));
        todoRepository.save(todo);
    }
}
