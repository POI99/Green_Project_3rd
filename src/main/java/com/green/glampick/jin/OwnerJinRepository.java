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
                    "SELECT glamp_id AS glampId " +
                            ", COUNT(glamp_id) AS glampIdCount " +
                            ", sum(pay_amount) AS payAmount " +
                            ", DATE(created_at) as createdAt " +
                            "FROM reservation_complete " +
                            "Group BY glamp_id, created_at  " +
                            "HAVING created_at BETWEEN DATE_ADD(NOW(), INTERVAL -7 DAY ) AND NOW() " +
                            "AND glamp_id = :ownerId ",
            nativeQuery = true
    )
    List<GetPopularRoom> findPopularRoom(long ownerId);

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
                    "SELECT COUNT(glamp_id) FROM reservation_cancel WHERE glamp_id = :glampId ",
            nativeQuery = true
    )
    long findCancelCount(long glampId);

}
