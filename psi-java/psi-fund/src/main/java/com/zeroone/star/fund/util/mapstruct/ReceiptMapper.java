package com.zeroone.star.fund.util.mapstruct;

import com.zeroone.star.fund.entity.ImyInfo;
import com.zeroone.star.project.dto.j4.fund.FundReceiptDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReceiptMapper {
    ReceiptMapper INSTANCE = Mappers.getMapper(ReceiptMapper.class);

    FundReceiptDetailDTO toDTO(ImyInfo imyInfo);
}
