package cdu.zch.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * 用户登录视图对象
 * @author Zch
 * @date 2023/7/10
 **/
@Data
@Schema(description = "当前登录用户视图对象")
public class UserInfoVO {

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="用户昵称")
    private String nickname;

    @Schema(description="头像地址")
    private String avatar;

    @Schema(description="用户角色编码集合")
    private Set<String> roles;

    @Schema(description="用户权限标识集合")
    private Set<String> perms;

}