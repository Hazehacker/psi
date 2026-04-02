package com.zeroone.star.purchase.service.impl;

import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnGoodsDetailDTO;
import com.zeroone.star.project.vo.j3.purchase.PurchaseReturnDetailVO;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnItemsDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseUpdateDTO;
import com.zeroone.star.project.vo.JsonVO;

import com.zeroone.star.purchase.DO.BreDO;
import com.zeroone.star.purchase.DO.BreInfoDO;
import com.zeroone.star.purchase.mapper.BreInfoMapper;
import com.zeroone.star.purchase.service.IBreInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zeroone.star.purchase.service.IBreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 采购退货单详情 服务实现类
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
@Service
@RequiredArgsConstructor
public class BreInfoServiceImpl extends ServiceImpl<BreInfoMapper, BreInfoDO> implements IBreInfoService {
    private final BreServiceImpl breServiceImpl;

    /**
     * 更新采购退货单 实现
     *
     * @param purchaseUpdateDTO 要更新的参数列表
     * @return 状态码 1成功 0失败
     * @author 景旭
     */
    @Transactional
    public JsonVO<Integer> updateBreInfoDo(PurchaseUpdateDTO purchaseUpdateDTO) {
        String pid = purchaseUpdateDTO.getPid();
        List<PurchaseReturnItemsDTO> returnItems = purchaseUpdateDTO.getReturnItems();
        ArrayList<BreInfoDO> updateList = new ArrayList<>();
        returnItems.forEach(item -> {
            try {
                BreInfoDO copy = toBreInfoDo(item);
                updateList.add(copy);
            } catch (Exception e) {
                throw new RuntimeException("参数封装出现异常" + e.getMessage());
            }
        });
        updateDO(purchaseUpdateDTO, pid, updateList);
        return JsonVO.success(1);
    }

    /**
     * 此方法用于更新 bre 和 bre_info
     */
    private void updateDO(PurchaseUpdateDTO purchaseUpdateDTO, String pid, List<BreInfoDO> updateList) {
        remove(new LambdaQueryWrapper<BreInfoDO>().eq(BreInfoDO::getPid, pid));
        updateList.forEach(this::save);
        breServiceImpl.updateBreDo(purchaseUpdateDTO);
    }

    /**
     * 此方法用于将DTO转DO
     */
    private BreInfoDO toBreInfoDo(PurchaseReturnItemsDTO purchaseReturnItemsDTO) {
        BreInfoDO breInfoDO = new BreInfoDO();
        breInfoDO.setPid(purchaseReturnItemsDTO.getPid());
        breInfoDO.setGoods(purchaseReturnItemsDTO.getGoods());
        breInfoDO.setAttr(purchaseReturnItemsDTO.getAttr());
        breInfoDO.setUnit(purchaseReturnItemsDTO.getUnit());
        breInfoDO.setWarehouse(purchaseReturnItemsDTO.getWarehouse());
        breInfoDO.setBatch(purchaseReturnItemsDTO.getBatch());
        breInfoDO.setMfd(String.valueOf(dateformat(purchaseReturnItemsDTO.getMfd())));
        breInfoDO.setPrice(purchaseReturnItemsDTO.getPrice());
        breInfoDO.setNums(purchaseReturnItemsDTO.getNums());
        breInfoDO.setTotal(purchaseReturnItemsDTO.getTotal());
        breInfoDO.setData(purchaseReturnItemsDTO.getData());
        return breInfoDO;
    }

    /**
     * 删除采购退货单
     *
     * @param pids 要删除的采购退货单列表
     */
    @Transactional
    public JsonVO<String> deletePurchaseReturnForm(List<String> pids) {
        if (pids.stream().anyMatch(pid -> pid == null || pid.trim().isEmpty())) {
            return JsonVO.fail("参数为空");
        }
        DelBo res = remove(pids);
        if (res == null) {
            return JsonVO.success("删除成功");
        } else if (!res.have) {
            return JsonVO.fail(res.pid + "不存在,无法删除");
        } else return JsonVO.fail(res.pid + "已审核,无法删除");
    }

    /**
     * 移除两张表的订单数据
     */
    @Transactional
    public DelBo remove(List<String> pids) {
        for (String pid : pids) {
            DelBo checkResult = check(pid);  // 获取订单检查结果
            if (!checkResult.flag) {  // flag == false 表示订单不存在
                return checkResult;   // 如果订单不存在，返回 DelBo 信息
            }
            if (checkResult.have) {  // have == true 表示订单已审核
                return checkResult;  // 如果已审核，返回 DelBo 信息
            }
        }

        // 如果通过所有检查，进行删除操作
        this.remove(new LambdaQueryWrapper<BreInfoDO>().in(BreInfoDO::getPid, pids));
        breServiceImpl.removeByIds(pids);
        return null;  // 删除成功
    }

    /**
     * 检查订单是否存在及是否已审核
     */
    private DelBo check(String pid) {
        // 检查订单是否存在
        BreDO breDO = breServiceImpl.getById(pid);
        if (breDO == null) {
            return new DelBo(pid, false, false);  // 订单不存在
        }

        // 检查订单是否已审核
        Integer examine = breDO.getExamine();
        if (examine == 1) {
            return new DelBo(pid, false, true);  // 已审核，无法删除
        }

        return new DelBo(pid, true, true);  // 订单存在，且未审核
    }

    /**
     * 格式化时间为LocalDate
     */
    private LocalDate dateformat(String localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(localDate, formatter);
    }

    /**
     * 内部类 用于传递订单id和状态
     */
    @Data
    @AllArgsConstructor
    private static class DelBo {
        String pid;
        boolean flag;
        boolean have;
    }

    /**
     * 注入BreInfoMapper
     *
     * @Author：xiaoliu
     */
    @Resource
    private BreInfoMapper breInfoMapper;

    /**
     * 注入IBreService
     * @Author：xiaoliu
     */
    @Resource
    private IBreService iBreService;

    /**
     * @auther xiaoliu
     * 获取取指定采购退货单详情VO
     * @param id
     * @return
     */
    @Override
    public PurchaseReturnDetailVO getPurchaseReturnDetailVO(String id) {
        // 1.查询表bre，得到退货单数据
        BreDO breDO = iBreService.getById(id);

        // 2.BreDO转PurchaseReturnDetailVO
        PurchaseReturnDetailVO purchaseReturnDetailVO = new PurchaseReturnDetailVO();
        BeanUtils.copyProperties(breDO, purchaseReturnDetailVO);

        // 3.查询bre_info,goods表得 到采购退货商品详情DTO
        List<PurchaseReturnGoodsDetailDTO> purchaseReturnGoodsDetailDTOList = breInfoMapper.selectPurchaseReturnGoodsDetail(id);

        // 4.封装为VO对象
        purchaseReturnDetailVO.setDetails(purchaseReturnGoodsDetailDTOList);

        return purchaseReturnDetailVO;
    }
}
