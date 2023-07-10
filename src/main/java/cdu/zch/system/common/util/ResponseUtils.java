package cdu.zch.system.common.util;

import cdu.zch.system.common.result.Result;
import cdu.zch.system.common.result.ResultCode;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * 响应工具类
 * @author Zch
 * @date 2023/7/10
 **/
public class ResponseUtils {

    public static void writeErrMsg(HttpServletResponse response, ResultCode resultCode) throws IOException {
        switch (resultCode) {
            case ACCESS_UNAUTHORIZED, TOKEN_INVALID -> response.setStatus(HttpStatus.UNAUTHORIZED.value());
            case TOKEN_ACCESS_FORBIDDEN -> response.setStatus(HttpStatus.FORBIDDEN.value());
            default -> response.setStatus(HttpStatus.BAD_REQUEST.value());

        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(JSONUtil.toJsonStr(Result.failed(resultCode)));
    }

}
