package com.zeroone.star.purchase.service.impl;

import com.alibaba.excel.EasyExcel;
import com.zeroone.star.project.dto.j3.purchase.PurchaseOrderExcelInfoDTO;
import com.zeroone.star.purchase.mapper.PurchaseOrderReportMapper;
import com.zeroone.star.purchase.service.IPurchaseOrderReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 采购订单报表服务实现类
 * @author: 加减法
 */
@Slf4j
@Service
public class PurchaseOrderReportServiceImpl implements IPurchaseOrderReportService {

    @Autowired
    private PurchaseOrderReportMapper purchaseOrderReportMapper;

    @Override
    public byte[] exportPurchaseOrderReport(List<String> ids) {
        // 使用ByteArrayOutStream来在内存中写入Excel
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try{
            // 从数据库中查询数据(根据IDs)
            List<PurchaseOrderExcelInfoDTO> dataList = purchaseOrderReportMapper.selectPurchaseOrderReportDataByIds(ids);

            // 使用EasyExcel将数据写入到ByteArrayOutputStream中
            EasyExcel.write(outputStream, PurchaseOrderExcelInfoDTO.class)
                    .sheet("采购订单详细报表")//设置sheet名称
                    .doWrite(dataList);//写入数据

            // 返回字节数组
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("导出采购订单详细报表失败", e);
            throw new RuntimeException("导出Excel失败", e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                log.error("关闭ByteArrayOutputStream失败", e);
            }
        }
    }
}
