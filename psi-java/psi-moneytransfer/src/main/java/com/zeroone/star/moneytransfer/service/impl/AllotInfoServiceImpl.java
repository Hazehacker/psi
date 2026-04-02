package com.zeroone.star.moneytransfer.service.impl;

import com.zeroone.star.moneytransfer.entity.AllotInfo;
import com.zeroone.star.moneytransfer.mapper.AllotInfoMapper;
import com.zeroone.star.moneytransfer.service.IAllotInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.j7.money.transfer.AllotInfoDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 转账单详情 服务实现类
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
@Service
public class AllotInfoServiceImpl extends ServiceImpl<AllotInfoMapper, AllotInfo> implements IAllotInfoService {

    @Resource
    MsAllotInfoMapper msAllotInfoMapper;

    @Resource
    AllotInfoMapper allotInfoMapper;

    @Override
    public String saveAllotInfo(AllotInfoDTO allotInfoDTO) {
        AllotInfo allotInfo=msAllotInfoMapper.allotInfoDTOToAllotInfo(allotInfoDTO);
        baseMapper.insert(allotInfo);

        return allotInfo.getId();
    }

    @Override
    public List<String> getAccountStringList() {
        return allotInfoMapper.getAccountStringList();
    }
}
