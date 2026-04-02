package com.zeroone.star.basic_information.service.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 杨潇, nick8311370879
 * @version 1.0
 * @date:
 */
@Getter
public class SimpleImportListener<T> extends AnalysisEventListener<T> {
    private final List<T> successList = new ArrayList<>();
    // 修改为单个字符串，用于存放首个错误信息
    private String errorMessage = "";
    // 记录错误行号
    private int errorRowNum = -1;

    @Override
    public void invoke(T data, AnalysisContext context) {
        // 获取当前行号（Excel中的实际行号）
        int currentRowNum = context.readRowHolder().getRowIndex() + 1;

        // 使用工具类进行校验
        String errorMsg = EasyExcelValidateHelper.validate(data);

        // 如果校验不通过，立即抛出异常，停止后续解析
        if (errorMsg != null && !errorMsg.trim().isEmpty()) {
            this.errorMessage = "第" + currentRowNum + "行: " + errorMsg;
            this.errorRowNum = currentRowNum;
            // 抛出异常，中断读取过程
            throw new RuntimeException(this.errorMessage);
        }

        // 只有校验通过的数据才会执行到这里
        successList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 所有数据解析完成，可以在这里进行后续操作，如批量保存successList
        System.out.println("Excel解析完成。成功条数: " + successList.size());
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        // 如果是我们主动抛出的校验异常，不再向上抛出，而是静默处理，停止解析。
        // EasyExcel会捕获到此异常并停止解析。
        if (exception instanceof RuntimeException) {
            System.out.println("捕获到校验异常，停止解析: " + exception.getMessage());

            throw new IllegalStateException("VALIDATION_ERROR:" + exception.getMessage());
        }
        // 如果是其他类型的异常（如IO异常），可以按照原有逻辑处理或抛出
        throw exception;
    }
}
