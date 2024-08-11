package com.green.glampick.jin;

import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.jin.object.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OwnerJinRepository extends JpaRepository<OwnerEntity, Long> {


//    @Query(
//            value =
//                    "SELECT A.glamp_id " +
//                            ", COUNT(A.glamp_id) AS heart " +
//                            ", DATE(A.created_at) as createdAt " +
//                            ", B.owner_id " +
//                            "FROM glamp_favorite A " +
//                            "JOIN glamping B " +
//                            "ON A.glamp_id = B.glamp_id " +
//                            "Group BY DATE(A.created_at) " +
//                            "HAVING createdAt BETWEEN DATE_ADD(NOW(), INTERVAL -1 week ) AND NOW() " +
//                            "AND B.owner_id = :ownerId ",
//            nativeQuery = true
//    )
//    List<GetGlampingHeart> findGlampingHeart(long ownerId);

    @Query(
            value =
                    "SELECT A.star_point_avg as starPointAvg" +
                            ", COUNT(A.glamp_id) AS heart " +
                            ", A.owner_id " +
                            "FROM glamping A " +
                            "JOIN glamp_favorite B ON B.glamp_id = A.glamp_id " +
                            "Group BY A.glamp_id " +
                            "HAVING  A.owner_id = :ownerId ",
            nativeQuery = true
    )
    List<GetStarHeart> findByIdStarPoint(long ownerId);


    @Query(
            value =
                    "SELECT SUM(glamp_count) AS totalCount " +
                            "FROM (SELECT COUNT(A.glamp_id) AS glamp_count , A.check_in_date as checkInDate " +
                            "FROM reservation_before A join glamping B ON A.glamp_id = B.glamp_id WHERE B.owner_id = :ownerId " +
                            "AND check_in_date BETWEEN :startDayId AND :endDayId " +
                            "UNION ALL " +
                            "SELECT COUNT(C.glamp_id) AS glamp_count , C.check_in_date as checkInDate " +
                            "FROM reservation_cancel C join glamping B ON C.glamp_id = B.glamp_id WHERE B.owner_id = :ownerId " +
                            "AND check_in_date BETWEEN :startDayId AND :endDayId " +
                            "UNION ALL " +
                            "SELECT COUNT(D.glamp_id) AS glamp_count , D.check_in_date as checkInDate " +
                            "FROM reservation_complete D join glamping B ON D.glamp_id = B.glamp_id WHERE B.owner_id = :ownerId " +
                            "AND check_in_date BETWEEN :startDayId AND :endDayId) AS counts ",
            nativeQuery = true
    )
    long findTotalCount(@Param("ownerId") long ownerId, @Param("startDayId") String startDayId, @Param("endDayId") String endDayId);

    @Query(
            value =
                    "SELECT SUM(glampId) FROM( " +
                            "SELECT COUNT(A.glamp_id) AS glampId, A.check_in_date AS checkInDate " +
                            "FROM reservation_cancel A join glamping B ON A.glamp_id = B.glamp_id WHERE B.owner_id = :ownerId " +
                            "Group BY check_in_date " +
                            "HAVING check_in_date BETWEEN :startDayId AND :endDayId) AS counts ",
            nativeQuery = true
    )
    long findCancelCount(@Param("ownerId") long ownerId, @Param("startDayId") String startDayId, @Param("endDayId") String endDayId);

//    @Query(
//            value =
//
//
//                    "SELECT C.room_name as roomName, C.room_id as roomId, A.check_in_date as :startDayId, IFNULL(SUM(A.pay_amount), 0) AS pay " +
//                            "FROM glamping B RIGHT JOIN " +
//                            "room C ON B.glamp_id = C.glamp_id " +
//                            "LEFT JOIN " +
//                            "reservation_complete A ON A.room_id = C.room_id " +
//                            "AND A.check_in_date = :startDayId " +
//                            "WHERE " +
//                            "B.owner_id = :ownerId " +
//                            "GROUP BY " +
//                            "C.room_name, " +
//                            "C.room_id, " +
//                            "A.check_in_date " +
//                            "ORDER BY " +
//                            "C.room_id ",
//            nativeQuery = true
//    )
//    List<GetRevenue> findRevenue(long ownerId, long startDayId);

    @Query(
            value =

                    "WITH DateRoom AS ( " +
                            "SELECT DATE(ADDDATE(:startDayId, INTERVAL seq DAY)) AS date, D.room_name, E.owner_id " +
                            "FROM (SELECT @rownum := @rownum + 1 AS seq " +
                            "FROM (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) a, " +
                            "(SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) b, " +
                            "(SELECT @rownum := -1) r) seq " +
                            "CROSS JOIN room D " +
                            "JOIN glamping E ON D.glamp_id = E.glamp_id " +
                            "WHERE DATE(ADDDATE(:startDayId, INTERVAL seq DAY)) BETWEEN :startDayId AND :endDayId " +
                            "AND E.owner_id = :ownerId) " +
                            ", SubQuery AS (SELECT A.glamp_id, " +
                            "SUM(A.pay_amount) AS pay, " +
                            "DATE(A. check_in_date) AS  checkInDate, " +
                            "C.room_name AS roomNam " +
                            "FROM reservation_complete A " +
                            "JOIN glamping B ON A.glamp_id = B.glamp_id " +
                            "JOIN room C ON A.room_id = C.room_id " +
                            "WHERE B.owner_id = :ownerId AND A. check_in_date BETWEEN :startDayId AND :endDayId " +
                            "GROUP BY A. check_in_date, C.room_name) " +
                            "SELECT IFNULL(SUM(sub.pay), 0) AS pay, " +
                            "DR.date AS times, " +
                            "DR.room_name AS roomName " +
                            "FROM DateRoom DR " +
                            "LEFT JOIN SubQuery sub ON DR.date = sub. checkInDate AND DR.room_name = sub.roomNam " +
                            "GROUP BY DR.date, DR.room_name " +
                            "ORDER BY DR.date, DR.room_name ",
            nativeQuery = true
    )
    List<GetRevenue> findRevenue(long ownerId, long startDayId, long endDayId);

    @Query(
            value =
                    "WITH RECURSIVE dates AS ( " +
                            "SELECT :startDayId AS check_in_date " +
                            "UNION ALL " +
                            "SELECT DATE_ADD(check_in_date, INTERVAL 1 DAY) " +
                            "FROM dates " +
                            "WHERE check_in_date < :endDayId) " +
                            "SELECT " +
                            "sud.roomName AS nameing, " +
                            "IFNULL(COUNT(sud.check_in_date), 0) AS cancelCount " +
                            "FROM dates " +
                            "LEFT JOIN ( " +
                            "SELECT " +
                            "A.room_id as counts, C.owner_id, B.room_name as roomName, A.check_in_date " +
                            "FROM reservation_cancel A " +
                            "JOIN room B ON A.room_id = B.room_id " +
                            "JOIN glamping C ON B.glamp_id = C.glamp_id " +
                            "WHERE C.owner_id = :ownerId) AS sud " +
                            "ON sud.check_in_date = dates.check_in_date " +
                            "GROUP BY  sud.roomName " +
                            "ORDER BY dates.check_in_date ",
            nativeQuery = true
    )
    List<GetCancelDto> findRoomCount(@Param("ownerId") long ownerId, @Param("startDayId") String startDayId, @Param("endDayId") String endDayId);
}

