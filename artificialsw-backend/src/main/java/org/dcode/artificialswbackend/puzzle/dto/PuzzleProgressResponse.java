package org.dcode.artificialswbackend.puzzle.dto;

import java.util.Map;

public class PuzzleProgressResponse {
    private String imageUrl;
    private int size;
    private Map<String, PiecePosition> pieces;

    public PuzzleProgressResponse(String imageUrl, int size, Map<String, PiecePosition> pieces) {
        this.imageUrl = imageUrl;
        this.size = size;
        this.pieces = pieces;
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public Map<String, PiecePosition> getPieces() { return pieces; }
    public void setPieces(Map<String, PiecePosition> pieces) { this.pieces = pieces; }

    public static class PiecePosition {
        private double x;
        private double y;

        public PiecePosition(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() { return x; }
        public void setX(double x) { this.x = x; }

        public double getY() { return y; }
        public void setY(double y) { this.y = y; }
    }


}
