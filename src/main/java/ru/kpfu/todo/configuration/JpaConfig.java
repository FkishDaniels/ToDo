package ru.kpfu.todo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "ru.kpfu.todo.repository")
@Configuration
public class JpaConfig {
}
