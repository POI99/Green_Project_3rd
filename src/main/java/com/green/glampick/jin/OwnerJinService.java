package com.green.glampick.jin;

import com.green.glampick.jin.request.*;
import com.green.glampick.jin.response.*;
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

    //매출
    ResponseEntity<? super GetOwnerRevenueResponseDto> getRevenue(ReviewGetRevenueRequestDto dto);
}
