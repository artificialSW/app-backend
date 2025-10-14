package org.dcode.artificialswbackend.puzzle.dto;

import java.time.LocalDateTime;

public class CompletedPuzzleDto {
    private Integer puzzleId;
    private String imageUrl;
    private String title;
    private Integer size;
    private LocalDateTime completedAt;

    public CompletedPuzzleDto(Integer puzzleId, String imagePath, String category, Integer size, LocalDateTime completedTime) {
        this.puzzleId = puzzleId;
        this.imageUrl = imagePath;
        this.title = category;
        this.size = size;
        this.completedAt = completedTime;
    }

    public Integer getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(Integer puzzleId) {
        this.puzzleId = puzzleId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
