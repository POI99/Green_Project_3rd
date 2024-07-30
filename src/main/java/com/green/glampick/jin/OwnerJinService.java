package com.green.glampick.jin;

import com.green.glampick.jin.request.ReviewGetCancelRequestDto;
import com.green.glampick.jin.request.ReviewGetHeartRequestDto;
import com.green.glampick.jin.request.ReviewGetRoomRequestDto;
import com.green.glampick.jin.request.ReviewGetStarRequestDto;
import com.green.glampick.jin.response.GetGlampingCancelResponseDto;
import com.green.glampick.jin.response.GetGlampingHeartResponseDto;
import com.green.glampick.jin.response.GetOwnerPopularRoomResponseDto;
import com.green.glampick.jin.response.GetOwnerStarResponseDto;
import org.springframework.http.ResponseEntity;

public interface OwnerJinService {

    // 이용 완료된 객실별 예약수, 매출
    ResponseEntity<? super GetOwnerPopularRoomResponseDto> getPopRoom(ReviewGetRoomRequestDto dto);

    // 별점
    ResponseEntity<? super GetOwnerStarResponseDto> getStarRoom(ReviewGetStarRequestDto dto);

    // 관심 수
    ResponseEntity<? super GetGlampingHeartResponseDto> getHeartRoom(ReviewGetHeartRequestDto dto);

    // 예약 취소율
    ResponseEntity<? super GetGlampingCancelResponseDto> getGlampingCancelRoom(ReviewGetCancelRequestDto dto);

}
