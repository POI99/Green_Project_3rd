package com.green.glampick.service.implement;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.common.Role;
import com.green.glampick.dto.response.admin.*;
import com.green.glampick.entity.BannerEntity;
import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.GlampingWaitEntity;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.AdminErrorCode;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.UserErrorCode;
import com.green.glampick.repository.*;
import com.green.glampick.repository.resultset.GetAccessGlampingListResultSet;
import com.green.glampick.repository.resultset.GetAccessOwnerSignUpListResultSet;
import com.green.glampick.repository.resultset.GetDeleteOwnerListResultSet;
import com.green.glampick.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.green.glampick.common.GlobalConst.MAX_BANNER_SIZE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final OwnerRepository ownerRepository;
    private final BannerRepository bannerRepository;
    private final GlampingRepository glampingRepository;
    private final GlampingWaitRepository glampingWaitRepository;
    private final AdminRepository adminRepository;
    private final CustomFileUtils customFileUtils;

    //  관리자 페이지 - 사장님 회원가입 정보 확인하기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetOwnerSignUpResponseDto> getOwnerSignUpInfo(Long ownerId) {

        try {

            OwnerEntity ownerEntity = ownerRepository.findByOwnerId(ownerId);
            return GetOwnerSignUpResponseDto.success(ownerEntity);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  관리자 페이지 - 대기중인 사장님 회원가입 리스트 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetAccessOwnerSignUpListResponseDto> accessSignUpList() {

        try {

            List<GetAccessOwnerSignUpListResultSet> list = adminRepository.getAccessOwnerSignUpList();

            return GetAccessOwnerSignUpListResponseDto.success(list);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  관리자 페이지 - 사장님 회원가입 승인 처리하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PatchAccessOwnerSignUpResponseDto> accessSignUp(Long ownerId) {

        try {

            ownerRepository.updateOwnerRole(Role.ROLE_OWNER, ownerId);

        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PatchAccessOwnerSignUpResponseDto.success();
    }

    //  관리자 페이지 - 사장님 회원가입 반려 처리하기  //
    @Override
    @Transactional
    public ResponseEntity<? super DeleteExclutionOwnerSignUpResponseDto> exclutionSignUp(Long ownerId) {

        try {

            OwnerEntity ownerEntity = ownerRepository.findByOwnerId(ownerId);

            if (ownerEntity.getRole() != Role.ROLE_RESERVE_OWNER) {
                throw new CustomException(UserErrorCode.NEP);
            }

            ownerRepository.delete(ownerEntity);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return DeleteExclutionOwnerSignUpResponseDto.success();

    }

    //  관리자 페이지 - 메인 화면 배너 추가하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostBannerResponseDto> postBanner(List<MultipartFile> file) {

        List<BannerEntity> bannerEntityList = bannerRepository.findAll();

        try {

            if (bannerEntityList.size() + file.size() > MAX_BANNER_SIZE) {
                throw new CustomException(AdminErrorCode.UFF);
            }

            for (MultipartFile image : file) {
                String makeFolder = String.format("banner");
                customFileUtils.makeFolders(makeFolder);
                String saveFileName = customFileUtils.makeRandomFileName(image);
                String saveDbFileName = String.format("/pic/banner/%s",saveFileName);
                String filePath = String.format("%s/%s", makeFolder, saveFileName);
                customFileUtils.transferTo(image, filePath);

                BannerEntity bannerEntity = new BannerEntity();
                bannerEntity.setBannerUrl(saveDbFileName);
                bannerRepository.save(bannerEntity);

            }

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
        return PostBannerResponseDto.success();
    }

    //  관리자 페이지 - 메인 화면 배너 삭제하기  //
    @Override
    @Transactional
    public ResponseEntity<? super DeleteBannerResponseDto> deleteBanner(Long bannerId) {

        try {

            BannerEntity bannerEntity = bannerRepository.findByBannerId(bannerId);
            bannerRepository.delete(bannerEntity);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return DeleteBannerResponseDto.success();


    }

    //  관리자 페이지 - 메인 화면 배너 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetBannerResponseDto> getBanner() {

        List<BannerEntity> list = bannerRepository.findAll();

        return GetBannerResponseDto.success(list);

    }

    //  관리자 페이지 - 승인 대기중인 글램핑장 리스트 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetAccessGlampingListResponseDto> getAccessGlampingList() {

        List<GetAccessGlampingListResultSet> list = adminRepository.getAccessGlampingList();

        return GetAccessGlampingListResponseDto.success(list);

    }

    //  관리자 페이지 - 사장님 글램핑 등록 상세 정보 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetAccessGlampingInfoResponseDto> getAccessGlamping(Long glampId) {
        GlampingEntity glampingEntity = new GlampingEntity();

        try {

            glampingEntity = glampingRepository.findByGlampId(glampId);

        } catch (CustomException e) {
          throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return GetAccessGlampingInfoResponseDto.success(glampingEntity);

    }

    //  관리자 페이지 - 글램핑 등록 승인 처리하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PatchGlampingAccessResponseDto> accessGlamping(Long glampId) {
        GlampingWaitEntity glampingWaitEntity = glampingWaitRepository.findByGlampId(glampId);

        GlampingEntity glampingEntity = new GlampingEntity();
        glampingEntity.setOwner(glampingWaitEntity.getOwner());
        glampingEntity.setGlampName(glampingWaitEntity.getGlampName());
        glampingEntity.setGlampCall(glampingWaitEntity.getGlampCall());
        glampingEntity.setRecommendScore(0D);
        glampingEntity.setGlampImage(glampingWaitEntity.getGlampImage());
        glampingEntity.setStarPointAvg(0D);
        glampingEntity.setReviewCount(0);
        glampingEntity.setGlampLocation(glampingWaitEntity.getGlampLocation());
        glampingEntity.setLocation(glampingWaitEntity.getLocation());
        glampingEntity.setRegion(glampingWaitEntity.getRegion());
        glampingEntity.setExtraCharge(glampingWaitEntity.getExtraCharge());
        glampingEntity.setGlampIntro(glampingWaitEntity.getGlampIntro());
        glampingEntity.setInfoBasic(glampingWaitEntity.getInfoBasic());
        glampingEntity.setInfoNotice(glampingWaitEntity.getInfoNotice());
        glampingEntity.setTraffic(glampingWaitEntity.getTraffic());
        glampingEntity.setActivateStatus(1);

        glampingRepository.save(glampingEntity);
        glampingWaitRepository.delete(glampingWaitEntity);

        return PatchGlampingAccessResponseDto.success();
    }

    //  관리자 페이지 - 글램핑 등록 반려 처리하기  //
    @Override
    @Transactional
    public ResponseEntity<? super GlampingExclutionResponseDto> exclutionGlamping(Long glampId) {

        GlampingWaitEntity glampingWaitEntity = glampingWaitRepository.findByGlampId(glampId);

        glampingWaitEntity.setExclusionStatus(-1);

        glampingWaitRepository.save(glampingWaitEntity);

        return GlampingExclutionResponseDto.success();

    }

    //  관리자 페이지 - 회원탈퇴 대기 사장님 리스트 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super getDeleteOwnerListResponseDto> deleteOwnerList() {

        List<GetDeleteOwnerListResultSet> list = ownerRepository.getDeleteOwnerList();

        return getDeleteOwnerListResponseDto.success(list);

    }

    //  관리자 페이지 - 사장님 회원탈퇴 승인 처리하기  //
    @Transactional
    @Override
    public ResponseEntity<? super PatchDeleteOwnerResponseDto> deleteOwner(Long ownerId) {

        OwnerEntity ownerEntity = ownerRepository.findByOwnerId(ownerId);
        GlampingEntity glampingEntity = glampingRepository.findByOwner(ownerEntity);

        glampingEntity.setActivateStatus(-1);
        ownerEntity.setActivateStatus(-1);

        return PatchDeleteOwnerResponseDto.success();

    }

}
