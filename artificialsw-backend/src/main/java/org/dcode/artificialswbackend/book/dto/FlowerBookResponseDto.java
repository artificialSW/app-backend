package org.dcode.artificialswbackend.book.dto;

import java.util.List;

public class FlowerBookResponseDto {
    private List<Integer> resolvedFlowers;
    
    public FlowerBookResponseDto() {}
    
    public FlowerBookResponseDto(List<Integer> resolvedFlowers) {
        this.resolvedFlowers = resolvedFlowers;
    }
    
    public List<Integer> getResolvedFlowers() {
        return resolvedFlowers;
    }
    
    public void setResolvedFlowers(List<Integer> resolvedFlowers) {
        this.resolvedFlowers = resolvedFlowers;
    }
}