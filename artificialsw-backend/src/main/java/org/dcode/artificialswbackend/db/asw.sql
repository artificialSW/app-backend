USE asw;

-- families 테이블

-- users 테이블

-- questions 테이블

-- public_questions 테이블

-- question_reference 테이블

-- comments 테이블 (family_id 컬럼 추가)

-- question_list 테이블 (family_id 포함하지 않음, 관리용)

-- flower_catalog 테이블 (family_id 불필요)

-- fruit_catalog 테이블 (family_id 불필요)

-- archives 테이블 (멀티 테넌트 용 family_id 포함)

-- tree 테이블 (멀티 테넌트 용 family_id 포함)

-- flowers 테이블 (tree_id 통해 family_id 간접관리)

-- fruits 테이블 (tree_id 통해 family_id 간접관리)

-- custom_tree_featured_items 테이블 (tree_id 통해 family_id 간접관리) - 삭제할거임

-- puzzle 테이블 (멀티 테넌트 용 family_id 포함)) - puzzle 아카이브로 만들어야겠네.....

-- puzzle_category 테이블 (puzzle_id 통해 family_id 간접관리)

-- puzzle_ai_keyword 테이블 (puzzle_id 통해 family_id 간접관리)

-- puzzle_pieces 테이블



DELIMITER //

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
