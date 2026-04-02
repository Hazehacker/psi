package com.zeroone.star.supportinfo;

import com.zeroone.star.project.config.mybatis.MybatisPlusConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;


/**
 * <p>
 * 描述：服务器启动入口
 * </p>
 * @author star
 * @version 1.0.0
 */


@SpringBootApplication
@EnableDiscoveryClient
@Import(MybatisPlusConfig.class)
public class SupportinfoTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupportinfoTestApplication.class, args);
    }

}