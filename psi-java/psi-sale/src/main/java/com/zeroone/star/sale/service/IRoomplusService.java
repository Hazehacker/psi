package com.zeroone.star.sale.service;

import com.zeroone.star.sale.entity.Room;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 仓储信息 服务类
 * </p>
 *
 * @author author
 * @since 2025-10-26
 */
public interface IRoomplusService extends IService<Room> {

    void updateAndsave(List<Room> rooms);

    boolean updateAndDelete(List<Room> rooms);
}
