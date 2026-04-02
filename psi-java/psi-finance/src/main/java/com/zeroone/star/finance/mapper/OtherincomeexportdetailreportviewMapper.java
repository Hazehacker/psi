package com.zeroone.star.finance.mapper;

import com.zeroone.star.finance.entity.Otherincomeexportdetailreportview;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author zyj
 * @since 2025-11-02
 */
@Mapper
public interface OtherincomeexportdetailreportviewMapper {

    List<Otherincomeexportdetailreportview> list(List<String> iceIds);

}
