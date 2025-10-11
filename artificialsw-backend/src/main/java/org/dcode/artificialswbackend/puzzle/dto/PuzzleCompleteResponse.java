package org.dcode.artificialswbackend.puzzle.dto;

import java.util.List;

public class PuzzleCompleteResponse {

    private Long puzzleId;
    private String message;      // 퍼즐 테이블의 message 컬럼 값
    private String fruitName;    // 랜덤 과일 이름
    private String fruitMessage; // switch-case로 생성된 메시지
    private List<String> contributors; // 닉네임 리스트

    public PuzzleCompleteResponse(Long puzzleId, String message, String fruitName, String fruitMessage, List<String> contributors) {
        this.puzzleId = puzzleId;
        this.message = message;
        this.fruitName = fruitName;
        this.fruitMessage = fruitMessage;
        this.contributors = contributors;
    }

    public Long getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(Long puzzleId) {
        this.puzzleId = puzzleId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }

    public String getFruitMessage() {
        return fruitMessage;
    }

    public void setFruitMessage(String fruitMessage) {
        this.fruitMessage = fruitMessage;
    }

    public List<String> getContributors() {
        return contributors;
    }

    public void setContributors(List<String> contributors) {
        this.contributors = contributors;
    }
}
