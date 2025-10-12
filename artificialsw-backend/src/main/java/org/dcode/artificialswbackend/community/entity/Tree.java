package org.dcode.artificialswbackend.community.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tree")
public class Tree {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "archive_id", nullable = false)
    private Long archiveId;
    
    @Column(name = "family_id", nullable = false)
    private Long familyId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tree_category", nullable = false)
    private TreeCategory treeCategory;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public enum TreeCategory {
        FLOWER("flower"),
        FRUIT("fruit");
        
        private final String value;
        
        TreeCategory(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    // 기본 생성자
    public Tree() {}
    
    // 생성자
    public Tree(Long archiveId, Long familyId, TreeCategory treeCategory) {
        this.archiveId = archiveId;
        this.familyId = familyId;
        this.treeCategory = treeCategory;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
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