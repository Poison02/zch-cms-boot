package cdu.zch.system.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zch
 * @date 2023/7/10
 **/
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Authorization",
                            new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer").bearerFormat("JWT")
                        )
                )
                .info(new Info()
                        .title("后台管理系统 API文档")
                        .version("1.0.0")
                        .description("API文档")
                        .license(new License().name("Apache 2.0")
                                .url("http://localhost:8989")
                        )
                );
    }

    @Bean
    public GroupedOpenApi systemApi() {
        String[] paths = {"/**"};
        String[] packagesToScan = {"cdu.zch.system.controller"};
        return GroupedOpenApi.builder()
                .group("系统接口")
                .packagesToScan(packagesToScan)
                .pathsToMatch(paths)
                .build();
    }

}
