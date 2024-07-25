package com.green.glampick.entity;

import com.green.glampick.dto.request.user.PostAvgRequest;
import com.green.glampick.dto.request.user.PostReviewRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.geo.Point;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "glamping")
public class GlampingEntity extends UpdatedAt {

    // 글램핑 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long glampId;  // 글램핑 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;  // 유저 ID

    @Column(length = 50, nullable = false)
    private String glampName;  // 글램핑명

    @ColumnDefault("0")
    private Double recommendScore;

    @Column(length = 200, nullable = false)
    private String glampImage;

    @Column(nullable = false) @ColumnDefault("0")
    private Double starPointAvg;  // 평균 별점

    @Column(nullable = false) @ColumnDefault("0")
    private int reviewCount; // 댓글갯수

    @Column(length = 50, nullable = false, unique = true)
    private String glampLocation;  // 글램핑 위치

    @Column(unique = true)
    private Point location;  // 좌표 (지도용)

    @Column(nullable = false)
    private int region;  // 글램핑 지역분류

    @ColumnDefault("0")
    private int extraCharge;   // 인원 추가 요금

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String glampIntro;  // 글램핑 소개

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String infoBasic;  // 글램핑 기본정보

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String infoNotice;  // 글램핑 이용안내

    @Column
    private String traffic;  // 글램핑 주차정보

}