package org.dcode.artificialswbackend.mypage.dto;

import java.time.LocalDateTime;

public class MyCompletedPuzzleResponseDto {
    private Integer puzzleId;
    private String imagePath;
    private LocalDateTime completedTime;
    
    public MyCompletedPuzzleResponseDto() {}
    
    public MyCompletedPuzzleResponseDto(Integer puzzleId, String imagePath, LocalDateTime completedTime) {
        this.puzzleId = puzzleId;
        this.imagePath = imagePath;
        this.completedTime = completedTime;
    }
    
    public Integer getPuzzleId() {
        return puzzleId;
    }
    
    public void setPuzzleId(Integer puzzleId) {
        this.puzzleId = puzzleId;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public LocalDateTime getCompletedTime() {
        return completedTime;
    }
    
    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }
}