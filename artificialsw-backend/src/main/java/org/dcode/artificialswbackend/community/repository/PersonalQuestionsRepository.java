package org.dcode.artificialswbackend.community.repository;

import org.dcode.artificialswbackend.community.entity.PersonalQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PersonalQuestionsRepository extends JpaRepository<PersonalQuestions, Long> {
    
    // 기존 메소드들
    Long countByReceiverAndSolvedFalse(Long receiverId);
    List<PersonalQuestions> findByReceiver(Long receiver);
    List<PersonalQuestions> findByReceiverAndSolvedFalse(Long receiver);

    @Modifying
    @Query("UPDATE PersonalQuestions p SET p.likes = p.likes + 1 WHERE p.id = :id")
    void increaseLikes(@Param("id") Long id);
    
    @Modifying
    @Query("UPDATE PersonalQuestions p SET p.likes = GREATEST(p.likes - 1, 0) WHERE p.id = :id")
    void decreaseLikes(@Param("id") Long id);
    
    // 가족 구성원 검증용
    boolean existsByIdAndFamilyId(Long id, Long familyId);
    
    // 새로운 family_id 기반 메소드들
    List<PersonalQuestions> findByFamilyId(Long familyId);
    
    List<PersonalQuestions> findByFamilyIdAndReceiver(Long familyId, Long receiver);
    
    List<PersonalQuestions> findByFamilyIdAndSender(Long familyId, Long sender);
    
    Long countByFamilyIdAndReceiverAndSolvedFalse(Long familyId, Long receiverId);
    
    List<PersonalQuestions> findByFamilyIdAndVisibilityTrue(Long familyId);
    
    // solved가 false인 나의 질문들 조회
    List<PersonalQuestions> findByFamilyIdAndReceiverAndSolvedFalse(Long familyId, Long receiver);
}