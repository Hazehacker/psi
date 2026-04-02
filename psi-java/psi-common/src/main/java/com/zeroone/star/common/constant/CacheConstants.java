package com.zeroone.star.common.constant;

/**
 * 缓存 Key 常量定义
 */
public class CacheConstants {

    // ==================== psi-report 报表服务 ====================

    /**
     * 商品收发汇总报表缓存前缀
     * Key格式: report:stock:summary:{warehouseId}:{goodsId}:{page}:{size}
     */
    public static final String REPORT_STOCK_SUMMARY = "report:stock:summary";

    /**
     * 商品收发明细报表缓存前缀
     * Key格式: report:stock:detail:{warehouseId}:{goodsId}:{startDate}:{endDate}:{page}
     */
    public static final String REPORT_STOCK_DETAIL = "report:stock:detail";

    /**
     * 商品库存余额报表缓存前缀
     * Key格式: report:stock:balance:{warehouseId}:{goodsCategoryId}:{page}
     */
    public static final String REPORT_STOCK_BALANCE = "report:stock:balance";

    // ==================== psi-purchase 采购服务 ====================

    /**
     * 采购退货单列表缓存前缀
     * Key格式: purchase:bre:list:{queryHash}
     */
    public static final String PURCHASE_BRE_LIST = "purchase:bre:list";

    /**
     * 采购订单报表缓存前缀
     * Key格式: purchase:bor:report:{ids}
     */
    public static final String PURCHASE_BOR_REPORT = "purchase:bor:report";

    // ==================== 通用常量 ====================

    /**
     * 缓存默认过期时间（秒）- 5分钟
     */
    public static final long DEFAULT_TTL = 300L;

    /**
     * 缓存随机过期时间增量上限（秒）- 60秒
     * 用于防止缓存雪崩
     */
    public static final long RANDOM_TTL_MAX = 60L;

    /**
     * 互斥锁 Key 前缀
     */
    public static final String LOCK_PREFIX = "lock:";
}
