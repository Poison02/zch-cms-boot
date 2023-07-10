package cdu.zch.system.common.enums;

import cdu.zch.system.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @author Zch
 * @date 2023/7/10
 **/
@Getter
public enum DataScopeEnum implements IBaseEnum<Integer> {

    /**
     * value越小，数据范围权限越大
     */
    ALL(0, "所有数据"),
    DEPT_AND_SUB(1, "部门及子部门数据"),
    DEPT(2, "本部门数据"),
    SELF(3, "本人数据");

    private Integer value;

    private String label;

    DataScopeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
