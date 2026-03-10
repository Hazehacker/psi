# GoodSearchForm 商品搜索表单组件

一个通用的商品/单据搜索表单组件，支持多种搜索条件配置和自定义字段扩展。

## 特性

- 🔍 支持多种搜索条件：商品名称、单据编号、供应商、客户、人员、用户、日期范围等
- 🎛️ 灵活的配置项，可显示/隐藏任意字段
- 🔧 支持自定义字段扩展
- 📅 自动初始化默认日期范围
- 💾 支持 v-model 双向绑定
- 📱 响应式设计，适配不同屏幕
- 🎨 **两种显示模式**：Popover 弹窗模式 / Inline 内联模式
- 🔗 **高级搜索对话框**：供应商、客户、人员、用户等字段支持弹出专用搜索组件
- 🎯 **专用搜索组件**：Supplier、Customer、UserList、PeopleList、Warehouse、Account 等
- 🔒 **手动关闭**：Popover 模式支持手动关闭，不会因为打开子对话框而意外关闭

## 组件导出

```typescript
// 主组件
export { GoodSearchForm } from './index'

// 专用搜索组件
export { 
  Supplier,      // 供应商搜索
  Customer,      // 客户搜索
  UserList,      // 用户搜索
  PeopleList,    // 人员搜索
  Warehouse,     // 仓库搜索
  Account        // 账户搜索
} from './index'

// 类型定义
export type { 
  SearchFormData, 
  GoodSearchConfig, 
  CustomField 
} from './index'
```
- 支持多种搜索条件：商品名称、单据编号、供应商、人员、日期范围等
- 灵活的配置项，可显示/隐藏任意字段
- 支持自定义字段扩展（输入框、下拉选择、日期选择、树形选择等）
- 自动初始化默认日期范围
- 支持 v-model 双向绑定
- 响应式设计，适配不同屏幕
- 支持树形选择器，可选择任意级别
- 支持多选下拉框，可折叠标签显示

## 基础用法

### 1. 简单使用（Popover 模式）

```vue
<template>
  <GoodSearchForm v-model="searchForm" @search="handleSearch" />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import GoodSearchForm from '@/components/goodSearchConpoent'
import type { SearchFormData } from '@/components/goodSearchConpoent'

const searchForm = ref<SearchFormData>({})

const handleSearch = (formData: SearchFormData) => {
  console.log('搜索参数:', formData)
  // 执行搜索逻辑
}
</script>
```

**特点**：
- 显示为"三个点"按钮，点击后弹出搜索表单
- 支持手动关闭（点击"关闭"按钮或再次点击"三个点"）
- 搜索后不会自动关闭弹窗
- 打开子对话框（如供应商搜索）不会关闭主弹窗

### 2. Inline 内联模式

```vue
<template>
  <GoodSearchForm 
    v-model="searchForm"
    :config="{ inline: true }"
    @search="handleSearch"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import GoodSearchForm from '@/components/goodSearchConpoent'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent'

const searchForm = ref<SearchFormData>({})

const searchConfig: GoodSearchConfig = {
  inline: true  // 启用内联模式
}

const handleSearch = (formData: SearchFormData) => {
  console.log('搜索参数:', formData)
}
</script>
```

**特点**：
- 直接显示表单，不需要点击按钮弹出
- 适合专用搜索页面或固定搜索区域
- 所有专用搜索组件（Supplier、Customer 等）都默认使用 inline 模式

### 3. 配置显示字段

```vue
<template>
  <GoodSearchForm v-model="searchForm" :config="searchConfig" @search="handleSearch" />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import GoodSearchForm from '@/components/goodSearchConpoent'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent'

const searchForm = ref<SearchFormData>({})

// 配置只显示部分字段
const searchConfig: GoodSearchConfig = {
  showGoods: true,
  showNumber: true,
  showSupplier: true,      // 点击弹出供应商高级搜索
  showCustomer: true,      // 点击弹出客户高级搜索
  showBillDate: true,
  showArrivalDate: false,  // 隐藏到货日期
  showUser: true,          // 显示制单人（点击弹出用户搜索）
  showPeople: true,        // 显示关联人员（点击弹出人员搜索）
  showExamine: true,
  showState: true,
  showRemark: true
}

const handleSearch = (formData: SearchFormData) => {
  console.log('搜索参数:', formData)
}
</script>
```

### 4. 添加自定义字段

