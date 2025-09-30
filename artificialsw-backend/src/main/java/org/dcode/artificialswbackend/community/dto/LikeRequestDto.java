package org.dcode.artificialswbackend.community.dto;

public class LikeRequestDto {
    private String type; // "question", "public_question", "comment"
    private Long id;

    public LikeRequestDto() {}

    public LikeRequestDto(String type, Long id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
