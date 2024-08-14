package com.green.glampick.service.implement;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.repository.GlampingWaitRepository;
import com.green.glampick.repository.OwnerRepository;
import com.green.glampick.security.AuthenticationFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@Import({ OwnerServiceImpl.class })
class OwnerServiceImplTest {

    @Mock private AuthenticationFacade authenticationFacade;
    @Mock private OwnerRepository ownerRepository;
    @Mock private GlampingWaitRepository glampingWaitRepository;
    @Mock private CustomFileUtils customFileUtils;

    @Test
    @DisplayName("글램핑 등록 테스트")
    void postGlampingInfo() {
        // 권한 없음
        given(authenticationFacade.getLoginUserId()).willReturn(1L);

    }

    @Test
    void getRoomList() {
    }

    @Test
    void deleteRoom() {
    }

    @Test
    void patchOwnerInfo() {
    }

}