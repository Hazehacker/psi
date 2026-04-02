package com.zeroone.star.capital.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.capital.DO.BillInfoDO;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * <p>
 * 核销单详情 服务类
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
public interface IBillInfoService extends IService<BillInfoDO> {

    /**
     * 导出详细报表
     * @author 小王
     * @param ids
     * @return
     */
    ResponseEntity<byte[]> exportBillInfo(List<String> ids);

}
