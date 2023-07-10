package cdu.zch.system.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

/**
 * @author Zch
 * @date 2023/7/10
 **/
@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> filterRegistrationBean() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许任何来源 *
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        // 允许任何请求头
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        // 允许任何方法
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        // 允许凭证
        corsConfiguration.setAllowCredentials(true);

        // 将所有URL都在注册为可跨域，且对跨域请求进行过滤处理
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        CorsFilter corsFilter = new CorsFilter(source);

        FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<>(corsFilter);
        // 设计过滤链的优先级，数字越小优先级越高
        filterRegistrationBean.setOrder(-101);

        return filterRegistrationBean;
    }

}
