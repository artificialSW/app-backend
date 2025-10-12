package org.dcode.artificialswbackend.community.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "question_reference")
public class QuestionReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @Column(name = "family_id", nullable = false)
    private Long familyId;

    public enum QuestionType {
        Personal, 
        Public;
        
        @Override
        public String toString() {
            return this.name();
        }
    }

    public QuestionReference() {}

    public QuestionReference(Long questionId, QuestionType questionType, Long familyId) {
        this.questionId = questionId;
        this.questionType = questionType;
        this.familyId = familyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }
}