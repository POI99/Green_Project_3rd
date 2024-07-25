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
@Table(name = "reservation_before")
public class ReservationBeforeEntity extends CreatedAt {
    //예약 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;//예약 ID

    @Column(length = 13, nullable = false)
    private String bookId;

    private long userId;//유저 ID


    private long glampId;

    private long roomId;  //객실 ID

    private String inputName;//예약자 성함

    private int personnel;

    LocalDate checkInDate;//체크인 일자

    LocalDate checkOutDate;//체크아웃 일자

    private String pg;

    private long payAmount;//최종 결제 가격


    public ReservationBeforeEntity(PostBookRequestDto dto) {

        this.reservationId = dto.getReservationId();
        this.userId = dto.getUserId();
        this.bookId = dto.getBookId();
        this.glampId = dto.getGlampId();
        this.roomId = dto.getRoomId();
        this.inputName = dto.getInputName();
        this.checkInDate = dto.getCheckInDate();
        this.checkOutDate = dto.getCheckOutDate();
        this.personnel = dto.getPersonnel();
        this.pg = dto.getPg();
        this.payAmount = dto.getPayAmount();
    }
}
