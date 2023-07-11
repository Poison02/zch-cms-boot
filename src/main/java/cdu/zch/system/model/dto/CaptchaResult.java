package cdu.zch.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Schema(description ="验证码响应对象")
@Builder
@Data
public class CaptchaResult {

    @Schema(description = "验证码缓存key")
    private String verifyCodeKey;

    @Schema(description = "验证码图片Base64字符串")
    private String verifyCodeBase64;

}
