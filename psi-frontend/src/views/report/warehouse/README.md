# 仓库报表

仓库报表模块，包含商品库存余额表、商品收发明细表、商品收发汇总表等报表功能。

## 商品库存余额表（InventorySummary.vue）

### 功能说明

商品库存余额表用于查看指定日期各仓库的商品库存情况，包括：
- 各仓库的商品数量、成本、总成本
- 所有仓库的汇总数据
- 支持按商品名称、仓库、日期筛选
- 支持数据导出

### 功能特性

- 🔍 **灵活搜索**：集成 `GoodSearchForm` 组件，支持商品名称、仓库、日期筛选
- 📊 **动态表头**：根据仓库数量动态生成表格列
- 💰 **成本统计**：自动计算各仓库和汇总的成本信息
- 📤 **数据导出**：支持导出当前筛选条件下的数据
- 📄 **分页显示**：支持分页查询，可调整每页显示条数
- 🔢 **数据格式化**：数字自动格式化为两位小数

### 页面结构

```
┌─────────────────────────────────────────────┐
│  搜索栏              [导出] [刷新]          │
├─────────────────────────────────────────────┤
│  商品  │ 编号 │ 规格 │ 单位 │ 仓库1 │ 汇总  │
│  信息  │      │      │      │ 仓库2 │       │
│        │      │      │      │  ...  │       │
├─────────────────────────────────────────────┤
│  分页器                                     │
├─────────────────────────────────────────────┤
│  统计信息（总成本等）                       │
└─────────────────────────────────────────────┘
```

### 表格列说明

#### 固定列
- **商品名称**：商品的名称
- **商品编号**：商品的唯一编号
- **规格型号**：商品的规格信息
- **单位**：商品的计量单位

#### 动态列（各仓库）
每个仓库显示三列：
- **成本**：单位成本（uct）
- **数量**：库存数量（uns）
- **总成本**：总成本（bct = uct × uns）

#### 汇总列
- **成本**：所有仓库的平均成本
- **数量**：所有仓库的总数量
- **总成本**：所有仓库的总成本

### 搜索条件

| 字段 | 类型 | 说明 |
|------|------|------|
| 商品名称 | 输入框 | 模糊搜索商品名称 |
| 仓库信息 | 下拉多选 | 选择要查询的仓库（可多选） |
| 库存日期 | 日期选择器 | 选择要查询的库存日期 |

### API 接口

#### 获取库存数据

**请求**
```typescript
POST /wrf/wbs

{
  page: number          // 当前页码
  limit: number         // 每页条数
  goods: string         // 商品名称
  warehouse: string[]   // 仓库ID列表
  time: string          // 库存日期 (yyyy-MM-dd)
}
```

**响应**
```typescript
{
  state: 'success' | 'error'
  info: Array<{
    goodsData: {
      name: string      // 商品名称
      number: string    // 商品编号
      spce: string      // 规格型号
    }
    unit: string        // 单位
    wb_${仓库ID}: {
      uct: number       // 单位成本
      uns: number       // 数量
      bct: number       // 总成本
    }
    balance: {
      uct: number       // 汇总单位成本
      uns: number       // 汇总数量
      bct: number       // 汇总总成本
    }
  }>
  column: Array<{
    id: number          // 仓库ID
    name: string        // 仓库名称
  }>
  count: number         // 总记录数
}
```

#### 导出数据

**请求**
```
GET /wrf/wbsExports?goods=xxx&warehouse[]=1&warehouse[]=2&time=2024-10-29
```

**响应**
- 直接下载 Excel 文件

### 数据结构示例

