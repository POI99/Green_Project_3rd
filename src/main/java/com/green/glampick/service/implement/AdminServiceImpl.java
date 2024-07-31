package com.green.glampick.service.implement;

import com.green.glampick.common.Role;
import com.green.glampick.dto.request.admin.PatchAccessOwnerSignUpRequestDto;
import com.green.glampick.dto.response.admin.PatchAccessOwnerSignUpResponseDto;
import com.green.glampick.entity.OwnerEntity;
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

        OwnerEntity ownerEntity = ownerRepository.findByOwnerId(dto.getOwnerId());

        ownerEntity.setRole(Role.ROLE_OWNER);

        ownerRepository.save(ownerEntity);

        return PatchAccessOwnerSignUpResponseDto.success();
    }

}
