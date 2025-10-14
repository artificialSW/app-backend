package org.dcode.artificialswbackend.puzzle.dto;

import java.util.List;

public class PuzzleCompletedResponse {
    private Integer puzzleId;
    private String imageUrl;
    private String category;
    private List<String> contributors;
    private String message;

    public PuzzleCompletedResponse(Integer puzzleId, String imageUrl, String category,
                                   List<String> contributors, String message) {
        this.puzzleId = puzzleId;
        this.imageUrl = imageUrl;
        this.category = category;
        this.contributors = contributors;
        this.message = message;
    }

    public Integer getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(Integer puzzleId) {
        this.puzzleId = puzzleId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getContributors() {
        return contributors;
    }

    public void setContributors(List<String> contributors) {
        this.contributors = contributors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
