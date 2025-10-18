package org.dcode.artificialswbackend.puzzle.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "puzzle_archive")
public class PuzzleArchive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imagePath;
    private String category;
    @Column(columnDefinition = "json")
    private String contributors;
    private Long familiesId;
    private LocalDateTime archivedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContributors() {
        return contributors;
    }

    public void setContributors(String contributors) {
        this.contributors = contributors;
    }

    public Long getFamiliesId() {
        return familiesId;
    }

    public void setFamiliesId(Long familiesId) {
        this.familiesId = familiesId;
    }

    public LocalDateTime getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }
}
