package org.dcode.artificialswbackend.archive.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class IslandArchives {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long familyId;
    private Integer month;
    private Integer year;
    private Integer period;

    @Column(name = "puzzle_score")
    private Integer puzzleScore = 0;
    @Column(name = "community_score")
    private Integer communityScore = 0;

    @Column(name = "capture_image_path", nullable = true, length = 255)
    private String captureImagePath;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

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

    public Integer getPuzzleScore() {
        return puzzleScore;
    }
    public void setPuzzleScore(Integer puzzleScore) {
        this.puzzleScore = puzzleScore;
    }

    public Integer getCommunityScore() {
        return communityScore;
    }
    public void setCommunityScore(Integer communityScore) {
        this.communityScore = communityScore;
    }

    public String getCaptureImagePath() {
        return captureImagePath;
    }

    public void setCaptureImagePath(String captureImagePath) {
        this.captureImagePath = captureImagePath;
    }
}
