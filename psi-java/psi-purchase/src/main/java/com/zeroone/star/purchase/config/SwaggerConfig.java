package com.zeroone.star.purchase.config;

import com.zeroone.star.project.config.swagger.SwaggerCore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * <p>
 * 描述：Swagger配置
 * </p>
 * @author Hazenix 
 * @version 1.0.0
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {
    @Bean
    Docket HomepageApi() {
        return SwaggerCore.defaultDocketBuilder("采购管理",
                "com.zeroone.star.purchase.controller", "采购管理");
    }
}

