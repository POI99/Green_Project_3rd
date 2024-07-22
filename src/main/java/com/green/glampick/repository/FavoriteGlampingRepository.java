package com.green.glampick.repository;

import com.green.glampick.entity.GlampFavoriteEntity;
import com.green.glampick.repository.resultset.GetFavoriteGlampingResultSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteGlampingRepository extends JpaRepository<GlampFavoriteEntity, Long> {


    @Query(
            value =
            "SELECT distinct(A.glamp_id) AS glampId " +
            ", A.glamp_name AS glampName " +
            ", A.glamp_location  AS glampLocation " +
            ", A.star_point_avg AS starPoint " +
            ", A.review_count AS reviewCount " +
            ", B.room_price AS price " +
            ", A.glamp_image AS glampImage " +
            ", C.user_id " +
            "FROM glamping A " +
            "JOIN room B " +
            "ON A.glamp_id = B.glamp_id " +
            "JOIN glamp_favorite C " +
            "ON A.glamp_id = C.glamp_id " +
            "WHERE C.user_id = ?1 " +
            "AND B.room_price = ( SELECT MIN(room_price) " +
                                "FROM room " +
                                "WHERE glamp_id = A.glamp_id ) " +
            "ORDER BY C.created_at DESC ",
            nativeQuery = true
    )
    List<GetFavoriteGlampingResultSet> getFavoriteGlamping(Long glampId);
}
//SELECT A.glamp_image  AS glampImage
//, A.glamp_location  AS glampLocation
//, A.glamp_name  AS glampName
//, A.star_point_avg  AS starPointAvg
//, B.room_price AS roomPrice
//, count(C.review_content) AS reviewContent
//, A.glamp_id  AS glampId
//, B.room_name  AS roomName
//, D.user_id
//FROM glamping A
//JOIN room B
//ON b.glamp_id = a.glamp_id
//JOIN review C
//ON C.room_id = B.room_id
//JOIN glamp_favorite D
//ON A.glamp_id = D.glamp_id
//GROUP BY A.glamp_id
//HAVING  D.user_id = 1

// 관심 글램핑 댓글수 실패
//SELECT A.glamp_image  AS glampImage
//, A.glamp_location  AS glampLocation
//, A.glamp_name  AS glampName
//, A.star_point_avg  AS starPointAvg
//, B.room_price AS roomPrice
//, count(C.review_content) AS reviewContent
//, A.glamp_id  AS glampId
//, B.room_name  AS roomName
//, D.user_id
//FROM glamping A
//JOIN room B
//ON A.glamp_id = B.glamp_id
//JOIN review C
//ON B.room_id = C.room_id
//JOIN glamp_favorite D
//ON D.glamp_id = A.glamp_id
//GROUP BY A.glamp_id
//HAVING MIN(B.room_price)
//-- AND D.user_id = 1
//and D.user_id = 1

// 댓글 수
//SELECT A.glamp_image AS glampImage
//, A.glamp_location AS glampLocation
//, A.glamp_name AS glampName
//, A.star_point_avg AS starPointAvg
//, B.room_price AS roomPrice
//, count(C.review_content) AS reviewContent
//, A.glamp_id AS glampId
//, B.room_name AS roomName
//FROM glamping A
//JOIN room B
//ON A.glamp_id = B.glamp_id
//JOIN review C
//ON B.room_id = C.room_id
//GROUP BY A.glamp_id
//HAVING A.glamp_id = 2
//ORDER BY C.created_at DESC




