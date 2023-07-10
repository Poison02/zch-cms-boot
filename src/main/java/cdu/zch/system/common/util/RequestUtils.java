package cdu.zch.system.common.util;

import cdu.zch.system.common.constant.SecurityConstants;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 请求工具类
 * @author Zch
 * @date 2023/7/10
 **/
public class RequestUtils {

    public static String resolveToken(HttpServletRequest request) {
        // 获取请求头中的Token
        String bearerToken = request.getHeader(SecurityConstants.TOKEN_KEY);
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            // 若Token非空且符合以 TOKEN_PREFIX开头，则截取掉TOKEN_PREFIX，返回剩下的Token体
            return bearerToken.substring(SecurityConstants.TOKEN_PREFIX.length());
        }
        return null;
    }

}
