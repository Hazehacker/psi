package com.zeroone.star.homepage.service;

import com.zeroone.star.homepage.entity.Often;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.j1.homepage.CommonMenuDTO;

import java.util.List;

/**
 * <p>
 * 常用功能 服务类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
public interface IOftenService extends IService<Often> {

    List<CommonMenuDTO> getCommonMenus();

}
