package com.zeroone.star.sysconfig.mapper;

import com.zeroone.star.project.query.j1.sysconfig.TempImportQuery;
import com.zeroone.star.project.vo.j1.sysconfig.TempImportDownloadVO;
import com.zeroone.star.project.vo.j1.sysconfig.TempImportVO;
import com.zeroone.star.sysconfig.entity.TmplImport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 导入模板 Mapper 接口
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Mapper
public interface TmplImportMapper extends BaseMapper<TmplImport> {


    TempImportDownloadVO selectDownloadInfoById(@Param("id") String id);
}
