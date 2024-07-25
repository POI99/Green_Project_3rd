package com.green.glampick.entity;

import com.green.glampick.dto.request.book.PostBookRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reservation_before", uniqueConstraints =  {
        @UniqueConstraint(
                columnNames = {"book_id"}
        )
})
public class ReservationBeforeEntity extends CreatedAt {
    //예약 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;//예약 ID

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


//    public ReservationBeforeEntity(PostBookRequestDto dto) {
//
//        this.reservationId = dto.getReservationId();
//        this.userId = dto.getUserId();
//        this.bookId = dto.getBookId();
//        this.glampId = dto.getGlampId();
//        this.roomId = dto.getRoomId();
//        this.inputName = dto.getInputName();
//        this.checkInDate = dto.getCheckInDate();
//        this.checkOutDate = dto.getCheckOutDate();
//        this.personnel = dto.getPersonnel();
//        this.pg = dto.getPg();
//        this.payAmount = dto.getPayAmount();
//    }
}
