package com.zeroone.star.purchase.service;

import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteInfoDTO;
import com.zeroone.star.purchase.DO.BuyInfoDO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 采购单详情 服务类
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
public interface IBuyInfoService extends IService<BuyInfoDO> {

    List<BuyInfoDO> selectBatchByPids (List<String> pids);
    /**
     * @Description: 获取指定采购单详情
     * @param: id
     * @author 加减法
     */
    PurchaseNoteInfoDTO getPurchaseNoteInfo(@NotEmpty(message = "ID不能为空") String id);
}
