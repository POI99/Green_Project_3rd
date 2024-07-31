package com.green.glampick.repository;

import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.GlampingWaitEntity;
import com.green.glampick.entity.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface GlampingRepository extends JpaRepository<GlampingEntity, Long> {

    GlampingEntity findByGlampId(long glampId);

    GlampingEntity findByOwner(OwnerEntity owner);
    GlampingEntity findByGlampLocation(String glampLocation);

    @Modifying
    @Transactional
    @Query("update GlampingEntity g set g.glampName = :name, g.glampCall = :call" +
            ", g.glampLocation = :location, g.region = :region" +
            ", g.extraCharge = :extra, g.glampIntro = :intro" +
            ", g.infoBasic = :basic, g.infoNotice = :notice" +
            ", g.traffic = :traffic where g.glampId = :glampId")
    void updateGlampingInformation(String name, String call, String location, String region, int extra,
            String intro, String basic, String notice, String traffic, Long glampId);


}
