package org.dcode.artificialswbackend.puzzle.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "puzzle")
public class Puzzle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer puzzleId;

    private String imagePath;

    private Integer size;

    @Column(columnDefinition = "json")
    private String completedPiecesID;

    @Column(nullable = false)
    private boolean completed = false;

    @Column(name = "isPlayingPuzzle", nullable = false)
    private boolean isPlayingPuzzle;

    private Long solverId;

    @Column(columnDefinition = "json")
    private String contributors;

    @Column(nullable = false)
    private Long familiesId;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private PuzzleCategory category;

    private Integer bePuzzle;


    public Puzzle() {
    }

    public Integer getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(Integer puzzleId) {
        this.puzzleId = puzzleId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getCompletedPiecesID() {
        return completedPiecesID;
    }

    public void setCompletedPiecesID(String completedPiecesID) {
        this.completedPiecesID = completedPiecesID;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isPlayingPuzzle() {
        return isPlayingPuzzle;
    }

    public void setPlayingPuzzle(boolean playingPuzzle) {
        isPlayingPuzzle = playingPuzzle;
    }

    public Long getSolverId() {
        return solverId;
    }

    public void setSolverId(Long solverId) {
        this.solverId = solverId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PuzzleCategory getCategory() {
        return category;
    }

    public void setCategory(PuzzleCategory category) {
        this.category = category;
    }

    public Integer getBePuzzle() {
        return bePuzzle;
    }

    public void setBePuzzle(Integer bePuzzle) {
        this.bePuzzle = bePuzzle;
    }
}
