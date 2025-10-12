package org.dcode.artificialswbackend.community.dto;

import java.util.Map;

public class AiPredictionResponseDto {
    
    private String label;
    private Double confidence;
    private String flower;
    private Map<String, Double> probs;
    
    public AiPredictionResponseDto() {}
    
    // Getters and Setters
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public Double getConfidence() {
        return confidence;
    }
    
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
    
    public String getFlower() {
        return flower;
    }
    
    public void setFlower(String flower) {
        this.flower = flower;
    }
    
    public Map<String, Double> getProbs() {
        return probs;
    }
    
    public void setProbs(Map<String, Double> probs) {
        this.probs = probs;
    }
}