package com.green.glampick.service;

import com.green.glampick.dto.object.owner.OwnerBookCountListItem;
import com.green.glampick.dto.request.owner.*;
import com.green.glampick.dto.request.ReviewPatchRequestDto;
import com.green.glampick.dto.request.user.GetReviewRequestDto;
import com.green.glampick.dto.response.owner.*;
import com.green.glampick.dto.response.owner.get.GetOwnerBookListResponseDto;
import com.green.glampick.dto.response.owner.get.OwnerInfoResponseDto;
import com.green.glampick.dto.response.owner.post.PostBusinessPaperResponseDto;
import com.green.glampick.dto.response.owner.post.PostRoomInfoResponseDto;
import com.green.glampick.dto.response.owner.put.PatchOwnerInfoResponseDto;
import com.green.glampick.dto.response.user.GetReviewResponseDto;
import com.green.glampick.repository.resultset.GetReservationBeforeResultSet;
import com.green.glampick.repository.resultset.GetReservationCancelResultSet;
import com.green.glampick.repository.resultset.GetReservationCompleteResultSet;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OwnerService {

    ResponseEntity<? super PostBusinessPaperResponseDto> postBusinessInfo(MultipartFile file);

    ResponseEntity<? super OwnerSuccessResponseDto> postGlampingInfo(GlampingPostRequestDto glampingPostRequestDtoReq, MultipartFile glampImg);
//    ResponseEntity<? super OwnerSuccessResponseDto> postRoomInfo(RoomPostRequestDto req, List<MultipartFile> img);
    ResponseEntity<? super PostRoomInfoResponseDto> postRoomInfo(RoomPostRequestDto req, List<MultipartFile> img);
    ResponseEntity<? super OwnerSuccessResponseDto> changeGlampingImage (MultipartFile image, long glampId);
    ResponseEntity<? super OwnerSuccessResponseDto> updateGlampingInfo(GlampingPutRequestDto req);
    ResponseEntity<? super OwnerSuccessResponseDto> updateRoomInfo(List<MultipartFile> addImg, RoomPutRequestDto p);
    ResponseEntity<? super OwnerSuccessResponseDto> deleteRoom(Long roomId);

    ResponseEntity<? super OwnerSuccessResponseDto> checkOwnerPassword(CheckPasswordRequestDto dto);
    ResponseEntity<? super OwnerInfoResponseDto> getOwnerInfo();
    ResponseEntity<? super PatchOwnerInfoResponseDto> patchOwnerInfo(PatchOwnerInfoRequestDto dto);
    ResponseEntity<? super OwnerSuccessResponseDto> withdrawOwner(Long glampId);

    ResponseEntity<? super GetOwnerBookListResponseDto> getOwnerReservation(@ParameterObject @ModelAttribute ReservationGetRequestDto p);
    ResponseEntity<? super PatchOwnerReviewInfoResponseDto> patchReview(ReviewPatchRequestDto p);

    List<GetReservationBeforeResultSet> getReservationBeforeList(ReservationGetRequestDto p);
    List<GetReservationCancelResultSet> getReservationCancelList(ReservationGetRequestDto p);
    List<GetReservationCompleteResultSet> getReservationCompleteList(ReservationGetRequestDto p);

    List<OwnerBookCountListItem> getTotalCount(String date,Long ownerId);
    ResponseEntity<? super GetReviewResponseDto> getReview(@ParameterObject @ModelAttribute GetReviewRequestDto dto);
}
