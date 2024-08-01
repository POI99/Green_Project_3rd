package com.green.glampick.repository;

import com.green.glampick.entity.RoomEntity;
import com.green.glampick.entity.RoomImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomImageRepository extends JpaRepository<RoomImageEntity, Long> {
    List<RoomImageEntity> findByRoomId(RoomEntity room);
}
