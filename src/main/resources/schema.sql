create table if not exists t_user(
    f_id bigint generated always as identity primary key,
    f_username varchar not null,
    f_password varchar,
    f_phone varchar not null,
    f_avatar varchar not null,
    f_ctime timestamptz not null,
    f_mtime timestamptz not null
);

create index if not exists idx_user_phone on t_user(f_phone);