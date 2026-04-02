package com.zeroone.star.moneytransfer;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class GenerateEntity {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://192.168.247.129:3306/zo_psi", "root", "123456")
                // 1. 全局配置（不变）
                .globalConfig((scanner, builder) -> {
                    builder.author(scanner.apply("请输入作者名称？")) // 作者
                            .outputDir("D:/testGit/zero-one-12psi/psi-java/psi-j7-moneytransfer" + "/src/main/java"); // 输出目录
                })
                // 2. 包配置（只保留entity的包路径，其他可省略但不影响）
                .packageConfig((scanner, builder) -> {
                    String parent = scanner.apply("请输入基础包名？");
                    builder.parent(parent) // 基础包（如：com.zeroone.star.supportinfo）
                            .entity("entity") // 实体类包名
                            // 其他包（mapper、service等）可以保留，但后续会被禁用生成
                            .mapper("mapper")
                            .service("service")
                            .controller("controller")
                            // 即使配置了mapperXml路径，后续也会禁用生成，可保留或删除
                            .pathInfo(Collections.singletonMap(
                                    OutputFile.mapperXml,
                                    "D:/testGit/zero-one-12psi/psi-java/psi-j7-moneytransfer/src/main/resources/com/zeroone/star/moneytransfer/mapper"
                            ));
                })
                // 3. 策略配置（只关注entity的策略，其他组件策略可省略）
                .strategyConfig((scanner, builder) -> {
                    builder.addInclude(getTables(scanner.apply("请输入表名（多个用,分隔，all 代表所有）？")))
                            // 实体类策略（保持你的原有配置）
                            .entityBuilder()
                            .enableLombok() // 启用Lombok
                            .addTableFills(
                                    new Column("create_time", FieldFill.INSERT),
                                    new Column("update_time", FieldFill.INSERT_UPDATE)
                            )
                            .enableTableFieldAnnotation() // 字段添加@TableField注解
                            .formatFileName("%s") // 实体类文件名（如：User.java）
                            .build();
                    // 注意：这里不需要配置mapper、service、controller的策略，因为后续会禁用它们的生成
                })
                // 4. 核心配置：模板配置（只保留entity模板，禁用其他所有文件）
                .templateConfig(builder -> {
                    builder
                            // 禁用mapper接口生成
                            .mapper(null)
                            // 禁用mapper.xml生成
                            .mapperXml(null)
                            // 禁用service接口生成
                            .service(null)
                            // 禁用service实现类生成
                            .serviceImpl(null)
                            // 禁用controller生成
                            .controller(null);
                    // 实体类模板保留默认（或指定自定义模板路径，这里用默认）

                })
                // 5. 模板引擎（保持Freemarker）
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    // 处理表名输入（不变）
    private static List<String> getTables(String tables) {
        if ("all".equals(tables)) {
            return Collections.emptyList();
        }
        return Arrays.asList(tables.split(","));
    }
}