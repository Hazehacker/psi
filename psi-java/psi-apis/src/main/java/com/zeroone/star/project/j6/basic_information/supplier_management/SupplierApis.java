package com.zeroone.star.project.j6.basic_information.supplier_management;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.customer_management.userDto;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.SupplierAddDTO;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.SupplierDTO;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.querySupplierDto;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.querySupplierListDto;
import com.zeroone.star.project.query.j6.basic_information.customer_management.userListQuery;
import com.zeroone.star.project.query.j6.basic_information.supplier_management.supplierListQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j6.basic_information.supplier_management.ImportResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 趣味生煎
 * @version 1.0
 * 描述：
 */
public interface SupplierApis {


    /**
     * 查询供应商列表
     * @param query 查询参数
     * @return 供应商列表
     */
    JsonVO<PageDTO<querySupplierListDto>> querySupplierList(supplierListQuery query);

    JsonVO<PageDTO<querySupplierDto>> querySupplier(supplierListQuery query);

    /**
     * 获取指定供应商信息
     * @param id 供应商ID
     * @return 供应商信息
     */
    JsonVO<SupplierDTO> querySupplierById(Integer id);

    /**
     * 添加供应商
     * @param supplierAddDTO 供应商信息
     * @return 添加结果
     */
    JsonVO<String> addSupplier(SupplierAddDTO supplierAddDTO);

    /**
     * 修改供应商信息
     * @param supplierDTO 供应商信息
     * @return 修改结果
     */
    JsonVO<String> updateSupplier(SupplierDTO supplierDTO);

    /**
     * 删除供应商（支持批量）
     * @param ids 供应商ID列表
     * @return 删除结果
     */
    JsonVO<String> deleteSupplier(List<Integer> ids);

    /**
     * 导入供应商数据
     * @param file Excel文件
     * @return 导入结果
     */
    JsonVO<ImportResultVO> importSuppliers(MultipartFile file,String url);

    /**
     * 导出供应商数据
     * @param query 查询条件
     * @return 导出文件的URL
     */
    JsonVO<String> exportSuppliers(supplierListQuery query);

    /**
     * 下载导入模板
     * @return 模板文件的URL
     */
    JsonVO<String> downloadTemplate();

}
