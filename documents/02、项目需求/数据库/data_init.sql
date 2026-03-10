-- 切换到目标数据库
USE `zo_psi`;

-- 设置编码
SET NAMES utf8mb4;

-- 禁用外键检查，以便插入数据
SET foreign_key_checks = 0;

--
-- 插入数据到 `frame` (组织机构)
--
INSERT INTO `frame` (`id`, `pid`, `name`, `sort`, `data`) VALUES
('f001', NULL, '总部', 0, '集团总部'),
('f002', 'f001', '华东分部', 1, '负责华东地区业务');

--
-- 插入数据到 `role` (用户角色)
--
INSERT INTO `role` (`id`, `name`, `data`, `root`, `auth`) VALUES
('r001', '超级管理员', '拥有所有权限', '{}', '{}'),
('r002', '财务人员', '负责财务审核', '{}', '{}');

--
-- 插入数据到 `user` (用户)
-- 密码 '123456' 的 MD5 是 'e10adc3949ba59abbe56e057f20f883e'
--
INSERT INTO `user` (`id`, `name`, `py`, `tel`, `frame`, `role`, `user`, `pwd`, `img`, `token`, `expire`, `data`, `more`) VALUES
('u001', '张三', 'zhangsan', '13800138000', 'f001', 'r001', 'admin', 'e10adc3949ba59abbe56e057f20f883e', NULL, 'token_admin_123', 2147483647, '系统管理员', '{}'),
('u002', '李四', 'lisi', '13900139000', 'f002', 'r002', 'caiwu', 'e10adc3949ba59abbe56e057f20f883e', NULL, 'token_caiwu_456', 2147483647, '财务部员工', '{}');

--
-- 插入数据到 `account` (资金账户)
--
INSERT INTO `account` (`id`, `name`, `number`, `frame`, `time`, `initial`, `balance`, `data`) VALUES
('acc001', '招商银行基本户', '1234567890', 'f001', '2025-01-01', 1000000.0000, 1000000.0000, '公司主账户'),
('acc002', '支付宝', 'pay@example.com', 'f001', '2025-01-01', 50000.0000, 50000.0000, '电商收入');

--
-- 插入数据到 `account_info` (资金详情)
--
INSERT INTO `account_info` (`id`, `pid`, `type`, `class`, `time`, `direction`, `money`) VALUES
('acc_info_001', 'acc001', 'IMY', 'imy001', NOW(), 1, 5000.0000),
('acc_info_002', 'acc001', 'OMY', 'omy001', NOW(), 0, -2000.0000);

--
-- 插入数据到 `people` (人员管理)
--
INSERT INTO `people` (`id`, `name`, `py`, `number`, `frame`, `sex`, `tel`, `add`, `card`, `data`, `more`) VALUES
('p001', '王五', 'wangwu', 'YG001', 'f001', 1, '13500135000', '总部A栋', '110110199001011234', '采购经理', '{}'),
('p002', '赵六', 'zhaoliu', 'YG002', 'f002', 0, '13600136000', '华东B栋', '120120199202022345', '销售代表', '{}');

--
-- 插入数据到 `allot` (转账单)
--
INSERT INTO `allot` (`id`, `frame`, `time`, `number`, `total`, `people`, `file`, `data`, `more`, `examine`, `user`) VALUES
('allot001', 'f001', NOW(), 'ZZD001', 5000.0000, 'p001', '[]', '内部转账', '{}', 1, 'u001'),
('allot002', 'f002', NOW(), 'ZZD002', 1000.0000, 'p002', '[]', '备用金', '{}', 0, 'u002');

--
-- 插入数据到 `allot_info` (转账单详情)
--
INSERT INTO `allot_info` (`id`, `pid`, `account`, `tat`, `money`, `settle`, `data`) VALUES
('allot_info_001', 'allot001', 'acc001', 'acc002', 5000.0000, 'SETTLE001', 'A转B'),
('allot_info_002', 'allot002', 'acc002', 'acc001', 1000.0000, 'SETTLE002', 'B转A');

--
-- 插入数据到 `category` (商品类别)
--
INSERT INTO `category` (`id`, `pid`, `name`, `sort`, `data`) VALUES
('cat001', NULL, '电子产品', 0, '消费电子'),
('cat002', 'cat001', '手机', 1, '智能手机');

--
-- 插入数据到 `goods` (商品)
--
INSERT INTO `goods` (`id`, `name`, `py`, `number`, `spec`, `category`, `brand`, `unit`, `buy`, `sell`, `code`, `location`, `stock`, `type`, `data`, `imgs`, `details`, `units`, `strategy`, `serial`, `batch`, `validity`, `protect`, `threshold`, `more`) VALUES
('g001', '智能手机X', 'zhinengshoujix', 'SP001', 'X-1000', 'cat002', '品牌A', '台', 1500.0000, 1999.0000, '1234567890123', 'A01', 20.0000, 0, '旗舰机型', '[]', '详情', '[]', '{}', 1, 1, 1, 365, 30, '{}'),
('g002', '蓝牙耳机', 'lanyaerji', 'SP002', 'TWS-8', 'cat001', '品牌B', '个', 200.0000, 399.0000, '9876543210987', 'A02', 50.0000, 0, '降噪耳机', '[]', '详情', '[]', '{}', 0, 0, 0, 0, 0, '{}');

