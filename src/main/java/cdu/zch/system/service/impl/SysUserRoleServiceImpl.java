package cdu.zch.system.service.impl;

import cdu.zch.system.mapper.SysUserRoleMapper;
import cdu.zch.system.model.entity.SysUserRole;
import cdu.zch.system.service.SysUserRoleService;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
    @Override
    public boolean saveUserRoles(Long userId, List<Long> roleIds) {
        if (userId == null || CollectionUtil.isEmpty(roleIds)) {
            return false;
        }
        List<Long> userRoleIds = this.list(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId))
                .stream()
                .map(SysUserRole::getRoleId)
                .toList();
        // 新增用户角色
        List<Long> saveRoleIds;
        if (CollectionUtil.isEmpty(userRoleIds)) {
            saveRoleIds = roleIds;
        } else {
            saveRoleIds = roleIds.stream()
                    .filter(roleId -> !userRoleIds.contains(roleId))
                    .toList();
        }

        List<SysUserRole> saveUserRoles = saveRoleIds
                .stream()
                .map(roleId -> new SysUserRole(userId, roleId))
                .toList();
        this.saveBatch(saveUserRoles);

        if (CollectionUtil.isEmpty(userRoleIds)) {
            List<Long> removeRoleIds = userRoleIds
                    .stream()
                    .filter(roleId -> !roleIds.contains(roleId))
                    .toList();

            if (CollectionUtil.isNotEmpty(removeRoleIds)) {
                this.remove(new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
                        .in(SysUserRole::getRoleId, removeRoleIds)
                );
            }
        }
        return true;
    }
}
