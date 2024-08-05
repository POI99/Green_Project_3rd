package com.green.glampick.service;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.request.owner.*;
import com.green.glampick.dto.request.ReviewPatchRequestDto;
import com.green.glampick.dto.request.user.GetReviewRequestDto;
import com.green.glampick.dto.response.owner.*;
import com.green.glampick.dto.response.owner.get.GetOwnerBookListResponseDto;
import com.green.glampick.dto.response.owner.get.OwnerInfoResponseDto;
import com.green.glampick.dto.response.owner.post.PostBusinessPaperResponseDto;
import com.green.glampick.dto.response.owner.post.PostGlampingInfoResponseDto;
import com.green.glampick.dto.response.owner.post.PostRoomInfoResponseDto;
import com.green.glampick.dto.response.owner.put.PutGlampingInfoResponseDto;
import com.green.glampick.dto.response.owner.put.PutRoomInfoResponseDto;
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

    ResponseEntity<? super PostGlampingInfoResponseDto> postGlampingInfo(GlampingPostRequestDto glampingPostRequestDtoReq, MultipartFile glampImg);
    ResponseEntity<? super PostRoomInfoResponseDto> postRoomInfo(RoomPostRequestDto req, List<MultipartFile> img);
    ResponseEntity<? super PutGlampingInfoResponseDto> changeGlampingImage (MultipartFile image, long glampId);
    ResponseEntity<? super PutGlampingInfoResponseDto> updateGlampingInfo(GlampingPutRequestDto req);
    ResponseEntity<? super PutRoomInfoResponseDto> updateRoomInfo(List<MultipartFile> addImg, RoomPutRequestDto p);
    ResponseEntity<? super ResponseDto> deleteRoom(Long roomId);

    ResponseEntity<? super ResponseDto> checkOwnerPassword(CheckPasswordRequestDto dto);
    ResponseEntity<? super OwnerInfoResponseDto> getOwnerInfo();
    ResponseEntity<? super ResponseDto> patchOwnerInfo(PatchOwnerInfoRequestDto dto);

    ResponseEntity<? super GetOwnerBookListResponseDto> getOwnerReservation(@ParameterObject @ModelAttribute ReservationGetRequestDto p);
    ResponseEntity<? super PatchOwnerReviewInfoResponseDto> patchReview(ReviewPatchRequestDto p);

    List<GetReservationBeforeResultSet> getReservationBeforeList(ReservationGetRequestDto p);
    List<GetReservationCancelResultSet> getReservationCancelList(ReservationGetRequestDto p);
    List<GetReservationCompleteResultSet> getReservationCompleteList(ReservationGetRequestDto p);
    ResponseEntity<? super GetReviewResponseDto> getReview(@ParameterObject @ModelAttribute GetReviewRequestDto dto);
}
