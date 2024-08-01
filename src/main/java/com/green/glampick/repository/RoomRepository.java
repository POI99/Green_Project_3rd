package com.green.glampick.repository;

import com.green.glampick.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    RoomEntity findByRoomId(long roomId);


}