USE asw;

-- families 테이블
CREATE TABLE `families` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `verification_code` varchar(100) NOT NULL,
                            `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- users 테이블
CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `family_id` bigint NOT NULL,
                         `name` varchar(100) NOT NULL,
                         `phone` varchar(20) NOT NULL,
                         `age` int unsigned DEFAULT NULL,
                         `birthday` date DEFAULT NULL,
                         `gender` enum('M','F') NOT NULL,
                         `password` varchar(255) NOT NULL,
                         `nickname` varchar(50) NOT NULL,
                         `profile_photo` varchar(255) DEFAULT NULL,
                         `family_type` enum('자녀','아빠','엄마','할아버지','할머니') DEFAULT NULL,
                         `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                         `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `phone` (`phone`),
                         UNIQUE KEY `nickname` (`nickname`),
                         KEY `fk_users_family` (`family_id`),
                         CONSTRAINT `fk_users_family` FOREIGN KEY (`family_id`) REFERENCES `families` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- personal_questions 테이블
CREATE TABLE `personal_questions` (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `family_id` bigint NOT NULL,
                                      `content` text NOT NULL,
                                      `sender` bigint NOT NULL,
                                      `receiver` bigint NOT NULL,
                                      `visibility` tinyint(1) DEFAULT '1',
                                      `solved` tinyint(1) DEFAULT '0',
                                      `likes` int unsigned DEFAULT '0',
                                      `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                      `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      PRIMARY KEY (`id`),
                                      KEY `fk_questions_sender` (`sender`),
                                      KEY `fk_questions_receiver` (`receiver`),
                                      KEY `fk_questions_family` (`family_id`),
                                      CONSTRAINT `fk_questions_family` FOREIGN KEY (`family_id`) REFERENCES `families` (`id`),
                                      CONSTRAINT `fk_questions_receiver` FOREIGN KEY (`receiver`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT `fk_questions_sender` FOREIGN KEY (`sender`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- public_questions 테이블
CREATE TABLE `public_questions` (
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `family_id` bigint NOT NULL,
                                    `content` text NOT NULL,
                                    `likes` int unsigned NOT NULL DEFAULT '0',
                                    `counts` int unsigned NOT NULL DEFAULT '0',
                                    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    PRIMARY KEY (`id`),
                                    KEY `fk_public_questions_family` (`family_id`),
                                    CONSTRAINT `fk_public_questions_family` FOREIGN KEY (`family_id`) REFERENCES `families` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- question_reference 테이블
CREATE TABLE `question_reference` (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `question_id` bigint NOT NULL,
                                      `question_type` enum('personal','public') NOT NULL,
                                      `family_id` bigint NOT NULL,
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uq_question` (`question_id`,`question_type`),
                                      KEY `fk_question_reference_family` (`family_id`),
                                      CONSTRAINT `fk_question_reference_family` FOREIGN KEY (`family_id`) REFERENCES `families` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- comments 테이블 (family_id 컬럼 추가)
