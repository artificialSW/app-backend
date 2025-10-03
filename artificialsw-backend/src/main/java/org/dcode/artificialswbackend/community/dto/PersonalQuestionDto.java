package org.dcode.artificialswbackend.community.dto;

import org.dcode.artificialswbackend.community.entity.PersonalQuestions;

import java.sql.Timestamp;

public class PersonalQuestionDto {

    private Long id;
    private Long familyId;
    private String content;
    private Long sender;
    private Long receiver;
    private Boolean visibility; // isPublic에서 visibility로 변경
    private Boolean solved;
    private Integer likes;
    private Timestamp created;

    public PersonalQuestionDto(Long id, Long familyId, String content, Long sender, Long receiver, Boolean visibility, Boolean solved, Integer likes, Timestamp created) {
        this.id = id;
        this.familyId = familyId;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.visibility = visibility;
        this.solved = solved;
        this.likes = likes;
        this.created = created;
    }

    // 엔티티를 DTO로 변환해주는 정적 메서드
    public static PersonalQuestionDto fromEntity(PersonalQuestions entity) {
        return new PersonalQuestionDto(
                entity.getId(),
                entity.getFamilyId(),
                entity.getContent(),
                entity.getSender(),
                entity.getReceiver(),
                entity.getVisibility(),
                entity.getSolved(),
                entity.getLikes(),
                entity.getCreated_at()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public Boolean getSolved() {
        return solved;
    }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
