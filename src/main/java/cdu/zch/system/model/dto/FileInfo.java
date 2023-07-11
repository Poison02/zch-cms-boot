package cdu.zch.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Schema(description = "文件对象")
@Data
public class FileInfo {

    @Schema(description = "文件名称")
    private String name;

    @Schema(description = "文件URL")
    private String url;

}
