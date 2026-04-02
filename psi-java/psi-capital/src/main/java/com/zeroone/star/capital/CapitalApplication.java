package com.zeroone.star.capital;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * 描述：程序启动入口
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan({
        "com.zeroone.star.capital.mapper",
})
public class CapitalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CapitalApplication.class, args);
    }

//    @Bean
//    public BreMapper breMapper(SqlSessionTemplate sqlSessionTemplate) {
//        return sqlSessionTemplate.getMapper(BreMapper.class);
//    }
//
//    @Bean
//    public ImyMapper imyMapper(SqlSessionTemplate sqlSessionTemplate) {
//        return sqlSessionTemplate.getMapper(ImyMapper.class);
//    }
//
//    @Bean
//    public SellMapper sellMapper(SqlSessionTemplate sqlSessionTemplate) {
//        return sqlSessionTemplate.getMapper(SellMapper.class);
//    }
//
//    @Bean
//    public SreMapper sreMapper(SqlSessionTemplate sqlSessionTemplate) {
//        return sqlSessionTemplate.getMapper(SreMapper.class);
//    }
//
//    @Bean
//    public SellBillMapper sellBillMapper(SqlSessionTemplate sqlSessionTemplate) {
//        return sqlSessionTemplate.getMapper(SellBillMapper.class);
//    }
//
//    @Bean
//    public SreBillMapper sreBillMapper(SqlSessionTemplate sqlSessionTemplate) {
//        return sqlSessionTemplate.getMapper(SreBillMapper.class);
//    }
//
//    @Bean
//    public BreBillMapper breBillMapper(SqlSessionTemplate sqlSessionTemplate) {
//        return sqlSessionTemplate.getMapper(BreBillMapper.class);
//    }

}

