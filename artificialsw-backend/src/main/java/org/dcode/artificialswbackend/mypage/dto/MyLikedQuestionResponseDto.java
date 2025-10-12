package org.dcode.artificialswbackend.mypage.dto;

public class MyLikedQuestionResponseDto {
    private Long questionRefId;
    private String contents;
    private String questionType; // "Personal" 또는 "Public"
    
    public MyLikedQuestionResponseDto() {}
    
    public MyLikedQuestionResponseDto(Long questionRefId, String contents, String questionType) {
        this.questionRefId = questionRefId;
        this.contents = contents;
        this.questionType = questionType;
    }
    
    public Long getQuestionRefId() {
        return questionRefId;
    }
    
    public void setQuestionRefId(Long questionRefId) {
        this.questionRefId = questionRefId;
    }
    
    public String getContents() {
        return contents;
    }
    
    public void setContents(String contents) {
        this.contents = contents;
    }
    
    public String getQuestionType() {
        return questionType;
    }
    
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
}