package com.zeronoe.star.report;


import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.components.fastdfs.FastDfsClientComponent;
import com.zeroone.star.project.components.fastdfs.FastDfsFileInfo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 描述：用于测试Excel操作
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */



@SpringBootTest
public class ExcelTest {

    @Resource
    EasyExcelComponent excel;

    final String path = "C:\\test.xlsx";

    @Test
    void testGenerateExcel() {
        // 定义测试数据
        List<User> dataList = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            User user = new User();
            user.setId(i);
            user.setName("用户" + i);
            user.setPhone("1234567890" + i);
            dataList.add(user);
        }

        excel.generateExcel(path, "用户信息", User.class, dataList);
    }

    @Test
    void testParseExcel() {
        List<User> users = excel.parseExcel(path, User.class);
        users.forEach(System.out::println);
    }

    @Resource
    FastDfsClientComponent dfs;

    @SneakyThrows
    @Test
    void testUploadFile() {
        // 定义测试数据
        List<User> dataList = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            User user = new User();
            user.setId(i);
            user.setName("FastDfs" + i);
            user.setPhone("1234567890" + i);
            dataList.add(user);
        }

        // 定义输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 生成Excel
        excel.export("用户信息", out, User.class, dataList);
        // 上传FastDfs
        FastDfsFileInfo info = dfs.uploadFile(out.toByteArray(), "xlsx");
        System.out.println(info);
        out.close();
    }
}
