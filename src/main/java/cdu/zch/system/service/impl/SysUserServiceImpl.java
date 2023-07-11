package cdu.zch.system.service.impl;

import cdu.zch.system.common.constant.SecurityConstants;
import cdu.zch.system.common.constant.SystemConstants;
import cdu.zch.system.common.util.SecurityUtils;
import cdu.zch.system.converter.UserConverter;
import cdu.zch.system.mapper.SysUserMapper;
import cdu.zch.system.model.bo.UserBO;
import cdu.zch.system.model.bo.UserFormBO;
import cdu.zch.system.model.dto.UserAuthInfo;
import cdu.zch.system.model.entity.SysUser;
import cdu.zch.system.model.form.UserForm;
import cdu.zch.system.model.query.UserPageQuery;
import cdu.zch.system.model.vo.UserExportVO;
import cdu.zch.system.model.vo.UserInfoVO;
import cdu.zch.system.model.vo.UserPageVO;
import cdu.zch.system.service.SysMenuService;
import cdu.zch.system.service.SysRoleService;
import cdu.zch.system.service.SysUserRoleService;
import cdu.zch.system.service.SysUserService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Zch
 * @date 2023/7/10
 **/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private SysUserRoleService userRoleService;

    @Resource
    private UserConverter userConverter;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleService roleService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public IPage<UserPageVO> getUserPage(UserPageQuery queryParams) {
        int pageNum = queryParams.getPageNum();
        int pageSize = queryParams.getPageSize();
        Page<UserBO> page = new Page<>(pageNum, pageSize);

        Page<UserBO> userBOPage = this.baseMapper.getUserPage(page, queryParams);

        Page<UserPageVO> userPageVOPage = userConverter.bo2Vo(userBOPage);
        return userPageVOPage;
    }

    @Override
    public UserForm getUserFormData(Long userId) {
        UserFormBO userFormBO = this.baseMapper.getUserDetail(userId);
        // 实体转换po->form
        UserForm userForm = userConverter.bo2Form(userFormBO);
        return userForm;
    }

    @Override
    public boolean saveUser(UserForm userForm) {
        String username = userForm.getUsername();

        long count = this.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        Assert.isTrue(count == 0, "用户名已存在");

        // 实体转换 form->entity
        SysUser entity = userConverter.form2Entity(userForm);

        // 设置默认加密密码
        String defaultEncryptPwd = passwordEncoder.encode(SystemConstants.DEFAULT_PASSWORD);
        entity.setPassword(defaultEncryptPwd);

        // 新增用户
        boolean result = this.save(entity);

        if (result) {
            // 保存用户角色
            userRoleService.saveUserRoles(entity.getId(), userForm.getRoleIds());
        }
        return result;
    }

    @Override
    public boolean updateUser(Long userId, UserForm userForm) {
        String username = userForm.getUsername();

        long count = this.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .ne(SysUser::getId, userId)
        );
        Assert.isTrue(count == 0, "用户名已存在");

        // form -> entity
        SysUser entity = userConverter.form2Entity(userForm);

        // 修改用户
        boolean result = this.updateById(entity);

        if (result) {
            // 保存用户角色
            userRoleService.saveUserRoles(entity.getId(), userForm.getRoleIds());
        }
        return result;
    }

    @Override
    public boolean deleteUsers(String idsStr) {
        Assert.isTrue(StrUtil.isNotBlank(idsStr), "删除的用户数据为空");
        // 逻辑删除
        List<Long> ids = Arrays.asList(idsStr.split(",")).stream()
                .map(Long::parseLong)
                .toList();
        boolean result = this.removeByIds(ids);
        return result;
    }

    @Override
    public boolean updatePassword(Long userId, String password) {
        return this.update(new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, userId)
                .set(SysUser::getPassword, passwordEncoder.encode(password))
        );
    }

    @Override
    public UserAuthInfo getUserAuthInfo(String username) {
        UserAuthInfo userAuthInfo = this.baseMapper.getUserAuthInfo(username);
        if (userAuthInfo != null) {
            Set<String> roles = userAuthInfo.getRoles();
            if (CollectionUtil.isNotEmpty(roles)) {
                Set<String> perms = sysMenuService.listRolePerms(roles);
                userAuthInfo.setPerms(perms);
            }

            // 获取最大范围的数据权限
            Integer dataScope = roleService.getMaximumDataScope(roles);
            userAuthInfo.setDataScope(dataScope);
        }
        return userAuthInfo;
    }

    @Override
    public List<UserExportVO> listExportUsers(UserPageQuery queryParams) {
        List<UserExportVO> list = this.baseMapper.listExportUsers(queryParams);
        return list;
    }

    @Override
    public UserInfoVO getUserLoginInfo() {
        // 登录用户entity
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, SecurityUtils.getUser().getUsername())
                .select(
                        SysUser::getId,
                        SysUser::getNickname,
                        SysUser::getAvatar
                )
        );
        // entity->VO
        UserInfoVO userInfoVO = userConverter.entity2UserInfoVo(user);

        // 用户角色集合
        Set<String> roles = SecurityUtils.getRoles();
        userInfoVO.setRoles(roles);

        // 用户权限集合
        Set<String> perms = (Set<String>) redisTemplate.opsForValue().get(SecurityConstants.USER_PERMS_CACHE_PREFIX+ user.getId());
        userInfoVO.setPerms(perms);

        return userInfoVO;
    }
}
