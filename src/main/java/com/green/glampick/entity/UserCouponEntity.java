package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_coupon", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"userId","couponId"} )})
// 유저 쿠폰 테이블
public class UserCouponEntity extends CreatedAt {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Comment("유저쿠폰 ID")
    private Long userCouponId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false) @Comment("유저 ID")
    private UserEntity userEntity;  // 유저 ID

    @ManyToOne
    @JoinColumn(name = "couponId", nullable = false) @Comment("쿠폰 ID")
    private CouponEntity couponEntity;  // 쿠폰 ID

    @Column(nullable = false) @Comment("쿠폰 마감일")
    private LocalDate deadLine;  // 쿠폰 마감일

    @Column(nullable = false) @ColumnDefault("1") @Comment("사용 가능 유무 / 가능 = 1, 불가능 = 0")
    private long isActive;  // 사용 유무

}
