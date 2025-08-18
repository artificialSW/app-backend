package org.dcode.artificialswbackend.community.dto;

public class PublicQuestionDto {
    private Long id;

    private String content;
    private Integer likes;
    private Integer counts;

    public PublicQuestionDto(Long id, String content, Integer likes, Integer counts) {
        this.id = id;
        this.content = content;
        this.likes = likes;
        this.counts = counts;
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

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }
}


