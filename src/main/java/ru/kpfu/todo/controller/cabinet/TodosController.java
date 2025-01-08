package ru.kpfu.todo.controller.cabinet;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.todo.controller.cabinet.payload.TodoUpdateRequest;
import ru.kpfu.todo.service.TodoService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodosController {
    private final TodoService todoService;

    @GetMapping
    public String todos(Model model) {

        return "todos";
    }

    @PutMapping("/edit")
    @ResponseBody
    public TodoUpdateRequest editTodo(@RequestBody @Valid TodoUpdateRequest todoUpdateRequest) {
        todoService.updateTodo(todoUpdateRequest);
        return todoUpdateRequest;  // Возвращаем обновленные данные, чтобы клиент знал, что все прошло успешно
    }
}
