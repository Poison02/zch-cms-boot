package cdu.zch.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cdu.zch.system.common.annotation.PreventDuplicateSubmit;
import cdu.zch.system.common.model.Option;
import cdu.zch.system.common.result.PageResult;
import cdu.zch.system.common.result.Result;
import cdu.zch.system.model.form.DictForm;
import cdu.zch.system.model.form.DictTypeForm;
import cdu.zch.system.model.query.DictPageQuery;
import cdu.zch.system.model.query.DictTypePageQuery;
import cdu.zch.system.model.vo.DictPageVO;
import cdu.zch.system.model.vo.DictTypePageVO;
import cdu.zch.system.service.SysDictService;
import cdu.zch.system.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Tag(name = "06.字典接口")
@RestController
@RequestMapping("/api/v1/dict")
public class SysDictController {

    @Resource
    private SysDictService dictService;

    @Resource
    private SysDictTypeService dictTypeService;

    @Operation(summary = "字典分页列表", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/page")
    public PageResult<DictPageVO> getDictPage(
            @ParameterObject DictPageQuery queryParams
    ) {
        Page<DictPageVO> result = dictService.getDictPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "字典数据表单数据", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/{id}/form")
    public Result<DictForm> getDictForm(
            @Parameter(description ="字典ID") @PathVariable Long id
    ) {
        DictForm formData = dictService.getDictForm(id);
        return Result.success(formData);
    }

    @Operation(summary = "新增字典", security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping
    @PreAuthorize("@ss.hasPerm('sys:dict:add')")
    @PreventDuplicateSubmit
    public Result saveDict(
            @RequestBody DictForm DictForm
    ) {
        boolean result = dictService.saveDict(DictForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改字典", security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping("/{id}")
    @PreAuthorize("@ss.hasPerm('sys:dict:edit')")
    public Result updateDict(
            @PathVariable Long id,
            @RequestBody DictForm DictForm
    ) {
        boolean status = dictService.updateDict(id, DictForm);
        return Result.judge(status);
    }

    @Operation(summary = "删除字典", security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:dict:delete')")
    public Result deleteDict(
            @Parameter(description ="字典ID，多个以英文逗号(,)拼接") @PathVariable String ids
    ) {
        boolean result = dictService.deleteDict(ids);
        return Result.judge(result);
    }


    @Operation(summary = "字典下拉列表", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/options")
    public Result<List<Option>> listDictOptions(
            @Parameter(description ="字典类型编码") @RequestParam String typeCode
    ) {
        List<Option> list = dictService.listDictOptions(typeCode);
        return Result.success(list);
    }


    /*----------------------------------------------------*/
    @Operation(summary = "字典类型分页列表", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/types/page")
    public PageResult<DictTypePageVO> getDictTypePage(
            @ParameterObject DictTypePageQuery queryParams
    ) {
        Page<DictTypePageVO> result = dictTypeService.getDictTypePage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "字典类型表单数据", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/types/{id}/form")
    public Result<DictTypeForm> getDictTypeForm(
            @Parameter(description ="字典ID") @PathVariable Long id
    ) {
        DictTypeForm dictTypeForm = dictTypeService.getDictTypeForm(id);
        return Result.success(dictTypeForm);
    }

    @Operation(summary = "新增字典类型", security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping("/types")
    @PreAuthorize("@ss.hasPerm('sys:dict_type:add')")
    @PreventDuplicateSubmit
    public Result saveDictType(@RequestBody DictTypeForm dictTypeForm) {
        boolean result = dictTypeService.saveDictType(dictTypeForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改字典类型", security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping("/types/{id}")
    @PreAuthorize("@ss.hasPerm('sys:dict_type:edit')")
    public Result updateDictType(@PathVariable Long id, @RequestBody DictTypeForm dictTypeForm) {
        boolean status = dictTypeService.updateDictType(id, dictTypeForm);
        return Result.judge(status);
    }

    @Operation(summary = "删除字典类型", security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping("/types/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:dict_type:delete')")
    public Result deleteDictTypes(
            @Parameter(description ="字典类型ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = dictTypeService.deleteDictTypes(ids);
        return Result.judge(result);
    }

}
