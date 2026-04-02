package com.zeroone.star.storemanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.OtherInListAddDTO;
import com.zeroone.star.project.dto.j2.store.OtherInListDTO;
import com.zeroone.star.project.dto.j2.store.OtherInListDetailDTO;
import com.zeroone.star.project.query.j2.store.OtherInQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.storemanagement.entity.EntryDO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IOtherInService extends IService<EntryDO> {
    void updateOtherInList(OtherInListDetailDTO otherInListDetailDTO);

    void examine(List<Integer> ids);

    void check(List<Integer> ids);

    OtherInListDetailDTO getOtherInListDetail(String id);

    JsonVO<String> saveOtherInList(OtherInListAddDTO dto);

    List<String> removeOtherInList(List<Integer> ids);

    JsonVO<PageDTO<OtherInListDTO>> getOtherInList(OtherInQuery query);

    JsonVO<String> importExcel(MultipartFile file);

    JsonVO<ResponseEntity<byte[]>> exportDetailExcel(String ids);

    JsonVO<ResponseEntity<byte[]>> exportEasyExcel(String ids);
}
