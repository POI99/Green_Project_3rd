package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room")
public class RoomEntity extends CreatedAt {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;  //객실 ID

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private GlampingEntity glampId;  //글램핑 ID

    @Column(length = 30, nullable = false)
    private String roomName;  //객실명

    @Column(length = 11, nullable = false)
    private int roomPrice;  //객실 가격

    @Column(length = 11, nullable = false)
    private int roomNumPeople;  //객실 기준인원

    @Column(length = 11, nullable = false)
    private int roomMaxPeople;  //객실 최대인원

    @Column(nullable = false)
    private String checkInTime;  //객실 체크인 시간

    @Column(nullable = false)
    private String checkOutTime;  //객실 체크아웃 시간




}
