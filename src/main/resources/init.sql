CREATE TABLE comment
(
    id bigint NOT NULL AUTO_INCREMENT,
    create_at datetime(6) DEFAULT NULL,
    modified_at datetime(6) DEFAULT NULL,
    content     varchar(255),
    member_id bigint NOT NULL,
    video_id bigint NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE video
(
    id bigint NOT NULL AUTO_INCREMENT,
    description   varchar(255),
    like_count    bigint,
    thumbnail_url varchar(255),
    title         varchar(255),
    video_url     varchar(255),
    view_count    bigint,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE like_video
(
    id bigint NOT NULL AUTO_INCREMENT,
    member_id bigint NOT NULL,
    video_id bigint NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE comment ADD CONSTRAINT FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE comment ADD CONSTRAINT FOREIGN KEY (video_id) REFERENCES video (id);

ALTER TABLE like_video ADD CONSTRAINT FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE like_video ADD CONSTRAINT FOREIGN KEY (video_id) REFERENCES video (id);