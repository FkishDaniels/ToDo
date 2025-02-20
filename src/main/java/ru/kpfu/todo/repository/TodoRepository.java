package ru.kpfu.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.todo.entity.Todo;

import java.util.List;

@Repository("todoJpaRepository")
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserList_Id(Long userId);
}
