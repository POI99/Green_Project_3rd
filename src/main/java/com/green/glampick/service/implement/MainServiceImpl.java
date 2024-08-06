package com.green.glampick.service.implement;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.main.MountainViewGlampingItem;
import com.green.glampick.dto.object.main.PetFriendlyGlampingItem;
import com.green.glampick.dto.object.main.PopularGlampingItem;
import com.green.glampick.dto.response.admin.GetBannerResponseDto;
import com.green.glampick.dto.response.glamping.GetSearchGlampingListResponseDto;
import com.green.glampick.dto.response.main.GetMainGlampingListResponseDto;
import com.green.glampick.entity.BannerEntity;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.mapper.MainMapper;
import com.green.glampick.repository.BannerRepository;
import com.green.glampick.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final BannerRepository bannerRepository;
    private final MainMapper mapper;

    //  메인 페이지 - 글램핑 리스트 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetMainGlampingListResponseDto> mainGlampingList() {

        try {

            //  메인페이지에 불러올 인기순, 반려동물 동반, 마운틴뷰에 대한 글램핑장 리스트를 불러온다.  //
            List<PopularGlampingItem> popular = mapper.popular();
            List<PetFriendlyGlampingItem> petFriendly = mapper.petFriendly();
            List<MountainViewGlampingItem> mountainView = mapper.mountainView();

            return GetMainGlampingListResponseDto.success(popular, petFriendly, mountainView);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    //  메인 페이지 - 메인 화면 배너 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetBannerResponseDto> getBanner() {

        List<BannerEntity> list = bannerRepository.findAll();

        return GetBannerResponseDto.success(list);

    }

}