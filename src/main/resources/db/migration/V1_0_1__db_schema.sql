CREATE TABLE users
(
    id          BIGSERIAL PRIMARY KEY,
    create_date TIMESTAMP,
    change_date TIMESTAMP,
    password    VARCHAR(255) NOT NULL,
    email       VARCHAR(254) NOT NULL UNIQUE
);

CREATE TABLE roles
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_role
        FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE tasks
(
    id          BIGSERIAL PRIMARY KEY,
    create_date TIMESTAMP,
    change_date TIMESTAMP,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    status      VARCHAR(20)  NOT NULL,
    priority    VARCHAR(20)  NOT NULL,
    creator_id  BIGINT       NOT NULL,
    assignee_id BIGINT,
    CONSTRAINT fk_task_creator FOREIGN KEY (creator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_task_assignee FOREIGN KEY (assignee_id) REFERENCES users (id) ON DELETE SET NULL
);

CREATE TABLE comments
(
    id          BIGSERIAL PRIMARY KEY,
    create_date TIMESTAMP,
    change_date TIMESTAMP,
    text        TEXT   NOT NULL,
    task_id     BIGINT NOT NULL,
    user_id     BIGINT NOT NULL,
    CONSTRAINT fk_comment_task FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
