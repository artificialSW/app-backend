package org.dcode.artificialswbackend.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PublicQuestionsResponseDto {
    
    @JsonProperty("questions")
    private List<PublicQuestionDto> questions;
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PublicQuestionDto {
        @JsonProperty("question_ref_id")
        private Long questionRefId;
        
        @JsonProperty("content")
        private String content;
        
        @JsonProperty("likes")
        private Integer likes;
        
        @JsonProperty("comments")
        private Integer comments;
        
        @JsonProperty("created_at")
        private String createdAt;
        
        @JsonProperty("isLiked")
        private Boolean isLiked;
    }
}