package org.dcode.artificialswbackend.puzzle.dto;

import java.time.LocalDateTime;
import java.util.List;

public class InProgressPuzzleDto {
    private Integer puzzleId;
    private String imageUrl;
    private List<Integer> completedPiecesId;
    private Integer size;
    private LocalDateTime lastSavedAt;

    public InProgressPuzzleDto(Integer puzzleId, String imagePath, List<Integer> completedPiecesId, Integer size, LocalDateTime lastSavedTime) {
        this.puzzleId = puzzleId;
        this.imageUrl = imagePath;
        this.completedPiecesId = completedPiecesId;
        this.size = size;
        this.lastSavedAt = lastSavedTime;
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

    public List<Integer> getCompletedPiecesId() {
        return completedPiecesId;
    }

    public void setCompletedPiecesId(List<Integer> completedPiecesId) {
        this.completedPiecesId = completedPiecesId;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public LocalDateTime getLastSavedAt() {
        return lastSavedAt;
    }

    public void setLastSavedAt(LocalDateTime lastSavedAt) {
        this.lastSavedAt = lastSavedAt;
    }
}
