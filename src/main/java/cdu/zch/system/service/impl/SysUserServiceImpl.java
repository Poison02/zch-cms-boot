package cdu.zch.system.service.impl;

import cdu.zch.system.mapper.SysUserMapper;
import cdu.zch.system.model.dto.UserAuthInfo;
import cdu.zch.system.model.entity.SysUser;
import cdu.zch.system.model.form.UserForm;
import cdu.zch.system.model.query.UserPageQuery;
import cdu.zch.system.model.vo.UserExportVO;
import cdu.zch.system.model.vo.UserInfoVO;
import cdu.zch.system.model.vo.UserPageVO;
import cdu.zch.system.service.SysUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zch
 * @date 2023/7/10
 **/
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public IPage<UserPageVO> getUserPage(UserPageQuery queryParams) {
        return null;
    }

    @Override
    public UserForm getUserFormData(Long userId) {
        return null;
    }

    @Override
    public boolean saveUser(UserForm userForm) {
        return false;
    }

    @Override
    public boolean updateUser(Long userId, UserForm userForm) {
        return false;
    }

    @Override
    public boolean deleteUsers(String idsStr) {
        return false;
    }

    @Override
    public boolean updatePassword(Long userId, String password) {
        return false;
    }

    @Override
    public UserAuthInfo getUserAuthInfo(String username) {
        return null;
    }

    @Override
    public List<UserExportVO> listExportUsers(UserPageQuery queryParams) {
        return null;
    }

    @Override
    public UserInfoVO getUserLoginInfo() {
        return null;
    }
}
