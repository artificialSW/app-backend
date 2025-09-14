USE test;

-- families 테이블
CREATE TABLE families (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- users 테이블
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       family_id BIGINT NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       phone VARCHAR(20) NOT NULL UNIQUE,
                       age INT UNSIGNED NULL,
                       birthday DATE NULL,
                       gender ENUM('M', 'F') NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       nickname VARCHAR(50) NOT NULL UNIQUE,
                       profile_photo VARCHAR(255) NULL,
                       family_type ENUM('자녀', '아빠', '엄마', '할아버지', '할머니') NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
                       CONSTRAINT fk_users_family FOREIGN KEY (family_id) REFERENCES families(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- questions 테이블
CREATE TABLE questions (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           family_id BIGINT NOT NULL,
                           content TEXT NOT NULL,
                           sender BIGINT NOT NULL,
                           receiver BIGINT NOT NULL,
                           isPublic BOOLEAN DEFAULT TRUE,
                           solved BOOLEAN DEFAULT FALSE,
                           likes INT UNSIGNED DEFAULT 0,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           CONSTRAINT fk_questions_sender FOREIGN KEY (sender) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
                           CONSTRAINT fk_questions_receiver FOREIGN KEY (receiver) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
                           CONSTRAINT fk_questions_family FOREIGN KEY (family_id) REFERENCES families(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- public_questions 테이블
CREATE TABLE public_questions (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  family_id BIGINT NOT NULL,
                                  content TEXT NOT NULL,
                                  likes INT UNSIGNED NOT NULL DEFAULT 0,
                                  counts INT UNSIGNED NOT NULL DEFAULT 0,
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  CONSTRAINT fk_public_questions_family FOREIGN KEY (family_id) REFERENCES families(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- question_reference 테이블
CREATE TABLE question_reference (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    question_id BIGINT NOT NULL,
                                    question_type ENUM('personal', 'public') NOT NULL,
                                    family_id BIGINT NOT NULL,
                                    UNIQUE KEY uq_question(question_id, question_type),
                                    CONSTRAINT fk_question_reference_family FOREIGN KEY (family_id) REFERENCES families(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- comments 테이블 (family_id 컬럼 추가)
CREATE TABLE comments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          question_ref_id BIGINT NOT NULL,
                          writer BIGINT NOT NULL,
                          family_id BIGINT NOT NULL,
                          content TEXT NOT NULL,
                          reply_to BIGINT DEFAULT NULL,
                          likes INT UNSIGNED NOT NULL DEFAULT 0,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          CONSTRAINT fk_comments_question_ref FOREIGN KEY (question_ref_id) REFERENCES question_reference(id) ON DELETE CASCADE ON UPDATE CASCADE,
                          CONSTRAINT fk_comments_writer FOREIGN KEY (writer) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
                          CONSTRAINT fk_comments_reply FOREIGN KEY (reply_to) REFERENCES comments(id) ON DELETE CASCADE ON UPDATE CASCADE,
                          CONSTRAINT fk_comments_family FOREIGN KEY (family_id) REFERENCES families(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- question_list 테이블 (family_id 포함하지 않음, 관리용)
CREATE TABLE question_list (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               content TEXT NOT NULL,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- flower_catalog 테이블 (family_id 불필요)
CREATE TABLE flower_catalog (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                name VARCHAR(50) NOT NULL,
                                keyword VARCHAR(50) NOT NULL,
                                unlocked BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- fruit_catalog 테이블 (family_id 불필요)
CREATE TABLE fruit_catalog (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               name VARCHAR(50) NOT NULL,
                               keyword VARCHAR(50) NOT NULL,
                               unlocked BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- archives 테이블 (멀티 테넌트 용 family_id 포함)
CREATE TABLE archives (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          family_id BIGINT NOT NULL,
                          month INT NOT NULL,
                          year INT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_archives_family FOREIGN KEY (family_id) REFERENCES families(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- tree 테이블 (멀티 테넌트 용 family_id 포함)
CREATE TABLE tree (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      archive_id BIGINT NOT NULL,
                      family_id BIGINT NOT NULL,
                      name VARCHAR(50) NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      tree_category ENUM('main', 'custom') NOT NULL DEFAULT 'main',
                      display_order INT DEFAULT 0,
                      FOREIGN KEY (archive_id) REFERENCES archives(id),
                      CONSTRAINT fk_tree_family FOREIGN KEY (family_id) REFERENCES families(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- flowers 테이블 (tree_id 통해 family_id 간접관리)
CREATE TABLE flowers (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         tree_id BIGINT NOT NULL,
                         flower_catalog_id BIGINT NOT NULL,
                         question_ref_id BIGINT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (tree_id) REFERENCES tree(id),
                         FOREIGN KEY (flower_catalog_id) REFERENCES flower_catalog(id),
                         FOREIGN KEY (question_ref_id) REFERENCES question_reference(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- fruits 테이블 (tree_id 통해 family_id 간접관리)
CREATE TABLE fruits (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        tree_id BIGINT NOT NULL,
                        fruit_catalog_id BIGINT NOT NULL,
                        question_ref_id BIGINT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (tree_id) REFERENCES tree(id),
                        FOREIGN KEY (fruit_catalog_id) REFERENCES fruit_catalog(id),
                        FOREIGN KEY (question_ref_id) REFERENCES question_reference(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- custom_tree_featured_items 테이블 (tree_id 통해 family_id 간접관리)
CREATE TABLE custom_tree_featured_items (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            tree_id BIGINT NOT NULL,
                                            item_type ENUM('flower', 'fruit') NOT NULL,
                                            item_id BIGINT NOT NULL,
                                            display_order INT NOT NULL DEFAULT 0,
                                            FOREIGN KEY (tree_id) REFERENCES tree(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- puzzle 테이블 (family_id 불필요)
CREATE TABLE puzzle (
                        puzzle_id INT AUTO_INCREMENT PRIMARY KEY,
                        image_path VARCHAR(255) NOT NULL,
                        size INT NULL,
                        category VARCHAR(50) NULL,
                        completedPiecesID JSON NULL,
                        completed BOOLEAN NOT NULL DEFAULT FALSE,
                        isPlayingPuzzle BOOLEAN NOT NULL DEFAULT FALSE,
                        solverId BIGINT NULL,
                        contributors JSON NULL,
                        CONSTRAINT fk_puzzle_solver FOREIGN KEY (solverId) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE puzzle_category (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 puzzle_id INT NOT NULL,
                                 category VARCHAR(50) NOT NULL,
                                 FOREIGN KEY (puzzle_id) REFERENCES puzzle(puzzle_id) ON DELETE CASCADE
);

CREATE TABLE puzzle_ai_keyword (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   puzzle_id INT NOT NULL,
                                   keyword VARCHAR(100) NOT NULL,
                                   FOREIGN KEY (puzzle_id) REFERENCES puzzle(puzzle_id) ON DELETE CASCADE
);

CREATE TABLE puzzle_pieces (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               puzzle_id INT NOT NULL,
                               piece_id VARCHAR(50) NOT NULL,
                               position JSON NOT NULL,
                               FOREIGN KEY (puzzle_id) REFERENCES puzzle(puzzle_id) ON DELETE CASCADE,
                               UNIQUE KEY uq_puzzle_piece(puzzle_id, piece_id)
);



DELIMITER //

DROP TRIGGER IF EXISTS trg_after_insert_questions;
DROP TRIGGER IF EXISTS trg_after_insert_public_questions;
DROP TRIGGER IF EXISTS trg_before_insert_comments;

CREATE TRIGGER trg_after_insert_questions
    AFTER INSERT ON questions
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
