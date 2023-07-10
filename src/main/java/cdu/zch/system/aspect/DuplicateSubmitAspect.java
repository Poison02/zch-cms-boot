package cdu.zch.system.aspect;

import cdu.zch.system.common.annotation.PreventDuplicateSubmit;
import cdu.zch.system.common.exception.BusinessException;
import cdu.zch.system.common.result.ResultCode;
import cdu.zch.system.common.util.RequestUtils;
import cdu.zch.system.security.JwtTokenManager;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;
import org.redisson.api.RedissonClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

/**
 * 处理重复提交的切面
 * @author Zch
 * @date 2023/7/10
 **/
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DuplicateSubmitAspect {

    private final RedissonClient redissonClient;

    private final JwtTokenManager jwtTokenManager;

    private static final String RESUBMIT_LOCK_PREFIX = "LOCK:RESUBMIT";

    /**
     * 防重复提交切点
     * @param preventDuplicateSubmit 自定义注解 防重提交
     */
    @Pointcut("@annotation(preventDuplicateSubmit)")
    public void preventDuplicateSubmitPointCut(PreventDuplicateSubmit preventDuplicateSubmit) {}

    /**
     * 环绕通知
     * @param joinPoint 连接点
     * @param preventDuplicateSubmit 自定义注解
     * @return
     */
    @Around("preventDuplicateSubmitPointCut(preventDuplicateSubmit)")
    public Object doAround(ProceedingJoinPoint joinPoint, PreventDuplicateSubmit preventDuplicateSubmit) throws Throwable {
        String resubmitLockKey = generateResubmitLockKey();
        if (resubmitLockKey != null) {
            // 过期时间
            int expire = preventDuplicateSubmit.expire();
            RLock lock = redissonClient.getLock(resubmitLockKey);
            boolean lockResult = lock.tryLock(0, expire, TimeUnit.SECONDS);
            if (!lockResult) {
                throw new BusinessException(ResultCode.REPEAT_SUBMIT_ERROR);
            }
        }
        return joinPoint.proceed();
    }

    private String generateResubmitLockKey() {
        String resubmitLockKey = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String jwt = RequestUtils.resolveToken(request);
        if (StrUtil.isNotBlank(jwt)) {
            String jti = (String) jwtTokenManager.getTokenClaims(jwt).get("jti");
            resubmitLockKey = RESUBMIT_LOCK_PREFIX + jti + ":" + request.getMethod() + "-" + request.getRequestURI();
        }
        return resubmitLockKey;
    }

}
