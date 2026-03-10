# 操作按钮组件使用说明

主要将常用的操作按钮（新增、批量操作、导出、刷新）封装到组件中，以及集成批量导入导出功能，方便实现常规列表数据的增删改查操作。

操作按钮组件会暴露如下函数给组件实例使用

- `openAddDialog`：用来打开新增对话框
- `openBatchDialog`：用来打开批量操作对话框
- `exportData`：用来导出当前数据
- `onRefresh`：用来刷新页面数据

**封装的主要思路就是通过配置驱动按钮的渲染，不需要再编写复杂的界面代码，以及繁琐的交互处理逻辑**，组件演示代码在sample中已经书写，可以自己运行观察效果。

## 1 操作按钮组件

### 1.1 导入组件

导入组件关键代码如下

```html
<script setup lang="ts">
// 导入组件和类型
import OperationButtons from '@/components/operationbuttons/OperationButtons.vue'
// 表格列配置类型（用于模板生成）
import type { TableColumn } from '@/components/operationbuttons/type'
</script>
```

### 1.2 定义属性

必须定义的属性包括：表格列配置（`tableColumns`），其他属性属于可选属性，具体属性模型定义如下：

```js
/**
 * 操作按钮组件属性模型
 */
export interface OperationButtonsProps {
  /** 是否显示新增按钮，默认true */
  showAdd?: boolean
  /** 是否显示批量操作按钮，默认true */
  showBatch?: boolean
  /** 是否显示导出按钮，默认true */
  showExport?: boolean
  /** 是否显示刷新按钮，默认true */
  showRefresh?: boolean
  /** 快捷配置按钮组合，默认'all' */
  buttonConfig?: 'all' | 'basic' | 'data' | 'custom'
  /** 表格列配置，用于生成导入模板 */
  tableColumns?: TableColumn[]
  /** 导出数据，如果不提供则使用示例数据 */
  exportData?: any[]
  /** 模板文件名，默认"数据导入模板" */
  templateFileName?: string
}

/**
 * 表格列配置模型
 */
export interface TableColumn {
  /** 列属性名 */
  prop: string
  /** 列显示名称 */
  label: string
  /** 列类型，用于生成示例数据 */
  type?: 'text' | 'number' | 'email' | 'phone' | 'date' | 'select' | 'textarea'
  /** 列宽度 */
  width?: number
  /** 是否必填 */
  required?: boolean
  /** 选择项配置（当type为select时使用） */
  options?: Array<{ label: string; value: any; disabled?: boolean }>
}
```

下面是定义组件属性的示例代码

```html
<script setup lang="ts">
// 定义表格列配置
const tableColumns = reactive<TableColumn[]>([
  {
    prop: 'id',
    label: 'ID',
    type: 'number',
    width: 80,
    required: true
  },
  {
    prop: 'name',
    label: '姓名',
    type: 'text',
    width: 120,
    required: true
  },
  {
    prop: 'email',
    label: '邮箱',
    type: 'email',
    width: 200
  },
  {
    prop: 'phone',
    label: '电话',
    type: 'phone',
    width: 150
  },
  {
    prop: 'department',
    label: '部门',
    type: 'select',
    width: 120,
    options: [
      { label: '技术部', value: 'tech' },
      { label: '销售部', value: 'sales' },
      { label: '人事部', value: 'hr' }
    ]
  },
  {
    prop: 'status',
    label: '状态',
    type: 'select',
    width: 100,
    options: [
      { label: '启用', value: 'active' },
      { label: '禁用', value: 'inactive' }
    ]
  },
  {
    prop: 'createTime',
    label: '创建时间',
    type: 'date',
    width: 150
  },
  {
    prop: 'remark',
    label: '备注',
    type: 'textarea',
    width: 200
  }
])

// 定义表格数据
const tableData = reactive([
  // 你的表格数据
])

// 定义操作按钮属性
const operationProps = reactive<OperationButtonsProps>({
  showAdd: true,
  showBatch: true,
  showRefresh: true,
  tableColumns: tableColumns,
  exportData: tableData,
  templateFileName: '用户数据导入模板'
})
</script>
```

关于组件上数据绑定直接用`:`绑定就行了，如`:="operationProps"`

***TIPS：操作按钮属性大部分是可选属性，根据你实际需求指定属性即可***

### 1.3 定义事件

定义的事件包括：

- @refresh：刷新按钮执行事件
- @import-data：批量导入数据完成事件

下面是事件处理逻辑函数示例

```html
<script setup lang="ts">
/**
 * 刷新数据
 */
function handleRefresh() {
  // 执行刷新逻辑
  console.log('刷新数据')
  // 可以调用API重新获取数据
  // fetchData()
}

/**
 * 导入数据完成回调
 * @param data 导入的数据
 */
function handleImportData(data: any[]) {
  // 处理导入的数据
  console.log('导入数据:', data)
  // 可以将数据添加到表格中
  // tableData.push(...data)
}
</script>
```

关于组件上事件绑定：

