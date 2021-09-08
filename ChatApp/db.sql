-- we don't know how to generate root <with-no-name> (class Root) :(
create table groupchat
(
    group_id   int auto_increment
        primary key,
    group_name varchar(50) null
);

create table groupmessages
(
    id           bigint unsigned auto_increment
        primary key,
    group_id     int                          null,
    message      longblob                     null,
    sender_id    varchar(11)                  null,
    message_name varchar(1000) default 'text' not null,
    constraint id
        unique (id)
);

create table user_data
(
    phone_number  varchar(11)                                                       not null
        primary key,
    user_name     varchar(50)                                                       null,
    email         varchar(50)                                                       null,
    picture       longblob                                                          null,
    user_password varchar(5000)                                                     null,
    gender        enum ('female', 'male')                                           null,
    country       varchar(20)                                                       null,
    date_birth    date                                                              null,
    bio           varchar(200)                                                      null,
    user_status   enum ('offline', 'available', 'busy', 'away') default 'available' null,
    on_line       tinyint(1)                                    default 0           not null
);

create table contacts
(
    user_id         varchar(11)       not null,
    contact_id      varchar(11)       not null,
    active_chat_bot tinyint default 0 null,
    primary key (user_id, contact_id),
    constraint contacts_ibfk_1
        foreign key (user_id) references user_data (phone_number)
            on delete cascade,
    constraint contacts_ibfk_2
        foreign key (contact_id) references user_data (phone_number)
            on delete cascade
);

create index contact_id
    on contacts (contact_id);

create table groupchatmember
(
    group_id  int         null,
    member_id varchar(11) null,
    constraint groupchatmember_ibfk_1
        foreign key (group_id) references groupchat (group_id)
            on delete cascade,
    constraint groupchatmember_ibfk_2
        foreign key (member_id) references user_data (phone_number)
            on delete cascade
);

create index group_id
    on groupchatmember (group_id);

create index member_id
    on groupchatmember (member_id);

create table message
(
    message_id   int auto_increment
        primary key,
    message_name varchar(50) null,
    sender_id    varchar(11) null,
    receiver_id  varchar(11) null,
    group_id     int         null,
    state        varchar(50) null,
    content      blob        null,
    message_date date        null,
    constraint message_ibfk_1
        foreign key (sender_id) references user_data (phone_number)
            on delete cascade,
    constraint message_ibfk_2
        foreign key (receiver_id) references user_data (phone_number)
            on delete cascade,
    constraint message_ibfk_3
        foreign key (group_id) references groupchatmember (group_id)
            on delete cascade
);

create index group_id
    on message (group_id);

create index receiver_id
    on message (receiver_id);

create index sender_id
    on message (sender_id);

create table notifications
(
    notification_id  int auto_increment
        primary key,
    content          varchar(1000) null,
    sender           varchar(11)   null,
    notifaction_date date          null,
    completed        tinyint(1)    null,
    constraint notifications_ibfk_1
        foreign key (sender) references user_data (phone_number)
            on delete cascade
);

create table notification_receivers
(
    notification_id int         not null,
    receiver        varchar(11) not null,
    primary key (notification_id, receiver),
    constraint notification_receivers_ibfk_1
        foreign key (notification_id) references notifications (notification_id)
            on delete cascade,
    constraint notification_receivers_ibfk_2
        foreign key (receiver) references user_data (phone_number)
            on delete cascade
);

create index receiver
    on notification_receivers (receiver);

create index sender
    on notifications (sender);

