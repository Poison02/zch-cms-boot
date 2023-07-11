package cdu.zch.system.controller;

import cdu.zch.system.common.annotation.PreventDuplicateSubmit;
import cdu.zch.system.common.constant.ExcelConstants;
import cdu.zch.system.common.result.PageResult;
import cdu.zch.system.common.result.Result;
import cdu.zch.system.common.util.ExcelUtils;
import cdu.zch.system.listener.easyexcel.UserImportListener;
import cdu.zch.system.model.entity.SysUser;
import cdu.zch.system.model.form.UserForm;
import cdu.zch.system.model.query.UserPageQuery;
import cdu.zch.system.model.vo.UserExportVO;
import cdu.zch.system.model.vo.UserImportVO;
import cdu.zch.system.model.vo.UserInfoVO;
import cdu.zch.system.model.vo.UserPageVO;
import cdu.zch.system.service.SysUserService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Tag(name = "02-用户接口")
@RestController
@RequestMapping("/api/v1/users")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @Operation(summary = "用户分页列表", security = {@SecurityRequirement(name = "Authentication")})
    @GetMapping("/page")
    public PageResult<UserPageVO> getUserPage(
            @ParameterObject UserPageQuery queryParams
    ) {
        IPage<UserPageVO> result = sysUserService.getUserPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增用户", security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping
    @PreAuthorize("@ss.hasPerm('sys:user:add')")
    @PreventDuplicateSubmit
    public Result saveUser(
            @RequestBody @Valid UserForm userForm
    ) {
        boolean result = sysUserService.saveUser(userForm);
        return Result.judge(result);
    }

    @Operation(summary = "用户表单数据", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/{userId}/form")
    public Result<UserForm> getUserForm(
            @Parameter(description = "用户ID") @PathVariable Long userId
    ) {
        UserForm formData = sysUserService.getUserFormData(userId);
        return Result.success(formData);
    }

    @Operation(summary = "修改用户", security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping(value = "/{userId}")
    @PreAuthorize("@ss.hasPerm('sys:user:edit')")
    public Result updateUser(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @RequestBody @Validated UserForm userForm) {
        boolean result = sysUserService.updateUser(userId, userForm);
        return Result.judge(result);
    }

    @Operation(summary = "删除用户", security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:user:delete')")
    public Result deleteUsers(
            @Parameter(description = "用户ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = sysUserService.deleteUsers(ids);
        return Result.judge(result);
    }

    @Operation(summary = "修改用户密码", security = {@SecurityRequirement(name = "Authorization")})
    @PatchMapping(value = "/{userId}/password")
    @PreAuthorize("@ss.hasPerm('sys:user:reset_pwd')")
    public Result updatePassword(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @RequestParam String password
    ) {
        boolean result = sysUserService.updatePassword(userId, password);
        return Result.judge(result);
    }

    @Operation(summary = "修改用户状态", security = {@SecurityRequirement(name = "Authorization")})
    @PatchMapping(value = "/{userId}/status")
    public Result updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "用户状态(1:启用;0:禁用)") @RequestParam Integer status
    ) {
        boolean result = sysUserService.update(new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, userId)
                .set(SysUser::getStatus, status)
        );
        return Result.judge(result);
    }

    @Operation(summary = "获取当前登录用户信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/me")
    public Result<UserInfoVO> getUserLoginInfo() {
        UserInfoVO userInfoVO = sysUserService.getUserLoginInfo();
        return Result.success(userInfoVO);
    }

    @Operation(summary = "用户导入模板下载", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        String fileName = "用户导入模板.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        String fileClassPath = ExcelConstants.EXCEL_TEMPLATE_DIR + File.separator + fileName;
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileClassPath);

        ServletOutputStream outputStream = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(outputStream).withTemplate(inputStream).build();

        excelWriter.finish();
    }

    @Operation(summary = "导入用户", security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping("/_import")
    public Result importUsers(@Parameter(description = "部门ID") Long deptId, MultipartFile file) throws IOException {
        UserImportListener listener = new UserImportListener(deptId);
        String msg = ExcelUtils.importExcel(file.getInputStream(), UserImportVO.class, listener);
        return Result.success(msg);
    }

    @Operation(summary = "导出用户", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/_export")
    public void exportUsers(UserPageQuery queryParams, HttpServletResponse response) throws IOException {
        String fileName = "用户列表.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        List<UserExportVO> exportUserList = sysUserService.listExportUsers(queryParams);
        EasyExcel.write(response.getOutputStream(), UserExportVO.class).sheet("用户列表")
                .doWrite(exportUserList);
    }

}
