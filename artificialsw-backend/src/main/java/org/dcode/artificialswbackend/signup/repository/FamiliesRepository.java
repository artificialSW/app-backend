package org.dcode.artificialswbackend.signup.repository;

import org.dcode.artificialswbackend.signup.entity.Families;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamiliesRepository extends JpaRepository<Families, Long> {

}