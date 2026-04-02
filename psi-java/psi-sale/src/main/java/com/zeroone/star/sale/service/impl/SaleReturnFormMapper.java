package com.zeroone.star.sale.service.impl;

import com.zeroone.star.project.dto.j4.sale.SalesReturnOrderDTO;
import com.zeroone.star.project.dto.j4.sale.info.SalesReturnOrderInfo;
import com.zeroone.star.sale.entity.IsSre;
import com.zeroone.star.sale.entity.IsSreInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface SaleReturnFormMapper {

     /*
     SalesReturnOrderInfo转IsSreInfo
     */
    IsSreInfo toIsSreInfo(SalesReturnOrderInfo salesReturnOrderInfo);

    /*
    SalesReturnOrderDTO转IsSre
    */
    IsSre toIsSre(SalesReturnOrderDTO salesReturnOrderDTO);
}
