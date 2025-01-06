package ru.kpfu.todo;

import org.springframework.boot.SpringApplication;

public class TestToDoApplication {

    public static void main(String[] args) {
        SpringApplication.from(ToDoApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
