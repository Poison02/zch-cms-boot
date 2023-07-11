package cdu.zch.system.controller;

import cdu.zch.system.common.annotation.PreventDuplicateSubmit;
import cdu.zch.system.common.model.Option;
import cdu.zch.system.common.result.Result;
import cdu.zch.system.model.form.MenuForm;
import cdu.zch.system.model.query.MenuQuery;
import cdu.zch.system.model.vo.MenuVO;
import cdu.zch.system.model.vo.RouteVO;
import cdu.zch.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Tag(name = "04-菜单接口")
@RestController
@RequestMapping("/api/v1/menus")
@Slf4j
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;

    @Operation(summary = "菜单列表",security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping
    public Result<List<MenuVO>> listMenus(@ParameterObject MenuQuery queryParams) {
        List<MenuVO> menuList = sysMenuService.listMenus(queryParams);
        return Result.success(menuList);
    }

    @Operation(summary = "菜单下拉列表",security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/options")
    public Result listMenuOptions() {
        List<Option> menus = sysMenuService.listMenuOptions();
        return Result.success(menus);
    }

    @Operation(summary = "路由列表",security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/routes")
    public Result<List<RouteVO>> listRoutes() {
        List<RouteVO> routeList = sysMenuService.listRoutes();
        return Result.success(routeList);
    }

    @Operation(summary = "菜单表单数据",security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/{id}/form")
    public Result<MenuForm> getMenuForm(
            @Parameter(description =  "菜单ID") @PathVariable Long id
    ) {
        MenuForm menu = sysMenuService.getMenuForm(id);
        return Result.success(menu);
    }

    @Operation(summary = "新增菜单",security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping
    @PreAuthorize("@ss.hasPerm('sys:menu:add')")
    @PreventDuplicateSubmit
    @CacheEvict(cacheNames = "system", key = "'routes'")
    public Result addMenu(@RequestBody MenuForm menuForm) {
        boolean result = sysMenuService.saveMenu(menuForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改菜单",security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('sys:menu:edit')")
    @CacheEvict(cacheNames = "system", key = "'routes'")
    public Result updateMenu(
            @RequestBody MenuForm menuForm
    ) {
        boolean result = sysMenuService.saveMenu(menuForm);
        return Result.judge(result);
    }

    @Operation(summary = "删除菜单",security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPerm('sys:menu:delete')")
    @CacheEvict(cacheNames = "system", key = "'routes'")
    public Result deleteMenu(
            @Parameter(description ="菜单ID，多个以英文(,)分割") @PathVariable("id") Long id
    ) {
        boolean result = sysMenuService.deleteMenu(id);
        return Result.judge(result);
    }

    @Operation(summary = "修改菜单显示状态",security = {@SecurityRequirement(name = "Authorization")})
    @PatchMapping("/{menuId}")
    public Result updateMenuVisible(
            @Parameter(description =  "菜单ID") @PathVariable Long menuId,
            @Parameter(description =  "显示状态(1:显示;0:隐藏)") Integer visible

    ) {
        boolean result =sysMenuService.updateMenuVisible(menuId, visible);
        return Result.judge(result);
    }

}
