package org.dcode.artificialswbackend.mypage.dto;

public class MyCommentResponseDto {
    private Long id;
    private String content;

    public MyCommentResponseDto() {}

    public MyCommentResponseDto(Long id, String content) {
        this.id = id;
        this.content = content;
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
}