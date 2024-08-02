package com.green.glampick.controller;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.request.owner.*;
import com.green.glampick.dto.request.ReviewPatchRequestDto;
import com.green.glampick.dto.request.ReviewPostRequestDto;
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
import jakarta.validation.constraints.NotNull;
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

import static com.green.glampick.common.swagger.description.owner.DeleteRoomImageSwaggerDescription.DELETE_ROOM_IMAGE_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.DeleteRoomImageSwaggerDescription.DELETE_ROOM_IMAGE_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.GetBookFromUserSwaggerDescription.BOOK_FROM_USER_REVIEW_VIEW_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.GetBookFromUserSwaggerDescription.BOOK_FROM_USER_REVIEW_VIEW_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.GetGlampingFromUserReviewSwaggerDescription.GLAMPING_FROM_USER_REVIEW_VIEW_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.GetGlampingFromUserReviewSwaggerDescription.GLAMPING_FROM_USER_REVIEW_VIEW_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.PostGlampingSwaggerDescription.POST_GLAMPING_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.PostGlampingSwaggerDescription.POST_GLAMPING_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.PostOwnerReviewSwaggerDescription.POST_OWNER_REVIEW_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.PostOwnerReviewSwaggerDescription.POST_OWNER_REVIEW_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.PostRoomSwaggerDescription.POST_ROOM_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.PostRoomSwaggerDescription.POST_ROOM_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.PutGlampingImageSwaggerDescription.UPDATE_GLAMPING_IMAGE_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.PutGlampingImageSwaggerDescription.UPDATE_GLAMPING_IMAGE_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.PutGlampingSwaggerDescription.UPDATE_GLAMPING_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.PutGlampingSwaggerDescription.UPDATE_GLAMPING_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.PutRoomSwaggerDescription.PUT_ROOM_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.PutRoomSwaggerDescription.PUT_ROOM_RESPONSE_ERROR_CODE;
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

    //  사장님 페이지 - 글램핑 정보 등록하기  //
    @PostMapping(value = "glamping", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "글램핑 정보 등록 (김민지)", description = POST_GLAMPING_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = POST_GLAMPING_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostGlampingInfoResponseDto.class)))
    public ResponseEntity<? super PostGlampingInfoResponseDto> createGlamping(@RequestPart @Valid GlampingPostRequestDto req
            , @RequestPart MultipartFile glampImg) {
        return service.postGlampingInfo(req, glampImg);
    }

    //  사장님 페이지 - 글램핑 정보 수정하기  //
    @PutMapping("glamping")
    @Operation(summary = "글램핑 정보 수정 (김민지)", description = UPDATE_GLAMPING_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = UPDATE_GLAMPING_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PutGlampingInfoResponseDto.class)))
    public ResponseEntity<? super PutGlampingInfoResponseDto> updateGlamping(@RequestBody GlampingPutRequestDto req) {
        return service.updateGlampingInfo(req);
    }

    //  사장님 페이지 - 글램핑 대표 이미지 수정하기  //
    @PutMapping("glamping/image")
    @Operation(summary = "글램핑 대표 이미지 수정 (김민지)", description = UPDATE_GLAMPING_IMAGE_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = UPDATE_GLAMPING_IMAGE_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PutGlampingInfoResponseDto.class)))
    public ResponseEntity<? super PutGlampingInfoResponseDto> updateGlampingImage(@RequestPart MultipartFile image, @RequestPart long glampId) {
        return service.changeGlampingImage(image, glampId);
    }

    //  사장님 페이지 - 객실 정보 등록하기  //
    @PostMapping(value = "room", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "객실 정보 등록 (김민지)", description = POST_ROOM_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = POST_ROOM_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PutGlampingInfoResponseDto.class)))
    public ResponseEntity<? super PostRoomInfoResponseDto> createRoom(@RequestPart @Valid RoomPostRequestDto req
            , @RequestPart List<MultipartFile> roomImg) {
        return service.postRoomInfo(req, roomImg);
    }

    //  사장님 페이지 - 객실 정보 수정하기  //
    @PutMapping("room")
    @Operation(summary = "객실 정보 수정 (김민지)", description = PUT_ROOM_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = PUT_ROOM_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PutRoomInfoResponseDto.class)))
    public ResponseEntity<? super PutRoomInfoResponseDto> updateRoom(@RequestPart List<MultipartFile> addImg, @RequestPart RoomPutRequestDto req) {
        return service.updateRoomInfo(addImg, req);
    }

//    //  사장님 페이지 - 객실 삭제하기  //
//    @DeleteMapping("room")
//    @Operation(summary = "객실 삭제 (김민지)", description = DELETE_ROOM_IMAGE_DESCRIPTION)
//    @ApiResponse(responseCode = "200", description = DELETE_ROOM_IMAGE_RESPONSE_ERROR_CODE,
//            content = @Content(
//                    mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
//    public ResponseEntity<? super ResponseDto> updateRoomImageDelete(@PathVariable("img_id") Long imgId, @PathVariable("room_id") Long roomId) {
//        return service.deleteRoomImage(imgId, roomId);
//    }

    // 비밀번호 확인
    @PostMapping("password")
    @Operation(summary = " (김민지)", description =
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
                    schema = @Schema(implementation = ResponseDto.class)))
    public ResponseEntity<? super ResponseDto> checkOwnerPassword(CheckPasswordRequestDto dto) {
        return service.checkOwnerPassword(dto);
    }

    // put - 사장님 정보 수정
    @PutMapping("info")
    @Operation(summary = "사장님 정보 수정 (김민지)", description =
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
                    schema = @Schema(implementation = ResponseDto.class)))
    public ResponseEntity<? super ResponseDto> updateOwnerInfo() {
//        return service.deleteRoomImage(imgId, roomId);
        return null;
    }
  
// 강국 =================================================================================================================

    //  사장님 페이지 - 리뷰 답글 작성하기  //
    @PatchMapping("/review")
    @Operation(summary = "리뷰 답글 작성하기 (배강국)", description = POST_OWNER_REVIEW_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = POST_OWNER_REVIEW_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PatchOwnerReviewInfoResponseDto.class)))
    public ResponseEntity<? super PatchOwnerReviewInfoResponseDto> patchReview(@RequestBody @Valid ReviewPatchRequestDto p) {
        log.info("controller {}", p);
        GlampingModule.ownerId(authenticationFacade);
        GlampingModule.roleCheck(authenticationFacade.getLoginUser().getRole());
        return service.patchReview(p);
    }

    //  사장님 페이지 - 리뷰 불러오기  //
    @GetMapping("/review")
    @Operation(summary = "리뷰 불러오기 (배강국)", description = GLAMPING_FROM_USER_REVIEW_VIEW_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = GLAMPING_FROM_USER_REVIEW_VIEW_RESPONSE_ERROR_CODE,
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

    // 사장님 페이지 - 예약 내역 불러오기 //
    @GetMapping("/book")
    @Operation(summary = "글램핑에 예약된 내역 불러오기 (배강국)", description = BOOK_FROM_USER_REVIEW_VIEW_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = BOOK_FROM_USER_REVIEW_VIEW_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetOwnerBookListResponseDto.class)))
    public ResponseEntity<? super GetOwnerBookListResponseDto> getOwnerReservation(@ParameterObject @ModelAttribute ReservationGetRequestDto p) {
        return service.getOwnerReservation(p);
    }


}

