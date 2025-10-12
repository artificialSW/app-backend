package org.dcode.artificialswbackend.puzzle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class SavePuzzleProgressRequest {
    private Integer puzzleId;
    private String captureImagePath;
    private Map<String, Coordinate> pieces;
    private List<Integer> completedPiecesId;
    private boolean completed;
    private boolean isPlayingPuzzle;

    public Integer getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(Integer puzzleId) {
        this.puzzleId = puzzleId;
    }

    public String getCaptureImagePath() {
        return captureImagePath;
    }

    public void setCaptureImagePath(String captureImagePath) {
        this.captureImagePath = captureImagePath;
    }

    public Map<String, Coordinate> getPieces() {
        return pieces;
    }

    public void setPieces(Map<String, Coordinate> pieces) {
        this.pieces = pieces;
    }

    public List<Integer> getCompletedPiecesId() {
        return completedPiecesId;
    }

    public void setCompletedPiecesId(List<Integer> completedPiecesId) {
        this.completedPiecesId = completedPiecesId;
    }

    public boolean isPlayingPuzzle() {
        return isPlayingPuzzle;
    }

    public void setPlayingPuzzle(boolean playingPuzzle) {
        isPlayingPuzzle = playingPuzzle;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public static class Coordinate {
        private double x;
        private double y;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}
