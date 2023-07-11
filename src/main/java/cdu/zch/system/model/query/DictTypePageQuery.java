package cdu.zch.system.model.query;


import cdu.zch.system.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Schema(description ="字典类型分页查询对象")
@Data
public class DictTypePageQuery extends BasePageQuery {

    @Schema(description="关键字(类型名称/类型编码)")
    private String keywords;

}
