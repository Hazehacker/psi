package com.zeroone.star.homepage.service;

import com.zeroone.star.homepage.entity.Imy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.j1.homepage.ImyCountsDTO;

import java.util.List;

/**
 * <p>
 * 收款单 服务类
 * </p>
 *
 * @author MRME39
 * @since 2025-10-23
 */
public interface IImyService extends IService<Imy> {
    List<ImyCountsDTO> Imycounts();
}
