package com.zeroone.star.finance.service.impl.otherincome;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.finance.entity.Ice;
import com.zeroone.star.finance.entity.IceInfo;
import com.zeroone.star.finance.entity.Iet;
import com.zeroone.star.finance.mapper.otherincome.IceMapper;
import com.zeroone.star.finance.service.IIceInfoService;
import com.zeroone.star.finance.service.IIceService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.otherincome.*;
import com.zeroone.star.project.query.j8.finance.otherincome.OtherIncomeQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 其它收入单 服务实现类
 * </p>
 *
 * @author Hazenix
 * @since 2025-10-24
 */
@Service
public class IceServiceImpl extends ServiceImpl<IceMapper, Ice> implements IIceService {

    @Resource
    private IIceInfoService iceInfoService;

    @Override
    public PageDTO<OtherIncomeDTO> listIncomeOrders(OtherIncomeQuery query) {
        // 构建分页查询对象
        Page<Ice> entityPage = new Page<>(query.getPageIndex(), query.getPageSize());
        baseMapper.selectOtherIncomePage(entityPage, query);
        return PageDTO.create(entityPage, (entity) -> {
            OtherIncomeDTO dto = new OtherIncomeDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
    }

    @Override
    public OtherIncomeDetailDTO getIncomeOrder(String id) {
        Ice entity = baseMapper.selectOtherIncomeById(id);
        if (entity == null) {
            return null;
        }
        OtherIncomeDetailDTO dto = new OtherIncomeDetailDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getIceBills() != null && !entity.getIceBills().isEmpty()) {
            List<OtherIncomeBillDTO> billDTOs = entity.getIceBills().stream()
                    .map(billEntity -> {
                        OtherIncomeBillDTO billDTO = new OtherIncomeBillDTO();
                        BeanUtils.copyProperties(billEntity, billDTO);
                        return billDTO;
                    })
                    .collect(Collectors.toList());
            dto.setBill(billDTOs);
        }

        //  【关键】手动处理嵌套的明细信息列表
        if (entity.getIceInfos() != null && !entity.getIceInfos().isEmpty()) {
            List<OtherIncomeInfoDTO> infoDTOs = entity.getIceInfos().stream()
                    .map(infoEntity -> {
                        OtherIncomeInfoDTO infoDTO = new OtherIncomeInfoDTO();
                        BeanUtils.copyProperties(infoEntity, infoDTO);

                        if (infoEntity.getIetData() != null) {
                            IncomeExpendTypeDTO ietDTO = new IncomeExpendTypeDTO();
                            BeanUtils.copyProperties(infoEntity.getIetData(), ietDTO);
                            infoDTO.setIetData(ietDTO);
                        }


                        return infoDTO;
                    })
                    .collect(Collectors.toList());
            dto.setInfo(infoDTOs);
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addIncomeOrder(AddOtherIncomeDTO dto) {
        BigDecimal total = dto.getTotal();
        BigDecimal actual = dto.getActual();
        BigDecimal money = dto.getMoney();
        // 业务规则：单据金额 >= 实际金额 >= 实收金额
        if (total.compareTo(actual) < 0) {
            throw new IllegalArgumentException("单据金额不能小于实际金额");
        }
        if (actual.compareTo(money) < 0) {
            throw new IllegalArgumentException("实际金额不能小于实收金额");
        }

        if (baseMapper.checkFrame(dto.getFrame()) == 0) {
            throw new IllegalArgumentException("所属组织不存在，请检查参数");
        }
        if (baseMapper.checkAccount(dto.getAccount()) == 0) {
            throw new IllegalArgumentException("结算账户不存在，请检查参数");
        }
        if (baseMapper.checkUser(dto.getUser()) == 0) {
            throw new IllegalArgumentException("制单人不存在，请检查参数");
        }
        if (StringUtils.isNotBlank(dto.getCustomer()) && baseMapper.checkCustomer(dto.getCustomer()) == 0) {
            throw new IllegalArgumentException("客户不存在，请检查参数");
        }
        if (StringUtils.isNotBlank(dto.getPeople()) && baseMapper.checkPeople(dto.getPeople()) == 0) {
            throw new IllegalArgumentException("关联人员不存在，请检查参数");
        }
        //转化为实体
        Ice incomeOrder = new Ice();
        BeanUtils.copyProperties(dto, incomeOrder);
        incomeOrder.setExamine(0);
        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            incomeOrder.setNucleus(0); // 未核销
        } else if (money.compareTo(actual) < 0) {
            incomeOrder.setNucleus(1); // 部分核销
        } else { // money >= actual
            incomeOrder.setNucleus(2); // 已核销
        }
        baseMapper.insert(incomeOrder);
        String primaryId = incomeOrder.getId();
        List<AddOtherIncomeInfoDTO> infoDTOs = dto.getInfo();
        if (!CollectionUtils.isEmpty(infoDTOs)) {
            List<String> ietIds = infoDTOs.stream().map(AddOtherIncomeInfoDTO::getIet).distinct().collect(Collectors.toList());
            // 一次性查询所有 Iet 对象，并放入 Map
            Map<String, Iet> ietMap = baseMapper.selectIncomeExpendTypeByIds(ietIds).stream()
                    .collect(Collectors.toMap(Iet::getId, Function.identity()));
            List<IceInfo> iceInfoList = infoDTOs.stream().map(infoDTO -> {
                if (!ietMap.containsKey(infoDTO.getIet())) {
                    throw new IllegalArgumentException("明细中包含无效的收支类型ID：" + infoDTO.getIet());
                }
                IceInfo iceInfo = new IceInfo();
                BeanUtils.copyProperties(infoDTO, iceInfo);
                iceInfo.setPid(primaryId);
                iceInfo.setIetData(ietMap.get(infoDTO.getIet()));
                return iceInfo;
            }).collect(Collectors.toList());

            BigDecimal detailsTotal = iceInfoList.stream()
                    .map(IceInfo::getMoney)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (incomeOrder.getTotal().compareTo(detailsTotal) != 0) {
                throw new IllegalArgumentException("单据总金额[" + incomeOrder.getTotal() + "]与所有明细金额之和[" + detailsTotal + "]不一致");
            }
            incomeOrder.setIceInfos(iceInfoList);
            iceInfoService.saveBatch(iceInfoList);
        }

//        List<AddOtherIncomeBillDTO> billDTOs = dto.getBill();
//        if (!CollectionUtils.isEmpty(billDTOs)) {
//            List<IceBill> iceBillList = billDTOs.stream().map(billDTO -> {
//                IceBill iceBill = new IceBill();
//                BeanUtils.copyProperties(billDTO, iceBill);
//                iceBill.setPid(primaryId);
//                return iceBill;
//            }).collect(Collectors.toList());
//            incomeOrder.setIceBills(iceBillList);
//            iceBillService.saveBatch(iceBillList);
//        }

        return incomeOrder.getId();
    }
}
