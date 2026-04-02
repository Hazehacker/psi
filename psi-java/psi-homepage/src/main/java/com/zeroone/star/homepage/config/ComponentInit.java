package com.zeroone.star.homepage.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
/**
 * <p>
 * 描述：组件初始化
 * </p>
 *
 * @author heavydrink
 * @version 1.0.0
 */
@Configuration
@ComponentScan("com.zeroone.star.project.config.redis")
@ComponentScan("com.zeroone.star.project.components.user")
@ComponentScan("com.zeroone.star.project.components.jwt")
public class ComponentInit {
}
