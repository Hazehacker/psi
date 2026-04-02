package com.zeroone.star.payment_order.service;

import com.zeroone.star.payment_order.entity.Omy;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.payment_order.*;
import com.zeroone.star.project.query.j6.payment_order.OmyQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface PaymentOrderService extends IService<Omy>{

    String insertOmyWithInfo(OmyWithInfoAddDTO addDto);
    String updateOmyAll(OmyAllDTO dto,String id);
    PageDTO<OmyWithBillDTO> listOmyWithBill(OmyQuery condition);
    OmyWithInfoDTO getOmyWithInfoById(String id);
    String removeOmy(List<String> ids);
    String updateExamineStatus(List<String> ids);

}
