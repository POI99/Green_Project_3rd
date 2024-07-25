package com.green.glampick.entity;

import com.green.glampick.common.Role;
import com.green.glampick.dto.request.login.SignUpRequestDto;
import com.green.glampick.dto.request.user.GetBookRequestDto;
import com.green.glampick.dto.request.user.UpdateUserRequestDto;
import com.green.glampick.security.SignInProviderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
// 유저 테이블
public class UserEntity extends UpdatedAt {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Comment("유저 ID")
    private Long userId;

    @Column(nullable = false, unique = true) @Comment("유저 이메일")
    private String userEmail;

    @Column(nullable = false) @Comment("쿠폰 ID")
    private String userPw;

    @Column(nullable = false) @Comment("유저 실명")
    private String userName;

    @Column(nullable = false, unique = true) @Comment("유저 닉네임")
    private String userNickname;

    @Comment("유저 휴대폰 번호")
    private String userPhone;

    @Comment("유저 프로필 이미지")
    private String userProfileImage;

    @Column(nullable = false) @Enumerated(value = EnumType.STRING) @Comment("유저 권한")
    private Role userRole;

    @Comment("소셜 유저 ID")
    private String providerId;

    @Column(nullable = false) @Enumerated(value = EnumType.STRING) @Comment("소셜 로그인 타입")
    private SignInProviderType userSocialType;

    public UserEntity(SignUpRequestDto dto) {
        this.userEmail = dto.getUserEmail();
        this.userPw = dto.getUserPw();
        this.userPhone = dto.getUserPhone();
        this.userName = dto.getUserName();
        this.userNickname = dto.getUserNickname();
        this.userRole = dto.getUserRole();
        this.userSocialType = dto.getUserSocialType();
    }

    public UserEntity(UpdateUserRequestDto dto) {
        this.userPw = dto.getUserPw();
        this.userNickname = dto.getUserNickname();
    }

    public UserEntity(long userId
            , String providerId
            , String userPw
            , String userName
            , String userProfileImage
            ) {
        this.userId = userId;
        this.providerId = providerId;
        this.userPw = userPw;
        this.userName = userName;
        this.userProfileImage= userProfileImage;
    }
}
