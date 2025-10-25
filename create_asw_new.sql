CREATE DATABASE IF NOT EXISTS asw_new DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_0900_ai_ci;
USE asw_new;

-- families 테이블
CREATE TABLE families (
    id bigint NOT NULL AUTO_INCREMENT,
    verification_code varchar(100) NOT NULL,
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- users 테이블
CREATE TABLE users (
    id bigint NOT NULL AUTO_INCREMENT,
    family_id bigint NOT NULL,
    name varchar(100) NOT NULL,
    phone varchar(20) NOT NULL,
    age int unsigned DEFAULT NULL,
    birthday date DEFAULT NULL,
    gender enum('M','F') NOT NULL,
    password varchar(255) NOT NULL,
    nickname varchar(50) NOT NULL,
    profile_photo varchar(255) DEFAULT NULL,
    family_type enum('자녀','아빠','엄마','할아버지','할머니') DEFAULT NULL,
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY phone (phone),
    UNIQUE KEY nickname (nickname),
    KEY fk_users_family (family_id),
    CONSTRAINT fk_users_family FOREIGN KEY (family_id) REFERENCES families (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- FCM 토큰 테이블
CREATE TABLE fcm_tokens (
    id bigint NOT NULL AUTO_INCREMENT,
    user_id bigint NOT NULL,
    family_id bigint NOT NULL,
    token varchar(500) NOT NULL,
    device_type varchar(50) NOT NULL DEFAULT 'MOBILE',
    is_active tinyint(1) NOT NULL DEFAULT '1',
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY unique_user_token (user_id, token),
    KEY idx_family_active (family_id, is_active),
    KEY idx_user_active (user_id, is_active),
    KEY fk_fcm_tokens_user (user_id),
    KEY fk_fcm_tokens_family (family_id),
    CONSTRAINT fk_fcm_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_fcm_tokens_family FOREIGN KEY (family_id) REFERENCES families (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 기존 asw에서 데이터 복사
INSERT INTO asw_new.families SELECT * FROM asw.families;
INSERT INTO asw_new.users SELECT * FROM asw.users;
