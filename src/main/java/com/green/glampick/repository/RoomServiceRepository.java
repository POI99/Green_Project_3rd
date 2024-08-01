package com.green.glampick.repository;

import com.green.glampick.entity.RoomEntity;
import com.green.glampick.entity.RoomServiceEntity;
import com.green.glampick.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomServiceRepository extends JpaRepository<RoomServiceEntity, Long> {

    void deleteAllByRoom(RoomEntity room);
}