--
-- 插入数据到 `attr` (辅助属性[商品])
--
INSERT INTO `attr` (`id`, `pid`, `name`, `buy`, `sell`, `code`) VALUES
('g_attr_001', 'g001', '红色,128G', 1500.0000, 1999.0000, '1234567890123-R'),
('g_attr_002', 'g001', '黑色,256G', 1700.0000, 2199.0000, '1234567890123-B');

--
-- 插入数据到 `attribute` (辅助属性[基础])
--
INSERT INTO `attribute` (`id`, `name`, `sort`, `data`) VALUES
('attr_base_001', '颜色', 1, '商品颜色'),
('attr_base_002', '容量', 2, '存储容量');

--
-- 插入数据到 `attribute_info` (属性详情表)
--
INSERT INTO `attribute_info` (`id`, `pid`, `name`) VALUES
('attr_info_001', 'attr_base_001', '红色'),
('attr_info_002', 'attr_base_001', '黑色');

--
-- 插入数据到 `warehouse` (仓库)
--
INSERT INTO `warehouse` (`id`, `name`, `number`, `frame`, `contacts`, `tel`, `add`, `data`) VALUES
('w001', '主仓库', 'CK001', 'f001', '仓管A', '13100131000', '总部A区', '主仓库'),
('w002', '华东仓', 'CK002', 'f002', '仓管B', '13200132000', '华东B区', '分仓');

--
-- 插入数据到 `room` (仓储信息)
--
INSERT INTO `room` (`id`, `warehouse`, `goods`, `attr`, `nums`) VALUES
('room001', 'w001', 'g001', '红色,128G', 50.0000),
('room002', 'w001', 'g002', NULL, 100.0000);

--
-- 插入数据到 `batch` (批次号)
--
INSERT INTO `batch` (`id`, `room`, `warehouse`, `goods`, `number`, `time`, `nums`) VALUES
('batch001', 'room001', 'w001', 'g001', 'PC20251001', '2025-10-01', 50.0000),
('batch002', 'room002', 'w001', 'g002', 'PC20251002', '2025-10-02', 100.0000);

--
-- 插入数据到 `batch_info` (批次号详情)
--
INSERT INTO `batch_info` (`id`, `pid`, `type`, `class`, `info`, `direction`, `nums`) VALUES
('batch_info_001', 'batch001', 'BUY', 'buy001', 'buy_info_001', 1, 50.0000),
('batch_info_002', 'batch002', 'BUY', 'buy001', 'buy_info_002', 1, 100.0000);

--
-- 插入数据到 `supplier` (供应商)
--
INSERT INTO `supplier` (`id`, `name`, `py`, `number`, `frame`, `user`, `category`, `rate`, `bank`, `account`, `tax`, `data`, `contacts`, `balance`, `more`) VALUES
('supp001', '供应商A', 'gongyingshanga', 'GYS001', 'f001', 'u001', '电子', 13.00, '工商银行', '1111111111', 'TAX111', '手机供应商', '[]', 0.0000, '{}'),
('supp002', '供应商B', 'gongyingshangb', 'f001', 'u001', '配件', 6.00, '建设银行', '2222222222', 'TAX222', '耳机供应商', '[]', 0.0000, '{}');

--
-- 插入数据到 `bill` (核销单)
--
INSERT INTO `bill` (`id`, `frame`, `customer`, `supplier`, `time`, `number`, `type`, `pmy`, `smp`, `people`, `file`, `data`, `more`, `examine`, `user`) VALUES
('bill001', 'f001', 'cust001', 'supp001', NOW(), 'HXD001', 2, 100.0000, 100.0000, 'p002', '[]', '应收冲应付', '{}', 1, 'u002'),
('bill002', 'f001', 'cust001', 'supp001', NOW(), 'HXD002', 0, 50.0000, 50.0000, 'p002', '[]', '预收冲应收', '{}', 0, 'u002');

--
-- 插入数据到 `bill_info` (核销单详情)
--
INSERT INTO `bill_info` (`id`, `pid`, `source`, `bill`, `mold`, `money`) VALUES
('bill_info_001', 'bill001', 'sell001', 'SELL', 'SELL', 100.0000),
('bill_info_002', 'bill001', 'buy001', 'BUY', 'BUY', 100.0000);

--
-- 插入数据到 `bor` (采购订单)
--
INSERT INTO `bor` (`id`, `source`, `frame`, `supplier`, `time`, `number`, `total`, `actual`, `people`, `arrival`, `logistics`, `file`, `data`, `more`, `examine`, `state`, `user`) VALUES
('bor001', NULL, 'f001', 'supp001', NOW(), 'CGDD001', 1999.0000, 1999.0000, 'p001', '2025-11-10 00:00:00', '{}', '[]', '采购手机', '{}', 1, 0, 'u001'),
('bor002', NULL, 'f001', 'supp002', NOW(), 'CGDD002', 399.0000, 399.0000, 'p001', '2025-11-11 00:00:00', '{}', '[]', '采购耳机', '{}', 0, 0, 'u001');

