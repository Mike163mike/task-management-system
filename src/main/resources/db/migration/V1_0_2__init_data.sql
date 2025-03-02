insert into roles(id, name)
values (1, 'ROLE_USER'),
       (2, 'ROLE_ASSIGNEE'),
       (3, 'ROLE_ADMIN');

insert into users(id, create_date, change_date, username, password, email)
values (1, now(), now(), 'Rick_Sanchez', '{noop}admin',
        'rick@gmail.com');

alter sequence public.users_id_seq restart with 2;

-- select pg_get_serial_sequence('users', 'id');
