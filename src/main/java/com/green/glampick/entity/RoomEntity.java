package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room")
public class RoomEntity extends UpdatedAt {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Comment("객실 ID")
    private Long roomId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) @Comment("글램핑 ID")
    private GlampingEntity glampId;

    @Column(length = 30, nullable = false) @Comment("객실명")
    private String roomName;

    @Column(length = 11, nullable = false) @Comment("객실 가격")
    private Integer roomPrice;

    @Column(length = 11, nullable = false) @Comment("객실 기준인원")
    private Integer roomNumPeople;

    @Column(length = 11, nullable = false) @Comment("객실 최대인원")
    private Integer roomMaxPeople;

    @Column(nullable = false) @Comment("객실 체크인 시간")
    private String checkInTime;

    @Column(nullable = false) @Comment("객실 체크아웃 시간")
    private String checkOutTime;




}
