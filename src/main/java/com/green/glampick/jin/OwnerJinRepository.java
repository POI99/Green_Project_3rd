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
                            "AND glamp_id = :glampId ",
            nativeQuery = true
    )
    List<GetPopularRoom> findPopularRoom(long glampId);

    @Query(
            value =
                    "SELECT glamp_id " +
                            ", COUNT(glamp_id) AS heart " +
                            ", DATE(created_at) as createdAt " +
                            "FROM glamp_favorite " +
                            "Group BY glamp_id, created_at " +
                            "HAVING created_at BETWEEN DATE_ADD(NOW(), INTERVAL -1 week ) AND NOW() " +
                            "AND glamp_id = :glampId ",
            nativeQuery = true
    )
    List<GetGlampingHeart> findGlampingHeart(long glampId);

}
