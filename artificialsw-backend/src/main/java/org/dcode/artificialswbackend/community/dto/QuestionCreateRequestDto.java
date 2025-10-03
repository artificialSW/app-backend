package org.dcode.artificialswbackend.community.dto;

public class QuestionCreateRequestDto {
    private Long receiver;
    private Boolean isPublic;
    private String content;

    public QuestionCreateRequestDto() {}

    public QuestionCreateRequestDto(Long receiver, Boolean isPublic, String content) {
        this.receiver = receiver;
        this.isPublic = isPublic;
        this.content = content;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}