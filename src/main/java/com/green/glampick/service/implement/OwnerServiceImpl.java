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
import com.green.glampick.dto.request.owner.module.GlampingValidate;
import com.green.glampick.dto.request.owner.module.RoomValidate;
import com.green.glampick.dto.request.user.GetReviewRequestDto;
import com.green.glampick.dto.response.owner.*;
import com.green.glampick.dto.response.owner.get.GetOwnerBookListResponseDto;
import com.green.glampick.dto.response.owner.post.PostGlampingInfoResponseDto;
import com.green.glampick.dto.response.owner.post.PostRoomInfoResponseDto;
import com.green.glampick.dto.response.owner.put.PutGlampingInfoResponseDto;
import com.green.glampick.dto.response.owner.put.PutRoomInfoResponseDto;
import com.green.glampick.dto.response.user.GetReviewResponseDto;
import com.green.glampick.entity.ReviewEntity;
import com.green.glampick.entity.ReviewImageEntity;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.OwnerErrorCode;
import com.green.glampick.mapper.OwnerMapper;
import com.green.glampick.repository.ReviewRepository;
import com.green.glampick.repository.resultset.GetUserReviewResultSet;
import com.green.glampick.security.AuthenticationFacade;
import com.green.glampick.service.OwnerService;
import jakarta.persistence.EntityManager;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static com.green.glampick.common.GlobalConst.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final OwnerMapper mapper;
    private final AuthenticationFacade authenticationFacade;
    private final CustomFileUtils customFileUtils;
    private final ReviewRepository reviewRepository;
