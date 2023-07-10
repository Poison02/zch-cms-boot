package cdu.zch.system.common.enums;

import cdu.zch.system.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @author Zch
 * @date 2023/7/10
 **/
@Getter
public enum StatusEnum implements IBaseEnum<Integer> {
    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    private Integer value;

    private String label;

    StatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
