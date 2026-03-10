-- Create DataBase
DROP DATABASE IF EXISTS `zo_psi`;
CREATE DATABASE `zo_psi`;

-- Change DataBase
USE `zo_psi`;

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;

CREATE TABLE `account` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '账户名称',
  `number` varchar(32) NOT NULL COMMENT '账户编号',
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `time` date NOT NULL COMMENT '余额日期',
  `initial` decimal(16,4) NOT NULL COMMENT '期初余额',
  `balance` decimal(16,4) DEFAULT '0.0000' COMMENT '账户余额',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金账户';

CREATE TABLE `account_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属账户',
  `type` varchar(32) NOT NULL COMMENT '单据类型',
  `class` varchar(32) NOT NULL COMMENT '所属类',
  `time` datetime NOT NULL COMMENT '单据时间',
  `direction` tinyint(1) NOT NULL COMMENT '方向[0:出|1:入]',
  `money` decimal(16,4) NOT NULL COMMENT '金额',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `class` (`class`),
  KEY `time` (`time`),
  KEY `direction` (`direction`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金详情';

CREATE TABLE `allot` (
  `id` varchar(32) NOT NULL,
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `total` decimal(16,4) NOT NULL COMMENT '单据金额',
  `people` varchar(32) COMMENT '关联人员',
  `file` text COMMENT '单据附件',
  `data` varchar(256) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) NOT NULL COMMENT '审核状态[0:未审核|1:已审核]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `time` (`time`),
  KEY `examine` (`examine`),
  KEY `people` (`people`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='转账单';

CREATE TABLE `allot_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `account` varchar(32) NOT NULL COMMENT '转出账户',
  `tat` varchar(32) NOT NULL COMMENT '转入账户',
  `money` decimal(12,4) NOT NULL COMMENT '结算金额',
  `settle` varchar(256) COMMENT '结算号',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `account` (`account`),
  KEY `tat` (`tat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='转账单详情';

CREATE TABLE `attr` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属商品',
  `name` varchar(64) NOT NULL COMMENT '属性名称',
  `buy` decimal(12,4) NOT NULL COMMENT '采购价格',
  `sell` decimal(12,4) NOT NULL COMMENT '销售价格',
  `code` varchar(64) COMMENT '条形码',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='辅助属性[商品]';

CREATE TABLE `attribute` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '属性名称',
  `sort` int(11) NOT NULL COMMENT '属性排序',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='辅助属性[基础]';

CREATE TABLE `attribute_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属属性',
  `name` varchar(32) NOT NULL COMMENT '属性名称',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='属性详情表';

CREATE TABLE `batch` (
  `id` varchar(32) NOT NULL,
  `room` varchar(32) NOT NULL COMMENT '所属仓储',
  `warehouse` varchar(32) NOT NULL COMMENT '所属仓库',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `number` varchar(64) NOT NULL COMMENT '批次号',
  `time` date NOT NULL COMMENT '生产日期',
  `nums` decimal(12,4) NOT NULL COMMENT '库存数量',
  PRIMARY KEY (`id`),
  KEY `room` (`room`),
  KEY `warehouse` (`warehouse`),
  KEY `goods` (`goods`),
  KEY `time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='批次号';

CREATE TABLE `batch_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属批次',
  `type` varchar(32) NOT NULL COMMENT '单据类型',
  `class` varchar(32) NOT NULL COMMENT '所属类',
  `info` varchar(32) NOT NULL COMMENT '所属详情',
  `direction` tinyint(1) NOT NULL COMMENT '方向[0:出|1:入]',
  `nums` decimal(12,4) NOT NULL COMMENT '出入数量',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `type` (`type`),
  KEY `direction` (`direction`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='批次号详情';

CREATE TABLE `bill` (
  `id` varchar(32) NOT NULL,
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `customer` varchar(32) NOT NULL COMMENT '客户',
  `supplier` varchar(32) NOT NULL COMMENT '供应商',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `type` tinyint(1) NOT NULL COMMENT '核销类型[0:预收冲应收|1:预付冲应付|2:应收冲应付|3:销退冲销售|4:购退冲采购]',
  `pmy` decimal(16,4) NOT NULL COMMENT '总核金额',
  `smp` decimal(16,4) NOT NULL COMMENT '总销金额',
  `people` varchar(32) COMMENT '关联人员',
  `file` text COMMENT '单据附件',
  `data` varchar(256) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) NOT NULL COMMENT '审核状态[0:未审核|1:已审核]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `customer` (`customer`),
  KEY `time` (`time`),
  KEY `type` (`type`),
  KEY `people` (`people`),
  KEY `examine` (`examine`),
  KEY `user` (`user`),
  KEY `supplier` (`supplier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='核销单';

CREATE TABLE `bill_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `source` varchar(32) NOT NULL COMMENT '关联单据',
  `bill` varchar(32) NOT NULL COMMENT '核销类型',
  `mold` varchar(32) NOT NULL COMMENT '单据类型',
  `money` decimal(12,4) NOT NULL COMMENT '核销金额',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `source` (`source`),
  KEY `mold` (`mold`),
  KEY `bill` (`bill`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='核销单详情';

CREATE TABLE `bor` (
  `id` varchar(32) NOT NULL,
  `source` varchar(32) COMMENT '关联单据|Sor',
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `supplier` varchar(32) NOT NULL COMMENT '供应商',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `total` decimal(16,4) NOT NULL COMMENT '单据金额',
  `actual` decimal(16,4) NOT NULL COMMENT '实际金额',
  `people` varchar(32) COMMENT '关联人员',
  `arrival` datetime COMMENT '到货时间',
  `logistics` text COMMENT '物流信息',
  `file` text COMMENT '单据附件',
  `data` varchar(256) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) NOT NULL COMMENT '审核状态[0:未审核|1:已审核]',
  `state` tinyint(1) NOT NULL COMMENT '入库状态[0:未入库|1:部分入库|2:已入库|3:关闭]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `supplier` (`supplier`),
  KEY `time` (`time`),
  KEY `examine` (`examine`),
  KEY `state` (`state`),
  KEY `source` (`source`),
  KEY `people` (`people`),
  KEY `arrival` (`arrival`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购订单';

CREATE TABLE `bor_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) COMMENT '辅助属性',
  `unit` varchar(32) NOT NULL COMMENT '单位',
  `warehouse` varchar(32) COMMENT '仓库',
  `price` decimal(12,4) NOT NULL COMMENT '单价',
  `nums` decimal(12,4) NOT NULL COMMENT '数量',
  `discount` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '折扣率',
  `dsc` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '折扣额',
  `total` decimal(12,4) NOT NULL COMMENT '金额',
  `tax` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '税率',
  `tat` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '税额',
  `tpt` decimal(12,4) NOT NULL COMMENT '价税合计',
  `data` varchar(256) COMMENT '备注信息',
  `handle` decimal(12,4) DEFAULT '0.0000' COMMENT '入库数量',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `goods` (`goods`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购订单详情';

CREATE TABLE `bre` (
  `id` varchar(32) NOT NULL,
  `source` varchar(32) COMMENT '关联单据|BUY',
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `supplier` varchar(32) NOT NULL COMMENT '供应商',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `total` decimal(16,4) NOT NULL COMMENT '单据金额',
  `actual` decimal(16,4) NOT NULL COMMENT '实际金额',
  `money` decimal(16,4) NOT NULL COMMENT '实收金额',
  `cost` decimal(16,4) NOT NULL DEFAULT '0.0000' COMMENT '单据费用',
  `account` varchar(32) COMMENT '结算账户',
  `people` varchar(32) COMMENT '关联人员',
  `logistics` text COMMENT '物流信息',
  `file` text COMMENT '单据附件',
  `data` varchar(256) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) NOT NULL COMMENT '审核状态[0:未审核|1:已审核]',
  `nucleus` tinyint(1) NOT NULL COMMENT '核销状态[0:未核销|1:部分核销|2:已核销]',
  `cse` tinyint(1) NOT NULL COMMENT '费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]',
  `invoice` tinyint(1) NOT NULL COMMENT '发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]',
  `check` tinyint(1) NOT NULL COMMENT '核对状态[0:未核对|1:已核对]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `supplier` (`supplier`),
  KEY `time` (`time`),
  KEY `examine` (`examine`),
  KEY `nucleus` (`nucleus`),
  KEY `source` (`source`),
  KEY `people` (`people`),
  KEY `cse` (`cse`),
  KEY `invoice` (`invoice`),
  KEY `check` (`check`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购退货单';

CREATE TABLE `bre_bill` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属单据',
  `type` varchar(32) NOT NULL COMMENT '核销类型',
  `source` varchar(32) NOT NULL COMMENT '关联单据',
  `time` datetime NOT NULL COMMENT '单据时间',
  `money` decimal(16,4) NOT NULL COMMENT '核销金额',
  PRIMARY KEY (`id`),
  KEY `pid_account_user` (`pid`),
  KEY `type` (`type`),
  KEY `source` (`source`),
  KEY `time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购退货单核销详情';

CREATE TABLE `bre_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `source` varchar(32) COMMENT '关联详情|BUY',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) COMMENT '辅助属性',
  `unit` varchar(32) NOT NULL COMMENT '单位',
  `warehouse` varchar(32) COMMENT '仓库',
  `batch` varchar(32) COMMENT '批次号',
  `mfd` date COMMENT '生产日期',
  `price` decimal(12,4) NOT NULL COMMENT '单价',
  `nums` decimal(12,4) NOT NULL COMMENT '数量',
  `serial` text COMMENT '序列号',
  `discount` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '折扣率',
  `dsc` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '折扣额',
  `total` decimal(12,4) NOT NULL COMMENT '金额',
  `tax` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '税率',
  `tat` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '税额',
  `tpt` decimal(12,4) NOT NULL COMMENT '价税合计',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `source` (`source`),
  KEY `goods` (`goods`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购退货单详情';

CREATE TABLE `buy` (
  `id` varchar(32) NOT NULL,
  `source` varchar(32) COMMENT '关联单据|BOR',
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `supplier` varchar(32) NOT NULL COMMENT '供应商',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `total` decimal(16,4) NOT NULL COMMENT '单据金额',
  `actual` decimal(16,4) NOT NULL COMMENT '实际金额',
  `money` decimal(16,4) NOT NULL COMMENT '实付金额',
  `cost` decimal(16,4) NOT NULL DEFAULT '0.0000' COMMENT '单据费用',
  `account` varchar(32) COMMENT '结算账户',
  `people` varchar(32) COMMENT '关联人员',
  `logistics` text COMMENT '物流信息',
  `file` text COMMENT '单据附件',
  `data` varchar(256) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) NOT NULL COMMENT '审核状态[0:未审核|1:已审核]',
  `nucleus` tinyint(1) NOT NULL COMMENT '核销状态[0:未核销|1:部分核销|2:已核销]',
  `cse` tinyint(1) NOT NULL COMMENT '费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]',
  `invoice` tinyint(1) NOT NULL COMMENT '发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]',
  `check` tinyint(1) NOT NULL COMMENT '核对状态[0:未核对|1:已核对]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `supplier` (`supplier`),
  KEY `time` (`time`),
  KEY `examine` (`examine`),
  KEY `nucleus` (`nucleus`),
  KEY `source` (`source`),
  KEY `people` (`people`),
  KEY `cse` (`cse`),
  KEY `invoice` (`invoice`),
  KEY `check` (`check`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购单';

CREATE TABLE `buy_bill` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属单据',
  `type` varchar(32) NOT NULL COMMENT '核销类型',
  `source` varchar(32) NOT NULL COMMENT '关联单据',
  `time` datetime NOT NULL COMMENT '单据时间',
  `money` decimal(16,4) NOT NULL COMMENT '核销金额',
  PRIMARY KEY (`id`),
  KEY `pid_account_user` (`pid`),
  KEY `type` (`type`),
  KEY `source` (`source`),
  KEY `time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购单核销详情';

CREATE TABLE `buy_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `source` varchar(32) COMMENT '关联详情|BOR',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) COMMENT '辅助属性',
  `unit` varchar(32) NOT NULL COMMENT '单位',
  `warehouse` varchar(32) COMMENT '仓库',
  `batch` varchar(32) COMMENT '批次号',
  `mfd` date COMMENT '生产日期',
  `price` decimal(12,4) NOT NULL COMMENT '单价',
  `nums` decimal(12,4) NOT NULL COMMENT '数量',
  `serial` text COMMENT '序列号',
  `discount` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '折扣率',
  `dsc` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '折扣额',
  `total` decimal(12,4) NOT NULL COMMENT '金额',
  `tax` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '税率',
  `tat` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '税额',
  `tpt` decimal(12,4) NOT NULL COMMENT '价税合计',
  `data` varchar(256) COMMENT '备注信息',
  `retreat` decimal(12,4) DEFAULT '0.0000' COMMENT '退货数量',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `source` (`source`),
  KEY `goods` (`goods`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购单详情';

CREATE TABLE `category` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) COMMENT '所属类别',
  `name` varchar(32) NOT NULL COMMENT '类别名称',
  `sort` int(11) DEFAULT '0' COMMENT '类别排序',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品类别';

CREATE TABLE `code` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '条码名称',
  `info` varchar(64) NOT NULL COMMENT '条码内容',
  `type` tinyint(1) NOT NULL COMMENT '条码类型[0:条形码 | 1:二维码]',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='条码';

CREATE TABLE `cost` (
  `id` varchar(32) NOT NULL,
  `type` varchar(32) NOT NULL COMMENT '单据类型',
  `class` varchar(32) NOT NULL COMMENT '所属类',
  `time` datetime NOT NULL COMMENT '单据时间',
  `iet` varchar(32) NOT NULL COMMENT '所属收支',
  `money` decimal(16,4) NOT NULL COMMENT '金额',
  `data` varchar(256) COMMENT '备注',
  `settle` decimal(16,4) DEFAULT '0.0000' COMMENT '结算金额',
  `state` tinyint(1) DEFAULT NULL COMMENT '结算状态[0:未结算|1:部分结算|2:已结算]',
  PRIMARY KEY (`id`),
  KEY `type` (`type`),
  KEY `class` (`class`),
  KEY `time` (`time`),
  KEY `iet` (`iet`),
  KEY `state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单据费用';

CREATE TABLE `cost_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `oce` varchar(32) NOT NULL COMMENT '所属支出',
  `time` datetime NOT NULL COMMENT '单据时间',
  `money` decimal(16,4) NOT NULL COMMENT '结算金额',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `oce` (`oce`),
  KEY `time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单据费用详情';

CREATE TABLE `customer` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '客户名称',
  `py` varchar(32) NOT NULL COMMENT '拼音信息',
  `number` varchar(32) NOT NULL COMMENT '客户编号',
  `frame` varchar(32) NOT NULL COMMENT '所属组织',
  `user` varchar(32) NOT NULL COMMENT '所属用户',
  `category` varchar(32) NOT NULL COMMENT '客户类别',
  `grade` varchar(32) NOT NULL COMMENT '客户等级',
  `bank` varchar(32) COMMENT '开户银行',
  `account` varchar(64) COMMENT '银行账号',
  `tax` varchar(64) COMMENT '纳税号码',
  `data` varchar(256) COMMENT '备注信息',
  `contacts` text COMMENT '联系资料',
  `balance` decimal(16,4) DEFAULT '0.0000' COMMENT '应收款余额',
  `more` text COMMENT '扩展信息',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `user` (`user`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户';

SET NAMES utf8mb4;

CREATE TABLE `entry` (
  `id` varchar(32) NOT NULL,
  `supplier` varchar(32) NOT NULL COMMENT '供应商',
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `type` tinyint(1) NOT NULL COMMENT '单据类型[0:其它入库单|1:盘盈单]',
  `total` decimal(16,4) NOT NULL COMMENT '单据成本',
  `cost` decimal(16,4) NOT NULL DEFAULT '0.0000' COMMENT '单据费用',
  `people` varchar(32) COMMENT '关联人员',
  `logistics` text COMMENT '物流信息',
  `file` text COMMENT '单据附件',
  `data` varchar(265) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) NOT NULL COMMENT '审核状态[0:未审核|1:已审核]',
  `cse` tinyint(1) NOT NULL COMMENT '费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]',
  `check` tinyint(1) NOT NULL COMMENT '核对状态[0:未核对|1:已核对]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `supplier` (`supplier`),
  KEY `frame` (`frame`),
  KEY `time` (`time`),
  KEY `type` (`type`),
  KEY `people` (`people`),
  KEY `examine` (`examine`),
  KEY `cse` (`cse`),
  KEY `check` (`check`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='其它入库单';

CREATE TABLE `entry_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `goods`varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) COMMENT '辅助属性',
  `unit` varchar(32) NOT NULL COMMENT '单位',
  `warehouse` varchar(32) COMMENT '仓库',
  `batch` varchar(32) COMMENT '批次号',
  `mfd` date COMMENT '生产日期',
  `price` decimal(12,4) NOT NULL COMMENT '成本',
  `nums` decimal(12,4) NOT NULL COMMENT '数量',
  `serial` text COMMENT '序列号',
  `total` decimal(12,4) NOT NULL COMMENT '总成本',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `goods` (`goods`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='其它入库单详情';

CREATE TABLE `extry` (
  `id` varchar(32) NOT NULL,
  `customer` varchar(32) NOT NULL COMMENT '客户',
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `type` tinyint(1) NOT NULL COMMENT '单据类型[0:其它出库单|1:盘亏单]',
  `total` decimal(16,4) NOT NULL COMMENT '单据成本',
  `cost` decimal(16,4) NOT NULL DEFAULT '0.0000' COMMENT '单据费用',
  `people` varchar(32) COMMENT '关联人员',
  `logistics` text COMMENT '物流信息',
  `file` text COMMENT '单据附件',
  `data` varchar(265) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) NOT NULL COMMENT '审核状态[0:未审核|1:已审核]',
  `cse` tinyint(1) NOT NULL COMMENT '费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]',
  `check` tinyint(1) NOT NULL COMMENT '核对状态[0:未核对|1:已核对]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `customer` (`customer`),
  KEY `frame` (`frame`),
  KEY `time` (`time`),
  KEY `type` (`type`),
  KEY `people` (`people`),
  KEY `examine` (`examine`),
  KEY `cse` (`cse`),
  KEY `check` (`check`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='其它出库单';

CREATE TABLE `extry_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) COMMENT '辅助属性',
  `unit` varchar(32) NOT NULL COMMENT '单位',
  `warehouse` varchar(32) COMMENT '仓库',
  `batch` varchar(32) COMMENT '批次号',
  `mfd` date COMMENT '生产日期',
  `price` decimal(12,4) NOT NULL COMMENT '成本',
  `nums` decimal(12,4) NOT NULL COMMENT '数量',
  `serial` text COMMENT '序列号',
  `total` decimal(12,4) NOT NULL COMMENT '总成本',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `goods` (`goods`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='其它出库单详情';

CREATE TABLE `field` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '模块名称',
  `key` varchar(32) NOT NULL COMMENT '模块标识',
  `data` varchar(256) COMMENT '备注信息',
  `fields` text NOT NULL COMMENT '字段数据',
  PRIMARY KEY (`id`),
  KEY `key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单字段';

CREATE TABLE `fifo` (
  `id` varchar(32) NOT NULL,
  `out` varchar(32) NOT NULL COMMENT '所属出',
  `in` varchar(32) NOT NULL COMMENT '所属进',
  `handle` decimal(12,4) NOT NULL COMMENT '处理数量',
  PRIMARY KEY (`id`),
  KEY `out` (`out`),
  KEY `in` (`in`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='先进先出记录';

CREATE TABLE `frame` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) COMMENT '所属ID',
  `name` varchar(32) NOT NULL COMMENT '组织名称',
  `sort` int(11) DEFAULT '0' COMMENT '组织排序',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `pid_sort` (`pid`,`sort`),
  KEY `sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织机构';

CREATE TABLE `goods` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '商品名称',
  `py` varchar(32) NOT NULL COMMENT '拼音信息',
  `number` varchar(32) NOT NULL COMMENT '商品编号',
  `spec` varchar(32) COMMENT '规格型号',
  `category` varchar(32) NOT NULL COMMENT '商品类别',
  `brand` varchar(32) COMMENT '商品品牌',
  `unit` varchar(32) NOT NULL COMMENT '商品单位[*:常规单位|-1:多单位]',
  `buy` decimal(12,4) NOT NULL COMMENT '采购价格',
  `sell` decimal(12,4) NOT NULL COMMENT '销售价格',
  `code` varchar(64) COMMENT '商品条码',
  `location` varchar(64) COMMENT '商品货位',
  `stock` decimal(12,4) NOT NULL COMMENT '库存阈值',
  `type` tinyint(1) NOT NULL COMMENT '产品类型[0:常规商品|1:服务商品]',
  `data` varchar(256) COMMENT '备注信息',
  `imgs` text COMMENT '商品图像',
  `details` text COMMENT '图文详情',
  `units` text  COMMENT '多单位配置',
  `strategy` text COMMENT '折扣策略',
  `serial` tinyint(1) DEFAULT '0' COMMENT '序列产品[0:关闭|1:启用]',
  `batch` tinyint(1) DEFAULT '0' COMMENT '批次产品[0:关闭|1:启用]',
  `validity` tinyint(1) DEFAULT '0' COMMENT '有效期[0:关闭|1:启用]',
  `protect` smallint(1) DEFAULT '0' COMMENT '保质期',
  `threshold` smallint(1) DEFAULT '0' COMMENT '预警阀值',
  `more` text NOT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`),
  KEY `number` (`number`),
  KEY `name_py` (`name`,`py`),
  KEY `category` (`category`),
  KEY `code` (`code`),
  KEY `type` (`type`),
  KEY `brand` (`brand`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品';

CREATE TABLE `ice` (
  `id` varchar(32) NOT NULL,
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `customer` varchar(32) COMMENT '客户',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `total` decimal(16,4) NOT NULL COMMENT '单据金额',
  `actual` decimal(16,4) NOT NULL COMMENT '实际金额',
  `money` decimal(16,4) NOT NULL COMMENT '实收金额',
  `account` varchar(32) NOT NULL COMMENT '结算账户',
  `people` varchar(32) COMMENT '关联人员',
  `file` text COMMENT '单据附件',
  `data` varchar(256) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) NOT NULL COMMENT '审核状态[0:未审核|1:已审核]',
  `nucleus` tinyint(1) NOT NULL COMMENT '核销状态[0:未核销|1:部分核销|2:已核销]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `time` (`time`),
  KEY `examine` (`examine`),
  KEY `people` (`people`),
  KEY `customer` (`customer`),
  KEY `account` (`account`),
  KEY `nucleus` (`nucleus`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='其它收入单';

CREATE TABLE `ice_bill` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属单据',
  `type` varchar(32) NOT NULL COMMENT '核销类型',
  `source` varchar(32) NOT NULL COMMENT '关联单据',
  `time` datetime NOT NULL COMMENT '单据时间',
  `money` decimal(16,4) NOT NULL COMMENT '核销金额',
  PRIMARY KEY (`id`),
  KEY `pid_account_user` (`pid`),
  KEY `type` (`type`),
  KEY `source` (`source`),
  KEY `time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='其它收入单核销详情';

CREATE TABLE `ice_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `iet` varchar(32) NOT NULL COMMENT '收支类型',
  `money` decimal(12,4) NOT NULL COMMENT '结算金额',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `iet` (`iet`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='其它收入单详情';

CREATE TABLE `iet` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '类别名称',
  `type` tinyint(1) NOT NULL COMMENT '收支类型[0:收入:1支出]',
  `sort` int(11) NOT NULL COMMENT '类别排序',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `sort` (`sort`),
  KEY `name` (`name`),
  KEY `type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收支类别';

CREATE TABLE `imy` (
  `id` varchar(32) NOT NULL,
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `customer` varchar(32) NOT NULL COMMENT '客户',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `total` decimal(16,4) NOT NULL COMMENT '单据金额',
  `people` varchar(32) COMMENT '关联人员',
  `file` text COMMENT '单据附件',
  `data` varchar(256) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) NOT NULL COMMENT '审核状态[0:未审核|1:已审核]',
  `nucleus` tinyint(1) NOT NULL COMMENT '核销状态[0:未核销|1:部分核销|2:已核销]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `supplier` (`customer`),
  KEY `time` (`time`),
  KEY `examine` (`examine`),
  KEY `nucleus` (`nucleus`),
  KEY `people` (`people`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收款单';

CREATE TABLE `imy_bill` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属单据',
  `type` varchar(32) NOT NULL COMMENT '核销类型',
  `source` varchar(32) NOT NULL COMMENT '关联单据',
  `time` datetime NOT NULL COMMENT '单据时间',
  `money` decimal(16,4) NOT NULL COMMENT '核销金额',
  PRIMARY KEY (`id`),
  KEY `pid_account_user` (`pid`),
  KEY `type` (`type`),
  KEY `source` (`source`),
  KEY `time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收款单核销详情';

CREATE TABLE `imy_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `account` varchar(32) COMMENT '结算账户',
  `money` decimal(12,4) NOT NULL COMMENT '结算金额',
  `settle` varchar(256) NOT NULL COMMENT '结算号',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收款单详情';

CREATE TABLE `invoice` (
  `id` varchar(32) NOT NULL,
  `type` varchar(32) NOT NULL COMMENT '单据类型',
  `class` varchar(32) NOT NULL COMMENT '所属单据',
  `time` datetime NOT NULL COMMENT '开票时间',
  `number` varchar(64) NOT NULL COMMENT '发票号码',
  `title` varchar(64) NOT NULL COMMENT '发票抬头',
  `money` decimal(16,4) NOT NULL COMMENT '开票金额',
  `file` text COMMENT '发票附件',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `type` (`type`),
  KEY `class` (`class`),
  KEY `time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发票详情';

CREATE TABLE `log` (
  `id` varchar(32) NOT NULL,
  `time` datetime NOT NULL COMMENT '操作时间',
  `user` varchar(32) NOT NULL COMMENT '所属用户',
  `info` varchar(256) NOT NULL COMMENT '操作内容',
  PRIMARY KEY (`id`),
  KEY `time` (`time`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志';

CREATE TABLE `menu` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) COMMENT '所属菜单',
  `name` varchar(32) NOT NULL COMMENT '菜单名称',
  `key` varchar(32) NOT NULL COMMENT '菜单标识',
  `model` tinyint(1) NOT NULL COMMENT '菜单模式[0:标签模式|1:新页模式]',
  `type` tinyint(1) NOT NULL COMMENT '菜单类型[0:独立菜单|1:附属菜单]',
  `resource` varchar(128) NOT NULL COMMENT '菜单地址',
  `sort` int(11) DEFAULT '0' COMMENT '菜单排序',
  `ico` varchar(32) COMMENT '菜单图标',
  `root` varchar(32) COMMENT '权限标识',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `name` (`name`),
  KEY `model` (`model`),
  KEY `type` (`type`),
  KEY `sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单信息';

CREATE TABLE `oce` (
  `id` varchar(32) NOT NULL,
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `supplier` varchar(32) COMMENT '供应商',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `total` decimal(16,4) NOT NULL COMMENT '单据金额',
  `actual` decimal(16,4) NOT NULL COMMENT '实际金额',
  `money` decimal(16,4) NOT NULL COMMENT '实付金额',
  `account` varchar(32) NOT NULL COMMENT '结算账户',
  `people` varchar(32) COMMENT '关联人员',
  `file` text COMMENT '单据附件',
  `data` varchar(256) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) NOT NULL COMMENT '审核状态[0:未审核|1:已审核]',
  `nucleus` tinyint(1) NOT NULL COMMENT '核销状态[0:未核销|1:部分核销|2:已核销]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `time` (`time`),
  KEY `examine` (`examine`),
  KEY `people` (`people`),
  KEY `supplier` (`supplier`),
  KEY `account` (`account`),
  KEY `nucleus` (`nucleus`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='其它支出单';

CREATE TABLE `oce_bill` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属单据',
  `type` varchar(32) NOT NULL COMMENT '核销类型',
  `source` varchar(32) NOT NULL COMMENT '关联单据',
  `time` datetime NOT NULL COMMENT '单据时间',
  `money` decimal(16,4) NOT NULL COMMENT '核销金额',
  PRIMARY KEY (`id`),
  KEY `pid_account_user` (`pid`),
  KEY `type` (`type`),
  KEY `source` (`source`),
  KEY `time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='其它支出单核销详情';

CREATE TABLE `oce_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `source` varchar(32) NOT NULL COMMENT '所属费用',
  `iet` varchar(32) NOT NULL COMMENT '收支类型',
  `money` decimal(12,4) NOT NULL COMMENT '结算金额',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `source` (`source`),
  KEY `iet` (`iet`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='其它支出单详情';

CREATE TABLE `often` (
  `id` varchar(32) NOT NULL,
  `user` varchar(32) NOT NULL COMMENT '所属用户',
  `name` varchar(64) NOT NULL COMMENT '功能名称',
  `key` varchar(32) NOT NULL COMMENT '功能标识',
  PRIMARY KEY (`id`),
  KEY `user` (`user`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='常用功能';

CREATE TABLE `omy` (
  `id` varchar(32) NOT NULL,
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `supplier` varchar(32) COMMENT '供应商',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `total` decimal(16,4) NOT NULL COMMENT '单据金额',
  `people` varchar(32) COMMENT '关联人员',
  `file` text COMMENT '单据附件',
  `data` varchar(256) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) NOT NULL COMMENT '审核状态[0:未审核|1:已审核]',
  `nucleus` tinyint(1) NOT NULL COMMENT '核销状态[0:未核销|1:部分核销|2:已核销]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `supplier` (`supplier`),
  KEY `time` (`time`),
  KEY `examine` (`examine`),
  KEY `nucleus` (`nucleus`),
  KEY `people` (`people`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='付款单';

CREATE TABLE `omy_bill` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属单据',
  `type` varchar(32) NOT NULL COMMENT '核销类型',
  `source` varchar(32) NOT NULL COMMENT '关联单据',
  `time` datetime NOT NULL COMMENT '单据时间',
  `money` decimal(16,4) NOT NULL COMMENT '核销金额',
  PRIMARY KEY (`id`),
  KEY `pid_account_user` (`pid`),
  KEY `type` (`type`),
  KEY `source` (`source`),
  KEY `time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='付款单核销详情';

CREATE TABLE `omy_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `account` varchar(32) COMMENT '结算账户',
  `money` decimal(12,4) NOT NULL COMMENT '结算金额',
  `settle` varchar(256) NOT NULL COMMENT '结算号',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='付款单详情';

CREATE TABLE `people` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '人员名称',
  `py` varchar(32) NOT NULL COMMENT '拼音信息',
  `number` varchar(32) NOT NULL COMMENT '人员编号',
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `sex` tinyint(1) NOT NULL COMMENT '人员性别[0:女|1:男]',
  `tel` varchar(32) NOT NULL COMMENT '联系电话',
  `add` varchar(64) COMMENT '联系地址',
  `card` varchar(32) COMMENT '身份证号',
  `data` varchar(256) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  PRIMARY KEY (`id`),
  KEY `name_py` (`name`,`py`),
  KEY `frame` (`frame`),
  KEY `user` (`sex`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='人员管理';

CREATE TABLE `period` (
  `id` varchar(32) NOT NULL,
  `date` date NOT NULL COMMENT '结账日期',
  `time` date NOT NULL COMMENT '操作日期',
  `user` varchar(32) NOT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  KEY `date` (`date`),
  KEY `time` (`time`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='结账表';

CREATE TABLE `record` (
  `id` varchar(32) NOT NULL,
  `type` varchar(32) NOT NULL COMMENT '单据类型',
  `source` varchar(32) NOT NULL COMMENT '关联单据',
  `time` datetime NOT NULL COMMENT '事件时间',
  `user` varchar(32) NOT NULL COMMENT '事件用户',
  `info` varchar(64) NOT NULL COMMENT '事件内容',
  PRIMARY KEY (`id`),
  KEY `type` (`type`),
  KEY `source` (`source`),
  KEY `time` (`time`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单据记录';

CREATE TABLE `role` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '角色名称',
  `data` varchar(256) COMMENT '备注信息',
  `root` text NOT NULL COMMENT '功能数据',
  `auth` text NOT NULL COMMENT '数据权限',
  PRIMARY KEY (`id`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色';

CREATE TABLE `room` (
  `id` varchar(32) NOT NULL,
  `warehouse` varchar(32) NOT NULL COMMENT '所属仓库',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) COMMENT '辅助属性',
  `nums` decimal(12,4) NOT NULL COMMENT '库存数量',
  PRIMARY KEY (`id`),
  KEY `warehouse` (`warehouse`),
  KEY `goods` (`goods`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='仓储信息';

CREATE TABLE `room_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属仓储',
  `type` varchar(32) NOT NULL COMMENT '单据类型',
  `class` varchar(32) NOT NULL COMMENT '所属类',
  `info` varchar(32) NOT NULL COMMENT '所属详情',
  `time` datetime NOT NULL COMMENT '单据时间',
  `direction` tinyint(1) NOT NULL COMMENT '方向[0:出|1:入]',
  `price` decimal(12,4) NOT NULL COMMENT '基础单价',
  `nums` decimal(12,4) NOT NULL COMMENT '基础数量',
  PRIMARY KEY (`id`),
  KEY `room_attr` (`pid`),
  KEY `type` (`type`),
  KEY `class` (`class`),
  KEY `info` (`info`),
  KEY `time` (`time`),
  KEY `direction` (`direction`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='仓储详情';

CREATE TABLE `sell` (
  `id` varchar(32) NOT NULL,
  `source` varchar(32) COMMENT '关联单据|SOR',
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `customer` varchar(32) NOT NULL COMMENT '客户',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `total` decimal(16,4) NOT NULL COMMENT '单据金额',
  `actual` decimal(16,4) NOT NULL COMMENT '实际金额',
  `money` decimal(16,4) NOT NULL COMMENT '实收金额',
  `cost` decimal(16,4) NOT NULL DEFAULT '0.0000' COMMENT '单据费用',
  `account` varchar(32) COMMENT '结算账户',
  `people` varchar(32) COMMENT '关联人员',
  `logistics` text COMMENT '物流信息',
  `file` text COMMENT '单据附件',
  `data` varchar(256) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) NOT NULL COMMENT '审核状态[0:未审核|1:已审核]',
  `nucleus` tinyint(1) NOT NULL COMMENT '核销状态[0:未核销|1:部分核销|2:已核销]',
  `cse` tinyint(1) NOT NULL COMMENT '费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]',
  `invoice` tinyint(1) NOT NULL COMMENT '发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]',
  `check` tinyint(1) NOT NULL COMMENT '核对状态[0:未核对|1:已核对]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `supplier` (`customer`),
  KEY `time` (`time`),
  KEY `examine` (`examine`),
  KEY `nucleus` (`nucleus`),
  KEY `people` (`people`),
  KEY `cse` (`cse`),
  KEY `invoice` (`invoice`),
  KEY `check` (`check`),
  KEY `user` (`user`),
  KEY `account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售单';

CREATE TABLE `sell_bill` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属单据',
  `type` varchar(32) NOT NULL COMMENT '核销类型',
  `source` varchar(32) NOT NULL COMMENT '关联单据',
  `time` datetime NOT NULL COMMENT '单据时间',
  `money` decimal(16,4) NOT NULL COMMENT '核销金额',
  PRIMARY KEY (`id`),
  KEY `pid_account_user` (`pid`),
  KEY `type` (`type`),
  KEY `source` (`source`),
  KEY `time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售单核销详情';

CREATE TABLE `sell_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `source` varchar(32) COMMENT '关联详情|SOR',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) COMMENT '辅助属性',
  `unit` varchar(32) NOT NULL COMMENT '单位',
  `warehouse` varchar(32) COMMENT '仓库',
  `batch` varchar(32) COMMENT '批次号',
  `mfd` date COMMENT '生产日期',
  `price` decimal(12,4) NOT NULL COMMENT '单价',
  `nums` decimal(12,4) NOT NULL COMMENT '数量',
  `serial` text COMMENT '序列号',
  `discount` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '折扣率',
  `dsc` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '折扣额',
  `total` decimal(12,4) NOT NULL COMMENT '金额',
  `tax` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '税率',
  `tat` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '税额',
  `tpt` decimal(12,4) NOT NULL COMMENT '价税合计',
  `data` varchar(256) COMMENT '备注信息',
  `retreat` decimal(12,4) DEFAULT '0.0000' COMMENT '退货数量',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `source` (`source`),
  KEY `goods` (`goods`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售单详情';

CREATE TABLE `serial` (
  `id` varchar(32) NOT NULL,
  `room` varchar(32) NOT NULL COMMENT '所属仓储',
  `warehouse` varchar(32) NOT NULL COMMENT '所属仓库',
  `batch` varchar(32) COMMENT '所属批次',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `number` varchar(64) NOT NULL COMMENT '序列号',
  `state` tinyint(1) NOT NULL COMMENT '状态[0:未销售|1:已销售|2:已调拨|3:已退货]',
  PRIMARY KEY (`id`),
  KEY `room_attr` (`room`),
  KEY `number` (`number`),
  KEY `state` (`state`),
  KEY `warehouse` (`warehouse`),
  KEY `batch` (`batch`),
  KEY `goods` (`goods`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='序列号';

CREATE TABLE `serial_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属序列',
  `type` varchar(32) NOT NULL COMMENT '单据类型',
  `class` varchar(32) NOT NULL COMMENT '所属类',
  `info` varchar(32) NOT NULL COMMENT '所属详情',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `type` (`type`),
  KEY `class` (`class`),
  KEY `info` (`info`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='序列号详情';

CREATE TABLE `serve` (
  `id` varchar(32) NOT NULL,
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) COMMENT '辅助属性',
  `nums` decimal(12,4) NOT NULL COMMENT '累计数量',
  PRIMARY KEY (`id`),
  KEY `goods_attr` (`goods`,`attr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务信息';

CREATE TABLE `serve_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `type` varchar(32) NOT NULL COMMENT '单据类型',
  `class` varchar(32) NOT NULL COMMENT '所属类',
  `info` varchar(32) NOT NULL COMMENT '所属详情',
  `time` datetime NOT NULL COMMENT '单据时间',
  `price` decimal(12,4) NOT NULL COMMENT '单价',
  `nums` decimal(12,4) NOT NULL COMMENT '数量',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `type_info` (`type`,`info`),
  KEY `class` (`class`),
  KEY `info` (`info`),
  KEY `time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务详情';

CREATE TABLE `sor` (
  `id` varchar(32) NOT NULL,
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `customer` varchar(32) NOT NULL COMMENT '客户',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `total` decimal(16,4) NOT NULL COMMENT '单据金额',
  `actual` decimal(16,4) NOT NULL COMMENT '实际金额',
  `people` varchar(32) COMMENT '关联人员',
  `arrival` date NOT NULL COMMENT '到货日期',
  `logistics` text COMMENT '物流信息',
  `file` text COMMENT '单据附件',
  `data` varchar(256) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) NOT NULL COMMENT '审核状态[0:未审核|1:已审核]',
  `state` tinyint(1) NOT NULL COMMENT '出库状态[0:未出库|1:部分出库|2:已出库|3:关闭]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `supplier` (`customer`),
  KEY `time` (`time`),
  KEY `examine` (`examine`),
  KEY `state` (`state`),
  KEY `people` (`people`),
  KEY `user` (`user`),
  KEY `arrival` (`arrival`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售订单';

CREATE TABLE `sor_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) COMMENT '辅助属性',
  `unit` varchar(32) NOT NULL COMMENT '单位',
  `warehouse` varchar(32) COMMENT '仓库',
  `price` decimal(12,4) NOT NULL COMMENT '单价',
  `nums` decimal(12,4) NOT NULL COMMENT '数量',
  `discount` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '折扣率',
  `dsc` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '折扣额',
  `total` decimal(12,4) NOT NULL COMMENT '金额',
  `tax` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '税率',
  `tat` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '税额',
  `tpt` decimal(12,4) NOT NULL COMMENT '价税合计',
  `data` varchar(256) COMMENT '备注信息',
  `handle` decimal(12,4) DEFAULT '0.0000' COMMENT '出库数量',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `goods` (`goods`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售订单详情';

CREATE TABLE `sre` (
  `id` varchar(32) NOT NULL,
  `source` varchar(32) COMMENT '关联单据|SELL',
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `customer` varchar(32) NOT NULL COMMENT '客户',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `total` decimal(16,4) NOT NULL COMMENT '单据金额',
  `actual` decimal(16,4) NOT NULL COMMENT '实际金额',
  `money` decimal(16,4) NOT NULL COMMENT '实付金额',
  `cost` decimal(16,4) NOT NULL DEFAULT '0.0000' COMMENT '单据费用',
  `account` varchar(32) COMMENT '结算账户',
  `people` varchar(32) COMMENT '关联人员',
  `logistics` text COMMENT '物流信息',
  `file` text COMMENT '单据附件',
  `data` varchar(256) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) NOT NULL COMMENT '审核状态[0:未审核|1:已审核]',
  `nucleus` tinyint(1) NOT NULL COMMENT '核销状态[0:未核销|1:部分核销|2:已核销]',
  `cse` tinyint(1) NOT NULL COMMENT '费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]',
  `invoice` tinyint(1) NOT NULL COMMENT '发票状态[0:未开票|1:部分开票|2:已开票|3:无需开具]',
  `check` tinyint(1) NOT NULL COMMENT '核对状态[0:未核对|1:已核对]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `time` (`time`),
  KEY `examine` (`examine`),
  KEY `nucleus` (`nucleus`),
  KEY `source` (`source`),
  KEY `people` (`people`),
  KEY `customer` (`customer`),
  KEY `account` (`account`),
  KEY `cse` (`cse`),
  KEY `invoice` (`invoice`),
  KEY `check` (`check`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售退货单';

CREATE TABLE `sre_bill` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属单据',
  `type` varchar(32) NOT NULL COMMENT '核销类型',
  `source` varchar(32) NOT NULL COMMENT '关联单据',
  `time` datetime NOT NULL COMMENT '单据时间',
  `money` decimal(16,4) NOT NULL COMMENT '核销金额',
  PRIMARY KEY (`id`),
  KEY `pid_account_user` (`pid`),
  KEY `type` (`type`),
  KEY `source` (`source`),
  KEY `time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售退货单核销详情';

CREATE TABLE `sre_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `source` varchar(32) COMMENT '关联详情|SELL',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) COMMENT '辅助属性',
  `unit` varchar(32) NOT NULL COMMENT '单位',
  `warehouse` varchar(32) COMMENT '仓库',
  `batch` varchar(32) COMMENT '批次号',
  `mfd` date COMMENT '生产日期',
  `price` decimal(12,4) NOT NULL COMMENT '单价',
  `nums` decimal(12,4) NOT NULL COMMENT '数量',
  `serial` text COMMENT '序列号',
  `discount` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '折扣率',
  `dsc` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '折扣额',
  `total` decimal(12,4) NOT NULL COMMENT '金额',
  `tax` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '税率',
  `tat` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '税额',
  `tpt` decimal(12,4) NOT NULL COMMENT '价税合计',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `source` (`source`),
  KEY `goods` (`goods`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售退货单详情';

CREATE TABLE `summary` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '仓储详情',
  `type` varchar(32) NOT NULL COMMENT '单据类型',
  `class` varchar(32) NOT NULL COMMENT '所属单据',
  `info` varchar(32) NOT NULL COMMENT '所属详情',
  `time` datetime NOT NULL COMMENT '单据时间',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) COMMENT '辅助属性',
  `warehouse` varchar(32) NOT NULL COMMENT '所属仓库',
  `batch` varchar(64) COMMENT '批次',
  `mfd` date COMMENT '生产日期',
  `serial` text COMMENT '序列号',
  `direction` tinyint(1) NOT NULL COMMENT '方向[0:出|1:入]',
  `price` decimal(12,4) NOT NULL COMMENT '基础单价',
  `nums` decimal(12,4) NOT NULL COMMENT '基础数量',
  `uct` decimal(12,4) NOT NULL COMMENT '单位成本',
  `bct` decimal(12,4) NOT NULL COMMENT '基础成本',
  `exist` text NOT NULL COMMENT '结存组',
  `balance` text NOT NULL COMMENT '结余组',
  `handle` decimal(12,4) NOT NULL COMMENT '先进先出',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `type` (`type`),
  KEY `class` (`class`),
  KEY `info` (`info`),
  KEY `time` (`time`),
  KEY `goods` (`goods`),
  KEY `direction` (`direction`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收发统计表';

CREATE TABLE `supplier` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '供应商名称',
  `py` varchar(32) NOT NULL COMMENT '拼音信息',
  `number` varchar(32) NOT NULL COMMENT '供应商编号',
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `user` varchar(32) NOT NULL COMMENT '所属用户',
  `category` varchar(32) NOT NULL COMMENT '供应商类别',
  `rate` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '增值税税率',
  `bank` varchar(32) COMMENT '开户银行',
  `account` varchar(64) COMMENT '银行账号',
  `tax` varchar(64) COMMENT '纳税号码',
  `data` varchar(256) COMMENT '备注信息',
  `contacts` text COMMENT '联系资料',
  `balance` decimal(16,4) DEFAULT '0.0000' COMMENT '应付款余额',
  `more` text COMMENT '扩展信息',
  PRIMARY KEY (`id`),
  KEY `name_py` (`name`,`py`),
  KEY `frame` (`frame`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='供应商';

CREATE TABLE `swap` (
  `id` varchar(32) NOT NULL,
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `total` decimal(16,4) NOT NULL COMMENT '单据成本',
  `cost` decimal(16,4) NOT NULL DEFAULT '0.0000' COMMENT '单据费用',
  `logistics` text COMMENT '物流信息',
  `people` varchar(32) COMMENT '关联人员',
  `file` text COMMENT '单据附件',
  `data` varchar(265) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  `examine` tinyint(1) DEFAULT '0' COMMENT '审核状态[0:未审核|1:已审核]',
  `cse` tinyint(1) DEFAULT NULL COMMENT '费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]',
  `user` varchar(32) NOT NULL COMMENT '制单人',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `time` (`time`),
  KEY `people` (`people`),
  KEY `examine` (`examine`),
  KEY `cse` (`cse`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='调拨单';

CREATE TABLE `swap_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) COMMENT '辅助属性',
  `unit` varchar(32) NOT NULL COMMENT '单位',
  `warehouse` varchar(32) NOT NULL COMMENT '调出仓库',
  `storehouse` varchar(32) NOT NULL COMMENT '调入仓库',
  `batch` varchar(32) COMMENT '批次号',
  `mfd` date COMMENT '生产日期',
  `price` decimal(12,4) NOT NULL COMMENT '成本',
  `nums` decimal(12,4) NOT NULL COMMENT '数量',
  `serial` text COMMENT '序列号',
  `total` decimal(12,4) NOT NULL COMMENT '总成本',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `goods` (`goods`),
  KEY `warehouse` (`warehouse`),
  KEY `storehouse` (`storehouse`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='调拨单详情';

CREATE TABLE `sys` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '配置名称',
  `info` text NOT NULL COMMENT '配置内容',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置';

CREATE TABLE `user` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '用户名称',
  `py` varchar(32) NOT NULL COMMENT '拼音信息',
  `tel` varchar(11) NOT NULL COMMENT '手机号码',
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `role` varchar(32) NOT NULL COMMENT '所属角色',
  `user` varchar(32) NOT NULL COMMENT '登陆账号',
  `pwd` varchar(255) NOT NULL COMMENT '登陆密码',
  `img` text COMMENT '用户头像',
  `token` varchar(32) COMMENT '秘钥信息',
  `expire` int(11) COMMENT '秘钥时效',
  `data` varchar(256) COMMENT '备注信息',
  `more` text COMMENT '扩展信息',
  PRIMARY KEY (`id`),
  KEY `name_py` (`name`,`py`),
  KEY `frame` (`frame`),
  KEY `tel` (`tel`),
  KEY `role` (`role`),
  KEY `user` (`user`),
  KEY `expire` (`expire`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

CREATE TABLE `warehouse` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '仓库名称',
  `number` varchar(32) NOT NULL COMMENT '仓库编号',
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `contacts` varchar(32) COMMENT '联系人员',
  `tel` varchar(32) COMMENT '联系电话',
  `add` varchar(64) COMMENT '仓库地址',
  `data` varchar(256) COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `frame` (`frame`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='仓库';

CREATE TABLE `tmpl_import` (
  `id` varchar(32) NOT NULL COMMENT '唯一标识',
  `name` varchar(255) NOT NULL COMMENT '模板名称',
  `code` varchar(255) NOT NULL COMMENT '模板编码',
  `save_type` varchar(255) NOT NULL COMMENT '文件存储方式，值对应字典表value',
  `save_path` varchar(5000) NOT NULL COMMENT '文件存储路径，不要将服务器域名和端口存储到数据库',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` int NOT NULL DEFAULT '1' COMMENT '数据状态 0 未使用 1使用中',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_code` (`code`) USING BTREE COMMENT '模板编码唯一约束'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='导入模板';

CREATE TABLE `dict_type` (
  `id` varchar(32) NOT NULL COMMENT '唯一标识',
  `name` varchar(255) NOT NULL COMMENT '字典类型名称',
  `code` varchar(255) NOT NULL COMMENT '字典类型编码',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_code` (`code`) USING BTREE COMMENT '字典类型编码唯一约束'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型';

CREATE TABLE `dict` (
  `id` varchar(32) NOT NULL COMMENT '唯一标识',
  `tid` varchar(32) NOT NULL COMMENT '所属字典类型',
  `name` varchar(255) NOT NULL COMMENT '字典名称',
  `value` varchar(255) NOT NULL COMMENT '字典值',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典';

CREATE TABLE `annex` (
  `id` varchar(32) NOT NULL COMMENT '唯一标识',
  `name` varchar(255) DEFAULT NULL COMMENT '文件名称',
  `file_type` varchar(255) NOT NULL COMMENT '文件类型，值对应字典表value',
  `save_type` varchar(255) NOT NULL COMMENT '文件存储方式，值对应字典表value',
  `save_path` varchar(5000) NOT NULL COMMENT '文件存储路径，不要将服务器域名和端口存储到数据库',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` int NOT NULL DEFAULT '1' COMMENT '数据状态 0 未使用 1使用中',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='附件';

CREATE TABLE `func_perm`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `mid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属菜单id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '功能权限名称',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问地址',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `is_enable` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否启用 1表示启用 0表示禁用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '功能权限表' ROW_FORMAT = DYNAMIC;

ALTER TABLE role ADD COLUMN keyword VARCHAR(24) NOT NULL COMMENT '关键词';

SET foreign_key_checks = 1;
