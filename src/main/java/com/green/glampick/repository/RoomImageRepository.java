package com.green.glampick.repository;

import com.green.glampick.entity.RoomEntity;
import com.green.glampick.entity.RoomImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomImageRepository extends JpaRepository<RoomImageEntity, Long> {
    List<RoomImageEntity> findByRoomId(RoomEntity room);

    @Query ("select ri.roomImageName from RoomImageEntity ri where ri.roomId = :room")
    List<String> getRoomImg(RoomEntity room);

}
