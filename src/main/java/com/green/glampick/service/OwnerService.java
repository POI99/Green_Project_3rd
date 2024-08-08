package com.green.glampick.service;

import com.green.glampick.dto.object.owner.OwnerBookCountListItem;
import com.green.glampick.dto.object.owner.OwnerBookDetailListItem;
import com.green.glampick.dto.request.owner.*;
import com.green.glampick.dto.request.ReviewPatchRequestDto;
import com.green.glampick.dto.request.user.GetReviewRequestDto;
import com.green.glampick.dto.response.owner.*;
import com.green.glampick.dto.response.owner.get.*;
import com.green.glampick.dto.response.owner.post.PostBusinessPaperResponseDto;
import com.green.glampick.dto.response.owner.post.PostRoomInfoResponseDto;
import com.green.glampick.dto.response.owner.put.PatchOwnerInfoResponseDto;
import com.green.glampick.dto.response.user.GetReviewResponseDto;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OwnerService {

    ResponseEntity<? super PostBusinessPaperResponseDto> postBusinessInfo(MultipartFile file);

    // 글램핑 등록
    ResponseEntity<? super OwnerSuccessResponseDto> postGlampingInfo(GlampingPostRequestDto glampingPostRequestDtoReq, MultipartFile glampImg);
    // 객실 등록
//    ResponseEntity<? super OwnerSuccessResponseDto> postRoomInfo(RoomPostRequestDto req, List<MultipartFile> img);
    ResponseEntity<? super PostRoomInfoResponseDto> postRoomInfo(RoomPostRequestDto req, List<MultipartFile> img);
    // 글램핑 대표이미지 변경
    ResponseEntity<? super OwnerSuccessResponseDto> changeGlampingImage (MultipartFile image, long glampId);
    // 글램핑 정보 변경
    ResponseEntity<? super OwnerSuccessResponseDto> updateGlampingInfo(GlampingPutRequestDto req);
    // 객실 정보 변경
    ResponseEntity<? super OwnerSuccessResponseDto> updateRoomInfo(List<MultipartFile> addImg, RoomPutRequestDto p);
    // 객실 삭제
    ResponseEntity<? super OwnerSuccessResponseDto> deleteRoom(Long roomId);
    // 글램핑 get
    ResponseEntity<? super GetGlampingInfoResponseDto> getGlamping();
    // 객실 get
    ResponseEntity<? super GetRoomListResponseDto> getRoomList(Long glampId);
    ResponseEntity<? super GetRoomInfoResponseDto> getRoomOne(Long glampId, Long roomId);
    // 비밀번호 확인
    ResponseEntity<? super OwnerSuccessResponseDto> checkOwnerPassword(CheckPasswordRequestDto dto);
    // 회원정보 불러오기
    ResponseEntity<? super OwnerInfoResponseDto> getOwnerInfo();
    // 회원 정보 수정
    ResponseEntity<? super PatchOwnerInfoResponseDto> patchOwnerInfo(PatchOwnerInfoRequestDto dto);
    // 탈퇴 승인 요청
    ResponseEntity<? super OwnerSuccessResponseDto> withdrawOwner(Long glampId);

    ResponseEntity<? super GetOwnerBookListResponseDto> getOwnerReservation(ReservationGetRequestDto p);
    ResponseEntity<? super PatchOwnerReviewInfoResponseDto> patchReview(ReviewPatchRequestDto p);

    List<OwnerBookDetailListItem> getReservationBeforeList(ReservationGetRequestDto p);
    List<OwnerBookDetailListItem> getReservationCancelList(ReservationGetRequestDto p);
    List<OwnerBookDetailListItem> getReservationCompleteList(ReservationGetRequestDto p);

    List<OwnerBookCountListItem> getTotalCount(String date,Long ownerId);
    ResponseEntity<? super GetReviewResponseDto> getReview(GetReviewRequestDto dto);
}
