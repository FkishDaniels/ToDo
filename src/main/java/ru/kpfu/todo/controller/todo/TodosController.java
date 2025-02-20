package ru.kpfu.todo.controller.todo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.todo.controller.todo.payload.TodoResponse;
import ru.kpfu.todo.controller.todo.payload.TodoRequest;
import ru.kpfu.todo.service.TodoService;
import ru.kpfu.todo.util.UserUtilService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodosController {
    private final TodoService todoService;
    private final UserUtilService userUtil;

    @GetMapping()
    public String todos(Model model) {
        return "todos";
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getById(
            @PathVariable Long id
    ) {
        return new ResponseEntity<>(todoService.findById(id),HttpStatus.valueOf(201));
    }


    @GetMapping("/get-all")
    @ResponseBody
    public Page<TodoResponse> findAll(
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sortStrategy", required = false, defaultValue = "desc") String sortStrategy,
            Authentication authentication
    ) {

        return todoService.findAllAndCheckTaken(pageNum,pageSize,sortStrategy,authentication);
    }

    @PutMapping("/edit")
    @ResponseBody
    public ResponseEntity<TodoRequest> editTodo(@Valid @RequestBody TodoRequest todoRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(todoRequest);
        }
        todoService.updateTodo(todoRequest);
        return ResponseEntity.ok(todoRequest);
    }

    @PostMapping("/sign/{id}")
    @ResponseBody
    public ResponseEntity<Void> sign(@PathVariable Long id, Authentication authentication) {
        todoService.takeTodo(id,authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<TodoResponse> createTodo(@Valid @RequestBody TodoRequest todoCreateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("Ошибка какая-то");
            System.out.println(bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(new TodoResponse());
        }
        return ResponseEntity.ok(todoService.createTodo(todoCreateRequest));
    }

    @DeleteMapping()
    @ResponseBody
    public ResponseEntity<Void> deleteTodo(@RequestParam Long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
