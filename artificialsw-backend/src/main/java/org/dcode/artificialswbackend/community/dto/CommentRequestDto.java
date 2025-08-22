package org.dcode.artificialswbackend.community.dto;

public class CommentRequestDto {
    private Long questionId;
    private String content;
    private Long replyTo;

    public CommentRequestDto() {}

    public CommentRequestDto(Long questionId, String content, Long replyTo) {
        this.questionId = questionId;
        this.content = content;
        this.replyTo = replyTo;
    }

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

    public Long getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Long replyTo) {
        this.replyTo = replyTo;
    }
}
