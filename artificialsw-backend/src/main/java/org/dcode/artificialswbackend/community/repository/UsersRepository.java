package org.dcode.artificialswbackend.community.repository;

import org.dcode.artificialswbackend.community.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    
    List<Users> findByFamilyId(Long familyId);
    
    Optional<Users> findByPhone(String phone);
    
    Optional<Users> findByNickname(String nickname);
    
    List<Users> findByFamilyIdAndFamilyType(Long familyId, Users.FamilyType familyType);
    
    // 가족 구성원 검증용
    boolean existsByIdAndFamilyId(Long id, Long familyId);
}