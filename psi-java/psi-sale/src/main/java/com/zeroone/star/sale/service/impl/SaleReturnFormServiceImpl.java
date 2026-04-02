package com.zeroone.star.sale.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.components.fastdfs.FastDfsClientComponent;
import com.zeroone.star.project.components.fastdfs.FastDfsFileInfo;
import com.zeroone.star.project.components.user.UserDTO;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.sale.SaleReturnDTO;
import com.zeroone.star.project.dto.j4.sale.SaleReturnInfoDTO;
import com.zeroone.star.project.query.j4.sale.SaleReturnQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.sale.entity.SaleReturnInfo;
import com.zeroone.star.sale.mapper.SaleReturnMapper;
import com.zeroone.star.sale.service.SaleReturnFormService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zeroone.star.project.dto.j4.sale.SalesReturnOrderDTO;
import com.zeroone.star.project.dto.j4.sale.info.SalesReturnOrderInfo;
import com.zeroone.star.sale.entity.IsSre;
import com.zeroone.star.sale.entity.IsSreInfo;
import com.zeroone.star.sale.mapper.IsSreInfoMapper;
import com.zeroone.star.sale.mapper.IsSreMapper;
import com.zeroone.star.sale.service.SaleReturnFormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class SaleReturnFormServiceImpl implements SaleReturnFormService {

    @Resource
    SaleReturnMapper saleReturnMapper;

    @Resource
    private SaleReturnFormMapper mapper;

    @Resource
    private IsSreMapper isSreMapper;

    @Resource
    private IsSreInfoMapper isSreInfoMapper;

    @Resource
    private EasyExcelComponent easyExcel;

    @Resource
    private FastDfsClientComponent dfs;

    @Resource
    private ThreadPoolExecutor importReturnThreadPool;

    @Value("${fastdfs.nginx-servers}")
    private String fileServerUrl;
    @Override
    public PageDTO<SaleReturnDTO> pageSaleReturn(SaleReturnQuery saleReturnQuery) {

        Long pageSize = saleReturnQuery.getPageSize() < 1 ? 10 : saleReturnQuery.getPageSize();
        Long pageIndex = saleReturnQuery.getPageIndex() < 1 ? 1 : saleReturnQuery.getPageIndex();

        Page<SaleReturnDTO> page = new Page<>(pageIndex, pageSize);
        Page<SaleReturnDTO> resultPage = saleReturnMapper.selectSaleReturnPage(page, saleReturnQuery);

        PageDTO<SaleReturnDTO> pageDTO = PageDTO.create(resultPage);

        return pageDTO;
    }

    @Override
    public List<SaleReturnInfoDTO> listSaleReturnInfo(String id) {
        List<SaleReturnInfoDTO> list = saleReturnMapper.selectSaleRReturnInfo(id);
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public boolean addSalesReturnOrder(SalesReturnOrderDTO dto) {
        //将dto转换成isSre
        IsSre isSre = mapper.toIsSre(dto);
        //插入isSre
        isSre.setId(null);
        int insert = isSreMapper.insert(isSre);

        if(insert<=0){
            return false;
        }
        //获取isSre的id
        String mainId = isSre.getId();
        //将dto转换成isSreInfo
        SalesReturnOrderInfo[] salesReturnOrderInfos = dto.getSalesReturnOrderInfos();
        //批量插入isSreInfo,给pid赋值
        for(int i = 0;i<salesReturnOrderInfos.length;i++){
            IsSreInfo isSreInfo = mapper.toIsSreInfo(salesReturnOrderInfos[i]);
            isSreInfo.setId(null);
            isSreInfo.setPid(mainId);
            int inserted = isSreInfoMapper.insert(isSreInfo);
            if(inserted<=0){
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updateSalesReturnOrder(SalesReturnOrderDTO dto) {
        //将dto转换成isSre
        IsSre isSre = mapper.toIsSre(dto);
        //将dto转换成isSreInfo
        SalesReturnOrderInfo[] salesReturnOrderInfos = dto.getSalesReturnOrderInfos();
        //批量修改isSreInfo
        for(int i = 0;i<salesReturnOrderInfos.length;i++){
            IsSreInfo isSreInfo = mapper.toIsSreInfo(salesReturnOrderInfos[i]);
            int update = isSreInfoMapper.updateById(isSreInfo);
            if(update<=0){
                return false;
            }
        }
        //修改isSre
        int update = isSreMapper.updateById(isSre);
        if(update<=0){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSalesReturnOrder(String id) {
        //查找要删除isSre所关联的isSreInfo
        IsSre isSre = isSreMapper.selectById(id);
        System.out.println(0);
        //如果没有该对象则返回false
        if(isSre==null){
            return false;
        }
        System.out.println(1);
        LambdaQueryWrapper<IsSreInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IsSreInfo::getPid,isSre.getId());
        //批量删除isSreInfo
        int deleted = isSreInfoMapper.delete(wrapper);
        System.out.println(2);
        //删除isSre
        int delete = isSreMapper.deleteById(isSre.getId());
        if(delete<=0){
            return false;
        }
        return true;
    }

    @SneakyThrows
    @Override
    public String importFund(MultipartFile file) {
        //获取文件后缀名
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (originalFilename == null || !originalFilename.endsWith(".xlsx")) {
            throw new RuntimeException("请上传以.xlsx结尾的Excel文件");
        }
        //上传
        String path = null;
        List<SaleReturnInfo> saleReturnInfos = null;
        try {
            FastDfsFileInfo info = dfs.uploadFile(file.getBytes(), extName);
            path = dfs.fetchUrl(info, fileServerUrl, true);
            path = path.replaceAll("\\\\\\\\", "/")  // 先替换多个反斜杠
                    .replaceAll("\\\\", "/")       // 再替换单个反斜杠
                    .replaceAll("http:/([^/])", "http://$1")
                    .replaceAll("https:/([^/])", "https://$1");
            log.info("文件上传成功，路径：{}", path);
            try (InputStream inputStream = new URL(path).openStream()) {
                saleReturnInfos = EasyExcel.read(inputStream)
                        .head(SaleReturnInfo.class)
                        .sheet()
                        .doReadSync();
            } catch (IOException e) {
                log.error("远程Excel文件读取失败，路径：{},{}", path, e.getMessage());
                throw new RuntimeException("远程Excel文件读取失败：" + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Excel文件解析完成，数量：{}", saleReturnInfos);
        if (CollectionUtils.isEmpty(saleReturnInfos)) {
            log.info("Excel文件内容为空");
            return path;
        }

        int size = saleReturnInfos.size();
        int count = 0;
        int batchSize = 1000;

        // 使用CountDownLatch等待所有任务完成                   设置数量
        CountDownLatch countDownLatch = new CountDownLatch((size + batchSize - 1) / batchSize);

        while (count < size) {
            int end = Math.min(count + batchSize, size);
            List<SaleReturnInfo> tempList = saleReturnInfos.subList(count, end);
            importReturnThreadPool.execute(() -> {
                try {
                    saleReturnMapper.insertBatch(tempList);
                    log.info("批量插入成功，数量：{}", tempList.size());
                } catch (Exception e) {
                    log.error("批量插入失败", e);
                } finally {
                    countDownLatch.countDown();
                }
            });
            count = end;
        }

        // 等待所有任务完成
        countDownLatch.await();
        log.info("所有数据导入完成，总数量：{}", size);
        return "上传成功";
    }



    @SneakyThrows
    @Override
    public String exportDetailFund() {
        List<SaleReturnInfo> allData = new ArrayList<>();
        int pageNum = 1;
        int batch_size = 1000;
        ByteArrayOutputStream out = null;
        try {
            while(true){
                int start = (pageNum - 1) * batch_size;
                List<SaleReturnInfo> batchList = saleReturnMapper.selectSaleReturnByPage(start, batch_size);
                if (CollectionUtils.isEmpty(batchList)) {
                    break;
                }
                allData.addAll(batchList);
                pageNum ++;
            }
            if (CollectionUtils.isEmpty(allData)) {
                log.warn("无销售退货数据可导出");
                return "无数据可导出";
            }

            out = new ByteArrayOutputStream();
            // 生成Excel到流
            easyExcel.export("用户信息", out, SaleReturnInfo.class, allData);
            out.flush();

            // 上传到DFS：此时流已刷新，字节数组有完整数据
            String fileName = "returnFund-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
            FastDfsFileInfo xlsx = dfs.uploadFile(out.toByteArray(), "xlsx");

            // 生成下载URL并返回
            if (xlsx != null) {
                String downloadUrl = dfs.fetchUrl(xlsx, fileServerUrl, true);
                StringBuilder urlBuilder = new StringBuilder(downloadUrl);
                urlBuilder.append("?filename=").append(URLEncoder.encode(fileName, "UTF-8"));
                return urlBuilder.toString();
            }
        } catch (Exception e) {
            // 捕获异常，避免流未关闭导致资源泄露（可选：加日志记录异常）
            e.printStackTrace();
            return "Excel生成或上传失败：" + e.getMessage();
        } finally {
            // 关闭流（
            if (out != null) {
                out.close();
            }
        }

        // 6. 上传失败的兜底返回
        return "Excel上传文件服务失败";
    }
}
