package com.zeroone.star.storemanagement.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.zeroone.star.project.components.user.UserHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 描述：初始化MP
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@Configuration
@ComponentScan("com.zeroone.star.project.config.mybatis")
public class MpConfig {

    @Resource
    UserHolder userHolder;

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            // 雪花算法生成器（可自定义workerId、datacenterId）
            final IdentifierGenerator idGenerator = new DefaultIdentifierGenerator();
            @Override
            public void insertFill(MetaObject metaObject) {
                // 1. 处理 id：Long → String 明确转换，避免类型冲突
                if (getFieldValByName("id", metaObject) == null) {
                    String idStr = String.valueOf(idGenerator.nextId(null)); // 强制转 String
                    setFieldValByName("id", idStr, metaObject);
                }

                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
                try {
                    this.strictInsertFill(metaObject, "creatBy", String.class, userHolder.getCurrentUser().getUsername());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
                try {
                    this.strictUpdateFill(metaObject, "updateBy", String.class, userHolder.getCurrentUser().getUsername());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
