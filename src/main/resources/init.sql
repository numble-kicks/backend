DROP TABLE if EXISTS chat_message;
DROP TABLE if EXISTS chat_room;
DROP TABLE IF EXISTS like_video;
DROP TABLE IF EXISTS video;
DROP TABLE IF EXISTS follow;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS category;

CREATE TABLE category
(
    id          bigint       not null auto_increment,
    create_at   timestamp default current_timestamp,
    modified_at timestamp default current_timestamp on update current_timestamp,
    name        varchar(255) not null,
    primary key (id)
) ENGINE = InnoDB;

CREATE TABLE chat_message
(
    id           bigint NOT NULL AUTO_INCREMENT,
    create_at    datetime(6),
    modified_at  datetime(6),
    message      varchar(255),
    chat_room_id bigint,
    member_id    bigint,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE chat_room
(
    id          bigint NOT NULL AUTO_INCREMENT,
    create_at   datetime(6),
    modified_at datetime(6),
    buyer_id    bigint,
    seller_id   bigint,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE follow
(
    id          bigint NOT NULL AUTO_INCREMENT,
    from_member bigint NOT NULL,
    to_member   bigint NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE like_video
(
    id        bigint NOT NULL AUTO_INCREMENT,
    member_id bigint NOT NULL,
    video_id  bigint NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE member
(
    id                bigint      NOT NULL AUTO_INCREMENT,
    create_at         datetime(6) DEFAULT NULL,
    modified_at       datetime(6) DEFAULT NULL,
    email             varchar(255),
    email_verified    bit         NOT NULL,
    last_login_date   datetime(6),
    name              varchar(255),
    profile_image_url varchar(255),
    provider          varchar(255),
    role              varchar(255),
    user_id           bigint,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE video
(
    id            bigint       not null auto_increment,
    create_at   datetime(6) DEFAULT NULL,
    modified_at datetime(6) DEFAULT NULL,
    description   varchar(255),
    like_count    bigint,
    price         integer      not null,
    thumbnail_url varchar(255) not null,
    title         varchar(255) not null,
    used_status   bit          not null,
    video_url     varchar(255) not null,
    view_count    bigint,
    category_id   bigint,
    member_id     bigint,
    primary key (id)
) ENGINE = InnoDB;

ALTER TABLE category
    ADD CONSTRAINT UNIQUE (name);

ALTER TABLE chat_message
    ADD CONSTRAINT FOREIGN KEY (chat_room_id) references chat_room (id);

ALTER TABLE chat_message
    ADD CONSTRAINT FOREIGN KEY (member_id) references member (id);

ALTER TABLE chat_room
    ADD CONSTRAINT FOREIGN KEY (buyer_id) references member (id);

ALTER TABLE chat_room
    ADD CONSTRAINT FOREIGN KEY (seller_id) references member (id);

ALTER TABLE follow
    ADD CONSTRAINT FOREIGN KEY (from_member) references member (id);

ALTER TABLE follow
    ADD CONSTRAINT FOREIGN KEY (to_member) references member (id);

ALTER TABLE like_video
    ADD CONSTRAINT FOREIGN KEY (member_id) references member (id);

ALTER TABLE like_video
    ADD CONSTRAINT FOREIGN KEY (video_id) references video (id);

ALTER TABLE video
    ADD CONSTRAINT FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE video
    ADD CONSTRAINT FOREIGN KEY (member_id) REFERENCES member (id);
