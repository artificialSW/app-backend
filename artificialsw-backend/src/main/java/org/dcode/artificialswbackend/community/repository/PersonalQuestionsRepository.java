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
    Long countByReceiverAndSolvedFalse(Long receiverId);
    List<PersonalQuestions> findByReceiver(Long receiver);
    List<PersonalQuestions> findBySender(Long sender);

    @Modifying
    @Query("UPDATE PersonalQuestions p SET p.likes = p.likes + 1 WHERE p.id = :id")
    void increaseLikes(@Param("id") Long id);
}