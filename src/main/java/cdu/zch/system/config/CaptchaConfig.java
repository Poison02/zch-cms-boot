package cdu.zch.system.config;

import cdu.zch.system.common.enums.CaptchaTypeEnum;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

/**
 * @author Zch
 * @date 2023/7/10
 **/
@Configuration
@Data
public class CaptchaConfig {
    /**
     * 验证码类型
     */
    @Value("${easy-captcha.type}")
    private CaptchaTypeEnum type;
    /**
     * 验证码过期时间
     */
    @Value("${easy-captcha.ttl}")
    private long ttl;
    /**
     * 验证码内容长度
     */
    private int length = 4;
    /**
     * 验证码宽度
     */
    private int width = 120;
    /**
     * 验证码高度
     */
    private int height = 36;
    /**
     * 验证码字体
     */
    private String fontName = "Verdana";
    /**
     * 字体风格
     */
    private Integer fontStyle = Font.PLAIN;
    /**
     * 字体大小
     */
    private int fontSize = 20;
}
