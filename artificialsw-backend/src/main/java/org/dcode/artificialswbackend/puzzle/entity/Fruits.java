package org.dcode.artificialswbackend.puzzle.entity;

import jakarta.persistence.*;

import java.security.Timestamp;

@Entity
@Table(name = "fruits")
public class Fruits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long treeId;

    @Column(name = "created_at")
    private Timestamp createdAt;

    private Integer puzzleId;

    @Column(length = 255)
    private String message;

    @Column(length = 100)
    private String fruitName;

    @Column(length = 50, nullable = false)
    private String category;

    @Column(columnDefinition = "json")
    private String contributors; // JSON 타입은 String으로 매핑

    public Fruits() {
    }

    // Getter & Setter
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(Integer puzzleId) {
        this.puzzleId = puzzleId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
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
}
