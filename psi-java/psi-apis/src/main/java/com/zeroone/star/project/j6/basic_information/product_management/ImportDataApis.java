package com.zeroone.star.project.j6.basic_information.product_management;

import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsImportResultDTO;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


public interface ImportDataApis {

    /**
     * 导入数据
     * @param file 文件
     * @return 导入结果
     */
    JsonVO<GoodsImportResultDTO> importGoods(@RequestPart("file") MultipartFile file);
}
