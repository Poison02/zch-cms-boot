package cdu.zch.system.filter;

import cdu.zch.system.common.constant.SecurityConstants;
import cdu.zch.system.common.result.ResultCode;
import cdu.zch.system.common.util.RequestUtils;
import cdu.zch.system.common.util.ResponseUtils;
import cdu.zch.system.security.JwtTokenManager;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT校验过滤器
 * @author Zch
 * @date 2023/7/10
 **/
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // 登录请求路径
    private static final AntPathRequestMatcher LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(SecurityConstants.LOGIN_PATH, "POST");

    // JWT管理器
    private final JwtTokenManager tokenManager;

    public JwtAuthenticationFilter(JwtTokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 是登录请求则直接放行
        if (LOGIN_PATH_REQUEST_MATCHER.matches(request)) {
            filterChain.doFilter(request, response);
        } else {
            // 获取Token
            String jwt = RequestUtils.resolveToken(request);
            // SecurityContextHolder.getContext().getAuthentication() == null 表示还未进行身份认证
            if (StrUtil.isNotBlank(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    // 返回该Token对应的Claim
                    Claims claims = this.tokenManager.parseAndValidateToken(jwt);

                    // 对该Claim进行认证
                    Authentication authentication = this.tokenManager.getAuthentication(claims);
                    // 标记为已进行身份认证
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    // jwt处理异常，已经存在黑名单里面了
                    ResponseUtils.writeErrMsg(response, ResultCode.TOKEN_INVALID);
                }
            } else {
                // jwt为空或者已经认证过了，返回异常
                ResponseUtils.writeErrMsg(response, ResultCode.TOKEN_INVALID);
            }
        }
    }
}
