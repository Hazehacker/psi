package com.zeroone.star.finance.service;

import com.zeroone.star.finance.entity.Otherincomeexportsimplereportview;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author zyj
 * @since 2025-11-02
 */
public interface IOtherincomeexportsimplereportviewService{

    List<Otherincomeexportsimplereportview> list(List<String> iceIds);
}
