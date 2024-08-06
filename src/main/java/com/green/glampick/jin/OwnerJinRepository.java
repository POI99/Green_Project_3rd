package com.green.glampick.jin;

import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.jin.object.GetGlampingHeart;
import com.green.glampick.jin.object.GetPopularRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OwnerJinRepository extends JpaRepository<OwnerEntity, Long> {
    @Query(
            value =
                    "SELECT  sub.roomcounts as roomCounts" +
                            ", sub.owners as owners" +
                            ", DATE(sub.createdAt)AS days " +
                            "FROM (SELECT C.room_name as roomName " +
                            ", COUNT(A.room_id) as roomcounts " +
                            ", B.owner_id AS owners " +
                            ", DATE(A.created_at) as createdAt " +
                            "FROM reservation_complete A " +
                            "left JOIN glamping B " +
                            "ON A.glamp_id = B.glamp_id " +
                            "JOIN room C " +
                            "ON C.room_id = A.room_id " +
                            "WHERE B.owner_id = :ownerId AND A.created_at BETWEEN DATE_ADD(NOW(), INTERVAL -:startDayId DAY ) AND DATE_ADD(NOW(), INTERVAL -:endDayId DAY) " +
                            "GROUP BY A.created_at) AS sub " +
                            "GROUP BY days ",
            nativeQuery = true
    )
    List<GetPopularRoom> findPopularRoom(long ownerId, long startDayId, long endDayId);

    @Query(
            value =
                    "SELECT A.glamp_id " +
                            ", COUNT(A.glamp_id) AS heart " +
                            ", DATE(A.created_at) as createdAt " +
                            ", B.owner_id " +
                            "FROM glamp_favorite A " +
                            "JOIN glamping B " +
                            "ON A.glamp_id = B.glamp_id " +
                            "Group BY DATE(A.created_at) " +
                            "HAVING createdAt BETWEEN DATE_ADD(NOW(), INTERVAL -1 week ) AND NOW() " +
                            "AND B.owner_id = :ownerId ",
            nativeQuery = true
    )
    List<GetGlampingHeart> findGlampingHeart(long ownerId);

    @Query(
            value =
                    "SELECT star_point_avg " +
                            "FROM glamping A " +
                            "JOIN owner B " +
                            "ON A.owner_id = B.owner_id " +
                            "WHERE B.owner_id = :ownerId ",
            nativeQuery = true
    )
    double findByIdStarPoint(long ownerId);

    @Query(
            value =
                    "SELECT SUM(glamp_count) AS total_count " +
                            "FROM (SELECT COUNT(glamp_id) AS glamp_count FROM reservation_before WHERE glamp_id = :glampId " +
                            "UNION ALL " +
                            "SELECT COUNT(glamp_id) AS glamp_count FROM reservation_cancel WHERE glamp_id = :glampId " +
                            "UNION ALL " +
                            "SELECT COUNT(glamp_id) AS glamp_count FROM reservation_complete WHERE glamp_id = :glampId) AS counts ",
            nativeQuery = true
    )
    long findTotalCount(long glampId);

    @Query(
            value =
                    "SELECT SUM(glampId) FROM(\n" +
                            "SELECT COUNT(glamp_id) AS glampId, DATE(created_at) AS createdAt FROM reservation_cancel WHERE glamp_id = 2\n" +
                            "Group BY DATE(created_at)\n" +
                            "HAVING createdAt BETWEEN DATE_ADD(NOW(), INTERVAL 9 ) AND DATE_ADD(NOW(), INTERVAL 0) ) AS counts ",
            nativeQuery = true
    )
    long findCancelCount(long glampId);

    @Query(
            value =
                    "SELECT SUM(sub.pay) AS total_count " +
                            ", DATE(sub.createdAt) AS times " +
                            ", sub.roomNam AS roomName " +
                            "FROM (SELECT A.glamp_id, SUM(A.pay_amount) AS pay " +
                            ", date(A.created_at ) AS createdAt " +
                            ", C.room_name AS roomNam " +
                            "FROM reservation_complete A " +
                            "JOIN glamping B ON A.glamp_id = B.glamp_id " +
                            "JOIN room C ON A.room_id = C.room_id " +
                            "WHERE B.owner_id = :ownerId AND A.created_at BETWEEN DATE_ADD(NOW(), INTERVAL -:startDayId DAY ) AND DATE_ADD(NOW(), INTERVAL -:endDayId DAY) " +
                            "GROUP BY A.created_at, C.room_name) AS sub " +
                            "GROUP BY times, roomName ",
            nativeQuery = true
    )
    long findRevenue(long ownerId, long startDayId, long endDayId);


}

/*
@Query(value = "SELECT A.glamp_id, COUNT(A.glamp_id) AS heart, DATE(A.created_at) as createdAt, B.owner_id " +
                   "FROM glamp_favorite A " +
                   "JOIN glamping B ON A.glamp_id = B.glamp_id " +
                   "WHERE B.owner_id = :ownerId " +
                   "AND A.created_at BETWEEN DATE_ADD(NOW(), INTERVAL -:interval DAY) AND NOW() " +
                   "GROUP BY DATE(A.created_at), A.glamp_id, B.owner_id",
           nativeQuery = true)
    List<Object[]> findPopularRooms(@Param("ownerId") long ownerId, @Param("interval") int interval);
 */