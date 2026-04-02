package com.zeroone.star.basic_information.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.star.basic_information.entity.Supplier;
import com.zeroone.star.basic_information.mapper.SupplierMapper;
import com.zeroone.star.basic_information.service.ISupplierService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.basic_information.service.util.PinyinUtils;
import com.zeroone.star.project.components.fastdfs.FastDfsClientComponent;
import com.zeroone.star.project.components.fastdfs.FastDfsFileInfo;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.*;
import com.zeroone.star.project.query.j6.basic_information.supplier_management.supplierListQuery;
import com.zeroone.star.project.vo.j6.basic_information.supplier_management.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * <p>
 * 供应商 服务实现类
 * </p>
 *
 * @author 趣味生煎
 * @since 2025-10-21
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {

    @Resource
    MsSupplierMapper ms;

    @Resource
    FastDfsClientComponent dfs;

    @Value("${fastdfs.nginx-servers}")
    private String fileServerUrl;

    @Resource
    private PinyinUtils pinyinUtils;

    @Override
    public boolean save(Supplier entity) {
        // 保存前自动设置拼音
        if (entity.getName() != null) {
            entity.setPy(pinyinUtils.getPinyinInitials(entity.getName()));
        }
        return super.save(entity);
    }

    @Override
    public boolean updateById(Supplier entity) {
        // 更新时如果名称变更，更新拼音
        if (entity.getName() != null) {
            Supplier existing = baseMapper.selectById(entity.getId());
            if (existing != null && !entity.getName().equals(existing.getName())) {
                entity.setPy(pinyinUtils.getPinyinInitials(entity.getName()));
            }
        }
        return super.updateById(entity);
    }

    @Override
    public SupplierDTO getById(Integer id) {
        Supplier supplier = baseMapper.selectById(id);
        if (supplier == null) {
            return null;
        }
        return ms.supplierToSupplierDto(supplier);
    }

    @Override
    public ImportResultVO importSuppliers(MultipartFile file,String url) {

        ImportResultVO result = new ImportResultVO();
        List<ImportFailureVO> failures = new ArrayList<>();
        int successCount = 0;

        try {
            // 1. 读取Excel数据
            List<SupplierImportAddDTO> importData = EasyExcel.read(file.getInputStream())
                    .head(SupplierImportAddDTO.class)
                    .sheet()
                    .doReadSync();

            result.setTotalCount(importData.size());


            // 2. 设置结果
            result.setSuccessCount(successCount);
            result.setFailureCount(failures.size());
            result.setFailureDetails(failures);

        } catch (Exception e) {
            log.error("导入失败", e);
            throw new RuntimeException("导入处理异常");
        }

        return result;

    }

    @Override
    public String exportSuppliers(supplierListQuery query) {
        try {
            // 1. 查询数据
            List<Supplier> suppliers = baseMapper.selectList(buildQueryWrapper(query));

            // 2. 使用MapStruct直接转换为导出VO
            List<SupplierExportVO> exportData = new ArrayList<>();
            for (Supplier supplier : suppliers) {
                SupplierExportVO exportVO = ms.supplierToExportVO(supplier);
                exportData.add(exportVO);
            }

            // 3. 生成Excel并上传到FastDFS
            byte[] excelBytes = generateExcelBytes(exportData);
            FastDfsFileInfo fileInfo = dfs.uploadFile(excelBytes, "xlsx");

            // 4. 返回下载URL
            return dfs.fetchUrl(fileInfo, fileServerUrl, false);

        } catch (Exception e) {
            log.error("导出失败", e);
            throw new RuntimeException("导出处理异常");
        }
    }

    @Override
    public String downloadTemplate() {
        try {
            // 1. 创建模板数据
            List<SupplierImportTemplateVO> template = createTemplateData();

            // 2. 生成Excel并上传到FastDFS
            byte[] templateBytes = generateExcelBytes(template);
            FastDfsFileInfo fileInfo = dfs.uploadFile(templateBytes, "xlsx");

            // 3. 返回下载URL
            return dfs.fetchUrl(fileInfo, fileServerUrl, false);

        } catch (Exception e) {
            log.error("模板下载失败", e);
            throw new RuntimeException("模板生成异常");
        }

    }
    /**
     * 创建模板数据
     */
    private List<SupplierImportTemplateVO> createTemplateData() {
        List<SupplierImportTemplateVO> template = new ArrayList<>();

        SupplierImportTemplateVO example = new SupplierImportTemplateVO();
        example.setName("示例供应商");
        example.setNumber("SUP001");
        example.setFrame("默认组织");
        example.setUser("管理员");
        example.setCategory("常规类别");
        example.setRate(0.13);
        example.setBank("中国银行");
        example.setAccount("12345678901234567890");
        example.setTax("91370100MA3C0R9X0X");
        example.setData("这是备注信息");
        example.setContacts("联系人：张三，电话：13800138000");

        template.add(example);
        return template;
    }

    /**
     * 生成Excel字节数组
     */
    private <T> byte[] generateExcelBytes(List<T> data) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        EasyExcel.write(outputStream)
                .head(data.isEmpty() ? Object.class : data.get(0).getClass())
                .sheet("数据")
                .doWrite(data);

        return outputStream.toByteArray();
    }

    /**
     * 查重验证
     */
    private boolean isDuplicate(String number) {
        QueryWrapper<Supplier> wrapper = new QueryWrapper<>();
        wrapper.eq("number", number);
        return baseMapper.selectCount(wrapper) > 0;
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<Supplier> buildQueryWrapper(supplierListQuery query) {
        QueryWrapper<Supplier> wrapper = new QueryWrapper<>();

        if (query.getName() != null) {
            wrapper.like("name", query.getName());
        }
        if (query.getNumber() != null) {
            wrapper.eq("number", query.getNumber());
        }
        if (query.getCategory() != null) {
            wrapper.eq("category", query.getCategory());
        }
        if (query.getFrame() != null) {
            wrapper.eq("frame", query.getFrame());
        }

        return wrapper;
    }

    /**
     * 创建失败记录
     */
    private ImportFailureVO createFailure(int rowNum, String data, String reason) {
        ImportFailureVO failure = new ImportFailureVO();
        failure.setRowNumber(rowNum);
        failure.setData(data);
        failure.setReason(reason);
        return failure;
    }

}

