package org.dcode.artificialswbackend.puzzle.entity;


import jakarta.persistence.*;

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
    private Boolean completed;
    private Boolean isPlayingPuzzle;
    private Long solverId;
    @Column(columnDefinition = "json")
    private String contributors;
    private Long familiesId;
    private String message;

    public Puzzle() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getFamiliesId() {
        return familiesId;
    }

    public void setFamiliesId(Long familiesId) {
        this.familiesId = familiesId;
    }

    public String getContributors() {
        return contributors;
    }

    public void setContributors(String contributors) {
        this.contributors = contributors;
    }

    public Long getSolverId() {
        return solverId;
    }

    public void setSolverId(Long solverId) {
        this.solverId = solverId;
    }

    public Boolean getIsPlayingPuzzle() {
        return isPlayingPuzzle;
    }

    public void setIsPlayingPuzzle(Boolean playingPuzzle) {
        isPlayingPuzzle = playingPuzzle;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getCompletedPiecesID() {
        return completedPiecesID;
    }

    public void setCompletedPiecesID(String completedPiecesID) {
        this.completedPiecesID = completedPiecesID;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(Integer puzzleId) {
        this.puzzleId = puzzleId;
    }
}