--
-- 插入数据到 `bor_info` (采购订单详情)
--
INSERT INTO `bor_info` (`id`, `pid`, `goods`, `attr`, `unit`, `warehouse`, `price`, `nums`, `discount`, `dsc`, `total`, `tax`, `tat`, `tpt`, `data`, `handle`) VALUES
('bor_info_001', 'bor001', 'g001', '红色,128G', '台', 'w001', 1999.0000, 1.0000, 0.00, 0.0000, 1999.0000, 13.00, 259.8700, 2258.8700, '备注', 0.0000),
('bor_info_002', 'bor002', 'g002', NULL, '个', 'w001', 399.0000, 1.0000, 0.00, 0.0000, 399.0000, 6.00, 23.9400, 422.9400, '备注', 0.0000);

--
-- 插入数据到 `buy` (采购单)
--
INSERT INTO `buy` (`id`, `source`, `frame`, `supplier`, `time`, `number`, `total`, `actual`, `money`, `cost`, `account`, `people`, `logistics`, `file`, `data`, `more`, `examine`, `nucleus`, `cse`, `invoice`, `check`, `user`) VALUES
('buy001', 'bor001', 'f001', 'supp001', NOW(), 'CGD001', 1999.0000, 1999.0000, 0.0000, 0.0000, 'acc001', 'p001', '{}', '[]', '采购入库', '{}', 1, 0, 3, 0, 0, 'u001'),
('buy002', 'bor002', 'f001', 'supp002', NOW(), 'CGD002', 399.0000, 399.0000, 0.0000, 0.0000, 'acc001', 'p001', '{}', '[]', '采购入库', '{}', 1, 0, 3, 0, 0, 'u001');

--
-- 插入数据到 `buy_info` (采购单详情)
--
INSERT INTO `buy_info` (`id`, `pid`, `source`, `goods`, `attr`, `unit`, `warehouse`, `batch`, `mfd`, `price`, `nums`, `serial`, `discount`, `dsc`, `total`, `tax`, `tat`, `tpt`, `data`, `retreat`) VALUES
('buy_info_001', 'buy001', 'bor_info_001', 'g001', '红色,128G', '台', 'w001', 'batch001', '2025-10-01', 1999.0000, 1.0000, '["SN001"]', 0.00, 0.0000, 1999.0000, 13.00, 259.8700, 2258.8700, '入库', 0.0000),
('buy_info_002', 'buy002', 'bor_info_002', 'g002', NULL, '个', 'w001', 'batch002', '2025-10-02', 399.0000, 1.0000, '[]', 0.00, 0.0000, 399.0000, 6.00, 23.9400, 422.9400, '入库', 0.0000);

--
-- 插入数据到 `bre` (采购退货单)
--
INSERT INTO `bre` (`id`, `source`, `frame`, `supplier`, `time`, `number`, `total`, `actual`, `money`, `cost`, `account`, `people`, `logistics`, `file`, `data`, `more`, `examine`, `nucleus`, `cse`, `invoice`, `check`, `user`) VALUES
('bre001', 'buy001', 'f001', 'supp001', NOW(), 'CGT001', 1999.0000, 1999.0000, 0.0000, 0.0000, 'acc001', 'p001', '{}', '[]', '质量问题退货', '{}', 1, 0, 3, 0, 0, 'u001'),
('bre002', NULL, 'f001', 'supp002', NOW(), 'CGT002', 399.0000, 399.0000, 0.0000, 0.0000, 'acc001', 'p001', '{}', '[]', '发错货退货', '{}', 0, 0, 3, 0, 0, 'u001');

--
-- 插入数据到 `bre_bill` (采购退货单核销详情)
--
INSERT INTO `bre_bill` (`id`, `pid`, `type`, `source`, `time`, `money`) VALUES
('bre_bill_001', 'bre001', 'BILL', 'bill001', NOW(), 1999.0000),
('bre_bill_002', 'bre002', 'OMY', 'omy001', NOW(), 399.0000);

--
-- 插入数据到 `bre_info` (采购退货单详情)
--
INSERT INTO `bre_info` (`id`, `pid`, `source`, `goods`, `attr`, `unit`, `warehouse`, `batch`, `mfd`, `price`, `nums`, `serial`, `discount`, `dsc`, `total`, `tax`, `tat`, `tpt`, `data`) VALUES
('bre_info_001', 'bre001', 'buy_info_001', 'g001', '红色,128G', '台', 'w001', 'batch001', '2025-10-01', 1999.0000, 1.0000, '["SN001"]', 0.00, 0.0000, 1999.0000, 13.00, 259.8700, 2258.8700, '退货'),
('bre_info_002', 'bre002', NULL, 'g002', NULL, '个', 'w001', 'batch002', '2025-10-02', 399.0000, 1.0000, '[]', 0.00, 0.0000, 399.0000, 6.00, 23.9400, 422.9400, '退货');

--
-- 插入数据到 `buy_bill` (采购单核销详情)
--
INSERT INTO `buy_bill` (`id`, `pid`, `type`, `source`, `time`, `money`) VALUES
('buy_bill_001', 'buy001', 'OMY', 'omy001', NOW(), 1999.0000),
('buy_bill_002', 'buy002', 'BILL', 'bill001', NOW(), 399.0000);

