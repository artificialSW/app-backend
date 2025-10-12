package org.dcode.artificialswbackend.community.repository;

import org.dcode.artificialswbackend.community.entity.QuestionList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionListRepository extends JpaRepository<QuestionList, Long> {
    
    @Query("SELECT ql FROM QuestionList ql ORDER BY ql.id ASC")
    java.util.List<QuestionList> findAllOrderById();
    
    Optional<QuestionList> findTopByOrderByIdAsc();
}