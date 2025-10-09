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

    @Column(name = "completed_pieces_id", columnDefinition = "json")
    private String completed_pieces_id;

    @Column(nullable = false)
    private boolean completed = false;

    @Column(name = "is_playing_puzzle", nullable = false)
    private boolean is_playing_puzzle;

    private Long solverId;

    @Column(columnDefinition = "json")
    private String contributors;

    @Column(name = "families_id", nullable = false)
    private Long families_id;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private PuzzleCategory category;

    private Integer be_puzzle;


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

    public String getCompleted_pieces_id() {
        return completed_pieces_id;
    }

    public void setCompleted_pieces_id(String completedPiecesID) {
        this.completed_pieces_id = completedPiecesID;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isIs_playing_puzzle() {
        return is_playing_puzzle;
    }

    public void setPlayingPuzzle(boolean playingPuzzle) {
        is_playing_puzzle = playingPuzzle;
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

    public Long getFamilies_id() {
        return families_id;
    }

    public void setFamilies_id(Long familiesId) {
        this.families_id = familiesId;
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

    public Integer getBe_puzzle() {
        return be_puzzle;
    }

    public void setBe_puzzle(Integer be_puzzle) {
        this.be_puzzle = be_puzzle;
    }
}
