package com.green.glampick.repository;

import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {
}
