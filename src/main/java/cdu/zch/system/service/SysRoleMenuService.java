package cdu.zch.system.service;

import cdu.zch.system.model.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色菜单业务接口
 * @author Zch
 * @date 2023/7/11
 **/
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 获取角色拥有的菜单ID集合
     * @param roleId
     * @return
     */
    List<Long> listMenuIdsByRoleId(Long roleId);

}
