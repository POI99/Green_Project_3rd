package com.green.glampick.jin;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.jin.object.GetGlampingHeart;
import com.green.glampick.jin.object.GetPopularRoom;
import com.green.glampick.jin.request.ReviewGetCancelRequestDto;
import com.green.glampick.jin.request.ReviewGetHeartRequestDto;
import com.green.glampick.jin.request.ReviewGetRoomRequestDto;
import com.green.glampick.jin.request.ReviewGetStarRequestDto;
import com.green.glampick.jin.response.GetGlampingCancelResponseDto;
import com.green.glampick.jin.response.GetGlampingHeartResponseDto;
import com.green.glampick.jin.response.GetOwnerPopularRoomResponseDto;
import com.green.glampick.jin.response.GetOwnerStarResponseDto;
import com.green.glampick.mapper.OwnerMapper;
import com.green.glampick.security.AuthenticationFacade;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerJinServiceImpl implements OwnerJinService {
    private final OwnerMapper mapper;
    private final AuthenticationFacade authenticationFacade;
    private final CustomFileUtils customFileUtils;
    private final EntityManager em;
    private final OwnerJinRepository ownerRepository;




    @Override// 이용 완료된 객실별 예약수, 매출
    public ResponseEntity<? super GetOwnerPopularRoomResponseDto> getPopRoom(ReviewGetRoomRequestDto dto) {
        try {
            dto.setOwnerId(authenticationFacade.getLoginUserId());
            if (dto.getOwnerId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GetOwnerPopularRoomResponseDto.validateUserId();
        }
        List<GetPopularRoom> popRoom = null;
        try {
            popRoom = ownerRepository.findPopularRoom(dto.getOwnerId());
        } catch (Exception e) {
            e.printStackTrace();

        }

        return GetOwnerPopularRoomResponseDto.success(popRoom);
    }

    @Override// 별점
    public ResponseEntity<? super GetOwnerStarResponseDto> getStarRoom(ReviewGetStarRequestDto dto) {
        try {
            dto.setOwnerId(authenticationFacade.getLoginUserId());
            if (dto.getOwnerId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GetOwnerStarResponseDto.validateUserId();
        }

        try {
             ownerRepository.findByIdStarPoint(dto.getOwnerId());
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return GetOwnerStarResponseDto.success(ownerRepository.findByIdStarPoint(dto.getOwnerId()));
    }
    @Override// 관심 수
    public ResponseEntity<? super GetGlampingHeartResponseDto> getHeartRoom(ReviewGetHeartRequestDto dto) {
        try {
            dto.setOwnerId(authenticationFacade.getLoginUserId());
            if (dto.getOwnerId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GetGlampingHeartResponseDto.validateUserId();
        }
            List<GetGlampingHeart> getGlampingHearts = null;
        try {
            getGlampingHearts = ownerRepository.findGlampingHeart(dto.getGlampId());
        } catch (Exception e) {
            e.printStackTrace();

        }

        return GetGlampingHeartResponseDto.success(getGlampingHearts);
    }



    @Override// 예약 취소율
    public ResponseEntity<? super GetGlampingCancelResponseDto> getGlampingCancelRoom(ReviewGetCancelRequestDto dto) {
        try {
            dto.setOwnerId(authenticationFacade.getLoginUserId());
            if (dto.getOwnerId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GetGlampingCancelResponseDto.validateUserId();
        }
        long total = ownerRepository.findTotalCount(dto.getGlampId());
        long cancel = ownerRepository.findCancelCount(dto.getGlampId());

        double result = (double) cancel / total * 100;
        String formattedResult = String.format("%.2f", result);

        return GetGlampingCancelResponseDto.success(formattedResult);
    }
}






























