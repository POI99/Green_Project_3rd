package com.green.glampick.common.swagger.description.owner;

public class DeleteRoomImageSwaggerDescription {

    public static final String DELETE_ROOM_IMAGE_DESCRIPTION =

            "<strong>로그인한 사장이 객실 이미지를 삭제합니다.</strong>" +
            "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>"
            ;

    public static final String DELETE_ROOM_IMAGE_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>MNF - 찾을 수 없는 회원정보 (400)</p>" +
            "<p>MNR - 찾을 수 없는 객실정보 (400)</p>" +
            "<p>CF - 이미지 삭제 오류 (400)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}
