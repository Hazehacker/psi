package com.zeroone.star.supportinfo.service.impl;

import com.zeroone.star.project.dto.j7.sysargs.supportinfo.inandexpend.IetDetailDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.inandexpend.IetListDTO;
import com.zeroone.star.supportinfo.entity.Iet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MsIetMapper {
    /**
     * 将实体类转化成iet的列表dto
     * @param iet
     * @return
     */
    IetListDTO IetToInandexListDTO(Iet iet);

    /**
     * 将实体类转成iet的详情dto
     * @param iet
     * @return
     */
    IetDetailDTO IetToInandexDetailDTO(Iet iet);
}
