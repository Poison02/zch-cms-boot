package cdu.zch.system.common.enums;

import cdu.zch.system.common.base.IBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Zch
 * @date 2023/7/10
 **/
@Getter
public enum MenuTypeEnum implements IBaseEnum<Integer> {

    NULL(0, null),
    MENU(1, "菜单"),
    CATALOG(2, "目录"),
    EXTERNAL_LINK(3, "外链"),
    BUTTON(4, "按钮");

    @EnumValue // 插入数据库时插入该值
    private Integer value;

    private String label;

    MenuTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
