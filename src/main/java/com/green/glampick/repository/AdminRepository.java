package com.green.glampick.repository;

import com.green.glampick.entity.AdminEntity;
import org.springframework.data.repository.Repository;

public interface AdminRepository extends Repository<AdminEntity, Long> {

    AdminEntity findByAdminId(String adminId);

}
