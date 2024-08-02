package com.green.glampick.common.swagger.description.owner;

public class GetGlampingFromUserReviewSwaggerDescription {

    public static final String GLAMPING_FROM_USER_REVIEW_VIEW_DESCRIPTION =

            "<strong>글램핑에 작성된 리뷰를 모두 불러옵니다.</strong>" +
            "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>"
            ;

    public static final String GLAMPING_FROM_USER_REVIEW_VIEW_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>MNF - 찾을 수 없는 회원정보 (400)</p>" +
            "<p>NP - 권한 없음 (403)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;


}
