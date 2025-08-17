package org.dcode.artificialswbackend.community;

import org.dcode.artificialswbackend.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long>{ }
