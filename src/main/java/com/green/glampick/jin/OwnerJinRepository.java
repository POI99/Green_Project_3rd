package com.green.glampick.jin;

import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.repository.resultset.GetReservationBeforeResultSet;
import com.green.glampick.repository.resultset.GetReservationCompleteResultSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OwnerJinRepository extends JpaRepository<OwnerEntity, Long> {
    @Query(
            value =
                    "SELECT glamp_id AS '숙소' " +
                            ", COUNT(glamp_id) AS '예약수' " +
                            ", sum(pay_amount) AS '매출' " +
                            ", created_at " +
                            "FROM reservation_complete " +
                            "Group BY glamp_id " +
                            "HAVING created_at BETWEEN DATE_ADD(NOW(), INTERVAL -30 DAY ) AND NOW() ",
            nativeQuery = true
    )
    void findStarPointAvg();

    @Query(
            value =
                    "SELECT glamp_id " +
                            ", COUNT(glamp_id) AS '좋아요' " +
                            ", created_at " +
                            "FROM glamp_favorite " +
                            "Group BY glamp_id " +
                            "HAVING created_at BETWEEN DATE_ADD(NOW(), INTERVAL -1 DAY ) AND NOW() ",
            nativeQuery = true
    )
    void findStarPointAv();
}
