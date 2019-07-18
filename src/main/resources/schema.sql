CREATE DATABASE IF NOT EXISTS library_system;

USE library_system;

CREATE TABLE IF NOT EXISTS `author` (
	`author_id` bigint NOT NULL AUTO_INCREMENT,
	`first_name` varchar(25) NOT NULL,
	`last_name` varchar(40) NOT NULL,
	PRIMARY KEY (`author_id`),
    UNIQUE KEY (first_name, last_name)
);

CREATE TABLE IF NOT EXISTS `keyword` (
	`keyword_id` bigint NOT NULL AUTO_INCREMENT,
	`word` varchar(15) NOT NULL UNIQUE,
	PRIMARY KEY (`keyword_id`)
);

CREATE TABLE IF NOT EXISTS `book_keyword` (
	`book_id` bigint NOT NULL,
	`keyword_id` bigint NOT NULL,
	PRIMARY KEY (`book_id`,`keyword_id`)
);

CREATE TABLE IF NOT EXISTS `book` (
	`book_id` bigint NOT NULL AUTO_INCREMENT,
	`title` varchar(255) NOT NULL,
	`year` int(4) NOT NULL,
	`description` varchar(500),
	PRIMARY KEY (`book_id`),
    UNIQUE KEY (title, year, description)
);

CREATE TABLE IF NOT EXISTS `user` (
	`user_id` bigint NOT NULL AUTO_INCREMENT,
	`email` varchar(255) NOT NULL UNIQUE,
	`password` varchar(100) NOT NULL,
	`phone` varchar(15) NOT NULL UNIQUE,
	`first_name` varchar(30) NOT NULL,
	`last_name` varchar(30) NOT NULL,
	`role_id` bigint NOT NULL,
	`karma` int NOT NULL DEFAULT '3',
	PRIMARY KEY (`user_id`)
);

CREATE TABLE IF NOT EXISTS `loan` (
	`loan_id` bigint NOT NULL AUTO_INCREMENT,
	`book_id` bigint NOT NULL,
	`user_id` bigint NOT NULL,
	`apply_date` DATE NOT NULL,
	`loan_date` DATE,
	`expired_date` DATE,
	`return_date` DATE,
	PRIMARY KEY (`loan_id`)
);

CREATE TABLE IF NOT EXISTS `author_book` (
	`author_id` bigint NOT NULL,
	`book_id` bigint NOT NULL,
	PRIMARY KEY (`author_id`,`book_id`),
    UNIQUE KEY (author_id, book_id)
);

CREATE TABLE IF NOT EXISTS `role` (
	`role_id` bigint NOT NULL AUTO_INCREMENT,
	`name` varchar(15) NOT NULL UNIQUE,
	PRIMARY KEY (`role_id`)
);

CREATE TABLE IF NOT EXISTS `location` (
	`location_id` bigint NOT NULL AUTO_INCREMENT,
	`book_id` bigint,
	`bookcase_id` bigint NOT NULL,
	`shelf_number` int NOT NULL,
	`cell_number` int NOT NULL,
	`is_occupied` BOOLEAN NOT NULL DEFAULT false,
	PRIMARY KEY (`location_id`)
);

CREATE TABLE IF NOT EXISTS `bookcase` (
	`bookcase_id` bigint NOT NULL AUTO_INCREMENT,
	`shelf_quantity` int NOT NULL,
	`cell_quantity` int NOT NULL,
	PRIMARY KEY (`bookcase_id`)
);

ALTER TABLE `book_keyword` ADD CONSTRAINT `book_keyword_fk0` FOREIGN KEY (`book_id`) REFERENCES `book`(`book_id`)  ON DELETE CASCADE;

ALTER TABLE `book_keyword` ADD CONSTRAINT `book_keyword_fk1` FOREIGN KEY (`keyword_id`) REFERENCES `keyword`(`keyword_id`) ON DELETE CASCADE;

ALTER TABLE `user` ADD CONSTRAINT `user_fk0` FOREIGN KEY (`role_id`) REFERENCES `role`(`role_id`)  ON DELETE NO ACTION;

ALTER TABLE `loan` ADD CONSTRAINT `loan_fk0` FOREIGN KEY (`book_id`) REFERENCES `book`(`book_id`);

ALTER TABLE `loan` ADD CONSTRAINT `loan_fk1` FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`);

ALTER TABLE `author_book` ADD CONSTRAINT `author_book_fk0` FOREIGN KEY (`author_id`) REFERENCES `author`(`author_id`) ON DELETE CASCADE;

ALTER TABLE `author_book` ADD CONSTRAINT `author_book_fk1` FOREIGN KEY (`book_id`) REFERENCES `book`(`book_id`) ON DELETE CASCADE;

ALTER TABLE `location` ADD CONSTRAINT `location_fk0` FOREIGN KEY (`book_id`) REFERENCES `book`(`book_id`);

ALTER TABLE `location` ADD CONSTRAINT `location_fk1` FOREIGN KEY (`bookcase_id`) REFERENCES `bookcase`(`bookcase_id`)  ON DELETE CASCADE;