/*
                    "WITH RECURSIVE date_range AS ( " +
                            "SELECT :startDayId AS check_in_date " +
                            "UNION ALL " +
                            "SELECT DATE_ADD(check_in_date, INTERVAL 1 DAY) " +
                            "FROM date_range " +
                            "WHERE check_in_date < :endDayId) " +
                            "SELECT " +
                            "date_range.check_in_date, " +
                            "IFNULL(COUNT(filtered_reservations.check_in_date), 0) AS reservation_count " +
                            "FROM date_range " +
                            "LEFT JOIN ( " +
                            "SELECT A.room_id as counts " +
                            ", C.owner_id " +
                            ", B.room_name as roomName " +
                            ", A.check_in_date " +
                            "FROM reservation_cancel A " +
                            "JOIN room B ON A.room_id = B.room_id " +
                            "JOIN glamping C ON B.glamp_id = C.glamp_id " +
                            "WHERE C.owner_id = :ownerId ) AS filtered_reservations ON filtered_reservations.check_in_date = date_range.check_in_date " +
                            "GROUP BY date_range.check_in_date " +
                            "ORDER BY date_range.check_in_date ",

>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Query(
            value =
                    "SELECT COUNT(A.room_id) as counts" +
            ", C.owner_id " +
            ", B.room_name as roomName" +
            "FROM reservation_cancel A " +
            "JOIN room B ON A.room_id = B.room_id " +
            "JOIN glamping C ON B.glamp_id = C.glamp_id " +
            "GROUP BY B.room_name " +
            "having C.owner_id = :ownerId ",
            nativeQuery = true
    )
    long findRoomCount(long ownerId);

    -- 1. 임시 날짜 테이블 생성 (예: 2024년 7월 1일부터 7일까지)
WITH RECURSIVE date_range AS (
    SELECT '2024-07-01' AS check_in_date
    UNION ALL
    SELECT DATE_ADD(check_in_date, INTERVAL 1 DAY)
    FROM date_range
    WHERE check_in_date < '2024-07-07'
)

SELECT
    IFNULL(COUNT(A.room_id), 0) AS roomId,
    dr.check_in_date AS checkInDate,
    IFNULL(B.glamp_id, 0) AS glampId
FROM
    date_range dr
    LEFT JOIN reservation_complete A ON dr.check_in_date = A.check_in_date
    LEFT JOIN glamping B ON A.glamp_id = B.glamp_id
    LEFT JOIN room C ON C.room_id = A.room_id
WHERE
    dr.check_in_date = '2024-07-02'
    AND (owner_id = 2 OR owner_id IS NULL) -- owner_id가 NULL인 경우도 고려
GROUP BY
    dr.check_in_date, B.glamp_id;

//    @Query(
//            value =
//                    "SELECT A.glamp_id " +
//                            ", COUNT(A.glamp_id) AS heart " +
//                            ", DATE(A.created_at) as createdAt " +
//                            ", B.owner_id " +
//                            "FROM glamp_favorite A " +
//                            "JOIN glamping B " +
//                            "ON A.glamp_id = B.glamp_id " +
//                            "Group BY DATE(A.created_at) " +
//                            "HAVING createdAt BETWEEN DATE_ADD(NOW(), INTERVAL -1 week ) AND NOW() " +
//                            "AND B.owner_id = :ownerId ",
//            nativeQuery = true
//    )
//    List<GetGlampingHeart> findGlampingHeart(long ownerId);




/*
    @Query(
            value =

                    "WITH DateRoom AS ( " +
                            "SELECT DATE(ADDDATE(:startDayId, INTERVAL seq DAY)) AS date, D.room_name, E.owner_id " +
                            "FROM (SELECT @rownum := @rownum + 1 AS seq " +
                            "FROM (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) a, " +
                            "(SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) b, " +
                            "(SELECT @rownum := -1) r) seq " +
                            "CROSS JOIN room D " +
                            "JOIN glamping E ON D.glamp_id = E.glamp_id " +
                            "WHERE DATE(ADDDATE(:startDayId, INTERVAL seq DAY)) BETWEEN :startDayId AND :endDayId " +
                            "AND E.owner_id = :ownerId) " +
                            ", SubQuery AS (SELECT A.glamp_id, " +
                            "SUM(A.pay_amount) AS pay, " +
                            "DATE(A. check_in_date) AS  checkInDate, " +
                            "C.room_name AS roomNam " +
                            "FROM reservation_complete A " +
                            "JOIN glamping B ON A.glamp_id = B.glamp_id " +
                            "JOIN room C ON A.room_id = C.room_id " +
                            "WHERE B.owner_id = :ownerId AND A. check_in_date BETWEEN :startDayId AND :endDayId " +
                            "GROUP BY A. check_in_date, C.room_name) " +
                            "SELECT IFNULL(SUM(sub.pay), 0) AS total_count, " +
                            "DR.date AS times, " +
                            "DR.room_name AS roomName " +
                            "FROM DateRoom DR " +
                            "LEFT JOIN SubQuery sub ON DR.date = sub. checkInDate AND DR.room_name = sub.roomNam " +
                            "GROUP BY DR.date, DR.room_name " +
                            "ORDER BY DR.date, DR.room_name ",
            nativeQuery = true
    )
    List<GetRevenue> findRevenue(long ownerId, long startDayId, long endDayId);

SELECT
    COALESCE(A.check_in_date, '20240702') AS check_in_date,
    IFNULL(COUNT(A.check_in_date), 0) AS reservation_count
FROM
    glamping B
RIGHT JOIN
    room C ON B.glamp_id = C.glamp_id
LEFT JOIN
    reservation_complete A ON A.room_id = C.room_id
        AND A.check_in_date = '20240703'
WHERE
    B.owner_id = 2
GROUP BY
    COALESCE(A.check_in_date, '20240702') -- 날짜별로 그룹화
ORDER BY
    check_in_date;



WITH RECURSIVE date_range AS (
    SELECT '2024-07-01' AS check_in_date
    UNION ALL
    SELECT DATE_ADD(check_in_date, INTERVAL 1 DAY)
    FROM date_range
    WHERE check_in_date < '2024-07-31'
)
SELECT
    date_range.check_in_date,
    IFNULL(COUNT(A.check_in_date), 0) AS reservation_count
FROM
    date_range
LEFT JOIN
    reservation_complete A ON A.check_in_date = date_range.check_in_date
LEFT JOIN
    room C ON A.room_id = C.room_id
LEFT JOIN
    glamping B ON B.glamp_id = C.glamp_id
        AND B.owner_id = 2
GROUP BY
    date_range.check_in_date
ORDER BY
    date_range.check_in_date;
 */