INSERT INTO roles (id, name)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ASSIGNEE'),
       (3, 'ROLE_ADMIN');

INSERT INTO users (id, create_date, change_date, password, email)
VALUES (1, NOW(), NOW(), '{noop}admin', 'rick_sanchez@gmail.com');

ALTER SEQUENCE public.users_id_seq RESTART WITH 2;

-- SELECT pg_get_serial_sequence('users', 'id');
