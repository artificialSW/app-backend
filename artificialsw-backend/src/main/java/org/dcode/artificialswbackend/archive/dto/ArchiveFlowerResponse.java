package org.dcode.artificialswbackend.archive.dto;

import java.time.LocalDateTime;

public class ArchiveFlowerResponse {
    private Long flowerId;
    private String flowerName;
    private LocalDateTime archivedAt;

    public ArchiveFlowerResponse(Long flowerId, String flowerName, LocalDateTime archivedAt) {
        this.flowerId = flowerId;
        this.flowerName = flowerName;
        this.archivedAt = archivedAt;
    }

    public Long getFlowerId() {
        return flowerId;
    }

    public void setFlowerId(Long flowerId) {
        this.flowerId = flowerId;
    }

    public String getFlowerName() {
        return flowerName;
    }

    public void setFlowerName(String flowerName) {
        this.flowerName = flowerName;
    }

    public LocalDateTime getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }
}