CREATE TABLE `comments` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `question_ref_id` bigint NOT NULL,
                            `writer` bigint NOT NULL,
                            `family_id` bigint NOT NULL,
                            `content` text NOT NULL,
                            `reply_to` bigint DEFAULT NULL,
                            `likes` int unsigned NOT NULL DEFAULT '0',
                            `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`),
                            KEY `fk_comments_question_ref` (`question_ref_id`),
                            KEY `fk_comments_writer` (`writer`),
                            KEY `fk_comments_reply` (`reply_to`),
                            KEY `fk_comments_family` (`family_id`),
                            CONSTRAINT `fk_comments_family` FOREIGN KEY (`family_id`) REFERENCES `families` (`id`),
                            CONSTRAINT `fk_comments_question_ref` FOREIGN KEY (`question_ref_id`) REFERENCES `question_reference` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                            CONSTRAINT `fk_comments_reply` FOREIGN KEY (`reply_to`) REFERENCES `comments` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                            CONSTRAINT `fk_comments_writer` FOREIGN KEY (`writer`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- question_list 테이블 (family_id 포함하지 않음, 관리용)
CREATE TABLE `question_list` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `content` text NOT NULL,
                                 `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- flower_catalog 테이블 (family_id 불필요)
CREATE TABLE `flower_catalog` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `unlocked` tinyint(1) DEFAULT '0',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- fruit_catalog 테이블 (family_id 불필요)
CREATE TABLE `fruit_catalog` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `unlocked` tinyint(1) DEFAULT '0',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- archives 테이블 (멀티 테넌트 용 family_id 포함)
CREATE TABLE `archives` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `family_id` bigint NOT NULL,
                            `month` int NOT NULL,
                            `year` int NOT NULL,
                            `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            `period` int NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `fk_archives_family` (`family_id`),
                            CONSTRAINT `fk_archives_family` FOREIGN KEY (`family_id`) REFERENCES `families` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- tree 테이블 (멀티 테넌트 용 family_id 포함)
CREATE TABLE `tree` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `archive_id` bigint NOT NULL,
                        `family_id` bigint NOT NULL,
                        `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                        `tree_category` enum('flower','fruit') NOT NULL,
                        PRIMARY KEY (`id`),
                        KEY `archive_id` (`archive_id`),
                        KEY `fk_tree_family` (`family_id`),
                        CONSTRAINT `fk_tree_family` FOREIGN KEY (`family_id`) REFERENCES `families` (`id`),
                        CONSTRAINT `tree_ibfk_1` FOREIGN KEY (`archive_id`) REFERENCES `archives` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- flowers 테이블 (tree_id 통해 family_id 간접관리)
CREATE TABLE `flowers` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `tree_id` bigint NOT NULL,
                           `question_ref_id` bigint DEFAULT NULL,
                           `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (`id`),
                           KEY `tree_id` (`tree_id`),
                           KEY `question_ref_id` (`question_ref_id`),
                           CONSTRAINT `flowers_ibfk_1` FOREIGN KEY (`tree_id`) REFERENCES `tree` (`id`),
                           CONSTRAINT `flowers_ibfk_3` FOREIGN KEY (`question_ref_id`) REFERENCES `question_reference` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- fruits 테이블 (tree_id 통해 family_id 간접관리)
CREATE TABLE `fruits` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `tree_id` bigint NOT NULL,
                          `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                          `puzzle_id` int DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          KEY `tree_id` (`tree_id`),
                          KEY `fk_fruits_puzzle` (`puzzle_id`),
                          CONSTRAINT `fk_fruits_puzzle` FOREIGN KEY (`puzzle_id`) REFERENCES `puzzle` (`puzzle_id`),
                          CONSTRAINT `fruits_ibfk_1` FOREIGN KEY (`tree_id`) REFERENCES `tree` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- puzzle 테이블 (멀티 테넌트 용 family_id 포함)) - puzzle 아카이브로 만들어야겠네.....
CREATE TABLE `puzzle` (
                          `puzzle_id` int NOT NULL AUTO_INCREMENT,
                          `image_path` varchar(255) NOT NULL,
                          `size` int DEFAULT NULL,
                          `completedPiecesID` json DEFAULT NULL,
                          `completed` tinyint(1) NOT NULL DEFAULT '0',
                          `isPlayingPuzzle` tinyint(1) NOT NULL DEFAULT '0',
                          `solverId` bigint DEFAULT NULL,
                          `contributors` json DEFAULT NULL,
                          `families_id` bigint NOT NULL,
                          PRIMARY KEY (`puzzle_id`),
                          KEY `fk_puzzle_solver` (`solverId`),
                          KEY `fk_puzzle_families` (`families_id`),
                          CONSTRAINT `fk_puzzle_families` FOREIGN KEY (`families_id`) REFERENCES `families` (`id`),
                          CONSTRAINT `fk_puzzle_solver` FOREIGN KEY (`solverId`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- puzzle_category 테이블 (puzzle_id 통해 family_id 간접관리)
CREATE TABLE `puzzle_category` (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `puzzle_id` int NOT NULL,
                                   `category` varchar(50) NOT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `puzzle_id` (`puzzle_id`),
                                   CONSTRAINT `puzzle_category_ibfk_1` FOREIGN KEY (`puzzle_id`) REFERENCES `puzzle` (`puzzle_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- puzzle_ai_keyword 테이블 (puzzle_id 통해 family_id 간접관리)
CREATE TABLE `puzzle_ai_keyword` (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `puzzle_id` int NOT NULL,
                                     `keyword` varchar(100) NOT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `puzzle_id` (`puzzle_id`),
                                     CONSTRAINT `puzzle_ai_keyword_ibfk_1` FOREIGN KEY (`puzzle_id`) REFERENCES `puzzle` (`puzzle_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- puzzle_pieces 테이블
CREATE TABLE `puzzle_pieces` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `puzzle_id` int NOT NULL,
                                 `piece_id` varchar(50) NOT NULL,
                                 `position` json NOT NULL,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `uq_puzzle_piece` (`puzzle_id`,`piece_id`),
                                 CONSTRAINT `puzzle_pieces_ibfk_1` FOREIGN KEY (`puzzle_id`) REFERENCES `puzzle` (`puzzle_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DELIMITER //

CREATE TRIGGER trg_after_insert_questions
    AFTER INSERT ON personal_questions
    FOR EACH ROW
BEGIN
    INSERT INTO question_reference (question_id, question_type, family_id)
    VALUES (NEW.id, 'personal', NEW.family_id);
END;
//

CREATE TRIGGER trg_after_insert_public_questions
    AFTER INSERT ON public_questions
    FOR EACH ROW
BEGIN
    INSERT INTO question_reference (question_id, question_type, family_id)
    VALUES (NEW.id, 'public', NEW.family_id);
END;
//

CREATE TRIGGER trg_before_insert_comments
    BEFORE INSERT ON comments
    FOR EACH ROW
BEGIN
    DECLARE ref_family_id BIGINT;
    SELECT family_id INTO ref_family_id
    FROM question_reference
    WHERE id = NEW.question_ref_id
    LIMIT 1;
    SET NEW.family_id = ref_family_id;
END;
//

DELIMITER ;
