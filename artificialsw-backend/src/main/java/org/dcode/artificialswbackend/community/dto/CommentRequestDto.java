package org.dcode.artificialswbackend.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentRequestDto {
    @JsonProperty("question_ref_id")
    private Long questionRefId;
    private String content;
    private Long replyTo;

    public CommentRequestDto() {}

    public CommentRequestDto(Long questionRefId, String content, Long replyTo) {
        this.questionRefId = questionRefId;
        this.content = content;
        this.replyTo = replyTo;
    }

    public Long getQuestionRefId() {
        return questionRefId;
    }

    public void setQuestionRefId(Long questionRefId) {
        this.questionRefId = questionRefId;
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
