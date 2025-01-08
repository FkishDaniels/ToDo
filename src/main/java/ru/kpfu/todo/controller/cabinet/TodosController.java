package ru.kpfu.todo.controller.cabinet;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String editTodo(@ModelAttribute("todoForm") @Valid TodoUpdateRequest todoUpdateRequest, Model model) {
        todoService.updateTodo(todoUpdateRequest);
        return "editTodo";
    }
}
