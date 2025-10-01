package org.dcode.artificialswbackend.community.repository;
import org.dcode.artificialswbackend.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Modifying
    @Query("UPDATE Comment c SET c.likes = c.likes + 1 WHERE c.id = :id")
    void increaseLikes(@Param("id") Long id);
    
    List<Comment> findByQuestionRefId(Long questionRefId);
    
    // family_id 기반 메소드들
    List<Comment> findByFamilyId(Long familyId);
    
    List<Comment> findByFamilyIdAndQuestionRefId(Long familyId, Long questionRefId);
    
    List<Comment> findByFamilyIdAndWriter(Long familyId, Long writer);
}
