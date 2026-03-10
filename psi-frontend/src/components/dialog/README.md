# Dialog 组件库使用说明

Dialog 组件库包含了一系列弹窗组件，这些组件主要用于数据展示、选择和编辑等场景。

## 组件列表

### 1. GoodsList.vue - 商品列表弹窗
**功能**: 商品选择弹窗，支持搜索、分页和数量编辑
**特点**:
- 支持商品搜索和筛选
- 支持分页加载
- 支持商品数量编辑
- 支持多选商品
- 支持树形结构显示（商品分类）

**使用示例**:
```vue
<template>
  <el-button @click="openGoodsList">选择商品</el-button>
  <GoodsList 
    ref="goodsListRef"
    @confirm="handleGoodsSelect"
  />
</template>

<script setup>
import GoodsList from '@/components/dialog/GoodsList.vue'

const goodsListRef = ref()

const openGoodsList = () => {
  goodsListRef.value.open()
}

const handleGoodsSelect = (selectedGoods) => {
  console.log('选中的商品:', selectedGoods)
}
</script>
```

**主要功能**:
- 商品列表展示
- 商品搜索和筛选
- 商品数量编辑
- 商品选择确认

### 2. BillList.vue - 单据列表弹窗
**功能**: 单据选择弹窗，用于选择各种业务单据
**特点**:
- 支持单据搜索
- 支持分页加载
- 支持单据状态筛选
- 支持单据详情查看

**使用示例**:
```vue
<template>
  <el-button @click="openBillList">选择单据</el-button>
  <BillList 
    ref="billListRef"
    :billType="'purchase'"
    @confirm="handleBillSelect"
  />
</template>

<script setup>
import BillList from '@/components/dialog/BillList.vue'

const billListRef = ref()

const openBillList = () => {
  billListRef.value.open()
}

const handleBillSelect = (selectedBill) => {
  console.log('选中的单据:', selectedBill)
}
</script>
```

### 3. Stock.vue - 库存弹窗
**功能**: 库存信息查看和选择弹窗
**特点**:
- 支持库存查询
- 支持库存统计
- 支持库存预警
- 支持库存调整

**使用示例**:
```vue
<template>
  <el-button @click="openStock">查看库存</el-button>
  <Stock 
    ref="stockRef"
    :goodsId="selectedGoodsId"
    @confirm="handleStockSelect"
  />
</template>

<script setup>
import Stock from '@/components/dialog/Stock.vue'

const stockRef = ref()
const selectedGoodsId = ref('')

const openStock = () => {
  stockRef.value.open()
}
</script>
```

### 4. StockDetail.vue - 库存详情弹窗
**功能**: 详细库存信息展示弹窗
**特点**:
- 支持库存明细查看
- 支持库存变动记录
- 支持库存预警信息
- 支持库存调整操作

### 5. BatchDetail.vue - 批次详情弹窗
**功能**: 商品批次信息详情弹窗
**特点**:
- 支持批次信息查看
- 支持批次追踪
- 支持批次状态管理
- 支持批次操作记录

### 6. SerialDetail.vue - 序列号详情弹窗
**功能**: 商品序列号详情弹窗
**特点**:
- 支持序列号查询
- 支持序列号状态管理
- 支持序列号追踪
- 支持序列号操作记录

### 7. Lot.vue - 批次管理弹窗
**功能**: 批次管理相关操作弹窗
**特点**:
- 支持批次创建
- 支持批次编辑
- 支持批次删除
- 支持批次状态更新

### 8. Snu.vue - 序列号管理弹窗
**功能**: 序列号管理相关操作弹窗
**特点**:
- 支持序列号生成
- 支持序列号编辑
- 支持序列号删除
- 支持序列号状态管理

## 通用特性

### 弹窗打开/关闭
所有弹窗组件都支持以下方法：
```javascript
// 打开弹窗
this.$refs.dialogRef.open()

// 关闭弹窗
this.$refs.dialogRef.close()
```

### 事件监听
所有弹窗组件都支持以下事件：
```javascript
// 确认选择
@confirm="handleConfirm"

// 取消选择
@cancel="handleCancel"

// 弹窗关闭
@close="handleClose"
```

### 通用 Props
```typescript
interface DialogProps {
  // 弹窗标题
  title?: string
  // 弹窗宽度
  width?: string
  // 是否可拖拽
  draggable?: boolean
  // 是否可调整大小
  resizable?: boolean
  // 是否显示遮罩层
  modal?: boolean
  // 是否点击遮罩层关闭
  closeOnClickModal?: boolean
  // 是否按 ESC 键关闭
  closeOnPressEscape?: boolean
}
```

## 使用注意事项

1. **组件引用**: 所有弹窗组件都需要通过 `ref` 引用
2. **事件处理**: 建议在父组件中处理弹窗的确认和取消事件
3. **数据传递**: 可以通过 props 向弹窗传递初始数据
4. **样式定制**: 可以通过 CSS 变量或深度选择器自定义弹窗样式
5. **响应式**: 弹窗组件支持响应式设计，在不同屏幕尺寸下自适应

## 样式定制

```css
/* 自定义弹窗样式 */
.custom-dialog {
  --el-dialog-border-radius: 8px;
  --el-dialog-box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 自定义表格样式 */
.custom-dialog .el-table {
  --el-table-border-color: #e4e7ed;
  --el-table-header-bg-color: #f5f7fa;
}

/* 自定义分页样式 */
.custom-dialog .el-pagination {
  --el-pagination-bg-color: #f5f7fa;
  --el-pagination-button-bg-color: #ffffff;
}
```

## 依赖

- Vue 3.x
- Element Plus
- 项目内部 API 服务
