package com.zeroone.star.storemanagement.convertor;


import com.zeroone.star.project.dto.j2.store.OtherOutListDTO;
import com.zeroone.star.project.dto.j2.store.OtherOutListInfoDTO;
import com.zeroone.star.storemanagement.entity.ExtryDO;
import com.zeroone.star.storemanagement.entity.ExtryInfoDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" ,uses={JsonTypeConvertor.class})
public interface MsExtryMapper {
    /**
     * 映射DTO
     * @param entity 实体
     * @return DTO
     */


    /**
     * 映射DTO
     * @param entity ExtryInfoDO
     * @return DTO
     */
    OtherOutListInfoDTO extryInfoToOtherOutListInfoDTO(ExtryInfoDO entity);

    /**
     * 映射实体
     * @param dto ExtryAddDTO
     * @return 实体
     */
//    @Mapping(target = "file", source = "file", qualifiedByName = "fileToJson")
//    @Mapping(target = "logistics", source = "logistics", qualifiedByName = "logisticsToJson")
//    ExtryDO addDtoToExtry(OtherOutListDTO dto);

    /**
     * 映射实体
     * @param dto OtherOutListDetailDto
     * @return 实体
     */



    ExtryInfoDO addDtoToExtryInfo(OtherOutListDTO dto);

    ExtryInfoDO addDetailDtoToExtryInfo(OtherOutListInfoDTO detail);


    /**
     * 映射实体
     * @param otherOutListDTO OtherOutListDTO
     * @return 实体
     */
    ExtryDO otherOutListDtoToExtry(OtherOutListDTO otherOutListDTO);
}
