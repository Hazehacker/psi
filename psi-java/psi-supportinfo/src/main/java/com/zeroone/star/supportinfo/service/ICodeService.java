package com.zeroone.star.supportinfo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage.CodeDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage.UpdateCodeDTO;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.codemanage.CodeFormQuery;
import com.zeroone.star.supportinfo.entity.Code;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 条码 服务类
 * </p>
 *
 * @author hh
 * @since 2025-10-23
 */
public interface ICodeService extends IService<Code> {
    /**
     * 分页查询条码列表
     * @param query 查询条件
     * @return 分页结果
     */
    Page<Code> queryCodeList(CodeFormQuery query);

    /**
     * 根据ID查询条码详情
     * @param id 条码ID
     * @return 条码实体
     */
    Code getCodeById(String id);

    /**
     * 生成条码图片
     * @param id 条码ID
     * @param width 图片宽度
     * @param height 图片高度
     * @return Base64编码的图片字符串
     */
    String generateCodeImage(String id, int width, int height);

    String saveCode(CodeDTO codeDTO);

    List<String> removeCodes(List<String> ids);

    String updateCode(UpdateCodeDTO codeTO);
}
