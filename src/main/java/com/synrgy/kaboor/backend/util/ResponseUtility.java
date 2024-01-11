package com.synrgy.kaboor.backend.util;

import java.util.Map;

public class ResponseUtility {

    public static BaseResponse<Map<String, Object>> getBaseResponse(int statusCode, String message, Map<String, Object> data) {
        BaseResponse<Map<String, Object>> response = new BaseResponse<>();
        response.setCode(statusCode);
        response.setMessage(message);
        response.setData(data);

        return response;
    }

}
