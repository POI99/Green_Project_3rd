package com.green.glampick.service.implement;

import com.green.glampick.common.Role;
import com.green.glampick.common.security.AppProperties;
import com.green.glampick.common.security.CookieUtils;
import com.green.glampick.dto.request.login.SignInRequestDto;
import com.green.glampick.dto.request.login.SignUpRequestDto;
import com.green.glampick.entity.UserEntity;
import com.green.glampick.jwt.JwtTokenProvider;
import com.green.glampick.oauth2.userinfo.OAuth2UserInfo;
import com.green.glampick.oauth2.userinfo.OAuth2UserInfoFactory;
import com.green.glampick.repository.UserRepository;
import com.green.glampick.security.MyUser;
import com.green.glampick.security.MyUserDetail;
import com.green.glampick.security.MyUserOAuth2Vo;
import com.green.glampick.security.SignInProviderType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginServiceImpl extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final OAuth2UserInfoFactory oAuth2UserInfoFactory;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtils cookieUtils;
    private final AppProperties appProperties;


    public OAuth2User loadUser(OAuth2UserRequest userRequest, HttpServletResponse res) throws OAuth2AuthenticationException {
        try {
            return this.process(userRequest, res);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, HttpServletResponse res) {
        OAuth2User oAuth2User = super.loadUser(userRequest); //제공자로부터 사용자정보를 얻음

        String accessToken = null;
        String refreshToken = null;

        //각 소셜플랫폼에 맞는 enum타입을 얻는다.
        SignInProviderType signInProviderType = SignInProviderType.valueOf(userRequest.getClientRegistration()
                .getRegistrationId()
                .toUpperCase()
        );

        //규격화된 UserInfo객체로 변환
        // oAuth2User.getAttributes() > Data가 HashMap 객체로 변환
        OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoFactory.getOAuth2UserInfo(signInProviderType, oAuth2User.getAttributes());

        //기존에 회원가입이 되어있는가 체크
        SignInRequestDto signInParam = new SignInRequestDto();
        signInParam.setProviderId(oAuth2UserInfo.getId()); //플랫폼에서 넘어오는 유니크값(항상 같은 값이며 다른 사용자와 구별되는 유니크 값)
        signInParam.setUserSocialType(signInProviderType.name());
        String providerId = signInParam.getProviderId();
        UserEntity userEntity = userRepository.findByProviderId(providerId);

        if (userEntity == null) { //회원가입 처리
            SignUpRequestDto signUpParam = new SignUpRequestDto();
            signUpParam.setUserSocialType(signInProviderType);
            signUpParam.setProviderId(oAuth2UserInfo.getId());
            signUpParam.setUserEmail(oAuth2UserInfo.getEmail());

            userEntity = new UserEntity();
            userEntity.setUserSocialType(signUpParam.getUserSocialType());
            userEntity.setProviderId(signUpParam.getProviderId());
            userEntity.setUserEmail(signUpParam.getUserEmail());
            userRepository.save(userEntity);

        }

        MyUserOAuth2Vo myUserOAuth2Vo
                = new MyUserOAuth2Vo(userEntity.getUserId(), Role.ROLE_USER, userEntity.getUserEmail());

        MyUserDetail signInUser = new MyUserDetail();
        signInUser.setMyUser(myUserOAuth2Vo);


//        자네는 엑세스토큰 리프레쉬 토큰 값 넣어주지도 않고
//        null값 들어온다하면 내가 뭘 말해야 하는걸까요?
//        이싸람아...
//        >> 옆에 코드 보면서 쿠키랑 토큰 같이 넘겨줘야지 이싸람이
//        이제보니까 엑세스토큰도 안줘? ㅋㅋㅋ

        //  myUser 에 넣은 데이터를 통해, AccessToken, RefreshToken 을 만든다.  //
        accessToken = jwtTokenProvider.generateAccessToken(signInUser.getMyUser());
        refreshToken = jwtTokenProvider.generateRefreshToken(signInUser.getMyUser());

        //  RefreshToken 을 갱신한다.  //
        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "refresh-token");
        cookieUtils.setCookie(res, "refresh-token", refreshToken, refreshTokenMaxAge);




        return signInUser;
    }


}
