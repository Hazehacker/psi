package com.zeroone.star.basic_information.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.basic_information.entity.Frame;
import com.zeroone.star.basic_information.mapper.FrameMapper;
import com.zeroone.star.basic_information.service.IFrameService;
import com.zeroone.star.project.dto.j6.basic_information.frame.FrameDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Service
public class FrameServiceImpl extends ServiceImpl<FrameMapper, Frame> implements IFrameService {
    @Resource
    MsFrameMapper ms;
    /**
     * 获取完整的组织结构树
     */
    public List<FrameDTO> getFrame() {
        // 从数据库查询所有 DO
        List<Frame> allFrame = baseMapper.selectList(null);

        // 转换为 DTO
        List<FrameDTO> allDTOs = ms.framesToFrameDtos(allFrame);

        // 构建树形结构
        return buildTree(allDTOs, null);
    }

    /**
     * 递归构建树形结构
     * @param allNodes 所有节点列表
     * @param parentId 父节点ID，根节点为null
     * @return 构建好的子树
     */
    private List<FrameDTO> buildTree(List<FrameDTO> allNodes, String parentId) {
        return allNodes.stream()
                // 过滤出当前父节点的直接子节点
                .filter(node -> Objects.equals(node.getPid(), parentId))
                // 递归构建子树
                .peek(node -> {
                    List<FrameDTO> children = buildTree(allNodes, node.getId());
                    node.setChildren(children);
                    node.setHasChildren(!children.isEmpty());
                })
                // 按sort字段排序
                .sorted(Comparator.comparing(FrameDTO::getSort))
                .collect(Collectors.toList());
    }
}
