package org.dcode.artificialswbackend.puzzle.dto;

import java.util.List;

public class PuzzleArchiveResponse {
    private Long puzzleId;
    private String imageUrl;
    private String category;
    private List<String> contributors;

    public PuzzleArchiveResponse(Long puzzleId, String imageUrl, String category, List<String> contributors) {
        this.puzzleId = puzzleId;
        this.imageUrl = imageUrl;
        this.category = category;
        this.contributors = contributors;
    }

    public Long getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(Long puzzleId) {
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
}
