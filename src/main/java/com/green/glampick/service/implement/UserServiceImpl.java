package com.green.glampick.service.implement;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.UserReviewListItem;
import com.green.glampick.dto.request.user.*;
import com.green.glampick.dto.response.login.PostSignUpResponseDto;
import com.green.glampick.dto.response.owner.post.PostGlampingInfoResponseDto;
import com.green.glampick.dto.response.user.*;
import com.green.glampick.entity.*;
import com.green.glampick.repository.*;
import com.green.glampick.repository.resultset.*;
import com.green.glampick.security.AuthenticationFacade;
import com.green.glampick.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ReservationBeforeRepository reservationBeforeRepository;
    private final ReservationCancelRepository reservationCancelRepository;
    private final ReservationCompleteRepository reservationCompleteRepository;
    private final ReviewRepository reviewRepository;
    private final AuthenticationFacade authenticationFacade;
    private final PasswordEncoder passwordEncoder;
    private final FavoriteGlampingRepository favoriteGlampingRepository;
    private final CustomFileUtils customFileUtils;
    private final ReviewImageRepository reviewImageRepository;
    private final GlampingStarRepository glampingStarRepository;


    //  마이페이지 - 예약 내역 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetBookResponseDto> getBook(GetBookRequestDto dto) {

        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GetBookResponseDto.validateUserId();
        }


        List<GetReservationBeforeResultSet> reservationBeforeResultSetList;
        List<GetReservationCancelResultSet> reservationCancelResultSetList;
        List<GetReservationCompleteResultSet> reservationCompleteResultSetList;

        try {

            reservationBeforeResultSetList = reservationBeforeRepository.getBook(dto.getUserId());
            reservationCancelResultSetList = reservationCancelRepository.getBook(dto.getUserId());
            reservationCompleteResultSetList = reservationCompleteRepository.getBook(dto.getUserId());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetBookResponseDto.success(reservationBeforeResultSetList
                , reservationCompleteResultSetList
                , reservationCancelResultSetList);
    }

    //  마이페이지 - 예약 취소하기  //
    @Override
    @Transactional
    public ResponseEntity<? super CancelBookResponseDto> cancelBook(CancelBookRequestDto dto) {

        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CancelBookResponseDto.validateUserId();
        }


        Optional<ReservationBeforeEntity> optionalBeforeEntity = Optional.empty();

        try {

            optionalBeforeEntity = reservationBeforeRepository.findById(dto.getReservationId());

            // Entity 로 가져온 데이터가 없다면, 존재하지 않는 예약내역에 대한 응답을 반환한다.
            if (optionalBeforeEntity.isEmpty()) {
                return CancelBookResponseDto.noExistedBook();
            }

            ReservationBeforeEntity beforeEntity = optionalBeforeEntity.get();
            ReservationCancelEntity cancelEntity = new ReservationCancelEntity(
                    beforeEntity.getReservationId()
                    , beforeEntity.getUserId()
                    , beforeEntity.getBookId()
                    , beforeEntity.getGlampId()
                    , beforeEntity.getRoomId()
                    , beforeEntity.getInputName()
                    , beforeEntity.getPersonnel()
                    , beforeEntity.getCheckInDate()
                    , beforeEntity.getCheckOutDate()
                    , beforeEntity.getPg()
                    , beforeEntity.getPayAmount()
                    , dto.getComment()
                    , beforeEntity.getCreatedAt());

            reservationCancelRepository.save(cancelEntity);
            reservationBeforeRepository.delete(beforeEntity);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CancelBookResponseDto.success();
    }

    //  마이페이지 - 리뷰 작성하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostReviewResponseDto> postReview(PostReviewRequestDto dto, List<MultipartFile> mf) {

        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return PostReviewResponseDto.validateUserId();
        }

        if (dto.getReviewStarPoint() > 5) { return PostReviewResponseDto.validateStarPoint(); }


        ReviewEntity reviewEntity = new ReviewEntity(dto);

        try {
            ReservationCompleteEntity reservationCompleteEntity = reservationCompleteRepository.findByReservationId(dto.getReservationId());
            reviewEntity.setReservationId(dto.getReservationId());
            reviewEntity.setReviewContent(dto.getReviewContent());
            reviewEntity.setReviewStarPoint(dto.getReviewStarPoint());
            reviewEntity.setGlampId(reservationCompleteEntity.getGlampId());
            reviewEntity = reviewRepository.save(reviewEntity);
            glampingStarRepository.fin(dto.getReservationId());
            glampingStarRepository.findStarPointAvg(reservationCompleteEntity.getGlampId());
        } catch (Exception e) {
            e.printStackTrace();
            return PostReviewResponseDto.reservationIdError();
        }

        if (mf == null){
            return PostReviewResponseDto.success(reviewEntity.getReviewId());
        }
        PostReviewPicsRequestDto postReviewPicsRequestDto = PostReviewPicsRequestDto.builder().reviewId(reviewEntity.getReviewId()).build();

        String makefolder = String.format("review/%d/%d", dto.getUserId(), reviewEntity.getReviewId());
        customFileUtils.makeFolders(makefolder);

        try {
            List<ReviewImageEntity> reviewImageEntityList = new ArrayList<>();



            for (MultipartFile image : mf) {
                String saveFileName = customFileUtils.makeRandomFileName(image);
                String saveDbFileName = String.format("/pic/review/%d/%d/%s", dto.getUserId(), reviewEntity.getReviewId(),saveFileName);
                postReviewPicsRequestDto.getReviewPicsName().add(saveDbFileName);
                String filePath = String.format("%s/%s", makefolder, saveFileName);
                customFileUtils.transferTo(image, filePath);

                ReviewImageEntity reviewImageEntity = new ReviewImageEntity();
                reviewImageEntity.setReviewId(reviewEntity.getReviewId());
                reviewImageEntity.setReviewImageName(saveDbFileName);
                reviewImageEntityList.add(reviewImageEntity);
            }
            this.reviewImageRepository.saveAll(reviewImageEntityList);
            reviewEntity.setReviewStarPoint(dto.getReviewStarPoint());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PostReviewResponseDto.success(reviewEntity.getReviewId());

    }

    @Override // 리뷰 삭제
    @Transactional
    public ResponseEntity<? super DeleteReviewResponseDto> deleteReview(DeleteReviewRequestDto dto) {

        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DeleteReviewResponseDto.validateUserId();
        }

        ReviewEntity reviewEntity = new ReviewEntity();
        try {
            reviewRepository.findById(dto.getReviewId());
            if (dto.getReviewId() == 0) {
                return DeleteReviewResponseDto.noExistedReview();
            }
            List<ReviewImageEntity> list = reviewImageRepository.findByReviewId(dto.getReviewId());

            for (int i = 0; i < list.size(); i++ ){
                reviewImageRepository.deleteById(list.get(i).getReviewImageId());
            }
            reviewRepository.deleteById(dto.getReviewId());
            reviewRepository.findStarPointAvg();


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();

        }
        return DeleteReviewResponseDto.success();
    }

    @Override // 리뷰 불러오기
    @Transactional
    public ResponseEntity<? super GetReviewResponseDto> getReview(GetReviewRequestDto dto) {

        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GetReviewResponseDto.validateUserId();
        }

        List<GetUserReviewResultSet> resultSetList = null;
        List<ReviewImageEntity> imageEntities = new ArrayList<>();

        try {
            int limit = dto.getLimit();
            int offset = dto.getOffset();

            resultSetList = reviewRepository.getReview(dto.getUserId(), limit, offset);
            List<Long> reviewIds = resultSetList.stream()
                    .map(GetUserReviewResultSet::getReviewId)
                    .collect(Collectors.toList());

            imageEntities = reviewImageRepository.findByReviewIdIn(reviewIds);

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
                        .filter(entity -> entity.getReviewId() == resultSet.getReviewId())
                        .map(ReviewImageEntity::getReviewImageName) // 경로를 파일명으로 구성
                        .collect(Collectors.toList());
                reviewListItem.setReviewImages(imageUrls);

                reviewListItems.add(reviewListItem);
            }

            return GetReviewResponseDto.success(reviewRepository.getTotalReviewsCount(dto.getUserId()), reviewListItems);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

    }

    @Override //관심글램핑 불러오기
    @Transactional
    public ResponseEntity<? super GetFavoriteGlampingResponseDto> getFavoriteGlamping(GetFavoriteGlampingRequestDto dto) {

        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GetFavoriteGlampingResponseDto.validateUserId();
        }

        List<GetFavoriteGlampingResultSet> resultSets;

        try {
            resultSets = favoriteGlampingRepository.getFavoriteGlamping(dto.getUserId());
            if (resultSets == null) {
                return GetFavoriteGlampingResponseDto.noExistedGlamp();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetFavoriteGlampingResponseDto.success(resultSets);
    }

    //  마이페이지 - 내 정보 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetUserResponseDto> getUser(GetUserRequestDto dto) {

        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GetUserResponseDto.validateUserId();
        }

        try {

            UserEntity userEntity = userRepository.findById(dto.getUserId()).get();
            if (dto.getUserId() == 0) {
                return GetUserResponseDto.noExistedUser();
            }

            return GetUserResponseDto.success(userEntity);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

    }

    //  마이페이지 - 내 정보 수정하기  //
    @Override
    @Transactional
    public ResponseEntity<? super UpdateUserResponseDto> updateUser(UpdateUserRequestDto dto, MultipartFile mf) {

        // 유저 PK 불러오기
        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return UpdateUserResponseDto.validateUserId();
        }

        // 수정사항이 하나도 입력되지 않은 경우에는 에러
        if (dto.getUserId() == 0 &&
                (dto.getUserPw() == null || dto.getUserPw().isEmpty()) &&
                (dto.getUserPhone() == null || dto.getUserPhone().isEmpty()) &&
                (dto.getUserNickname() == null || dto.getUserNickname().isEmpty()) &&
                (mf == null || mf.isEmpty()))
        {
            return UpdateUserResponseDto.validationFailed();
        }



        try {
            UserEntity userEntity = userRepository.findById(dto.getUserId()).get();
            if (dto.getUserId() == 0) {
                return UpdateUserResponseDto.noExistedUser();
            }
            boolean existedNickname = userRepository.existsByUserNickname(dto.getUserNickname());
            if (existedNickname) { return PostSignUpResponseDto.duplicatedNickname(); }

            if (mf == null || mf.isEmpty()) { dto.setUserProfileImage(null); }
            else {
                String path = String.format("user/%d", userEntity.getUserId());
                customFileUtils.deleteFolder(path);
                customFileUtils.makeFolders(path);
                String saveFileName = customFileUtils.makeRandomFileName(mf);
                String filePath = String.format("%s/%s", path, saveFileName);
                customFileUtils.transferTo(mf, filePath);

                String dbFileName = String.format("/pic/user/%d/%s", userEntity.getUserId(), saveFileName);
                userEntity.setUserProfileImage(dbFileName);
            }

            if (dto.getUserPw() != null && !dto.getUserPw().isEmpty()) {
                String userPw = dto.getUserPw();
                String encodingPw = passwordEncoder.encode(userPw);
                dto.setUserPw(encodingPw);
                userEntity.setUserPw(dto.getUserPw());
            }

            if (dto.getUserNickname() != null && !dto.getUserNickname().isEmpty()) {
                userEntity.setUserNickname(dto.getUserNickname());
            }
            if (dto.getUserPhone() != null && !dto.getUserPhone().isEmpty()) {
                userEntity.setUserPhone(dto.getUserPhone());
            }

            userRepository.save(userEntity);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return UpdateUserResponseDto.success();
    }

    @Override// 회원 탈퇴
    @Transactional
    public ResponseEntity<? super DeleteUserResponseDto> deleteUser(DeleteUserRequestDto dto) {

        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DeleteUserResponseDto.validateUserId();
        }

        try {

            userRepository.findById(dto.getUserId());
            if (dto.getUserId() == 0) {
                return DeleteUserResponseDto.noExistedUser();
            }
            userRepository.deleteById(dto.getUserId());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return DeleteUserResponseDto.success();

    }

    @Override
    @Transactional
    public ResponseEntity<? super PostUserPasswordResponseDto> postUserPassword(PostUserPasswordRequestDto dto) {

        try {
            dto.setUserId(authenticationFacade.getLoginUserId());
            if (dto.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return PostUserPasswordResponseDto.validateUserId();
        }

        try {

            UserEntity userEntity = userRepository.findByUserId(dto.getUserId());
            if (dto.getUserId() == 0) {
                return PostUserPasswordResponseDto.noExistedUser();
            }

            String userPw = dto.getUserPw();
            String encodingPw = userEntity.getUserPw();
            boolean matches = passwordEncoder.matches(userPw, encodingPw);
            if (!matches) {
                return PostUserPasswordResponseDto.invalidPassword();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PostUserPasswordResponseDto.success();
    }
}



