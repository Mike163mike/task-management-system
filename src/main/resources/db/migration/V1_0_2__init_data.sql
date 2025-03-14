INSERT INTO roles (id, name)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ASSIGNEE'),
       (3, 'ROLE_ADMIN')
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (id, create_date, change_date, password, email)
VALUES (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '$2a$10$hLTyEgtHwGGcVkj43RIwGel6bA16OKF58tQLlyomvt8XSiyouyDWG',
        'rick_sanchez@gmail.com')
ON CONFLICT (id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 3)
ON CONFLICT (user_id, role_id) DO NOTHING;

ALTER SEQUENCE public.users_id_seq RESTART WITH 2;

ALTER SEQUENCE public.roles_id_seq RESTART WITH 4;

-- SELECT pg_get_serial_sequence('users', 'id');
