package com.green.glampick.service.implement;

import com.green.glampick.common.Role;
import com.green.glampick.dto.request.admin.PatchAccessOwnerSignUpRequestDto;
import com.green.glampick.dto.response.admin.PatchAccessOwnerSignUpResponseDto;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.repository.OwnerRepository;
import com.green.glampick.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

}
