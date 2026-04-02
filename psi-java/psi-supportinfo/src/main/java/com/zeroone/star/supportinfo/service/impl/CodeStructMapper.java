package com.zeroone.star.supportinfo.service.impl;

import com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage.CodeDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage.CodeDetailDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage.CodeListDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage.UpdateCodeDTO;
import com.zeroone.star.supportinfo.entity.Code;
import org.mapstruct.Mapper;

/**
 * 条码实体与DTO的映射接口
 * @author: star
 * @date: 2025/10/24
 */
@Mapper(componentModel = "spring")
public interface CodeStructMapper {

    /**
     * Code实体转CodeListDTO
     * （实体type为Integer，直接映射到DTO的type，无需转换）
     */
    CodeListDTO toCodeListDTO(Code code);

    /**
     * Code实体转CodeDetailDTO
     * （实体type为Integer，直接映射到DTO的type，无需转换）
     */
    CodeDetailDTO toCodeDetailDTO(Code code);

    CodeDTO codeToCodeDto(Code code);
    Code  codeDtoToCode(CodeDTO codeDTO);

    Code updateCodeDtoToCode(UpdateCodeDTO updateCodeDTO);
}