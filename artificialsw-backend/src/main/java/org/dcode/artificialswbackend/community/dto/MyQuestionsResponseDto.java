package org.dcode.artificialswbackend.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyQuestionsResponseDto {
    
    @JsonProperty("questions")
    private List<MyQuestionDto> questions;
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyQuestionDto {
        @JsonProperty("question_ref_id")
        private String questionRefId;
        
        @JsonProperty("content")
        private String content;
        
        @JsonProperty("sender")
        private String sender;
        
        @JsonProperty("visibility")
        private Boolean visibility;
    }
}