```typescript
// 表格数据示例
tableData: [
  {
    goodsData: {
      name: '苹果',
      number: 'AP001',
      spce: '红富士/特级'
    },
    unit: '斤',
    wb_1: {           // 仓库1
      uct: 5.5,       // 单位成本 5.5元/斤
      uns: 100,       // 数量 100斤
      bct: 550        // 总成本 550元
    },
    wb_2: {           // 仓库2
      uct: 5.3,
      uns: 200,
      bct: 1060
    },
    balance: {        // 汇总
      uct: 5.37,      // 平均成本
      uns: 300,       // 总数量
      bct: 1610       // 总成本
    }
  }
]

// 仓库列信息
tableColumn: [
  { id: 1, name: '总仓' },
  { id: 2, name: '分仓A' }
]
```

### 使用示例

#### 基础使用

```vue
<template>
  <InventorySummary />
</template>

<script setup lang="ts">
import InventorySummary from '@/views/report/warehouse/InventorySummary.vue'
</script>
```

#### 路由配置

```typescript
{
  path: '/report/warehouse/inventory-summary',
  name: 'InventorySummary',
  meta: {
    label: '商品库存余额表'
  },
  component: () => import('@/views/report/warehouse/InventorySummary.vue')
}
```

### 待完成事项（TODO）

1. **仓库选项加载**
   ```typescript
   // 需要根据实际的 store 结构调整
   function initWarehouseOptions() {
     import { useCommonStore } from '@/stores/common'
     const commonStore = useCommonStore()
     
     if (searchConfig.customFields) {
       const warehouseField = searchConfig.customFields.find(f => f.key === 'warehouse')
       if (warehouseField && commonStore.warehouseList) {
         warehouseField.options = commonStore.warehouseList.map(w => ({
           label: w.name,
           value: w.id
         }))
       }
     }
   }
   ```

2. **API 地址配置**
   - 需要根据实际的后端 API 地址修改 `axios.post('wrf/wbs', params)`
   - 需要根据实际的导出 API 地址修改导出功能中的 URL

3. **环境变量配置**
   - 确保 `.env` 文件中配置了 `VITE_API_BASE_URL`

### 技术栈

- **Vue 3** - Composition API
- **TypeScript** - 类型安全
- **Element Plus** - UI 组件库
- **GoodSearchForm** - 自定义搜索组件
- **Axios** - HTTP 请求

### 注意事项

1. **数据格式化**
   - 所有数字字段都会格式化为两位小数
   - 空值或 undefined 会显示为 `-`

2. **性能优化**
   - 表格使用虚拟滚动（通过 height 属性）
   - 动态列使用 `v-for` + `:key` 优化渲染

3. **仓库多选**
   - 仓库字段支持多选
   - 导出时会将数组转换为多个参数

4. **日期初始化**
   - 默认查询当天的库存数据
   - 刷新后会重置为当天日期

### 常见问题

**Q: 为什么仓库列表没有数据？**
A: 需要在 `initWarehouseOptions` 函数中从 store 或 API 加载仓库列表。

**Q: 导出功能报错？**
A: 检查导出 API 地址是否正确，以及环境变量 `VITE_API_BASE_URL` 是否配置。

**Q: 表格列显示不全？**
A: 检查后端返回的 `column` 数据是否正确，以及表格宽度是否足够。

**Q: 数字显示异常？**
A: 检查 `formatNumber` 函数，确保传入的值是数字类型。

### 从 Vue 2 迁移的变化

| Vue 2 | Vue 3 | 说明 |
|-------|-------|------|
| `data()` | `ref()` / `reactive()` | 响应式数据定义 |
| `this.$refs` | `ref` 变量 | 模板引用 |
| `this.$store.state` | `useStore()` | Pinia 替代 Vuex |
| `this.$axios` | `import axios` | 直接导入 axios |
| `this.$moment()` | 原生 Date | 使用原生日期处理 |
| `.sync` | `v-model:xxx` | 双向绑定语法 |
| `$bus.emit` | `emit()` | 事件触发 |
| 选项式 API | 组合式 API | 代码组织方式 |

### 版本历史

- **v1.0.0** (2024-10-29) - 初始版本，基于 Vue 3 + TypeScript 重构

