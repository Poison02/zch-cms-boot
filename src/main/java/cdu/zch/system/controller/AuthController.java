package cdu.zch.system.controller;

import cdu.zch.system.common.constant.SecurityConstants;
import cdu.zch.system.common.result.Result;
import cdu.zch.system.common.util.RequestUtils;
import cdu.zch.system.model.dto.CaptchaResult;
import cdu.zch.system.model.dto.LoginResult;
import cdu.zch.system.security.JwtTokenManager;
import cdu.zch.system.security.captcha.EasyCaptchaService;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Auth认证中心
 * @author Zch
 * @date 2023/7/11
 **/
@Tag(name = "01-认证中心")
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenManager jwtTokenManager;

    @Resource
    private EasyCaptchaService easyCaptchaService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result<LoginResult> login(
            @Parameter(description = "用户名", example = "admin") @RequestParam String username,
            @Parameter(description = "密码", example = "123456") @RequestParam String password
    ) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username.toLowerCase().trim(),
                password
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 生成Token
        String accessToken = jwtTokenManager.createToken(authentication);
        LoginResult loginResult = LoginResult.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .build();
        return Result.success(loginResult);
    }

    @Operation(summary = "注销", security = {@SecurityRequirement(name = SecurityConstants.TOKEN_KEY)})
    @DeleteMapping("/logout")
    public Result logout(HttpServletRequest request) {
        String token = RequestUtils.resolveToken(request);
        if (StrUtil.isNotBlank(token)) {
            Claims claims = jwtTokenManager.getTokenClaims(token);
            String jti = claims.get("jti", String.class);
            Date expiration = claims.getExpiration();
            if (expiration != null) {
                // 有过期时间，在有效时间内存入黑名单，超出时间移除黑名单
                long ttl = (expiration.getTime() - System.currentTimeMillis());
                redisTemplate.opsForValue().set(SecurityConstants.BLACK_TOKEN_CACHE_PREFIX + jti, null, ttl, TimeUnit.MILLISECONDS);
            } else {
                // 无过期时间，永久加入黑名单
                redisTemplate.opsForValue().set(SecurityConstants.BLACK_TOKEN_CACHE_PREFIX + jti, null);;
            }
        }
        SecurityContextHolder.clearContext();
        return Result.success("注销成功");
    }

    @Operation(summary = "获取验证码")
    @GetMapping("/captcha")
    public Result getCaptcha() {
        CaptchaResult captchaResult = easyCaptchaService.getCaptcha();
        return Result.success(captchaResult);
    }

}
