package cdu.zch.system.common.util;

import cdu.zch.system.common.constant.SystemConstants;
import cdu.zch.system.security.userdetails.SysUserDetails;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Zch
 * @date 2023/7/10
 **/
public class SecurityUtils {

    /**
     * 获取当前登录人信息
     * @return SysUserDetails
     */
    public static SysUserDetails getUser() {
        // 获取当前请求的身份认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof SysUserDetails) {
                return (SysUserDetails) authentication.getPrincipal();
            }
        }
        return null;
    }

    /**
     * 获取用户ID
     * @return Long id
     */
    public static Long getUserId() {
        return Convert.toLong(getUser().getUserId());
    }

    /**
     * 获取部门ID
     * @return
     */
    public static Long getDeptId() {
        return Convert.toLong(getUser().getDeptId());
    }

    /**
     * 获取数据权限范围
     * @return
     */
    public static Integer getDataScope() {
        return Convert.toInt(getUser().getDataScope());
    }

    /**
     * 获取用户角色集合
     * @return
     */
    public static Set<String> getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (CollectionUtil.isNotEmpty(authorities)) {
                return authorities.stream()
                        // 只保留以 ROLE_ 为前缀的role
                        .filter(item -> item.getAuthority().startsWith("ROLE_"))
                        // 将过滤之后的选项进行处理，去掉前缀
                        .map(item -> StrUtil.removePrefix(item.getAuthority(), "ROLE_"))
                        .collect(Collectors.toSet());

            }
        }
        return Collections.EMPTY_SET;
    }

    /**
     * 获取用户权限集合
     * @return
     */
    public static Set<String> getPerms() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (CollectionUtil.isNotEmpty(authorities)) {
                return authorities.stream()
                        // 过滤掉 ROLE_ 为前缀的role
                        .filter(item -> !item.getAuthority().startsWith("ROLE_"))
                        // 将过滤之后的选项进行处理，得到权限集合
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());

            }
        }
        return Collections.EMPTY_SET;
    }

    /**
     * 是否是超级管理员
     * @return
     */
    public static boolean isRoot() {
        Set<String> roles = getRoles();
        if (roles.contains(SystemConstants.ROOT_ROLE_CODE)) {
            return true;
        }
        return false;
    }

    /**
     * 是否拥有权限
     * @param perm
     * @return
     */
    public static boolean hasPerm(String perm) {
        if (isRoot()) {
            return true;
        }
        Set<String> perms = getPerms();
        boolean hasPerm = perms.stream()
                .anyMatch(item -> PatternMatchUtils.simpleMatch(perm, item));
        return hasPerm;
    }

}
