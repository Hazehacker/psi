package com.zeroone.star.supportinfo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.components.fastdfs.FastDfsFileInfo;
import com.zeroone.star.project.dto.PageDTO;

import com.zeroone.star.project.dto.j7.sysargs.supportinfo.attachment.AnnexDTO;

import com.zeroone.star.project.query.j7.sysargs.supportinfo.attachment.AnnexQuery;

import com.zeroone.star.supportinfo.entity.Annex;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 * 附件 服务类
 * </p>
 *
 * @author kai
 * @since 2025-10-23
 */
public interface IAnnexService extends IService<Annex> {

    PageDTO<AnnexDTO> listByPage(AnnexQuery query);

    void saveAttachment(MultipartFile file, FastDfsFileInfo info, String fileServerUrl);


    //修改附件
    boolean update(AnnexDTO dto) throws Exception;

}