// 민지 =================================================================================================================

    @Transactional
    public ResponseEntity<? super PostGlampingInfoResponseDto> postGlampingInfo(GlampingPostRequestDto req
            , MultipartFile glampImg) {

        try {
            req.setUserId(userValidationGlamping());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }

        // GlampingValidate
        try {
            // 글램핑을 이미 가지고 있는가? (hasGlamping = 유저가 가진 글램핑장 PK)
            Long hasGlamping = mapper.hasGlamping(req.getUserId());
            GlampingValidate.hasGlamping(hasGlamping);
            // 이미지가 들어있는가?
            GlampingValidate.imgExist(glampImg);
            // 글램핑 위치가 중복되는가? (existingLocation = 위치가 동일한 글램핑장 PK)
            Long existingLocation = mapper.existingLocation(req.getGlampLocation());
            GlampingValidate.existingLocation(existingLocation);
            // 필요한 데이터가 모두 입력되었는가?
            GlampingValidate.isNull(req);
        } catch (Exception e) {
            String msg = e.getMessage();
            return PostGlampingInfoResponseDto.validationFailed(msg);
        }

        // 이미지 파일명 만들기
        String glmapImgName = customFileUtils.makeRandomFileName(glampImg);
        req.setGlampingImg(glmapImgName);
        // glamping insert 실행
        try {
            mapper.insertGlamping(req);
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
        long glampId = req.getGlampId();

        // 이미지 url로 저장하기
        String picNameUrl = String.format("/pic/glamping/%d/glamp/%s", glampId, glmapImgName);
        mapper.updateGlampingImageNameToUrl(picNameUrl, glampId);

        // 글램핑 대표 이미지 넣기
        try {
            // 폴더 : /glamping/{glampId}
            String glampPath = String.format("glamping/%s/glamp", glampId);
            customFileUtils.makeFolders(glampPath);
            // 파일을 저장한다
            String target = String.format("/%s/%s", glampPath, glmapImgName);
            customFileUtils.transferTo(glampImg, target);
        } catch (Exception e) {
            e.printStackTrace();
            return PostGlampingInfoResponseDto.fileUploadError();
        }

        return PostGlampingInfoResponseDto.success(glampId);
    }

    @Transactional
    public ResponseEntity<? super PostRoomInfoResponseDto> postRoomInfo(RoomPostRequestDto req
            , List<MultipartFile> image) {
        try {
            userValidationRoom(req.getGlampId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }

        // RoomValidate
        try {
            RoomValidate.imgExist(image);   // 이미지가 들어있는가?
            RoomValidate.isNull(req);    // 필요한 데이터가 모두 입력되었는가?
            RoomValidate.personnel(req.getPeopleNum(), req.getPeopleMax());  // 인원 정보가 올바른가?
            RoomValidate.timeValidator(req.getInTime());   // 시간 형식이 올바른가?
            RoomValidate.timeValidator(req.getOutTime());
        } catch (Exception e) {
            String msg = e.getMessage();
            return PostRoomInfoResponseDto.validationFailed(msg);
        }

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
    public ResponseEntity<? super PutGlampingInfoResponseDto> updateGlampingInfo(GlampingPutRequestDto p) {
        GlampingPostRequestDto req = p.getRequestDto();

        try {
            req.setUserId(userValidationGlamping());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }

        // GlampingValidate
        try {
            // 글램핑 Id가 올바른가?
            GlampingValidate.isNull(p.getGlampId());
            req.setGlampId(p.getGlampId());
            // 필요한 데이터가 모두 입력되었는가?
            GlampingValidate.isNull(req);
            // 글램핑 위치가 중복되는가? (existingLocation = 위치가 동일한 글램핑장 PK)
            Long existingLocation = mapper.existingLocation(req.getGlampLocation());
            GlampingValidate.locationUpdate(existingLocation, req.getGlampId());
        } catch (Exception e) {
            String msg = e.getMessage();
            return PutGlampingInfoResponseDto.validationFailed(msg);
        }
        mapper.updateGlampingInfo(req);
        return PutGlampingInfoResponseDto.success();
    }

    @Transactional
    public ResponseEntity<? super PutRoomInfoResponseDto> updateRoomInfo(RoomPutRequestDto p) {
        RoomPostRequestDto req = p.getRequestDto();

        try {
            userValidationRoom(req.getGlampId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }

        // RoomValidate
        try {
            GlampingValidate.isNull(p.getRoomId()); // 룸 Id가 올바른가?
            req.setRoomId(p.getRoomId());
            RoomValidate.isNull(req);    // 필요한 데이터가 모두 입력되었는가?
            RoomValidate.personnel(req.getPeopleNum(), req.getPeopleMax());  // 인원 정보가 올바른가?
            RoomValidate.timeValidator(req.getInTime());   // 시간 형식이 올바른가?
            RoomValidate.timeValidator(req.getOutTime());
        } catch (Exception e) {
            String msg = e.getMessage();
            return PutRoomInfoResponseDto.validationFailed(msg);
        }
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

        try {
            userValidationGlamping();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }

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

    private long userValidationGlamping() {
        long userId = 0;
        try {
            userId = authenticationFacade.getLoginUserId();
            if (userId <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return userId;
    }

    private void userValidationRoom(long glampId) {
        long loginUserId = userValidationGlamping();
        Long getUserId = mapper.getUserIdByGlampId(glampId);
        if (getUserId == null || loginUserId != getUserId || loginUserId <= 0) {
            throw new RuntimeException();
        }
    }


// 강국 =================================================================================================================
    @Override
    @Transactional
    public ResponseEntity<? super PostOwnerReviewInfoResponseDto> postReview(ReviewPostRequestDto p) {

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

        return PostOwnerReviewInfoResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<? super PatchOwnerReviewInfoResponseDto> patchReview(ReviewPatchRequestDto p) {
        return null;
    }

    @Override
    public ResponseEntity<?super GetReviewResponseDto> getReview(@ParameterObject @ModelAttribute GetReviewRequestDto dto) {

        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }

        List<GetUserReviewResultSet> resultSetList = null;  //리뷰 목록
        List<ReviewImageEntity> imageEntities = new ArrayList<>(); // 이미지 목록

        try {
            int limit = dto.getLimit();
            int offset = dto.getOffset();

            resultSetList = reviewRepository.getReview(dto.getUserId(), limit, offset);

            List<Long> reviewIds = resultSetList.stream()
                    .map(GetUserReviewResultSet::getReviewId)
                    .collect(Collectors.toList());

//            imageEntities = reviewImageRepository.findByReviewIdIn(reviewIds);

            List<UserReviewListItem> reviewListItems = new ArrayList<>();

            for (GetUserReviewResultSet resultSet : resultSetList) {
                UserReviewListItem reviewListItem = new UserReviewListItem();
                reviewListItem.setGlampName(resultSet.getGlampName());
                reviewListItem.setRoomName(resultSet.getRoomName());
                reviewListItem.setUserNickName(resultSet.getUserNickname());
                reviewListItem.setUserProfileImage(resultSet.getUserProfileImage());
                reviewListItem.setReviewId(resultSet.getReviewId());
                reviewListItem.setReservationId(resultSet.getReservationId());
                reviewListItem.setUserReviewContent(resultSet.getReviewContent());
                reviewListItem.setStarPoint(resultSet.getReviewStarPoint());
                reviewListItem.setOwnerReviewContent(resultSet.getOwnerReviewComment());
                reviewListItem.setCreatedAt(resultSet.getCreatedAt().toString());
                reviewListItem.setBookId(resultSet.getBookId());
                reviewListItem.setGlampId(resultSet.getGlampId());

                List<String> imageUrls = imageEntities.stream()
                        .filter(entity -> entity.getReviewId().getReviewId() == resultSet.getReviewId())
                        .map(ReviewImageEntity::getReviewImageName) // 경로를 파일명으로 구성
                        .collect(Collectors.toList());
                reviewListItem.setReviewImages(imageUrls);

                reviewListItems.add(reviewListItem);
            }

            return GetReviewResponseDto.success(reviewRepository.getTotalReviewsCount(dto.getUserId()), reviewListItems);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

}
