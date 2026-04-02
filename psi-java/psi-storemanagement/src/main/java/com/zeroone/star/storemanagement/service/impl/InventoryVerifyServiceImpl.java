package com.zeroone.star.storemanagement.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.*;
import com.zeroone.star.project.query.j2.store.InventoryQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.storemanagement.entity.AttrDO;
import com.zeroone.star.storemanagement.entity.GoodsDO;
import com.zeroone.star.storemanagement.mapper.AttrMapper;
import com.zeroone.star.storemanagement.mapper.GoodsMapper;
import com.zeroone.star.storemanagement.mapper.InventoryMapper;
import com.zeroone.star.storemanagement.mapper.InventoryVerifyMapper;
import com.zeroone.star.storemanagement.service.InventoryVerifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class InventoryVerifyServiceImpl implements InventoryVerifyService {

    @Autowired
    InventoryVerifyMapper inventoryVerifyMapper;

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    AttrMapper attrMapper;

    @Autowired
    EasyExcelComponent easyExcelComponent;

    /**
     * 获取盘盈单数据
     *
     * @param inventoryVerifyListS
     * @return
     */
    @Override
    public PageDTO<OtherInListInfoDTO> getInventoryPlus(List<InventoryVerifyDTO> inventoryVerifyListS) {

        List<OtherInListInfoDTO> list = new ArrayList<>();
        for (InventoryVerifyDTO inventoryVerifyList : inventoryVerifyListS) {
            GoodsDO good = goodsMapper.selectById(inventoryVerifyList.getGoodId());

            String attrName=null;
            if(inventoryVerifyList.getAttrId() != null && !inventoryVerifyList.getAttrId().isEmpty()) {
                attrName = attrMapper.selectById(inventoryVerifyList.getAttrId()).getName();
            }

            OtherInListInfoDTO otherInListInfoDTO = OtherInListInfoDTO.builder()
                    .goods(inventoryVerifyList.getGoodId())
                    .attr(attrName)
                    .unit(good.getUnit())
                    .warehouse(inventoryVerifyList.getWarehouseId())
                    .price(good.getBuy())
                    .nums(inventoryVerifyList.getInventoryDifference())
                    .total(inventoryVerifyList.getInventoryDifference().multiply(good.getBuy()))
                    .build();
            list.add(otherInListInfoDTO);
        }


        PageDTO<OtherInListInfoDTO> page = new PageDTO<>();
        page.setRows(list);

        return page;
    }

    /**
     * 获取盘亏单数据
     *
     * @param inventoryVerifyListS
     * @return
     */
    @Override
    public PageDTO<OtherOutListInfoDTO> getInventoryReduce(List<InventoryVerifyDTO> inventoryVerifyListS) {
        List<OtherOutListInfoDTO> list = new ArrayList<>();
        for (InventoryVerifyDTO inventoryVerifyList : inventoryVerifyListS) {
           GoodsDO good = goodsMapper.selectById(inventoryVerifyList.getGoodId());

            String attrName=null;
           if(inventoryVerifyList.getAttrId() != null && !inventoryVerifyList.getAttrId().isEmpty()) {
               attrName = attrMapper.selectById(inventoryVerifyList.getAttrId()).getName();
           }
                   OtherOutListInfoDTO otherOutListInfoDTO = OtherOutListInfoDTO.builder()
                           .goods(inventoryVerifyList.getGoodId())
                           .attr(attrName)
                           .unit(good.getUnit())
                           .warehouse(inventoryVerifyList.getWarehouseId())
                           .price(good.getBuy())
                           .nums(inventoryVerifyList.getInventoryDifference())
                           .total(inventoryVerifyList.getInventoryDifference().multiply(good.getBuy()))
                           .build();
                   list.add(otherOutListInfoDTO);
               }



            PageDTO<OtherOutListInfoDTO> page = new PageDTO<>();
            page.setRows(list);

            return page;

    }
        /**
         * 导出库存盘点单（不需要参数，直接在数据库去查）
         *
         * @param
         * @return
         */
        @Override
        public ByteArrayOutputStream exportInventoryVerifyExcel () throws IOException {
            try {
                // 查询数据
                List<InventoryVerifyListDTO> inventoryVerifyList = inventoryVerifyMapper.selectInventoryVerifyListDTO();
                log.info("库存盘点单导出数据：{}", inventoryVerifyList);

                List<InventoryVerifyListDTO> inventoryVerifyListS = new ArrayList<>();

                // 组装数据
                //查看对象有有无属性attrName，如果有则将其包装成一个InventoryVerifyListDTO对象，并将其添加到inventoryVerifyListS中
                for (int i = 0; i < inventoryVerifyList.size(); i++) {

                    if (inventoryVerifyList.get(i).getAttrName() != null && !inventoryVerifyList.get(i).getAttrName().isEmpty()) {
                        if (i != 0 && inventoryVerifyList.get(i).getName().equals(inventoryVerifyList.get(i - 1).getName())) {
                            InventoryVerifyListDTO inventoryVerifyListDTO1 = new InventoryVerifyListDTO();
                            inventoryVerifyListDTO1.setName(inventoryVerifyList.get(i).getAttrName());
                            inventoryVerifyListS.add(inventoryVerifyListDTO1);
                        } else {
                            inventoryVerifyList.get(i).setAttrName(null);
                            inventoryVerifyListS.add(inventoryVerifyList.get(i));
                        }

                    } else {
                        inventoryVerifyListS.add(inventoryVerifyList.get(i));
                    }

                }

                log.info("组装后的库存盘点单数据：{}", inventoryVerifyListS);

                // 1. 准备字节输出流（用于存储Excel文件的字节数据）
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                // 2. 读取Excel模板文件（位于resources/templates目录下）
                InputStream is = null;
                try {
                    is = new ClassPathResource("templates/InventoryVerify.xlsx").getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e); // 模板读取失败则抛异常
                }

                // 3. 初始化EasyExcel写入器（基于模板）
                ExcelWriter workBook = EasyExcel.write(baos, InventoryVerifyListDTO.class)
                        .withTemplate(is) // 关联模板
                        .build();

                // 4. 定义写入的sheet
                WriteSheet sheet = EasyExcel.writerSheet().build();

                // 6. 填充数据到模板（强制新增行，避免覆盖模板样式）
                FillConfig build = FillConfig.builder().forceNewRow(true).build();
                workBook.fill(inventoryVerifyListS, build, sheet); // 填充明细数据

                // 7. 完成写入并获取字节数组
                workBook.finish();

                // 8. 返回成功响应
                return baos;


            } catch (Exception e) {
                log.error("导出库存盘点单Excel失败", e);
                throw new IOException("导出库存盘点单Excel失败: " + e.getMessage(), e);
            }
        }


}