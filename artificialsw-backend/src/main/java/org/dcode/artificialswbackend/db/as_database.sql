USE `as`;

CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(100) NOT NULL,
                       age INT UNSIGNED,
                       birthday DATE,
                       loginId VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       profilePhoto VARCHAR(255),
                       role VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE questions (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           content TEXT NOT NULL,
                           sender BIGINT NOT NULL,
                           receiver BIGINT NOT NULL,
                           isPublic BOOLEAN DEFAULT TRUE,
                           solved BOOLEAN DEFAULT FALSE,
                           likes INT UNSIGNED DEFAULT 0,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           CONSTRAINT fk_questions_sender
                               FOREIGN KEY (sender) REFERENCES users(id)
                                   ON DELETE CASCADE ON UPDATE CASCADE,
                           CONSTRAINT fk_questions_receiver
                               FOREIGN KEY (receiver) REFERENCES users(id)
                                   ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE public_questions (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  content TEXT NOT NULL,
                                  likes INT UNSIGNED NOT NULL DEFAULT 0,
                                  counts INT UNSIGNED NOT NULL DEFAULT 0,
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE question_list (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               content TEXT NOT NULL,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE question_reference (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    question_id BIGINT NOT NULL,
                                    question_type ENUM('personal', 'public') NOT NULL,
                                    UNIQUE KEY uq_question(question_id, question_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE comments (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          question_ref_id BIGINT NOT NULL,
                          writer BIGINT NOT NULL,
                          content TEXT NOT NULL,
                          reply_to BIGINT DEFAULT NULL,
                          likes INT UNSIGNED NOT NULL DEFAULT 0,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                          CONSTRAINT fk_comments_question_ref
                              FOREIGN KEY (question_ref_id) REFERENCES question_reference(id)
                                  ON DELETE CASCADE ON UPDATE CASCADE,

                          CONSTRAINT fk_comments_writer
                              FOREIGN KEY (writer) REFERENCES users(id)
                                  ON DELETE CASCADE ON UPDATE CASCADE,

                          CONSTRAINT fk_comments_reply
                              FOREIGN KEY (reply_to) REFERENCES comments(id)
                                  ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DELIMITER //

CREATE TRIGGER trg_after_insert_questions
    AFTER INSERT ON questions
    FOR EACH ROW
BEGIN
    INSERT INTO question_reference (question_id, question_type)
    VALUES (NEW.id, 'personal');
END;
//

CREATE TRIGGER trg_after_insert_public_questions
    AFTER INSERT ON public_questions
    FOR EACH ROW
BEGIN
    INSERT INTO question_reference (question_id, question_type)
    VALUES (NEW.id, 'public');
END;
//

DELIMITER ;