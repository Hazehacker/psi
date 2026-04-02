package com.zeroone.star.finance.service.impl.otherexpense;

import cn.hutool.core.date.DateTime;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.finance.entity.*;
import com.zeroone.star.finance.mapper.*;
import com.zeroone.star.finance.mapper.otherexpense.OceDetailViewMapper;
import com.zeroone.star.finance.mapper.otherexpense.OceInfoViewMapper;
import com.zeroone.star.finance.mapper.otherexpense.OceSimpleViewMapper;
import com.zeroone.star.finance.service.otherexpense.OtherExpenseService;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.components.pdf.PdfComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.otherexpense.*;
import com.zeroone.star.project.query.j8.finance.othexpense.OtherExpenseQuery;
import com.zeroone.star.project.vo.JsonVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 其它支出单服务实现
 * ---------------------------------
 * 功能模块：
 * 1. 获取支出单列表（支持条件查询 + 分页）
 * 2. 获取指定支出单详情
 * 3. 新增支出单
 * 4. 修改支出单
 * 5. 审核/反审核支出单（支持批量）
 * 6. 删除支出单（支持批量）
 * 7. 导入数据（Excel/CSV）
 * 8. 导出简表（基础字段）
 * 9. 导出详表（含明细字段）
 */
@Slf4j
@Validated
@Service
public class OtherExpenseServiceImpl implements OtherExpenseService {
    @Resource
    private EasyExcelComponent easyExcel;

    @Resource
    private PdfComponent pdfComponent;

    @Resource
    private OceDetailViewMapper detailMapper;

    @Resource
    private OceSimpleViewMapper simpleViewMapper;

    @Resource
    private OceInfoViewMapper infoMapper;

    @Resource
    private OceMapper oceMapper;

    @Resource
    private OceInfoMapper oceInfoMapper;

    @Resource
    private OceBillMapper oceBillMapper;

    @Resource
    private FrameMapper frameMapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    PeopleMapper peopleMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    private SupplierMapper supplierMapper;


    @Resource
    private IetMapper ietMapper;
    /**
     * 1.获取支出单列表（支持条件 + 分页）
     *
     * @param query 查询参数
     * @return 其它支出单列表
     */
    public PageDTO<OtherExpenseDTO> getExpenseList(OtherExpenseQuery query) {
        log.info("条件查询:{}", query);

        long pageIndex = query.getPageIndex() < 1 ? 1 : query.getPageIndex();
        long pageSize = query.getPageSize() < 1 ? 10 : query.getPageSize();

        // 需要总数：new Page<>(pi, ps)；不要总数就用 new Page<>(pi, ps, false)
        Page<OceSimpleViewDO> page = new Page<>(pageIndex, pageSize);

        // 分页查询（由插件自动拼 LIMIT 与 COUNT）
        IPage<OceSimpleViewDO> p = simpleViewMapper.selectByCondition(page, query);

        // DO -> DTO
        List<OtherExpenseDTO> rows = p.getRecords().stream().map(o -> {
            OtherExpenseDTO dto = new OtherExpenseDTO();
            BeanUtils.copyProperties(o, dto);
            return dto;
        }).collect(Collectors.toList());

        // 组装返回
        PageDTO<OtherExpenseDTO> pageDTO = new PageDTO<>();
        pageDTO.setPageIndex(p.getCurrent());
        pageDTO.setPageSize(p.getSize());
        pageDTO.setTotal(p.getTotal());
        pageDTO.setPages(p.getPages());
        pageDTO.setRows(rows);

        return pageDTO;
    }

