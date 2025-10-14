package org.dcode.artificialswbackend.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWithCommentsResponseDto {
    
    @JsonProperty("question")
    private QuestionInfo question;
    
    @JsonProperty("comments")
    private List<CommentInfo> comments;
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionInfo {
        @JsonProperty("question_ref_id")
        private Long questionRefId;
        
        @JsonProperty("content")
        private String content;
        
        @JsonProperty("sender")
        private Long sender;
        
        @JsonProperty("sender_role")
        private String senderRole;
        
        @JsonProperty("likes")
        private Integer likes;
        
        @JsonProperty("isLiked")
        private Boolean isLiked;
        
        @JsonProperty("CreateAt")
        private String createAt;
    }
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentInfo {
        @JsonProperty("commentId")
        private Long commentId;
        
        @JsonProperty("writer")
        private Long writer;
        
        @JsonProperty("writer_role")
        private String writerRole;
        
        @JsonProperty("content")
        private String content;
        
        @JsonProperty("likes")
        private Integer likes;
        
        @JsonProperty("isLiked")
        private Boolean isLiked;
        
        @JsonProperty("reply")
        private List<CommentInfo> reply; // 대댓글 전체 정보 목록
    }
}