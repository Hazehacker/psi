package com.zeroone.star.storemanagement.convertor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeroone.star.project.dto.j2.store.*;
import com.zeroone.star.project.dto.j2.store.*;
import com.zeroone.star.storemanagement.entity.EntryDO;
import com.zeroone.star.storemanagement.entity.EntryInfoDO;
import org.mapstruct.*;

import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.zeroone.star.storemanagement.entity.EntryInfoDO;
import com.zeroone.star.storemanagement.entity.ExtryInfoDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" ,uses={JsonTypeConvertor.class})
public interface MsEntryMapper {
    /**
     * 映射DTO
     * @param entity 实体
     * @return DTO
     */
    @Mapping(target = "file", source = "file", qualifiedByName = "jsonToFile")
    @Mapping(target = "logistics", source = "logistics", qualifiedByName = "jsonToLogistics")
    OtherInListDetailDTO entryToOtherInListDetailDTO(EntryDO entity);

    /**
     * 映射DTO
     * @param entity ExtryInfoDO
     * @return DTO
     */
    OtherOutListInfoDTO extryInfoToOtherOutListInfoDTO(ExtryInfoDO entity);

    /**
     * 映射实体
     * @param dto EntryAddDTO
     * @return 实体
     */
    @Mapping(target = "file", source = "file", qualifiedByName = "fileToJson")
    @Mapping(target = "logistics", source = "logistics", qualifiedByName = "logisticsToJson")
    @Mapping(target = "more", expression = "java(convertMoreToString(dto.getMore()))")
    EntryDO addDtoToEntry(OtherInListAddDTO dto);

    /**
     * 映射实体
     * @param dto OtherInListDetailDto
     * @return 实体
     */
    @Mapping(target = "file", source = "file", qualifiedByName = "fileToJson")
    @Mapping(target = "logistics", source = "logistics", qualifiedByName = "logisticsToJson")
    @Mapping(target = "more", expression = "java(convertMoreToString(dto.getMore()))")
    EntryDO otherInListDetailDtoToEntry(OtherInListDetailDTO dto);
    default String convertMoreToString(Object more) {
        if (more == null) {
            return null;
        }
        try {
            if (more instanceof String) {
                return (String) more;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(more);
        } catch (Exception e) {
            throw new RuntimeException("more字段转换失败", e);
        }
    }


//    EntryInfoDO addDtoToEntryInfo(OtherInListAddDTO dto);

    EntryInfoDO addDetailDtoToEntryInfo(OtherInListDetailInfoDTO detail);
//    EntryDO entryDtoToEntry(OtherInListDTO dto);

    /**
     * OtherInImportExcelDTO  --> EntryDO
     * @return
     */
    @Mapping(source = "total1", target = "total")
    @Mapping(source = "data1", target = "data")
    @Mapping(target = "time", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "people", ignore = true)
    @Mapping(target = "logistics", ignore = true)
    EntryDO otherInImportExcelDTOToEntryDO(OtherInImportExcelDTO otherInImportExcelDTO);
    /**
     * OtherInImportExcelDTO  --> EntryDO 的before方法，填充type和logistics
     */
    @BeforeMapping
    default void otherInImportExcelDTOToEntryDOBefore(OtherInImportExcelDTO source, @MappingTarget EntryDO target){
        if(source.getType().equals("其它入库单")) {
            target.setType(0);
        }
        else if(source.getType().equals("盘盈单")){
            target.setType(1);
        }
        else{
            target.setType(-1);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", "auto");
        jsonObject.put("name", "自动识别");
        jsonObject.put("number", source.getLogistics());
        target.setLogistics(jsonObject.toJSONString());
    }


    /**
     * OtherInImportExcelDTO --> EntryInfoDO
     */
    @Mapping(source = "total2", target = "total")
    @Mapping(source = "data2", target = "data")
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "mfd", ignore = true)
    EntryInfoDO otherInImportExcelDTOToEntryInfoDO(OtherInImportExcelDTO otherInImportExcelDTO);
    /**
     * OtherInImportExcelDTO --> EntryInfoDO 的List方法
     */
    List<EntryInfoDO> otherInImportExcelDTOListToEntryInfoDOList(List<OtherInImportExcelDTO> otherInImportExcelDTOList);

    /**
     * EntryInfoDO --> OtherInDetailExportExcel
     * @param entryInfoDO
     * @return
     */
    OtherInDetailExportExcelDTO entryInfoDOToOtherInDetailExportExcel(EntryInfoDO entryInfoDO);


    /**
     * EntryDO --> OtherInEasyExportExcelDTO
     * @param entryDO
     * @return
     */
    OtherInEasyExportExcelDTO entryDOToOtherInEasyExportExcelDTO(EntryDO entryDO);
    /**
     * EntryDO --> OtherInEasyExportExcelDTO 的List映射
     */
    List<OtherInEasyExportExcelDTO> entryDOListToOtherInEasyExportExcelDTOList (List<EntryDO> entryDOList);
}
