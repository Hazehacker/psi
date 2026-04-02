package com.zeroone.star.report.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 描述：初始化自定义组件
 * </p>
 * @author Hazenix 
 * @version 1.0.0
 */
@Configuration
@ComponentScan({
        "com.zeroone.star.project.components.fastdfs",
        "com.zeroone.star.project.components.easyexcel"
})
public class StockBalanceComponentsInit {
}
