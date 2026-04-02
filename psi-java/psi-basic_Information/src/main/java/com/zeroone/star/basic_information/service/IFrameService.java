package com.zeroone.star.basic_information.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.basic_information.entity.Frame;
import com.zeroone.star.project.dto.j6.basic_information.frame.FrameDTO;

import java.util.List;

/**
 * <p>
 * 获取组织结构树 服务类
 * </p>
 *
 * @author 温昊璇
 * @since 2025-10-21
 */
public interface IFrameService extends IService<Frame> {
    List<FrameDTO> getFrame();
}
