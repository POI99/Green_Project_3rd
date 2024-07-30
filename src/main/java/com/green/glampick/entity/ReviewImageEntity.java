package com.green.glampick.entity;

import com.green.glampick.dto.request.user.PostReviewRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review_image")
public class ReviewImageEntity extends CreatedAt{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Comment("객실 이미지 ID")
    private Long reviewImageId; // 객실 이미지 ID

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false) @Comment("리뷰 ID")
    private ReviewEntity reviewId; // 리뷰 ID

    @Column(length = 200,nullable = false) @Comment("리뷰 이미지명")
    private String reviewImageName; // 리뷰 이미지명



}
