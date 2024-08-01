package com.green.glampick.controller;

import com.green.glampick.dto.request.owner.GlampingPostRequestDto;
import com.green.glampick.dto.request.ReviewPatchRequestDto;
import com.green.glampick.dto.request.ReviewPostRequestDto;
import com.green.glampick.dto.request.owner.GlampingPutRequestDto;
import com.green.glampick.dto.request.owner.RoomPostRequestDto;
import com.green.glampick.dto.request.owner.RoomPutRequestDto;
import com.green.glampick.dto.request.owner.module.GlampingModule;
import com.green.glampick.dto.request.user.GetReviewRequestDto;
import com.green.glampick.dto.response.owner.*;
import com.green.glampick.dto.response.owner.get.GetOwnerBookListResponseDto;
import com.green.glampick.dto.response.owner.post.PostGlampingInfoResponseDto;
import com.green.glampick.dto.response.owner.post.PostRoomInfoResponseDto;
import com.green.glampick.dto.response.owner.put.PutGlampingInfoResponseDto;
import com.green.glampick.dto.response.owner.put.PutRoomInfoResponseDto;
import com.green.glampick.dto.response.user.GetReviewResponseDto;
import com.green.glampick.security.AuthenticationFacade;
import com.green.glampick.service.OwnerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.green.glampick.common.swagger.description.owner.PostOwnerReviewSwaggerDescription.POST_OWNER_REVIEW_DESCRIPTION;
import static com.green.glampick.common.swagger.description.user.GetUserReviewSwaggerDescription.USER_REVIEW_VIEW_DESCRIPTION;
import static com.green.glampick.common.swagger.description.user.GetUserReviewSwaggerDescription.USER_REVIEW_VIEW_RESPONSE_ERROR_CODE;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner")
@Tag(name = "사장님 컨트롤러")
public class OwnerController {

    private final OwnerService service;
    private final AuthenticationFacade authenticationFacade;
// 민지 =================================================================================================================
    // create - 글램핑
    @PostMapping(value = "glamping", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "글램핑 정보 등록 (김민지)", description =
            "<p> <strong> 선택입력 : extraCharge(기준 인원 외 추가 인원당 요금) </strong> </p>" +
                    "<p> <strong> 나머지 모든 데이터는 필수 입력입니다. </strong> </p>" +
                    "<p> 사진 업로드를 위해 테스트는 포스트맨에서 해주세요 ~ </p>")
    @ApiResponse(description =
                    "<p> <strong> ResponseCode 응답 코드 </strong> </p> " +
                            "<p> SU(200) : 글램핑 등록 성공 </p> " +
                            "<p> VF(400) : request 데이터 입력 오류 </p> " +
                            "<p> CU(400) : jwt 오류 </p> " +
                            "<p> FE(400) : 이미지 업로드 오류 </p> " +
                            "<p> DBE(500) : 데이터베이스 서버 오류 </p> ",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PostGlampingInfoResponseDto.class)))
    public ResponseEntity<? super PostGlampingInfoResponseDto> createGlamping(@RequestPart @Valid GlampingPostRequestDto req
            , @RequestPart MultipartFile glampImg) {
        return service.postGlampingInfo(req, glampImg);
    }

    // update - 글램핑
    @PutMapping("glamping")
    @Operation(summary = "글램핑 정보 수정 (김민지)", description =
            "<p> <strong> 선택입력 : extraCharge(기준 인원 외 추가 인원당 요금) </strong> </p>" +
                    "<p> <strong> 나머지 모든 데이터는 필수 입력입니다. </strong> </p>")
    @ApiResponse(description =
            "<p> <strong> ResponseCode 응답 코드 </strong> </p> " +
                    "<p> SU(200) : 정보 수정 성공 </p> " +
                    "<p> VF(400) : request 데이터 입력 오류 </p> " +
                    "<p> CU(400) : jwt 오류 </p> " +
                    "<p> DBE(500) : 데이터베이스 서버 오류 </p> ",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PutGlampingInfoResponseDto.class)))
    public ResponseEntity<? super PutGlampingInfoResponseDto> updateGlamping(@RequestBody GlampingPutRequestDto req) {
        return service.updateGlampingInfo(req);
    }

    // update - 글램핑 사진
    @PutMapping("glamping/image")
    @Operation(summary = "글램핑 대표 이미지 변경", description =
            "<p> <strong> 선택입력 : extraCharge(기준 인원 외 추가 인원당 요금) </strong> </p>" +
                    "<p> <strong> 나머지 모든 데이터는 필수 입력입니다. </strong> </p>")
    @ApiResponse(description =
            "<p> <strong> ResponseCode 응답 코드 </strong> </p> " +
                    "<p> SU(200) : 정보 수정 성공 </p> " +
                    "<p> VF(400) : request 데이터 입력 오류 </p> " +
                    "<p> CU(400) : jwt 오류 </p> " +
                    "<p> DBE(500) : 데이터베이스 서버 오류 </p> ",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PutGlampingInfoResponseDto.class)))
    public ResponseEntity<? super PutGlampingInfoResponseDto> updateGlampingImage(@RequestPart MultipartFile image, @RequestPart long glampId) {
        return service.changeGlampingImage(image, glampId);
    }

    // create - 객실
    @PostMapping(value = "room", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "객실 정보 등록 (김민지)", description =
            "<p> <strong> 선택입력 : service[] </strong> </p>" +
                    "<p> <strong> 나머지 모든 데이터는 필수 입력입니다. </strong> </p>" +
                    "<p> 사진 업로드를 위해 테스트는 포스트맨에서 해주세요 ~ </p>" +
                    "<p> <strong> 시간 입력 형식 = 시:분:초  ex) 12:00:00 </strong> </p>")
    @ApiResponse(description =
            "<p> <strong> ResponseCode 응답 코드 </strong> </p> " +
                    "<p> SU(200) : 글램핑 등록 성공 </p> " +
                    "<p> VF(400) : request 데이터 입력 오류 </p> " +
                    "<p> FE(400) : 이미지 업로드 오류 </p> " +
                    "<p> DBE(500) : 데이터베이스 서버 오류 </p> ",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PostRoomInfoResponseDto.class)))
    public ResponseEntity<? super PostRoomInfoResponseDto> createRoom(@RequestPart @Valid RoomPostRequestDto req
            , @RequestPart List<MultipartFile> roomImg) {
        return service.postRoomInfo(req, roomImg);
    }


    // update - 객실
    @PutMapping("room")
    @Operation(summary = "객실 정보 수정 (김민지)", description =
            "<p> <strong> 선택입력 : service[] </strong> </p>" +
                    "<p> <strong> 나머지 모든 데이터는 필수 입력입니다. </strong> </p>" +
                    "<p> <strong> 시간 입력 형식 = 시:분:초  ex) 12:00:00 </strong> </p>")
    @ApiResponse(description =
            "<p> <strong> ResponseCode 응답 코드 </strong> </p> " +
                    "<p> SU(200) : 정보 수정 성공 </p> " +
                    "<p> VF(400) : request 데이터 입력 오류 </p> " +
                    "<p> DBE(500) : 데이터베이스 서버 오류 </p> ",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PutRoomInfoResponseDto.class)))
    public ResponseEntity<? super PutRoomInfoResponseDto> updateRoom(@RequestBody RoomPutRequestDto req) {
        return service.updateRoomInfo(req);
    }

    // read - 예약
    @GetMapping("book/{glamp_id}")
    @Operation(summary = "글램핑 예약 내역 불러오기 (김민지)", description =
            "<strong> <p> glamp_id (글램핑 PK) 는 필수입력입니다 </p> </strong>" +
                    "<p> before : 이용 예정  </p>" +
                    "<p> complete : 이용 완료 </p>" +
                    "<p> cancel : 취소 </p>")
    @ApiResponse(description =
            "<p> <strong> ResponseCode 응답 코드 </strong> </p> " +
                    "<p> SU(200) : 예약 내역 불러오기 성공 </p> " +
                    "<p> RN(200) : 예약 내역 없음 </p> " +
                    "<p> VF(400) : 글램핑 PK가 입력되지 않음 </p> " +
                    "<p> DBE(500) : 데이터베이스 서버 오류 </p> ",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetOwnerBookListResponseDto.class)))
    public ResponseEntity<? super GetOwnerBookListResponseDto> getGlampReservation(@PathVariable("glamp_id") Long glampId) {
        return service.getGlampReservation(glampId);
    }
