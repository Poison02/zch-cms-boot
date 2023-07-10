package cdu.zch.system.filter;

import cdu.zch.system.common.constant.SecurityConstants;
import cdu.zch.system.common.result.ResultCode;
import cdu.zch.system.common.util.ResponseUtils;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Zch
 * @date 2023/7/10
 **/
public class VerifyCodeFilter extends OncePerRequestFilter {

    // 匹配登录请求的路径
    private static final AntPathRequestMatcher LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(SecurityConstants.LOGIN_PATH, "POST");

    // 验证码的键
    private static final String VERIFY_CODE_PARAM_KEY = "verifyCode";

    // 验证码的值
    private static final String VERIFY_CODE_KEY_PARAM_KEY = "verifyCodeKey";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 对登录接口的验证码进行校验
        if (LOGIN_PATH_REQUEST_MATCHER.matches(request)) {
            // 请求中的验证码
            String requestVerifyCode = request.getParameter(VERIFY_CODE_PARAM_KEY);

            if (StrUtil.isBlank(requestVerifyCode)) {
                // 若没有输入验证码，则终止当前过滤器
                filterChain.doFilter(request, response);
                return;
            }

            // 从Redis中取出验证码
            RedisTemplate redisTemplate = SpringUtil.getBean("redisTemplate", RedisTemplate.class);
            String verifyCodeKey = request.getParameter(VERIFY_CODE_KEY_PARAM_KEY);
            Object cacheVerifyCode = redisTemplate.opsForValue().get(SecurityConstants.VERIFY_CODE_CACHE_PREFIX + verifyCodeKey);
            if (cacheVerifyCode == null) {
                // 没找到验证码，则验证码超时
                ResponseUtils.writeErrMsg(response, ResultCode.VERIFY_CODE_TIMEOUT);
            } else {
                // 校验验证码
                if (StrUtil.equals(requestVerifyCode, Convert.toStr(cacheVerifyCode))) {
                    filterChain.doFilter(request, response);
                } else {
                    ResponseUtils.writeErrMsg(response, ResultCode.VERIFY_CODE_ERROR);
                }
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }
}
