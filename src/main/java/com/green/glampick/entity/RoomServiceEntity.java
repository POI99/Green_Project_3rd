package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "glampService", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"roomId","serviceId"}
)})
public class RoomServiceEntity extends CreatedAt {
    //룸 서비스시설 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomServiceId; // 룸-서비스 ID

    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    private RoomEntity room;  //객실 ID

    @ManyToOne
    @JoinColumn(name = "serviceId", nullable = false)
    private ServiceEntity service;  //서비스 ID

}
