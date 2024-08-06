package com.green.glampick.repository;

import org.springframework.data.jpa.repository.Modifying;
import com.green.glampick.entity.GlampingWaitEntity;
import com.green.glampick.entity.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface GlampingWaitRepository extends JpaRepository<GlampingWaitEntity, Long> {

    @Modifying
    @Transactional
    @Query("update GlampingWaitEntity g set g.glampImage = :glampImg where g.glampId = :glampId")
    void updateGlampImageByGlampId(String glampImg, Long glampId);

    GlampingWaitEntity findByOwner(OwnerEntity owner);
    GlampingWaitEntity findByGlampId(Long glampId);
    GlampingWaitEntity findByGlampLocation(String glampLocation);

}
