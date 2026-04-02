package com.zeroone.star.project.j6.basic_information.frame;

import com.zeroone.star.project.dto.j6.basic_information.frame.FrameDTO;
import com.zeroone.star.project.vo.JsonVO;

import java.util.List;

/**
 * <p>
 * 描述：组织结构树获取接口
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 温昊璇
 * @version 1.0.0
 */
public interface FrameApis {
    //获取组织结构树接口
    JsonVO<List<FrameDTO>> getFrame();
}
