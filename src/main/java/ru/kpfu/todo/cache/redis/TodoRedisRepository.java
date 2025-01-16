package ru.kpfu.todo.cache.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.todo.controller.todo.payload.TodoResponse;
import ru.kpfu.todo.entity.Todo;

@Repository("todoRedisRepository")
public interface TodoRedisRepository extends CrudRepository<TodoResponse, Long> {
}
