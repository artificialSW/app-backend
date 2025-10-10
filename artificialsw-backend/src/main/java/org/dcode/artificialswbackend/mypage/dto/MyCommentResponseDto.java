package org.dcode.artificialswbackend.mypage.dto;

public class MyCommentResponseDto {
    private Long id;
    private String content;
    private String questionType; // "questions" or "public_questions"
    private Long questionId;
    private String questionContent;

    public MyCommentResponseDto() {}

    public MyCommentResponseDto(Long id, String content, String questionType, Long questionId, String questionContent) {
        this.id = id;
        this.content = content;
        this.questionType = questionType;
        this.questionId = questionId;
        this.questionContent = questionContent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }
}