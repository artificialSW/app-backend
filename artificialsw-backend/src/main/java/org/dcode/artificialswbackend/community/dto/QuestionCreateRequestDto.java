package org.dcode.artificialswbackend.community.dto;

public class QuestionCreateRequestDto {
    private Long receiverId;
    private Integer visibility;
    private String content;

    public QuestionCreateRequestDto() {}

    public QuestionCreateRequestDto(Long receiverId, Integer visibility, String content) {
        this.receiverId = receiverId;
        this.visibility = visibility;
        this.content = content;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}