--
-- 插入数据到 `code` (条码)
--
INSERT INTO `code` (`id`, `name`, `info`, `type`, `data`) VALUES
('code001', '商品A条码', '1234567890123', 0, 'EAN13'),
('code002', '官网二维码', 'https://www.example.com', 1, '公司网址');

--
-- 插入数据到 `iet` (收支类别)
--
INSERT INTO `iet` (`id`, `name`, `type`, `sort`, `data`) VALUES
('iet001', '销售收入', 0, 1, '主营业务收入'),
('iet002', '运费', 1, 10, '物流支出');

--
-- 插入数据到 `cost` (单据费用)
--
INSERT INTO `cost` (`id`, `type`, `class`, `time`, `iet`, `money`, `data`, `settle`, `state`) VALUES
('cost001', 'SELL', 'sell001', NOW(), 'iet002', 50.0000, '销售运费', 0.0000, 0),
('cost002', 'BUY', 'buy001', NOW(), 'iet002', 30.0000, '采购运费', 0.0000, 0);

--
-- 插入数据到 `oce` (其它支出单)
--
INSERT INTO `oce` (`id`, `frame`, `supplier`, `time`, `number`, `total`, `actual`, `money`, `account`, `people`, `file`, `data`, `more`, `examine`, `nucleus`, `user`) VALUES
('oce001', 'f001', 'supp001', NOW(), 'QTZC001', 50.0000, 50.0000, 0.0000, 'acc002', 'p001', '[]', '支付运费', '{}', 1, 0, 'u002'),
('oce002', 'f001', 'supp002', NOW(), 'QTZC002', 100.0000, 100.0000, 0.0000, 'acc002', 'p001', '[]', '办公用品', '{}', 0, 0, 'u002');

--
-- 插入数据到 `cost_info` (单据费用详情)
--
INSERT INTO `cost_info` (`id`, `pid`, `oce`, `time`, `money`) VALUES
('cost_info_001', 'cost001', 'oce001', NOW(), 50.0000),
('cost_info_002', 'cost002', 'oce001', NOW(), 30.0000);

--
-- 插入数据到 `customer` (客户)
--
INSERT INTO `customer` (`id`, `name`, `py`, `number`, `frame`, `user`, `category`, `grade`, `bank`, `account`, `tax`, `data`, `contacts`, `balance`, `more`) VALUES
('cust001', '客户甲', 'kehujia', 'KH001', 'f001', 'u002', '零售', 'A', '建设银行', '8888888888', 'TAX888', '零售客户', '[]', 0.0000, '{}'),
('cust002', '客户乙', 'kehuyi', 'KH002', 'f002', 'u002', '批发', 'B', '中国银行', '9999999999', 'TAX999', '批发客户', '[]', 0.0000, '{}');

--
-- 插入数据到 `entry` (其它入库单)
--
INSERT INTO `entry` (`id`, `supplier`, `frame`, `time`, `number`, `type`, `total`, `cost`, `people`, `logistics`, `file`, `data`, `more`, `examine`, `cse`, `check`, `user`) VALUES
('entry001', 'supp001', 'f001', NOW(), 'QTRK001', 0, 100.0000, 0.0000, 'p001', '{}', '[]', '赠品入库', '{}', 1, 3, 0, 'u001'),
('entry002', 'supp001', 'f001', NOW(), 'PYD001', 1, 200.0000, 0.0000, 'p001', '{}', '[]', '盘盈入库', '{}', 0, 3, 0, 'u001');

--
-- 插入数据到 `entry_info` (其它入库单详情)
--
INSERT INTO `entry_info` (`id`, `pid`, `goods`, `attr`, `unit`, `warehouse`, `batch`, `mfd`, `price`, `nums`, `serial`, `total`, `data`) VALUES
('entry_info_001', 'entry001', 'g002', NULL, '个', 'w001', 'batch002', '2025-10-02', 100.0000, 1.0000, '[]', 100.0000, '赠品'),
('entry_info_002', 'entry002', 'g002', NULL, '个', 'w001', 'batch002', '2025-10-02', 100.0000, 2.0000, '[]', 200.0000, '盘盈');

--
-- 插入数据到 `extry` (其它出库单)
--
INSERT INTO `extry` (`id`, `customer`, `frame`, `time`, `number`, `type`, `total`, `cost`, `people`, `logistics`, `file`, `data`, `more`, `examine`, `cse`, `check`, `user`) VALUES
('extry001', 'cust001', 'f001', NOW(), 'QTCK001', 0, 100.0000, 0.0000, 'p002', '{}', '[]', '样品出库', '{}', 1, 3, 0, 'u001'),
('extry002', 'cust001', 'f001', NOW(), 'PKD001', 1, 200.0000, 0.0000, 'p002', '{}', '[]', '盘亏出库', '{}', 0, 3, 0, 'u001');

