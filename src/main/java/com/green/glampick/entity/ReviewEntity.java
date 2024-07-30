package com.green.glampick.entity;

import com.green.glampick.dto.request.user.PostReviewRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
public class ReviewEntity extends CreatedAt{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;  // 리뷰 PK

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userId;  // 유저 ID

    @ManyToOne
    @JoinColumn(name = "glamp_id", nullable = false)
    private GlampingEntity glampId;// 글램핑 ID

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private ReservationCompleteEntity reservationId; // 객실 ID

    @Column(length = 500, nullable = false )
    private String reviewContent;  // 리뷰 내용

    @Column(nullable = false )
    private int reviewStarPoint;  // 리뷰 별점

    @Column(length = 500)
    private String reviewComment; // 사장님 답변

}
