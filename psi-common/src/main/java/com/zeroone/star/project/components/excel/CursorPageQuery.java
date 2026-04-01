package com.zeroone.star.project.components.excel;

import java.util.List;

/**
 * 游标分页查询接口
 * 用于流式导出时按页查询数据，支持基于主键ID的深度分页优化
 *
 * @param <T> 查询结果数据类型
 */
public interface CursorPageQuery<T> {

    /**
     * 获取下一页数据
     * @param lastId 上一页最后一条记录的主键ID，null表示第一页
     * @param pageSize 每页数量
     * @return 下一页数据列表
     */
    List<T> nextPage(Object lastId, int pageSize);

    /**
     * 获取第一页数据（首次调用时使用）
     * @param pageSize 每页数量
     * @return 第一页数据列表
     */
    default List<T> firstPage(int pageSize) {
        return nextPage(null, pageSize);
    }

    /**
     * 是否还有更多数据
     * @param lastId 当前页最后一条记录的主键ID
     * @return 是否有更多数据
     */
    boolean hasMore(Object lastId);
}