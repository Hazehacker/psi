package com.zeroone.star.moneytransfer.service.impl;


import com.zeroone.star.moneytransfer.entity.Allot;
import com.zeroone.star.moneytransfer.entity.AllotInfo;
import com.zeroone.star.moneytransfer.mapper.AllotMapper;
import com.zeroone.star.project.dto.j7.money.transfer.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类名：MsAllotMapper
 * 包名：com.zeroone.star.moneytransfer.service.impl
 * 描述：Allot的MapStruct映射接口
 * 作者：hh
 * 创建日期：2025/10/24
 * 版本号：V1.0
 */
@Mapper(componentModel = "spring")
public interface MsAllotMapper {




    AllotListDTO allotToAllotListDto(Allot allot);

    Allot addAllotDtoToAllot(AddAllotDTO addAllotDTO);

    /**
     * 将 AllotModifyDTO 转换为 Allot 实体
     * @param allotModifyDTO DTO对象
     * @return Allot 实体
     */
    Allot allotModifyDtoToAllot(AllotModifyDTO allotModifyDTO);



    AllotDetailDTO allotToAllotDetailDTO(Allot allot);

}
