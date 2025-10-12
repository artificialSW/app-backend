package org.dcode.artificialswbackend.puzzle.dto;

public class PuzzleCreateResponse {
    private Integer puzzleId;
    private String imageURL;
    private String category;
    private String message;

    public PuzzleCreateResponse() {}

    public PuzzleCreateResponse(Integer puzzleId, String imageURL, String category, String message) {
        this.puzzleId = puzzleId;
        this.imageURL = imageURL;
        this.category = category;
        this.message = message;
    }

    public Integer getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(Integer puzzleId) {
        this.puzzleId = puzzleId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
