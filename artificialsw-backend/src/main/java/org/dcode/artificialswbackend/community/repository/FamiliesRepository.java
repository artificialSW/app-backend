package org.dcode.artificialswbackend.community.repository;

import org.dcode.artificialswbackend.community.entity.Families;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamiliesRepository extends JpaRepository<Families, Long> {
    Optional<Families> findByName(String name);
}