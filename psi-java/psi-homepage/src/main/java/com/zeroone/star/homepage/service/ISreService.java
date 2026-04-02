package com.zeroone.star.homepage.service;

import com.zeroone.star.homepage.entity.Sre;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.j1.homepage.SreCountsDTO;

import java.util.List;

/**
 * <p>
 * 销售退货单 服务类
 * </p>
 *
 * @author MRME39
 * @since 2025-10-23
 */
public interface ISreService extends IService<Sre> {
    List<SreCountsDTO> Srecounts();
}
