package org.dcode.artificialswbackend.community.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flower_catalog")
public class FlowerCatalog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "family_id", nullable = false)
    private Long familyId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "flower_type", nullable = false)
    private Flowers.FlowerType flowerType;
    
    @Column(name = "unlocked", nullable = false)
    private Boolean unlocked = false;
    
    @Column(name = "unlocked_at")
    private LocalDateTime unlockedAt;
    
    // 기본 생성자
    public FlowerCatalog() {}
    
    // 생성자
    public FlowerCatalog(Long familyId, Flowers.FlowerType flowerType) {
        this.familyId = familyId;
        this.flowerType = flowerType;
        this.unlocked = false;
    }
    
    // unlock 메서드
    public void unlock() {
        this.unlocked = true;
        this.unlockedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getFamilyId() {
        return familyId;
    }
    
    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }
    
    public Flowers.FlowerType getFlowerType() {
        return flowerType;
    }
    
    public void setFlowerType(Flowers.FlowerType flowerType) {
        this.flowerType = flowerType;
    }
    
    public Boolean getUnlocked() {
        return unlocked;
    }
    
    public void setUnlocked(Boolean unlocked) {
        this.unlocked = unlocked;
    }
    
    public LocalDateTime getUnlockedAt() {
        return unlockedAt;
    }
    
    public void setUnlockedAt(LocalDateTime unlockedAt) {
        this.unlockedAt = unlockedAt;
    }
}