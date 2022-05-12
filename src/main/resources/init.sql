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

CREATE TABLE video
(
    id            bigint       not null auto_increment,
    create_at     datetime(6),
    modified_at   datetime(6),
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
    role              varchar(30) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE follow
(
    id          bigint NOT NULL AUTO_INCREMENT,
    from_member bigint NOT NULL,
    to_member   bigint NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;


ALTER TABLE like_video
    ADD CONSTRAINT FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE like_video
    ADD CONSTRAINT FOREIGN KEY (video_id) REFERENCES video (id);

ALTER TABLE follow
    ADD CONSTRAINT FOREIGN KEY (from_member) REFERENCES member (id);

ALTER TABLE follow
    ADD CONSTRAINT FOREIGN KEY (to_member) REFERENCES member (id);

ALTER TABLE video
    ADD CONSTRAINT FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE video
    ADD CONSTRAINT FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE category
    ADD CONSTRAINT UNIQUE (name);