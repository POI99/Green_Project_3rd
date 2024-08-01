package com.green.glampick.service.implement;

import com.green.glampick.common.Role;
import com.green.glampick.dto.request.admin.PatchAccessOwnerSignUpRequestDto;
import com.green.glampick.dto.request.admin.PostBannerRequestDto;
import com.green.glampick.dto.response.admin.DeleteExclutionOwnerSignUpResponseDto;
import com.green.glampick.dto.response.admin.PatchAccessOwnerSignUpResponseDto;
import com.green.glampick.dto.response.admin.PostBannerResponseDto;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.UserErrorCode;
import com.green.glampick.repository.OwnerRepository;
import com.green.glampick.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final OwnerRepository ownerRepository;

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

    @Override
    public ResponseEntity<? super PostBannerResponseDto> postBanner(PostBannerRequestDto dto, MultipartFile file) {

        try {



        } catch (CustomException e) {
            e.getErrorCode();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
        return null;
    }

}
