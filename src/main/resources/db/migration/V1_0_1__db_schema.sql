CREATE TABLE users
(
    id          BIGSERIAL PRIMARY KEY,
    create_date TIMESTAMP WITH TIME ZONE,
    change_date TIMESTAMP WITH TIME ZONE,
    password    VARCHAR(255) NOT NULL,
    email       VARCHAR(254) NOT NULL UNIQUE,
    CONSTRAINT uc_email UNIQUE (email)
);

CREATE INDEX idx_users_email ON users (email);

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
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE refresh_tokens
(
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(512)             NOT NULL UNIQUE,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id     BIGINT                   NOT NULL,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_refresh_token_token ON refresh_tokens (token);

CREATE TABLE tasks
(
    id          BIGSERIAL PRIMARY KEY,
    create_date TIMESTAMP WITH TIME ZONE,
    change_date TIMESTAMP WITH TIME ZONE,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    status      VARCHAR(20)  NOT NULL,
    priority    VARCHAR(20)  NOT NULL,
    creator_id  BIGINT       NOT NULL,
    assignee_id BIGINT,
    CONSTRAINT fk_task_creator FOREIGN KEY (creator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_task_assignee FOREIGN KEY (assignee_id) REFERENCES users (id) ON DELETE SET NULL
);

CREATE INDEX idx_tasks_creator_id ON tasks (creator_id);
CREATE INDEX idx_tasks_assignee_id ON tasks (assignee_id);

CREATE TABLE comments
(
    id          BIGSERIAL PRIMARY KEY,
    create_date TIMESTAMP WITH TIME ZONE,
    change_date TIMESTAMP WITH TIME ZONE,
    text        TEXT   NOT NULL,
    task_id     BIGINT NOT NULL,
    user_id     BIGINT NOT NULL,
    CONSTRAINT fk_comment_task FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_comments_task_id ON comments (task_id);
CREATE INDEX idx_comments_user_id ON comments (user_id);
