package org.dcode.artificialswbackend.notification.repository;

import org.dcode.artificialswbackend.notification.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    
    Optional<FcmToken> findByUserIdAndToken(Long userId, String token);
    
    List<FcmToken> findByUserIdAndIsActiveTrue(Long userId);
    
    List<FcmToken> findByFamilyIdAndIsActiveTrue(Long familyId);
    
    void deleteByUserIdAndToken(Long userId, String token);
}