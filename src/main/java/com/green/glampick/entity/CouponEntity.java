package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupon")
public class CouponEntity extends CreatedAt {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;  // 쿠폰 ID

    @Column(length = 30, nullable = false)
    private String couponName;  //쿠폰명

    @Column(length = 30, nullable = false)
    private String discount;  //쿠폰 할인금액
}
