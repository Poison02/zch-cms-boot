package cdu.zch.system.common.enums;

import cdu.zch.system.common.base.IBaseEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author Zch
 * @date 2023/7/10
 **/
@Getter
@Schema(enumAsRef = true)
public enum GenderEnum implements IBaseEnum<Integer> {

    MALE(1, "男"),
    FEMALE(2, "女");

    private Integer value;

    private String label;

    GenderEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
