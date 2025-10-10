package org.dcode.artificialswbackend.puzzle.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "puzzle_pieces",
        uniqueConstraints = @UniqueConstraint(name = "uq_puzzle_piece", columnNames = {"puzzle_id", "piece_id"}))
public class PuzzlePieces {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "puzzle_id", nullable = false)
    private Integer puzzleId;


    @Column(name = "position", columnDefinition = "json", nullable = false)
    private String position; // "pieces": {...} 형태의 JSON 문자열

    public PuzzlePieces() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getPuzzleId() { return puzzleId; }
    public void setPuzzleId(Integer puzzleId) { this.puzzleId = puzzleId; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
}
