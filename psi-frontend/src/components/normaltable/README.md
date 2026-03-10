# NormalTable 表格组件使用文档

## 组件概述

NormalTable 是一个基于 Element Plus Table 组件封装的增强型表格组件，主要实现了分页、多选、操作栏、自定义列等功能的封装。**封装的主要思路是通过数据驱动表格渲染，减少重复的界面代码编写和繁琐的交互处理逻辑**。

## 快速开始

### 1. 基础使用

#### 1.1 导入组件

```html
<script setup lang="ts">
  // 引入组件和类型
  import NormalTable from '@/components/normal-table/NormalTable.vue'
  import { createPageDTO, type PageDTO, type MyTableColumn } from '@/components/normal-table/type'
</script>
```

#### 1.2 定义必需属性

**表格列数据模型**：

```typescript
export interface MyTableColumn {
  /** 列标题 */
  label: string
  /** 列字段名 */
  prop: string
  /** 列宽度，不指定自适应宽度 */
  width?: string
  /** 最小列宽度 */
  'min-width'?: number
  /** 是否固定列 */
  fixed?: 'left' | 'right'
  /** 当内容过长被隐藏时显示tooltip */
  'show-overflow-tooltip'?: boolean
  /** 列对齐方式 */
  align?: 'left' | 'center' | 'right'
  /** 自定义格式化函数 */
  formatter?: (row: any) => string
}
```

**表格数据模型**：

```typescript
export interface PageDTO<T> {
  /** 页码 */
  pageIndex: number
  /** 每页数据条数 */
  pageSize: number
  /** 数据总条数 */
  total: number
  /** 数据总页数 */
  pages?: number
  /** 当前页数据 */
  rows?: Array<T>
}
```

**示例代码**：

```html
<script setup lang="ts">
  import { ref } from 'vue'
  import NormalTable from '@/components/normal-table/NormalTable.vue'
  import { createPageDTO, type PageDTO, type MyTableColumn } from '@/components/normal-table/type'

  // 业务数据模型
  interface SampleData {
    id?: number
    name?: string
    date?: string
    state?: string
    city?: string
    address?: string
    zip?: number
  }

  // 定义列数据
  const tabdatacolumns: MyTableColumn[] = [
    {
      prop: 'name',
      label: '姓名',
      width: '80px',
      fixed: 'left'
    },
    {
      prop: 'date',
      label: '生日',
      width: '200px',
      align: 'center'
    },
    {
      prop: 'state',
      label: '省份',
      width: '150px'
    },
    {
      prop: 'address',
      label: '详细地址',
      width: '600px',
      'show-overflow-tooltip': true
    }
  ]

  // 定义表格数据
  const tabdata = ref<PageDTO<SampleData>>(createPageDTO())
</script>
```

#### 1.3 定义必需事件

```html
<script setup lang="ts">
  import { ref, onMounted } from 'vue'
  import { listall } from '@/apis/sample/index' // 你的API接口

  // 省略属性定义...

  /**
   * 加载数据
   */
  function loadData() {
    listall(
      {
        pageIndex: tabdata.value.pageIndex,
        pageSize: tabdata.value.pageSize
      },
      (data) => {
        tabdata.value = data
      },
      (error: any) => {
        console.log(error)
      }
    )
  }

  /**
   * 分页组件改变触发数据刷新
   */
  function handlePageChange(data: PageDTO<SampleData>) {
    tabdata.value.pageIndex = data.pageIndex
    tabdata.value.pageSize = data.pageSize
    loadData()
  }

  onMounted(() => {
    loadData()
  })
</script>
```

#### 1.4 编写组件标签

```html
<template>
  <normal-table
    :tabdatacolumns="tabdatacolumns"
    :tabdata="tabdata"
    @page-change="handlePageChange"
  />
</template>
```

## 扩展属性配置

### 2.1 表格标题

```html
<normal-table tabtitle="用户列表" ... />
```

### 2.2 表格高度控制

新增了灵活的高度控制属性：

