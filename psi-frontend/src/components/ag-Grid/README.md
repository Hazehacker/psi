# ag-Grid 组件库

## 组件详细说明（Vue 3）

### Allowance.vue（表头 | 批量折扣率）
- 作用：为当前网格所有有效行批量写入折扣率
- 依赖：`window.$lib.validate('percentage', value)`、`window.$grid.updateGridData(params, uniqid, key, value)`
- 读取字段：`params.dispose.title`（标题）、`params.dispose.key`（写入的字段名）
- 行数据要求：`node.data.key != null` 的行会被更新
- 交互：点击图标打开 Popover，输入后 Enter 或点击保存



### Batch.vue（编辑器 | 批次录入）
- 作用：录入或选择商品批次号，可选对接外部批次选择弹窗
- 依赖：可选 `window.openLotDialog(condition, cb)`，Element Plus `ElMessage`
- 读取字段：`node.data.batchType`（是否启用）、`node.data.warehouseId`、`node.data.key`、`node.data.attr`
- 写入字段：编辑结果返回列本身；若弹窗回填且有 `row.time`，写入 `node.data.mfd`
- 交互：
  - 若无 `batchType`，`isCancelBeforeStart()` 使编辑器不启动
  - 有 `batchType` 时自动聚焦输入框；可点击“BP”打开选择弹窗



### Cpe.vue（编辑器 | 支出类别）
- 作用：编辑支出类别；当存在来源 `source != 0` 时，可点击查看来源单据的支出详情
- 依赖：`window.$axios.post('service/getCost', { cost: source })`，Element Plus `ElMessage`
- 读取字段：`node.data.source`（是否有来源）、`params.value`（初值）
- 交互：
  - 聚焦输入框并选中内容
  - 有来源时点击标签图标触发展示详情 Popover



### Company.vue（编辑器 | 多单位选择）
- 作用：在多单位商品中切换单位，联动价格与数量
- 依赖：`window.$lib.unitRelation(unit, unitData)`、`window.$calc.chain`（可选）、`window.$grid.updateGridData`
- 读取字段：`node.data.unitData`（单位数组）、`node.data.unitRelation.valence`（基础单价）、`node.data.serial`（序列号列表，可选）
- 写入字段：`price`（按倍数/折扣计算后价格）、`nums`（按倍数换算的数量）
- 交互：弹出单位列表，键盘上下选择、Enter确认、Esc取消



### Depot.vue（渲染器 | 仓库展示与选择）
- 作用：显示当前仓库并提供选择入口（依赖宿主提供弹窗函数）
- 依赖：可选 `window.openStockDialog(condition, cb)`，`window.$grid.updateGridData`
- 显示条件：仅当 `node.data.key != null` 且 `node.data.goodsType == 0` 时显示搜索图标
- 读取/写入字段：`dispose.key`（仓库ID字段），`dispose.text`（仓库名称字段）



### Dispose.vue（渲染器 | 字段显示配置）
- 作用：对列进行“显示/隐藏”的配置，并持久化到 localStorage
- 依赖：`localStorage`、`params.columnApi.setColumnVisible`
- 读取字段：列定义中 `dispose === true` 的列会纳入配置；存储键 `params.dispose.key`



### Fat.vue（编辑器 | 资金账户选择）
- 作用：从全局 `store.state.account` 列表中选择资金账户
- 依赖：`window.$store.state.account`（数组，元素含 id/name）
- 写入字段：`dispose.key`（账户ID），显示 `dispose.text`（账户名称）
- 交互：键盘上下选择、Enter确认、Esc取消



### Price.vue（编辑器 | 价格/最近价）
- 作用：编辑价格，并在选择了供应商/客户与单位后，查询最近采购/销售价格
- 依赖：`window.$axios.post('service/recentPrice', {...})`，Element Plus `ElMessage`
- 前置校验：需存在 `dispose.source()` 的返回值；若 `unitData.length>0` 且 `unit=='点击选择'` 则提示先选单位
- 读取字段：`dispose.model`（业务模块）、`dispose.source()`、`node.data.key/attr/unit`
- 交互：点击图标打开 Popover，自动请求最近价，点击文本可回填



### Bmy.vue（表头 | 核销金额自动分配）
- 作用：按业务类型将可核销金额分配到各行的 `money` 字段
- 依赖：`window.$grid.getGridData`、`window.$grid.updateGridData`、可选 `window.$calc` 精度计算
- 读取字段：`params.dispose.type()`（业务场景）、行数据的 `total/anwo/moldType/uniqid`
- 写入字段：`money`



### Handle.vue（渲染器 | 行操作）
- 作用：在首行提供“新增”按钮，其余行提供“删除”按钮，便于快速维护表格行
- 依赖：ag-Grid API（`applyTransaction`）、父组件监听自定义事件 `addRow`
- 行为：
  - 首行：点击加号图标，向父组件派发 `addRow` 事件，参数为 `api.getFocusedCell()`；父组件据此决定插入位置
  - 非首行：点击删除图标，调用 `api.applyTransaction({ remove: [params.data] })` 移除行，随后触发 `params.context.runHandleGrid()`

