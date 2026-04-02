package com.zeroone.star.sale.mapper;

import com.zeroone.star.project.dto.j4.sale.SaleExportDTO;
import com.zeroone.star.project.dto.j4.sale.SaleExportDetailDTO;
import com.zeroone.star.project.dto.j4.sale.SaleImportDTO;
import com.zeroone.star.project.dto.j4.sale.SaleInfoDTO;
import com.zeroone.star.sale.entity.Sale;
import com.zeroone.star.sale.entity.SellInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MsSaleFormMapper {
    /**
     * 映射导出dto
     */
    SaleExportDTO toExportDTO(Sale sale);
    /**
     * 映射导出详情dto
     */
    SaleExportDetailDTO toExportdetailDTO(SellInfo detail);

    /**
     * 导入数据对象到实体对象
     * @param
     * @return
     */
    Sale toEntity(SaleImportDTO dto);
    /**
     * 导入数据对象到实体对象
     * @param
     * @return
     */
    SellInfo todetailEntity(SaleInfoDTO dto);
}
