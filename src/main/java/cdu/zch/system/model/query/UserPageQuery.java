package cdu.zch.system.model.query;

import cdu.zch.system.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Zch
 * @date 2023/7/11
 **/
@Schema 
@Data
public class UserPageQuery extends BasePageQuery {

    @Schema(description="关键字(用户名/昵称/手机号)")
    private String keywords;

    @Schema(description="用户状态")
    private Integer status;

    @Schema(description="部门ID")
    private Long deptId;

}
