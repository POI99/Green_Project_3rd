package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_image")
public class RoomImageEntity extends CreatedAt {
    //객실 이미지 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomImageId;  // 객실 이미지 ID

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity roomId;  // 객실 ID

    @Column(length = 200, nullable = false)
    private String roomImageName;  // 객실 이미지명

}
