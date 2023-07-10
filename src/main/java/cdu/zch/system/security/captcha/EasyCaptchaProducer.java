package cdu.zch.system.security.captcha;

import cdu.zch.system.config.CaptchaConfig;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * 验证码生成器
 * @author Zch
 * @date 2023/7/10
 **/
@Component
@RequiredArgsConstructor
public class EasyCaptchaProducer {

    private final CaptchaConfig captchaConfig;

    public Captcha getCaptcha() {
        Captcha captcha;
        int width = captchaConfig.getWidth();
        int height = captchaConfig.getHeight();
        int length = captchaConfig.getLength();
        String fontName = captchaConfig.getFontName();

        // 生成验证码
        switch (captchaConfig.getType()) {
            case ARITHMETIC -> {
                captcha = new ArithmeticCaptcha(width, height);
                captcha.setLen(2);
            }
            case CHINESE -> {
                captcha = new ChineseCaptcha(width, height);
                captcha.setLen(length);
            }
            case CHINESE_GIF -> {
                captcha = new ChineseGifCaptcha(width, height);
                captcha.setLen(length);
            }
            case GIF -> {
                //最后一位是位数
                captcha = new GifCaptcha(width, height);
                captcha.setLen(length);
            }
            case SPEC -> {
                captcha = new SpecCaptcha(width, height);
                captcha.setLen(length);
            }
            default -> throw new RuntimeException("验证码配置错误！");
        }
        captcha.setFont(new Font(fontName, captchaConfig.getFontStyle(), captchaConfig.getFontSize()));;
        return captcha;
    }

}
