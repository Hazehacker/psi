
package com.zeroone.star.sale.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.components.easyexcel.ExcelReadListener;
import com.zeroone.star.project.dto.j4.sale.SaleExportDTO;
import com.zeroone.star.project.dto.j4.sale.SaleExportDetailDTO;
import com.zeroone.star.project.dto.j4.sale.SaleImportDTO;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.sale.entity.Sale;
import com.zeroone.star.sale.entity.SellInfo;
import com.zeroone.star.sale.mapper.MsSaleFormMapper;
import com.zeroone.star.sale.mapper.SalePortMapper;
import com.zeroone.star.sale.mapper.SellInfoMapper;


import com.zeroone.star.sale.service.SaleFormPortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 销售单 服务实现类
 * </p>
 *
 * @author renjian
 * @since 2025-10-26
 */
@Service
public class SaleFormServicePortImpl extends ServiceImpl<SalePortMapper, Sale> implements SaleFormPortService {
    @Resource
    MsSaleFormMapper msSaleFormMapper;
    @Resource
    EasyExcelComponent excel;
    @Autowired
    private SalePortMapper saleMapper;
    @Resource
    private SellInfoMapper sellInfoMapper;

    @Override
    public ResponseEntity<byte[]> exportDetailSaleForm(List<String> ids) {
        //1 对ids进行校验
        if (ids.isEmpty()) {
            throw new RuntimeException("请选择要导出的记录");
        }
        // 用来缓存每个 Excel 文件：key = 文件名 , value = Excel 二进制
        Map<String, byte[]> files = new LinkedHashMap<>(ids.size());
        //2遍历ids查询good数据
        for(String id:ids)
        {
            try {
                //查询详情数据
                QueryWrapper<SellInfo> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("pid",id);
                List<SellInfo> Selldetails = sellInfoMapper.selectList(queryWrapper);
                if(!Selldetails.isEmpty()) {
                    //3 将SellInfo转换成SaleExportDetailDTO
                    List<SaleExportDetailDTO> dtos = new ArrayList<>();
                    for (SellInfo detail : Selldetails) {
                        dtos.add(msSaleFormMapper.toExportdetailDTO(detail));
                    }
                    ByteArrayOutputStream xlsOut = new ByteArrayOutputStream();
                    excel.export("销售详细单", xlsOut, SaleExportDetailDTO.class, dtos);
                    // 2.4 文件名最好带 id，防止重名
                    String fileName = id + "详细销售单" + ".xlsx";
                    files.put(fileName, xlsOut.toByteArray());
                    xlsOut.close();
                }
            } catch (Exception e) {
                throw new RuntimeException("excel生成失败");
            }
        }
        // 3. 把多个 Excel 打成 zip
        ByteArrayOutputStream zipOut = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(zipOut)) {
            for (Map.Entry<String, byte[]> entry : files.entrySet()) {
                ZipEntry zipEntry = new ZipEntry(entry.getKey());
                zos.putNextEntry(zipEntry);
                zos.write(entry.getValue());
                zos.closeEntry();
            }
            zos.finish();   // 重要
        } catch (IOException e) {
            throw new RuntimeException("创建zip失败");
        }
        HttpHeaders headers = new HttpHeaders();
        String filename = "销售单列表"+ DateTime.now().toString("yyyyMMddHHmmssS")+".zip";
        headers.setContentDispositionFormData("attachment",filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // 5. 返回
        return new ResponseEntity<>(zipOut.toByteArray(), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> exportSimpleSaleForm(List<String> ids) {
        //1 对ids进行校验
        if (ids.isEmpty()) {
            throw new RuntimeException("请选择要导出的记录");
        }
        //2 查询简单数据
        List<Sale> sales = this.listByIds(ids);
        List<SaleExportDTO> dtos = new ArrayList<>();
        for (Sale sale : sales) {
            dtos.add(msSaleFormMapper.toExportDTO(sale));
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            excel.export("销售单列表", out, SaleExportDTO.class, dtos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HttpHeaders headers = new HttpHeaders();
        String filename = "销售单列表"+ DateTime.now().toString("yyyyMMddHHmmssS")+".xlsx";
        headers.setContentDispositionFormData("attachment",filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
    }

    //导入数据
    @Override
    public JsonVO<String> importDetailSaleForm(MultipartFile file) {
        List<SaleImportDTO> dtos =new ArrayList<>();
        try (InputStream in = file.getInputStream()) {
                ExcelReadListener<SaleImportDTO> listener = new ExcelReadListener<>();
                //sheet()方法表示读取所有的sheet
                //doRead() 表示表示执行读取动作
                EasyExcel.read(in, SaleImportDTO.class, listener).sheet().doRead();
                dtos = listener.getDataList();
            }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        for ( SaleImportDTO dto : dtos) {
            Sale entity = msSaleFormMapper.toEntity(dto);
            entity.setExamine(0);
            entity.setNucleus(0);
            entity.setInvoice(0);
            entity.setCse(0);
            entity.setCheck(0);
            int insert = saleMapper.insert(entity);
            if (insert != 1)
            {
                throw new RuntimeException("插入数据失败");
            }
//            List<SellInfo> details =new ArrayList<>();
//            for(SaleInfoDTO detail : dto.getItems())
//            {
//                SellInfo sellInfo = msSaleFormMapper.todetailEntity(detail);
//                try {
//                    sellInfoMapper.insert(sellInfo);
//                } catch (Exception e) {
//                    throw new RuntimeException("详细插入失败");
//                }
//
//            }


        }
        return JsonVO.success("导入成功");
    }
}
