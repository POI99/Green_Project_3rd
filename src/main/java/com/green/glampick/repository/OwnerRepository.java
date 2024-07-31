package com.green.glampick.repository;

import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.entity.ReviewEntity;
import com.green.glampick.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {
    OwnerEntity findByOwnerEmail(String ownerEmail);
}
