package ru.kpfu.todo.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.todo.controller.todo.payload.TodoResponse;
import ru.kpfu.todo.controller.todo.payload.TodoRequest;
import ru.kpfu.todo.entity.ApplicationUser;
import ru.kpfu.todo.entity.Todo;
import ru.kpfu.todo.enumiration.SortStrategy;
import ru.kpfu.todo.enumiration.Status;
import ru.kpfu.todo.exception.notFound.TodoNotFoundException;
import ru.kpfu.todo.repository.TodoRepository;
import ru.kpfu.todo.util.PageUtil;
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
    private final ApplicationUserService applicationUserService;

    @Transactional(readOnly = true)
    public List<TodoResponse> getFilteredAll( ApplicationUser user,
                                              String priority,
                                              String status,
                                              SortStrategy sortBy ) {

        List<TodoResponse> filteredTodos = user.getTodoList()
                .stream()
                .map(this::toDto)
                .toList();

        if (StringUtils.isNotEmpty(priority)) {
            filteredTodos = filteredTodos.stream()
                    .filter(todo -> todo.getPriority().name().equalsIgnoreCase(priority))
                    .toList();
        }

        if (StringUtils.isNotEmpty(status)) {
            filteredTodos = filteredTodos.stream()
                    .filter(todo -> todo.getStatus().name().equalsIgnoreCase(status))
                    .toList();
        }
        if(sortBy != null) {
            filteredTodos = switch (sortBy) {

                case DueDate -> filteredTodos.stream()
                        .sorted(Comparator.comparing(TodoResponse::getDueDate))
                        .toList();
                case Status -> {
                    Map<String, Integer> statusPriority = Map.of(
                            "TODO", 1,
                            "IN_PROGRESS", 2,
                            "COMPLETED", 3
                    );
                    yield filteredTodos.stream()
                            .sorted(Comparator.comparing(todo -> statusPriority.get(todo.getStatus().name())))
                            .toList();
                }
                default -> throw new IllegalStateException("Unexpected value: " + sortBy);
            };

        }

        return filteredTodos;
    }

    @Transactional
    public void updateTodo(TodoRequest todoRequest) {
        Todo todo = todoRepository.findById(todoRequest.getId()).orElseThrow(
                () -> new TodoNotFoundException(todoRequest.getId())
        );
        todo.setTitle(todoRequest.getTitle());
        todo.setDescription(todoRequest.getDescription());
        todo.setDueDate(todoRequest.getDueDate().atStartOfDay());
        todo.setPriority(todoRequest.getPriority());
        todo.setStatus(todoRequest.getStatus());
        todoRepository.save(todo);
    }


    @Transactional(readOnly = true)
    public Page<TodoResponse> findAll(Integer pageNum,
                                      Integer pageSize,
                                      String sortStrategy) {


        Pageable pageable = PageUtil.toPageable(pageNum,pageSize,Sort.by("title"),sortStrategy);

        return todoRepository.findAll(pageable)
                .map(this::toDto);
    }

    @Transactional()
    public void takeTodo(Long id, Authentication authentication) {
        ApplicationUser user = applicationUserService.findUserByAuthentication(authentication);
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

    @Transactional(readOnly = true)
    public List<Long> getUserTasks(Authentication authentication) {
        var userId = applicationUserService.findUserByAuthentication(authentication).getId();


        return todoRepository.findByUserList_Id(userId).stream()
                .map(Todo::getId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "todo", key = "#id")
    public TodoResponse findById(Long id){
        return toDto(todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(id)));
    }


    @Transactional(readOnly = true)
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

    @Transactional
    public TodoResponse createTodo(TodoRequest todoCreateRequest) {
        var todo = mapToEntity(todoCreateRequest);
        var savedTodo = todoRepository.save(todo);

        return toDto(savedTodo);
    }


    private Todo mapToEntity(TodoRequest todoCreateRequest) {
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

    @Transactional
    public void delete(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new TodoNotFoundException(id)
        );

        todoRepository.delete(todo);
    }

}
