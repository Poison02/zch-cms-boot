package cdu.zch.system.common.annotation;

import java.lang.annotation.*;

/**
 * Mybatis-Plus数据权限注解
 * @author Zch
 * @date 2023/7/10
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DataPermission {

    String deptAlias() default "";

    String deptIdColumnName() default "dept_id";

    String userAlias() default "";

    String userIdColumnName() default "create_by";

}
