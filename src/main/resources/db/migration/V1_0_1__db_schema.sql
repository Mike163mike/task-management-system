create table users
(
    id          bigserial primary key,
    create_date timestamp,
    change_date timestamp,
    username    varchar(150) not null unique,
    password    varchar(255) not null,
    email       varchar(254) not null unique
);

create table roles
(
    id   bigserial primary key,
    name varchar(50) not null unique
);

create table user_roles
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    constraint fk_user
        foreign key (user_id) references users (id) on delete cascade,
    constraint fk_role
        foreign key (role_id) references roles (id) on delete cascade
);

create table tasks
(
    id          bigserial primary key,
    create_date timestamp,
    change_date timestamp,
    title       varchar(255) not null,
    description text,
    status      varchar(50)  not null,
    priority    varchar(50)  not null,
    creator_id  bigint       not null,
    assignee_id bigint,
    constraint fk_task_creator foreign key (creator_id) references users (id) on delete cascade,
    constraint fk_task_assignee foreign key (assignee_id) references users (id) on delete set null
);

create table comments
(
    id          bigserial primary key,
    create_date timestamp,
    change_date timestamp,
    text        text   not null,
    task_id     bigint not null,
    user_id     bigint not null,
    constraint fk_comment_task foreign key (task_id) references tasks (id) on delete cascade,
    constraint fk_comment_user foreign key (user_id) references users (id) on delete cascade
);
