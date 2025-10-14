package org.dcode.artificialswbackend.community.dto;

public class CommentResponseDto {
    private Long replyId;
    private String content;

    public CommentResponseDto() {}

    public CommentResponseDto(Long replyId, String content) {
        this.replyId = replyId;
        this.content = content;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}