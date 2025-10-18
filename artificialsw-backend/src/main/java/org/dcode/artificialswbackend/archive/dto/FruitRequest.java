package org.dcode.artificialswbackend.archive.dto;

public class FruitRequest {
    private Long fruitId;

    public FruitRequest() {}

    public FruitRequest(Long fruitId) {
        this.fruitId = fruitId;
    }

    public Long getFruitId() {
        return fruitId;
    }

    public void setFruitId(Long fruitId) {
        this.fruitId = fruitId;
    }
}
