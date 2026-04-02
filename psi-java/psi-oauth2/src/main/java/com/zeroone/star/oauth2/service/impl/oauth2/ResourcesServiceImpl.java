package com.zeroone.star.oauth2.service.impl.oauth2;



import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.zeroone.star.oauth2.entity.FunIdAndUrl;
import com.zeroone.star.oauth2.entity.Menu;
import com.zeroone.star.oauth2.entity.Role;
import com.zeroone.star.oauth2.mapper.RoleMapper;
import com.zeroone.star.oauth2.service.IMenuService;
import com.zeroone.star.oauth2.service.IRoleService;
import com.zeroone.star.project.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 描述：资源与角色缓存初始化服务
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@Service
@Slf4j
public class ResourcesServiceImpl {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private IMenuService menuService;
    @Resource
    private IRoleService roleService;

    @Resource
    private RoleMapper roleMapper;

    /**
     * 实现角色权限的缓存
     * Redis 结构：key=RedisConstant.RESOURCE_ROLES_MAP（总Hash键），
     * field=权限的url，value=对应角色的keyword的集合
     */
    public void loadData(boolean isClearCacheBeforeLoad) {
        // 1. 清除旧缓存（直接删除总Hash键，一次性清理所有角色权限缓存）
        if (isClearCacheBeforeLoad) {
            redisTemplate.delete(RedisConstant.RESOURCE_ROLES_MAP);
            log.info("已清除旧缓存：{}", RedisConstant.RESOURCE_ROLES_MAP);
        }

        //role 里去找id的List集合
        List<Role>roleList=roleService.list();
        //keyword  funids
        Map<String,List<String>> keyWordAndFunIds=new HashMap<>();
        for (Role role:roleList){
            String root=role.getRoot();
            List<String> funIds=extractFunIdsToIntegerList(root);
            System.out.println("--------------");
            System.out.println("funIds:"+funIds);
            System.out.println("--------------");

            if(role.getKeyword()!=null){
                keyWordAndFunIds.put(role.getKeyword(),funIds);
            }
        }

        //id url
        List<FunIdAndUrl> IdAndUrls=roleMapper.selectIdAndUrl();

        Map<String,List<String>> rolePermissionHash = new HashMap<>();

        //遍历所有的id，如果有url的，将url设置为键、value设置这个id在keyWordAndFunIds作为值为的 key
        for (FunIdAndUrl idAndUrl: IdAndUrls) {
            String url = idAndUrl.getUrl();
            if (url != null) {
                String id = idAndUrl.getId();
                List<String>matchKeyWords =new ArrayList<>();

                //进行keyword数组的构建
                for(String keyword : keyWordAndFunIds.keySet()){
                    List<String> funIds = keyWordAndFunIds.get(keyword);
                    if(funIds != null && funIds.contains(id)){
                        matchKeyWords.add(keyword);
                    }
                }
                if(!matchKeyWords.isEmpty()){
                    rolePermissionHash.put(url,matchKeyWords);
                }
            }
        }

        // 6. 写入Redis（核心：单层Hash结构，总键=RESOURCE_ROLES_MAP）
        if (!rolePermissionHash.isEmpty()) {
            // 批量写入Hash：key=总键，field=keyword，value=name集合JSON
            redisTemplate.opsForHash().putAll(RedisConstant.RESOURCE_ROLES_MAP, rolePermissionHash);
            log.info("缓存加载完成！总Hash键：{}，共缓存{}个角色的权限集合",
                    RedisConstant.RESOURCE_ROLES_MAP, rolePermissionHash.size());

        } else {
            log.warn("未匹配到任何角色-权限关系，未写入Redis");
        }
    }


    /**
     * 递归提取 JSON 中所有层级的 fun.id 列表
     * @param json 包含嵌套结构的 JSON 字符串
     * @return 所有 fun.id 的集合
     */
    public static List<String> extractFunIdsToIntegerList(String json) {
        List<String> funIdList = new ArrayList<>();
        if (json == null || json.trim().isEmpty()) {
            log.debug("传入的 JSON 为空");
            return funIdList;
        }

        try {
            JSONArray rootArray = new JSONArray(json);
            extractFunIdsFromJSONArray(rootArray, funIdList);
        } catch (Exception e) {
            log.warn("JSON 解析失败", e);
        }

        return funIdList;
    }

    /**
     * 递归从 JSONArray 中提取所有 fun.id
     */
    private static void extractFunIdsFromJSONArray(JSONArray array, List<String> result) {
        for (Object item : array) {
            if (item instanceof JSONObject) {
                JSONObject obj = (JSONObject) item;
                // 提取当前节点的 fun.id
                Object funObj = obj.get("fun");
                if (funObj instanceof JSONArray) {
                    JSONArray funArray = (JSONArray) funObj;
                    for (Object funItem : funArray) {
                        if (funItem instanceof JSONObject) {
                            Object idObj = ((JSONObject) funItem).get("id");
                            if (idObj != null) {
                                // 修复：用 toString() 替代强转，兼容 Integer/String
                                String idStr = idObj.toString().trim();
                                if (!idStr.isEmpty()) {
                                    result.add(idStr);
                                }
                            }
                        }
                    }
                }

                // 递归处理 children 节点
                Object childrenObj = obj.get("children");
                if (childrenObj instanceof JSONArray) {
                    extractFunIdsFromJSONArray((JSONArray) childrenObj, result);
                }
            }
        }
    }


    @PostConstruct
    public void init() {
        loadData(false);
    }
}
