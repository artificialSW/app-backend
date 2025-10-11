package org.dcode.artificialswbackend.puzzle.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "puzzle")
public class Puzzle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer puzzleId;

    private String imagePath;

    private String capture_image_path;

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
    private Long familiesId;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private PuzzleCategory category;


    private Integer bePuzzle = 0;


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

    public String getCapture_image_path() {
        return capture_image_path;
    }

    public void setCapture_image_path(String capture_image_path) {
        this.capture_image_path = capture_image_path;
    }

    public boolean getIs_playing_puzzle() {
        return is_playing_puzzle;
    }

    public void setIs_playing_puzzle(boolean is_playing_puzzle) {
        this.is_playing_puzzle = is_playing_puzzle;
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

    public void setBePuzzle(Integer be_puzzle) {
        this.bePuzzle = be_puzzle;
    }

}
