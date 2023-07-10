package cdu.zch.system.model.bo;

import lombok.Data;

import java.util.List;

/**
 * 用户表单持久化对象
 * @author Zch
 * @date 2023/7/10
 **/
@Data
public class UserFormBO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 性别(1:男;2:女)
     */
    private Integer gender;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 状态(1:启用;0:禁用)
     */
    private Integer status;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 角色ID集合
     */
    private List<Long> roleIds;

}