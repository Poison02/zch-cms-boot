package cdu.zch.system.config;

import cdu.zch.system.common.constant.SecurityConstants;
import cdu.zch.system.filter.JwtAuthenticationFilter;
import cdu.zch.system.filter.VerifyCodeFilter;
import cdu.zch.system.security.JwtTokenManager;
import cdu.zch.system.security.exception.MyAccessDeniedHandler;
import cdu.zch.system.security.exception.MyAuthenticationEntryPoint;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security配置类
 * @author Zch
 * @date 2023/7/10
 **/
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Resource
    private MyAuthenticationEntryPoint authenticationEntryPoint;

    @Resource
    private MyAccessDeniedHandler accessDeniedHandler;

    @Resource
    private JwtTokenManager jwtTokenManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 配置请求授权规则
        http.authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                        // 允许登录请求路径
                        .requestMatchers(SecurityConstants.LOGIN_PATH).permitAll()
                        // 除了登录允许，其他请求都需要进行身份认证
                        .anyRequest().authenticated()
            )
            // 配置会话管理
            .sessionManagement(configurer -> configurer
                    // 不创建会话，无状态的身份认证，为了后面使用Token认证
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 异常处理
            .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                    // 自定义的认证入口点
                    .authenticationEntryPoint(authenticationEntryPoint)
                    // 自定义的认证访问拒绝处理器
                    .accessDeniedHandler(accessDeniedHandler)
            )
            // 禁用csrf
            .csrf(AbstractHttpConfigurer::disable);
        // 添加过滤器1，在处理用户名密码之前处理验证码的过滤器
        http.addFilterBefore(new VerifyCodeFilter(), UsernamePasswordAuthenticationFilter.class);
        // 添加过滤器2，在处理用户名密码之前处理JWT令牌的认证和授权
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenManager), UsernamePasswordAuthenticationFilter.class);

        // 构建并返回过滤器链
        return http.build();
    }

    /**
     * 忽略一些不进行认证的请求
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring()
                .requestMatchers(
                        "/api/v1/auth/captcha",
                        "/webjars/**",
                        "/doc.html",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/ws/**"
                ));
    }

    /**
     * 密码编码，使用BCryptPasswordEncoder
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 注入身份认证管理器
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
