package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity(name = "reservation_cancel")
@Table(name = "reservation_cancel", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"book_id"}
        )
})
public class ReservationCancelEntity extends CreatedAt {

    //예약 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reservationId;  //예약 ID

    @Column(length = 13, nullable = false)
    private String bookId;  //예약 번호

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;// 유저 ID

    @ManyToOne
    @JoinColumn(name = "glampId", nullable = false)
    private GlampingEntity glamping; // 글램핑 ID

    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
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

    @Column(length = 500, nullable = true)
    private String comment;  //예약 취소 사유

//    public ReservationCancelEntity(long reservationId, long userId, String bookId, long glampId, long roomId, String inputName,
//                                   int personnel, LocalDate checkInDate, LocalDate checkOutDate, String pg,
//                                   long payAmount, String comment, LocalDateTime createdAt)
//    {
//        this.reservationId = reservationId;
//        this.userId = userId;
//        this.bookId = bookId;
//        this.glampId = glampId;
//        this.roomId = roomId;
//        this.inputName = inputName;
//        this.personnel = personnel;
//        this.checkInDate = checkInDate;
//        this.checkOutDate = checkOutDate;
//        this.pg = pg;
//        this.payAmount = payAmount;
//        this.comment = comment;
//        this.createdAt = createdAt;
//    }
}
