package com.zeroone.star.supportinfo.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.attachment.AnnexDTO;
import com.zeroone.star.supportinfo.entity.Annex;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 附件 Mapper 接口
 * </p>
 *
 * @author kai
 * @since 2025-10-23
 */
@Mapper
public interface AnnexMapper extends BaseMapper<Annex> {

}
