package com.zeroone.star.supportinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage.CodeDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage.CodeDetailDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage.UpdateCodeDTO;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.codemanage.CodeFormQuery;
import com.zeroone.star.supportinfo.entity.Code;
import com.zeroone.star.supportinfo.mapper.CodeMapper;
import com.zeroone.star.supportinfo.service.ICodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 条码 服务实现类
 * </p>
 *
 * @author hh
 * @since 2025-10-23
 */
@Service
public class CodeServiceImpl extends ServiceImpl<CodeMapper, Code> implements ICodeService {
    @Resource
    CodeStructMapper codeStructMapper;

    @Override
    public Page<Code> queryCodeList(CodeFormQuery query) {
        try {
            // 参数校验
            if (query == null) {
                throw new RuntimeException("查询参数不能为空");
            }

            // 获取分页参数
            long pageIndex = query.getPageIndex();
            long pageSize = query.getPageSize();

            // 检查pageIndex是否无效
            if (pageIndex <= 0) {
                throw new RuntimeException("页码(pageIndex)必须大于0");
            }

            // 检查pageSize是否无效
            if (pageSize <= 0) {
                throw new RuntimeException("每页条数(pageSize)必须大于0");
            }

            // 限制pageSize最大值
            if (pageSize > 100) {
                throw new RuntimeException("每页条数(pageSize)不能超过100");
            }

            // 创建查询条件包装器
            QueryWrapper<Code> wrapper = new QueryWrapper<>();

            // 添加条件查询
            if (StringUtils.hasText(query.getName())) {
                wrapper.like("name", query.getName());
            }
            if (StringUtils.hasText(query.getInfo())) {
                wrapper.like("info", query.getInfo());
            }
            if (query.getType() != null) {
                wrapper.eq("type", query.getType());
            }
            if (StringUtils.hasText(query.getData())) {
                wrapper.like("data", query.getData());
            }

            // 使用校验过的分页参数
            Page<Code> page = new Page<>(pageIndex, pageSize);
            return baseMapper.selectPage(page, wrapper);
        } catch (NumberFormatException e) {
            // 处理参数类型转换异常
            throw new RuntimeException("分页参数格式错误，请输入正确的数值");
        } catch (Exception e) {
            // 处理其他异常
            throw new RuntimeException("查询条码列表失败: " + e.getMessage());
        }
    }

    @Override
    public Code getCodeById(String id) {
        // 参数校验
        if (id == null || id.trim().isEmpty()) {
            throw new RuntimeException("条码ID不能为空");
        }

        // 使用MyBatis-Plus的getById方法根据ID查询条码
        Code code = this.getById(id);

        // 如果未找到记录，可以返回null，由Controller层处理
        // Controller层会检查并返回友好的错误信息
        return code;
    }

    @Override
    public String generateCodeImage(String id, int width, int height) {
        // 参数校验
        if (id == null || id.trim().isEmpty()) {
            throw new RuntimeException("条码ID不能为空");
        }

        // 尺寸校验
        if (width <= 0 || height <= 0) {
            throw new RuntimeException("图片尺寸必须为正数");
        }

        // 根据ID获取条码信息
        Code code = this.getById(id);
        if (code == null) {
            throw new RuntimeException("未找到ID为[" + id + "]的条码信息");
        }

        if (!StringUtils.hasText(code.getInfo())) {
            throw new RuntimeException("条码内容为空，无法生成图片");
        }

        try {
            // 根据条码类型生成不同的图片
            BufferedImage image;
            // 数据库中type字段为tinyint(1)，1表示二维码，0表示条形码
            if (code.getType() != null && code.getType() == 1) {
                // 生成二维码
                image = generateQrCode(code.getInfo(), width, height);
            } else if (code.getType() != null && code.getType() == 0) {
                // 检查条形码内容是否包含非ASCII字符
                if (!isAscii(code.getInfo())) {
                    throw new RuntimeException("条形码仅支持ASCII字符，当前内容包含中文或其他非ASCII字符");
                }
                // 生成条形码
                image = generateBarCode(code.getInfo(), width, height);
            } else {
                // 对于无效的类型值，抛出异常
                throw new RuntimeException("无效的条码类型: " + code.getType() + ", 仅支持类型1(二维码)和类型0(条形码)");
            }

            // 检查生成的图片是否有效
            if (image == null || image.getWidth() <= 0 || image.getHeight() <= 0) {
                throw new RuntimeException("生成的图片无效");
            }

            // 将BufferedImage转换为Base64字符串
            String base64Image = imageToBase64(image);

            // 检查Base64转换结果
            if (base64Image == null || base64Image.isEmpty()) {
                throw new RuntimeException("图片数据转换失败");
            }

            return base64Image;
        } catch (RuntimeException e) {
            // 直接传递已有的业务异常
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("生成条码图片失败: " + e.getMessage(), e);
        }
    }

    /**
     * 检查字符串是否只包含ASCII字符
     */
    private boolean isAscii(String str) {
        if (str == null) {
            return true;
        }
        for (char c : str.toCharArray()) {
            if (c > 127) {
                return false;
            }
        }
        return true;
    }

    /**
     * 生成二维码
     */
    private BufferedImage generateQrCode(String content, int width, int height) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // 设置字符集编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 设置纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 设置边距
        hints.put(EncodeHintType.MARGIN, 1);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 生成条形码
     */
    private BufferedImage generateBarCode(String content, int width, int height) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // 设置字符集编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 设置边距
        hints.put(EncodeHintType.MARGIN, 1);

        Code128Writer barcodeWriter = new Code128Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(content, BarcodeFormat.CODE_128, width, height, hints);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 将BufferedImage转换为Base64字符串
     */
    private String imageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        // 转换为纯Base64字符串，不添加前缀
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    @Transactional
    public String saveCode(CodeDTO codeDTO){

        Code code=codeStructMapper.codeDtoToCode(codeDTO);

        if(baseMapper.insert(code)==1){

            return code.getId();
        }else{
            return null;
        }

    }

    @Transactional
    public List<String> removeCodes(List<String> ids) {
        List<String> successIds = new ArrayList<>();
        // 遍历每个ID，逐个删除并记录成功的ID
        for (String id : ids) {
            if (this.removeById(id)) {
                successIds.add(id);
            }
        }
        return successIds;
    }

    @Transactional
    public String updateCode(UpdateCodeDTO codeTO){
        Code code=codeStructMapper.updateCodeDtoToCode(codeTO);
        if(baseMapper.updateById(code)==1){
            return  code.getId();
        }
        else return null;
    }

}
