package ru.kpfu.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.todo.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
