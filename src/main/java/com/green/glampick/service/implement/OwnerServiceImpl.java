package com.green.glampick.service.implement;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.dto.object.UserReviewListItem;
import com.green.glampick.dto.object.owner.*;
import com.green.glampick.dto.request.owner.*;
import com.green.glampick.dto.request.ReviewPatchRequestDto;
import com.green.glampick.dto.response.owner.patch.PatchOwnerPeakResponseDto;
import com.green.glampick.dto.response.owner.patch.PatchOwnerReviewInfoResponseDto;
import com.green.glampick.module.GlampingModule;
import com.green.glampick.module.RoomModule;
import com.green.glampick.dto.request.user.GetReviewRequestDto;
import com.green.glampick.dto.response.owner.*;
import com.green.glampick.dto.response.owner.get.*;
import com.green.glampick.dto.response.owner.post.PostBusinessPaperResponseDto;
import com.green.glampick.dto.response.owner.post.PostRoomInfoResponseDto;
import com.green.glampick.dto.response.owner.put.PatchOwnerInfoResponseDto;
import com.green.glampick.dto.response.user.GetReviewResponseDto;
import com.green.glampick.entity.*;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.OwnerErrorCode;
import com.green.glampick.exception.errorCode.UserErrorCode;

import com.green.glampick.repository.*;
import com.green.glampick.repository.resultset.*;
import com.green.glampick.security.AuthenticationFacade;
import com.green.glampick.service.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final AuthenticationFacade authenticationFacade;
    private final CustomFileUtils customFileUtils;
    private final PasswordEncoder passwordEncoder;
    private final GlampingWaitRepository glampingWaitRepository;
    private final GlampingRepository glampingRepository;
    private final OwnerRepository ownerRepository;
    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;
    private final ServiceRepository serviceRepository;
    private final RoomServiceRepository roomServiceRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReservationBeforeRepository reservationBeforeRepository;
    private final ReservationCancelRepository reservationCancelRepository;
    private final ReservationCompleteRepository reservationCompleteRepository;
    private final RoomPriceRepository roomPriceRepository;

