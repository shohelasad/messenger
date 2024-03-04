use messengerdb;
create table public.message1 (
    id bigint primary key not null,
    content character varying(255) not null,
    message_status smallint,
    timestamp timestamp(6) without time zone not null,
    recipient_id bigint,
    sender_id bigint
);

create table users
(
    id  bigint  not null primary key,
    nickname varchar(255) not null unique
);

