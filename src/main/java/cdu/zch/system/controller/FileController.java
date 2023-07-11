package cdu.zch.system.controller;

import cdu.zch.system.common.result.Result;
import cdu.zch.system.model.dto.FileInfo;
import cdu.zch.system.service.OssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Tag(name = "07.文件接口")
@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    @Resource
    private OssService ossService;

    @PostMapping
    @Operation(summary = "文件上传", security = {@SecurityRequirement(name = "Authorization")})
    public Result<FileInfo> uploadFile(
            @Parameter(description ="表单文件对象") @RequestParam(value = "file") MultipartFile file
    ) {
        FileInfo fileInfo = ossService.uploadFile(file);
        return Result.success(fileInfo);
    }

    @DeleteMapping
    @Operation(summary = "文件删除", security = {@SecurityRequirement(name = "Authorization")})
    @SneakyThrows
    public Result deleteFile(
            @Parameter(description ="文件路径") @RequestParam String filePath
    ) {
        boolean result = ossService.deleteFile(filePath);
        return Result.judge(result);
    }
}