```vue
<template>
  <GoodSearchForm v-model="searchForm" :config="searchConfig" @search="handleSearch" />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import GoodSearchForm from '@/components/goodSearchConpoent'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent'

const searchForm = ref<SearchFormData>({})

const searchConfig: GoodSearchConfig = {
  // 基础字段配置...
  showGoods: true,
  showNumber: true,

  // 自定义字段
  customFields: [
    {
      key: 'orderStatus',
      type: 'select',
      label: '订单状态',
      options: [
        { label: '待处理', value: 1 },
        { label: '处理中', value: 2 },
        { label: '已完成', value: 3 }
      ]
    },
    {
      key: 'customerId',
      type: 'nodList',
      label: '请选择客户',
      nodListConfig: {
        action: 'service/customerRecord',
        scene: 'customer'
      }
    },
    {
      key: 'deliveryDate',
      type: 'date',
      label: '发货日期'
    },
    {
      key: 'trackingNumber',
      type: 'input',
      label: '请输入快递单号'
    }
  ]
}

const handleSearch = (formData: SearchFormData) => {
  console.log('搜索参数:', formData)
  console.log('自定义字段值:', formData.orderStatus, formData.customerId)
}
</script>
```

### 5. 使用多选下拉框

```vue
<template>
  <GoodSearchForm v-model="searchForm" :config="searchConfig" @search="handleSearch" />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import GoodSearchForm from '@/components/goodSearchConpoent'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent'

const searchForm = ref<SearchFormData>({})

const searchConfig: GoodSearchConfig = {
  customFields: [
    {
      key: 'documentTypes',
      type: 'select',
      label: '请选择单据类型',
      multiple: true, // 启用多选
      collapseTags: true, // 启用标签折叠
      options: [
        { label: '采购单', value: 'buy' },
        { label: '销售单', value: 'sell' },
        { label: '调拨单', value: 'transfer' },
        { label: '退货单', value: 'return' }
      ]
    }
  ]
}

const handleSearch = (formData: SearchFormData) => {
  console.log('选择的多选值:', formData.documentTypes) // 数组格式
}
</script>
```

### 5. 使用树形选择器

```vue
<template>
  <GoodSearchForm v-model="searchForm" :config="searchConfig" @search="handleSearch" />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import GoodSearchForm from '@/components/goodSearchConpoent'
import type { SearchFormData, GoodSearchConfig, TreeData } from '@/components/goodSearchConpoent'

const searchForm = ref<SearchFormData>({})

// 树形数据
const categoryTreeData: TreeData[] = [
  {
    value: '1',
    label: '电子产品',
    children: [
      {
        value: '1-1',
        label: '手机',
        children: [
          { value: '1-1-1', label: '智能手机' },
          { value: '1-1-2', label: '功能手机' }
        ]
      },
      {
        value: '1-2',
        label: '电脑',
        children: [
          { value: '1-2-1', label: '笔记本电脑' },
          { value: '1-2-2', label: '台式电脑' }
        ]
      }
    ]
  },
  {
    value: '2',
    label: '服装',
    children: [
      {
        value: '2-1',
        label: '男装'
      },
      {
        value: '2-2',
        label: '女装'
      }
    ]
  }
]

const searchConfig: GoodSearchConfig = {
  customFields: [
    {
      key: 'category',
      type: 'treeSelect',
      label: '请选择商品分类',
      checkStrictly: true, // 可选择任意级别
      renderAfterExpand: false,
      clearable: true,
      treeData: categoryTreeData
    },
    {
      key: 'multiCategory',
      type: 'treeSelect',
      label: '请选择多个分类',
      multiple: true, // 多选模式
      checkStrictly: true,
      treeData: categoryTreeData
    }
  ]
}

const handleSearch = (formData: SearchFormData) => {
  console.log('选择的分类:', formData.category)
  console.log('选择的多个分类:', formData.multiCategory)
}
</script>
```

### 6. 设置默认日期范围

```vue
<template>
  <!-- 默认显示最近7天的数据 -->
  <GoodSearchForm v-model="searchForm" :default-days="7" @search="handleSearch" />
</template>
```

### 7. 在原有 BorForm 中使用

```vue
<template>
  <div class="borForm area">
    <div class="layout">
      <!-- 使用新的搜索组件替代原有的 el-popover + el-form -->
      <GoodSearchForm v-model="searchFrom" @search="record(1)" />

      <el-button-group>
        <!-- 其他按钮... -->
      </el-button-group>
    </div>
    <!-- 表格等其他内容... -->
  </div>
</template>

<script setup lang="ts">
const searchConfig: GoodSearchConfig = {
  showUser: true,
  userPlaceholder: '请选择所属用户'  // 自定义占位符文本
}
</script>
```

