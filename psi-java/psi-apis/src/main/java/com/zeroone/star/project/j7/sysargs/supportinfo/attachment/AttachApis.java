package com.zeroone.star.project.j7.sysargs.supportinfo.attachment;


import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.attachment.AnnexDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.attachment.UpdateDTO;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.attachment.AnnexQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.attachment.UpdateQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;


/**
 * 类名：AttachApis
 * 包名：com.zeroone.star.project.j7.sysargs.supportinfo.attachment
 * 描述：附件管理接口
 * 作者：hh
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */

public interface AttachApis {


    /**
     *
     * @param query
     * @return
     */
    JsonVO<PageDTO<AnnexDTO>> listByPage(AnnexQuery query);

    /**
     *
     * @param file
     * @return
     */
    JsonVO<String> uploadAttachment(MultipartFile file);

    /**
     * 删除附件
     * @param id  删除的查询参数
     * @return   删除结果
     */
    JsonVO<String> deleteAnnex( String id);


    /**
     * 修改附件
     * 查询修改参数
     * @param annexDTO   修改数据
     * @return   修改结果
     */

    JsonVO<String>  updateAnnex( @RequestBody AnnexDTO annexDTO);
}
