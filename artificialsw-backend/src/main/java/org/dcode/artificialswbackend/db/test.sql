USE test;

-- 기존 사용자 테이블
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       phone VARCHAR(20) NOT NULL UNIQUE,
                       age INT UNSIGNED NULL,
                       birthday DATE NULL,
                       gender ENUM('M', 'F') NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       nickname VARCHAR(50) NOT NULL UNIQUE,
                       profilePhoto VARCHAR(255) NULL,
                       family_type ENUM('자녀', '아빠', '엄마', '할아버지', '할머니') NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 기존 질문 테이블 (personal)
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
                           CONSTRAINT fk_questions_sender FOREIGN KEY (sender) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
                           CONSTRAINT fk_questions_receiver FOREIGN KEY (receiver) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 기존 공개 질문 테이블
CREATE TABLE public_questions (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  content TEXT NOT NULL,
                                  likes INT UNSIGNED NOT NULL DEFAULT 0,
                                  counts INT UNSIGNED NOT NULL DEFAULT 0,
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 기존 question_list 테이블
CREATE TABLE question_list (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               content TEXT NOT NULL,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 기존 question_reference 테이블
CREATE TABLE question_reference (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    question_id BIGINT NOT NULL,
                                    question_type ENUM('personal', 'public') NOT NULL,
                                    UNIQUE KEY uq_question(question_id, question_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 기존 comments 테이블
CREATE TABLE comments (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          question_ref_id BIGINT NOT NULL,
                          writer BIGINT NOT NULL,
                          content TEXT NOT NULL,
                          reply_to BIGINT DEFAULT NULL,
                          likes INT UNSIGNED NOT NULL DEFAULT 0,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                          CONSTRAINT fk_comments_question_ref FOREIGN KEY (question_ref_id) REFERENCES question_reference(id) ON DELETE CASCADE ON UPDATE CASCADE,
                          CONSTRAINT fk_comments_writer FOREIGN KEY (writer) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
                          CONSTRAINT fk_comments_reply FOREIGN KEY (reply_to) REFERENCES comments(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 트리거: questions 삽입 시 question_reference 자동 생성
DELIMITER //
CREATE TRIGGER trg_after_insert_questions
    AFTER INSERT ON questions
    FOR EACH ROW
BEGIN
    INSERT INTO question_reference (question_id, question_type)
    VALUES (NEW.id, 'personal');
END;
//

-- 트리거: public_questions 삽입 시 question_reference 자동 생성
CREATE TRIGGER trg_after_insert_public_questions
    AFTER INSERT ON public_questions
    FOR EACH ROW
BEGIN
    INSERT INTO question_reference (question_id, question_type)
    VALUES (NEW.id, 'public');
END;
//
DELIMITER ;

-- 꽃 도감 테이블
CREATE TABLE flower_catalog (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                name VARCHAR(50) NOT NULL,
                                keyword VARCHAR(50) NOT NULL,
                                unlocked BOOLEAN DEFAULT FALSE
);

-- 열매 도감 테이블
CREATE TABLE fruit_catalog (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               name VARCHAR(50) NOT NULL,
                               keyword VARCHAR(50) NOT NULL,
                               unlocked BOOLEAN DEFAULT FALSE
);

-- 아카이브 테이블 (월별 관리)
CREATE TABLE archives (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          month INT NOT NULL,
                          year INT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 나무 테이블 (아카이브 소속, main/custom 구분)
CREATE TABLE tree (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       archive_id BIGINT NOT NULL,
                       name VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       tree_category ENUM('main', 'custom') NOT NULL DEFAULT 'main',
                       display_order INT DEFAULT 0,
                       FOREIGN KEY (archive_id) REFERENCES archives(id)
);

-- 꽃 테이블 (나무별 꽃 객체, 질문 참조 포함)
CREATE TABLE flowers (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         tree_id BIGINT NOT NULL,
                         flower_catalog_id BIGINT NOT NULL,
                         question_ref_id BIGINT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (tree_id) REFERENCES tree(id),
                         FOREIGN KEY (flower_catalog_id) REFERENCES flower_catalog(id),
                         FOREIGN KEY (question_ref_id) REFERENCES question_reference(id)
);

-- 열매 테이블 (나무별 열매 객체, 질문 참조 포함)
CREATE TABLE fruits (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        tree_id BIGINT NOT NULL,
                        fruit_catalog_id BIGINT NOT NULL,
                        question_ref_id BIGINT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (tree_id) REFERENCES tree(id),
                        FOREIGN KEY (fruit_catalog_id) REFERENCES fruit_catalog(id),
                        FOREIGN KEY (question_ref_id) REFERENCES question_reference(id)
);

-- 커스텀 나무 대표 꽃/열매 선택 테이블
CREATE TABLE custom_tree_featured_items (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            tree_id BIGINT NOT NULL,
                                            item_type ENUM('flower', 'fruit') NOT NULL,
                                            item_id BIGINT NOT NULL, -- item_type에 따라 달라지는 flower, fruit id
                                            display_order INT NOT NULL DEFAULT 0,
                                            FOREIGN KEY (tree_id) REFERENCES tree(id)
);
