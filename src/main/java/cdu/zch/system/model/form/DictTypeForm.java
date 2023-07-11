package cdu.zch.system.model.form;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Schema(description = "字典类型")
@Data
public class DictTypeForm {

    @Schema(description="字典类型ID")
    private Long id;

    @Schema(description="类型名称")
    private String name;

    @Schema(description="类型编码")
    private String code;

    @Schema(description="类型状态(1:启用;0:禁用)")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}
