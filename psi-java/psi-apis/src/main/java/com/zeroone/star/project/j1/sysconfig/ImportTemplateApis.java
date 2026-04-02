package com.zeroone.star.project.j1.sysconfig;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j1.sysconfig.TemplateAddDTO;
import com.zeroone.star.project.dto.j1.sysconfig.TemplateUpdateDTO;
import com.zeroone.star.project.query.j1.sysconfig.TempImportQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j1.sysconfig.TempImportDownloadVO;
import com.zeroone.star.project.vo.j1.sysconfig.TempImportVO;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * <p>
 * 描述：导入模版
 * </p>
 *
 * @author heavydrink
 * @version 1.0.0
 */
public interface ImportTemplateApis {
    /*
    * 添加模板
    *@param 模板添加数据对象
    *@return 添加结果
    * @author cwznb
    * */
    JsonVO<String> importTemplate(TemplateAddDTO templateAddDTO);


    /*
     * 更新模板
     *@param 模板修改数据对象
     *@return 修改结果
     * @author cwznb
     * */
    JsonVO<String> updateTemplate(String id,TemplateUpdateDTO templateUpdateDTO);

    /*
     * 删除模板
     *@param 模板id
     *@return 删除结果
     * @author cwznb
     * */
    JsonVO<String> deleteTemplate(String id);

    /*
     * 查询导入模板列表
     *
     * @param query 查询条件，包含 templateName, templateCode, status, page, size 等
     * @return 分页结果，data 为 PageDTO<TmplImportVO>
     */
    JsonVO<PageDTO<TempImportVO>> queryTemplateList(TempImportQuery query);

    /*
     * 查询指定导入模板的详细信息（下载）
     *
     * @param id 模板唯一标识
     * @return 包含downloadUrl的信息
     */

    JsonVO<TempImportDownloadVO> getDownloadInfoById(String id);

}
