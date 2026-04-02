package com.zeroone.star.project.j2.store;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.OtherOutListInfoDTO;
import com.zeroone.star.project.query.j2.store.OtherOutQuery;
import com.zeroone.star.project.vo.JsonVO;
import java.util.List;
import com.zeroone.star.project.dto.j2.store.OtherOutListDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-19 18:46
 * @Description: 其他出库单接口
 * @Version: 1.0
 */
public interface OtherOutApis {
    /**
     * 审核或反审核
     * @param ids 审核id列表
     * @return 修改结果
     */
    JsonVO<String> examine(List<Integer> ids);

    /**
     * 核对或反核对
     * @param ids 核对id列表
     * @return 修改结果
     */
    JsonVO<String> check(List<Integer> ids);
    /**
     * 修改其他出库单
     * @param otherOutListDTO 修改参数
     * @return 修改结果
     */
    JsonVO<String> updateOtherOutList(OtherOutListDTO otherOutListDTO);


    /**
     * 删除其他出库单
     * @param ids 删除的出库单ID列表
     * @return 删除结果
     */
    JsonVO<List<String>> deleteOtherOutList(List<Integer> ids);

    /**
     * 获取其他出库单列表
     * @param query 查询出库单列表
     * @return 获取结果
     */
    JsonVO<PageDTO<OtherOutListDTO>> listOtherOut(OtherOutQuery query);
    /**
     * 获取指定其他出库单详细
     * @param id 获取的出库单ID
     * @return 获取结果
     */
    JsonVO<OtherOutListInfoDTO> getOtherOutListInfo(String id);
//
    /**
     * 新增其他出库单
     * @param otherOutListDTO 添加参数
     * @return 新增结果
     */
    JsonVO<String> addOtherOutList(OtherOutListDTO otherOutListDTO);


    /**
     * 导出其他出库单简单报表
     * @param ids 需要导出的出库单ID列表
     * @return
     */
    JsonVO<ResponseEntity<byte[]>> exportOrderListExcel(List<String> ids);

    /**
     * 导出其他出库单详细报表
     * @param ids 需要导出的出库单ID列表
     * @return
     */
    JsonVO<ResponseEntity<byte[]>> exportOrderDetailExcel(List<String> ids);

    /**
     * 批量导入其他出库单数据
     * @param file 上传的Excel文件
     * @return 导入结果
     */
    ResponseEntity<JsonVO<String>> importOrderList(MultipartFile file);

}
