package com.zeroone.star.storemanagement.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.components.fastdfs.FastDfsClientComponent;
import com.zeroone.star.project.components.user.UserDTO;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.*;
import com.zeroone.star.project.j2.store.TransferApis;
import com.zeroone.star.project.query.j2.store.TransferQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.storemanagement.entity.*;
import com.zeroone.star.storemanagement.mapper.*;
import com.zeroone.star.storemanagement.service.ISwapService;
import com.zeroone.star.storemanagement.service.ITransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.storemanagement.controller
 * @Author: 高
 * @CreateTime: 2025-10-19 18:51
 * @Description: 调拨单控制器类
 * @Version: 1.0
 */
@RestController
@RequestMapping("/transfer")
@Api(tags = "调拨单")
@Slf4j
public class TransferController implements TransferApis {
    private final ITransferService transferService;
    @Autowired
    private SwapMapper swapMapper;
    @Autowired
    private EasyExcelComponent easyExcelComponent;

    public TransferController(ITransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping("/query-transferList")
    @ApiOperation(value = "获取调拨单列表（条件+分页）")
    @Override
    public JsonVO<PageDTO<TransferListDTO>> queryTransferList(
            @ApiParam(value = "调拨单查询参数,所有参数都为空不查", required = false)
            TransferQuery transferQuery) {
        return transferService.queryTransferListAchieve(transferQuery);
    }

    @GetMapping("/detail-transferInfo")
    @ApiOperation(value = "获取指定调拨单详细信息")
    @Override
    public JsonVO<TransferDetailListDTO> detailTransferList(
            /*根据传入的调拨单id获取调拨单*/
            @ApiParam(value = "调拨单id", required = true)
            @RequestParam(required = true)
            String id) {
        return transferService.detailTransferListAchieve(id);
    }

    @PostMapping("/add-transferList")
    @ApiOperation(value = "新增调拨单")
    @Override
    public JsonVO<String> addTransferList(
            /*根据前端传来的信息新增调拨单*/
            @ApiParam(value = "调拨单详细信息", required = true)
            @RequestBody
            TransferDetailListDTO transferDetailListDTO) {
        return transferService.addTransferListAchieve(transferDetailListDTO);
    }

    @PutMapping("/modify-transfer")
    @ApiOperation(value = "修改调拨单")
    @Override
    public JsonVO<String> modifyTransfer(@RequestBody TransferDetailDTO dto) {
        return transferService.modifyTransfer(dto);
    }

    @PostMapping("/batch-audit-transfer")
    @ApiOperation(value = "审核/反审核(支持批量)")
    @Override
    public JsonVO<String> batchAuditTransfer(@RequestBody BatchAuditTransferDTO dto) {
        return transferService.batchAuditTransfer(dto);
    }

    @PostMapping("/remove-transfer")
    @ApiOperation(value = "删除调拨单(支持批量)")
    @Override
    public JsonVO<String> removeTransfer(@RequestBody RemoveTransferDTO dto) {
        return transferService.deleteTransfer(dto);
    }

    @Resource
    ISwapService swapService;

    @Resource
    SwapInfoMapper swapInfoMapper;

    @Resource
    EasyExcelComponent excel;

    @Resource
    FastDfsClientComponent dfs;

    @Resource
    UserHolder userHolder;

    @Resource
    GoodsMapper goodsMapper;

    @Resource
    RecordMapper recordMapper;

    @Resource
    LogMapper logMapper;

    @SneakyThrows
    @PostMapping("/import")
    @ApiOperation(value = "导入数据")
    public JsonVO<String> importTransferList(@RequestPart("file") MultipartFile file) {
        if(file.isEmpty()) {
            return JsonVO.fail("文件为空");
        }

        ArrayList<SwapDO> swapList = new ArrayList<>();
        ArrayList<SwapInfoDO>  swapInfoList = new ArrayList<>();
        ArrayList<String> goodsNames = new ArrayList<>();

        try {
            EasyExcel.read(file.getInputStream(), new AnalysisEventListener<Map<Integer, String>>() {
                @Override
                public void invoke(Map<Integer, String> data, AnalysisContext context) {
                    int rowIndex = context.readRowHolder().getRowIndex();
                    if(rowIndex < 2) {
                        return;
                    }
                    boolean isEmpty = data.values().stream().allMatch(v -> v == null || v.trim().isEmpty());
                    if(isEmpty) {
                        return; // 空行直接跳过
                    }

                    if(rowIndex == 2) {
                        SwapDO swap = new SwapDO();
                        if(data.get(0)==null){
                            return;
                        }
                        //swap.setTime(new DateTime(data.get(0)));
                        swap.setNumber(data.get(1));
                        swap.setTotal(new BigDecimal(data.get(2)));
                        swap.setPeople(data.get(3));
                        swap.setLogistics(data.get(4));
                        swap.setData(data.get(5));
                        swapList.add(swap);
                    }
                    SwapInfoDO swapInfo = new SwapInfoDO();
                    goodsNames.add(data.get(6));
                    swapInfo.setAttr(data.get(7));
                    swapInfo.setUnit(data.get(8));
                    swapInfo.setWarehouse(data.get(9));
                    swapInfo.setStorehouse(data.get(10));
                    swapInfo.setBatch(data.get(11));
                    swapInfo.setMfd(new DateTime(data.get(12)).toLocalDateTime().toLocalDate());
                    swapInfo.setPrice(new BigDecimal(data.get(13)));
                    swapInfo.setNums(new BigDecimal(data.get(14)));
                    swapInfo.setSerial(data.get(15));
                    swapInfo.setTotal(new BigDecimal(data.get(16)== null?"0":data.get(16)));
                    swapInfo.setData(data.get(17));
                    swapInfoList.add(swapInfo);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    System.out.println("主表数据：" + swapList);
                    System.out.println("商品名称：" + goodsNames);
                    System.out.println("明细数据：" + swapInfoList);
                }
            }).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonVO.fail("fail");
        }

        if(swapList.isEmpty()){
            return new JsonVO<String>(1001,"数据错误",null);
        }
        if(swapList.get(0).getNumber()==null){
            return new JsonVO<String>(1001,"单据编号为空",null);
        }
        UserDTO currentUser;
        try {
            currentUser = userHolder.getCurrentUser();
        } catch (Exception e) {
            // 如果解析token失败，就临时伪造一个用户
            currentUser = UserDTO.builder()
                    .id("1")
                    .username("系统导入")
                    .frameName("总部")
                    .build();
        }
        String id = IdUtil.getSnowflakeNextIdStr();
        swapList.get(0).setId(id);
        swapList.get(0).setFrame(currentUser.getFrameName());
        swapList.get(0).setCost(BigDecimal.ZERO);
        swapList.get(0).setUser(currentUser.getUsername());

        for(int i=0;i<swapInfoList.size();i++) {
            swapInfoList.get(i).setPid(swapList.get(0).getId());
            GoodsDO good = goodsMapper.selectOne(new QueryWrapper<GoodsDO>().eq("name", goodsNames.get(i)));

            if(good==null) {
                return new JsonVO<>(1001,"第"+(i+2)+"行商品名称有误",null);
            }

            if(swapInfoList.get(i).getWarehouse()==null) {
                return new JsonVO<>(1001,"第"+(i+2)+"行调出仓库为空",null);
            }

            if(swapInfoList.get(i).getStorehouse()==null) {
                return new JsonVO<>(1001,"第"+(i+2)+"行调入仓库为空",null);
            }

            if (swapInfoList.get(i).getPrice()==null){
                return new JsonVO<>(1001,"第"+(i+2)+"行成本为空",null);
            }

            if(swapInfoList.get(i).getNums()==null){
                return new JsonVO<>(1001,"第"+(i+2)+"行数量为空   ",null);
            }

            swapInfoList.get(i).setGoods(good.getId());
            swapInfoMapper.insert(swapInfoList.get(i));
        }
        swapMapper.insert(swapList.get(0));

        RecordDO record = new RecordDO();
        record.setType("swap");
        record.setSource(swapList.get(0).getId());
        record.setTime(LocalDateTime.now());
        record.setUser(currentUser.getUsername());
        record.setInfo("新增单据");
        recordMapper.insert(record);

        LogDO log = new LogDO();
        log.setTime(LocalDateTime.now());
        log.setUser(currentUser.getUsername());
        log.setInfo("新增调拨单"+swapList.get(0).getNumber());
        logMapper.insert(log);

        System.out.println("主表数据：" + swapList);
        System.out.println("明细数据：" + swapInfoList);
        return new JsonVO<String>(200,"success",null);
    }

    @SneakyThrows
    @PostMapping("/export")
    @ApiOperation(value = "导出简单报表")
    public ResponseEntity<byte[]> exportTransferList(@RequestBody List<String> idList) {
        if(idList.isEmpty()) {
            return new ResponseEntity<>("列表为空".getBytes(),HttpStatus.BAD_REQUEST);
        }
        ArrayList<TransferListDTO> transferListDTOList = swapService.getTransferListDTOList(idList);
        if (transferListDTOList.isEmpty()) {
            return new ResponseEntity<>("无数据".getBytes(),HttpStatus.BAD_REQUEST);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        List<List<String>> headList = new ArrayList<>();
        headList.add(Collections.singletonList("所属组织"));
        headList.add(Collections.singletonList("单据时间"));
        headList.add(Collections.singletonList("单据编号"));
        headList.add(Collections.singletonList("单据成本"));
        headList.add(Collections.singletonList("单据费用"));
        headList.add(Collections.singletonList("关联人员"));
        headList.add(Collections.singletonList("审核状态"));
        headList.add(Collections.singletonList("费用状态"));
        headList.add(Collections.singletonList("制单人"));
        headList.add(Collections.singletonList("备注信息"));

        List<List<Object>> data = new ArrayList<>();
        for (TransferListDTO dto : transferListDTOList) {
            List<Object> row = new ArrayList<>();
            // 跳过前两列字段
            // row.add(dto.getColumn1()); // 不加
            // row.add(dto.getColumn2()); // 不加

            row.add(dto.getFrame());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            row.add(dto.getTime().format(formatter));
            row.add(dto.getNumber());
            row.add(dto.getCost());
            row.add(dto.getTotal());
            row.add(dto.getPeople());
            row.add(dto.getExamine());
            row.add(dto.getCse());
            row.add(dto.getUser());
            row.add(dto.getData());

            data.add(row);
        }

        EasyExcel.write(out).head(headList).sheet("简单报表").doWrite(data);
        out.flush();

        HttpHeaders headers = new HttpHeaders();
        String filename = DateTime.now().toString("yyyyMMddHHmmssS")+ ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ResponseEntity<byte[]> res = new ResponseEntity<>(out.toByteArray(),headers,HttpStatus.CREATED);
        out.close();

        UserDTO user = userHolder.getCurrentUser();
        LogDO log = new LogDO();
        log.setTime(LocalDateTime.now());
        log.setUser(user.getUsername());
        log.setInfo("导出调拨单列表");
        logMapper.insert(log);

        return res;
    }

    @SneakyThrows
    @PostMapping("/exportDetail")
    @ApiOperation(value = "导出详细报表")
    public ResponseEntity<byte[]> exportTransferDetailList(@RequestBody List<String> idList) {
        if(idList.isEmpty()) {
            return new ResponseEntity<>("列表为空".getBytes(),HttpStatus.BAD_REQUEST);
        }
        ArrayList<ArrayList<TransferDetailListDTO>> transferDetailListDTOList = swapService.getTransferDetailListDTOList(idList);
        if (transferDetailListDTOList.isEmpty()) {
            return new ResponseEntity<>("无数据".getBytes(),HttpStatus.BAD_REQUEST);
        }
        ByteArrayOutputStream zip = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(zip);
        for (ArrayList<TransferDetailListDTO> transferDetailListDTO : transferDetailListDTOList) {
            if(transferDetailListDTO.isEmpty()) {
                continue;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            List<List<Object>> allRows = new ArrayList<>();


            List<Object> dateRow = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            dateRow.add("单据日期:");
            dateRow.add(transferDetailListDTO.get(0).getTime().format(formatter));
            dateRow.add("单据编号:");
            dateRow.add(transferDetailListDTO.get(0).getSwapNumber());
            while (dateRow.size() < 10) dateRow.add("");
            allRows.add(dateRow);
            List<Object> headerRow = new ArrayList<>();
            headerRow.add("商品名称");
            headerRow.add("规格型号");
            headerRow.add("辅助属性");
            headerRow.add("单位");
            headerRow.add("调出仓库");
            headerRow.add("调入仓库");
            headerRow.add("成本");
            headerRow.add("数量");
            headerRow.add("总成本");
            headerRow.add("备注信息");
            allRows.add(headerRow);

            for (TransferDetailListDTO dto : transferDetailListDTO) {
                List<Object> row = new ArrayList<>();
                row.add(dto.getName());
                row.add(dto.getSpec());
                row.add(dto.getAttr());
                row.add(dto.getUnit());
                row.add(dto.getWarehouse());
                row.add(dto.getStorehouse());
                row.add(dto.getPrice());
                row.add(dto.getNums());
                row.add(dto.getTotal());
                row.add(dto.getData());
                allRows.add(row);
            }

            List<Object> summaryRow = new ArrayList<>();
            summaryRow.add("单据成本:");
            summaryRow.add(transferDetailListDTO.get(0).getSwapTotal());
            summaryRow.add("单据费用:");
            summaryRow.add(transferDetailListDTO.get(0).getCost());
            summaryRow.add("关联人员:");
            summaryRow.add(transferDetailListDTO.get(0).getPeople());
            summaryRow.add("备注信息:");
            summaryRow.add(transferDetailListDTO.get(0).getSwapData());
            while (summaryRow.size() < 10) summaryRow.add("");

            allRows.add(summaryRow);

            EasyExcel.write(out).sheet("详细报表").doWrite(allRows);

            out.flush();
            zipOutputStream.putNextEntry(new ZipEntry("详细报表"+DateTime.now().toString("yyyyMMddHHmmssS")+".xlsx"));
            zipOutputStream.write(out.toByteArray());
            zipOutputStream.closeEntry();
            out.close();
        }
        zip.close();
        zipOutputStream.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "detail"+new DateTime().now().toString("yyyyMMddHHmmss")+".zip");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ResponseEntity<byte[]> res = new ResponseEntity<>(zip.toByteArray(),headers,HttpStatus.CREATED);

        UserDTO user = userHolder.getCurrentUser();
        LogDO log = new LogDO();
        log.setTime(LocalDateTime.now());
        log.setUser(user.getUsername());
        log.setInfo("导出详细调拨单列表");
        logMapper.insert(log);

        return res;
    }


}
