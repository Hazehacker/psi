package com.zeroone.star.supportinfo.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.inandexpend.*;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.inandexpend.IetAddQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.inandexpend.IetQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.inandexpend.IetUpdateQuery;
import com.zeroone.star.supportinfo.entity.Iet;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 收支类别 服务类
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
public interface IIetService extends IService<Iet> {
    /**
     * 按收支类别查询收支名称
     * @param type
     * @return
     */
    List<IetNameDTO> selectIetName(Integer type);

    /**
     * 查询收支类别列表
     * @param query
     * @return
     */
    PageDTO<IetListDTO> listAll(IetQuery query);

    /**
     * 获取收支类别详情
     * @param id
     * @return
     */
    IetDetailDTO getInandexDetail(String id);


    String insertIet(IetAddDTO ietAddDto);
    String updateIet(IetUpdateDTO ietUpdateDTO);
    String deleteIet(String id);
}
