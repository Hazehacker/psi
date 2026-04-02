package com.zeroone.star.basic_information.service.impl;

import com.zeroone.star.basic_information.entity.Supplier;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.*;
import com.zeroone.star.project.vo.j6.basic_information.supplier_management.SupplierExportVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author 趣味生煎
 * @version 1.0
 * 描述：供应商对应MapStruct映射接口
 */
@Mapper(componentModel = "spring")
public interface MsSupplierMapper {

    /**
     * 映射DTO
     * @param entity 源实体
     * @return 映射DTO
     */
    querySupplierListDto toDto(Supplier entity);

    /**
     * 映射DTO
     * @param entity 实体
     * @return DTO
     */
    SupplierDTO supplierToSupplierDto(Supplier entity);

    /**
     * 映射实体
     * @param dto SupplierAddDTO
     * @return 实体
     */
    Supplier addDtoToSupplier(SupplierAddDTO dto);

    /**
     * 映射实体
     * @param dto SupplierDTO
     * @return 实体
     */
    Supplier supplierDtoToSupplier(SupplierDTO dto);

    /**
     * 实体类转换导入DTO
     * @param entity 实体
     * @return SupplierImportDTO
     */
    SupplierImportDTO supplierToSupplierImportDto(Supplier entity);

    /**
     * 添加对象映射实体
     * @param dto SupplierImportAddDTO
     * @return Supplier
     */
    Supplier addImportDtoToSupplier(SupplierImportAddDTO dto);

    /**
     * DTO映射实体
     * @param dto SupplierImportDTO
     * @return 实体
     */
    Supplier supplierImportDtoToSupplier(SupplierImportDTO dto);

    /**
     * 实体转换为导出VO
     * @param entity 实体
     * @return 导出VO
     */
    SupplierExportVO supplierToExportVO(Supplier entity);

    /**
     * Double转BigDecimal
     */
    @Named("doubleToBigDecimal")
    default BigDecimal doubleToBigDecimal(Double value) {
        return value != null ? BigDecimal.valueOf(value) : BigDecimal.ZERO;
    }

    /**
     * BigDecimal转Double
     */
    @Named("bigDecimalToDouble")
    default Double bigDecimalToDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : 0.0;
    }
}