// 강국 =================================================================================================================

    @Operation(summary = "리뷰 답글 작성하기 (배강국)",
            description = POST_OWNER_REVIEW_DESCRIPTION
            ,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description =
                                    "성공에 대한 반환 값 입니다." +
                                            " <p> userId : 유저 PK <p>  ex)13 </p>",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PatchOwnerReviewInfoResponseDto.class)
                            ))})
    @PatchMapping("/review")
    public ResponseEntity<? super PatchOwnerReviewInfoResponseDto> patchReview(@RequestBody @Valid ReviewPatchRequestDto p) {
        log.info("controller {}", p);
        GlampingModule.ownerId(authenticationFacade);
        GlampingModule.roleCheck(authenticationFacade.getLoginUser().getRole());
        return service.patchReview(p);
    }
    @GetMapping("/review")
    @Operation(summary = "리뷰 불러오기 (배강국)", description = USER_REVIEW_VIEW_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_REVIEW_VIEW_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetReviewResponseDto.class)))
    public ResponseEntity<?super GetReviewResponseDto> getReview(@ParameterObject @ModelAttribute GetReviewRequestDto dto) {
        log.info("controller p: {}", dto);

        long ownerId = GlampingModule.ownerId(authenticationFacade);
        GlampingModule.roleCheck(authenticationFacade.getLoginUser().getRole());
        dto.setOwnerId(ownerId);
        return service.getReview(dto);
    }
//    @Operation(summary = "예약정보 취소 처리 하기",
//            description =
//                    "<strong> 변수명 </strong> reservationId : 예약 PK <p>  ex)21 </p>",
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description =
//                                    "<p> result: 수정실패 0 수정성공 1 </p>",
//                            content = @Content(
//                                    mediaType = "application/json",
//                                    schema = @Schema(implementation = PatchOwnerReviewInfoResponseDto.class)
//                            ))})
//    @PatchMapping("book")
//    public ResponseEntity<? super PatchOwnerReviewInfoResponseDto> patchReview(@RequestBody ReviewPatchRequestDto p) {
//        return service.patchReview(p);
//    }


}

