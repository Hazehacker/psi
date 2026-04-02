package com.zeroone.star.payment_order.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.zeroone.star.project.config.mybatis")
public class MpConfig {
    //注入自动填充句柄
    @Bean
    public MetaObjectHandler metaObjectHandler(){
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject,"frame",String.class,"默认组织");
                this.strictInsertFill(metaObject,"user",String.class,"管理员");
                this.strictInsertFill(metaObject,"examine",Integer.class,0);
                this.strictInsertFill(metaObject,"nucleus",Integer.class,0);
            }

            @Override
            public void updateFill(MetaObject metaObject) {

            }
        };
    }
}
