package org.dcode.artificialswbackend.puzzle.dto;

import java.util.List;

public class PuzzleHomeResponse {
    private List<String> category;
    private List<InProgressPuzzleDto> inProgress;
    private List<CompletedPuzzleDto> completedThisWeek;
    private boolean isFull;
    private boolean isEmpty;

    public PuzzleHomeResponse(List<String> category, List<InProgressPuzzleDto> inProgress, List<CompletedPuzzleDto> completedThisWeek, boolean isFull, boolean isEmpty) {
        this.category = category;
        this.inProgress = inProgress;
        this.completedThisWeek = completedThisWeek;
        this.isFull = isFull;
        this.isEmpty = isEmpty;
    }


    public List<String> getCategory() { return category; }
    public void setCategory(List<String> category) { this.category = category; }

    public List<InProgressPuzzleDto> getInProgress() { return inProgress; }
    public void setInProgress(List<InProgressPuzzleDto> inProgress) { this.inProgress = inProgress; }

    public List<CompletedPuzzleDto> getCompletedThisWeek() { return completedThisWeek; }
    public void setCompletedThisWeek(List<CompletedPuzzleDto> completedThisWeek) { this.completedThisWeek = completedThisWeek; }

    public boolean isFull() { return isFull; }
    public void setFull(boolean isFull) { this.isFull = isFull; }

    public boolean isEmpty() { return isEmpty; }
    public void setEmpty(boolean isEmpty) { this.isEmpty = isEmpty; }
}