// 수찬 =================================================================================================================

    //  사장님 페이지 - 사업자 등록증 첨부하기  //
    @Override
    public ResponseEntity<? super PostBusinessPaperResponseDto> postBusinessInfo(MultipartFile file) {

        Long ownerId = GlampingModule.ownerId(authenticationFacade);

        try {

            String makeFolder = String.format("businessInfo/%d", ownerId);
            customFileUtils.makeFolders(makeFolder);
            String saveFileName = customFileUtils.makeRandomFileName(file);
            String saveDbFileName = String.format("/pic/businessInfo/%d/%s", ownerId, saveFileName);
            String filePath = String.format("%s/%s", makeFolder, saveFileName);
            customFileUtils.transferTo(file, filePath);

            OwnerEntity ownerEntity = ownerRepository.findByOwnerId(ownerId);
            ownerEntity.setBusinessPaperImage(saveDbFileName);
            ownerRepository.save(ownerEntity);


        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
        return PostBusinessPaperResponseDto.success();

    }

// 민지 =================================================================================================================

    // 글램핑 등록
    @Transactional
    public ResponseEntity<? super OwnerSuccessResponseDto> postGlampingInfo(GlampingPostRequestDto req
            , MultipartFile glampImg) {
        GlampingWaitEntity entity = new GlampingWaitEntity();
        // 오너 PK 불러오기
        long ownerId = GlampingModule.ownerId(authenticationFacade);
        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());
        entity.setOwner(owner);

        // 사장님이 글램핑을 이미 가지고 있는가?
        GlampingModule.hasGlamping(glampingWaitRepository, glampingRepository, owner);
        // 이미지가 들어있는가?
        GlampingModule.imgExist(glampImg);
        // 글램핑 위치가 중복되는가?
        GlampingModule.existingLocation(glampingWaitRepository, glampingRepository, req.getGlampLocation());

        // 글램핑 아이디 받아오기
        entity.setGlampName(req.getGlampName());
        if(req.getGlampCall() != null && !req.getGlampCall().isEmpty()){
            entity.setGlampCall(GlampingModule.glampingCall(req.getGlampCall()));
        }
        entity.setExclusionStatus(0);
        entity.setGlampImage("img");
        entity.setGlampLocation(req.getGlampLocation());
        entity.setRegion(req.getRegion());
        entity.setExtraCharge(0);
        if(req.getExtraCharge() != null && req.getExtraCharge() > 0){
            entity.setExtraCharge(req.getExtraCharge());
        }
        entity.setGlampIntro(req.getIntro());
        entity.setInfoBasic(req.getBasic());
        entity.setInfoNotice(req.getNotice());
        entity.setTraffic(req.getTraffic());
        glampingWaitRepository.save(entity);
        long glampId = entity.getGlampId();

        // 이미지 저장하기
        String fileName = GlampingModule.imageUpload(customFileUtils, glampImg, glampId, "glampingWait");
        glampingWaitRepository.updateGlampImageByGlampId(fileName, glampId);

        return OwnerSuccessResponseDto.postInformation();
    }

    // 글램핑 수정
    @Transactional
    public ResponseEntity<? super OwnerSuccessResponseDto> updateGlampingInfo(GlampingPutRequestDto p) {

        long ownerId = GlampingModule.ownerId(authenticationFacade);

        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());

        // 로그인 유저와 글램핑 PK가 매치되는가?
        GlampingModule.isGlampIdOk(glampingRepository, owner, p.getGlampId());

        GlampingPostRequestDto dto = p.getRequestDto();

        // 전화번호 형식 맞추기
        if (dto.getGlampCall() != null && !dto.getGlampCall().isEmpty()) {
            dto.setGlampCall(GlampingModule.glampingCall(dto.getGlampCall()));
        }

        // 위치정보 중복되는지 확인하기
        if (dto.getGlampLocation() != null && !dto.getGlampLocation().isEmpty()) {
            GlampingModule.locationUpdate(dto.getGlampLocation(), glampingWaitRepository, glampingRepository, p.getGlampId());
        }

        // 입력되지 않은 데이터에는 기존 값 넣어주기
        GlampingEntity entity = glampingRepository.findByGlampId(p.getGlampId());
        dto = GlampingModule.dtoNull(dto, entity);

        glampingRepository.updateGlampingInformation(dto.getGlampName(), dto.getGlampCall()
                , dto.getGlampLocation(), dto.getRegion(), dto.getExtraCharge()
                , dto.getIntro(), dto.getBasic(), dto.getNotice(), dto.getTraffic(), p.getGlampId());


        return OwnerSuccessResponseDto.updateInformation();
    }

    // 글램핑 사진 수정
    @Transactional
    public ResponseEntity<? super OwnerSuccessResponseDto> changeGlampingImage(MultipartFile image, long glampId) {

        long ownerId = GlampingModule.ownerId(authenticationFacade);

        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());

        // 로그인 유저와 글램핑 PK가 매치되는가?
        GlampingModule.isGlampIdOk(glampingRepository, owner, glampId);

        if (image == null || image.isEmpty()) {
            throw new CustomException(OwnerErrorCode.NF);
        }

        String folderPath = String.format("glampingWait/%d/glamp", glampId);
        customFileUtils.deleteFolder(folderPath);
        String fileName = GlampingModule.imageUpload(customFileUtils, image, glampId, "glamping");
        glampingRepository.updateGlampImageByGlampId(fileName, glampId);

        return OwnerSuccessResponseDto.updateInformation();
    }

    // 객실 등록
    @Transactional
    public ResponseEntity<? super PostRoomInfoResponseDto> postRoomInfo(RoomPostRequestDto req
            , List<MultipartFile> image) {

        // 오너 PK 불러오기
        long ownerId = GlampingModule.ownerId(authenticationFacade);
        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());
        // 로그인 유저와 글램핑 PK가 매치되는가?
        GlampingModule.isGlampIdOk(glampingRepository, owner, req.getGlampId());

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
        if (req.getService() != null) {
            List<RoomServiceEntity> service = RoomModule.saveService(req.getService(), roomRepository.findByRoomId(room.getRoomId()), serviceRepository);
            roomServiceRepository.saveAll(service);
        }

