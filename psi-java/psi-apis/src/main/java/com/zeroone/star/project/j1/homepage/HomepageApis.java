package com.zeroone.star.project.j1.homepage;

import com.zeroone.star.project.dto.ImyOfJson;
import com.zeroone.star.project.dto.SreOfJson;
import com.zeroone.star.project.dto.j1.homepage.*;

import com.zeroone.star.project.query.j1.homepage.PurchaseDailyTotalQuery;
import com.zeroone.star.project.vo.JsonVO;

import com.zeroone.star.project.query.j1.homepage.SalesDailyTotalQuery;
import com.zeroone.star.project.query.j1.homepage.PurchaseReturnDailyTotalQuery;

import com.zeroone.star.project.dto.j1.homepage.CommonMenuDTO;
import com.zeroone.star.project.dto.j1.homepage.DataStatisticsDTO;
import com.zeroone.star.project.vo.j1.homepage.AccountBalanceVO;
import com.zeroone.star.project.vo.j1.homepage.BalanceVO;
import com.zeroone.star.project.vo.j1.homepage.ListVO;


import javax.validation.Valid;

import com.zeroone.star.project.vo.j1.homepage.OmyTotalVo;
import com.zeroone.star.project.vo.j1.homepage.WareHouseRoomVo;


import java.util.List;

/**
 * <p>
 * 描述：首页面板
 * </p>
 *
 * @author heavydrink
 * @version 1.0.0
 */
public interface HomepageApis {
    /**
     * 首页-数据概览-付款单
     * 查询一段时间中，每天的付款单金额
     * @return 每天付款单金额 的 列表
     * Fantasy-hv
     */
    JsonVO<OmyTotalVo> queryOmyTotalDailySum(String from,String to,String type);

    /**
     * 首页-数据概览-库存数据 查询接口
     * Fantasy-hv
     * @return 各仓库库存信息的列表
     */
    JsonVO<WareHouseRoomVo> queryWareHouseRoomData();

    /**
     * 首页-数据概览-收款单
     * 查询每天已审核的收款金额
     * @author MRME39
     * @return 每天收款单金额 的 列表
     */
    JsonVO<ImyOfJson> queryimyCounts(String type);

    /**
     * 首页-数据概览-销售退货单
     * 查询每天已审核的销售退货金额
     * @author MRME39
     * @return 每天销售退货单 的 列表
     */
    JsonVO<SreOfJson> querySreCounts(String type);
    /*
     * 数据统计
     * @author heavydrink
     * @return 封装好的数据
     */
    JsonVO<DataStatisticsDTO> queryDataStatistics();

    /**
     * 常用功能
     * @author heavydrink
     * @return 封装好的数据
     */
    JsonVO<List<CommonMenuDTO>> queryCommonMenus();

    /**

     * 首页-数据概览-销售单数据 查询接口
     * @author Altolia
     * @param query
     * @return
     */
    JsonVO<SalesDailyTotalResponseDTO> querySalesDailyTotal(SalesDailyTotalQuery query);

    /**
     * 首页-数据概览-采购退货单数据 查询接口
     *
     * @param query
     * @return
     * @author Altolia
     */
    JsonVO<PurchaseReturnDailyTotalResponseDTO> queryPurchaseReturnDailyTotal(PurchaseReturnDailyTotalQuery query);

    /**
     * 首页-数据概览-采购单数据 查询接口
     * @author Altolia
     * @param query
     * @return
     */
    JsonVO<PurchaseDTO> queryPurchaseDailyTotal(@Valid PurchaseDailyTotalQuery query);

    /**
     * 首页-数据概览-汇总信息数据 查询接口
     */
    JsonVO<List<SummaryDTO>> querySummaryInfo();

    /**
     * 资产数据
     * @author: Duuuuuu
     * @return
     */
    JsonVO<ListVO<BalanceVO>> queryBalanceSum();

    /**
     * 资金数据
     * @author Duuuuuu
     * @return
     */
    JsonVO<ListVO<AccountBalanceVO>> queryAccountBalance();

}
