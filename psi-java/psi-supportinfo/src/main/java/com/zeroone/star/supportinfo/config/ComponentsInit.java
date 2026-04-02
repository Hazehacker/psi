package com.zeroone.star.supportinfo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/*
 *Author:kunge
 */
@Configuration
@ComponentScan({
        "com.zeroone.star.project.components.easyexcel",
        "com.zeroone.star.project.components.fastdfs"
})
public class ComponentsInit {
}
