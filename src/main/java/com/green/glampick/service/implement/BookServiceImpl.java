package com.green.glampick.service.implement;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.request.book.GetBookPayRequestDto;
import com.green.glampick.dto.request.book.PostBookRequestDto;
import com.green.glampick.dto.response.book.GetBookPayResponseDto;
import com.green.glampick.dto.response.book.PostBookResponseDto;
import com.green.glampick.entity.*;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.BookErrorCode;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.GlampingErrorCode;
import com.green.glampick.repository.*;
import com.green.glampick.security.AuthenticationFacade;
import com.green.glampick.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final ReservationBeforeRepository reservationBeforeRepository;
    private final ReservationCompleteRepository reservationCompleteRepository;
    private final RoomRepository roomRepository;
    private final GlampingRepository glampingRepository;
    private final AuthenticationFacade authenticationFacade;

    //  글램핑 예약하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostBookResponseDto> postBook(PostBookRequestDto dto) {

        //  로그인 유저가 없다면, 권한이 없다는 응답을 보낸다.  //
        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }

        ReservationBeforeEntity reservationBeforeEntity = new ReservationBeforeEntity();

        try {
            //  중복된 예약 내역이 있는지 확인하고 있다면 중복된 예약내역에 대한 응답을 보낸다.  //
            boolean existedReservation = reservationBeforeRepository.existsByReservationId
                    (reservationBeforeEntity.getReservationId());
            if (existedReservation) {
                throw new CustomException(BookErrorCode.DB);
            }

            if (checkDate(dto.getCheckInDate(), dto.getCheckOutDate())) {
                throw new CustomException(GlampingErrorCode.WD);
            }

            //  현재시간을 기준으로 랜덤 13자리의 수를 생성한다.  //
            long currentTimeMillis = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");  // 날짜 포맷 설정 (년월일시분초)
            String dateString = dateFormat.format(new Date(currentTimeMillis));
            Random random = new Random();
            int randomNum = random.nextInt(10);  // 0-9까지의 숫자 중 하나 생성
            String reservationNumber = dateString + randomNum;  // 예약번호 생성 (날짜 + 난수)

            //  생성한 랜덤 13자리 수를 예약번호에 넣는다.  //
            reservationBeforeEntity.setBookId(reservationNumber);

            RoomEntity roomEntity = roomRepository.findByRoomId(dto.getRoomId());
            GlampingEntity glampingEntity = glampingRepository.findByGlampId(dto.getGlampId());
            long roomPrice = roomEntity.getRoomPrice();
            long personnel = dto.getPersonnel();
            long roomPeople = roomEntity.getRoomNumPeople();
            long extraCharge = glampingEntity.getExtraCharge();

            long payAmount = roomPrice + (personnel - roomPeople) * extraCharge;

            reservationBeforeEntity.setPayAmount(payAmount);

            log.info("PerSonnel : {}", personnel);

            //  가공이 완료된 Entity 를 DB에 저장한다.  //
            reservationBeforeRepository.save(reservationBeforeEntity);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PostBookResponseDto.success();

    }

    //  최종 결제 가격정보  //
    @Override
    public ResponseEntity<? super GetBookPayResponseDto> getReservationAmount(GetBookPayRequestDto dto) {

        long roomPrice = 0;
        long extraChargePrice = 0;
        long payAmount = 0;


        try {

            RoomEntity roomEntity = roomRepository.findByRoomId(dto.getRoomId());
            GlampingEntity glampingEntity = glampingRepository.findByGlampId(dto.getGlampId());
            roomPrice = roomEntity.getRoomPrice();
            long roomPeople = roomEntity.getRoomNumPeople();
            long extraCharge = glampingEntity.getExtraCharge();

            extraChargePrice = (dto.getPersonnel() - roomPeople) * extraCharge;

            payAmount = roomPrice + (dto.getPersonnel() - roomPeople) * extraCharge;

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return GetBookPayResponseDto.success(roomPrice, extraChargePrice, payAmount);

    }

    private boolean checkDate(LocalDate in, LocalDate out) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return out.isBefore(in); // 틀리면 true
    }

//    @Scheduled(fixedRate = 600000)
//    public void cleanUpExpiredCodes() {
//
//        LocalDate currentDateTime = LocalDate.now();
//
//        // 체크아웃 날짜가 지난 모든 데이터를 가져옴
//        List<ReservationBeforeEntity> expiredReservations = reservationBeforeRepository.findAllByCheckOutDateBefore(currentDateTime);
//
//
//        for (ReservationBeforeEntity beforeEntity : expiredReservations) {
//            // 예약 데이터를 완료 엔티티로 옮김
//            ReservationCompleteEntity completeEntity = new ReservationCompleteEntity();
//            completeEntity.setUser(beforeEntity.getUser());
//            completeEntity.setBookId(beforeEntity.getBookId());
//            completeEntity.setGlamping(beforeEntity.getGlamping());
//            completeEntity.setRoomId(beforeEntity.getRoomId());
//            completeEntity.setInputName(beforeEntity.getInputName());
//            completeEntity.setPersonnel(beforeEntity.getPersonnel());
//            completeEntity.setCheckInDate(beforeEntity.getCheckInDate());
//            completeEntity.setCheckOutDate(beforeEntity.getCheckOutDate());
//            completeEntity.setPg(beforeEntity.getPg());
//            completeEntity.setPayAmount(beforeEntity.getPayAmount());
//
//            reservationCompleteRepository.save(completeEntity);
//            reservationBeforeRepository.delete(beforeEntity);
//        }
//
//    }

}
