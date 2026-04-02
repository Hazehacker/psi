package com.zeroone.star.finance.service.impl.otherincome;

import com.zeroone.star.finance.entity.Otherincomeexportdetailreportview;
import com.zeroone.star.finance.mapper.OtherincomeexportdetailreportviewMapper;
import com.zeroone.star.finance.service.IOtherincomeexportdetailreportviewService;
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
public class OtherincomeexportdetailreportviewServiceImpl implements IOtherincomeexportdetailreportviewService {

    @Autowired
    private OtherincomeexportdetailreportviewMapper otherincomeexportdetailreportviewMapper;

    @Override
    public List<Otherincomeexportdetailreportview> list(List<String> iceIds) {
        return otherincomeexportdetailreportviewMapper.list(iceIds);
    }
}
