package com.green.glampick.jin;


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
        List<GetPopularRoom> popRoom = new ArrayList<>();

        HashMap<String, List<String>> hashMapRoom = new HashMap<>();
        try {
            popRoom = reservationCompleteRepository.findPopularRoom(dto.getOwnerId(), dto.getStartDayId(), dto.getEndDayId());
            for (GetPopularRoom item : popRoom){
                String roomCounts = item.getRoomCounts();
                String days = item.getDays();
                hashMapRoom.put(days, new ArrayList<>());
                hashMapRoom.get(days).add(roomCounts);

//                return 1;
//                throw new CustomException(OwnerErrorCode.NMG);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return GetOwnerPopularRoomResponseDto.success(popRoom);
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

//    @Override// 관심 수
//    @Transactional
//    public ResponseEntity<? super GetGlampingHeartResponseDto> getHeartRoom(ReviewGetHeartRequestDto dto) {
//        try {
//            dto.setOwnerId(authenticationFacade.getLoginUserId());
//            if (dto.getOwnerId() <= 0) {
//                throw new RuntimeException();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new CustomException(CommonErrorCode.MNF);
//        }
//        List<GetGlampingHeart> getGlampingHearts = null;
//        try {
//            getGlampingHearts = ownerRepository.findGlampingHeart(dto.getOwnerId());
//            if (dto.getOwnerId() == 0) {
//                throw new CustomException(OwnerErrorCode.NMG);
//            }
//        } catch (CustomException e) {
//            throw new CustomException(e.getErrorCode());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new CustomException(CommonErrorCode.DBE);
//        }
//
//        return GetGlampingHeartResponseDto.success(getGlampingHearts);
//    }


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
        String formattedResult;

        List<GetCancelDto> room = ownerRepository.findRoomCount(dto.getOwnerId(), dto.getStartDayId(), dto.getEndDayId());
        try {
            long total = ownerRepository.findTotalCount(dto.getOwnerId(), dto.getStartDayId(), dto.getEndDayId());
            long cancel = ownerRepository.findCancelCount(dto.getOwnerId(), dto.getStartDayId(), dto.getEndDayId());
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
        try {
            revenue = ownerRepository.findRevenue(dto.getOwnerId(), dto.getStartDayId());
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






























