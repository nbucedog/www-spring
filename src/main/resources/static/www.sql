drop table if exists `users`;
create table `users`(
    `id` int(11) auto_increment,
    `username` varchar(45) not null,
    `password` varchar(200) not null,
    `salt` varchar(45) DEFAULT NULL,
    `state` tinyint(1) DEFAULT '0',
    `nickname` varchar(45),
    `sex` varchar(7),
    -- `role_id` int(11),-- 多对多
    -- 文章 一对多
    `mail` varchar(45),
    `phone` varchar(15),
    `resume` varchar(200),
    PRIMARY KEY (`id`),
    UNIQUE KEY `username_UNIQUE` (`username`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists `roles`;
create table `roles`(
    `id` int(11) auto_increment,
    `role` varchar(20),
    `description` varchar(45),
    -- `user_id` int(11),-- 多对多
    -- `permission_id` int(11) -- 多对多
        PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists `permissions`;
create table `permissions`(
    `id` int(11) auto_increment,
    `permission` varchar(20),
    -- role_id 多对多
    `description` varchar(45),
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists `tags`;
create table `tags`(
    `id` int(11) auto_increment,
    `tag` varchar(20),
    `color` varchar(20),
    -- article 多对多
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists `articles`;
create table `articles`(
    `id` int(11) auto_increment,
    `title` varchar(100) not null,
    `summary` longtext,
    `date` date,
    `user_id` int(11),-- 多对一
    `thumbs` int(11),
    `views` int(11),
    --  comments 一对多
    `content` longtext,
    -- `article_id` int(11), -- 多对多
    primary key (`id`),
    constraint fk_article_user foreign key (`user_id`) references `users` (`id`) on update cascade on delete restrict
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists `comments`;
create table `comments`(
    `id` int(11) auto_increment,
    `user_id` int(11), -- 多对一
    `comment_id` int(11), -- 多对一
    `date` datetime,
    `content` varchar(200),
    primary key (`id`),
    constraint fk_comment_user foreign key (`user_id`) references `users` (`id`) on update cascade on delete restrict,
    constraint fk_comment_comment foreign key (`comment_id`) references `comments` (`id`) on update cascade on delete restrict
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 中间表

-- article和tag
drop table if exists `article_tag_relationships`;
create table `article_tag_relationships`(
    `article_id` int(11),
    `tag_id` int(11),
    primary key (`article_id`,`tag_id`),
    constraint fk_re_article_tag foreign key (`article_id`) references `articles`(`id`) on delete no action on update no action,
    constraint fk_re_tag_article foreign key (`tag_id`) references `tags`(`id`) on DELETE no action on UPDATE no action
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- user和role
drop table if exists `user_role_relationships`;
create table `user_role_relationships`(
    `user_id` int(11),
    `role_id` int(11),
    primary key (`user_id`,`role_id`),
    constraint fk_re_user_role foreign key (`user_id`) references `users`(`id`) on delete no action on update no action,
    constraint fk_re_role_user foreign key (`role_id`) references `roles`(`id`) on delete no action on update no action
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- role和permission
drop table if exists `role_permission_relationships`;
create table `role_permission_relationships`(
    `role_id` int(11),
    `permission_id` int(11),
    primary key (`role_id`,`permission_id`),
    constraint fk_re_role_permission foreign key (`role_id`) references `roles`(`id`) on delete no action on update no action,
    constraint fk_re_permission_role foreign key (`permission_id`) references `permissions`(`id`) on delete no action on update no action
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table `articles` add column publish boolean;

create table `persistent_logins` (
    `username` varchar(64) not null,
    `series` varchar(64) not null,
    `token` varchar(64) not null,
    `last_used` timestamp not null default current_timestamp on update current_timestamp,
    primary key (`series`)
)engine=InnoDB default charset=utf8;

-- 文章评论
drop table if exists `reviews`;
create table `reviews`(
    `id` int(11) auto_increment,
    `article_id` int(11), -- 多对一
    `user_id` int(11), -- 多对一
    -- `reply_id` int(11), -- 一对多
    `date` datetime,
    `content` longtext,
    primary key (`id`),
    constraint fk_review_article foreign key (`article_id`) references `articles` (`id`) on update cascade on delete restrict,
    constraint fk_review_user foreign key (`user_id`) references `users` (`id`) on update cascade on delete restrict
)engine=InnoDB default charset=utf8mb4;

-- 回复评论
drop table if exists `review_replies`;
create table `review_replies`(
    `id` int(11) auto_increment,
    -- `article_id` int(11), -- 多对一
    `user_id` int(11), -- 多对一
    `r_user_id` int(11), -- 多对一
    `review_id` int(11), -- 多对一
    `date` datetime,
    `content` longtext,
    primary key (`id`),
    constraint fk_review_reply_user foreign key (`user_id`) references `users` (`id`) on update cascade on delete restrict,
    constraint fk_review_reply_r_user foreign key (`r_user_id`) references `users` (`id`) on update cascade on delete restrict,
    constraint fk_reply_review foreign key (`review_id`) references `reviews` (`id`) on update cascade on delete restrict
)engine=InnoDB default charset=utf8mb4;