package com.zeroone.star.fund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p>
 * 描述：程序启动入口
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class FundApplication {

    public static void main(String[] args) {
        // 设置默认的服务名称和服务端口，防止启动时报错
        System.setProperty("sn.fund", "fund");
        System.setProperty("sp.fund", "10600");
        SpringApplication.run(FundApplication.class, args);
    }

}