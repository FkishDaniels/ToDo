-- changeset DaniilMarukha:1
CREATE TABLE IF NOT EXISTS application_user
(
    id       BIGSERIAL NOT NULL,
    username VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- changeset DaniilMarukha:2
CREATE TABLE IF NOT EXISTS todo
(
    id            BIGSERIAL NOT NULL,
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    created_date  DATE DEFAULT CURRENT_DATE NOT NULL,
    due_date      DATE,
    status  VARCHAR(255) DEFAULT 'TODO' NOT NULL,
    priority      VARCHAR(50) DEFAULT 'MEDIUM' NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_todo
(
    user_id BIGINT NOT NULL,
    todo_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, todo_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES application_user (id),
    CONSTRAINT fk_todo FOREIGN KEY (todo_id) REFERENCES todo (id)
);

-- changeset DaniilMarukha:3
CREATE TABLE IF NOT EXISTS global_permission
(
    id   BIGSERIAL NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);
-- changeset DaniilMarukha:4
CREATE TABLE IF NOT EXISTS user_permission
(
    user_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, permission_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES application_user (id),
    CONSTRAINT fk_permission FOREIGN KEY (permission_id) REFERENCES global_permission (id)
);
--changeset DaniilMarukha:5
ALTER TABLE todo add column name VARCHAR(255) NOT NULL;
-- Добавление задач в таблицу todo
INSERT INTO application_user(username, email, password) VALUES ('Daniil Marukha','daniilka2003super@mail.ru','$2a$10$ad8HFweogf3oMSAsWQay2.c.5sm/rnVTbUovc1eR65aVwIVXpO3ae');
INSERT INTO todo (title, description, created_date, due_date, status, priority, name)
VALUES
    ('Complete Project', 'Finish the project documentation and submit it to the manager', NOW(), '2025-01-15 23:59:59', 'IN_PROGRESS', 'HIGH', 'Work'),
    ('Grocery Shopping', 'Buy vegetables, fruits, and other essentials for the week', NOW(), '2025-01-10 18:00:00', 'IN_PROGRESS', 'MEDIUM', 'Personal');

-- Связывание задач с пользователем с ID 1
INSERT INTO user_todo (user_id, todo_id)
VALUES
    (1, 1), -- Связываем задачу с ID 1 с пользователем с ID 1
    (1, 2); -- Связываем задачу с ID 2 с пользователем с ID 1
