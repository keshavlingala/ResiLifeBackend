create extension if not exists "uuid-ossp";

create table public.apartments
(
    "apartmentId" uuid default uuid_generate_v4() not null
        primary key,
    name          varchar(255),
    picture       varchar(255),
    members       json,
    payload       json,
    "createDate"  timestamp                       not null
);

alter table public.apartments
    owner to keshav;

create table public.users
(
    email         varchar(255) not null
        constraint email_pk
            primary key,
    password      varchar(255) not null,
    "firstName"   varchar(255),
    "lastName"    varchar(255),
    meta          json,
    "apartmentId" uuid
);

alter table public.users
    owner to keshav;



