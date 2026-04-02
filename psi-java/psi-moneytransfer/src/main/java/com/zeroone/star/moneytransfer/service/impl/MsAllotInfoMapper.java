package com.zeroone.star.moneytransfer.service.impl;

import com.zeroone.star.moneytransfer.entity.AllotInfo;
import com.zeroone.star.project.dto.j7.money.transfer.AddAllotDTO;
import com.zeroone.star.project.dto.j7.money.transfer.AddAllotInfoDTO;
import com.zeroone.star.project.dto.j7.money.transfer.AllotInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 类名：AllotInfoMapper
 * 包名：com.zeroone.star.moneytransfer.service.impl
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/24
 * 版本号：V1.0
 */
@Mapper(componentModel = "spring")
public interface MsAllotInfoMapper {
    AllotInfoDTO allotInfoToAllotInfoDto(AllotInfo allotInfo);

    AllotInfo allotInfoDTOToAllotInfo(AllotInfoDTO allotInfoDTO);

    AllotInfo addAllotInfoDtoToAllotInfo(AddAllotInfoDTO allotInfoDTO);


}
