package com.green.glampick.jin;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.OwnerErrorCode;
import com.green.glampick.jin.object.*;
import com.green.glampick.jin.request.*;
import com.green.glampick.jin.response.*;
import com.green.glampick.repository.ReservationCompleteRepository;
import com.green.glampick.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerJinServiceImpl implements OwnerJinService {
    private final AuthenticationFacade authenticationFacade;
    private final OwnerJinRepository ownerRepository;
    private final ReservationCompleteRepository reservationCompleteRepository;


    @Override// 이용 완료된 객실별 예약수
    @Transactional
    public ResponseEntity<? super GetOwnerPopularRoomResponseDto> getPopRoom(ReviewGetRoomRequestDto dto) {
        try {
            dto.setOwnerId(authenticationFacade.getLoginUserId());
            if (dto.getOwnerId() <= 0) {
                throw new CustomException(CommonErrorCode.MNF);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }
        Long total = 0L;
        List<GetPopularRoom> popRoom = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formStart = dto.getStartDayId().format(formatter);
        String formEnd = dto.getEndDayId().format(formatter);
        try {

            total = reservationCompleteRepository.findTotal(dto.getOwnerId(), formStart, formEnd);
            popRoom = reservationCompleteRepository.findPopularRoom(dto.getOwnerId(), formStart, formEnd);
            if (dto.getOwnerId() == 0) {
                throw new CustomException(OwnerErrorCode.NMG);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return GetOwnerPopularRoomResponseDto.success(total, popRoom);
    }

    @Override// 별점
    @Transactional
    public ResponseEntity<? super GetOwnerStarResponseDto> getStarRoom(ReviewGetStarRequestDto dto) {
        try {
            dto.setOwnerId(authenticationFacade.getLoginUserId());
            if (dto.getOwnerId() <= 0) {
                throw new CustomException(CommonErrorCode.MNF);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }
        List<GetStarHeart> starHearts = null;
        try {
            starHearts = ownerRepository.findByIdStarPoint(dto.getOwnerId());
            if (dto.getOwnerId() == 0) {
                throw new CustomException(OwnerErrorCode.NMG);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return GetOwnerStarResponseDto.success(starHearts);
    }


    @Override// 예약 취소율
    @Transactional
    public ResponseEntity<? super GetGlampingCancelResponseDto> getGlampingCancelRoom(ReviewGetCancelRequestDto dto) {
        try {
            dto.setOwnerId(authenticationFacade.getLoginUserId());
            if (dto.getOwnerId() <= 0) {
                throw new CustomException(CommonErrorCode.MNF);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }
        String formattedResult = null;
        List<GetCancelDto> room = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formStart = dto.getStartDayId().format(formatter);
        String formEnd = dto.getEndDayId().format(formatter);
        try {
            room = ownerRepository.findRoomCount(dto.getOwnerId(), formStart, formEnd);
            Long total = ownerRepository.findTotalCount(dto.getOwnerId(), formStart, formEnd);
            total = total == null ? 0L : total;
            Long cancel = ownerRepository.findCancelCount(dto.getOwnerId(), formStart, formEnd);
            cancel = cancel == null ? 0L : cancel;
            if (dto.getOwnerId() == 0) {
                throw new CustomException(OwnerErrorCode.NMG);
            }
            double result = (double) cancel / total * 100;
            formattedResult = String.format("%.2f", result);


        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
        return GetGlampingCancelResponseDto.success(room, formattedResult);
    }

    @Override//매출
    @Transactional
    public ResponseEntity<? super GetOwnerRevenueResponseDto> getRevenue(ReviewGetRevenueRequestDto dto) {
        try {
            dto.setOwnerId(authenticationFacade.getLoginUserId());
            if (dto.getOwnerId() <= 0) {
                throw new CustomException(CommonErrorCode.MNF);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }
        List<GetRevenue> revenue = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formStart = dto.getStartDayId().format(formatter);
        String formEnd = dto.getEndDayId().format(formatter);
        try {
            revenue = ownerRepository.findRevenue(dto.getOwnerId(), formStart, formEnd);
            if (dto.getOwnerId() == 0) {
                throw new CustomException(OwnerErrorCode.NMG);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return GetOwnerRevenueResponseDto.success(revenue);
    }
}






























