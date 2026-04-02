package com.zeroone.star.moneytransfer.service;

import com.zeroone.star.moneytransfer.entity.AllotInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.j7.money.transfer.AllotInfoDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 转账单详情 服务类
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
public interface IAllotInfoService extends IService<AllotInfo> {

    String saveAllotInfo(AllotInfoDTO allotInfoDTO);


    List<String> getAccountStringList();

}
