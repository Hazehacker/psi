package com.zeroone.star.payment_order.service.impl;

import com.zeroone.star.payment_order.entity.Omy;
import com.zeroone.star.payment_order.entity.OmyInfo;
import com.zeroone.star.project.dto.j6.payment_order.*;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * <p>
 * 描述：PaymentOrder对应MapStruct映射接口
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@Mapper(componentModel = "spring")
public interface MsPaymentOrderMapper {

    /**
     * DO转DTO映射
     * @param entity 实体
     * @return DTO
     */
    OmyDTO toOmyDTO(Omy entity);
    OmyInfoDTO toOmyInfoDTO(OmyInfo entity);

    /**
     * DTO转DO映射
     * @param dto
     * @return 实体
     */
    Omy toOmy(OmyWithInfoAddDTO dto);
    OmyInfo toOmyInfo(OmyInfoAddDTO dto);
    Omy toOmy(OmyAllDTO dto);

}

