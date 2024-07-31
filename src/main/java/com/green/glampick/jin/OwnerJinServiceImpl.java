package com.green.glampick.jin;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.dto.response.user.GetBookResponseDto;
import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.ReservationCompleteEntity;
import com.green.glampick.entity.UserEntity;
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
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GetOwnerPopularRoomResponseDto.validateUserId();
        }




        return null;
    }

    @Override// 별점
    public ResponseEntity<? super GetOwnerStarResponseDto> getStarRoom(ReviewGetStarRequestDto dto) {
        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GetOwnerStarResponseDto.validateUserId();
        }
        ownerRepository.findStarPointAv();
        return null;
    }

    @Override// 관심 수
    public ResponseEntity<? super GetGlampingHeartResponseDto> getHeartRoom(ReviewGetHeartRequestDto dto) {
        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GetGlampingHeartResponseDto.validateUserId();
        }
        return null;
    }
    @Override// 예약 취소율
    public ResponseEntity<? super GetGlampingCancelResponseDto> getGlampingCancelRoom(ReviewGetCancelRequestDto dto) {
        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GetGlampingCancelResponseDto.validateUserId();
        }
        return null;
    }
}
