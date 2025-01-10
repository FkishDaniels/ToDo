package ru.kpfu.todo.controller.cabinet;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.todo.service.ApplicationUserService;
import ru.kpfu.todo.service.TodoService;
import ru.kpfu.todo.util.UserUtilService;


@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class CabinetController {

    private final ApplicationUserService userService;
    private final UserUtilService userUtil;
    private final TodoService todoService;

    @GetMapping()
    public String homePage(
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sortBy,
            Model model,
            Authentication authentication
    ) {
        var user = userUtil.findUserByAuthentication(authentication);
        var filteredTodos = todoService.getFilteredAll(user,priority,status,sortBy);


        model.addAttribute("user", user);
        model.addAttribute("filteredTodos", filteredTodos);
        model.addAttribute("selectedPriority", priority);

        return "home";
    }
}
