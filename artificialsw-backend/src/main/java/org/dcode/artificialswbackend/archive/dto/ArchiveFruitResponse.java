package org.dcode.artificialswbackend.archive.dto;

import java.time.LocalDateTime;

public class ArchiveFruitResponse {
    private Long fruitId;
    private String fruitName;
    private LocalDateTime archivedAt;

    public ArchiveFruitResponse(Long flowerId, String flowerName, LocalDateTime archivedAt) {
        this.fruitId = flowerId;
        this.fruitName = flowerName;
        this.archivedAt = archivedAt;
    }

    public Long getFruitId() {
        return fruitId;
    }

    public void setFruitId(Long fruitId) {
        this.fruitId = fruitId;
    }

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }

    public LocalDateTime getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }
}