//        return OwnerSuccessResponseDto.postInformation();
        return PostRoomInfoResponseDto.success(room.getRoomId());
    }

    // 객실 수정
    @Transactional
    public ResponseEntity<? super OwnerSuccessResponseDto> updateRoomInfo(List<MultipartFile> addImg, RoomPutRequestDto p) {
        RoomPostRequestDto dto = p.getRequestDto();

        long ownerId = GlampingModule.ownerId(authenticationFacade);
        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());

        // 로그인 유저와 룸 Id가 매치되는가?
        RoomModule.isRoomIdOk(roomRepository, glampingRepository, owner, p.getRoomId());

        // 입력된 인원 정보가 올바른지 확인
        RoomModule.personnelUpdate(dto.getPeopleNum(), dto.getPeopleMax());

        // 시간이 올바른지 확인
        if (dto.getInTime() != null && !dto.getInTime().isEmpty()) {
            RoomModule.isValidTime(dto.getInTime());
        }
        if (dto.getOutTime() != null && !dto.getOutTime().isEmpty()) {
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

        // 삭제되는 사진이 있다면 삭제
        if (p.getRemoveImg() != null && !p.getRemoveImg().isEmpty()) {
            for (Long img : p.getRemoveImg()) {
                // 해당 객실의 사진이 맞는지 확인
                RoomImageEntity imageEntity = roomImageRepository.getReferenceById(img);
                RoomModule.checkImgId(room, imageEntity);
                // 삭제
                RoomModule.deleteImageOne(img, roomImageRepository, customFileUtils);
            }
        }

        // 추가되는 사진이 있다면 추가
        if (addImg != null && !addImg.isEmpty() && !addImg.get(0).isEmpty()) {
            List<String> roomImgName = RoomModule.imgInsert(addImg, dto.getGlampId(), room.getRoomId(), customFileUtils);
            List<RoomImageEntity> saveImage = RoomModule.saveImage(roomImgName, roomRepository.findByRoomId(room.getRoomId()));
            roomImageRepository.saveAll(saveImage);
        }

        return OwnerSuccessResponseDto.updateInformation();
    }

    // 객실 삭제
    public ResponseEntity<? super OwnerSuccessResponseDto> deleteRoom(Long roomId) {
        // PK 불러오기
        long ownerId = GlampingModule.ownerId(authenticationFacade);

        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());
        // 사장님이 해당 객실을 가지고있는지 확인
        RoomModule.isRoomIdOk(roomRepository, glampingRepository, owner, roomId);

        // 삭제
        RoomEntity entity = roomRepository.getReferenceById(roomId);
        roomRepository.delete(entity);

        return OwnerSuccessResponseDto.deleteInformation();
    }

    // 글램핑 정보 불러오기
    public ResponseEntity<? super GetGlampingInfoResponseDto> getGlamping() {
        /*
            state : owner table 에 glamping status 가 1이면 true 0이면 false
                    true - glamping table 에서 get
                    false -	glamping wait table 에서 get
         */

        // 사장님 PK 불러오기
        long ownerId = GlampingModule.ownerId(authenticationFacade);
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());

        GetGlampingInfoResultSet resultSet = null;
        if(owner.getGlampingStatus() == 0) {
            resultSet = glampingWaitRepository.getGlampingInfo(owner);
            return GetGlampingInfoResponseDto.successWait(false, resultSet);
        }
        resultSet = glampingRepository.getGlampingInfo(owner);
        return GetGlampingInfoResponseDto.success(true, resultSet);
    }

    // 객실 정보 미리보기
    public ResponseEntity<? super GetRoomListResponseDto> getRoomList(Long glampId) {
        // PK 불러오기
        long ownerId = GlampingModule.ownerId(authenticationFacade);

        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());
        // 사장님이 해당 글램핑을 가지고있는지 확인
        GlampingModule.isGlampIdOk(glampingRepository, owner, glampId);

        GlampingEntity glamping = glampingRepository.getReferenceById(glampId);
        List<GetRoomListResultSet> resultSet = roomRepository.getRoomList(glamping);
        List<GetRoomItem> result = new ArrayList<>();
        for (GetRoomListResultSet item : resultSet) {
            GetRoomItem room = new GetRoomItem(item.getRoomId(), item.getRoomName(), item.getRoomImageName());
            result.add(room);
        }

        return GetRoomListResponseDto.success(result);
    }

    // 객실 정보 상세보기
    public ResponseEntity<? super GetRoomInfoResponseDto> getRoomOne(Long glampId, Long roomId) {
        // PK 불러오기
        long ownerId = GlampingModule.ownerId(authenticationFacade);

        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());
        // 사장님이 해당 글램핑을 가지고있는지 확인
        GlampingModule.isGlampIdOk(glampingRepository, owner, glampId);

        // 정보 불러오기
        GetRoomInfoResultSet resultSet = roomRepository.getRoomInfo(roomId);
        RoomEntity room = roomRepository.getReferenceById(roomId);
        List<String> roomImage = roomImageRepository.getRoomImg(room);
        List<Long> service = serviceRepository.findRoomServiceIdByRoom(room);

        return GetRoomInfoResponseDto.success(resultSet, roomImage, service);
    }

    // 비밀번호 확인
    public ResponseEntity<? super OwnerSuccessResponseDto> checkOwnerPassword(CheckPasswordRequestDto dto) {

        long ownerId = GlampingModule.ownerId(authenticationFacade);
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);

        if (!passwordEncoder.matches(dto.getPassword(), owner.getOwnerPw())) {
            throw new CustomException(UserErrorCode.NMP);
        }
        return OwnerSuccessResponseDto.passwordTrue();
    }

    // 정보 불러오기
    public ResponseEntity<? super OwnerInfoResponseDto> getOwnerInfo() {
        long ownerId = GlampingModule.ownerId(authenticationFacade);
        OwnerInfoResultSet result = ownerRepository.getOwnerInfo(ownerId);
        return OwnerInfoResponseDto.success(result);
    }

    // 사장님 정보 수정
    public ResponseEntity<? super PatchOwnerInfoResponseDto> patchOwnerInfo(PatchOwnerInfoRequestDto dto) {

        long ownerId = GlampingModule.ownerId(authenticationFacade);
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        if(dto.getOwnerPw() == null || dto.getOwnerPw().isEmpty()){
            if(dto.getPhoneNum() == null || dto.getPhoneNum().isEmpty()){
                // 변경된 내용이 없음
                return PatchOwnerInfoResponseDto.noUpdate();
            }
            // 폰번호만 바뀜
            owner.setOwnerPhone(GlampingModule.glampingCall(dto.getPhoneNum()));
            ownerRepository.save(owner);
            return PatchOwnerInfoResponseDto.success();
        }
        if(dto.getPhoneNum() != null && !dto.getPhoneNum().isEmpty()){
            owner.setOwnerPhone(GlampingModule.glampingCall(dto.getPhoneNum()));
        }
        owner.setOwnerPw(passwordEncoder.encode(dto.getOwnerPw()));
        ownerRepository.save(owner);
        return PatchOwnerInfoResponseDto.success();
    }

    // 사장님 탈퇴 승인 요청
    public ResponseEntity<? super OwnerSuccessResponseDto> withdrawOwner(Long glampId) {

        long ownerId = GlampingModule.ownerId(authenticationFacade);
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.isGlampIdOk(glampingRepository, owner, glampId);

        // 예약 된 글램핑이 있는지 확인
        GlampingEntity glamping = glampingRepository.getReferenceById(glampId);
        GlampingModule.existReservation(glamping, reservationBeforeRepository);

        // 탈퇴 요청
        owner.setActivateStatus(0);
        ownerRepository.save(owner);
        glamping.setActivateStatus(0);
        glampingRepository.save(glamping);

        return OwnerSuccessResponseDto.withdraw();
    }


    // 강국 =================================================================================================================
    @Override
    @Transactional // 사장님 답글달기
    public ResponseEntity<? super PatchOwnerReviewInfoResponseDto> patchReview(ReviewPatchRequestDto p) {

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

    @Override // 예약 중 리스트 불러오기
    public List<OwnerBookDetailListItem> getReservationBeforeList(ReservationGetRequestDto p) {

        try {
            log.info("p: {}", p);

            List<OwnerBookItem> reservationBeforeResultSetList;
            Long ownerId = p.getOwnerId();
            int limit = p.getLimit(); // size
            int offset = p.getOffset(); // startIdx
            String date = p.getDate(); // Date

            Pageable pageable = PageRequest.of(offset,limit);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);

            List<OwnerBookDetailListItem> bookDetailListItems = new ArrayList<>();

            reservationBeforeResultSetList = reservationBeforeRepository.getReservationBeforeByOwnerId(ownerId, pageable, localDate);
            setBookDetailList(reservationBeforeResultSetList, bookDetailListItems);
            return bookDetailListItems;
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    @Override // 예약취소 리스트 불러오기
    public List<OwnerBookDetailListItem> getReservationCancelList(ReservationGetRequestDto p) {
        try {
            List<OwnerBookItem> reservationCancelResultSetList;
            Long ownerId = p.getOwnerId();
            int limit = p.getLimit();
            int offset = p.getOffset();
            String date = p.getDate();

            Pageable pageable = PageRequest.of(offset,limit);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);

            List<OwnerBookDetailListItem> bookDetailListItems = new ArrayList<>();


            reservationCancelResultSetList = reservationCancelRepository.getReservationCancelByOwnerId(ownerId, pageable, localDate);
            setBookDetailList(reservationCancelResultSetList,bookDetailListItems);

            return bookDetailListItems;

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    @Override // 예약완료 리스트 불러오기
    public List<OwnerBookDetailListItem> getReservationCompleteList(ReservationGetRequestDto p) {
        try {
            List<OwnerBookItem> reservationCompleteResultSetList;
            Long ownerId = p.getOwnerId();
            int limit = p.getLimit();
            int offset = p.getOffset();
            String date = p.getDate();

            Pageable pageable = PageRequest.of(offset,limit);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);

            List<OwnerBookDetailListItem> bookDetailListItems = new ArrayList<>();

            reservationCompleteResultSetList = reservationCompleteRepository.getReservationCompleteByOwnerId(ownerId, pageable, localDate);
            setBookDetailList(reservationCompleteResultSetList, bookDetailListItems);

            return bookDetailListItems;

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    @Override //리뷰 리스트 불러오기
    public ResponseEntity<? super GetReviewResponseDto> getReview(GetReviewRequestDto p) {

        //리뷰 데이터 추출
        try {

            //setting
            Long ownerId = p.getOwnerId();
            int limit = p.getLimit();
            int offset = p.getOffset();
            long typeNum = p.getTypeNum();
            List<GetUserReviewResultSet> reviewInfo = new ArrayList<>();
            Long totalCount = 0L;

            if (typeNum == 0) {
                reviewInfo = reviewRepository.getReviewForOwner(ownerId, limit, offset);
                totalCount = reviewRepository.getTotalOwnersReviewsCount(ownerId);

            } else if (typeNum == 1) {
                reviewInfo = reviewRepository.getReviewForOwnerExcludeComment(ownerId, limit, offset);
                totalCount = reviewRepository.getTotalOwnersNoReviewsCount(ownerId);
            }

            //review PK 세팅
            List<ReviewEntity> reviewEntityList = getReviewEntities(reviewInfo);

            //image Entity 추출
            List<ReviewImageEntity> imageEntities = reviewImageRepository.findByReviewEntityIn(reviewEntityList);

            //dto 생성
            List<UserReviewListItem> reviewListItem = new ArrayList<>();

            //reviewItem List Setting
            setReviewItem(reviewInfo, imageEntities, reviewListItem);

            //reviewTotalCount
            return GetReviewResponseDto.success(reviewListItem,totalCount);

        } catch (CustomException e) {
            e.printStackTrace();
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    @Override // 날짜별 예약건수
    public List<OwnerBookCountListItem> getTotalCount(String date, Long ownerId) {
        LocalDate localDate = parseToLocalDate(date);
        int month = localDate.getMonth().getValue();
        try {
            List<GetOwnerBookBeforeCountResponseDto> countBefore = reservationBeforeRepository.getCountFromReservationBefore(month,ownerId);
            List<GetOwnerBookCancelCountResponseDto> countCancel = reservationCancelRepository.getCountFromReservationCancel(month,ownerId);
            List<GetOwnerBookCompleteCountResponseDto> countComplete = reservationCompleteRepository.getCountFromReservationComplete(month,ownerId);

            List<OwnerBookCountListItem> bookCountListItems = new ArrayList<>();


            for (GetOwnerBookBeforeCountResponseDto bookCount :countBefore) {
                OwnerBookCountListItem item = new OwnerBookCountListItem();
                item.setCheckInDate(bookCount.getCheckInDate());
                item.setIngCount(bookCount.getCountBefore());
                bookCountListItems.add(item);
            }
            List<OwnerBookCountListItem> plusBookCountListItems = new LinkedList<>(bookCountListItems);

            for (GetOwnerBookCancelCountResponseDto bookCount : countCancel) {
//                for (OwnerBookCountListItem ownerItem : bookCountListItems) {

                for (Integer i = 0 ; i < bookCountListItems.size() ;i++ ) {

                    String checkInDate = bookCountListItems.get(i).getCheckInDate();
                    String cancelDate = bookCount.getCheckInDate().toString();
                    Long cancelCount = bookCount.getCountCancel();
                    if (checkInDate.equals(cancelDate)) {
                        plusBookCountListItems.get(i).setCancelCount(cancelCount); //
                        break;
                    }
                    if (i == bookCountListItems.size()-1)
                    {
                        OwnerBookCountListItem newItem = new OwnerBookCountListItem();
                        newItem.setCheckInDate(bookCount.getCheckInDate());
                        newItem.setCancelCount(cancelCount);
                        plusBookCountListItems.add(newItem);
                    }
                }
            }
            List<OwnerBookCountListItem> plusBookCountListItems2 = new LinkedList<>(plusBookCountListItems);

            for (GetOwnerBookCompleteCountResponseDto bookCount : countComplete) {
//                for (OwnerBookCountListItem ownerItem : bookCountListItems) {

                for (Integer i = 0 ; i < plusBookCountListItems.size() ;i++ ) {

                    String checkInDate = plusBookCountListItems.get(i).getCheckInDate();
                    String completeDate = bookCount.getCheckInDate().toString();
                    Long completeCount = bookCount.getCountComplete();
                    if (checkInDate.equals(completeDate)) {
                        plusBookCountListItems2.get(i).setCompleteCount(completeCount); //
                        break;
                    }
                    if (i == plusBookCountListItems.size()-1)
                    {
                        OwnerBookCountListItem newItem = new OwnerBookCountListItem();
                        newItem.setCheckInDate(bookCount.getCheckInDate());
                        newItem.setCompleteCount(completeCount);
                        plusBookCountListItems2.add(newItem);
                    }
                }
            }


            return plusBookCountListItems2;

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public ResponseEntity<? super PatchOwnerPeakResponseDto> patchPeak(Long glampId, PatchOwnerPeakRequestDto p) {
        log.info("service: {}", p);
        //가격정보 찾아오기
        List<OwnerRoomPriceItem> priceResultSet = roomPriceRepository.getRoomPriceList(glampId);
        List<GetRoomPriceItem> priceItems = new ArrayList<>();

        setPriceList(priceResultSet, priceItems);

        double percent = p.getPeakCost() * 0.01;


        for (GetRoomPriceItem item : priceItems) {
            Integer weekdayPrice = item.getWeekdayPrice();
            Integer weekendPrice = item.getWeekendPrice();

            weekdayPrice += (int)(weekdayPrice * percent);
            weekendPrice += (int)(weekendPrice * percent);

            RoomPriceEntity entity = roomPriceRepository.findRoomPriceByRoomId(item.getRoomId());
            entity.setWeekdayPrice(weekdayPrice);
            entity.setWeekendPrice(weekendPrice);
            roomPriceRepository.save(entity);
        }



        return null;
    }

    //========================================= < PRIVATE METHOD > =========================================================//

    private static void setPriceList(List<OwnerRoomPriceItem> priceResultSet, List<GetRoomPriceItem> priceItems) {
        for (OwnerRoomPriceItem resultSet : priceResultSet) {
            GetRoomPriceItem item = new GetRoomPriceItem();

            item.setRoomId(resultSet.getRoomId());
            item.setWeekdayPrice(resultSet.getWeekdayPrice());
            item.setWeekendPrice(resultSet.getWeekendPrice());

            priceItems.add(item);
        }
    }
    private static void setBookDetailList(List<OwnerBookItem> reservationResultSetList, List<OwnerBookDetailListItem> bookDetailListItems) {
        for (OwnerBookItem resultSet : reservationResultSetList) {
            OwnerBookDetailListItem item = new OwnerBookDetailListItem();
            item.setInputName(resultSet.getInputName());
            item.setRoomName(resultSet.getRoomName());
            item.setPersonnel(resultSet.getPersonnel());
            item.setPayAmount(resultSet.getPayAmount());
            item.setCheckInDate(resultSet.getCheckInDate().toString());
            item.setCheckOutDate(resultSet.getCheckOutDate().toString());

            bookDetailListItems.add(item);
        }
    }
    private static void setReviewItem(List<GetUserReviewResultSet> reviewInfo, List<ReviewImageEntity> imageEntities, List<UserReviewListItem> reviewListItem) {
        for (GetUserReviewResultSet resultSet : reviewInfo) {
            UserReviewListItem item = new UserReviewListItem();
            item.setGlampName(resultSet.getGlampName());
            item.setRoomName(resultSet.getRoomName());
            item.setUserNickName(resultSet.getUserNickname());
            item.setUserProfileImage(resultSet.getUserProfileImage());
            item.setReviewId(resultSet.getReviewId());
            item.setReservationId(resultSet.getReservationId());
            item.setUserReviewContent(resultSet.getReviewContent());
            item.setStarPoint(resultSet.getReviewStarPoint());
            item.setOwnerReviewContent(resultSet.getOwnerReviewComment());
            item.setCreatedAt(resultSet.getCreatedAt().toString());
            item.setGlampId(resultSet.getGlampId());

            List<String> imageUrls = imageEntities.stream()
                    .filter(entity -> Objects.equals(entity.getReviewEntity().getReviewId(), resultSet.getReviewId()))
                    .map(ReviewImageEntity::getReviewImageName) // 경로를 파일명으로 구성
                    .collect(Collectors.toList());

            item.setReviewImages(imageUrls);

            reviewListItem.add(item);

        }
    }

    @NotNull
    private static List<ReviewEntity> getReviewEntities(List<GetUserReviewResultSet> reviewInfo) {
        List<ReviewEntity> reviewEntityList = reviewInfo.stream().map(item -> { // 1)리스트 스트림변환, 2)reviewId 값 들을 세팅해서 ReviewEntity 객체로 추출 3)추출한 값을 List 로 반환
            ReviewEntity entity = new ReviewEntity();
            entity.setReviewId(item.getReviewId());
            return entity;
        }).toList();
        return reviewEntityList;
    }
    private LocalDate parseToLocalDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate result = LocalDate.parse(date, dateTimeFormatter);
        return result;
    }


}
