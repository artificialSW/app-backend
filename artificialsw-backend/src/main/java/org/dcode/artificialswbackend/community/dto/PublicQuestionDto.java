package org.dcode.artificialswbackend.community.dto;

public class PublicQuestionDto {
    private Long id;

    private String content;
    private Boolean isPublic;
    private Boolean solved;
    private Integer likes;

    public PublicQuestionDto(Long id, String content, Boolean isPublic, Boolean solved, Integer likes) {
        this.id = id;
        this.content = content;
        this.isPublic = isPublic;
        this.solved = solved;
        this.likes = likes;
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

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Boolean getSolved() {
        return solved;
    }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}


