package org.dcode.artificialswbackend.community.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "island_archives")
public class IslandArchive {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "family_id", nullable = false)
    private Long familyId;
    
    @Column(name = "month", nullable = false)
    private Integer month;
    
    @Column(name = "year", nullable = false)
    private Integer year;
    
    @Column(name = "period", nullable = false)
    private Integer period;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // 기본 생성자
    public IslandArchive() {}
    
    // 생성자
    public IslandArchive(Long familyId, Integer month, Integer year, Integer period) {
        this.familyId = familyId;
        this.month = month;
        this.year = year;
        this.period = period;
        this.createdAt = LocalDateTime.now();
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
    
    public Integer getMonth() {
        return month;
    }
    
    public void setMonth(Integer month) {
        this.month = month;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public Integer getPeriod() {
        return period;
    }
    
    public void setPeriod(Integer period) {
        this.period = period;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}