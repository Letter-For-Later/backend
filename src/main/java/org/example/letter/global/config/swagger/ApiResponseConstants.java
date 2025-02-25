package org.example.letter.global.config.swagger;

public class ApiResponseConstants {
    
    public static final String SUCCESS_RESPONSE = 
            "{\"isSuccess\": true, \"code\": \"COMMON200\", \"message\": \"성공입니다.\", \"result\": null}";
    
    public static final String BAD_REQUEST_RESPONSE = 
            "{\"isSuccess\": false, \"code\": \"COMMON400\", \"message\": \"잘못된 요청입니다.\", \"result\": null}";
    
    public static final String NOT_FOUND_RESPONSE = 
            "{\"isSuccess\": false, \"code\": \"COMMON404\", \"message\": \"리소스를 찾을 수 없습니다.\", \"result\": null}";
    
    public static final String SERVER_ERROR_RESPONSE = 
            "{\"isSuccess\": false, \"code\": \"COMMON500\", \"message\": \"서버 에러, 관리자에게 문의 바랍니다.\", \"result\": null}";
}
