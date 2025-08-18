package org.dcode.artificialswbackend.community.repository;

import org.dcode.artificialswbackend.community.entity.PublicQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PublicQuestionsRepository extends JpaRepository<PublicQuestions, Long> {
}
