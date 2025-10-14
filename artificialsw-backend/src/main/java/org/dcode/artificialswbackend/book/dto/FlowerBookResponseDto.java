package org.dcode.artificialswbackend.book.dto;

import java.time.LocalDateTime;

public class FlowerBookResponseDto {
    private String flowerName; // 한국어 꽃 이름
    private String flowerType; // 영어 타입 (enum value)
    private Boolean unlocked;
    private LocalDateTime unlockedAt;
    
    public FlowerBookResponseDto() {}
    
    public FlowerBookResponseDto(String flowerName, String flowerType, Boolean unlocked, LocalDateTime unlockedAt) {
        this.flowerName = flowerName;
        this.flowerType = flowerType;
        this.unlocked = unlocked;
        this.unlockedAt = unlockedAt;
    }
    
    public String getFlowerName() {
        return flowerName;
    }
    
    public void setFlowerName(String flowerName) {
        this.flowerName = flowerName;
    }
    
    public String getFlowerType() {
        return flowerType;
    }
    
    public void setFlowerType(String flowerType) {
        this.flowerType = flowerType;
    }
    
    public Boolean getUnlocked() {
        return unlocked;
    }
    
    public void setUnlocked(Boolean unlocked) {
        this.unlocked = unlocked;
    }
    
    public LocalDateTime getUnlockedAt() {
        return unlockedAt;
    }
    
    public void setUnlockedAt(LocalDateTime unlockedAt) {
        this.unlockedAt = unlockedAt;
    }
}