package cdu.zch.system.common.annotation;

import java.lang.annotation.*;

/**
 * @author Zch
 * @date 2023/7/10
 **/
// 指定注解的使用目标为方法
@Target(ElementType.METHOD)
// 指定注解在运行时可见
@Retention(RetentionPolicy.RUNTIME)
// 该注解包含在生成的API文档中
@Documented
// 该注解可被继承
@Inherited
public @interface PreventDuplicateSubmit {

    /**
     * 防重复提交锁过期时间（秒）
     * 默认5秒内不允许重复提交
     * @return int
     */
    int expire() default 5;

}