--
-- 插入数据到 `extry_info` (其它出库单详情)
--
INSERT INTO `extry_info` (`id`, `pid`, `goods`, `attr`, `unit`, `warehouse`, `batch`, `mfd`, `price`, `nums`, `serial`, `total`, `data`) VALUES
('extry_info_001', 'extry001', 'g002', NULL, '个', 'w001', 'batch002', '2025-10-02', 100.0000, 1.0000, '[]', 100.0000, '样品'),
('extry_info_002', 'extry002', 'g002', NULL, '个', 'w001', 'batch002', '2025-10-02', 100.0000, 2.0000, '[]', 200.0000, '盘亏');

--
-- 插入数据到 `field` (表单字段)
--
INSERT INTO `field` (`id`, `name`, `key`, `data`, `fields`) VALUES
('field001', '客户扩展字段', 'customer_extra', '客户自定义信息', '[{"key":"level", "name":"客户级别"}]'),
('field002', '商品扩展字段', 'goods_extra', '商品自定义信息', '[{"key":"material", "name":"材质"}]');

--
-- 插入数据到 `fifo` (先进先出记录)
-- 假设 'summary_out_001' 是一条出库, 'summary_in_001' 是一条入库
--
INSERT INTO `fifo` (`id`, `out`, `in`, `handle`) VALUES
('fifo001', 'summary_out_001', 'summary_in_001', 10.0000),
('fifo002', 'summary_out_002', 'summary_in_001', 5.0000);

--
-- 插入数据到 `ice` (其它收入单)
--
INSERT INTO `ice` (`id`, `frame`, `customer`, `time`, `number`, `total`, `actual`, `money`, `account`, `people`, `file`, `data`, `more`, `examine`, `nucleus`, `user`) VALUES
('ice001', 'f001', 'cust001', NOW(), 'QTSR001', 300.0000, 300.0000, 0.0000, 'acc001', 'p002', '[]', '咨询费', '{}', 1, 0, 'u002'),
('ice002', 'f001', 'cust002', NOW(), 'QTSR002', 500.0000, 500.0000, 0.0000, 'acc002', 'p002', '[]', '服务费', '{}', 0, 0, 'u002');

--
-- 插入数据到 `ice_bill` (其它收入单核销详情)
--
INSERT INTO `ice_bill` (`id`, `pid`, `type`, `source`, `time`, `money`) VALUES
('ice_bill_001', 'ice001', 'IMY', 'imy001', NOW(), 300.0000),
('ice_bill_002', 'ice002', 'IMY', 'imy002', NOW(), 500.0000);

--
-- 插入数据到 `ice_info` (其它收入单详情)
--
INSERT INTO `ice_info` (`id`, `pid`, `iet`, `money`, `data`) VALUES
('ice_info_001', 'ice001', 'iet001', 300.0000, '咨询'),
('ice_info_002', 'ice002', 'iet001', 500.0000, '服务');

--
-- 插入数据到 `imy` (收款单)
--
INSERT INTO `imy` (`id`, `frame`, `customer`, `time`, `number`, `total`, `people`, `file`, `data`, `more`, `examine`, `nucleus`, `user`) VALUES
('imy001', 'f001', 'cust001', NOW(), 'SKD001', 1999.0000, 'p002', '[]', '收销售款', '{}', 1, 0, 'u002'),
('imy002', 'f002', 'cust002', NOW(), 'SKD002', 399.0000, 'p002', '[]', '收销售款', '{}', 0, 0, 'u002');

--
-- 插入数据到 `imy_bill` (收款单核销详情)
--
INSERT INTO `imy_bill` (`id`, `pid`, `type`, `source`, `time`, `money`) VALUES
('imy_bill_001', 'imy001', 'SELL', 'sell001', NOW(), 1999.0000),
('imy_bill_002', 'imy002', 'SELL', 'sell002', NOW(), 399.0000);

--
-- 插入数据到 `imy_info` (收款单详情)
--
INSERT INTO `imy_info` (`id`, `pid`, `account`, `money`, `settle`, `data`) VALUES
('imy_info_001', 'imy001', 'acc001', 1999.0000, 'SETTLE_SK_001', '银行转账'),
('imy_info_002', 'imy002', 'acc002', 399.0000, 'SETTLE_SK_002', '支付宝');

--
-- 插入数据到 `invoice` (发票详情)
--
INSERT INTO `invoice` (`id`, `type`, `class`, `time`, `number`, `title`, `money`, `file`, `data`) VALUES
('invoice001', 'SELL', 'sell001', NOW(), 'FP123456', '客户甲', 2258.8700, '[]', '销售发票'),
('invoice002', 'BUY', 'buy001', NOW(), 'FP654321', '我方公司', 2258.8700, '[]', '采购发票');

--
-- 插入数据到 `log` (操作日志)
--
INSERT INTO `log` (`id`, `time`, `user`, `info`) VALUES
('log001', NOW(), 'u001', '用户 [admin] 登录系统'),
('log002', NOW(), 'u002', '用户 [caiwu] 审核了采购单 [CGD001]');

