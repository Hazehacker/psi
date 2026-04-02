package com.zeroone.star.project.components.excel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.function.Function;

/**
 * MyBatis-Plus 游标分页查询实现
 * 基于主键 ID 实现高效的深度分页
 *
 * @param <T> 实体类型
 */
public class MybatisCursorPageQuery<T> implements CursorPageQuery<T> {

    private final BaseMapper<T> mapper;
    private final Function<Integer, LambdaQueryWrapper<T>> wrapperBuilder;

    /**
     * 使用实体类创建游标分页查询
     * @param mapper MyBatis-Plus BaseMapper
     * @param wrapperBuilder 构建查询条件，接收 lastId 返回 WHERE id > lastId 的条件
     */
    public MybatisCursorPageQuery(BaseMapper<T> mapper,
                                   Function<Integer, LambdaQueryWrapper<T>> wrapperBuilder) {
        this.mapper = mapper;
        this.wrapperBuilder = wrapperBuilder;
    }

    @Override
    public List<T> nextPage(Object lastId, int pageSize) {
        Page<T> page = new Page<>(1, pageSize, false); // current=1, size=pageSize, isSearchCount=false
        LambdaQueryWrapper<T> wrapper = wrapperBuilder.apply(lastId == null ? 0 : (Integer) lastId);
        return mapper.selectPage(page, wrapper).getRecords();
    }

    @Override
    public boolean hasMore(Object lastId) {
        if (lastId == null) {
            return true; // 第一页前默认有数据
        }
        // 检查是否还有更多数据：查询比 lastId 大的记录是否存在
        LambdaQueryWrapper<T> wrapper = wrapperBuilder.apply((Integer) lastId);
        wrapper.last("LIMIT 1");
        return !mapper.selectList(wrapper).isEmpty();
    }

    /**
     * 获取最后一页的最后一条记录的 ID
     */
    public Integer getLastId(List<?> page) {
        if (page == null || page.isEmpty()) {
            return null;
        }
        try {
            T lastItem = (T) page.get(page.size() - 1);
            java.lang.reflect.Method idMethod = lastItem.getClass().getMethod("getId");
            Object id = idMethod.invoke(lastItem);
            return (Integer) id;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get last ID from page", e);
        }
    }
}