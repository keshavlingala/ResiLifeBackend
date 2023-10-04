create table users
(
    id       int          not null generated always as identity,
    email    varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
);

create table homes
(
    id          integer generated by default as identity,
    owner_id    integer      not null,
    username    varchar(255) not null,
    token       varchar(255) not null,
    description text,
    roomates    json,
    constraint homes_pk
        unique (username, owner_id)
);

comment on table homes is 'A Group of Users with Home Specifics';



