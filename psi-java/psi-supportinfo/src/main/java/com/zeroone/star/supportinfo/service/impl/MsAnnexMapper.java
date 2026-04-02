package com.zeroone.star.supportinfo.service.impl;

import com.zeroone.star.project.dto.j7.sysargs.supportinfo.attachment.AnnexDTO;
import com.zeroone.star.supportinfo.entity.Annex;
import org.mapstruct.Mapper;

/**
 * 类名：MsAnnexMapper
 * 包名：com.zeroone.star.supportinfo.service.impl
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/28
 * 版本号：V1.0
 */
@Mapper(componentModel = "spring")
public interface MsAnnexMapper {
    Annex annexDtoToAnnex(AnnexDTO annexDTO);
}