- 通过`@refresh`绑定刷新事件，如：`@refresh="handleRefresh"`
- 通过`@import-data`绑定导入事件，如：`@import-data="handleImportData"`

### 1.4 编写组件标签

在template中编写组件标签，并绑定数据和事件，示例代码如下

```html
<template>
  <div>
    <!-- 表格组件 -->
    <el-table :data="tableData" :height="400">
      <el-table-column 
        v-for="col in tableColumns" 
        :key="col.prop"
        :prop="col.prop" 
        :label="col.label" 
        :width="col.width"
      />
    </el-table>
    
    <!-- 操作按钮 -->
    <OperationButtons
      :="operationProps"
      @refresh="handleRefresh"
      @import-data="handleImportData"
    />
  </div>
</template>
```

***TIPS：以上是操作按钮组件的基本使用步骤，前面的操作基本上可以完成大部分业务需求了***

## 2 按钮显示控制

### 2.1 基本按钮控制属性

组件提供了4个独立的布尔属性来控制每个按钮的显示：

```vue
<template>
  <OperationButtons
    :show-add="true"        <!-- 显示新增按钮 -->
    :show-batch="false"     <!-- 隐藏批量按钮 -->
    :show-export="true"     <!-- 显示导出按钮 -->
    :show-refresh="false"   <!-- 隐藏刷新按钮 -->
    :table-columns="tableColumns"
  />
</template>
```

### 2.2 快捷配置模式

除了单独控制每个按钮，还提供了4种预设的快捷配置：

#### 2.2.1 `all` - 显示所有按钮（默认）
```vue
<OperationButtons 
  button-config="all"
  :table-columns="tableColumns"
/>
```

#### 2.2.2 `basic` - 只显示基础操作按钮
```vue
<OperationButtons 
  button-config="basic"
  :table-columns="tableColumns"
/>
<!-- 等同于：只显示新增和刷新按钮 -->
```

#### 2.2.3 `data` - 只显示数据处理按钮
```vue
<OperationButtons 
  button-config="data"
  :table-columns="tableColumns"
/>
<!-- 等同于：只显示批量、导出和刷新按钮 -->
```

#### 2.2.4 `custom` - 自定义模式
```vue
<OperationButtons 
  button-config="custom"
  :show-add="true"
  :show-batch="false"
  :show-export="true"
  :show-refresh="true"
  :table-columns="tableColumns"
/>
```

### 2.3 实际使用示例

#### 示例1：列表页面（显示所有按钮）
```vue
<template>
  <div>
    <OperationButtons 
      button-config="all"
      :table-columns="columns"
      :export-data="tableData"
      @refresh="handleRefresh"
    />
  </div>
</template>

<script setup>
const columns = [
  { prop: 'id', label: 'ID', type: 'number' },
  { prop: 'name', label: '姓名', type: 'text' },
  { prop: 'email', label: '邮箱', type: 'email' }
]
</script>
```

#### 示例2：只读页面（只显示刷新按钮）
```vue
<template>
  <div>
    <OperationButtons 
      button-config="custom"
      :show-add="false"
      :show-batch="false"
      :show-export="false"
      :show-refresh="true"
      @refresh="handleRefresh"
    />
  </div>
</template>
```

#### 示例3：数据管理页面（只显示数据处理相关按钮）
```vue
<template>
  <div>
    <OperationButtons 
      button-config="data"
      :table-columns="columns"
      :export-data="tableData"
      @refresh="handleRefresh"
      @import-data="handleImport"
    />
  </div>
</template>
```

### 2.4 优先级说明

- **快捷配置优先**：如果设置了 `button-config`，会按照预设模式控制按钮显示
- **单独属性覆盖**：在 `custom` 模式下，单独的 `show*` 属性会生效
- **默认值**：所有按钮默认都显示（`show*` 默认为 `true`）

### 2.5 动态控制

您也可以根据业务逻辑动态控制按钮显示：

```vue
<template>
  <OperationButtons 
    :show-add="userRole === 'admin'"
    :show-batch="hasPermission"
    :show-export="tableData.length > 0"
    :show-refresh="true"
    :table-columns="columns"
  />
</template>

<script setup>
import { ref, computed } from 'vue'

const userRole = ref('admin')
const hasPermission = computed(() => userRole.value === 'admin')
const tableData = ref([])
</script>
```

## 3 导出按钮功能

### 3.1 功能说明

导出按钮提供了直接导出当前数据的功能，与批量操作弹窗中的"导出数据"标签页功能完全一致。

- **直接导出**：点击按钮直接导出数据，无需打开弹窗
- **数据来源**：使用 `exportData` prop 传入的数据
- **文件格式**：导出为 Excel (.xlsx) 格式
- **文件命名**：使用 `exportFileName` prop 指定的文件名

### 3.2 使用方式

导出按钮通过 `showExport` prop 控制显示，默认为 `true`。当点击按钮时，会直接调用 `exportData()` 函数进行数据导出。

### 3.3 配置选项