```html
<script setup lang="ts">
  // 使用默认高度（calc(100vh - 205px)）
  <normal-table ... />

  // 自定义固定高度
  <normal-table height="500px" ... />

  // 使用数字高度（自动转换为px）
  <normal-table :height="600" ... />

  // 设置最小和最大高度
  <normal-table
    height="auto"
    min-height="400px"
    max-height="800px"
    ...
  />

  // 在弹窗中使用较小高度
  <normal-table
    height="400px"
    ...
  />
</script>
```

### 2.3 表格属性

```typescript
export interface MyTableAttr {
  /** 分页组件大小, 默认default */
  psize?: 'large' | 'default' | 'small'
  /** 表格高度，不指定自动适配表格高度 */
  height?: number | string
  /** 表格最大高度 */
  'max-height'?: number | string
  /** 是否显示斑马纹, 默认显示 */
  stripe?: boolean
  /** 是否显示纵向边框, 默认不显示 */
  border?: boolean
  /** 是否高亮当前选择行, 默认高亮 */
  'highlight-current-row'?: boolean
  /** 是否在表尾显示合计行, 默认不显示 */
  'show-summary'?: boolean
  // ... 更多属性
}
```

**使用示例**：

```html
<script setup lang="ts">
  import type { MyTableAttr } from '@/components/normal-table/type'

  const tabattr: MyTableAttr = {
    height: 400,
    'max-height': 600,
    stripe: true,
    border: true
  }
</script>

<template>
  <normal-table :tabattr="tabattr" ... />
</template>
```

### 2.4 功能属性

```html
<normal-table
  :istabseq="true"        <!-- 显示序号列 -->
  :istabmultiple="true"   <!-- 开启多选 -->
  :istabpage="true"       <!-- 开启分页 -->
  :istabexpand="false"    <!-- 开启展开行 -->
  ...
/>
```

## 高级功能

### 3. 数据格式化功能

新增了 `formatter` 属性，支持对单元格数据进行格式化处理：

#### 3.1 基础格式化使用

```typescript
const columns: MyTableColumn[] = [
  {
    prop: 'time',
    label: '操作时间',
    align: 'center',
    minWidth: '150',
    formatter: (row: any) => {
      if (!row.time) return '-'
      const date = new Date(row.time)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    }
  },
  {
    prop: 'status',
    label: '状态',
    align: 'center',
    width: '100',
    formatter: (row: any) => {
      const statusMap = {
        '1': '启用',
        '0': '禁用',
        '2': '待审核'
      }
      return statusMap[row.status] || '未知'
    }
  },
  {
    prop: 'amount',
    label: '金额',
    align: 'right',
    width: '120',
    formatter: (row: any) => {
      return `¥${(row.amount || 0).toFixed(2)}`
    }
  }
]
```

#### 3.2 复杂数据格式化

```typescript
const columns: MyTableColumn[] = [
  {
    prop: 'costData.frameData.name',
    label: '所属组织',
    align: 'center',
    minWidth: '120',
    formatter: (row: any) => {
      return row.costData?.frameData?.name || '-'
    }
  },
  {
    prop: 'type',
    label: '单据类型',
    align: 'center',
    minWidth: '120',
    formatter: (row: any) => {
      const typeMap: Record<string, string> = {
        buy: '采购单',
        bre: '采购退货单',
        sell: '销售单',
        sre: '销售退货单',
        swapOut: '调拨单-出',
        swapEnter: '调拨单-入',
        entry: '其它入库单',
        extry: '其它出库单'
      }
      return typeMap[row.type || ''] || row.type || '-'
    }
  },
  {
    prop: 'direction',
    label: '操作类型',
    align: 'center',
    minWidth: '100',
    formatter: (row: any) => {
      return row.direction === 1 ? '入库' : '出库'
    }
  },
  {
    prop: 'nums',
    label: '操作数量',
    align: 'center',
    minWidth: '100',
    formatter: (row: any) => {
      const num = row.nums || 0
      const direction = row.direction
      const prefix = direction === 1 ? '+' : '-'
      return `${prefix}${num}`
    }
  }
]
```

### 4. 多级表头支持

通过 `columns` 插槽定义多级表头：

