package com.zeroone.star.homepage.controller;

import com.zeroone.star.homepage.service.*;
import com.zeroone.star.homepage.service.impl.OftenServiceImpl;
import com.zeroone.star.homepage.service.impl.SellInfoServiceImpl;
import com.zeroone.star.homepage.utils.localDate.LocalDateUtil;
import com.zeroone.star.project.dto.j1.homepage.*;
import com.zeroone.star.project.dto.ImyOfJson;
import com.zeroone.star.project.dto.SreOfJson;
import com.zeroone.star.project.j1.homepage.HomepageApis;
import com.zeroone.star.project.query.j1.homepage.PurchaseDailyTotalQuery;
import com.zeroone.star.project.query.j1.homepage.PurchaseReturnDailyTotalQuery;
import com.zeroone.star.project.query.j1.homepage.SalesDailyTotalQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.ResultStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.zeroone.star.project.vo.j1.homepage.AccountBalanceVO;
import com.zeroone.star.project.vo.j1.homepage.OmyTotalVo;
import com.zeroone.star.project.vo.j1.homepage.WareHouseRoomVo;
import com.zeroone.star.project.vo.j1.homepage.BalanceVO;
import com.zeroone.star.project.vo.j1.homepage.ListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.util.StringUtils;
import java.util.Arrays;

import java.util.List;

@RestController
@RequestMapping("/homepage")
@Api(tags="首页面板")
@Slf4j
public class HomepageController implements HomepageApis {
    @Resource
    IOmyService iOmyService;

    @Resource
    ISellService sellService;
    @Resource
    IBreService breService;
    @Resource
    IAccountService accountService;
    @Resource
    ICustomerService customerService;
    @Resource
    IGoodsService goodsService;
    @Resource
    ISupplierService supplierService;


    @GetMapping("/data-overview-omy")
    @Override
    @ApiOperation(value = "付款单",produces = "application/json",notes = "需要传入统计日期范围的起始和结束日期,最多查询最近1年数据")
//    @ApiImplicitParam(value = "日期范围",required = true)
    public JsonVO<OmyTotalVo> queryOmyTotalDailySum (@RequestParam(value = "from", required = false) String from,@RequestParam(value="to", required = false) String to, @RequestParam("type") String type) {
        //解析-校验
        //考虑只传to不传f，如果to在一个月前，就会报错，此时应该将f设置为to一个月前
        LocalDate toDate = StringUtils.isEmpty(to) ?LocalDate.now():LocalDateUtil.parse(to);
        if (toDate==null)return JsonVO.create(null, ResultStatus.FAIL.getCode(),"参数错误");
        LocalDate fromDate =StringUtils.isEmpty(from)? toDate.minusMonths(1):LocalDateUtil.parse(from);
        if (fromDate==null)return JsonVO.create(null, ResultStatus.FAIL.getCode(),"参数错误");
        if (fromDate.isAfter(toDate))return JsonVO.create(null, ResultStatus.FAIL.getCode(),"起始日期不能晚于结束日期");
        if (toDate.isAfter(LocalDate.now()))toDate = LocalDate.now();
        if (fromDate.isBefore(LocalDate.now().minusYears(1)))fromDate = LocalDate.now().minusYears(1);
        List<OmyDailyTotalDto> dtos = iOmyService.listOmyDailyTotal(fromDate,toDate);
        OmyTotalVo vo = new OmyTotalVo("付款单",dtos);
        return JsonVO.success(vo);
    }
    @Resource
    IRoomService iRoomService;
    @GetMapping("/data-overview-room")
    @Override
    @ApiOperation(value = "库存数据",produces = "application/json")
    public JsonVO<WareHouseRoomVo> queryWareHouseRoomData() {
        List<WareHouseRoomDataDto> res = iRoomService.listWareHouseRoomData();
        return JsonVO.success(new WareHouseRoomVo(res));

    }



    @Resource
    private SellInfoServiceImpl sellInfoService;
    /**
     * 数据统计
     * @author heavydrink
     * @return 响应的数据
     */
    @GetMapping("/query-data-statistics")
    @ApiOperation(value = "数据统计")
    @Override
    public JsonVO<DataStatisticsDTO> queryDataStatistics() {
        log.info("-----数据统计------");
        try{
            DataStatisticsDTO dataStatisticsDTO = sellInfoService.getDataStatisticsDTO();
            return JsonVO.success(dataStatisticsDTO);
        }catch (Exception e){
            e.printStackTrace();
            return JsonVO.create(null, ResultStatus.FAIL.getCode(), e.getMessage());
        }
    }

    @Resource
    private OftenServiceImpl oftenService;

    /**
     * 常用功能
     * @author heavydrink
     * @return 响应的数据
     */
    @GetMapping("/query-common-menus")
    @ApiOperation(value = "常用功能")
    @Override
    public JsonVO<List<CommonMenuDTO>> queryCommonMenus() {
        log.info("-----常用功能------");
        try{
            List<CommonMenuDTO> commonMenus = oftenService.getCommonMenus();
            return JsonVO.success(commonMenus);
        }catch (Exception e){
            return JsonVO.create(null, ResultStatus.FAIL.getCode(), e.getMessage());
        }
    }


