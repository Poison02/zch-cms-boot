package cdu.zch.system.service;

import cdu.zch.system.model.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Zch
 * @date 2023/7/11
 **/
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 保存用户角色
     * @param userId
     * @param roleIds
     * @return
     */
    boolean saveUserRoles(Long userId, List<Long> roleIds);

}
