package org.dcode.artificialswbackend.community.repository;

import org.dcode.artificialswbackend.community.entity.PersonalQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalQuestionsRepository extends JpaRepository<PersonalQuestions, Long> {
    Long countByReceiverAndSolvedFalse(Long receiverId);
}
