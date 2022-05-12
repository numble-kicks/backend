DROP TABLE IF EXISTS like_video;
DROP TABLE IF EXISTS video;
DROP TABLE IF EXISTS follow;
DROP TABLE IF EXISTS member;


CREATE TABLE video
(
    id            bigint       not null auto_increment,
    description   varchar(255),
    like_count    bigint DEFAULT 0,
    thumbnail_url varchar(255) not null,
    title         varchar(255) not null,
    video_url     varchar(255) not null,
    view_count    bigint DEFAULT 0,
    member_id     bigint,
    create_at   datetime(6) DEFAULT NULL,
    modified_at datetime(6) DEFAULT NULL,
    primary key (id)
) ENGINE=InnoDB;

CREATE TABLE like_video
(
    id bigint NOT NULL AUTO_INCREMENT,
    member_id bigint NOT NULL,
    video_id bigint NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE member
(
    id bigint NOT NULL AUTO_INCREMENT,
    create_at datetime(6) DEFAULT NULL,
    modified_at datetime(6) DEFAULT NULL,
    email             varchar(255),
    email_verified    bit NOT NULL,
    last_login_date   datetime(6),
    name              varchar(255),
    profile_image_url varchar(255),
    role              varchar(30) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE follow
(
    id bigint NOT NULL AUTO_INCREMENT,
    from_member bigint NOT NULL,
    to_member bigint NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;


ALTER TABLE like_video ADD CONSTRAINT FOREIGN KEY (member_id) REFERENCES member (id);


ALTER TABLE like_video ADD CONSTRAINT FOREIGN KEY (video_id) REFERENCES video (id);

ALTER TABLE follow ADD CONSTRAINT FOREIGN KEY (from_member) REFERENCES member (id);

ALTER TABLE follow ADD CONSTRAINT FOREIGN KEY (to_member) REFERENCES member (id);

ALTER TABLE video
    ADD CONSTRAINT FOREIGN KEY (member_id) REFERENCES member (id)
