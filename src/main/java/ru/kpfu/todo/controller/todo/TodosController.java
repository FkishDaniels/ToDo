package ru.kpfu.todo.controller.todo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.todo.controller.todo.payload.TodoCreateRequest;
import ru.kpfu.todo.controller.todo.payload.TodoResponse;
import ru.kpfu.todo.controller.todo.payload.TodoUpdateRequest;
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
    public ResponseEntity<TodoUpdateRequest> editTodo(@Valid @RequestBody TodoUpdateRequest todoUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(todoUpdateRequest);
        }
        todoService.updateTodo(todoUpdateRequest);
        return ResponseEntity.ok(todoUpdateRequest);
    }

    @PostMapping("/sign/{id}")
    @ResponseBody
    public ResponseEntity<Void> sign(@PathVariable Long id, Authentication authentication) {
        todoService.takeTodo(id,authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<TodoResponse> createTodo(@Valid @RequestBody TodoCreateRequest todoCreateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
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
