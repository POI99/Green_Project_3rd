package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "reservation_complete")
@Table(name = "reservation_complete")
public class ReservationCompleteEntity extends CreatedAt{

    //예약 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;//예약 ID

    @Column(length = 13, nullable = false, unique = true)
    private String bookId;  //예약 번호

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;// 유저 ID

    @ManyToOne
    @JoinColumn(name = "glamp_id", nullable = false)
    private GlampingEntity glamping; // 글램핑 ID

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity roomId;  //객실 ID

    @Column(length = 10, nullable = false)
    private String inputName;//예약자 성함

    @Column(length = 11, nullable = false)
    private int personnel;

    @Column(nullable = false)
    LocalDate checkInDate;//체크인 일자

    @Column(nullable = false)
    LocalDate checkOutDate;//체크아웃 일자

    @Column(length = 10, nullable = false)
    private String pg;  //결제수단

    @Column(length = 20, nullable = false)
    private long payAmount;//최종 결제 가격

    @Column(nullable = false)
    @ColumnDefault("0")
    private long status;    //리뷰 작성 가능 상태


//    public ReservationCompleteEntity(Long reservationId, Long userId, String bookId, long glampId, Long roomId, String inputName,
//                                     int personnel, LocalDate checkInDate, LocalDate checkOutDate,
//                                     long payAmount, String pg, LocalDateTime createdAt) {
//        this.reservationId = reservationId;
//        this.userId = userId;
//        this.bookId = bookId;
//        this.glampId = glampId;
//        this.roomId = roomId;
//        this.inputName = inputName;
//        this.personnel = personnel;
//        this.checkInDate = checkInDate;
//        this.checkOutDate = checkOutDate;
//        this.payAmount = payAmount;
//        this.pg = pg;
//        this.createdAt = createdAt;
//    }

}