## 专用搜索组件

### 1. 供应商搜索组件（Supplier）

```vue
<template>
  <Supplier @search="handleSupplierSearch" />
</template>

<script setup lang="ts">
import { Supplier } from '@/components/goodSearchConpoent'

const handleSupplierSearch = (params: any) => {
  console.log('供应商搜索参数:', params)
  // params 包含: name, number, category, level, contacts, tel, user, data 等
}
</script>
```

**字段说明**：
- 供应商名称
- 供应商编号
- 供应商类别（下拉选择）
- 供应商等级（下拉选择）
- 联系人
- 联系电话
- 所属用户（点击弹出用户搜索）
- 备注信息

### 2. 客户搜索组件（Customer）

```vue
<template>
  <Customer @search="handleCustomerSearch" />
</template>

<script setup lang="ts">
import { Customer } from '@/components/goodSearchConpoent'

const handleCustomerSearch = (params: any) => {
  console.log('客户搜索参数:', params)
  // params 包含: name, number, category, level, contacts, tel, user, data 等
}
</script>
```

**字段说明**：
- 客户名称
- 客户编号
- 客户类别（下拉选择）
- 客户等级（下拉选择）
- 联系人
- 联系电话
- 所属用户（点击弹出用户搜索）
- 备注信息

### 3. 用户搜索组件（UserList）

```vue
<template>
  <UserList @search="handleUserSearch" />
</template>

<script setup lang="ts">
import { UserList } from '@/components/goodSearchConpoent'

const handleUserSearch = (params: any) => {
  console.log('用户搜索参数:', params)
  // params 包含: name, number, tel, data 等
}
</script>
```

**字段说明**：
- 用户名称
- 用户编号
- 联系电话
- 备注信息

### 4. 人员搜索组件（PeopleList）

```vue
<template>
  <PeopleList @search="handlePeopleSearch" />
</template>

<script setup lang="ts">
import { PeopleList } from '@/components/goodSearchConpoent'

const handlePeopleSearch = (params: any) => {
  console.log('人员搜索参数:', params)
  // params 包含: name, number, gender, tel, address, idCard, data 等
}
</script>
```

**字段说明**：
- 人员名称
- 人员编号
- 性别（下拉选择）
- 联系电话
- 联系地址
- 身份证号
- 备注信息

### 5. 仓库搜索组件（Warehouse）

```vue
<template>
  <Warehouse @search="handleWarehouseSearch" />
</template>

<script setup lang="ts">
import { Warehouse } from '@/components/goodSearchConpoent'

const handleWarehouseSearch = (params: any) => {
  console.log('仓库搜索参数:', params)
  // params 包含: id, name, number 等
}
</script>
```

**字段说明**：
- 仓库唯一标识
- 仓库名称
- 仓库编号

### 6. 账户搜索组件（Account）

```vue
<template>
  <Account @search="handleAccountSearch" />
</template>

<script setup lang="ts">
import { Account } from '@/components/goodSearchConpoent'

const handleAccountSearch = (params: any) => {
  console.log('账户搜索参数:', params)
  // params 包含: name, number, data 等
}
</script>
```

**字段说明**：
- 账户名称
- 账户编号
- 备注信息

## API

### Props

| 参数        | 说明                     | 类型               | 默认值 |
| ----------- | ------------------------ | ------------------ | ------ |
| modelValue  | 表单数据（支持 v-model） | `SearchFormData`   | `{}`   |
| config      | 配置项                   | `GoodSearchConfig` | `{}`   |
| defaultDays | 默认日期范围（天数）     | `number`           | `30`   |

### Events

| 事件名            | 说明               | 回调参数                     |
| ----------------- | ------------------ | ---------------------------- |
| search            | 点击搜索按钮时触发 | `(formData: SearchFormData)` |
| update:modelValue | 表单数据更新时触发 | `(formData: SearchFormData)` |

### Expose Methods

| 方法名    | 说明     | 参数 |
| --------- | -------- | ---- |
| resetForm | 重置表单 | -    |

使用示例：

```vue
<template>
  <GoodSearchForm ref="searchFormRef" />
  <el-button @click="reset">重置</el-button>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import GoodSearchForm from '@/components/goodSearchConpoent'

const searchFormRef = ref()

const reset = () => {
  searchFormRef.value?.resetForm()
}
</script>
```