--
-- 插入数据到 `menu` (菜单信息)
--
INSERT INTO `menu` (`id`, `pid`, `name`, `key`, `model`, `type`, `resource`, `sort`, `ico`, `root`, `data`) VALUES
('m001', NULL, '仪表盘', 'dashboard', 0, 0, '/dashboard', 0, 'el-icon-home', 'DASHBOARD', '系统首页'),
('m002', NULL, '采购管理', 'purchase', 0, 0, '/purchase', 1, 'el-icon-shopping-cart', 'PURCHASE', '采购模块');

--
-- 插入数据到 `oce_bill` (其它支出单核销详情)
--
INSERT INTO `oce_bill` (`id`, `pid`, `type`, `source`, `time`, `money`) VALUES
('oce_bill_001', 'oce001', 'COST', 'cost001', NOW(), 50.0000),
('oce_bill_002', 'oce002', 'COST', 'cost002', NOW(), 30.0000);

--
-- 插入数据到 `oce_info` (其它支出单详情)
--
INSERT INTO `oce_info` (`id`, `pid`, `source`, `iet`, `money`, `data`) VALUES
('oce_info_001', 'oce001', 'cost001', 'iet002', 50.0000, '付运费'),
('oce_info_002', 'oce002', 'cost002', 'iet002', 30.0000, '付运费');

--
-- 插入数据到 `often` (常用功能)
--
INSERT INTO `often` (`id`, `user`, `name`, `key`) VALUES
('often001', 'u001', '新增销售单', 'SELL_ADD'),
('often002', 'u001', '新增采购单', 'BUY_ADD');

--
-- 插入数据到 `omy` (付款单)
--
INSERT INTO `omy` (`id`, `frame`, `supplier`, `time`, `number`, `total`, `people`, `file`, `data`, `more`, `examine`, `nucleus`, `user`) VALUES
('omy001', 'f001', 'supp001', NOW(), 'FKD001', 1999.0000, 'p001', '[]', '付采购款', '{}', 1, 0, 'u002'),
('omy002', 'f001', 'supp002', NOW(), 'FKD002', 399.0000, 'p001', '[]', '付采购款', '{}', 0, 0, 'u002');

--
-- 插入数据到 `omy_bill` (付款单核销详情)
--
INSERT INTO `omy_bill` (`id`, `pid`, `type`, `source`, `time`, `money`) VALUES
('omy_bill_001', 'omy001', 'BUY', 'buy001', NOW(), 1999.0000),
('omy_bill_002', 'omy002', 'BUY', 'buy002', NOW(), 399.0000);

--
-- 插入数据到 `omy_info` (付款单详情)
--
INSERT INTO `omy_info` (`id`, `pid`, `account`, `money`, `settle`, `data`) VALUES
('omy_info_001', 'omy001', 'acc001', 1999.0000, 'SETTLE_FK_001', '银行转账'),
('omy_info_002', 'omy002', 'acc001', 399.0000, 'SETTLE_FK_002', '银行转账');

--
-- 插入数据到 `period` (结账表)
--
INSERT INTO `period` (`id`, `date`, `time`, `user`) VALUES
('period001', '2025-09-30', '2025-10-01', 'u001'),
('period002', '2025-08-31', '2025-09-01', 'u001');

--
-- 插入数据到 `record` (单据记录)
--
INSERT INTO `record` (`id`, `type`, `source`, `time`, `user`, `info`) VALUES
('record001', 'BUY', 'buy001', NOW(), 'u001', '创建采购单'),
('record002', 'BUY', 'buy001', NOW(), 'u002', '审核采购单');

--
-- 插入数据到 `room_info` (仓储详情)
--
INSERT INTO `room_info` (`id`, `pid`, `type`, `class`, `info`, `time`, `direction`, `price`, `nums`) VALUES
('room_info_001', 'room001', 'BUY', 'buy001', 'buy_info_001', NOW(), 1, 1500.0000, 50.0000),
('room_info_002', 'room002', 'BUY', 'buy001', 'buy_info_002', NOW(), 1, 200.0000, 100.0000);

--
-- 插入数据到 `sell` (销售单)
--
INSERT INTO `sell` (`id`, `source`, `frame`, `customer`, `time`, `number`, `total`, `actual`, `money`, `cost`, `account`, `people`, `logistics`, `file`, `data`, `more`, `examine`, `nucleus`, `cse`, `invoice`, `check`, `user`) VALUES
('sell001', NULL, 'f001', 'cust001', NOW(), 'XSD001', 1999.0000, 1999.0000, 0.0000, 50.0000, 'acc001', 'p002', '{}', '[]', '销售手机', '{}', 1, 0, 0, 0, 0, 'u001'),
('sell002', NULL, 'f002', 'cust002', NOW(), 'XSD002', 399.0000, 399.0000, 0.0000, 0.0000, 'acc002', 'p002', '{}', '[]', '销售耳机', '{}', 0, 0, 3, 0, 0, 'u001');

--
-- 插入数据到 `sell_bill` (销售单核销详情)
--
INSERT INTO `sell_bill` (`id`, `pid`, `type`, `source`, `time`, `money`) VALUES
('sell_bill_001', 'sell001', 'IMY', 'imy001', NOW(), 1999.0000),
('sell_bill_002', 'sell002', 'IMY', 'imy002', NOW(), 399.0000);

