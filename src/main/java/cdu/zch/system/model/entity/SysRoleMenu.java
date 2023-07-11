package cdu.zch.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Zch
 * @date 2023/7/11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleMenu  {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}