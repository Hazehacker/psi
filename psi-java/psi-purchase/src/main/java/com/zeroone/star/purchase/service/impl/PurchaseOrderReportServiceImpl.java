package com.zeroone.star.purchase.service.impl;

import com.alibaba.excel.EasyExcel;
import com.zeroone.star.common.constant.CacheConstants;
import com.zeroone.star.common.utils.CacheUtil;
import com.zeroone.star.project.dto.j3.purchase.PurchaseOrderExcelInfoDTO;
import com.zeroone.star.purchase.mapper.PurchaseOrderReportMapper;
import com.zeroone.star.purchase.service.IPurchaseOrderReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 采购订单报表服务实现类
 * @author: Hazenix 
 */
@Slf4j
@Service
public class PurchaseOrderReportServiceImpl implements IPurchaseOrderReportService {

    @Autowired
    private PurchaseOrderReportMapper purchaseOrderReportMapper;

    @Autowired
    private CacheUtil cacheUtil;

    @Override
    public byte[] exportPurchaseOrderReport(List<String> ids) {
        // 构建缓存key
        String idsKey = String.join(",", ids);
        String cacheKey = CacheConstants.PURCHASE_BOR_REPORT + ":" + idsKey.hashCode();

        // 使用缓存穿透防御查询（导出数据较大，使用passThrough避免锁等待）
        return cacheUtil.queryWithPassThrough(
                cacheKey,
                byte[].class,
                () -> queryData(ids),
                CacheConstants.DEFAULT_TTL,
                TimeUnit.SECONDS
        );
    }

    /**
     * 查询并生成报表数据
     */
    private byte[] queryData(List<String> ids) {
        // 使用ByteArrayOutStream来在内存中写入Excel
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try{
            // 从数据库中查询数据(根据IDs)
            List<PurchaseOrderExcelInfoDTO> dataList = purchaseOrderReportMapper.selectPurchaseOrderReportDataByIds(ids);
            if (dataList == null) {
                dataList = Collections.emptyList();
            }

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