--
-- 插入数据到 `sell_info` (销售单详情)
--
INSERT INTO `sell_info` (`id`, `pid`, `source`, `goods`, `attr`, `unit`, `warehouse`, `batch`, `mfd`, `price`, `nums`, `serial`, `discount`, `dsc`, `total`, `tax`, `tat`, `tpt`, `data`, `retreat`) VALUES
('sell_info_001', 'sell001', NULL, 'g001', '红色,128G', '台', 'w001', 'batch001', '2025-10-01', 1999.0000, 1.0000, '["SN001"]', 0.00, 0.0000, 1999.0000, 13.00, 259.8700, 2258.8700, '出库', 0.0000),
('sell_info_002', 'sell002', NULL, 'g002', NULL, '个', 'w001', 'batch002', '2025-10-02', 399.0000, 1.0000, '[]', 0.00, 0.0000, 399.0000, 6.00, 23.9400, 422.9400, '出库', 0.0000);

--
-- 插入数据到 `serial` (序列号)
--
INSERT INTO `serial` (`id`, `room`, `warehouse`, `batch`, `goods`, `number`, `state`) VALUES
('sn001', 'room001', 'w001', 'batch001', 'g001', 'SN001', 1),
('sn002', 'room001', 'w001', 'batch001', 'g001', 'SN002', 0);

--
-- 插入数据到 `serial_info` (序列号详情)
--
INSERT INTO `serial_info` (`id`, `pid`, `type`, `class`, `info`) VALUES
('sn_info_001', 'sn001', 'BUY', 'buy001', 'buy_info_001'),
('sn_info_002', 'sn001', 'SELL', 'sell001', 'sell_info_001');

--
-- 插入数据到 `serve` (服务信息)
--
INSERT INTO `serve` (`id`, `goods`, `attr`, `nums`) VALUES
('serve001', 'g001', '红色,128G', 1.0000),
('serve002', 'g002', NULL, 1.0000);

--
-- 插入数据到 `serve_info` (服务详情)
--
INSERT INTO `serve_info` (`id`, `pid`, `type`, `class`, `info`, `time`, `price`, `nums`) VALUES
('serve_info_001', 'serve001', 'SELL', 'sell001', 'sell_info_001', NOW(), 1999.0000, 1.0000),
('serve_info_002', 'serve002', 'SELL', 'sell002', 'sell_info_002', NOW(), 399.0000, 1.0000);

--
-- 插入数据到 `sor` (销售订单)
--
INSERT INTO `sor` (`id`, `frame`, `customer`, `time`, `number`, `total`, `actual`, `people`, `arrival`, `logistics`, `file`, `data`, `more`, `examine`, `state`, `user`) VALUES
('sor001', 'f001', 'cust001', NOW(), 'XSDD001', 1999.0000, 1999.0000, 'p002', '2025-11-10', '{}', '[]', '销售手机', '{}', 1, 0, 'u001'),
('sor002', 'f002', 'cust002', NOW(), 'XSDD002', 399.0000, 399.0000, 'p002', '2025-11-11', '{}', '[]', '销售耳机', '{}', 0, 0, 'u001');

--
-- 插入数据到 `sor_info` (销售订单详情)
--
INSERT INTO `sor_info` (`id`, `pid`, `goods`, `attr`, `unit`, `warehouse`, `price`, `nums`, `discount`, `dsc`, `total`, `tax`, `tat`, `tpt`, `data`, `handle`) VALUES
('sor_info_001', 'sor001', 'g001', '红色,128G', '台', 'w001', 1999.0000, 1.0000, 0.00, 0.0000, 1999.0000, 13.00, 259.8700, 2258.8700, '备注', 0.0000),
('sor_info_002', 'sor002', 'g002', NULL, '个', 'w001', 399.0000, 1.0000, 0.00, 0.0000, 399.0000, 6.00, 23.9400, 422.9400, '备注', 0.0000);

--
-- 插入数据到 `sre` (销售退货单)
--
INSERT INTO `sre` (`id`, `source`, `frame`, `customer`, `time`, `number`, `total`, `actual`, `money`, `cost`, `account`, `people`, `logistics`, `file`, `data`, `more`, `examine`, `nucleus`, `cse`, `invoice`, `check`, `user`) VALUES
('sre001', 'sell001', 'f001', 'cust001', NOW(), 'XST001', 1999.0000, 1999.0000, 0.0000, 0.0000, 'acc001', 'p002', '{}', '[]', '客户退货', '{}', 1, 0, 3, 0, 0, 'u001'),
('sre002', NULL, 'f002', 'cust002', NOW(), 'XST002', 399.0000, 399.0000, 0.0000, 0.0000, 'acc002', 'p002', '{}', '[]', '客户退货', '{}', 0, 0, 3, 0, 0, 'u001');

--
-- 插入数据到 `sre_bill` (销售退货单核销详情)
--
INSERT INTO `sre_bill` (`id`, `pid`, `type`, `source`, `time`, `money`) VALUES
('sre_bill_001', 'sre001', 'OMY', 'omy001', NOW(), 1999.0000),
('sre_bill_002', 'sre002', 'OMY', 'omy002', NOW(), 399.0000);

