package com.green.glampick.repository;

import com.green.glampick.common.Role;
import com.green.glampick.entity.AdminEntity;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.repository.resultset.GetAccessGlampingListResultSet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface AdminRepository extends Repository<AdminEntity, Long> {

    AdminEntity findByAdminId(String adminId);

    @Modifying
    @Query( "update OwnerEntity oe set oe.role = :role where oe.ownerId = :ownerId" )
    void updateOwnerRole (Role role, Long ownerId);

    @Query( value = "select oe.owner_name AS ownerName, oe.business_number AS businessNumber" +
            ", oe.owner_id AS ownerId, ge.glamp_id AS glampId, ge.glamp_name AS glampName " +
            "from owner oe inner join glamping_wait ge on oe.owner_id = ge.owner_id", nativeQuery = true )
    List<GetAccessGlampingListResultSet> getAccessGlampingList();

    @Query( value = "select oe.ownerId, oe.ownerName from OwnerEntity oe WHERE oe.activateStatus = 0" )
    List<OwnerEntity> getAccessOwnerSignUpList();

}
