package com.green.glampick.service.implement;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.common.Role;
import com.green.glampick.dto.request.admin.PatchAccessOwnerSignUpRequestDto;
import com.green.glampick.dto.request.admin.PostBannerRequestDto;
import com.green.glampick.dto.response.admin.DeleteBannerResponseDto;
import com.green.glampick.dto.response.admin.DeleteExclutionOwnerSignUpResponseDto;
import com.green.glampick.dto.response.admin.PatchAccessOwnerSignUpResponseDto;
import com.green.glampick.dto.response.admin.PostBannerResponseDto;
import com.green.glampick.entity.BannerEntity;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.UserErrorCode;
import com.green.glampick.repository.BannerRepository;
import com.green.glampick.repository.OwnerRepository;
import com.green.glampick.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final OwnerRepository ownerRepository;
    private final BannerRepository bannerRepository;
    private final CustomFileUtils customFileUtils;

    //  관리자 페이지 - 사장님 회원가입 승인 처리하기  //
    @Override
    public ResponseEntity<? super PatchAccessOwnerSignUpResponseDto> accessSignUp(PatchAccessOwnerSignUpRequestDto dto) {

        try {

            ownerRepository.updateOwnerRole(Role.ROLE_OWNER, dto.getOwnerId());

        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PatchAccessOwnerSignUpResponseDto.success();
    }

    //  관리자 페이지 - 사장님 회원가입 반려 처리하기  //
    @Override
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
    public ResponseEntity<? super PostBannerResponseDto> postBanner(List<MultipartFile> file) {

        try {

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

}
