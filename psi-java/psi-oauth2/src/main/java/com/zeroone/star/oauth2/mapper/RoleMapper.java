package com.zeroone.star.oauth2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.oauth2.entity.FunIdAndUrl;
import com.zeroone.star.oauth2.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 * @author 阿伟
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 通过用户编号查询角色
     * @param userId 用户编号
     * @return 角色列表
     */
    List<Role> selectByUserId(String userId);

    /**
     * 通过role里root下fun里面的所有id，与func_perm里的url进行匹配
     * @return 返回所有对应的url
     */
    List<FunIdAndUrl> selectIdAndUrl();
}
