package com.zeroone.star.finance.service;

import com.zeroone.star.finance.entity.Oce;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.vo.JsonVO;

import java.util.List;

/**
 * <p>
 * 其它支出单 服务类
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
public interface IOceService extends IService<Oce> {

    public JsonVO<String> deleteOtherExpenseForm(List<String> Ids);

}
