package org.dcode.artificialswbackend.signup.repository;

import org.dcode.artificialswbackend.signup.entity.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SignUpRepository extends JpaRepository<SignUp, Long> {
    Optional<SignUp> findByPhone(String phone);

    Optional<SignUp> findByIdAndFamilyId(Long id, Long familyId);

    @Query("SELECT s.familyType FROM SignUp s WHERE s.id = :userId")
    String findFamilyTypeById(@Param("userId") Long userId);
}
