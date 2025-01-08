package ru.kpfu.todo.controller.cabinet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodosController {

    @GetMapping
    public String todos(Model model) {

        return "todos";
    }
}
