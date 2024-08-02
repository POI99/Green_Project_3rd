package com.green.glampick.jin;

import com.green.glampick.dto.response.user.GetBookResponseDto;
import com.green.glampick.jin.request.*;
import com.green.glampick.jin.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.green.glampick.common.swagger.description.user.GetUserBookSwaggerDescription.USER_BOOK_DESCRIPTION;
import static com.green.glampick.common.swagger.description.user.GetUserBookSwaggerDescription.USER_BOOK_RESPONSE_ERROR_CODE;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jin")
@Tag(name = "jin 컨트롤러")
public class OwnerJinController {

    private final OwnerJinService service;


    // 이용 완료된 객실별 예약수
    @GetMapping("/poproom")
    @Operation(summary = "이용 완료된 객실별 예약수 (이진현)", description = USER_BOOK_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_BOOK_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetOwnerPopularRoomResponseDto.class)))
    public ResponseEntity<? super GetOwnerPopularRoomResponseDto> getPopRoom(@ParameterObject ReviewGetRoomRequestDto dto) {
        return service.getPopRoom(dto);
    }

    // 평균 별점
    @GetMapping("/star")
    @Operation(summary = "평균 별점 (이진현)", description = USER_BOOK_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_BOOK_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetOwnerStarResponseDto.class)))
    public ResponseEntity<? super GetOwnerStarResponseDto> getStarRoom(@ParameterObject ReviewGetStarRequestDto dto) {
        return service.getStarRoom(dto);
    }

    // 관심 수
    @GetMapping("/roomheart")
    @Operation(summary = "관심 수  (이진현)", description = USER_BOOK_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_BOOK_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetGlampingHeartResponseDto.class)))
    public ResponseEntity<? super GetGlampingHeartResponseDto> getHeartRoom(@ParameterObject ReviewGetHeartRequestDto dto) {
        return service.getHeartRoom(dto);
    }

    // 예약 취소율
    @GetMapping("/glampingcancel")
    @Operation(summary = "예약 취소율 (이진현)", description = USER_BOOK_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_BOOK_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetGlampingCancelResponseDto.class)))
    public ResponseEntity<? super GetGlampingCancelResponseDto> getGlampingCancelRoom(@ParameterObject ReviewGetCancelRequestDto dto) {
        return service.getGlampingCancelRoom(dto);
    }

    //매출
    @GetMapping("/revenue")
    @Operation(summary = "매출 (이진현)", description = USER_BOOK_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_BOOK_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetOwnerRevenueResponseDto.class)))
    public ResponseEntity<? super GetOwnerRevenueResponseDto> getPopRoom(@ParameterObject ReviewGetRevenueRequestDto dto) {
        return service.getRevenue(dto);
    }



}

