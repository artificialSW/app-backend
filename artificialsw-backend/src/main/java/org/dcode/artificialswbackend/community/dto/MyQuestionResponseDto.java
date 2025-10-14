package org.dcode.artificialswbackend.community.dto;

public class MyQuestionResponseDto {
    private Long questionId;
    private String content;
    private boolean resolved;
    private String questionType; // "personal" or "public"

    public MyQuestionResponseDto(Long questionId, String content, boolean resolved, String questionType) {
        this.questionId = questionId;
        this.content = content;
        this.resolved = resolved;
        this.questionType = questionType;
    }

    // Getters and Setters
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
}