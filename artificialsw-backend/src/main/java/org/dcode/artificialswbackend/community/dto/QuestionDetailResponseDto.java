package org.dcode.artificialswbackend.community.dto;

import java.sql.Timestamp;
import java.util.List;

public class QuestionDetailResponseDto {
    private QuestionInfo question;
    private List<CommentInfo> comments;

    public QuestionDetailResponseDto() {}

    public QuestionDetailResponseDto(QuestionInfo question, List<CommentInfo> comments) {
        this.question = question;
        this.comments = comments;
    }

    public QuestionInfo getQuestion() {
        return question;
    }

    public void setQuestion(QuestionInfo question) {
        this.question = question;
    }

    public List<CommentInfo> getComments() {
        return comments;
    }

    public void setComments(List<CommentInfo> comments) {
        this.comments = comments;
    }

    // 질문 정보 클래스
    public static class QuestionInfo {
        private Long Q_id;
        private String content;
        private String sender;
        private Integer likes;
        private String CreateAt;

        public QuestionInfo() {}

        public QuestionInfo(Long Q_id, String content, String sender, Integer likes, String CreateAt) {
            this.Q_id = Q_id;
            this.content = content;
            this.sender = sender;
            this.likes = likes;
            this.CreateAt = CreateAt;
        }

        // Getters and Setters
        public Long getQ_id() {
            return Q_id;
        }

        public void setQ_id(Long Q_id) {
            this.Q_id = Q_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public Integer getLikes() {
            return likes;
        }

        public void setLikes(Integer likes) {
            this.likes = likes;
        }

        public String getCreateAt() {
            return CreateAt;
        }

        public void setCreateAt(String CreateAt) {
            this.CreateAt = CreateAt;
        }
    }

    // 댓글 정보 클래스
    public static class CommentInfo {
        private Long commentId;
        private String writer;
        private String content;
        private Integer likes;
        private List<String> reply; // 대댓글의 comment ID 목록

        public CommentInfo() {}

        public CommentInfo(Long commentId, String writer, String content, Integer likes, List<String> reply) {
            this.commentId = commentId;
            this.writer = writer;
            this.content = content;
            this.likes = likes;
            this.reply = reply;
        }

        // Getters and Setters
        public Long getCommentId() {
            return commentId;
        }

        public void setCommentId(Long commentId) {
            this.commentId = commentId;
        }

        public String getWriter() {
            return writer;
        }

        public void setWriter(String writer) {
            this.writer = writer;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Integer getLikes() {
            return likes;
        }

        public void setLikes(Integer likes) {
            this.likes = likes;
        }

        public List<String> getReply() {
            return reply;
        }

        public void setReply(List<String> reply) {
            this.reply = reply;
        }
    }
}