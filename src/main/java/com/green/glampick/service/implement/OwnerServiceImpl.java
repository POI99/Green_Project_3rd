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
import com.green.glampick.dto.request.owner.module.RoomModule;
import com.green.glampick.dto.request.user.GetReviewRequestDto;
import com.green.glampick.dto.response.owner.*;
import com.green.glampick.dto.response.owner.get.GetOwnerBookListResponseDto;
import com.green.glampick.dto.response.owner.post.PostGlampingInfoResponseDto;
import com.green.glampick.dto.response.owner.post.PostRoomInfoResponseDto;
import com.green.glampick.dto.response.owner.put.PutGlampingInfoResponseDto;
import com.green.glampick.dto.response.owner.put.PutRoomInfoResponseDto;
import com.green.glampick.dto.response.user.GetReviewResponseDto;
import com.green.glampick.entity.*;
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
import java.util.HashSet;
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
    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;
    private final ServiceRepository serviceRepository;
    private final RoomServiceRepository roomServiceRepository;
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

        // 입력되지 않은 데이터에는 기존 값 넣어주기
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

        // 오너 PK 불러오기
        long ownerId = GlampingModule.ownerId(authenticationFacade);
        // 로그인 유저와 글램핑 PK가 매치되는가?
        GlampingModule.isGlampIdOk(glampingRepository, ownerRepository, req.getGlampId(), ownerId);

        // 사진이 들어있나?
        RoomModule.imgExist(image);

        // 인원 정보가 올바른가?
        RoomModule.personnel(req.getPeopleNum(), req.getPeopleMax());

        // room 테이블 insert
        GlampingEntity glamping = glampingRepository.getReferenceById(req.getGlampId());
        RoomEntity room = new RoomEntity(null, glamping, req.getRoomName(), req.getPrice()
                , req.getPeopleNum(), req.getPeopleMax(), req.getInTime(), req.getOutTime());
        roomRepository.save(room);

        // 이미지 저장
        List<String> roomImgName = RoomModule.imgInsert(image, req.getGlampId(), room.getRoomId(), customFileUtils);
        List<RoomImageEntity> saveImage = RoomModule.saveImage(roomImgName, roomRepository.findByRoomId(room.getRoomId()));
        roomImageRepository.saveAll(saveImage);

        // 서비스 저장
        if(req.getService() != null){
            List<RoomServiceEntity> service = RoomModule.saveService(req.getService(), roomRepository.findByRoomId(room.getRoomId()), serviceRepository);
            roomServiceRepository.saveAll(service);
        }

        return PostRoomInfoResponseDto.success(room.getRoomId());
    }

    // 객실 수정
    @Transactional
    public ResponseEntity<? super PutRoomInfoResponseDto> updateRoomInfo(RoomPutRequestDto p) {
        RoomPostRequestDto dto = p.getRequestDto();

        long ownerId = GlampingModule.ownerId(authenticationFacade);

        // 로그인 유저와 룸 Id가 매치되는가?
        RoomModule.isRoomIdOk(roomRepository, glampingRepository, ownerRepository, p.getRoomId(), ownerId);

        // 입력된 인원 정보가 올바른지 확인
        RoomModule.personnelUpdate(dto.getPeopleNum(), dto.getPeopleMax());

        // 시간이 올바른지 확인
        if(dto.getInTime() != null && !dto.getInTime().isEmpty()){
            RoomModule.isValidTime(dto.getInTime());
        }
        if(dto.getOutTime() != null && !dto.getOutTime().isEmpty()){
            RoomModule.isValidTime(dto.getOutTime());
        }


        // null 인 경우 기존값 넣어주기
        RoomEntity room = roomRepository.getReferenceById(p.getRoomId());
        dto = RoomModule.dtoNull(dto, room);

        RoomEntity roomUpdate = new RoomEntity(p.getRoomId()
                , glampingRepository.getReferenceById(dto.getGlampId())
                , dto.getRoomName(), dto.getPrice(), dto.getPeopleNum()
                , dto.getPeopleMax(), dto.getInTime(), dto.getOutTime());
        roomRepository.save(roomUpdate);

        // 서비스 수정
        List<Long> roomService = serviceRepository.findRoomServiceIdByRoom(room);
        List<Long> inputService = dto.getService();
        RoomModule.updateService(roomService, inputService, room, roomServiceRepository, serviceRepository);

        return PutRoomInfoResponseDto.success();
    }

    @Transactional
    public ResponseEntity<? super GetOwnerBookListResponseDto> getGlampReservation(Long glampId) {


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