    @Resource
    IImyService iImyService;
    /**
     * 收款单
     * @author MRME39
     * @return 响应的数据
     */

    @GetMapping("/query-show-omy")
    @ApiOperation(value = "收款单")
    @Override
    public JsonVO<ImyOfJson> queryimyCounts(String type) {
        ImyOfJson responseData = new ImyOfJson();
        responseData.setType(type);
        List<ImyCountsDTO> imycounts = iImyService.Imycounts();
        // 响应数据校验
        if (imycounts == null) {
            return JsonVO.success(responseData);
        }
        // 验证响应数据的完整性，如果有null就fail
        for (ImyCountsDTO dto : imycounts) {
            if (dto.getDate() == null || dto.getValue() == null) {
                return JsonVO.fail(responseData);
            }
        }
        responseData.setList(imycounts);
        return JsonVO.success(responseData);
    }

    @Resource
    ISreService iSreService;

    /**
     * 销售退货单
     * @author MRME39
     * @return 响应的数据
     */
    @GetMapping("/query-show-Imy")
    @Override
    @ApiOperation(value = "销售退货单")
    public JsonVO<SreOfJson> querySreCounts(String type){
        SreOfJson responseData = new SreOfJson();
        responseData.setType(type);
        List<SreCountsDTO> srecounts = iSreService.Srecounts();
        // 响应数据校验
        if (srecounts == null) {
            return JsonVO.success(responseData);
        }
        // 验证响应数据的完整性，如果有null就fail
        for (SreCountsDTO dto : srecounts) {
            if (dto.getDate() == null || dto.getValue() == null) {
                return JsonVO.fail(responseData);
            }
        }
        responseData.setList(srecounts);
        return JsonVO.success(responseData);
    }

    /**
     * 获取 数据概览 销售单统计表 数据
     *
     * @param query
     * @return
     * @author Altolia
     */

    @GetMapping("/query-overview-sales")
    @ApiOperation(value = "销售单")
    @Override
    public JsonVO<SalesDailyTotalResponseDTO> querySalesDailyTotal(@Valid SalesDailyTotalQuery query) {
        SalesDailyTotalResponseDTO salesDailyTotalResponseDTO = sellService.querySalesDailyTotal(query);

        return JsonVO.success(salesDailyTotalResponseDTO);
    }


    /**
     * 获取 数据概览 采购退货单统计表 数据
     *
     * @param query
     * @return
     * @author Altolia
     */
    @GetMapping("/query-overview-purchase-return")
    @ApiOperation(value = "采购退货单")
    @Override
    public JsonVO<PurchaseReturnDailyTotalResponseDTO> queryPurchaseReturnDailyTotal(@Valid PurchaseReturnDailyTotalQuery query) {
        PurchaseReturnDailyTotalResponseDTO purchaseReturnDailyTotalResponseDTO = breService.queryPurchaseReturnDailyTotal(query);
        return JsonVO.success(purchaseReturnDailyTotalResponseDTO);
    }

    /**
     * 获取 数据概览 采购单统计表 数据
     */
    @Resource
    IBuyService buyService;

    @GetMapping("/query-overview-purchase")
    @ApiOperation(value = "采购单")
    @Override
    public JsonVO<PurchaseDTO> queryPurchaseDailyTotal(@Valid PurchaseDailyTotalQuery query) {
        PurchaseDTO purchase = new PurchaseDTO();
        purchase.setType("采购单");
        ArrayList<DV> list = buyService.getPurchaseOrderStatistics(query);
        purchase.setList(list);

        return JsonVO.success(purchase);
    }

    /**
     * 汇总信息
     */
    @Resource
    ISummaryService summaryService;

    @GetMapping("/query-summary-info")
    @ApiOperation(value = "汇总信息")
    @Override
    public JsonVO<List<SummaryDTO>> querySummaryInfo() {
        ArrayList<SummaryDTO> list = summaryService.getSummaryInfo();
        return JsonVO.success(list);
    }


    /**
     * 资金数据
     * @author Duuuuuu
     * @return 响应的数据
     */
    @GetMapping("/query-balance/account")
    @Override
    @ApiOperation("资金数据")
    public JsonVO<ListVO<AccountBalanceVO>> queryAccountBalance() {
        ListVO<AccountBalanceVO> vo =accountService.queryAccountBalance();
        return JsonVO.success(vo);
    }
    /**
     * 资产数据
     * @author Duuuuuu
     * @return 响应的数据
     */
    @GetMapping("/query-balance/sum")
    @ApiOperation("资产数据")
    @Override
    public JsonVO<ListVO<BalanceVO>> queryBalanceSum() {
        log.info("-----资产数据sum------");
        BalanceVO accountBalanceVO = accountService.queryBalance();
        BalanceVO customerBalanceVO = customerService.queryBalance();
        BalanceVO supplierBalanceVO = supplierService.queryBalance();
        BalanceVO goodsBalanceVO = goodsService.queryBalance();
        ListVO<BalanceVO> listVO = new ListVO<>();
        listVO.setList(Arrays.asList(accountBalanceVO,customerBalanceVO,supplierBalanceVO,goodsBalanceVO));
        return JsonVO.success(listVO);
    }
}
