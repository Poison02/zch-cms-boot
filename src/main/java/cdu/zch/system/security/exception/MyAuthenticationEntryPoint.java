package cdu.zch.system.security.exception;

import cdu.zch.system.common.result.ResultCode;
import cdu.zch.system.common.util.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义认证入口点
 * 在请求需要进行身份验证的情况下，当用户未经身份验证时，自定义处理方式来返回错误响应
 * @author Zch
 * @date 2023/7/10
 **/
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseUtils.writeErrMsg(response, ResultCode.TOKEN_INVALID);
    }
}
