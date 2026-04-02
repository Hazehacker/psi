package com.zeroone.star.capital.service;

import com.zeroone.star.capital.DO.BillDO;
import com.baomidou.mybatisplus.extension.service.IService;

import com.zeroone.star.project.dto.j3.capital.VerificationSheetAddDTO;
import com.zeroone.star.project.dto.j3.capital.VerificationSheetModifyDTO;
import com.zeroone.star.project.vo.j3.capital.VerificationSheetVO;
import com.zeroone.star.project.vo.j3.capital.VerificationSheetAddVO;
import com.zeroone.star.project.vo.j3.capital.VerificationSheetModifyVO;


import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j3.capital.VerificationSheetBillWrittenDTO;
import com.zeroone.star.project.dto.j3.capital.VerificationSheetPendingBillOffDTO;
import com.zeroone.star.project.query.j3.capital.VerificationBillWrittenQuery;
import com.zeroone.star.project.query.j3.capital.VerificationPendingBillQuery;
import com.zeroone.star.project.vo.JsonVO;

import com.zeroone.star.project.dto.j3.capital.VerificationOperationDTO;
import com.zeroone.star.project.dto.j3.capital.VerificationBatchDeleteDTO;
import com.zeroone.star.project.query.j3.capital.VerificationImportQuery;
import com.zeroone.star.project.vo.j3.capital.BatchOperateResultVO;
import com.zeroone.star.project.vo.j3.capital.ImportResultVO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * <p>
 * 核销单 服务类
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
public interface IBillService extends IService<BillDO> {

    /**
     * 获取需要核销的单据列表
     *
     * @param query 查询参数
     * @return
     * @author: 简单点
     * @date: 2025/10/19
     */
    JsonVO<PageDTO<VerificationSheetPendingBillOffDTO>> listPendingWriteOff(VerificationPendingBillQuery query);


    /**
     * 获取核销单列表
     *
     * @param query 查询参数
     * @return 核销单列表
     * @author: 简单点
     * @date: 2025/10/19
     */
    JsonVO<PageDTO<VerificationSheetBillWrittenDTO>> listWrittenOff(VerificationBillWrittenQuery query);


    /**
     * 核销单操作（核销、反核销）
     * @param dto
     * @return
     */
    BatchOperateResultVO approve(VerificationOperationDTO dto);

    /**
     * 批量删除核销单
     * @param dto 批量删除参数
     * @author 俊杰
     * @return 批量操作结果
     */
    BatchOperateResultVO batchDelete(VerificationBatchDeleteDTO dto);

    /**
     * 导入核销单数据
     * @param file 上传的Excel文件
     * @author 俊杰
     * @param query 导入查询参数
     * @return 导入结果
     */
    ImportResultVO importData(MultipartFile file, VerificationImportQuery query);

    /**
     * 获取核销单详情
     * @param id 核销单id
     * @return JsonVO<VerificationSheetDTO>
     * @author: a
     * @date: 2025/10/28
     */
    VerificationSheetVO getVSDetailById(String id);

    /**
     * 新增核销单
     * @param verificationSheetAddDTO 添加核销单数据
     * @return JsonVO<VerificationSheetAddVO>
     * @author: a
     * @date: 2025/10/30
     */
    VerificationSheetAddVO addVS(VerificationSheetAddDTO verificationSheetAddDTO, String token);

    /**
     * 修改核销单
     * @param verificationSheetModifyDTO 修改核销单数据
     * @return JsonVO<VerificationSheetModifyVO>
     * @author: a
     * @date: 2025/10/30
     */
    VerificationSheetModifyVO updateVS(VerificationSheetModifyDTO verificationSheetModifyDTO, String token);

    /**
     * 导出简单报表
     * @author 小王
     * @param ids
     * @return
     */
    ResponseEntity<byte[]> exportBill(List<String> ids);
}
