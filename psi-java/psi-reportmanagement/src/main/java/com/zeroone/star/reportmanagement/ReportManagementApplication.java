package com.zeroone.star.reportmanagement;

import com.zeroone.star.project.config.mybatis.MybatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * @author yu
 * @date 2025/10/18
 */
@EnableDiscoveryClient
@SpringBootApplication
@Import(MybatisPlusConfig.class)
@MapperScan("com.zeroone.star.reportmanagement.mapper")
public class ReportManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportManagementApplication.class, args);
    }

}
