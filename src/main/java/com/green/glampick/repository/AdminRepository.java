package com.green.glampick.repository;

import com.green.glampick.common.Role;
import com.green.glampick.entity.AdminEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface AdminRepository extends Repository<AdminEntity, Long> {

    AdminEntity findByAdminId(String adminId);

    @Modifying
    @Query( "update OwnerEntity oe set oe.role = :role where oe.ownerId = :ownerId" )
    void updateOwnerRole (Role role, Long ownerId);

}
