package ru.kpfu.todo.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.kpfu.todo.entity.Todo;
import ru.kpfu.todo.enumiration.Priority;
import ru.kpfu.todo.enumiration.Status;

import java.time.LocalDateTime;
import java.util.List;


@DataJpaTest
@Testcontainers
public class TodoRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("test_db")
                    .withUsername("test_user")
                    .withPassword("test_password")
                    .withInitScript("db/changelog/db.changelog-1.0.sql");

    @Autowired
    private TodoRepository todoRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgresContainer::getDriverClassName);
    }

    @Test
    @DisplayName("Вывод всех задач")
    void findAll() {
        Todo todo1 = new Todo();
        todo1.setTitle("Task 1");
        todo1.setDescription("Description 1");
        todo1.setName("name");
        todo1.setCreatedDate(LocalDateTime.now());
        todo1.setDueDate(LocalDateTime.now());
        todo1.setPriority(Priority.HIGH);
        todo1.setStatus(Status.TODO);

        todoRepository.save(todo1);

        Todo todo2 = new Todo();
        todo2.setTitle("Task 2");
        todo2.setDescription("Description 2");
        todo2.setStatus(Status.TODO);
        todo2.setPriority(Priority.HIGH);
        todo2.setName("name");
        todo2.setCreatedDate(LocalDateTime.now());
        todo2.setDueDate(LocalDateTime.now());

        todoRepository.save(todo2);

        List<Todo> result = todoRepository.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(4, result.size());
    }

    //Готовый пользователь с задачами уже лежит в бд
    @Test
    @DisplayName("Задачи по пользователю")
    void findByUserList_Status_ShouldReturnTodos() {
        List<Todo> result = todoRepository.findByUserList_Id(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }
}
