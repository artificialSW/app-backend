package org.dcode.artificialswbackend.puzzle.dto;

import java.util.List;

public class PuzzleInProgressResponse {
    private Integer puzzleId;
    private String captureImageUrl;
    private List<String> contributors;
    private String category;
    private int completedPiecesCount;
    private int size;


    public PuzzleInProgressResponse(Integer puzzleId, String imageUrl, List<String> contributors,
                                    String category, int completedPiecesCount, int size) {
        this.puzzleId = puzzleId;
        this.captureImageUrl = imageUrl;
        this.contributors = contributors;
        this.category = category;
        this.completedPiecesCount = completedPiecesCount;
        this.size = size;
    }

    public Integer getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(Integer puzzleId) {
        this.puzzleId = puzzleId;
    }

    public String getCaptureImageUrl() {
        return captureImageUrl;
    }

    public void setCaptureImageUrl(String captureImageUrl) {
        this.captureImageUrl = captureImageUrl;
    }

    public List<String> getContributors() {
        return contributors;
    }

    public void setContributors(List<String> contributors) {
        this.contributors = contributors;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCompletedPiecesCount() {
        return completedPiecesCount;
    }

    public void setCompletedPiecesCount(int completedPiecesCount) {
        this.completedPiecesCount = completedPiecesCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
