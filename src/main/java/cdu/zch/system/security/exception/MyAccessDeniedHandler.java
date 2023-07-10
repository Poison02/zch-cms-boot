package cdu.zch.system.security.exception;

import cdu.zch.system.common.result.ResultCode;
import cdu.zch.system.common.util.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义访问被拒绝处理器
 * 在请求访问被拒绝时，自定义处理方式来返回错误响应
 * @author Zch
 * @date 2023/7/10
 **/
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseUtils.writeErrMsg(response, ResultCode.TOKEN_ACCESS_FORBIDDEN);
    }
}