    /**
     * 2.根据ID获取支出单详情
     *
     * @param id 查询ID
     * @return 支出单详情
     */
    @Transactional(readOnly = true)
    public OtherExpenseDetailDTO getDetailById(String id) {
        log.info("获取id为{}支出单详情", id);
        //获取主DO列表,copy值
        OceDetailViewDO mainDO = detailMapper.selectById(id);
        //为空说明不存在
        if (mainDO == null) return null;
        OtherExpenseDetailDTO mainDTO = new OtherExpenseDetailDTO();
        BeanUtils.copyProperties(mainDO, mainDTO);
        //获取信息DO列表,转化为信息DTO列表
        List<OceInfoViewDO> infoDOS = infoMapper.selectByPid(id);
        mainDTO.setInfo(infoDOS.stream().map(info -> {
            OtherExpenseInfoDTO dto = new OtherExpenseInfoDTO();
            BeanUtils.copyProperties(info, dto);
            return dto;
        }).collect(Collectors.toList()));
        return mainDTO;
    }

    /**
     * 8.导出选中ids的其它支出单简表
     *
     * @param ids 支出单ID列表
     * @return ResponseEntity<byte [ ]> excel文件
     */
    @SneakyThrows
    public ResponseEntity<byte[]> export(List<String> ids) {
        // 查询出指定ID的其他支出单数据，并转成DTO
        List<OceSimpleViewDO> doList = simpleViewMapper.selectByIds(ids);
        List<OtherExpenseDTO> dtoList = doList.stream().map(o -> {
            OtherExpenseDTO dto = new OtherExpenseDTO();
            BeanUtils.copyProperties(o, dto);
            return dto;
        }).collect(Collectors.toList());
        // 创建了一个内存输出流ByteArrayOutputStream，用来在内存中暂存Excel数据
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 调用export方法，生成excel文件，将数据写入内存输出流中
        easyExcel.export("其它支出单", out, OtherExpenseDTO.class, dtoList);
        // 构造HTTP响应，让用户能够下载Excel文件
        HttpHeaders headers = new HttpHeaders();
        // 一个带有时间戳的文件名，确保每次导出的文件名都不一样
        String filename = "rep-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        // 设置响应的内容类型为二进制流（APPLICATION_OCTET_STREAM），告诉浏览器这是一个需要下载的文件
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // 将内存中的Excel数据转换为字节数组并包装成ResponseEntity返回
        ResponseEntity<byte[]> res = new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
        out.close();
        return res;
    }

