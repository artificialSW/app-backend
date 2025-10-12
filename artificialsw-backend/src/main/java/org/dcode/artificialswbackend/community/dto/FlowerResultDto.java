package org.dcode.artificialswbackend.community.dto;

public class FlowerResultDto {
    private String flower;
    private boolean isNewlyUnlocked;
    
    public FlowerResultDto() {}
    
    public FlowerResultDto(String flower, boolean isNewlyUnlocked) {
        this.flower = flower;
        this.isNewlyUnlocked = isNewlyUnlocked;
    }
    
    public String getFlower() {
        return flower;
    }
    
    public void setFlower(String flower) {
        this.flower = flower;
    }
    
    public boolean isNewlyUnlocked() {
        return isNewlyUnlocked;
    }
    
    public void setNewlyUnlocked(boolean newlyUnlocked) {
        isNewlyUnlocked = newlyUnlocked;
    }
}