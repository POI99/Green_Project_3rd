package com.green.glampick.service.implement;

import com.green.glampick.dto.object.ReviewListItem;
import com.green.glampick.dto.object.glamping.*;
import com.green.glampick.dto.request.glamping.*;
import com.green.glampick.dto.response.glamping.*;
import com.green.glampick.dto.response.glamping.favorite.GetFavoriteGlampingResponseDto;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.GlampingErrorCode;
import com.green.glampick.mapper.GlampingMapper;
import com.green.glampick.security.AuthenticationFacade;
import com.green.glampick.service.GlampingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GlampingServiceImpl implements GlampingService {
    private final GlampingMapper mapper;
    private final AuthenticationFacade facade;

    // 민지 =================================================================================================================
    @Override
    @Transactional
    public ResponseEntity<? super GetSearchGlampingListResponseDto> searchGlamping(GlampingSearchRequestDto req) {

        // 날짜가 올바르게 들어가있나?
        if (checkDate(req.getInDate(), req.getOutDate())) {
            throw new CustomException(GlampingErrorCode.WD);
        }

        // 필터가 있는지
        if(req.getFilter() != null && !req.getFilter().isEmpty()){
            req.setFilterSize(1);
        }

        // searchGlamping1 : 필터, 날짜, 인원수
        List<Long> roomList = mapper.searchGlamping1(req);
        if(roomList == null || roomList.isEmpty()){
            // 검색결과 없음
            return GetSearchGlampingListResponseDto.isNull();
        }

        req.setRoomList(roomList);
        List<GlampingListItem> result = mapper.searchGlamping2(req);
        if(result == null || result.isEmpty()){
            // 검색결과 없음
            return GetSearchGlampingListResponseDto.isNull();
        }

        int searchCount = mapper.searchCount(req);

        return GetSearchGlampingListResponseDto.success(searchCount, result);
    }

    // 강국 =================================================================================================================
    @Override
    @Transactional
    public ResponseEntity<? super GetFavoriteGlampingResponseDto> favoriteGlamping(GetFavoriteRequestDto p) {

        try {
            p.setUserId(facade.getLoginUserId());
            if (p.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }

        try {
            //관심글램픽
            int result = mapper.deleteFavorite(p);
            if (result == 0) {
                // 관심등록
                mapper.insertFavorite(p);
                return GetFavoriteGlampingResponseDto.success(result);
            } else {
                return GetFavoriteGlampingResponseDto.success(result);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    @Override
    public ResponseEntity<? super GetGlampingInformationResponseDto> infoGlampingDetail(GetInfoRequestDto p) {

        //글램핑 정보,객실 정보 리스트, 리뷰 리스트, 리뷰 유저수
        GetGlampingInformationResponseDto glampInfoDto = mapper.selGlampingInfo(p);
        List<GlampingRoomListItem> rooms = mapper.selRoomInfo(p);
        List<GlampingDetailReviewItem> reviews = mapper.selReviewInfoInGlamping(p.getGlampId());

        // 로그인 중 이면 관심 등록 데이터 판단
        if (facade.getLoginUserId() != 0) {
            log.info("p: {}", p);
            log.info("p: {} ", facade.getLoginUserId());
            int isFavData = getIsFavData(p, facade.getLoginUserId());
            glampInfoDto.setIsFav(isFavData);
        }
        //리뷰 한 유저 수
        int userCount = mapper.selCount(p.getGlampId());
        boolean isReservationAvailable = true;


        //중복데이터 방지를 위한 HashSet
        HashSet<String> hashServices = new HashSet<>();
        //  서비스 & 예약가능여부 데이터 세팅
        for (GlampingRoomListItem item : rooms) {
            item.setReservationAvailable(isReservationAvailable);
            item.setRoomServices(mapper.selRoomService(item.getRoomId()));

            p.setRoomId(item.getRoomId());
            //객실 별 
            List<GlampingDateItem> dateItems = mapper.selDate(p);
            for (GlampingDateItem dateItem : dateItems) {
                //검색된 날짜와 예약상태의 객실들 데이터
                HashMap<String, LocalDate> dateHashMap = parseDate(p.getInDate(), p.getOutDate(), dateItem.getCheckInDate(), dateItem.getCheckOutDate());
                boolean k = checkOverlap(dateHashMap);

                if (k) {
                    item.setReservationAvailable(false);
                    break;
                }

            }

            List<String> roomServices = item.getRoomServices();
            //데이터 중복 방지
            for (String s : roomServices) {
                if (!roomServices.isEmpty()) {
                    hashServices.add(s);
                }
            }

        }
        Collections.sort(rooms);

        // dto 데이터 세팅
        glampInfoDto.setRoomService(hashServices);
        glampInfoDto.setCountReviewUsers(userCount);
        glampInfoDto.setReviewItems(reviews);
        glampInfoDto.setRoomItems(rooms);

        return new ResponseEntity<>(glampInfoDto, HttpStatus.OK);
    }

    /*
    @Override
    public ResponseEntity<? super GetMoreRoomItemResponseDto> moreDetailsRoom(GetInfoRequestDto p) {
        // 객실 정보 리스트
        List<GlampingRoomListItem> rooms = mapper.selRoomInfo(p);
        boolean isReservationAvailable = true;

        // 추가 객실 정보 리스트
        rooms.subList(0, GlobalConst.PAGING_SIZE).clear();
        HashSet<String> hashServices = new HashSet<>();

        //객실 서비스 세팅
        for (GlampingRoomListItem item : rooms) {
            item.setReservationAvailable(isReservationAvailable);
            item.setRoomServices(mapper.selRoomService(item.getRoomId()));

            p.setRoomId(item.getRoomId());

            List<GlampingDateItem> dateItems = mapper.selDate(p);

            for (GlampingDateItem dateItem : dateItems) {
                HashMap<String, LocalDate> dateHashMap = parseDate(p.getInDate(), p.getOutDate(), dateItem.getCheckInDate(), dateItem.getCheckOutDate());
                boolean k = checkOverlap(dateHashMap) ;

                if (k) {
                    item.setReservationAvailable(false);
                    break;
                }
            }
        }

        GetMoreRoomItemResponseDto glampInfoDto = GetMoreRoomItemResponseDto.builder().roomItems(rooms).build();

        return new ResponseEntity<>(glampInfoDto,HttpStatus.OK);
    }
     */
    @Override
    public ResponseEntity<? super GetGlampingReviewInfoResponseDto> infoReviewList(ReviewInfoRequestDto p) {

        // Data Get
        List<ReviewListItem> reviews = mapper.selReviewInfo(p);
        List<GlampingRoomNameAndImage> roomNameDto = mapper.selRoomNames(p.getGlampId());
        List<String> reviewImage = mapper.thumbnailReviewImage(p);

        //리뷰사진 가져오기
        for (int i = 0; i < reviews.size(); i++) {
            List<String> inputImageList = mapper.selReviewImage(reviews.get(i).getReviewId());
            reviews.get(i).setReviewImages(inputImageList);
        }
        List<String> roomNameList = new ArrayList<>();
        for (int i = 0; i < roomNameDto.size(); i++) {
            String roomName = roomNameDto.get(i).getRoomName();
            roomNameList.add(roomName);
        }

        //input ResponseDto
        GetGlampingReviewInfoResponseDto dto = GetGlampingReviewInfoResponseDto.builder()
                .reviewListItems(reviews).roomNames(roomNameList).allReviewImage(reviewImage).build();

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<? super GetMoreReviewImgageResponseDto> moreReviewImage(GetMoreReviewImageRequestDto p) {
        //페이징 리뷰 이미지
        List<String> images = mapper.allReviewImages(p);

        GetMoreReviewImgageResponseDto dto = GetMoreReviewImgageResponseDto.builder().moreReviewImage(images).build();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    public ResponseEntity<? super GetMoreRoomImageResponseDto> moreRoomImage(@ParameterObject @ModelAttribute GetMoreRoomImageRequestDto p) {

        //dto 에 넣을 HashMap
        HashMap<String, List<String>> hashMapImages = new HashMap<>();

        List<GlampingRoomNameAndImage> roomNameAndImages = mapper.selRoomNameAndImages(p);

        for (GlampingRoomNameAndImage item : roomNameAndImages) {
            String roomName = item.getRoomName();
            String roomImage = item.getRoomImages();

            if (!hashMapImages.containsKey(roomName)) {
                hashMapImages.put(roomName, new ArrayList<>());
            }
            hashMapImages.get(roomName).add(roomImage);
        }

        for (String roomName : hashMapImages.keySet()) {
            System.out.println("Room Name: " + roomName);
            List<String> imageNames = hashMapImages.get(roomName);
            for (String imageName : imageNames) {
                System.out.println("  Image: " + imageName);
            }
        }

        GetMoreRoomImageResponseDto dto = GetMoreRoomImageResponseDto.builder().moreRoomImages(hashMapImages).build();

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // =========================================================================================================
    private void isNull(String text) {
        if (text == null || text.isEmpty()) {
            throw new RuntimeException();
        }
    }

    private boolean checkDate(String in, String out) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate inDate = LocalDate.parse(in, formatter);
        LocalDate outDate = LocalDate.parse(out, formatter);
        return outDate.isBefore(inDate); // 틀리면 true
    }

    private HashMap<String, LocalDate> parseDate(String in, String out, String dbCheckIn, String dbCheckOut) {
        HashMap<String, LocalDate> date = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //Request check-in check-out
        LocalDate inDate = LocalDate.parse(in, formatter);
        LocalDate outDate = LocalDate.parse(out, formatter);
        date.put("inDate", inDate);
        date.put("outDate", outDate);

        //비교 할 check-in check-out
        LocalDate inDate2 = LocalDate.parse(dbCheckIn, formatter);
        LocalDate outDate2 = LocalDate.parse(dbCheckOut, formatter);
        date.put("inDate2", inDate2);
        date.put("outDate2", outDate2);

        return date;
    }

    private boolean checkOverlap(HashMap<String, LocalDate> dateHashMap) {
        //get Req in out date
        LocalDate start1 = dateHashMap.get("inDate");
        LocalDate end1 = dateHashMap.get("outDate");
        System.out.println("start1: " + start1);
        System.out.println("end1: " + end1);
        //get DB in out date
        LocalDate start2 = dateHashMap.get("inDate2");
        LocalDate end2 = dateHashMap.get("outDate2");
        System.out.println("start2: " + start2);
        System.out.println("end2: " + end2);
        System.out.println(!start1.isAfter(end2) && !start2.isAfter(end1));
        return !start1.isAfter(end2) && !start2.isAfter(end1); // 날짜가 겹치면 true
    }

    private int getIsFavData(GetInfoRequestDto dto, long loginUserId) {
        if (loginUserId != 0) {
            dto.setUserId(loginUserId);
            if (mapper.getIsFavData(dto) == null) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }
}