--
-- 插入数据到 `sre_info` (销售退货单详情)
--
INSERT INTO `sre_info` (`id`, `pid`, `source`, `goods`, `attr`, `unit`, `warehouse`, `batch`, `mfd`, `price`, `nums`, `serial`, `discount`, `dsc`, `total`, `tax`, `tat`, `tpt`, `data`) VALUES
('sre_info_001', 'sre001', 'sell_info_001', 'g001', '红色,128G', '台', 'w001', 'batch001', '2025-10-01', 1999.0000, 1.0000, '["SN001"]', 0.00, 0.0000, 1999.0000, 13.00, 259.8700, 2258.8700, '退货入库'),
('sre_info_002', 'sre002', NULL, 'g002', NULL, '个', 'w001', 'batch002', '2025-10-02', 399.0000, 1.0000, '[]', 0.00, 0.0000, 399.0000, 6.00, 23.9400, 422.9400, '退货入库');

--
-- 插入数据到 `summary` (收发统计表)
--
INSERT INTO `summary` (`id`, `pid`, `type`, `class`, `info`, `time`, `goods`, `attr`, `warehouse`, `batch`, `mfd`, `serial`, `direction`, `price`, `nums`, `uct`, `bct`, `exist`, `balance`, `handle`) VALUES
('summary001', 'room_info_001', 'BUY', 'buy001', 'buy_info_001', NOW(), 'g001', '红色,128G', 'w001', 'batch001', '2025-10-01', '["SN001"]', 1, 1500.0000, 1.0000, 1500.0000, 1500.0000, '[]', '[]', 0.0000),
('summary002', 'room_info_002', 'BUY', 'buy001', 'buy_info_002', NOW(), 'g002', NULL, 'w001', 'batch002', '2025-10-02', '[]', 1, 200.0000, 1.0000, 200.0000, 200.0000, '[]', '[]', 0.0000);

--
-- 插入数据到 `swap` (调拨单)
--
INSERT INTO `swap` (`id`, `frame`, `time`, `number`, `total`, `cost`, `logistics`, `people`, `file`, `data`, `more`, `examine`, `cse`, `user`) VALUES
('swap001', 'f001', NOW(), 'DBD001', 1500.0000, 0.0000, '{}', 'p001', '[]', '仓库调拨', '{}', 1, 3, 'u001'),
('swap002', 'f001', NOW(), 'DBD002', 200.0000, 0.0000, '{}', 'p001', '[]', '仓库调拨', '{}', 0, 3, 'u001');

--
-- 插入数据到 `swap_info` (调拨单详情)
--
INSERT INTO `swap_info` (`id`, `pid`, `goods`, `attr`, `unit`, `warehouse`, `storehouse`, `batch`, `mfd`, `price`, `nums`, `serial`, `total`, `data`) VALUES
('swap_info_001', 'swap001', 'g001', '红色,128G', '台', 'w001', 'w002', 'batch001', '2025-10-01', 1500.0000, 1.0000, '["SN001"]', 1500.0000, '调拨'),
('swap_info_002', 'swap002', 'g002', NULL, '个', 'w001', 'w002', 'batch002', '2025-10-02', 200.0000, 1.0000, '[]', 200.0000, '调拨');

--
-- 插入数据到 `sys` (系统配置)
--
INSERT INTO `sys` (`id`, `name`, `info`, `data`) VALUES
('sys001', 'CompanyName', '我的公司', '公司名称'),
('sys002', 'InventoryStrategy', 'FIFO', '库存计价方式: FIFO/LIFO/Average');

--
-- 插入数据到 `tmpl_import` (导入模板)
--
INSERT INTO `tmpl_import` (`id`, `name`, `code`, `save_type`, `save_path`, `remark`, `status`) VALUES
('tmpl001', '商品导入模板', 'GOODS_IMPORT', 'local', '/templates/goods.xlsx', '用于批量导入商品', 1),
('tmpl002', '客户导入模板', 'CUSTOMER_IMPORT', 'local', '/templates/customer.xlsx', '用于批量导入客户', 1);

--
-- 插入数据到 `dict_type` (字典类型)
--
INSERT INTO `dict_type` (`id`, `name`, `code`, `remark`) VALUES
('dt001', '存储方式', 'SAVE_TYPE', '文件存储方式'),
('dt002', '文件类型', 'FILE_TYPE', '附件文件类型');

--
-- 插入数据到 `dict` (字典)
--
INSERT INTO `dict` (`id`, `tid`, `name`, `value`, `remark`) VALUES
('d001', 'dt001', '本地存储', 'local', '服务器本地'),
('d002', 'dt001', '阿里云OSS', 'oss', '阿里云对象存储');

--
-- 插入数据到 `annex` (附件)
--
INSERT INTO `annex` (`id`, `name`, `file_type`, `save_type`, `save_path`, `remark`, `status`) VALUES
('annex001', 'logo.png', 'image', 'local', '/uploads/logo.png', '公司Logo', 1),
('annex002', '合同模板.pdf', 'pdf', 'local', '/uploads/contract.pdf', '标准合同', 1);

-- 重新启用外键检查
SET foreign_key_checks = 1;
