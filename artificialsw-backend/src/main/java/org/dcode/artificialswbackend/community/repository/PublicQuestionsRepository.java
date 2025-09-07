package org.dcode.artificialswbackend.community.repository;

import org.dcode.artificialswbackend.community.entity.PublicQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PublicQuestionsRepository extends JpaRepository<PublicQuestions, Long> {
    @Modifying
    @Query("UPDATE PublicQuestions q SET q.likes = q.likes + 1 WHERE q.id = :id")
    void increaseLikes(@Param("id") Long id);
}
