package cdu.zch.system.service;

import cdu.zch.system.model.dto.UserAuthInfo;
import cdu.zch.system.model.entity.SysUser;
import cdu.zch.system.model.form.UserForm;
import cdu.zch.system.model.query.UserPageQuery;
import cdu.zch.system.model.vo.UserExportVO;
import cdu.zch.system.model.vo.UserInfoVO;
import cdu.zch.system.model.vo.UserPageVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户业务接口
 * @author Zch
 * @date 2023/7/10
 **/
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户分页列表
     * @param queryParams
     * @return
     */
    IPage<UserPageVO> getUserPage(UserPageQuery queryParams);

    /**
     * 获取用户表单数据
     * @param userId 用户ID
     * @return
     */
    UserForm getUserFormData(Long userId);

    /**
     * 新增用户
     * @param userForm 用户表单对象
     * @return
     */
    boolean saveUser(UserForm userForm);

    /**
     * 修改用户
     * @param userId 用户ID
     * @param userForm 用户表单对象
     * @return
     */
    boolean updateUser(Long userId, UserForm userForm);

    /**
     * 删除用户
     * @param idsStr 用户ID，多个用户以 , 分割
     * @return
     */
    boolean deleteUsers(String idsStr);

    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param password 用户新密码
     * @return
     */
    boolean updatePassword(Long userId, String password);

    /**
     * 根据用户名获取认证信息
     * @param username 用户名
     * @return
     */
    UserAuthInfo getUserAuthInfo(String username);

    List<UserExportVO> listExportUsers(UserPageQuery queryParams);

    /**
     * 获取登录用户信息
     * @return
     */
    UserInfoVO getUserLoginInfo();

}
