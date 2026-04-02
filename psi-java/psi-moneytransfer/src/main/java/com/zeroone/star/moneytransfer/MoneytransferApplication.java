package com.zeroone.star.moneytransfer;

import com.zeroone.star.project.config.mybatis.MybatisPlusConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;




@Import(MybatisPlusConfig.class)
@SpringBootApplication
public class MoneytransferApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoneytransferApplication.class, args);
    }

}
