package com.zeroone.star.basic_information.service;

import com.zeroone.star.basic_information.entity.Supplier;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.SupplierDTO;
import com.zeroone.star.project.query.j6.basic_information.supplier_management.supplierListQuery;
import com.zeroone.star.project.vo.j6.basic_information.supplier_management.ImportResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 供应商 服务类
 * </p>
 *
 * @author 趣味生煎
 * @since 2025-10-21
 */
public interface ISupplierService extends IService<Supplier> {

    /**
     * 根据id获取指定供应商信息
     * @param id
     * @return
     */
    SupplierDTO getById(Integer id);

    /**
     * 导入供应商数据
     * @param file Excel文件
     * @return 导入结果VO
     */
    ImportResultVO importSuppliers(MultipartFile file,String url);

    /**
     * 导出供应商数据
     * @param query 查询条件
     * @return 导出文件路径或URL
     */
    String exportSuppliers(supplierListQuery query);

    /**
     * 下载导入模板
     * @return 模板文件路径或URL
     */
    String downloadTemplate();

}
