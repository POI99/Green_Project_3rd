package com.green.glampick.common.swagger.description.owner;

public class GetRevenueDescription {

    public static final String OWNER_REVENUE_DESCRIPTION =

            "<strong>매출 및 방마다 매출을 불러옵니다.</strong>" +
                    "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>" +
                    "<p>-------------------------------------------------</p>" +
                    "<p>startDayId : 조회를 시작하는 날짜 </p>" +
                    "<p>endDayId : 조회를 끝내는 날짜 </p>" +
                    "<p> </p>"
            ;

    public static final String OWNER_REVENUE_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
                    "<p>CU - 미 로그인 (400)</p>" +
                    "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}
