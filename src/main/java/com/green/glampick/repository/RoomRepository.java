package com.green.glampick.repository;

import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.RoomEntity;
import com.green.glampick.repository.resultset.GetRoomInfoResultSet;
import com.green.glampick.repository.resultset.GetRoomListResultSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    RoomEntity findByRoomId(long roomId);

    @Query(" select r.roomId AS roomId, r.roomName AS roomName, ri.roomImageName AS roomImageName from RoomEntity r" +
            " join RoomImageEntity ri on r = ri.roomId where r.glamp = :glamp group by ri.roomId ")
    List<GetRoomListResultSet> getRoomList(GlampingEntity glamp);

    @Query (" select r.roomName AS roomName, r.roomPrice AS price" +
            ", r.roomNumPeople AS peopleNum, r.roomMaxPeople AS peopleMax" +
            ", r.checkInTime AS inTime, r.checkOutTime AS outTime" +
            " from RoomEntity r where r.roomId = :roomId")
    GetRoomInfoResultSet getRoomInfo(Long roomId);
}