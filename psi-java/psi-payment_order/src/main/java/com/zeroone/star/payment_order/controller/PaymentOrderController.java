package com.zeroone.star.payment_order.controller;

import com.zeroone.star.payment_order.service.PaymentOrderService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.payment_order.*;
import com.zeroone.star.project.j6.payment_order.PaymentOrderApis;
import com.zeroone.star.project.query.j6.payment_order.OmyQuery;
import com.zeroone.star.project.vo.JsonVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/payment-order")
@Api(tags = "付款单管理")
public class PaymentOrderController implements PaymentOrderApis {

    @Autowired
    private PaymentOrderService paymentOrderService;

    /**
     * 新增付款单
     * @param addDto 新增付款单参数
     * @return 新增结果
     */
    @PostMapping("/add")
    @Override
    public JsonVO<String> addOmyWithInfo(@Validated @RequestBody OmyWithInfoAddDTO addDto) {
        String result = paymentOrderService.insertOmyWithInfo(addDto);
        return JsonVO.success(result);
    }


    /**
     * 修改付款单
     * @param dto 修改付款单参数
     * @return 修改结果
     */
    @PutMapping("/modify")
    @Override
    public JsonVO<String> modifyOmyAll(@Validated @RequestBody OmyAllDTO dto,String id) {
        paymentOrderService.updateOmyAll(dto,id);
        return JsonVO.success("修改付款单成功");
    }

    /**
     * 获取付款单列表
     * @param condition 查询条件
     * @return 查询结果
     */
    @GetMapping("query-all")
    @Override
    public JsonVO<PageDTO<OmyWithBillDTO>> queryAllWithBill(@Validated  OmyQuery condition) {
        PageDTO<OmyWithBillDTO> result = paymentOrderService.listOmyWithBill(condition);
        return JsonVO.success(result);
    }

    /**
     * 获取指定付款单详情
     * @param id 付款单id
     * @return 详情
     */
    @GetMapping("/query-by-id/{id}")
    @Override
    public JsonVO<OmyWithInfoDTO> queryByIdWithInfo(@PathVariable String id) {
        OmyWithInfoDTO result = paymentOrderService.getOmyWithInfoById(id);
        return JsonVO.success(result);
    }

    /**
     * 删除付款单
     * @param ids 删除的付款单id
     * @return 删除结果
     */
    @DeleteMapping("/remove")
    @Override
    public JsonVO<String> removeOmy(@RequestBody List<String> ids) {
        String result = paymentOrderService.removeOmy(ids);
        return JsonVO.success(result);
    }

    /**
     * 修改付款单审核状态(审核/反审核)
     * @param ids 修改的付款单id
     * @return 修改结果
     */
    @PutMapping("/modify-examine-status")
    @Override
    public JsonVO<String> modifyExamineStatus(@RequestBody List<String> ids) {
        String result = paymentOrderService.updateExamineStatus(ids);
        return JsonVO.success(result);
    }
}
