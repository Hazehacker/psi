package com.zeroone.star.report.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 注册 JSR-310 日期时间格式转换器（支持 LocalDate、LocalDateTime 等）
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        // 设置 LocalDate 的默认解析格式为 yyyy-MM-dd
        registrar.setDateFormatter(DateTimeFormatter.ISO_LOCAL_DATE); // ISO格式即 yyyy-MM-dd
        registrar.registerFormatters(registry);
    }
}