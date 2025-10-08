package org.dcode.artificialswbackend.puzzle.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "puzzle_category")
public class PuzzleCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    public PuzzleCategory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
