package com.zeroone.star.moneytransfer;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://192.168.247.129:3306/zo_psi", "root", "123456")
                // 1. 全局配置
                .globalConfig((scanner, builder) -> {
                    builder.author(scanner.apply("请输入作者名称？")) // 作者
                            .outputDir("D:/testGit/zero-one-12psi/psi-java/psi-j7-moneytransfer"+ "/src/main/java"); // 输出目录

                })
                // 2. 包配置
                .packageConfig((scanner, builder) -> {
                    String parent = scanner.apply("请输入基础包名？");
                    builder.parent(parent) // 基础包（如：com.zeroone.star.supportinfo）
                            .entity("entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            // 关键配置：指定mapper.xml输出到resources目录
                            .pathInfo(Collections.singletonMap(
                                    OutputFile.mapperXml,
                                    "D:/TestGit/zero-one-12psi/psi-java/psi-j7-moneytransfer/src/main/resources/com/zeroone/star/moneytransfer/mapper"
                            ));
                })
                // 3. 策略配置
                .strategyConfig((scanner, builder) -> {
                    builder.addInclude(getTables(scanner.apply("请输入表名（多个用,分隔，all 代表所有）？")))
                            // 实体策略
                            .entityBuilder()
                            .enableLombok()
                            .addTableFills(new Column("create_time", FieldFill.INSERT))
                            // 添加以下配置：为每个字段添加 @TableField 注解
                            .formatFileName("%s")
                            .addTableFills(
                                    new Column("create_time", FieldFill.INSERT),
                                    new Column("update_time", FieldFill.INSERT_UPDATE)
                            )
                            // 关键配置：为所有字段添加 @TableField 注解
                            .enableTableFieldAnnotation()
                            .build() // 回到 StrategyConfig.Builder
                            // Controller 策略
                            .controllerBuilder()
                            .enableRestStyle()
                            .build(); // 回到 StrategyConfig.Builder
                })
                // 4. 模板引擎（Freemarker）
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    // 处理表名输入（all → 所有表）
    private static List<String> getTables(String tables) {
        if ("all".equals(tables)) {
            return Collections.emptyList();
        }
        return Arrays.asList(tables.split(","));
    }
}