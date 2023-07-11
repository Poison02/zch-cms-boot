package cdu.zch.system.controller;

import cdu.zch.system.common.annotation.PreventDuplicateSubmit;
import cdu.zch.system.common.model.Option;
import cdu.zch.system.common.result.PageResult;
import cdu.zch.system.common.result.Result;
import cdu.zch.system.model.form.RoleForm;
import cdu.zch.system.model.query.RolePageQuery;
import cdu.zch.system.model.vo.RolePageVO;
import cdu.zch.system.service.SysRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Tag(name = "03-角色接口")
@RestController
@RequestMapping("/api/v1/roles")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @Operation(summary = "角色分页列表",security = {@SecurityRequirement(name = "Authorization")} )
    @GetMapping("/page")
    public PageResult<RolePageVO> getRolePage(
            @ParameterObject RolePageQuery queryParams
    ) {
        Page<RolePageVO> result = sysRoleService.getRolePage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "角色下拉列表",security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/options")
    public Result<List<Option>> listRoleOptions() {
        List<Option> list = sysRoleService.listRoleOptions();
        return Result.success(list);
    }

    @Operation(summary = "新增角色",security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping
    @PreAuthorize("@ss.hasPerm('sys:role:add')")
    @PreventDuplicateSubmit
    public Result addRole(@Valid @RequestBody RoleForm roleForm) {
        boolean result = sysRoleService.saveRole(roleForm);
        return Result.judge(result);
    }

    @Operation(summary = "角色表单数据",security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/{roleId}/form")
    public Result<RoleForm> getRoleForm(
            @Parameter(description ="角色ID") @PathVariable Long roleId
    ) {
        RoleForm roleForm = sysRoleService.getRoleForm(roleId);
        return Result.success(roleForm);
    }

    @Operation(summary = "修改角色",security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('sys:role:edit')")
    public Result updateRole(@Valid @RequestBody RoleForm roleForm) {
        boolean result = sysRoleService.saveRole(roleForm);
        return Result.judge(result);
    }

    @Operation(summary = "删除角色",security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:role:delete')")
    public Result deleteRoles(
            @Parameter(description ="删除角色，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = sysRoleService.deleteRoles(ids);
        return Result.judge(result);
    }

    @Operation(summary = "修改角色状态",security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping(value = "/{roleId}/status")
    public Result updateRoleStatus(
            @Parameter(description ="角色ID") @PathVariable Long roleId,
            @Parameter(description ="状态(1:启用;0:禁用)") @RequestParam Integer status
    ) {
        boolean result = sysRoleService.updateRoleStatus(roleId, status);
        return Result.judge(result);
    }

    @Operation(summary = "获取角色的菜单ID集合",security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/{roleId}/menuIds")
    public Result<List<Long>> getRoleMenuIds(
            @Parameter(description ="角色ID") @PathVariable Long roleId
    ) {
        List<Long> menuIds = sysRoleService.getRoleMenuIds(roleId);
        return Result.success(menuIds);
    }

    @Operation(summary = "分配菜单权限给角色",security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping("/{roleId}/menus")
    public Result updateRoleMenus(
            @PathVariable Long roleId,
            @RequestBody List<Long> menuIds
    ) {
        boolean result = sysRoleService.updateRoleMenus(roleId,menuIds);
        return Result.judge(result);
    }

}
