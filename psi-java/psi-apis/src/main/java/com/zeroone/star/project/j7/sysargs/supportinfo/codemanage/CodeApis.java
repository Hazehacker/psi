package com.zeroone.star.project.j7.sysargs.supportinfo.codemanage;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage.*;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.codemanage.CodeDetailQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.codemanage.CodeFormQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.codemanage.CodeImgQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j7.sysargs.supportinfo.codemanage.CodeVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

/**
 * 类名：CodeApis
 * 包名：com.zeroone.star.project.j7.sysargs.supportinfo.codemanage
 * 描述：条码管理接口
 * 作者：hh
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
public interface CodeApis {



    /**
     * 获取条码列表（带条件分页）
     * @param query 查询参数
     * @return 条码列表
     */
    JsonVO<PageDTO<CodeListDTO>> getCodeList(CodeFormQuery query);

    /**
     * 获取指定条码详情
     * @param query 查询参数
     * @return 条码详情
     */
    JsonVO<CodeDetailDTO> getCodeDetail(CodeDetailQuery query);

    /**
     * 获取指定条码图片
     * @param query 查询参数
     * @return 条码图片数据(base64格式)
     */
    JsonVO<String> getCodeImage(CodeImgQuery query);

    /**
     * 新增条码
     * @param codeTO
     * @return
     */
    JsonVO<String> addCode(CodeDTO codeTO);

    /**
     * 修改条码
     * @param codeTO
     * @return
     */
    JsonVO<String> modifyCode(UpdateCodeDTO codeTO);

    /**
     * 删除条码批量
     * @param ids
     * @return
     */
    JsonVO<List<String>> removeCodes(List<String> ids);

    JsonVO<List<CodeVO>> importCode(MultipartFile file);
    /**
     * 导出exlce
     * @param codeList 导出数据
     * @return
     */

     void exportCode(@RequestBody  List<ExportCodeDTO> codeList);
}
