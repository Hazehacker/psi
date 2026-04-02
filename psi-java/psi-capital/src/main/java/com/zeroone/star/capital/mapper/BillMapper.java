package com.zeroone.star.capital.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.capital.DO.BillDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j3.capital.VerificationSheetBillWrittenDTO;
import com.zeroone.star.project.dto.j3.capital.VerificationSheetPendingBillOffDTO;
import com.zeroone.star.project.query.j3.capital.VerificationBillWrittenQuery;
import com.zeroone.star.project.query.j3.capital.VerificationPendingBillQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 核销单 Mapper 接口
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
@Mapper
@SuppressWarnings("all")
public interface BillMapper extends BaseMapper<BillDO> {


    /**
     * 获取需要核销的单据列表  对应 imy_bill
     * @param query 查询参数
     * @return 列表
     * @author: 简单点
     * @date: 2025/10/19
     */
    Page<VerificationSheetPendingBillOffDTO> listPendingWriteOff00(
            Page<VerificationSheetPendingBillOffDTO> page,
            @Param("query") VerificationPendingBillQuery query);

    /**
     * 获取需要核销的单据列表  对应 sell_bill
     * @param query  查询参数
     * @return 列表
     * @author: 简单点
     * @date: 2025/10/19
     */
    Page<VerificationSheetPendingBillOffDTO> listPendingWriteOff01(
            Page<VerificationSheetPendingBillOffDTO> page,
            @Param("query")VerificationPendingBillQuery query);


    /**
     * 获取需要核销的单据列表  对应 sre_bill
     * @param query 查询参数
     * @return 列表
     * @author: 简单点
     * @date: 2025/10/19
     */
    Page<VerificationSheetPendingBillOffDTO> listPendingWriteOff02(
            Page<VerificationSheetPendingBillOffDTO> page,
            @Param("query")VerificationPendingBillQuery query);


    /**
     * 获取需要核销的单据列表  对应 ice_bill
     * @param query 查询参数
     * @return 列表
     * @author: 简单点
     * @date: 2025/10/19
     */
    Page<VerificationSheetPendingBillOffDTO> listPendingWriteOff03(
            Page<VerificationSheetPendingBillOffDTO> page,
            @Param("query")VerificationPendingBillQuery query);

    /**
     * 查询核销单列表
     * @param query 查询参数
     * @return 列表
     * @author: 简单点
     * @date: 2025/10/19
     */
    Page<VerificationSheetBillWrittenDTO> listWrittenOff(
            Page<VerificationSheetBillWrittenDTO> page,
            @Param("query") VerificationBillWrittenQuery query);
}
