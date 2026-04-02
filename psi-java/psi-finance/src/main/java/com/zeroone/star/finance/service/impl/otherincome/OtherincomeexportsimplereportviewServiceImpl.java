package com.zeroone.star.finance.service.impl.otherincome;

import com.zeroone.star.finance.entity.Otherincomeexportsimplereportview;
import com.zeroone.star.finance.mapper.OtherincomeexportsimplereportviewMapper;
import com.zeroone.star.finance.service.IOtherincomeexportsimplereportviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2025-11-02
 */
@Service
public class OtherincomeexportsimplereportviewServiceImpl implements IOtherincomeexportsimplereportviewService {

    @Autowired
    private OtherincomeexportsimplereportviewMapper mapper;

    @Override
    public List<Otherincomeexportsimplereportview> list(List<String> iceIds) {
        return mapper.list(iceIds);
    }
}
