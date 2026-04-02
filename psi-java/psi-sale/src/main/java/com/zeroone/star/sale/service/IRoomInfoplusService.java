package com.zeroone.star.sale.service;

import com.zeroone.star.sale.entity.RoomInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.sale.entity.SreInfo;

import java.util.List;

/**
 * <p>
 * 仓储详情 服务类
 * </p>
 *
 * @author author
 * @since 2025-10-26
 */
public interface IRoomInfoplusService extends IService<RoomInfo> {

    void saveBySreInfo(List<SreInfo> list);


    void deleteBySreInfo(List<SreInfo> list);
}
