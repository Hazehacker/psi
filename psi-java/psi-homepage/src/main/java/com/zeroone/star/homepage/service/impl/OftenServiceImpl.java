package com.zeroone.star.homepage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.star.homepage.entity.Often;
import com.zeroone.star.homepage.mapper.OftenMapper;
import com.zeroone.star.homepage.service.IOftenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.project.dto.j1.homepage.CommonMenuDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 常用功能 服务实现类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Service
public class OftenServiceImpl extends ServiceImpl<OftenMapper, Often> implements IOftenService {

    @Resource
    private OftenMapper oftenMapper;
    @Resource
    private UserHolder userHolder;

    @Transactional
    @Override
    public List<CommonMenuDTO> getCommonMenus() {
        try{
            String userId = userHolder.getCurrentUser().getId();
            List<CommonMenuDTO> list = oftenMapper.getCommonMenus(userId);
            return list == null ? Collections.emptyList() : list;
        }catch (Exception e){
            return Collections.emptyList();
        }

    }
}
