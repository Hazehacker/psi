package com.zeroone.star.sysconfig;

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

@SpringBootApplication(scanBasePackages = {
        "com.zeroone.star.sysconfig",
        "com.zeroone.star.project.config",
        "com.zeroone.star.project.components.fastdfs"
})
public class SysconfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysconfigApplication.class, args);
    }

}