```html
<template>
  <normal-table ...>
    <template #columns>
      <el-table-column prop="name" label="商品名称" width="160px" fixed="left" />
      <el-table-column prop="totalStock" label="库存数量" width="120px" />
      <el-table-column label="仓库">
        <el-table-column prop="warehouse_001.stockNum" label="主仓库" width="90px" />
        <el-table-column prop="warehouse_002.stockNum" label="分仓A" width="90px" />
        <el-table-column prop="warehouse_003.stockNum" label="分仓B" width="90px" />
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="100px" fixed="right">
        <template #default="scope">
          <el-button @click="showDetail(scope.row)" size="mini">详情</el-button>
        </template>
      </el-table-column>
    </template>
  </normal-table>
</template>
```

### 5. 自定义单元格内容

通过 `customercell` 插槽自定义单元格内容（与 formatter 共存）：

```html
<template>
  <normal-table ...>
    <template #customercell="{ prop, row, column, index }">
      <!-- 对标签列特殊显示处理 -->
      <template v-if="prop === 'tag'">
        <el-tag :type="row[prop] === '成功' ? 'success' : 'danger'"> {{ row[prop] }} </el-tag>
      </template>

      <!-- 对状态列特殊处理 -->
      <template v-else-if="prop === 'status'">
        <el-icon v-if="row[prop] === '1'"><Check /></el-icon>
        <el-icon v-else><Close /></el-icon>
      </template>

      <!-- 对嵌套属性处理 -->
      <template v-else-if="prop === 'categoryData.name'">
        {{ row.categoryData?.name || '-' }}
      </template>

      <!-- 仓库库存特殊显示 -->
      <template v-else-if="prop.startsWith('warehouse_') && prop.endsWith('.stockNum')">
        <template v-if="row.attrs && row.attrs.length > 0">
          <span>-</span>
        </template>
        <template v-else> {{ row[prop] }} </template>
      </template>
    </template>
  </normal-table>
</template>
```

**注意**：`formatter` 和 `customercell` 插槽可以同时使用，`formatter` 用于简单的数据转换，`customercell` 用于复杂的 UI 渲染。

### 6. 多选功能

```html
<template>
  <normal-table :istabmultiple="true" @selection-change="handleSelectionChange" ... />
</template>

<script setup lang="ts">
  /**
   * 选择行数据发生变化事件处理
   */
  function handleSelectionChange(rows: SampleData[]) {
    console.log('选中行数据:', rows)
    // 执行批量操作逻辑
  }
</script>
```

### 7. 自定义页眉页脚

#### 7.1 自定义页眉

```html
<template>
  <normal-table ...>
    <template #header="{ table, props }">
      <el-input
        style="width: 200px"
        prefix-icon="Search"
        placeholder="输入搜索关键词"
        v-model="keywords"
      />
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="handleReset">重置</el-button>
    </template>
  </normal-table>
</template>
```

#### 7.2 自定义页脚

```html
<template>
  <normal-table ...>
    <template #footer="{ table, props }">
      <el-button type="primary" @click="handleSelectAll(table)">
        {{ isSelectAll ? '反选' : '全选' }}
      </el-button>
      <el-button @click="handleExport">导出数据</el-button>
      <span>已选择: {{ selectedCount }} 条</span>
    </template>
  </normal-table>
</template>

<script setup lang="ts">
  const isSelectAll = ref(false)
  const selectedCount = ref(0)

  function handleSelectAll(table: any) {
    table?.toggleAllSelection()
    isSelectAll.value = !isSelectAll.value
  }

  function handleExport() {
    // 导出逻辑
  }
</script>
```

### 8. 展开行功能

```html
<template>
  <normal-table :istabexpand="true" ...>
    <template #customerexpand="{ row }">
      <div style="padding: 20px; background: #f5f7fa;">
        <h4>详细信息</h4>
        <p>姓名: {{ row.name }}</p>
        <p>地址: {{ row.address }}</p>
        <p>创建时间: {{ row.createTime }}</p>
        <!-- 更多详细信息 -->
      </div>
    </template>
  </normal-table>
</template>
```

### 9. 树形表格

```html
<script setup lang="ts">
  const tabattr: MyTableAttr = {
    'row-key': 'id',
    'tree-props': {
      children: 'children',
      hasChildren: 'hasChildren'
    },
    lazy: true
  }
</script>

<template>
  <normal-table :tabattr="tabattr" ... />
</template>
```

## 组件方法

通过 `ref` 可以调用组件暴露的方法：

