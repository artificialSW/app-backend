package org.dcode.artificialswbackend.community.repository;

import org.dcode.artificialswbackend.community.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    // 특정 사용자가 특정 대상에 좋아요를 눌렀는지 확인
    Optional<Like> findByUserIdAndTargetTypeAndTargetId(Long userId, Like.TargetType targetType, Long targetId);
    
    // 특정 사용자가 특정 대상에 좋아요를 눌렀는지 확인 (boolean)
    boolean existsByUserIdAndTargetTypeAndTargetId(Long userId, Like.TargetType targetType, Long targetId);
    
    // 특정 대상의 총 좋아요 수 조회
    long countByTargetTypeAndTargetId(Like.TargetType targetType, Long targetId);
    
    // 특정 사용자가 좋아요한 대상 목록 조회
    @Query("SELECT l.targetId FROM Like l WHERE l.userId = :userId AND l.targetType = :targetType AND l.familyId = :familyId")
    List<Long> findTargetIdsByUserIdAndTargetTypeAndFamilyId(@Param("userId") Long userId, 
                                                            @Param("targetType") Like.TargetType targetType, 
                                                            @Param("familyId") Long familyId);
    
    // 특정 가족의 총 좋아요 수
    long countByFamilyId(Long familyId);
    
    // 특정 대상들의 좋아요 수를 배치로 조회
    @Query("SELECT l.targetId, COUNT(l) FROM Like l WHERE l.targetType = :targetType AND l.targetId IN :targetIds GROUP BY l.targetId")
    List<Object[]> countLikesByTargetIds(@Param("targetType") Like.TargetType targetType, 
                                        @Param("targetIds") List<Long> targetIds);
    
    // 사용자의 배치 좋아요 상태 조회
    @Query("SELECT l.targetId FROM Like l WHERE l.userId = :userId AND l.targetType = :targetType AND l.targetId IN :targetIds")
    List<Long> findUserLikedTargets(@Param("userId") Long userId, 
                                   @Param("targetType") Like.TargetType targetType, 
                                   @Param("targetIds") List<Long> targetIds);
}