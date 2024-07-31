package com.green.glampick.jin;

import com.green.glampick.jin.request.ReviewGetCancelRequestDto;
import com.green.glampick.jin.request.ReviewGetHeartRequestDto;
import com.green.glampick.jin.request.ReviewGetRoomRequestDto;
import com.green.glampick.jin.request.ReviewGetStarRequestDto;
import com.green.glampick.jin.response.GetGlampingCancelResponseDto;
import com.green.glampick.jin.response.GetGlampingHeartResponseDto;
import com.green.glampick.jin.response.GetOwnerPopularRoomResponseDto;
import com.green.glampick.jin.response.GetOwnerStarResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jin/owner")
@Tag(name = "사장님 컨트롤러")
public class OwnerJinController {

    private final OwnerJinService service;


    // 이용 완료된 객실별 예약수, 매출
    public ResponseEntity<? super GetOwnerPopularRoomResponseDto> getPopRoom(@RequestPart ReviewGetRoomRequestDto dto) {
        return service.getPopRoom(dto);
    }

    // 별점
    public ResponseEntity<? super GetOwnerStarResponseDto> getStarRoom(@RequestPart ReviewGetStarRequestDto dto) {
        return service.getStarRoom(dto);
    }

    // 관심 수
    public ResponseEntity<? super GetGlampingHeartResponseDto> getHeartRoom(@RequestBody ReviewGetHeartRequestDto dto) {
        return service.getHeartRoom(dto);
    }

    // 예약 취소율
    public ResponseEntity<? super GetGlampingCancelResponseDto> getGlampingCancelRoom(@RequestBody ReviewGetCancelRequestDto dto) {
        return service.getGlampingCancelRoom(dto);
    }





}

