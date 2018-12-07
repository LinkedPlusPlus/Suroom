CREATE DATABASE suroom;

use suroom;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) DEFAULT NULL,
  `sex` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `subject` (
  `id` int(11) NOT NULL,
  `subject_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `tendency` (
  `id` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `group_study` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Group_name` varchar(45) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `user_group` (
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `joined` datetime DEFAULT NULL,
  `role` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`group_id`),
  KEY `fk_user_group2_idx` (`group_id`),
  CONSTRAINT `fk_user_group1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_user_group2` FOREIGN KEY (`group_id`) REFERENCES `group_study` (`id`)
);

CREATE TABLE `user_subject` (
  `user_id` int(11) NOT NULL,
  `subject_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`subject_id`),
  KEY `fk_subject_1_idx` (`subject_id`),
  CONSTRAINT `FK_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_subject_1` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`)
);

CREATE TABLE `user_tendency` (
  `user_id` int(11) NOT NULL,
  `tendency_id` int(11) NOT NULL,
  `selected` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`tendency_id`),
  KEY `fk_u_t_tendency_idx` (`tendency_id`),
  CONSTRAINT `fk_u_t_tendency` FOREIGN KEY (`tendency_id`) REFERENCES `tendency` (`id`),
  CONSTRAINT `fk_u_t_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);
