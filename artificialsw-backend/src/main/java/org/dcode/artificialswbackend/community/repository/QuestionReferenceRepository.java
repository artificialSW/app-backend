package org.dcode.artificialswbackend.community.repository;

import org.dcode.artificialswbackend.community.entity.QuestionReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionReferenceRepository extends JpaRepository<QuestionReference, Long> {
    
    List<QuestionReference> findByFamilyId(Long familyId);
    
    @Query("SELECT qr FROM QuestionReference qr WHERE qr.questionId = :questionId AND qr.questionType = :questionType")
    Optional<QuestionReference> findByQuestionIdAndQuestionType(@Param("questionId") Long questionId, @Param("questionType") QuestionReference.QuestionType questionType);
    
    List<QuestionReference> findByFamilyIdAndQuestionType(Long familyId, QuestionReference.QuestionType questionType);
}