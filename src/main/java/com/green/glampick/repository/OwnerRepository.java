package com.green.glampick.repository;

import com.green.glampick.common.Role;
import com.green.glampick.dto.response.owner.get.OwnerInfoResponseDto;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.entity.ReviewEntity;
import com.green.glampick.entity.UserEntity;
import com.green.glampick.repository.resultset.GetDeleteOwnerListResultSet;
import com.green.glampick.repository.resultset.OwnerInfoResultSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {

    OwnerEntity findByOwnerEmail(String ownerEmail);
    OwnerEntity findByOwnerId(Long ownerId);

    @Modifying
    @Transactional
    @Query( "update OwnerEntity oe set oe.role = :role where oe.ownerId = :ownerId" )
    void updateOwnerRole (Role role, Long ownerId);

    @Transactional
    @Query( "select o.ownerEmail AS ownerEmail, o.ownerName AS ownerName" +
            ", o.businessNumber AS businessNumber, o.ownerPhone AS ownerPhone" +
            " from OwnerEntity o where o.ownerId = :ownerId" )
    OwnerInfoResultSet getOwnerInfo(Long ownerId);

    @Query( "select o.ownerId AS ownerId, o.ownerName AS ownerName, o.businessNumber AS businessNumber" +
            ", o.ownerPhone AS ownerPhone from OwnerEntity o where o.activateStatus = 0 " )
    List<GetDeleteOwnerListResultSet> getDeleteOwnerList();

    boolean existsByOwnerPhone(String ownerPhone);
    boolean existsByOwnerEmail(String ownerEmail);


}
