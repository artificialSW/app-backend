package org.dcode.artificialswbackend.archive.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Tree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long archiveId;
    private Long familyId;


    @Enumerated(EnumType.STRING)
    private TreeCategory treeCategory;

    public enum TreeCategory {
        FRUIT, FLOWER
    }
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(Long archiveId) {
        this.archiveId = archiveId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public TreeCategory getTreeCategory() {
        return treeCategory;
    }

    public void setTreeCategory(TreeCategory treeCategory) {
        this.treeCategory = treeCategory;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
