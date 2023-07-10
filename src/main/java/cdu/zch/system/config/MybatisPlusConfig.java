package cdu.zch.system.config;

import cdu.zch.system.handler.mybatisplus.MyDataPermissionHandler;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Zch
 * @date 2023/7/10
 **/
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {

    @Value("${system.config.data-permission-enabled}")
    private Boolean dataPermissionEnabled;

    /**
     * 分页插件和数据权限插件
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 数据权限
        if (dataPermissionEnabled) {
            interceptor.addInnerInterceptor(new DataPermissionInterceptor(new MyDataPermissionHandler()));;
        }
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
