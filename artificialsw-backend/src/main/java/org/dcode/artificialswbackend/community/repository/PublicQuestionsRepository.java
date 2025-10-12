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
    
    @Modifying
    @Query("UPDATE PublicQuestions q SET q.likes = GREATEST(q.likes - 1, 0) WHERE q.id = :id")
    void decreaseLikes(@Param("id") Long id);
    
    // 가족 구성원 검증용
    boolean existsByIdAndFamilyId(Long id, Long familyId);
    
    @Query("SELECT pq FROM PublicQuestions pq ORDER BY pq.counts DESC")
    java.util.List<PublicQuestions> findAllOrderByCountsDesc();
    
    java.util.Optional<PublicQuestions> findTopByOrderByCountsDesc();
    
    // family_id 기반 메소드들
    java.util.List<PublicQuestions> findByFamilyId(Long familyId);
    
    @Query("SELECT pq FROM PublicQuestions pq WHERE pq.familyId = :familyId ORDER BY pq.counts DESC")
    java.util.List<PublicQuestions> findByFamilyIdOrderByCountsDesc(@Param("familyId") Long familyId);
    
    java.util.Optional<PublicQuestions> findTopByFamilyIdOrderByCountsDesc(Long familyId);
    
    // 최신순 정렬 메서드들 (created_at 필드 사용)
    @Query("SELECT pq FROM PublicQuestions pq WHERE pq.familyId = :familyId ORDER BY pq.created_at DESC")
    java.util.List<PublicQuestions> findByFamilyIdOrderByCreatedAtDesc(@Param("familyId") Long familyId);
    
    @Query("SELECT pq FROM PublicQuestions pq WHERE pq.familyId = :familyId ORDER BY pq.created_at DESC LIMIT 1")
    java.util.Optional<PublicQuestions> findTopByFamilyIdOrderByCreatedAtDesc(@Param("familyId") Long familyId);
}
