package com.zeroone.star.moneytransfer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.zeroone.star.moneytransfer.entity.Allot;
import com.zeroone.star.moneytransfer.entity.AllotInfo;
import com.zeroone.star.moneytransfer.mapper.AllotInfoMapper;
import com.zeroone.star.moneytransfer.mapper.AllotMapper;
import com.zeroone.star.moneytransfer.service.IAllotExportImportService;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.components.user.UserDTO;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.project.dto.j7.money.transfer.ImportAllotDTO;
import com.zeroone.star.project.utils.j7.EasyExcelUtils;
import com.zeroone.star.project.vo.j7.money.transfer.ExportAllotDetailVO;
import com.zeroone.star.project.vo.j7.money.transfer.ExportAllotVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AllotExportImportServiceImpl implements IAllotExportImportService {
    @Resource
    private EasyExcelUtils easyExcelUtils;

    @Resource
    private AllotMapper allotMapper;

    @Resource
    private AllotInfoMapper allotInfoMapper;

    @Resource
    private EasyExcelComponent excel;
    //    @Resource
//    private UserHolder userHolder;
    @Override
    public boolean importAllot(MultipartFile file) throws Exception{
        UserDTO user = null;
        String frameName=null;
        try {
//            user = userHolder.getCurrentUser();
//            frameName= allotInfoMapper.selectUserById(user.getId()).getFrame();
            frameName=allotInfoMapper.selectUserById("1").getFrame();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 用户ID  一般可以从上下文获取
        try {
            InputStream inputStream = file.getInputStream();
            try {
                // 获取Excel读取器
                ExcelReader reader = ExcelUtil.getReader(inputStream);
                List<List<Object>> mapData = reader.read(2, 5000);
                for (int i = 0; i < mapData.size(); i++) {
                    try {
                        ImportAllotDTO importAllotDTO = easyExcelUtils.excelToBean(mapData.get(i), ImportAllotDTO.class, i);
                        verify(importAllotDTO);
                        String id = allotInfoMapper.selectByNumber(importAllotDTO.getNumber());
                        if (id == null) {
                            Allot allot = BeanUtil.copyProperties(importAllotDTO, Allot.class);
//                            allot.setUser(user.getUsername());
                            allot.setUser("管理员");
                            allot.setFrame(frameName);
                            allot.setPeople(importAllotDTO.getPeopleName());
                            allot.setTotal((BigDecimal) importAllotDTO.getTotal());
                            allot.setExamine(0);
                            allot.setId(UUID.randomUUID().toString().substring(0, 32));
                            allotMapper.insertBackId(allot);
                            id = allot.getId();
                            //````````````````````````
                        }
                        AllotInfo allotInfo = BeanUtil.copyProperties(importAllotDTO, AllotInfo.class);
                        allotInfo.setAccount(importAllotDTO.getOutName());
                        allotInfo.setTat(importAllotDTO.getInName());
                        allotInfo.setPid(id);
                        allotInfo.setData(importAllotDTO.getRemark());
                        allotInfo.setId(UUID.randomUUID().toString().substring(0, 32));
                        allotInfoMapper.insert(allotInfo);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new RuntimeException("导入失败，错误原因为:" + e.getMessage());
                    }
                }
                reader.close();

            } catch (Exception e) {
                System.err.println("读取Excel文件时发生错误: " + e.getMessage());
                throw new RuntimeException("读取Excel文件时发生错误: " + e.getMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public byte[] exportAllotDetail(List<String> ids) throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 生成Excel
        List<AllotInfo> allotInfos = allotInfoMapper.selectBatchPids(ids);
        if (allotInfos==null || allotInfos.isEmpty()) {
            throw new RuntimeException("没有数据");
        }
        List<ExportAllotDetailVO> allotDetailVOList = new ArrayList<>();
        for (int i = 0; i < allotInfos.size(); i++) {
            AllotInfo allotInfo = allotInfos.get(i);
            if (allotInfo==null) continue;
            ExportAllotDetailVO allotDetailVO = BeanUtil.copyProperties(allotInfo, ExportAllotDetailVO.class);
            String pid = allotInfo.getPid();
            Allot allot = allotMapper.selectById(pid);
            BeanUtil.copyProperties(allot, allotDetailVO);
            allotDetailVO.setInName(allotInfo.getAccount());
            allotDetailVO.setOutName(allotInfo.getTat());
            allotDetailVO.setRemark(allotInfo.getData());
            allotDetailVO.setPeopleName(allot.getPeople());

            allotDetailVOList.add(allotDetailVO);
        }
        try {
            excel.export("转账单信息", out, ExportAllotDetailVO.class,allotDetailVOList);
        }catch (Exception e){
            throw new RuntimeException("导出失败,错误原因为"+e.getMessage());
        }

        return out.toByteArray();
    }

    @Override
    public byte[] exportAllot(List<String> ids) throws Exception{

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 生成Excel
        List<Allot> allotVOList = allotMapper.selectBatchIds(ids);
        if (allotVOList==null || allotVOList.isEmpty()) {
            throw new RuntimeException("没有数据");
        }
        List<ExportAllotVO> exportAllotVOS = new ArrayList<>();
        BigDecimal sumMoney = BigDecimal.valueOf(0);
        int count = 0;
        for (int i = 0; i < allotVOList.size(); i++) {
            count++;
            Allot allot = allotVOList.get(i);
            if (allot== null) continue;
            ExportAllotVO exportAllotVO = new ExportAllotVO();
            BeanUtil.copyProperties(allot, exportAllotVO);
            System.out.println(exportAllotVO);
            exportAllotVO.setPeopleName(allot.getPeople());
            exportAllotVO.setFrameName(allot.getFrame());
            exportAllotVO.setUserName("管理员");
            sumMoney = sumMoney.add(exportAllotVO.getTotal());
            exportAllotVOS.add(exportAllotVO);
            if(i==allotVOList.size()-1){
                ExportAllotVO lastRow=new ExportAllotVO();
                lastRow.setSumMoney(sumMoney);
                lastRow.setCount(count);
                exportAllotVOS.add(new ExportAllotVO());
                exportAllotVOS.add(lastRow);
            }
        }
        try {
            excel.export("转账单信息", out, ExportAllotVO.class,exportAllotVOS);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return out.toByteArray();
    }

    public boolean verify(ImportAllotDTO importAllotDTO) throws ParseException{
        if(importAllotDTO.getInName()==null){

            throw new ParseException("转入账户名称不能为空", 0);
        }else {
            if (allotInfoMapper.selectByAccount(importAllotDTO.getInName())==null) {
                throw new ParseException("转入账户名称不存在", 0);
            }
        }
        if(importAllotDTO.getOutName()==null){
            throw new ParseException("转出账户名称不能为空", 0);
        }else {
            if (allotInfoMapper.selectByAccount(importAllotDTO.getOutName())==null) {
                throw new ParseException("转出账户名称不存在", 0);
            }
        }
        if(importAllotDTO.getPeopleName()==null){
            throw new ParseException("关联人员名称不能为空", 0);
        }else {
            if (allotInfoMapper.selectByName(importAllotDTO.getPeopleName())== null) {
                throw new ParseException("关联人员名称不存在", 0);
            }
        }
        if(importAllotDTO.getSettle()==null){
            throw new ParseException("结算号不能为空", 0);
        }
        return true;
    }
}