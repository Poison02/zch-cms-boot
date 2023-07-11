package cdu.zch.system.service.impl;

import cdu.zch.system.common.constant.SystemConstants;
import cdu.zch.system.common.model.Option;
import cdu.zch.system.common.util.SecurityUtils;
import cdu.zch.system.converter.RoleConverter;
import cdu.zch.system.mapper.SysRoleMapper;
import cdu.zch.system.model.entity.SysRole;
import cdu.zch.system.model.entity.SysUserRole;
import cdu.zch.system.model.entity.SysRoleMenu;
import cdu.zch.system.model.form.RoleForm;
import cdu.zch.system.model.query.RolePageQuery;
import cdu.zch.system.model.vo.RolePageVO;
import cdu.zch.system.service.SysRoleMenuService;
import cdu.zch.system.service.SysRoleService;
import cdu.zch.system.service.SysUserRoleService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRoleMenuService sysRoleMenuService;
    @Resource
    private SysUserRoleService sysUserRoleService;
    @Resource
    private RoleConverter roleConverter;

    @Override
    public Page<RolePageVO> getRolePage(RolePageQuery queryParams) {
        // 查询参数
        int pageNum = queryParams.getPageNum();
        int pageSize = queryParams.getPageSize();
        String keywords = queryParams.getKeywords();

        // 查询数据
        Page<SysRole> rolePage = this.page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<SysRole>()
                        .and(StrUtil.isNotBlank(keywords),
                                wrapper ->
                                        wrapper.like(StrUtil.isNotBlank(keywords), SysRole::getName, keywords)
                                                .or()
                                                .like(StrUtil.isNotBlank(keywords), SysRole::getCode, keywords)
                        )
                        // 非超级管理员不显示超级管理员角色
                        .ne(!SecurityUtils.isRoot(), SysRole::getCode, SystemConstants.ROOT_ROLE_CODE)
        );

        // 实体转换
        Page<RolePageVO> pageResult = roleConverter.entity2Page(rolePage);
        return pageResult;
    }

    @Override
    public List<Option> listRoleOptions() {
        // 查询数据
        List<SysRole> roleList = this.list(new LambdaQueryWrapper<SysRole>()
                .ne(!SecurityUtils.isRoot(), SysRole::getCode, SystemConstants.ROOT_ROLE_CODE)
                .select(SysRole::getId, SysRole::getName)
                .orderByAsc(SysRole::getSort)
        );

        // 实体转换
        List<Option> list = roleConverter.entities2Options(roleList);
        return list;
    }

    @Override
    public boolean saveRole(RoleForm roleForm) {
        Long roleId = roleForm.getId();
        String roleCode = roleForm.getCode();

        long count = this.count(new LambdaQueryWrapper<SysRole>()
                .ne(roleId != null, SysRole::getId, roleId)
                .and(wrapper ->
                        wrapper.eq(SysRole::getCode, roleCode).or().eq(SysRole::getName, roleCode)
                ));
        Assert.isTrue(count == 0, "角色名称或角色编码重复，请检查！");

        // 实体转换
        SysRole role = roleConverter.form2Entity(roleForm);

        boolean result = this.saveOrUpdate(role);
        return result;
    }

    @Override
    public RoleForm getRoleForm(Long roleId) {
        SysRole entity = this.getById(roleId);
        RoleForm roleForm = roleConverter.entity2Form(entity);
        return roleForm;
    }

    @Override
    public boolean updateRoleStatus(Long roleId, Integer status) {
        boolean result = this.update(new LambdaUpdateWrapper<SysRole>()
                .eq(SysRole::getId, roleId)
                .set(SysRole::getStatus, status));
        return result;
    }

    @Override
    public boolean deleteRoles(String ids) {
        List<Long> roleIds = Arrays.asList(ids.split(",")).stream().map(id -> Long.parseLong(id)).collect(Collectors.toList());
        Optional.ofNullable(roleIds)
                .orElse(new ArrayList<>())
                .forEach(id -> {
                    long count = sysUserRoleService.count(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, id));
                    Assert.isTrue(count <= 0, "该角色已分配用户，无法删除");
                    sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
                });


        boolean result = this.removeByIds(roleIds);
        return result;
    }

    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        List<Long> menuIds = sysRoleMenuService.listMenuIdsByRoleId(roleId);
        return menuIds;
    }

    @Override
    public boolean updateRoleMenus(Long roleId, List<Long> menuIds) {
        // 删除角色菜单
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        // 新增角色菜单
        if (CollectionUtil.isNotEmpty(menuIds)) {
            List<SysRoleMenu> roleMenus = menuIds.stream()
                    .map(menuId -> new SysRoleMenu(roleId, menuId))
                    .collect(Collectors.toList());
            sysRoleMenuService.saveBatch(roleMenus);
        }
        return true;
    }

    @Override
    public Integer getMaximumDataScope(Set<String> roles) {
        Integer dataScope = this.baseMapper.getMaximumDataScope(roles);
        return dataScope;
    }
}
