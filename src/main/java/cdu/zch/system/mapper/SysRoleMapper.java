package cdu.zch.system.mapper;

import cdu.zch.system.model.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Mapper
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 获取最大范围的数据权限
     * @param roles
     * @return
     */
    Integer getMaximumDataScope(Set<String> roles);

}