```html
<template>
  <normal-table ref="tableRef" ... />
</template>

<script setup lang="ts">
  import { ref } from 'vue'
  import type { TableInstance } from 'element-plus'

  const tableRef = ref<TableInstance>()

  // 获取表格数据
  const tableData = tableRef.value?.tableData()

  // 清除选择
  tableRef.value?.clearSelection()

  // 获取选中行
  const selectedRows = tableRef.value?.getSelectionRows()

  // 设置当前行
  tableRef.value?.setCurrentRow(someRow)

  // 切换行展开状态
  tableRef.value?.toggleRowExpansion(someRow, true)
</script>
```

## 样式定制

组件支持通过 CSS 变量和深度选择器进行样式定制：

```css
/* 修改表格行高度 */
:deep(.el-table .el-table__row) {
  height: 50px;
}

/* 修改表头样式 */
:deep(.el-table .el-table__header) {
  background-color: #f8f9fa;
}

/* 修改斑马纹颜色 */
:deep(.el-table .el-table__row--striped) {
  background-color: #fafafa;
}

/* 自定义表格容器样式 */
.table-container {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}
```

## 注意事项

1. **数据更新**：表格数据更新需要通过 `@page-change` 事件或直接更新 `tabdata` 来实现
2. **列定义**：操作列的 `prop` 必须为 `'operate'`
3. **树形表格**：使用树形表格时必须指定 `row-key` 属性
4. **性能优化**：大数据量时建议开启虚拟滚动或分页
5. **高度控制**：默认高度为 `calc(100vh - 205px)`，可根据需要自定义
6. **格式化优先级**：`formatter` 函数优先于 `customercell` 插槽，建议根据复杂度选择使用

## 完整示例

```html
<template>
  <normal-table
    tabtitle="库存详情"
    :istabseq="true"
    :istabpage="true"
    :height="400"
    :tabdatacolumns="columns"
    :tabdata="tableData"
    @page-change="handlePageChange"
  >
    <template #footer>
      <el-button @click="handleExport" type="primary">导出数据</el-button>
    </template>
  </normal-table>
</template>

<script setup lang="ts">
  import { ref } from 'vue'
  import NormalTable from '@/components/normal-table/NormalTable.vue'
  import type { PageDTO, MyTableColumn } from '@/components/normal-table/type'
  import { formatDateTime } from '@/utils/time'

  const columns: MyTableColumn[] = [
    {
      prop: 'name',
      label: '所属组织',
      align: 'center',
      minWidth: '120',
      formatter: (row: any) => {
        return row.costData?.frameData?.name || '-'
      }
    },
    {
      prop: 'time',
      label: '操作时间',
      align: 'center',
      minWidth: '150',
      formatter: (row: any) => {
        return formatDateTime(row.time)
      }
    },
    {
      prop: 'type',
      label: '单据类型',
      align: 'center',
      minWidth: '120',
      formatter: (row: any) => {
        const typeMap = {
          buy: '采购单',
          sell: '销售单',
          entry: '入库单',
          extry: '出库单'
        }
        return typeMap[row.type] || row.type || '-'
      }
    },
    {
      prop: 'nums',
      label: '操作数量',
      align: 'center',
      minWidth: '100',
      formatter: (row: any) => {
        const num = row.nums || 0
        const direction = row.direction
        const prefix = direction === 1 ? '+' : '-'
        return `${prefix}${num}`
      }
    }
  ]

  const tableData = ref<PageDTO<any>>({
    rows: [],
    total: 0,
    pageIndex: 1,
    pageSize: 20
  })

  function handlePageChange(data: PageDTO<any>) {
    tableData.value.pageIndex = data.pageIndex
    tableData.value.pageSize = data.pageSize
    loadData()
  }

  function handleExport() {
    // 导出逻辑
  }
</script>
```

## 更新日志

### v2.1.0

- **新增**：`formatter` 格式化函数支持，简化数据展示逻辑
- **新增**：灵活的高度控制属性：`height`、`minHeight`、`maxHeight`
- **增强**：支持多级表头渲染
- **优化**：样式和布局改进

### v2.0.0

- 基础表格功能封装
- 分页、多选、操作栏支持
- 自定义插槽支持
