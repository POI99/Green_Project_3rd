package com.green.glampick.service.implement;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.dto.object.UserReviewListItem;
import com.green.glampick.dto.object.owner.BookBeforeItem;
import com.green.glampick.dto.object.owner.BookCancelItem;
import com.green.glampick.dto.object.owner.BookCompleteItem;
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
import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.GlampingWaitEntity;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.entity.ReviewEntity;
import com.green.glampick.entity.ReviewImageEntity;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.OwnerErrorCode;
import com.green.glampick.mapper.OwnerMapper;
import com.green.glampick.repository.*;
import com.green.glampick.repository.resultset.GetUserReviewResultSet;
import com.green.glampick.security.AuthenticationFacade;
import com.green.glampick.service.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final OwnerMapper mapper;
    private final AuthenticationFacade authenticationFacade;
    private final CustomFileUtils customFileUtils;
    private final GlampingWaitRepository waitRepository;
    private final GlampingRepository glampingRepository;
    private final OwnerRepository ownerRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;

// 민지 =================================================================================================================

    // 글램핑 등록
    @Transactional
    public ResponseEntity<? super PostGlampingInfoResponseDto> postGlampingInfo(GlampingPostRequestDto req
            , MultipartFile glampImg) {
        GlampingWaitEntity entity = new GlampingWaitEntity();
        // 오너 PK 불러오기
        long ownerId = GlampingModule.ownerId(authenticationFacade);
        entity.setOwner(ownerRepository.getReferenceById(ownerId));

        // 사장님이 글램핑을 이미 가지고 있는가?
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.hasGlamping(waitRepository, glampingRepository, owner);
        // 이미지가 들어있는가?
        GlampingModule.imgExist(glampImg);
        // 글램핑 위치가 중복되는가?
        GlampingModule.existingLocation(waitRepository, glampingRepository, req.getGlampLocation());

        // 글램핑 아이디 받아오기
        entity.setGlampName(req.getGlampName());
        entity.setGlampCall(GlampingModule.glampingCall(req.getGlampCall()));
        entity.setGlampImage("img");
        entity.setGlampLocation(req.getGlampLocation());
        entity.setRegion(req.getRegion());
        entity.setExtraCharge(req.getExtraCharge());
        entity.setGlampIntro(req.getIntro());
        entity.setInfoBasic(req.getBasic());
        entity.setInfoNotice(req.getNotice());
        entity.setTraffic(req.getTraffic());
        waitRepository.save(entity);
        long glampId = entity.getGlampId();

        // 이미지 저장하기
        String fileName = GlampingModule.imageUpload(customFileUtils, glampImg, glampId, "glampingWait");
        waitRepository.updateGlampImageByGlampId(fileName, glampId);

        return PostGlampingInfoResponseDto.success(glampId);
    }

    // 글램핑 수정
    @Transactional
    public ResponseEntity<? super PutGlampingInfoResponseDto> updateGlampingInfo(GlampingPutRequestDto p) {

        long ownerId = GlampingModule.ownerId(authenticationFacade);

        // 로그인 유저와 글램핑 PK가 매치되는가?
        GlampingModule.isGlampIdOk(glampingRepository, ownerRepository, p.getGlampId(), ownerId);

        GlampingPostRequestDto dto = p.getRequestDto();

        // 전화번호 형식 맞추기
        if (dto.getGlampCall() != null && !dto.getGlampCall().isEmpty()) {
            dto.setGlampCall(GlampingModule.glampingCall(dto.getGlampCall()));
        }

        // 위치정보 중복되는지 확인하기
        if (dto.getGlampLocation() != null && !dto.getGlampLocation().isEmpty()) {
            GlampingModule.locationUpdate(dto.getGlampLocation(), waitRepository, glampingRepository, p.getGlampId());
        }

        GlampingEntity entity = glampingRepository.findByGlampId(p.getGlampId());
        dto = GlampingModule.dtoNull(dto, entity);

        glampingRepository.updateGlampingInformation(dto.getGlampName(), dto.getGlampCall()
                , dto.getGlampLocation(), dto.getRegion(), dto.getExtraCharge()
                , dto.getIntro(), dto.getBasic(), dto.getNotice(), dto.getTraffic(), p.getGlampId());


        return PutGlampingInfoResponseDto.success();
    }

    // 글램핑 사진 수정
    @Transactional
    public ResponseEntity<? super PutGlampingInfoResponseDto> changeGlampingImage(MultipartFile image, long glampId) {
        if(image == null || image.isEmpty()) {
            throw new CustomException(OwnerErrorCode.NF);
        }
        GlampingModule.isGlampIdOk(glampingRepository, ownerRepository, glampId, GlampingModule.ownerId(authenticationFacade));
        String folderPath = String.format("glampingWait/%d/glamp", glampId);
        customFileUtils.deleteFolder(folderPath);
        String fileName = GlampingModule.imageUpload(customFileUtils, image, glampId, "glamping");
        glampingRepository.updateGlampImageByGlampId(fileName, glampId);
        return PutGlampingInfoResponseDto.success();
    }

    // 객실 등록
    @Transactional
    public ResponseEntity<? super PostRoomInfoResponseDto> postRoomInfo(RoomPostRequestDto req
            , List<MultipartFile> image) {
//        try {
//            userValidationRoom(req.getGlampId());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new CustomException(CommonErrorCode.MNF);
//        }

//        // RoomValidate
//        try {
//            RoomValidate.imgExist(image);   // 이미지가 들어있는가?
//            RoomValidate.isNull(req);    // 필요한 데이터가 모두 입력되었는가?
//            RoomValidate.personnel(req.getPeopleNum(), req.getPeopleMax());  // 인원 정보가 올바른가?
//            RoomValidate.timeValidator(req.getInTime());   // 시간 형식이 올바른가?
//            RoomValidate.timeValidator(req.getOutTime());
//        } catch (Exception e) {
//            String msg = e.getMessage();
//            return PostRoomInfoResponseDto.validationFailed(msg);
//        }

        req.setGlampId(req.getGlampId());
        mapper.insertRoom(req);  // room 테이블 insert

        // 폴더 만들기
        String roomPath = String.format("glamping/%s/room/%s", req.getGlampId(), req.getRoomId());
        customFileUtils.makeFolders(roomPath);
        // room 파일명 생성 및 저장
        try {
            List<String> roomImg = new ArrayList<>();
            for (MultipartFile file : image) {
                String imgName = customFileUtils.makeRandomFileName(file);
                String imgUrlName = String.format("/pic/%s/%s", roomPath, imgName);
                roomImg.add(imgUrlName);
                String target = String.format("%s/%s", roomPath, imgName);
                customFileUtils.transferTo(file, target);
            }
            req.setRoomImgName(roomImg);
        } catch (Exception e) {
            e.printStackTrace();
            return PostRoomInfoResponseDto.fileUploadError();
        }
        // 룸 이미지 / 룸 서비스 insert
        try {
            mapper.insertRoomImg(req);
            if (req.getService() != null) {
                mapper.insertRoomService(req);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
        return PostRoomInfoResponseDto.success(req.getRoomId());
    }


    @Transactional
    public ResponseEntity<? super PutRoomInfoResponseDto> updateRoomInfo(RoomPutRequestDto p) {
        RoomPostRequestDto req = p.getRequestDto();

//        try {
//            userValidationRoom(req.getGlampId());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new CustomException(CommonErrorCode.MNF);
//        }

//        // RoomValidate
//        try {
//            GlampingModule.isNull(p.getRoomId()); // 룸 Id가 올바른가?
//            req.setRoomId(p.getRoomId());
//            RoomValidate.isNull(req);    // 필요한 데이터가 모두 입력되었는가?
//            RoomValidate.personnel(req.getPeopleNum(), req.getPeopleMax());  // 인원 정보가 올바른가?
//            RoomValidate.timeValidator(req.getInTime());   // 시간 형식이 올바른가?
//            RoomValidate.timeValidator(req.getOutTime());
//        } catch (Exception e) {
//            String msg = e.getMessage();
//            return PutRoomInfoResponseDto.validationFailed(msg);
//        }
        // 정보 업데이트
        mapper.updateRoomInfo(req);
        // 서비스 업데이트
        List<Integer> service = mapper.selService(req.getRoomId());
        if (service != req.getService()) {
            if (service != null) {
                mapper.delService(req.getRoomId());
            }
            if (req.getService() != null) {
                mapper.insertRoomService(req.getRoomId(), req.getService());
            }
        }

        return PutRoomInfoResponseDto.success();
    }

    @Transactional
    public ResponseEntity<? super GetOwnerBookListResponseDto> getGlampReservation(Long glampId) {

//        try {
//            userValidationGlamping();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new CustomException(CommonErrorCode.MNF);
//        }

        if (glampId == null || glampId < 0) {
            throw new CustomException(OwnerErrorCode.WG);
        }

        List<BookBeforeItem> before;
        List<BookCompleteItem> complete;
        List<BookCancelItem> cancel;
        try {
            before = mapper.getBookBefore(glampId);
            complete = mapper.getBookComplete(glampId);
            cancel = mapper.getBookCancel(glampId);
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return GetOwnerBookListResponseDto.success(before, complete, cancel);
    }


//    private void userValidationRoom(long glampId) {
//        long loginUserId = userValidationGlamping();
//        Long getUserId = mapper.getUserIdByGlampId(glampId);
//        if (getUserId == null || loginUserId != getUserId || loginUserId <= 0) {
//            throw new RuntimeException();
//        }
//    }


    // 강국 =================================================================================================================
    @Override
    @Transactional
    public ResponseEntity<? super PatchOwnerReviewInfoResponseDto> patchReview(ReviewPatchRequestDto p) {

        try {   // 로그인
            p.setOwnerId(authenticationFacade.getLoginUserId());
            if (p.getOwnerId() == 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);// 유저를 찾을 수 없음
        }

        try {
            ReviewEntity review = reviewRepository.findReviewById(p.getReviewId());
            review.setReviewComment(p.getReviewOwnerContent());
            reviewRepository.save(review);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PatchOwnerReviewInfoResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetReviewResponseDto> getReview(@ParameterObject @ModelAttribute GetReviewRequestDto p) {

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


}
