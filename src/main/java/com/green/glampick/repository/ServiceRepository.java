package com.green.glampick.repository;

import com.green.glampick.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
}
