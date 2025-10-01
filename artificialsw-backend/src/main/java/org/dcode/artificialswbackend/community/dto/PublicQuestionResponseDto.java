package org.dcode.artificialswbackend.community.dto;

public class PublicQuestionResponseDto {
    private Long questionId;
    private String questionContent;
    private Integer count;

    public PublicQuestionResponseDto() {}

    public PublicQuestionResponseDto(Long questionId, String questionContent, Integer count) {
        this.questionId = questionId;
        this.questionContent = questionContent;
        this.count = count;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}