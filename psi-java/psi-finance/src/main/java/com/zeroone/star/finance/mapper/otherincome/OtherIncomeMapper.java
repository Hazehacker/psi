package com.zeroone.star.finance.mapper.otherincome;

import com.zeroone.star.finance.entity.Ice;
import com.zeroone.star.project.dto.j8.finance.otherincome.AddOtherIncomeDTO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OtherIncomeMapper {

    // name -> id 字典
    @MapKey("name")
    Map<String, String> mapFrameName2Id();

    @MapKey("name")
    Map<String, String> mapCustomerName2Id();

    @MapKey("name")
    Map<String, String> mapAccountName2Id();

    @MapKey("name")
    Map<String, String> mapPeopleName2Id();

    @MapKey("name")
    Map<String, String> mapIetName2Id();

    List<String> listExistingIceNumbers(@Param("numbers") List<String> numbers);

    int batchInsertIce(@Param("list") List<Ice> list);

    int batchInsertInfosMap(@Param("list") List<Map<String, Object>> list);

    int batchInsertBillsMap(@Param("list") List<Map<String, Object>> list);

    // 基础校验 / 查询
    Ice selectIceStatus(@Param("id") String id);                 // 查审核状态等

    int existsCustomer(@Param("id") String id);

    int existsAccount(@Param("id") String id);

    int existsPeople(@Param("id") String id);

    List<String> selectExistingIetIds(@Param("list") List<String> ietIds);

    int deleteInfosByPids(@org.apache.ibatis.annotations.Param("list") List<String> ids);

    int deleteIceByIds(@org.apache.ibatis.annotations.Param("list") List<String> ids);

    // 更新单头（仅未审核）
    int updateIceHeadIfUnexamined(@Param("dto") AddOtherIncomeDTO dto);

    // 明细删旧插新（pid = dto.id）
    int deleteInfosByPid(@Param("dto") AddOtherIncomeDTO dto);

    int batchInsertInfos(@Param("dto") AddOtherIncomeDTO dto);

    // 可用于批量场景
    List<String> listAuditedIds(@Param("list") List<String> ids);
}
