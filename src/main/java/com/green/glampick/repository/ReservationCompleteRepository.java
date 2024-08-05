package com.green.glampick.repository;

import com.green.glampick.dto.response.owner.get.GetOwnerBookCompleteCountResponseDto;
import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.ReservationCompleteEntity;
import com.green.glampick.repository.resultset.GetReservationCompleteResultSet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationCompleteRepository extends JpaRepository<ReservationCompleteEntity, Long> {

    @Query(
            value =
                    "SELECT C.glamp_name AS glampName" +
                            ", C.glamp_id AS glampId " +
                            ", C.glamp_image AS glampImage " +
                            ", A.book_id AS bookId " +
                            ", B.room_name AS roomName " +
                            ", A.reservation_id AS reservationId" +
                            ", A.check_in_date AS checkInDate " +
                            ", A.check_out_date AS checkOutDate " +
                            ", A.status AS status " +
                            ", A.created_at AS createdAt " +
                            ", B.check_in_time AS checkInTime " +
                            ", B.check_out_time AS checkOutTime " +
                            "FROM reservation_complete A " +
                            "JOIN room B " +
                            "ON A.room_id = B.room_id " +
                            "JOIN glamping C " +
                            "ON B.glamp_id = C.glamp_id " +
                            "WHERE A.user_id = :userId " +
                            "ORDER BY A.check_in_date DESC ",
            nativeQuery = true
    )
    List<GetReservationCompleteResultSet> getBook(Long userId);

    @Query(
            value =
                    "SELECT " +
                            "B.glamp_name AS glampName, " +
                            "B.glamp_id AS glampId, " +
                            "A.book_id AS bookId, " +
                            "C.room_name AS roomName, " +
                            "A.reservation_id AS reservationId, " +
                            "A.check_in_date AS checkInDate, " +
                            "A.check_out_date AS checkOutDate, " +
                            "A.created_at AS createdAt, " +
                            "C.check_in_time AS checkInTime, " +
                            "C.check_out_time AS checkOutTime, " +
                            "A.status AS status, " +
                            "C.room_id AS roomId " +
                            "FROM reservation_complete A " +
                            "INNER JOIN	glamping B ON A.glamp_id = B.glamp_id " +
                            "INNER JOIN room C ON A.room_id = C.room_id " +
                            "WHERE B.owner_id = ?1 " +
                            "ORDER BY A.check_in_date " +
                            "LIMIT ?2 OFFSET ?3 ",
            nativeQuery = true
    )
    List<GetReservationCompleteResultSet> getReservationCompleteByOwnerId(Long userId, int limit, int offset);

    @Transactional
    ReservationCompleteEntity findByReservationId(Long reservationId);

    @Query("SELECT COUNT(rc) FROM ReservationCompleteEntity rc WHERE rc.checkInDate = :dateTime")
    List<GetOwnerBookCompleteCountResponseDto> getCountFromReservationComplete(LocalDate dateTime);

}
