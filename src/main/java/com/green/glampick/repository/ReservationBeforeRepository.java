package com.green.glampick.repository;

import com.green.glampick.dto.response.owner.get.GetOwnerBookBeforeCountResponseDto;
import com.green.glampick.entity.ReservationBeforeEntity;
import com.green.glampick.repository.resultset.GetReservationBeforeResultSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Repository
public interface ReservationBeforeRepository extends JpaRepository<ReservationBeforeEntity, Long> {

    @Query(
            value =
            "SELECT C. glamp_name AS glampName " +
            ", C.glamp_id AS glampId " +
            ", C. glamp_image AS glampImage " +
            ", A. book_id AS bookId " +
            ", B. room_name AS roomName " +
            ", A. reservation_id AS reservationId" +
            ", A. check_in_date AS checkInDate " +
            ", A. check_out_date AS checkOutDate " +
            ", A. created_at AS createdAt " +
            ", B. check_in_time AS checkInTime " +
            ", B. check_out_time AS checkOutTime " +
            "FROM reservation_before A " +
            "JOIN room B " +
            "ON A.room_id = B.room_id " +
            "JOIN glamping C " +
            "ON B.glamp_id = C.glamp_id " +
            "WHERE A.user_id = :userId " +
            "ORDER BY A.check_in_date ",
            nativeQuery = true
    )
    List<GetReservationBeforeResultSet> getBook(Long userId);
    @Query(
     value =
             "SELECT " +
             "B.glamp_name AS glampName, " +
             "B.glamp_id AS glampId, " +
             "A.book_id AS bookId, " +
             "C.room_name AS roomName, " +
             "A.reservation_id AS reservationId, " +
             "A.check_in_date AS checkInDate, " +
             "A.check_out_date AS checkOutDate, "+
             "A.created_at AS createdAt, " +
             "C.check_in_time AS checkInTime, " +
             "C.check_out_time AS checkOutTime, " +
             "C.room_id AS roomId " +
             "FROM reservation_before A " +
             "INNER JOIN	glamping B ON A.glamp_id = B.glamp_id " +
             "INNER JOIN room C ON A.room_id = C.room_id " +
             "WHERE B.owner_id = ?1 " +
             "ORDER BY A.check_in_date " +
             "LIMIT ?2 OFFSET ?3 ",
            nativeQuery = true
    )
    List<GetReservationBeforeResultSet> getReservationBeforeByOwnerId(Long userId, int limit, int offset);

    boolean existsByReservationId(Long reservationId);

    @Query("SELECT r FROM ReservationBeforeEntity r JOIN r.glamping g JOIN r.room rm JOIN r.user u WHERE r.checkOutDate < :dateTime")
    List<ReservationBeforeEntity> findAllByCheckOutDateBefore(LocalDate dateTime);

    @Query("SELECT rb.checkInDate AS checkInDate, COUNT(rb.checkInDate) AS countBefore " +
            "FROM  ReservationBeforeEntity rb " +
            "WHERE FUNCTION('MONTH', rb.checkInDate) = :month " +
            "GROUP BY rb.checkInDate")
    List<GetOwnerBookBeforeCountResponseDto> getCountFromReservationBefore(@Param("month")int month);
}