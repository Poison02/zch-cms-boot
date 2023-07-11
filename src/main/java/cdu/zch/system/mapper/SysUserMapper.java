package cdu.zch.system.mapper;

import cdu.zch.system.common.annotation.DataPermission;
import cdu.zch.system.model.bo.UserBO;
import cdu.zch.system.model.bo.UserFormBO;
import cdu.zch.system.model.dto.UserAuthInfo;
import cdu.zch.system.model.entity.SysUser;
import cdu.zch.system.model.query.UserPageQuery;
import cdu.zch.system.model.vo.UserExportVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户持久层
 * @author Zch
 * @date 2023/7/10
 **/
@Mapper
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 获取用户分页列表
     * @param page
     * @param queryParams 查询参数
     * @return
     */
    @DataPermission(deptAlias = "u")
    Page<UserBO> getUserPage(Page<UserBO> page, UserPageQuery queryParams);

    /**
     * 获取用户表单详情
     * @param UserId 用户ID
     * @return
     */
    UserFormBO getUserDetail(Long UserId);

    /**
     * 根据用户名获取认证信息
     * @param username
     * @return
     */
    UserAuthInfo getUserAuthInfo(String username);

    /**
     * 获取导出用户列表
     * @param queryParams
     * @return
     */
    List<UserExportVO> listExportUsers(UserPageQuery queryParams);

}
