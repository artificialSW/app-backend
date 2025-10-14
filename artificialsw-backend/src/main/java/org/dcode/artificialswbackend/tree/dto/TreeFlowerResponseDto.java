package org.dcode.artificialswbackend.tree.dto;

import java.time.LocalDateTime;

public class TreeFlowerResponseDto {
    private Long flowerId;
    private String flowerName; // 한국어 꽃 이름
    private LocalDateTime createdAt;
    
    public TreeFlowerResponseDto() {}
    
    public TreeFlowerResponseDto(Long flowerId, String flowerName, LocalDateTime createdAt) {
        this.flowerId = flowerId;
        this.flowerName = flowerName;
        this.createdAt = createdAt;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}