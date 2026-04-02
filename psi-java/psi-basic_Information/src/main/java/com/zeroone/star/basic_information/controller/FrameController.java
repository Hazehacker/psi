package com.zeroone.star.basic_information.controller;

import com.zeroone.star.basic_information.service.IFrameService;
import com.zeroone.star.project.dto.j6.basic_information.frame.FrameDTO;
import com.zeroone.star.project.j6.basic_information.frame.FrameApis;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/basic_information/frame")
public class FrameController implements FrameApis {
    @Resource
    private IFrameService frameService;

    @GetMapping("/getFrame")
    public JsonVO<List<FrameDTO>> getFrame() {
        try {
            // 获取组织结构树
            List<FrameDTO> treeData = frameService.getFrame();
            return JsonVO.success(treeData);
        }
        catch (Exception e) {
            return JsonVO.fail(null);
        }
    }
}
