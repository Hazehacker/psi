package com.zeroone.star.sysconfig.service;

import com.zeroone.star.project.dto.j1.sysconfig.TemplateAddDTO;
import com.zeroone.star.project.dto.j1.sysconfig.TemplateUpdateDTO;
import com.zeroone.star.project.query.j1.sysconfig.TempImportQuery;
import com.zeroone.star.project.vo.j1.sysconfig.TempImportDownloadVO;
import com.zeroone.star.project.vo.j1.sysconfig.TempImportVO;
import com.zeroone.star.sysconfig.entity.TmplImport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.PageDTO;

import java.util.List;

/**
 * <p>
 * 导入模板 服务类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
public interface ITmplImportService extends IService<TmplImport> {
    /*
    * 导入模板服务
    *
    * @author cwznb
    * @since 2025-10-29
    */
     int importTemplate(TemplateAddDTO templateAddDTO);

     int deleteTemplate(String id);

     int updateTemplate(TemplateUpdateDTO templateUpdateDTO);

    PageDTO<TempImportVO> queryTemplateList(TempImportQuery query);

    TempImportDownloadVO getDownloadInfoById(String id);
}
