package cdu.zch.system.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 下拉选项对象
 * @author Zch
 * @date 2023/7/10
 **/
@Schema(description = "下拉选项对象")
@Data
@NoArgsConstructor
public class Option<T> {

    @Schema(description = "选项的值")
    private T value;

    @Schema(description = "选项的标签")
    private String label;

    @Schema(description = "子选项列表")
    private List<Option> children;

    public Option(T value, String label) {
        this.value = value;
        this.label = label;
    }

    public Option(T value, String label, List<Option> children) {
        this.value = value;
        this.label = label;
        this.children = children;
    }
}