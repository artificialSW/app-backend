package org.dcode.artificialswbackend.community.dto;

public class CommentResponseDto {
    private Long replyId;
    private String flower;
    private Boolean isNewFlowerUnlocked;
    
    public CommentResponseDto() {}
    
    public CommentResponseDto(Long replyId) {
        this.replyId = replyId;
        this.flower = null;
        this.isNewFlowerUnlocked = false;
    }
    
    public CommentResponseDto(Long replyId, String flower, Boolean isNewFlowerUnlocked) {
        this.replyId = replyId;
        this.flower = flower;
        this.isNewFlowerUnlocked = isNewFlowerUnlocked;
    }
    
    // Getters and Setters
    public Long getReplyId() {
        return replyId;
    }
    
    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }
    
    public String getFlower() {
        return flower;
    }
    
    public void setFlower(String flower) {
        this.flower = flower;
    }
    
    public Boolean getIsNewFlowerUnlocked() {
        return isNewFlowerUnlocked;
    }
    
    public void setIsNewFlowerUnlocked(Boolean isNewFlowerUnlocked) {
        this.isNewFlowerUnlocked = isNewFlowerUnlocked;
    }
}