    /**
     * 9. 批量导出其它支出单明细
     *
     * @param ids 支出单ID列表
     * @return ResponseEntity<byte [ ]> excel
     */
    @SneakyThrows
    @Override
    public ResponseEntity<byte[]> exportDetail(List<String> ids) {
        String desktopPath = null;
        List<String> files = new ArrayList<>();
        // 查询出指定ID的其他支出单数据(主单+明细)，并转成DTO
        List<OtherExpenseDetailDTO> detailDTOList = new ArrayList<>();
        for (String id : ids) {
            // 获取主单信息
            OceDetailViewDO mainDO = detailMapper.selectById(id);
            if (mainDO == null) continue;
            OtherExpenseDetailDTO mainDTO = new OtherExpenseDetailDTO();
            BeanUtils.copyProperties(mainDO, mainDTO);
            // 获取明细信息
            List<OceInfoViewDO> infoDOS = infoMapper.selectByPid(id);
            List<OtherExpenseInfoDTO> infoDTOS = new ArrayList<>();
            if (infoDOS != null) {
                for (OceInfoViewDO infoDO : infoDOS) {
                    OtherExpenseInfoDTO infoDTO = new OtherExpenseInfoDTO();
                    BeanUtils.copyProperties(infoDO, infoDTO);
                    infoDTOS.add(infoDTO);
                }
            }
            mainDTO.setInfo(infoDTOS);
            detailDTOList.add(mainDTO);
        }
        if (detailDTOList.isEmpty()) return null;

        // 生成PDF并保存到本地
        for (OtherExpenseDetailDTO otherExpenseDetailDTO : detailDTOList) {
            Map<String, Object> datas = new HashMap<>();
            datas.put("supplier", otherExpenseDetailDTO.getSupplier());
            datas.put("time", otherExpenseDetailDTO.getTime());
            datas.put("pid", otherExpenseDetailDTO.getId());
            datas.put("number", otherExpenseDetailDTO.getNumber());
            datas.put("total", otherExpenseDetailDTO.getTotal());
            datas.put("actual", otherExpenseDetailDTO.getActual());
            datas.put("money", otherExpenseDetailDTO.getMoney());
            datas.put("account", otherExpenseDetailDTO.getAccount());
            datas.put("people", otherExpenseDetailDTO.getPeople());
            datas.put("data", otherExpenseDetailDTO.getData());
            datas.put("items", otherExpenseDetailDTO.getInfo());
            // 生成PDF
            // 获取桌面路径（跨平台）
            desktopPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "pdfTemp";
            // 创建桌面上的 pdfTemp 目录（如果不存在）
            File dir = new File(desktopPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 拼接 PDF 文件输出路径
            String outputPath = desktopPath + File.separator + otherExpenseDetailDTO.getNumber() + ".pdf";
            files.add(outputPath);

            //用模板"ocedetail.fo"和数据生成PDF文件
            // 其中模板文件ocedetail.fo定义了PDF的样式和布局，
            // 里面使用了FreeMarker语法（如${supplier}、${time}等）来占位，这些占位符会被实际数据替换。
            pdfComponent.generateToFile("ocedetail.fo", datas, outputPath);
        }

        String zipName = "pdfs.zip";
        if (desktopPath == null) {
            throw new RuntimeException("压缩文件zip失败");
        }
        String zipPath = desktopPath + File.separator + zipName;
        filesToZip(files, zipPath);// 【核心】 将所有PDF文件压缩成zip
        //将本地文件读取成字节
        byte[] bytes = Files.readAllBytes(Paths.get(zipPath));
        // 删除文件
        for (String file : files) {
            deleteFile(file);
        }
        deleteFile(zipPath);
        // 响应给前端
        HttpHeaders headers = new HttpHeaders();
        String filename = "rep-detail-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".zip";
        headers.setContentDispositionFormData("attachment", filename);
        // 设置响应的内容类型为二进制流（APPLICATION_OCTET_STREAM），告诉浏览器这是一个需要下载的文件
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(bytes, headers, HttpStatus.CREATED);
    }



    // 新增支出单
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addOtherExpense(OtherExpenseViewDTO otherExpense, List<OtherExpenseInfoViewDTO> otherExpenseInfoList) {
        // ===== 基础校验 =====
        if (otherExpense == null) {
            throw new RuntimeException("主表对象不能为空");
        }
        if (otherExpenseInfoList == null || otherExpenseInfoList.isEmpty()) {
            throw new RuntimeException("子表明细不能为空");
        }
//        if (simpleViewMapper == null) {
//            throw new RuntimeException("simpleViewMapper 未注入");
//        }
//        if (infoMapper == null) {
//            throw new RuntimeException("infoMapper 未注入");
//        }

        // ===== 自动生成主表ID =====
        String mainId = "oce" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        otherExpense.setId(mainId);

        // ===== 检查主表主键是否重复 =====
        OtherExpenseViewDTO existMain = simpleViewMapper.selectById(mainId);
        if (existMain != null) {
            throw new RuntimeException("主表主键重复: " + mainId);
        }

        // 检查account、frame、supplier、user、people字段是否存在于对应的表中
        if (simpleViewMapper.countAccountById(otherExpense.getAccount()) == 0) {
            throw new RuntimeException("账户不存在：" + otherExpense.getAccount());
        }
        if (simpleViewMapper.countFrameById(otherExpense.getFrame()) == 0) {
            throw new RuntimeException("组织不存在：" + otherExpense.getFrame());
        }
        if (simpleViewMapper.countSupplierById(otherExpense.getSupplier()) == 0) {
            throw new RuntimeException("供应商不存在：" + otherExpense.getSupplier());
        }
        if (simpleViewMapper.countPeopleById(otherExpense.getPeople()) == 0) {
            throw new RuntimeException("人员不存在：" + otherExpense.getPeople());
        }
        if (simpleViewMapper.countUserById(otherExpense.getUser()) == 0) {
            throw new RuntimeException("用户不存在：" + otherExpense.getUser());
        }

        // ===== 校验每个子表的 iet 是否存在 =====
        for (OtherExpenseInfoViewDTO detail : otherExpenseInfoList) {
            if (infoMapper.countIetById(detail.getIet()) == 0) {
                throw new RuntimeException("收支类别不存在：" + detail.getIet());
            }
        }

        // ===== 校验总金额与主表一致性 =====
        BigDecimal total = otherExpenseInfoList.stream()
                .map(i -> i.getMoney() == null ? BigDecimal.ZERO : i.getMoney())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        otherExpense.setTotal(total);

        // ===== 主表和子表金额一致性校验 =====
        if (otherExpense.getTotal().compareTo(total) != 0 ||
                otherExpense.getActual().compareTo(total) != 0 ||
                otherExpense.getMoney().compareTo(total) != 0) {
            throw new RuntimeException("主表金额不一致：子表金额总和为 " + total +
                    "，但 单据金额 =" + otherExpense.getTotal() +
                    "，实际金额 =" + otherExpense.getActual() +
                    "，实付金额 =" + otherExpense.getMoney());
        }

        // ===== 主表的金额校验 =====
        if (otherExpense.getActual().compareTo(total) > 0) {
            throw new RuntimeException("实际金额不能大于单据金额");
        }
        if (otherExpense.getMoney().compareTo(otherExpense.getActual()) > 0) {
            throw new RuntimeException("实付金额不能大于实际金额");
        }

        // ===== 默认审核与核销状态 =====
        otherExpense.setExamine(0);
        otherExpense.setNucleus(0);

        // ===== 插入主表 =====
        int r1 = simpleViewMapper.insertOce(otherExpense);

        // ===== 生成并插入子表 =====
        for (OtherExpenseInfoViewDTO detail : otherExpenseInfoList) {
            // 子表主键 = 时间戳 + 随机后缀，防止重复
            String detailId = "oce_info" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                    + "_" + UUID.randomUUID().toString().substring(0, 6);
            detail.setId(detailId);
            detail.setPid(mainId);
        }
        int r2 = infoMapper.insertOceInfoView(otherExpenseInfoList);

        return r1 > 0 && r2 > 0;
    }

    // 修改支出单
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOtherExpense(OtherExpenseViewDTO otherExpense, List<OtherExpenseInfoViewDTO> otherExpenseInfoList) {
        // ===== 校验主表存在性 =====
        OtherExpenseViewDTO origin = simpleViewMapper.selectById(otherExpense.getId());
        if (origin == null) {
            throw new RuntimeException("单据不存在");
        }
        if (origin.getExamine() != 0) {
            throw new RuntimeException("单据[" + otherExpense.getId() + "]已审核，不能修改");
        }

        // ===== 校验子表 =====
        if (otherExpenseInfoList == null || otherExpenseInfoList.isEmpty()) {
            throw new RuntimeException("子表明细不能为空");
        }

        // 检查account、frame、supplier、user、people字段是否存在于对应的表中
        if (simpleViewMapper.countAccountById(otherExpense.getAccount()) == 0) {
            throw new RuntimeException("账户不存在：" + otherExpense.getAccount());
        }
        if (simpleViewMapper.countFrameById(otherExpense.getFrame()) == 0) {
            throw new RuntimeException("组织不存在：" + otherExpense.getFrame());
        }
        if (simpleViewMapper.countSupplierById(otherExpense.getSupplier()) == 0) {
            throw new RuntimeException("供应商不存在：" + otherExpense.getSupplier());
        }
        if (simpleViewMapper.countPeopleById(otherExpense.getPeople()) == 0) {
            throw new RuntimeException("人员不存在：" + otherExpense.getPeople());
        }
        if (simpleViewMapper.countUserById(otherExpense.getUser()) == 0) {
            throw new RuntimeException("用户不存在：" + otherExpense.getUser());
        }

        // ===== 校验每个子表的 iet 是否存在 =====
        for (OtherExpenseInfoViewDTO detail : otherExpenseInfoList) {
            if (infoMapper.countIetById(detail.getIet()) == 0) {
                throw new RuntimeException("收支类别不存在：" + detail.getIet());
            }
        }

        // ===== 重新计算总金额 =====
        BigDecimal total = otherExpenseInfoList.stream()
                .map(i -> i.getMoney() == null ? BigDecimal.ZERO : i.getMoney())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        otherExpense.setTotal(total);

        // ===== 主表和子表金额一致性校验 =====
        if (otherExpense.getTotal().compareTo(total) != 0 ||
                otherExpense.getActual().compareTo(total) != 0 ||
                otherExpense.getMoney().compareTo(total) != 0) {
            throw new RuntimeException("主表金额不一致：子表金额总和为 " + total +
                    "，但 单据金额 =" + otherExpense.getTotal() +
                    "，实际金额 =" + otherExpense.getActual() +
                    "，实付金额 =" + otherExpense.getMoney());
        }

        // ===== 主表的金额校验 =====
        if (otherExpense.getActual().compareTo(total) > 0) {
            throw new RuntimeException("实际金额不能大于单据金额");
        }
        if (otherExpense.getMoney().compareTo(otherExpense.getActual()) > 0) {
            throw new RuntimeException("实付金额不能大于实际金额");
        }

        // ===== 更新主表 =====
        int r1 = simpleViewMapper.updateOce(otherExpense);

        // ===== 删除旧子表 =====
        infoMapper.deleteOceInfoView(otherExpense.getId());

        // ===== 新增新子表 =====
        for (OtherExpenseInfoViewDTO detail : otherExpenseInfoList) {
            String detailId = "oce_info" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                    + "_" + UUID.randomUUID().toString().substring(0, 6);
            detail.setId(detailId);
            detail.setPid(otherExpense.getId());
        }
        int r2 = infoMapper.insertOceInfoView(otherExpenseInfoList);

        return r1 > 0 && r2 > 0;
    }

    // 批量审核/反审核
    @Override
    public boolean examineOtherExpense(List<String> ids, boolean approve) {
        if (ids == null || ids.isEmpty())
            throw new RuntimeException("未选择导出数据内容");

        int examine = approve ? 1 : 0; // 1=已审核, 0=未审核
        int count = simpleViewMapper.updateExamine(ids, examine);

        if (count > 0) {
            System.out.println("操作单据成功");
            return true;
        } else {
            System.out.println("操作单据失败");
            return false;
        }
    }

    private void filesToZip(List<String> files, String zipPath) throws IOException {
        // 创建ZIP输出流
        FileOutputStream fos = new FileOutputStream(zipPath);
        ZipOutputStream zos = new ZipOutputStream(fos);
        // 遍历所有要打包的文件路径
        for (String file : files) {
            File f = new File(file);
            FileInputStream fis = new FileInputStream(f);
            // 创建ZIP条目（文件名就是PDF文件名
            ZipEntry entry = new ZipEntry(f.getName());
            zos.putNextEntry(entry);
            // 逐块读取文件内容并写入ZIP
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            // 关闭当前文件流和条目
            fis.close();
            zos.closeEntry();
        }
        zos.close();
    }

    /**
     * 删除指定路径文件
     */
    private void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    System.err.println("文件删除失败：" + filePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 7.导入支出单
     *
     * @param file 上传的文件
     * @return JsonVO<String>
     */
    @Override
    @Transactional
    public JsonVO<OtherExpenseImportResultDTO> importOtherExpenseExcel(MultipartFile file) {
        int totalCount = 0;
        int successCount = 0;
        int failCount = 0;
        List<String> failMessages = new ArrayList<>();
        System.err.println("校验文件");
        try {
            // 1. 检查文件，确保上传的是合法的excel文件
            if (file.isEmpty()) {
                return JsonVO.fail(new OtherExpenseImportResultDTO(0, 0, 0,
                        Collections.singletonList("文件为空")));
            }

            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
                return JsonVO.fail(new OtherExpenseImportResultDTO(0, 0, 0,
                        Collections.singletonList("请上传Excel文件")));
            }

            // 2. 定义存储容器
            List<Oce> oceList = new ArrayList<>();
            List<OceInfo> oceInfoList = new ArrayList<>();
            List<OceBill> oceBillList = new ArrayList<>();
            List<Iet> ietList = new ArrayList<>();

            // 3. 使用 EasyExcel 读取三个工作表的数据
            try (InputStream inputStream = file.getInputStream()) {
                ExcelReader excelReader = EasyExcel.read(inputStream).build();

                ReadSheet sheet1 = EasyExcel.readSheet("其它支出单主表")
                        .head(Oce.class)
                        .registerReadListener(new PageReadListener<Oce>(oceList::addAll))
                        .build();

                ReadSheet sheet2 = EasyExcel.readSheet("其它支出单详情")
                        .head(OceInfo.class)
                        .registerReadListener(new PageReadListener<OceInfo>(oceInfoList::addAll))
                        .build();

                ReadSheet sheet3 = EasyExcel.readSheet("其它支出单核销详情")
                        .head(OceBill.class)
                        .registerReadListener(new PageReadListener<OceBill>(oceBillList::addAll))
                        .build();


                excelReader.read(sheet1, sheet2, sheet3);
                excelReader.finish();
            }

            // 4. 过滤空行（移除没有ID的无效行）
            oceList.removeIf(o -> o.getId() == null || o.getId().trim().isEmpty());
            oceInfoList.removeIf(i -> i.getId() == null || i.getId().trim().isEmpty());
            oceBillList.removeIf(b -> b.getId() == null || b.getId().trim().isEmpty());


            // 5. 统计总有效记录数（过滤空行后）
            totalCount = oceList.size() + oceInfoList.size() + oceBillList.size();
            if (totalCount == 0) {
                return JsonVO.success(new OtherExpenseImportResultDTO(0, 0, 0,
                        Collections.singletonList("无有效数据")));
            }

            System.err.println("校验并筛选有效数据");
            // 6. 校验并筛选有效数据【确保导入的数据在数据库中能找到相应的关联数据】
            // 6.1 校验Oce主表（关联frame、supplier、account、people表）
            List<Oce> validOceList = new ArrayList<>();
            for (Oce oce : oceList) {
                List<String> errors = new ArrayList<>();

                // 校验frame表ID是否存在
                System.err.println("检测frame");
                if (oce.getFrame() != null) {
                    Frame f = frameMapper.existsByName(oce.getFrame());
                    System.out.println(f);
                    if (f == null) {
                        errors.add("关联的frame不存在：" + oce.getFrame());
                    } else {
                        oce.setFrame(f.getId());
                    }
                }

                // 校验supplier表ID是否存在
                System.err.println("检测supplier");
                if (oce.getSupplier() != null) {
                    Supplier s = supplierMapper.existsByName(oce.getSupplier());
                    System.out.println(s);
                    if (s == null) {
                        errors.add("关联的supplier不存在：" + oce.getSupplier());
                    } else {
                        oce.setSupplier(s.getId());
                    }
                }
                // 校验account表ID是否存在
                System.err.println("检测account");
                if (oce.getAccount() != null) {
                    Account a = accountMapper.existsByName(oce.getAccount());
                    System.out.println(a);
                    if (a == null) {
                        errors.add("关联的account不存在：" + oce.getAccount());
                    } else {
                        oce.setAccount(a.getId());
                    }
                }

                // 校验people表ID是否存在
                System.err.println("检测people");
                if (oce.getPeople() != null) {
                    People p = peopleMapper.existsByName(oce.getPeople());
                    System.out.println(p);
                    if (p == null) {
                        errors.add("关联的people不存在：" + oce.getPeople());
                    } else {
                        oce.setPeople(p.getId());
                    }
                }

                // 校验user表ID是否存在
                System.err.println("检测user");
                if (oce.getUser() != null) {
                    User u = userMapper.existsByName(oce.getUser());
                    System.out.println(u);
                    if (u == null) {
                        errors.add("关联的user不存在：" + oce.getUser()); // 修正原错误信息中的"people"为"user"
                    } else {
                        oce.setUser(u.getId());
                    }
                }

                // 记录结果
                if (errors.isEmpty()) {
                    validOceList.add(oce);
                } else {
                    failMessages.add("其它支出单主表（ID：" + oce.getId() + "）导入失败：" + String.join("；", errors));
                    failCount++;
                }
            }


            // 6.2 提取有效Oce的ID
            Set<String> validOceIds = validOceList.stream()
                    .map(Oce::getId)
                    .collect(Collectors.toSet());


            System.err.println(validOceIds);

            // 6.3 校验OceInfo详情表（关联Oce主表和user表）
            List<OceInfo> validOceInfoList = new ArrayList<>();
            for (OceInfo info : oceInfoList) {
                List<String> errors = new ArrayList<>();
                // 校验关联的Oce主表ID是否有效
                if (info.getPid() == null || !validOceIds.contains(info.getPid())) {
                    errors.add("关联的其它支出单主表ID不存在或无效：" + info.getPid());
                }
                // 记录结果
                if (errors.isEmpty()) {
                    validOceInfoList.add(info);
                } else {
                    failMessages.add("其它支出单详情（ID：" + info.getId() + "）导入失败：" + String.join("；", errors));
                    failCount++;
                }
            }

            // 6.4 校验OceBill核销表
            List<OceBill> validOceBillList = new ArrayList<>();
            for (OceBill bill : oceBillList) {
                List<String> errors = new ArrayList<>();

                // 校验关联的Oce主表ID是否有效
                if (bill.getPid() == null || !validOceIds.contains(bill.getPid())) {
                    errors.add("关联的其它支出单主表ID不存在或无效：" + bill.getPid());
                }
                // 记录结果
                if (errors.isEmpty()) {
                    validOceBillList.add(bill);
                } else {
                    failMessages.add("其它支出单核销详情（ID：" + bill.getId() + "）导入失败：" + String.join("；", errors));
                    failCount++;
                }
            }


            //检验oce_info的iet是否存在
            ArrayList<OceInfo> validOceInfoList1 = new ArrayList<>();
            validOceInfoList.forEach(oceInfo -> {
                List<String> errors = new ArrayList<>();
                if (oceInfo.getIet() != null) {
                    Iet i = ietMapper.selectOneIet(oceInfo.getIet());
                    if (i == null) {
                        errors.add("关联的iet不存在：" + oceInfo.getIet());
                    } else {
                        oceInfo.setIet(i.getId());
                        validOceInfoList1.add(oceInfo);
                    }
                }
            });


            // 7. 批量插入有效数据
            if (!validOceList.isEmpty()) {
                oceMapper.insertBatchOce(validOceList);
            }
            if (!validOceInfoList.isEmpty()) {
                oceInfoMapper.insertBatchOceBill(validOceInfoList1);
                //插入对应的iet
            }
            if (!validOceBillList.isEmpty()) {
                oceBillMapper.insertBatchtOceBill(validOceBillList);
            }
            // 8. 统计成功数量
            successCount = validOceList.size() + validOceInfoList1.size() + validOceBillList.size();
            // 9. 返回结果
            OtherExpenseImportResultDTO result = new OtherExpenseImportResultDTO(
                    totalCount, successCount, failCount, failMessages
            );
            return JsonVO.success(result);
        } catch (Exception e) {
            //log.error("Excel导入失败", e);
            failCount = totalCount; // 异常时所有已统计的记录视为失败
            failMessages.add("系统异常导致导入失败：" + e.getMessage());
            OtherExpenseImportResultDTO result = new OtherExpenseImportResultDTO(
                    totalCount, successCount, failCount, failMessages
            );
            return JsonVO.fail(result);
        }
    }
}
