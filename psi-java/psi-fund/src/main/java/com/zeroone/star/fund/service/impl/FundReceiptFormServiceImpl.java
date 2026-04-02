package com.zeroone.star.fund.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.fund.entity.ImyInfo;
import com.zeroone.star.fund.mapper.ImyInfoMapper;
import com.zeroone.star.fund.mapper.ImyMapper;
import com.zeroone.star.fund.service.FundReceiptFormService;
import com.zeroone.star.fund.util.mapstruct.ReceiptMapper;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.fund.FundReceiptDetailDTO;
import com.zeroone.star.project.dto.j4.fund.FundReceiptListDTO;
import com.zeroone.star.project.query.j4.fund.FundReceiptListQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundReceiptFormServiceImpl implements FundReceiptFormService {
    @Autowired
    private ImyInfoMapper imyInfoMapper;

    @Autowired
    private ImyMapper imyMapper;

    @Override
    public JsonVO<FundReceiptDetailDTO> selectFundReceiptDetail(String id) {
        LambdaQueryWrapper<ImyInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ImyInfo::getPid, id);
        ImyInfo imyInfo = imyInfoMapper.selectOne(queryWrapper);
        FundReceiptDetailDTO dto = ReceiptMapper.INSTANCE.toDTO(imyInfo);
        if (dto == null) {
            return JsonVO.fail(dto);
        }
        return JsonVO.success(dto);
    }



    @Override
    public JsonVO<PageDTO<FundReceiptListDTO>> pageList(FundReceiptListQuery query) {
        IPage<FundReceiptListDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        IPage<FundReceiptListDTO> resultPage = imyMapper.listAll(page, query);
        // 转换为PageDTO（根据实际结构调整）
        PageDTO<FundReceiptListDTO> res = new PageDTO<>(
                  resultPage.getCurrent(),
                resultPage.getSize(),
                resultPage.getTotal(),
                resultPage.getPages(),
                resultPage.getRecords()
        );

        return JsonVO.success(res);
    }
}