## 类型定义

### SearchFormData

```typescript
interface SearchFormData {
  goods?: string // 商品名称
  number?: string // 单据编号
  supplier?: string | null // 供应商ID
  people?: string | null // 关联人员ID
  user?: string | null // 制单人ID
  startTime?: string // 单据开始日期
  endTime?: string // 单据结束日期
  startArrival?: string // 到货开始日期
  endArrival?: string // 到货结束日期
  examine?: number | string // 审核状态
  state?: number | string // 入库状态
  data?: string // 备注信息
  [key: string]: any // 其他自定义字段
}
```

### GoodSearchConfig

```typescript
interface GoodSearchConfig {
  // 显示模式
  inline?: boolean            // 是否使用内联模式（默认 false，使用 popover 模式）
  
  // 基础字段显示控制
  showGoods?: boolean         // 是否显示商品名称（默认 true）
  showNumber?: boolean        // 是否显示单据编号（默认 true）
  showSupplier?: boolean      // 是否显示供应商（默认 true，点击弹出高级搜索）
  showCustomer?: boolean      // 是否显示客户（默认 false，点击弹出高级搜索）
  showPeople?: boolean        // 是否显示关联人员（默认 true，点击弹出高级搜索）
  showBillDate?: boolean      // 是否显示单据日期（默认 true）
  showArrivalDate?: boolean   // 是否显示到货日期（默认 true）
  showUser?: boolean          // 是否显示制单人/所属用户（默认 true，点击弹出高级搜索）
  showExamine?: boolean       // 是否显示审核状态（默认 true）
  showState?: boolean         // 是否显示入库状态（默认 true）
  showRemark?: boolean        // 是否显示备注（默认 true）
  
  // 自定义配置
  userPlaceholder?: string    // 用户字段的占位符文本（默认"请选择制单人"）
  customFields?: CustomField[] // 自定义字段
  searchButton?: SearchButtonConfig // 搜索按钮配置
  defaultDays?: number // 默认日期范围（天数）
}
```

### CustomField

```typescript
interface CustomField {
  key: string // 字段key
  type: 'input' | 'select' | 'date' | 'nodList' | 'treeSelect' // 字段类型
  label: string // 字段标签
  placeholder?: string // 占位符文本
  clearable?: boolean // 是否可清空
  options?: Array<{
    // 下拉选项（select类型）
    label: string
    value: any
  }>
  multiple?: boolean // 是否多选（select/treeSelect类型）
  collapseTags?: boolean // 是否折叠标签（多选时使用）
  nodListConfig?: {
    // NodList配置
    action: string
    scene: string
  }
  treeData?: TreeData[] // 树形数据（treeSelect类型）
  checkStrictly?: boolean // 是否严格选择（treeSelect类型）
  renderAfterExpand?: boolean // 是否在展开后渲染（treeSelect类型）
}
```

### TreeData

```typescript
interface TreeData {
  value: any
  label: string
  children?: TreeData[]
  disabled?: boolean
}
```

### SearchButtonConfig

```typescript
interface SearchButtonConfig {
  icon?: string
  text?: string
  class?: string
}
```

## 高级功能

### 1. 高级搜索对话框机制

当点击以下字段时，会弹出专用的高级搜索对话框：
- **供应商**：弹出 `Supplier` 组件
- **客户**：弹出 `Customer` 组件
- **关联人员**：弹出 `PeopleList` 组件
- **制单人/所属用户**：弹出 `UserList` 组件

**特点**：
- ✅ 在高级搜索对话框中选择数据后，会自动填充到主表单
- ✅ 关闭高级搜索对话框不会关闭主 popover
- ✅ 支持嵌套对话框（如供应商搜索中的所属用户搜索）

### 2. Popover 手动关闭机制

**问题场景**：
- 在 popover 模式下，点击高级搜索字段会弹出对话框
- Element Plus 默认会在检测到外部点击时关闭 popover
- 这会导致打开对话框时 popover 意外关闭

**解决方案**：
1. **预先锁定机制**：在打开对话框前立即锁定 popover
2. **同步监听**：使用 `flush: 'sync'` 确保状态变化立即生效
3. **手动关闭**：添加"关闭"按钮，只有手动点击才会关闭
4. **多层保护**：通过 `popoverLocked`、`hasDialogOpen` 等多个状态变量确保稳定性

