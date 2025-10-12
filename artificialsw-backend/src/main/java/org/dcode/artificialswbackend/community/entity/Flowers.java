package org.dcode.artificialswbackend.community.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flowers")
public class Flowers {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tree_id", nullable = false)
    private Long treeId;
    
    @Column(name = "question_ref_id")
    private Long questionRefId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "flower", nullable = false)
    private FlowerType flower;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public enum FlowerType {
        CAMELLIA("camellia"),      // 동백꽃
        ROSE("rose"),              // 장미
        ACACIA("acacia"),          // 아카시아꽃
        HYDRANGEA("hydrangea"),    // 수국
        PLUM_BLOSSOM("plum_blossom"), // 매화꽃
        TULIP("tulip"),            // 튤립
        PEAR_BLOSSOM("pear_blossom"), // 팥배꽃
        VIOLET("violet"),          // 재비꽃
        CHERRY_BLOSSOM("cherry_blossom"), // 벚꽃
        COSMOS("cosmos"),          // 코스모스
        MAGNOLIA("magnolia"),      // 목련
        SUNFLOWER("sunflower");    // 해바라기
        
        private final String value;
        
        FlowerType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static FlowerType fromKoreanName(String koreanName) {
            switch (koreanName) {
                case "동백꽃": return CAMELLIA;
                case "장미": return ROSE;
                case "아카시아꽃": return ACACIA;
                case "수국": return HYDRANGEA;
                case "매화꽃": return PLUM_BLOSSOM;
                case "튤립": return TULIP;
                case "팥배꽃": return PEAR_BLOSSOM;
                case "재비꽃": return VIOLET;
                case "벚꽃": return CHERRY_BLOSSOM;
                case "코스모스": return COSMOS;
                case "목련": return MAGNOLIA;
                case "해바라기": return SUNFLOWER;
                default: return ROSE; // 기본값
            }
        }
    }
    
    // 기본 생성자
    public Flowers() {}
    
    // 생성자
    public Flowers(Long treeId, Long questionRefId, FlowerType flower) {
        this.treeId = treeId;
        this.questionRefId = questionRefId;
        this.flower = flower;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getTreeId() {
        return treeId;
    }
    
    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }
    
    public Long getQuestionRefId() {
        return questionRefId;
    }
    
    public void setQuestionRefId(Long questionRefId) {
        this.questionRefId = questionRefId;
    }
    
    public FlowerType getFlower() {
        return flower;
    }
    
    public void setFlower(FlowerType flower) {
        this.flower = flower;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}