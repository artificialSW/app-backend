package org.dcode.artificialswbackend.mypage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyLikedQuestionResponseDto {
    @JsonProperty("question_ref_id")
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