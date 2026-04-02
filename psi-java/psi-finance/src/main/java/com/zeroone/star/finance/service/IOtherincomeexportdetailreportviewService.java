package com.zeroone.star.finance.service;

import com.zeroone.star.finance.entity.Otherincomeexportdetailreportview;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author zyj
 * @since 2025-11-02
 */
public interface IOtherincomeexportdetailreportviewService {

    List<Otherincomeexportdetailreportview> list(List<String> iceIds);
}
