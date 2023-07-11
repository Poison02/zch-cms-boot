package cdu.zch.system.mapper;

import cdu.zch.system.model.bo.RouteBO;
import cdu.zch.system.model.entity.SysMenu;
import cdu.zch.system.model.vo.RouteVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Mapper
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 返回路由列表
     * @return
     */
    List<RouteBO> listRoutes();

    /**
     * 获取角色权限集合
     * @param roles
     * @return
     */
    Set<String> listRolePerms(Set<String> roles);

}
