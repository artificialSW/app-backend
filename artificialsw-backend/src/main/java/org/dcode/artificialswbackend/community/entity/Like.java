package org.dcode.artificialswbackend.community.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "likes", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "target_type", "target_id"}))
public class Like {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private TargetType targetType;
    
    @Column(name = "target_id", nullable = false)
    private Long targetId;
    
    @Column(name = "family_id", nullable = false)
    private Long familyId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
    
    public enum TargetType {
        question, public_question, comment
    }
    
    public Like() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
    
    public Like(Long userId, TargetType targetType, Long targetId, Long familyId) {
        this.userId = userId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.familyId = familyId;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public TargetType getTargetType() { return targetType; }
    public void setTargetType(TargetType targetType) { this.targetType = targetType; }
    
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    
    public Long getFamilyId() { return familyId; }
    public void setFamilyId(Long familyId) { this.familyId = familyId; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}