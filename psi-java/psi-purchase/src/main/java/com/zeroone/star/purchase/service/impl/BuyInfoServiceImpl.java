package com.zeroone.star.purchase.service.impl;

import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteInfoDTO;
import com.zeroone.star.purchase.DO.BuyInfoDO;
import com.zeroone.star.purchase.mapper.BuyInfoMapper;
import com.zeroone.star.purchase.service.IBuyInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 * 采购单详情 服务实现类
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-27
 */
@Service
public class BuyInfoServiceImpl extends ServiceImpl<BuyInfoMapper, BuyInfoDO> implements IBuyInfoService {

    /**
     * 批量查询采购单详情
     * author: Hazenix 
     */
    @Override
    public List<BuyInfoDO> selectBatchByPids(List<String> ids) {
        return lambdaQuery().in(BuyInfoDO::getPid, ids).list();

    }


    /**
     * 获取指定采购单详情
     * @param id
     * @return
     * @author: Hazenix 
     */
    @Override
    public PurchaseNoteInfoDTO getPurchaseNoteInfo(String id) {
        // 根据id查询采购单详情
        // baseMapper对应BuyInfoMapper
        PurchaseNoteInfoDTO purchaseNoteInfoDTO = baseMapper.getPurchaseNoteInfoDetail(id);

        // 查询结果为null，直接返回
        if(purchaseNoteInfoDTO == null){
            return null;
        }

        return purchaseNoteInfoDTO;
    }
}
