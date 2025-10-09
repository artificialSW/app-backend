package org.dcode.artificialswbackend.puzzle.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "puzzle_piece")
public class PuzzlePiece {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer piece_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puzzle_id", nullable = false)
    private Puzzle puzzle;

    private double x;
    private double y;

    public PuzzlePiece() {
    }

    public Integer getPiece_id() {
        return piece_id;
    }

    public void setPiece_id(Integer piece_id) {
        this.piece_id = piece_id;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

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
