package cdu.zch.system.mapper;

import cdu.zch.system.model.entity.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Mapper
@Repository
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 获取角色拥有的菜单ID集合
     * @param roleId
     * @return
     */
    List<Long> listMenuIdsByRoleId(Long roleId);

}
