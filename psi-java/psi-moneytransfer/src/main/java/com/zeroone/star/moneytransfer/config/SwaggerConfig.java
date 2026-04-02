package com.zeroone.star.moneytransfer.config;

import com.zeroone.star.project.config.swagger.SwaggerCore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * 类名：SwaggerConfig
 * 包名：com.zeroone.star.supportinfo.config
 * 描述：
 * 作者：hh1
 * 创建日期：2025/10/21
 * 版本号：V1.0
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {
    @Bean
    Docket sampleApi() {
        return SwaggerCore.defaultDocketBuilder("转账单",
                "com.zeroone.star.moneytransfer.controller", "moneytransfer");
    }
}