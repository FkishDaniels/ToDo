package ru.kpfu.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.kpfu.todo.controller.todo.payload.TodoCreateRequest;
import ru.kpfu.todo.controller.todo.payload.TodoResponse;
import ru.kpfu.todo.controller.todo.payload.TodoUpdateRequest;
import ru.kpfu.todo.entity.ApplicationUser;
import ru.kpfu.todo.entity.Todo;
import ru.kpfu.todo.enumiration.Status;
import ru.kpfu.todo.exception.not_found.TodoNotFoundException;
import ru.kpfu.todo.repository.TodoRepository;
import ru.kpfu.todo.util.PageUtil;
import ru.kpfu.todo.util.UserUtilService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TodoService {


    private final TodoRepository todoRepository;
    private final UserUtilService userUtilService;
    private final ApplicationUserService applicationUserService;


    public List<TodoResponse> getFilteredAll( ApplicationUser user,
                                              String priority,
                                              String status,
                                              String sortBy ) {

        List<TodoResponse> filteredTodos = user.getTodoList()
                .stream()
                .map(this::toDto)
                .toList();

        if (priority != null && !priority.isEmpty()) {
            filteredTodos = filteredTodos.stream()
                    .filter(todo -> todo.getPriority().name().equalsIgnoreCase(priority))
                    .toList();
        }

        if (status != null && !status.isEmpty()) {
            filteredTodos = filteredTodos.stream()
                    .filter(todo -> todo.getStatus().name().equalsIgnoreCase(status))
                    .toList();
        }

        if ("dueDate".equalsIgnoreCase(sortBy)) {
            filteredTodos = filteredTodos.stream()
                    .sorted(Comparator.comparing(TodoResponse::getDueDate))
                    .toList();
        } else if ("status".equalsIgnoreCase(sortBy)) {
            Map<String, Integer> statusPriority = Map.of(
                    "TODO", 1,
                    "IN_PROGRESS", 2,
                    "COMPLETED", 3
            );
            filteredTodos = filteredTodos.stream()
                    .sorted(Comparator.comparing(todo -> statusPriority.get(todo.getStatus().name())))
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
        todo.setStatus(todoUpdateRequest.getStatus());
        todoRepository.save(todo);
    }


    public Page<TodoResponse> findAll(Integer pageNum,
                                      Integer pageSize,
                                      String sortStrategy) {


        Pageable pageable = PageUtil.toPageable(pageNum,pageSize,Sort.by("title"),sortStrategy);

        return todoRepository.findAll(pageable)
                .map(this::toDto);
    }

    public void takeTodo(Long id, Authentication authentication) {
        ApplicationUser user = userUtilService.findUserByAuthentication(authentication);
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new TodoNotFoundException(id)
        );
        applicationUserService.updateUserTaskList(todo,authentication);
    }

    public TodoResponse toDto(Todo todo) {
        TodoResponse dto = new TodoResponse();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setCreatedDate(todo.getCreatedDate());
        dto.setPriority(todo.getPriority());
        dto.setStatus(todo.getStatus());
        dto.setDueDate(todo.getDueDate());
        dto.setDaysUntilDue(ChronoUnit.DAYS.between(todo.getCreatedDate(), todo.getDueDate()));
        return dto;
    }

    public List<Long> getUserTasks(Authentication authentication) {
        var userId = userUtilService.findUserByAuthentication(authentication).getId();


        return todoRepository.findByUserList_Id(userId).stream()
                .map(Todo::getId)
                .collect(Collectors.toList());
    }

    public Page<TodoResponse> findAllAndCheckTaken(Integer pageNum, Integer pageSize, String sortStrategy, Authentication authentication) {
        var userTasksIds = getUserTasks(authentication);

        var todos = findAll(pageNum,pageSize,sortStrategy);

        todos.forEach(
                todo -> todo.setTaken(
                        userTasksIds.contains(todo.getId())
                )
        );

        return todos;
    }

    public TodoResponse createTodo(TodoCreateRequest todoCreateRequest) {
        var todo = mapToEntity(todoCreateRequest);
        var savedTodo = todoRepository.save(todo);

        return toDto(savedTodo);
    }


    private Todo mapToEntity(TodoCreateRequest todoCreateRequest) {
        Todo todo = new Todo();
        todo.setTitle(todoCreateRequest.getTitle());
        todo.setDescription(todoCreateRequest.getDescription());
        todo.setCreatedDate(LocalDateTime.now());
        todo.setPriority(todoCreateRequest.getPriority());
        todo.setStatus(Status.TODO);

        todo.setDueDate(todoCreateRequest.getDueDate().atStartOfDay());

        todo.setName(todoCreateRequest.getName());

        return todo;
    }

    public void delete(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new TodoNotFoundException(id)
        );

        todoRepository.delete(todo);
    }

}
