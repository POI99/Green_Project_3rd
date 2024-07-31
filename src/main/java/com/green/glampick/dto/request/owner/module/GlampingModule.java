package com.green.glampick.dto.request.owner.module;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.dto.response.owner.post.PostGlampingInfoResponseDto;
import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.GlampingWaitEntity;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.OwnerErrorCode;
import com.green.glampick.repository.GlampingRepository;
import com.green.glampick.repository.GlampingWaitRepository;
import com.green.glampick.security.AuthenticationFacade;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.PUT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlampingModule {

    // 오너 PK 불러오기
    public static long ownerId(AuthenticationFacade facade){
        long userId = 0;
        userId = facade.getLoginUserId();
        if (userId <= 0) {
            throw new CustomException(CommonErrorCode.MNF);
        }
        return userId;
    }

    // 글램핑을 이미 가지고 있는가?
    public static void hasGlamping(GlampingWaitRepository waitRepository,
                                   GlampingRepository glampingRepository, OwnerEntity owner) {
        GlampingWaitEntity glamping1 = waitRepository.findByOwner(owner);
        GlampingEntity glamping2 = glampingRepository.findByOwner(owner);

        if (glamping1 != null || glamping2 != null) {
            throw new CustomException(OwnerErrorCode.AH);
        }
    }

    // 이미지가 들어있는가?
    public static void imgExist(MultipartFile img) {
        if (img == null || img.isEmpty()) {
            throw new CustomException(OwnerErrorCode.NF);
        }
    }

    // 글램핑 위치가 중복되는가?
    public static void existingLocation(GlampingWaitRepository waitRepository,
                                        GlampingRepository glampingRepository, String location) {
        GlampingWaitEntity glamping1 = waitRepository.findByGlampLocation(location);
        GlampingEntity glamping2 = glampingRepository.findByGlampLocation(location);
        if (glamping1 != null || glamping2 != null) {
            throw new CustomException(OwnerErrorCode.DL);
        }
    }
    // 글램핑 위치가 중복되는가? (위치정보 수정할 때)
    public static void locationUpdate(Long existingGlampId, long glampId) {
        if (existingGlampId != null && existingGlampId != glampId) {
            throw new CustomException(OwnerErrorCode.DL);
        }
    }

    // 전화번호 형식 변경하기 (053-000-0000 으로 왔을 때 0530000000 으로 바꾸기)
    public static String glampingCall(String call) {
        if(Pattern.compile("^[0-9]*?").matcher(call).matches()) {
            return call;
        }
        StringBuilder sb = new StringBuilder();
        Matcher matcher = Pattern.compile("[0-9]+").matcher(call);

        while (matcher.find()) {
            sb.append(matcher.group());
        }
        return sb.toString();
    }

    // 이미지 업로드
    public static String imageUpload(CustomFileUtils customFileUtils, MultipartFile img, long glampId) {
        // 이미지 파일명 만들기
        String glmapImgName = customFileUtils.makeRandomFileName(img);

        // 이미지 url로 저장하기
        String picNameUrl = String.format("/pic/glamping/%d/glamp/%s", glampId, glmapImgName);


        // 글램핑 대표 이미지 넣기
        try {
            // 폴더 : /glamping/{glampId}
            String glampPath = String.format("glamping/%s/glamp", glampId);
            customFileUtils.makeFolders(glampPath);
            // 파일을 저장한다
            String target = String.format("/%s/%s", glampPath, glmapImgName);
            customFileUtils.transferTo(img, target);
        } catch (Exception e) {
            throw new CustomException(OwnerErrorCode.FE);
        }

        return picNameUrl;
    }





}
