package cdu.zch.system.security.captcha;

import cdu.zch.system.common.constant.SecurityConstants;
import cdu.zch.system.config.CaptchaConfig;
import cdu.zch.system.model.dto.CaptchaResult;
import cn.hutool.core.util.IdUtil;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 验证码业务类
 * @author Zch
 * @date 2023/7/10
 **/
@Component
@RequiredArgsConstructor
public class EasyCaptchaService {

    private final EasyCaptchaProducer easyCaptchaProducer;

    private final RedisTemplate<String, Object> redisTemplate;

    private final CaptchaConfig captchaConfig;

    public CaptchaResult getCaptcha() {
        Captcha captcha = easyCaptchaProducer.getCaptcha();
        String captchaText = captcha.text();
        String captchaBase64 = captcha.toBase64();

        String verifyCodeKey = IdUtil.fastSimpleUUID();
        redisTemplate.opsForValue().set(SecurityConstants.VERIFY_CODE_CACHE_PREFIX + verifyCodeKey, captchaText,
                captchaConfig.getTtl(), TimeUnit.SECONDS);

        return CaptchaResult.builder()
                .verifyCodeKey(verifyCodeKey)
                .verifyCodeBase64(captchaBase64).build();
    }

}