```js
const operationProps = {
  showExport: true,                    // 显示导出按钮
  exportData: tableData,              // 要导出的数据
  exportFileName: '用户数据导出'        // 导出文件名
}
```

## 4 新增对话框

### 4.1 功能说明

新增对话框（`AddDialog.vue`）提供了完整的客户信息录入表单，包含多个标签页：

- **基础资料**：客户名称、编号、组织、用户、类别、等级等基础信息
- **联系资料**：联系人、电话、地址、邮箱等联系方式
- **其他资料**：备注、状态等其他信息

### 4.2 使用方式

新增对话框通过操作按钮组件自动调用，无需单独使用。当点击"新增"按钮时，会自动打开新增对话框。

### 4.3 表单验证

新增对话框内置了完整的表单验证规则：

- 必填字段验证
- 格式验证（邮箱、电话等）
- 长度限制验证

## 5 联系人对话框

### 5.1 功能说明

联系人对话框（`ContactDialog.vue`）用于录入联系人信息，包含以下字段：

- 主联系人（开关）
- 联系人姓名
- 联系电话
- 联系地址
- 备注信息

### 5.2 使用方式

联系人对话框通过新增对话框中的"联系资料"标签页调用，用于添加多个联系人信息。

## 6 批量操作功能

### 6.1 导入数据

批量操作弹窗提供了强大的数据导入功能：

#### 6.1.1 下载模板

- 根据表格列配置自动生成Excel模板
- 包含示例数据和格式说明
- 支持自定义模板文件名

#### 6.1.2 上传模板

- 支持.xlsx和.xls格式文件
- 自动解析Excel数据
- 提供数据预览功能
- 智能匹配列名和数据

#### 6.1.3 数据预览

- 显示上传数据的前5条记录
- 显示总记录数
- 提供确认导入和取消操作

### 6.2 导出数据

- 将当前表格数据导出为Excel文件
- 保持原有的列格式和样式
- 支持自定义文件名

### 6.3 使用提示

批量操作弹窗提供了详细的使用说明：

1. 该功能适用于批量导入数据
2. 需要下载数据模板后使用Excel录入数据
3. 录入数据时，请勿修改首行数据标题以及排序
4. 请查阅使用文档获取字段格式内容以及相关导入须知
5. 点击下方上传模板，选择您编辑好的模板文件即可

## 7 组件配置

### 7.1 按钮显示控制

```js
const operationProps = {
  showAdd: true,      // 显示新增按钮
  showBatch: true,    // 显示批量操作按钮
  showExport: true,   // 显示导出按钮
  showRefresh: true   // 显示刷新按钮
}
```

### 7.2 表格列配置

```js
const tableColumns = [
  {
    prop: 'name',           // 列属性名
    label: '姓名',          // 列显示名称
    type: 'text',           // 列类型
    width: 120,             // 列宽度
    required: true,         // 是否必填
    options: [              // 选择项（type为select时使用）
      { label: '选项1', value: 'option1' },
      { label: '选项2', value: 'option2' }
    ]
  }
]
```

### 7.3 导出数据配置

```js
const operationProps = {
  exportData: tableData,                    // 导出数据
  templateFileName: '用户数据导入模板'        // 模板文件名
}
```

## 8 样式特点

### 8.1 响应式布局

- 使用 `flex gap-2 justify-end` 实现右对齐排列
- 按钮间距统一，视觉效果良好

### 8.2 按钮类型

- **新增按钮**：`type="primary"` (蓝色主按钮)
- **批量按钮**：`type="warning"` (橙色警告按钮)
- **导出按钮**：`type="success"` (绿色成功按钮)
- **刷新按钮**：`type="info"` (灰色信息按钮)

### 8.3 条件显示

- 每个按钮都有独立的显示控制
- 可以根据业务需求灵活配置

## 9 注意事项

1. **表格列配置**：确保 `tableColumns` 配置正确，这将影响模板生成和数据导入
2. **数据格式**：导入的Excel文件格式需要与模板格式一致
3. **文件大小**：建议单次导入数据不超过10000条，避免性能问题
4. **浏览器兼容**：建议使用现代浏览器，确保Excel解析功能正常工作
5. **权限控制**：根据用户权限控制按钮的显示和功能

## 10 常见问题

### 10.1 模板下载失败

**问题**：点击下载模板按钮没有反应
**解决**：检查浏览器是否阻止了文件下载，允许下载即可

### 10.2 数据导入失败

**问题**：上传Excel文件后无法导入
**解决**：
- 检查文件格式是否为.xlsx或.xls
- 确认文件内容与模板格式一致
- 检查必填字段是否完整

### 10.3 导出数据为空

**问题**：点击导出按钮生成的文件为空
**解决**：检查 `exportData` 属性是否正确传入数据

### 10.4 按钮不显示

**问题**：某些按钮没有显示
**解决**：检查对应的 `show*` 属性是否设置为 `true`

---

**TIPS：操作按钮组件设计得非常灵活，可以根据不同页面的需求选择性显示按钮，并且批量操作功能特别强大，支持智能模板生成和数据导入导出！** 🚀