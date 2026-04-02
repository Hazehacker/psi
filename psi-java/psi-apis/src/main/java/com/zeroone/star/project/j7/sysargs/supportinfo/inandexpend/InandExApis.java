package com.zeroone.star.project.j7.sysargs.supportinfo.inandexpend;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.inandexpend.*;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.inandexpend.IetQuery;
import com.zeroone.star.project.vo.JsonVO;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 类名：InandExApis
 * 包名：com.zeroone.star.project.j7.sysargs.supportinfo.inandexpend
 * 描述：收支类别接口
 * 作者：map
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */

public interface InandExApis {

    /**
     * 查询收支类别名称
     * @param type 查询参数——收支类型
     * @return 类别名称列表
     */
    JsonVO<List<IetNameDTO>> listInandexName(@Min(value = 0, message = "类型必须为0（收入）或1（支出）")
                                             @Max(value = 1, message = "类型必须为0（收入）或1（支出）")
                                             Integer type);
    /**
     * 查询收支类别列表
     * @param query 查询参数
     * @return
     */
    JsonVO<PageDTO<IetListDTO>> listInandex(@Valid IetQuery query);
    /**
     * 查询类别详情
     * @param id
     * @return
     */
    JsonVO<IetDetailDTO> getInandexDetail(String id);
    /**
     *
     * 新增收支类别
     * @param  ietAddDTO 查询参数——收支类型
     * @return 类别名称列表
     */
    JsonVO<String> addInandex(IetAddDTO ietAddDTO);

    /**
     * 修改指定类别
     * @param ietUpdateDTO 查询参数
     * @return
     */
    JsonVO<String> updateInandex(IetUpdateDTO ietUpdateDTO);

    /**
     * 删除指定类别
     * @param id 删除的收支类别id
     * @return
     */
    JsonVO<String> deleteInandex(String id);
}
