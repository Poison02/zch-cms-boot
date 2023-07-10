package cdu.zch.system.model.dto;

import lombok.Data;

import java.util.Set;

/**
 * @author Zch
 * @date 2023/7/10
 **/
@Data
public class UserAuthInfo {

    private Long userId;

    private String username;

    private String nickname;

    private Long deptId;

    private String password;

    private Integer status;

    private Set<String> roles;

    private Set<String> perms;

    private Integer dataScope;

}
