package com.zeroone.star.project.j7.money.transfer;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.money.transfer.*;
import com.zeroone.star.project.query.j7.money.transfer.AllotExamineQuery;
import com.zeroone.star.project.query.j7.money.transfer.AllotQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
/**
 * 类名：AllotApis
 * 包名：com.zeroone.star.project.j7.money.transfer
 * 描述：转账单接口
 * 作者：hh
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */

public interface AllotApis {
    /**
     * 获取转账单列表
     * @param query
     * @return
     */
    JsonVO<PageDTO<AllotListDTO>> allotList(AllotQuery query);
    /**
     * 新增转账单
     * @param allotDTO
     * @return
     */
    JsonVO<String> addAllot(AddAllotDTO allotDTO);
    /**
     * 获取转账单详情
     * @param id --根据id查询
     * @return
     */
    public JsonVO<AllotDetailDTO> queryDetail(String id);
    /**
     *
     * @param file excel表
     * @return
     */
    JsonVO<Object> importAllot(MultipartFile file);
    /**
     * 导出简略转账单
     * @param ids
     * @return
     */
    ResponseEntity<byte[]> exportAllot(List<String> ids);
    /**
     *
     * @param ids
     * @return
     */
    ResponseEntity<byte[]> exportAllotDetail(List<String> ids);
    /**
     * 批量删除
     * @param ids
     * @return
     */
    JsonVO<List<String>> deleteallots(List<String> ids);

    /**
     * 批量审核，反审核
     * @param ids
     * @return
     */
    JsonVO<List<String>> examine(AllotExamineQuery query, List<String> ids);

    /**
     * 修改转账单
     * @param allotModifyDTO
     * @return
     */
    JsonVO<String> updateAllot(AllotModifyDTO allotModifyDTO);
}

