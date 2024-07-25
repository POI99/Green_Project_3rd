package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "glamp_favorite", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"userId","glampId"}
)})
public class GlampFavoriteEntity extends CreatedAt {
    //관심 글램핑 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long glampFavoriteId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;  //유저 ID

    @ManyToOne
    @JoinColumn(name = "glampId", nullable = false)
    private GlampingEntity glamping;  //글램핑 ID

}
