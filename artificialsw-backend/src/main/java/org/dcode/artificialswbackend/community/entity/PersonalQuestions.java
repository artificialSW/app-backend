package org.dcode.artificialswbackend.community.entity;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "personal_questions")
public class PersonalQuestions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //questions 테이블
    private Long id;

    private String content;
    private Long sender;
    private Long receiver;
    private Boolean isPublic;
    private Boolean solved;
    private Integer likes;
    private Timestamp created_at;
    private Timestamp updated_at;

    public PersonalQuestions() {}

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

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean aPublic) {
        isPublic = aPublic;
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

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
