-- reset_and_insert_test_data.sql
-- 清空并重建表，然后插入测试数据

SET FOREIGN_KEY_CHECKS=0;
SET NAMES utf8;

-- DROP (按依赖顺序安全删除)
DROP TABLE IF EXISTS `summary`;
DROP TABLE IF EXISTS `bre_info`;
DROP TABLE IF EXISTS `buy_info`;
DROP TABLE IF EXISTS `buy`;
DROP TABLE IF EXISTS `goods`;

-- CREATE buy
CREATE TABLE `buy` (
  `id` varchar(32) NOT NULL,
  `source` varchar(32) DEFAULT NULL COMMENT '关联单据|BOR',
  `frame` varchar(32) NOT NULL DEFAULT '0' COMMENT '所属组织',
  `supplier` varchar(32) NOT NULL COMMENT '供应商',
  `time` datetime NOT NULL COMMENT '单据时间',
  `number` varchar(32) NOT NULL COMMENT '单据编号',
  `total` decimal(16,4) NOT NULL COMMENT '单据金额',
  `actual` decimal(16,4) NOT NULL COMMENT '实际金额',
  `money` decimal(16,4) NOT NULL COMMENT '实付金额',
  `cost` decimal(16,4) NOT NULL DEFAULT '0.0000' COMMENT '单据费用',
  `account` varchar(32) DEFAULT NULL COMMENT '结算账户',
  `people` varchar(32) DEFAULT NULL COMMENT '关联人员',
  `logistics` text COMMENT '物流信息',
  `file` text COMMENT '单据附件',
  `data` varchar(256) DEFAULT NULL COMMENT '备注信息',
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

-- CREATE buy_info
CREATE TABLE `buy_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `source` varchar(32) DEFAULT NULL COMMENT '关联详情|BOR',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) DEFAULT NULL COMMENT '辅助属性',
  `unit` varchar(32) NOT NULL COMMENT '单位',
  `warehouse` varchar(32) DEFAULT NULL COMMENT '仓库',
  `batch` varchar(32) DEFAULT NULL COMMENT '批次号',
  `mfd` date DEFAULT NULL COMMENT '生产日期',
  `price` decimal(12,4) NOT NULL COMMENT '单价',
  `nums` decimal(12,4) NOT NULL COMMENT '数量',
  `serial` text COMMENT '序列号',
  `discount` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '折扣率',
  `dsc` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '折扣额',
  `total` decimal(12,4) NOT NULL COMMENT '金额',
  `tax` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '税率',
  `tat` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '税额',
  `tpt` decimal(12,4) NOT NULL COMMENT '价税合计',
  `data` varchar(256) DEFAULT NULL COMMENT '备注信息',
  `retreat` decimal(12,4) DEFAULT '0.0000' COMMENT '退货数量',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `source` (`source`),
  KEY `goods` (`goods`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购单详情';

-- CREATE bre_info
CREATE TABLE `bre_info` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '所属ID',
  `source` varchar(32) DEFAULT NULL COMMENT '关联详情|BUY',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) DEFAULT NULL COMMENT '辅助属性',
  `unit` varchar(32) NOT NULL COMMENT '单位',
  `warehouse` varchar(32) DEFAULT NULL COMMENT '仓库',
  `batch` varchar(32) DEFAULT NULL COMMENT '批次号',
  `mfd` date DEFAULT NULL COMMENT '生产日期',
  `price` decimal(12,4) NOT NULL COMMENT '单价',
  `nums` decimal(12,4) NOT NULL COMMENT '数量',
  `serial` text COMMENT '序列号',
  `discount` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '折扣率',
  `dsc` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '折扣额',
  `total` decimal(12,4) NOT NULL COMMENT '金额',
  `tax` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '税率',
  `tat` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '税额',
  `tpt` decimal(12,4) NOT NULL COMMENT '价税合计',
  `data` varchar(256) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `source` (`source`),
  KEY `goods` (`goods`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购退货单详情';

-- CREATE goods
CREATE TABLE `goods` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '商品名称',
  `py` varchar(32) NOT NULL COMMENT '拼音信息',
  `number` varchar(32) NOT NULL COMMENT '商品编号',
  `spec` varchar(32) DEFAULT NULL COMMENT '规格型号',
  `category` varchar(32) NOT NULL COMMENT '商品类别',
  `brand` varchar(32) DEFAULT NULL COMMENT '商品品牌',
  `unit` varchar(32) NOT NULL COMMENT '商品单位[*:常规单位|-1:多单位]',
  `buy` decimal(12,4) NOT NULL COMMENT '采购价格',
  `sell` decimal(12,4) NOT NULL COMMENT '销售价格',
  `code` varchar(64) DEFAULT NULL COMMENT '商品条码',
  `location` varchar(64) DEFAULT NULL COMMENT '商品货位',
  `stock` decimal(12,4) NOT NULL COMMENT '库存阈值',
  `type` tinyint(1) NOT NULL COMMENT '产品类型[0:常规商品|1:服务商品]',
  `data` varchar(256) DEFAULT NULL COMMENT '备注信息',
  `imgs` text COMMENT '商品图像',
  `details` text COMMENT '图文详情',
  `units` text COMMENT '多单位配置',
  `strategy` text COMMENT '折扣策略',
  `serial` tinyint(1) DEFAULT '0' COMMENT '序列产品[0:关闭|1:启用]',
  `batch` tinyint(1) DEFAULT '0' COMMENT '批次产品[0:关闭|1:启用]',
  `validity` tinyint(1) DEFAULT '0' COMMENT '有效期[0:关闭|1:启用]',
  `protect` smallint DEFAULT '0' COMMENT '保质期',
  `threshold` smallint DEFAULT '0' COMMENT '预警阀值',
  `more` text NOT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`),
  KEY `number` (`number`),
  KEY `name_py` (`name`,`py`),
  KEY `category` (`category`),
  KEY `code` (`code`),
  KEY `type` (`type`),
  KEY `brand` (`brand`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品';

-- CREATE summary
CREATE TABLE `summary` (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) NOT NULL COMMENT '仓储详情',
  `type` varchar(32) NOT NULL COMMENT '单据类型',
  `class` varchar(32) NOT NULL COMMENT '所属单据',
  `info` varchar(32) NOT NULL COMMENT '所属详情',
  `time` datetime NOT NULL COMMENT '单据时间',
  `goods` varchar(32) NOT NULL COMMENT '所属商品',
  `attr` varchar(64) DEFAULT NULL COMMENT '辅助属性',
  `warehouse` varchar(32) NOT NULL COMMENT '所属仓库',
  `batch` varchar(64) DEFAULT NULL COMMENT '批次',
  `mfd` date DEFAULT NULL COMMENT '生产日期',
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

-- ----------------------
-- INSERT sample data
-- ----------------------

-- goods: 10 rows (注意列顺序与值数量一一对应)
INSERT INTO `goods` (`id`,`name`,`py`,`number`,`spec`,`category`,`brand`,`unit`,`buy`,`sell`,`code`,`location`,`stock`,`type`,`data`,`imgs`,`details`,`units`,`strategy`,`serial`,`batch`,`validity`,`protect`,`threshold`,`more`)
VALUES
('G001','螺丝M4','lsm4','G001','M4x10','五金','品牌A','个',1.5000,2.5000,'690000000001','A-01-01',100.0000,0,'测试数据','','','','',0,0,0,0,0,'{}'),
('G002','螺母M4','snm4','G002','M4','五金','品牌A','个',0.8000,1.5000,'690000000002','A-01-02',200.0000,0,'测试数据','','','','',0,0,0,0,0,'{}'),
('G003','垫圈8','dh8','G003','8','五金','品牌B','个',0.1000,0.3000,'690000000003','A-01-03',500.0000,0,'测试数据','','','','',0,0,0,0,0,'{}'),
('G004','电阻10k','dr10k','G004','10kΩ','电子','品牌C','个',0.0500,0.1200,'690000000004','B-01-01',1000.0000,0,'测试数据','','','','',0,0,0,0,0,'{}'),
('G005','电容100uF','dc100','G005','100uF','电子','品牌C','个',0.2000,0.5000,'690000000005','B-01-02',800.0000,0,'测试数据','','','','',0,0,0,0,0,'{}'),
('G006','铝型材20x20','lxc2020','G006','20x20','型材','品牌D','米',12.0000,18.0000,'690000000006','C-01-01',50.0000,0,'测试数据','','','','',0,0,0,0,0,'{}'),
('G007','电机24V','dj24','G007','24V,30W','机电','品牌E','台',120.0000,180.0000,'690000000007','D-01-01',20.0000,0,'测试数据','','','','',0,0,0,0,0,'{}'),
('G008','风扇80mm','ff80','G008','80mm','零件','品牌F','个',8.5000,12.5000,'690000000008','D-01-02',60.0000,0,'测试数据','','','','',0,0,0,0,0,'{}'),
('G009','开关','kg1','G009','单刀单掷','电器','品牌G','个',3.0000,5.0000,'690000000009','E-01-01',150.0000,0,'测试数据','','','','',0,0,0,0,0,'{}'),
('G010','指示灯','zsd','G010','LED绿','电器','品牌G','个',1.2000,2.2000,'690000000010','E-01-02',300.0000,0,'测试数据','','','','',0,0,0,0,0,'{}');

-- buy: 3 sample purchase orders
INSERT INTO `buy` (`id`,`source`,`frame`,`supplier`,`time`,`number`,`total`,`actual`,`money`,`cost`,`account`,`people`,`logistics`,`file`,`data`,`more`,`examine`,`nucleus`,`cse`,`invoice`,`check`,`user`)
VALUES
('B001',NULL,'0','SUP_A','2025-10-01 10:00:00','PO-1001',1000.0000,1000.0000,1000.0000,0.0000,'acct-1','alice','{"carrier":"快递A"}',NULL,'测试采购单1','{}',1,0,0,0,1,'alice'),
('B002',NULL,'0','SUP_B','2025-10-05 15:30:00','PO-1002',500.0000,500.0000,500.0000,0.0000,'acct-2','bob','{"carrier":"快递B"}',NULL,'测试采购单2','{}',1,0,0,0,1,'bob'),
('B003',NULL,'0','SUP_A','2025-10-20 09:20:00','PO-1003',300.0000,300.0000,300.0000,0.0000,'acct-1','charlie','{"carrier":"快递C"}',NULL,'测试采购单3','{}',0,0,0,0,0,'charlie');

-- buy_info: 5 sample detail lines
INSERT INTO `buy_info` (`id`,`pid`,`source`,`goods`,`attr`,`unit`,`warehouse`,`batch`,`mfd`,`price`,`nums`,`serial`,`discount`,`dsc`,`total`,`tax`,`tat`,`tpt`,`data`,`retreat`)
VALUES
('BI001','B001',NULL,'G001','默认','个','WH1','BATCH001','2025-09-01',1.5000,200.0000,NULL,0.00,0.0000,300.0000,0.00,0.0000,300.0000,NULL,0.0000),
('BI002','B001',NULL,'G002','默认','个','WH1','BATCH001','2025-09-01',0.8000,100.0000,NULL,0.00,0.0000,80.0000,0.00,0.0000,80.0000,NULL,0.0000),
('BI003','B002',NULL,'G007','型号A','台','WH2','BATCH002','2025-08-10',120.0000,2.0000,NULL,0.00,0.0000,240.0000,0.00,0.0000,240.0000,NULL,0.0000),
('BI004','B002',NULL,'G008','默认','个','WH2','BATCH002','2025-08-12',8.5000,10.0000,NULL,0.00,0.0000,85.0000,0.00,0.0000,85.0000,NULL,0.0000),
('BI005','B003',NULL,'G005','默认','个','WH1','BATCH003','2025-07-01',0.2000,50.0000,NULL,0.00,0.0000,10.0000,0.00,0.0000,10.0000,NULL,0.0000);

-- bre_info: 2 sample return lines
INSERT INTO `bre_info` (`id`,`pid`,`source`,`goods`,`attr`,`unit`,`warehouse`,`batch`,`mfd`,`price`,`nums`,`serial`,`discount`,`dsc`,`total`,`tax`,`tat`,`tpt`,`data`)
VALUES
('RI001','R001',NULL,'G001','默认','个','WH1','RBATCH001','2025-09-15',1.5000,5.0000,NULL,0.00,0.0000,7.5000,0.00,0.0000,7.5000,NULL),
('RI002','R002',NULL,'G008','默认','个','WH2','RBATCH002','2025-08-20',8.5000,1.0000,NULL,0.00,0.0000,8.5000,0.00,0.0000,8.5000,NULL);

-- summary: 6 sample summary records (方向 direction: 1=入,0=出)
INSERT INTO `summary` (`id`,`pid`,`type`,`class`,`info`,`time`,`goods`,`attr`,`warehouse`,`batch`,`mfd`,`serial`,`direction`,`price`,`nums`,`uct`,`bct`,`exist`,`balance`,`handle`)
VALUES
('S001','B001','buy','B001','BI001','2025-10-01 10:05:00','G001','默认','WH1','BATCH001','2025-09-01',NULL,1,1.5000,200.0000,1.5000,300.0000,'[]','[]',0.0000),
('S002','B001','buy','B001','BI002','2025-10-01 10:06:00','G002','默认','WH1','BATCH001','2025-09-01',NULL,1,0.8000,100.0000,0.8000,80.0000,'[]','[]',0.0000),
('S003','B002','buy','B002','BI003','2025-10-05 15:35:00','G007','型号A','WH2','BATCH002','2025-08-10',NULL,1,120.0000,2.0000,120.0000,240.0000,'[]','[]',0.0000),
('S004','B002','buy','B002','BI004','2025-10-05 15:36:00','G008','默认','WH2','BATCH002','2025-08-12',NULL,1,8.5000,10.0000,8.5000,85.0000,'[]','[]',0.0000),
('S005','B003','buy','B003','BI005','2025-10-20 09:25:00','G005','默认','WH1','BATCH003','2025-07-01',NULL,1,0.2000,50.0000,0.2000,10.0000,'[]','[]',0.0000),
('S006','R001','bre','R001','RI001','2025-10-10 11:00:00','G001','默认','WH1','RBATCH001','2025-09-15',NULL,0,1.5000,5.0000,1.5000,7.5000,'[]','[]',0.0000);

-- finalize
COMMIT;
SET FOREIGN_KEY_CHECKS=1;
