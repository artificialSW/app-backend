package org.dcode.artificialswbackend.community.repository;

import org.dcode.artificialswbackend.community.entity.Flowers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowersRepository extends JpaRepository<Flowers, Long> {
}