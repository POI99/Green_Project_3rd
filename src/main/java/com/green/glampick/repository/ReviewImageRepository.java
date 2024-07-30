package com.green.glampick.repository;

import com.green.glampick.entity.ReviewEntity;
import com.green.glampick.entity.ReviewImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImageEntity, Long> {

    List<ReviewImageEntity> findByReviewId(ReviewEntity reviewId);
    List<ReviewImageEntity> findByReviewIdIn(List<Long> reviewIds);

    ReviewImageEntity findByReviewImageId(Long reviewId);
}
