USE test;

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
);

CREATE TABLE public_questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    content TEXT NOT NULL,
    likes INT UNSIGNED NOT NULL DEFAULT 0,
    counts INT UNSIGNED NOT NULL DEFAULT 0,   -- 용도: 조회수/응답수 등(정의 필요)
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                 ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE question_list (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT NOT NULL,        -- 연결된 질문
    writer BIGINT NOT NULL,             -- 작성자 (users.id 참조)
    content TEXT NOT NULL,              -- 댓글 내용
    reply_to BIGINT DEFAULT NULL,       -- 다른 댓글 id (답글)
    likes INT UNSIGNED NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                 ON UPDATE CURRENT_TIMESTAMP,

    -- FK: 질문
    CONSTRAINT fk_comments_question
        FOREIGN KEY (question_id) REFERENCES questions(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    -- FK: 작성자
    CONSTRAINT fk_comments_writer
        FOREIGN KEY (writer) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    -- Self-reference: 다른 댓글에 대한 답글
    CONSTRAINT fk_comments_reply
        FOREIGN KEY (reply_to) REFERENCES comments(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;
