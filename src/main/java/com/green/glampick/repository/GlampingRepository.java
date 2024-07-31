package com.green.glampick.repository;

import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.GlampingWaitEntity;
import com.green.glampick.entity.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GlampingRepository extends JpaRepository<GlampingEntity, Long> {

    GlampingEntity findByGlampId(long glampId);

    GlampingEntity findByOwner(OwnerEntity owner);
    GlampingEntity findByGlampLocation(String glampLocation);

}
