package org.dcode.artificialswbackend.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PublicQuestionWithCommentsResponseDto {
    
    @JsonProperty("question")
    private QuestionInfo question;
    
    @JsonProperty("comments")
    private List<CommentInfo> comments;
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionInfo {
        @JsonProperty("question_ref_id")
        private String questionRefId;
        
        @JsonProperty("content")
        private String content;
        
        @JsonProperty("likes")
        private Integer likes;
        
        @JsonProperty("CreateAt")
        private String createAt;
        
        @JsonProperty("count")
        private Integer count;
    }
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentInfo {
        @JsonProperty("commentId")
        private String commentId;
        
        @JsonProperty("writer")
        private String writer;
        
        @JsonProperty("content")
        private String content;
        
        @JsonProperty("likes")
        private Integer likes;
        
        @JsonProperty("reply")
        private List<CommentInfo> reply; // 대댓글 전체 정보 목록
    }
}