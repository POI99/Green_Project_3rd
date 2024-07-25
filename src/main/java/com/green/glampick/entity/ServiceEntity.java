package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service")
public class ServiceEntity extends CreatedAt {
    //서비스시설 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;  // 서비스 ID

    @Column(length = 20, nullable = false)
    private String serviceTitle;  // 서비스 이름

}
