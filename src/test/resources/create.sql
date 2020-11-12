create table hibernate_sequence
(
    next_val bigint null
);

create table tag
(
    id   int          not null
        primary key,
    name varchar(255) null
);

create table user
(
    user_id int          not null
        primary key,
    name    varchar(255) null,
    constraint UK_gj2fy3dcix7ph7k8684gka40c
        unique (name)
);

create table certificate
(
    id            int          not null
        primary key,
    creation_time datetime(6)  null,
    description   varchar(255) null,
    duration      int          null,
    name          varchar(255) null,
    price         float        null,
    update_time   datetime(6)  null,
    user_id       int          null,
    constraint FKp2ure8wwndmepxyj2ey8r3lb2
        foreign key (user_id) references user (user_id)
);

create table certificate_tags
(
    certificate_id int not null,
    tags_id        int not null,
    constraint FKojrcrs9uw9xhky01esy525irv
        foreign key (certificate_id) references certificate (id),
    constraint FKqh8mewbi9cxs4fdgwnmtp0jom
        foreign key (tags_id) references tag (id)
);

