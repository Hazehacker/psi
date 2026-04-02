package com.zeroone.star.payment_order;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>
 * 描述：程序启动入口
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@SpringBootApplication
@MapperScan("com.zeroone.star.payment_order.mapper")// 扫描payment_order模块下的所有Mapper接口
@ComponentScan(basePackages = {
        "com.zeroone.star.project.components.easyexcel",  // 包含EasyExcelComponent的包
        "com.zeroone.star.payment_order"       // 项目自身包
})
public class payment_orderApplication {


    public static void main(String[] args) {
        SpringApplication.run(payment_orderApplication.class, args);
    }

}
