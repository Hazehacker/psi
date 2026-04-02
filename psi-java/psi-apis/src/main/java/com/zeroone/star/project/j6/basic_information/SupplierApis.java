package com.zeroone.star.project.j6.basic_information;

import com.zeroone.star.project.dto.j6.basic_information.supplier_management.SupplierAddDTO;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.SupplierDTO;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j6.basic_information.supplier_management.ExportResultVO;
import com.zeroone.star.project.vo.j6.basic_information.supplier_management.ImportResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 趣味生煎
 * @version 1.0
 * 描述：供应商管理API接口
 */
public interface SupplierApis {

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
     * @param file 导入的文件（支持Excel等格式）
     * @return 导入结果
     */
    JsonVO<ImportResultVO> importSuppliers(MultipartFile file);

    /**
     * 导出供应商数据
     * @param ids 要导出的供应商ID列表（为空则导出全部）
     * @param exportFields 导出字段列表（为空则导出所有字段）
     * @return 导出文件下载信息
     */
    JsonVO<ExportResultVO> exportSuppliers(List<Integer> ids, List<String> exportFields);


}
