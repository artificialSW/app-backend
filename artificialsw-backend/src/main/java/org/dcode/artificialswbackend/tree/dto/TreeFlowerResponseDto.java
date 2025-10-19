package org.dcode.artificialswbackend.tree.dto;

import java.time.LocalDateTime;

public class TreeFlowerResponseDto {
    private Long flowerId;
    private String flowerName; // 한국어 꽃 이름
    private LocalDateTime archivedAt;
    
    public TreeFlowerResponseDto() {}
    
    public TreeFlowerResponseDto(Long flowerId, String flowerName, LocalDateTime archivedAt) {
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