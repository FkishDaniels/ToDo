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
    created_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    due_date      TIMESTAMP,
    is_completed  BOOLEAN DEFAULT FALSE NOT NULL,
    priority      VARCHAR(50) DEFAULT 'MEDIUM',
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