**用户体验**：
- 点击"三个点"打开 popover
- 点击任何高级搜索字段，popover 保持打开
- 在子对话框中操作，popover 保持打开
- 关闭子对话框，popover 仍然保持打开
- 点击"关闭"按钮或再次点击"三个点"才会关闭 popover

### 3. 专用搜索组件的设计模式

所有专用搜索组件都遵循统一的设计模式：

```vue
<template>
  <div class="inline-search-form">
    <GoodSearchForm 
      ref="searchForm"
      :config="searchConfig"
      @search="handleSearch"
    />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import GoodSearchForm from './GoodSearchForm.vue'
import type { GoodSearchConfig, SearchFormData } from './type'

const emit = defineEmits<{
  (e: 'search', params: any): void
}>()

const searchConfig = reactive<GoodSearchConfig>({
  inline: true,              // 使用内联模式
  showCustomer: false,       // 隐藏不需要的字段
  showGoods: false,
  // ... 其他配置
  
  customFields: [
    // 自定义字段配置
  ]
})

function handleSearch(formData: SearchFormData) {
  const searchParams = {
    // 字段映射
  }
  emit('search', searchParams)
}
</script>
```

## 注意事项

1. **组件依赖**：
   - 依赖 `NodList` 组件，确保项目中已正确引入
   - 依赖 Element Plus 图标组件 `Search` 和 `MoreFilled`

2. **日期格式**：
   - 日期格式默认为 `yyyy-MM-dd`
   - 可通过 `el-date-picker` 的 `value-format` 属性自定义

3. **字段显示逻辑**：
   - 所有字段默认都是显示的（除了 `showCustomer` 默认为 false）
   - 通过 `config` 中的 `showXxx: false` 来隐藏字段
   - 自定义字段会追加在默认字段之后显示

4. **专用搜索组件**：
   - 所有专用搜索组件都默认使用 `inline: true` 模式
   - 所有专用搜索组件都设置 `showCustomer: false` 避免字段污染
   - 建议在对话框或独立页面中使用专用搜索组件

5. **性能优化**：
   - 使用 `flush: 'sync'` 的 watch 确保状态同步
   - 使用 `destroy-on-close` 确保对话框销毁释放内存
   - 避免在 popover 内容中放置过多复杂组件

## 更新日志

### v2.0.0 (2024-10-29)

- ✨ 新增 inline 内联模式支持
- ✨ 新增客户字段支持（`showCustomer`）
- ✨ 新增 6 个专用搜索组件：Supplier、Customer、UserList、PeopleList、Warehouse、Account
- ✨ 新增高级搜索对话框机制（供应商、客户、人员、用户）
- ✨ 新增 `userPlaceholder` 配置支持自定义用户字段占位符
- 🐛 修复 popover 在打开子对话框时意外关闭的问题
- 🐛 修复搜索后自动关闭 popover 的问题
- 🎨 改进 popover 为手动关闭模式，添加"关闭"按钮
- 🔧 优化状态管理，使用 `flush: 'sync'` 确保同步响应
- 🔧 添加预先锁定机制，在打开对话框前锁定 popover
- 📝 完善 TypeScript 类型定义

### v1.2.0

- 新增树形选择器支持
- 新增多选下拉框支持
- 支持标签折叠显示
- 增强自定义字段配置

### v1.1.0

- 新增多选下拉框功能
- 支持标签折叠显示
- 优化表单初始化逻辑

### v1.0.0

- 🎉 初始版本发布
- ✨ 支持基础搜索字段
- ✨ 支持自定义字段扩展
- ✨ 支持 v-model 双向绑定
1. 组件依赖 `NodList` 和 `ElTreeSelect` 组件，确保项目中已正确引入
2. 日期格式默认为 `yyyy-MM-dd`
3. 所有字段默认都是显示的，通过 `config` 中的 `showXxx: false` 来隐藏
4. 自定义字段会追加在默认字段之后显示
5. 多选字段会自动初始化为数组类型
6. 树形选择器支持任意级别选择，适合分类、组织架构等场景

## 更新日志

### v1.2.0

- 新增树形选择器支持
- 新增多选下拉框支持
- 支持标签折叠显示
- 增强自定义字段配置

### v1.1.0

- 新增多选下拉框功能
- 支持标签折叠显示
- 优化表单初始化逻辑

### v1.0.0

- 初始版本发布
- 支持基础搜索字段
- 支持自定义字段扩展
- 支持 v-model 双向绑定
