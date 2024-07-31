package com.green.glampick.repository;

import com.green.glampick.common.Role;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.entity.ReviewEntity;
import com.green.glampick.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {

    OwnerEntity findByOwnerEmail(String ownerEmail);
    OwnerEntity findByOwnerId(Long ownerId);

}
