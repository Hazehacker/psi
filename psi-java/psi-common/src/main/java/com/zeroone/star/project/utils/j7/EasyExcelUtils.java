package com.zeroone.star.project.utils.j7;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EasyExcelUtils {
    public static boolean isNumber(Object obj) {
        try {
            new BigDecimal(obj.toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("参数不匹配，" + obj + "不是数字类型");
        }
        return true;
    }

    /**
     *
     * @param mapData excel数据文件
     * @param clazz 映射的实体类
     * @param index 行索引
     * @return 实体类
     * @param <T> 实体类类型
     */
    public <T> T excelToBean(List<Object> mapData, Class<T> clazz, int index) throws Exception {
        Field[] columnName = clazz.getDeclaredFields();
        Map<String, Object> map=new HashMap<>();

        if (index >= mapData.size()) {
            return null;
        }
        for (int i = 0; i < columnName.length; i++) {
            try {
                if(columnName[i].getType()==BigDecimal.class && isNumber(mapData.get(i))){
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(mapData.get(i)));
                    map.put(columnName[i].getName(), bigDecimal);
                } else {
                    if(ObjectUtil.isNull(mapData.get(i))){
                        continue;
                    }
                    map.put(columnName[i].getName(),mapData.get(i) );
                }
            }catch (IllegalArgumentException argumentException){
                System.out.println(columnName[i].getName()+":"+argumentException.getMessage());
                throw argumentException ;
            }catch (Exception e){
                System.out.println(e.getMessage());
                throw e ;
            }

        }

        return BeanUtil.toBean(map, clazz);
    }


}
