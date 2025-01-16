package ru.kpfu.todo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kpfu.todo.controller.todo.payload.TodoResponse;
import ru.kpfu.todo.controller.todo.payload.TodoRequest;
import ru.kpfu.todo.entity.ApplicationUser;
import ru.kpfu.todo.entity.Todo;
import ru.kpfu.todo.enumiration.Priority;
import ru.kpfu.todo.enumiration.Status;
import ru.kpfu.todo.repository.TodoRepository;
import ru.kpfu.todo.exception.notFound.TodoNotFoundException;
import ru.kpfu.todo.util.UserUtilService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserUtilService userUtilService;

    @InjectMocks
    private TodoService todoService;



    @Test
    @DisplayName("Успешное создание Todo")
    public void createTodo_Success() {
        var createRequest = new TodoRequest();

        createRequest.setDescription("test description");
        createRequest.setPriority(Priority.HIGH);
        createRequest.setName("test name");
        createRequest.setTitle("test title");
        createRequest.setDueDate(LocalDate.now());
        var todo = createTodo();

        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TodoResponse result = todoService.createTodo(createRequest);

        assertEquals("test title", result.getTitle());
        assertEquals("test description", result.getDescription());
        assertEquals(Priority.HIGH, result.getPriority());

        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    @DisplayName("Успешное обновление Todo")
    public void updateTodo_Success() {
        var todo = createTodo();
        var updateRequest = new TodoRequest();
        updateRequest.setDescription("Test Description");
        updateRequest.setTitle("Test Title");
        updateRequest.setId(1L);
        updateRequest.setStatus(Status.TODO);
        updateRequest.setPriority(Priority.HIGH);
        updateRequest.setDueDate(LocalDate.now());

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        todoService.updateTodo(updateRequest);

        assertEquals("Test Title", todo.getTitle());
        assertEquals("Test Description", todo.getDescription());
        assertEquals(Priority.HIGH, todo.getPriority());
        assertEquals(Status.TODO, todo.getStatus());

        verify(todoRepository).save(todo);
    }

    @Test
    @DisplayName("Ошибка при обновлении Todo с несуществующим ID")
    public void updateTodo_NotFound() {
        var updateRequest = new TodoRequest();
        updateRequest.setDescription("Test Description");
        updateRequest.setTitle("Test Title");
        updateRequest.setId(1L);
        updateRequest.setStatus(Status.TODO);
        updateRequest.setPriority(Priority.HIGH);
        updateRequest.setDueDate(LocalDate.now());

        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                TodoNotFoundException.class,
                () -> todoService.updateTodo(updateRequest)
        );

        verify(todoRepository).findById(1L);
    }

    @Test
    @DisplayName("Успешное удаление Todo")
    public void deleteTodo_Success() {
        var todo = createTodo();

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        todoService.delete(1L);

        verify(todoRepository).delete(todo);
    }

    @Test
    @DisplayName("Ошибка при удалении несуществующего Todo")
    public void deleteTodo_NotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                TodoNotFoundException.class,
                () -> todoService.delete(1L)
        );

        verify(todoRepository).findById(1L);
    }

    // Вспомогательные методы
    private Todo createTodo() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Task");
        todo.setDescription("Test Description");

        return todo;
    }

    private TodoResponse createTodoResponse() {
        return TodoResponse.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .build();
    }
    private ApplicationUser mockUser() {
        ApplicationUser user = new ApplicationUser();
        user.setId(1L);
        user.setUsername("Test User");
        return user;
    }
}
