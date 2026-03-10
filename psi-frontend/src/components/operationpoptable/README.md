# 📘 operationpoptable 组件说明文档

## 🧩 一、组件简介

`OperationPopTable` 是一个基于 **Vue3 + Element Plus** 开发的可复用商品选择弹窗组件。
组件提供了以下主要功能：

- 主弹窗展示商品列表，支持横向滚动浏览数据。
- 部分列（选择 / 商品名称 / 数量）固定，不随表格滚动。
- 支持父子层级结构：

  - 可展开查看子商品；
  - 子商品选择后，自动联动父商品半选 / 全选状态。

- 支持分页、每页显示数量切换、总数统计。
- 支持点击“⋯”按钮打开左下角搜索小弹窗进行数据筛选。
- 支持点击“选择”按钮将选中数据暴露给外部父组件（通过 `emit`）。
- 预留后端 API 接口接入位置，当前为本地模拟数据。

---

## 🏗 二、文件结构

组件文件位于：

```
src/components/operationpoptable/
├── index.ts                # 对外导出文件
├── OperationPopTable.vue   # 主弹窗组件
├── SearchPopup.vue         # 左下角搜索子弹窗组件
├── type.ts                 # 数据类型定义文件
```

---

## ⚙️ 三、组件注册与使用

### 1️⃣ 引入方式

在项目的组件中使用时，可通过 `index.ts` 导入：

```ts
import OperationPopTable from '@/components/operationpoptable'
```

或直接在模板中注册：

```vue
<script setup lang="ts">
import OperationPopTable from '@/components/operationpoptable/OperationPopTable.vue'
</script>

<template>
  <OperationPopTable v-model:visible="showPop" @confirm="handleSelect" />
</template>
```

---

## 🧠 四、主要属性与事件

### 🔹 Props

| 属性名            | 类型        | 默认值         | 说明                         |
| ----------------- | ----------- | -------------- | ---------------------------- |
| `visible`         | `boolean`   | `false`        | 控制主弹窗是否显示           |
| `title`           | `string`    | `'选择商品'`   | 弹窗标题                     |
| `width`           | `string`    | `'50%'`        | 主弹窗宽度                   |
| `dataSource`      | `Product[]` | `[]`           | 商品数据源，可从后端接口传入 |
| `pageSizeOptions` | `number[]`  | `[30, 60, 90]` | 每页显示数量可选项           |

---

### 🔸 Emits（事件）

| 事件名           | 参数           | 说明                                         |
| ---------------- | -------------- | -------------------------------------------- |
| `update:visible` | `boolean`      | 控制弹窗显示状态                             |
| `confirm`        | `Product[]`    | 当点击“选择”按钮时，返回用户选中的商品数组   |
| `search`         | `SearchParams` | 从搜索小弹窗触发的搜索事件，可与后端接口对接 |
| `cancel`         | 无             | 点击“取消”按钮关闭弹窗                       |

---

## 🧮 五、数据结构定义（type.ts）

```ts
export interface TableItem {
  id: number
  name: string
  code: string
  model: string
  category: string
  brand: string
  unit: string
  purchasePrice: number
  salePrice: number
  barcode: string
  quantity: number
  checked?: boolean
  children?: Product[]
}

export interface SearchParams {
  name?: string
  code?: string
  barcode?: string
  category?: string
  brand?: string
  type?: string
}
```

---

## 🔍 六、搜索子弹窗功能（SearchPopup.vue）

- 点击主弹窗左下角“⋯”按钮后出现；
- 固定在主弹窗左下方；
- 包含输入项与下拉选择项；
- 点击“搜索”后：

  - 会关闭小弹窗；
  - 向主弹窗发送 `search` 事件；
  - 主弹窗根据搜索条件在当前数据中筛选显示结果；

- 预留接口以支持后端搜索（只需在 `handleSearch()` 中替换为 API 请求）。

---

## 🔗 七、外部通信说明

当用户选择商品后点击“选择”按钮，组件会通过：

```ts
emit('confirm', selectedItems.value)
```

将用户选择的商品数组暴露给父级组件。
外部组件只需监听 `@confirm` 事件即可获得数据，例如：

```vue
<OperationPopTable v-model:visible="showPop" @confirm="handleSelectedItems" />

<script setup lang="ts">
const showPop = ref(false)

const handleSelectedItems = (items) => {
  console.log('用户选择的商品：', items)
  // 此处可将 items 传入外部表格或存储到状态管理
}
</script>
```

---

## ⚙️ 八、与后端接口对接说明（TODO）

后续接入后端 API 时，只需修改两处位置：

1️⃣ 在 `OperationPopTable.vue` 的 `loadData()` 中，从接口获取商品数据：

```ts
// TODO: 替换为真实接口
const loadData = async () => {
  const res = await api.get('/product/list', { params: queryParams })
  tableData.value = res.data
}
```

2️⃣ 在 `SearchPopup.vue` 的 `handleSearch()` 中，向父组件传递搜索参数或直接请求后端接口。

---

## 🧪 九、测试与调试

### 启动项目

```bash
pnpm install
pnpm run dev
```

### 测试页面示例

可在任意页面中引入：

```vue
<template>
  <el-button @click="showPop = true">打开商品选择弹窗</el-button>
  <OperationPopTable v-model:visible="showPop" @confirm="handleSelect" />
</template>

<script setup lang="ts">
import OperationPopTable from '@/components/operationpoptable/OperationPopTable.vue'
const showPop = ref(false)
const handleSelect = (items) => {
  console.log('选择的商品：', items)
}
</script>
```

---

## 🎨 十、UI 风格与交互说明

- 弹窗宽度默认 50%，可通过 `width` 属性修改；
- 表格横向滚动区域仅包含可拖动列；
- 数量列为可编辑数字输入框；
- 选择与展开共用一列：

  - 若商品可展开，则无选择框；
  - 子商品行可独立选择；
  - 父商品与子商品之间存在选中状态联动；

- 小弹窗位置固定于主弹窗左下角，避免影响主内容展示。

---

## 🧾 十一、版本与依赖

| 依赖         | 版本   |
| ------------ | ------ |
| Vue          | ^3.4.x |
| Element Plus | ^2.8.x |
| TypeScript   | ^5.x   |
| Vite         | ^5.x   |

---

## 📄 十二、更新计划

- ✅ 支持本地模拟数据
- ✅ 父子节点选中状态联动
- ✅ 小弹窗搜索与过滤功能
- 🔜 支持后端接口数据获取
- 🔜 支持多选结果缓存与批量导出

---

---

## 🧑‍💻 十三、快速开发者指南（Dev Notes）

以下内容供维护或扩展该组件的开发者参考。

---

### 🗂 1️⃣ 文件结构与职责说明

| 文件                    | 作用说明                                     |
| ----------------------- | -------------------------------------------- |
| `OperationPopTable.vue` | 主弹窗组件，包含表格展示、分页、按钮交互逻辑 |
| `SearchPopup.vue`       | 左下角搜索小弹窗，负责输入与筛选条件收集     |
| `index.ts`              | 统一导出组件入口文件，用于简化外部引入       |
| `type.ts`               | 定义 `Product`、`SearchParams` 等通用类型    |
| `mockData.ts`（可选）   | 本地测试数据文件，可用于前端开发阶段调试     |
| `README.md`             | 组件使用文档与维护说明（即本文档）           |

---

### ⚙️ 2️⃣ 组件注册方式说明

如果你的项目采用 **自动注册组件机制**（例如 `components.d.ts` 自动导入），只需将 `OperationPopTable.vue` 放置在 `components/operationpoptable` 下即可自动识别。
否则可手动在使用页面中导入：

```ts
import OperationPopTable from '@/components/operationpoptable/OperationPopTable.vue'
```

---

### 🔗 3️⃣ 与父组件通信说明

组件对外通信主要依靠以下事件：

- `update:visible` —— 控制主弹窗显示与关闭；
- `confirm` —— 将已选商品数据数组传递给父组件；
- `search` —— 当小弹窗提交搜索条件时触发；
- `cancel` —— 用户点击“取消”关闭弹窗时触发。

外部父组件监听事件示例：

```vue
<OperationPopTable
  v-model:visible="showPop"
  @confirm="handleSelectedProducts"
  @search="handleSearchParams"
/>
```

---

### 🧩 4️⃣ 表格功能说明与注意事项

| 功能点   | 说明                                                                 |
| -------- | -------------------------------------------------------------------- |
| 固定列   | 选择 / 商品名称 / 数量 不可横向滚动                                  |
| 可滚动列 | 商品编号、规格型号、分类、品牌、价格等列                             |
| 展开功能 | 若商品有 `children` 字段则显示展开按钮                               |
| 选择功能 | 展开后的子商品可单独选择                                             |
| 联动逻辑 | 父商品全选 → 所有子商品自动选中；子商品部分选中 → 父商品显示半选状态 |
| 数量列   | 纯数字输入框（`<el-input-number>` 或 `<el-input>` 实现）             |
| 数据加载 | 默认使用本地数据，可替换为后端接口返回的分页结果                     |

---

### 🔍 5️⃣ 搜索子弹窗功能说明

| 区域     | 说明                                                       |
| -------- | ---------------------------------------------------------- |
| 显示方式 | 点击主弹窗左下角“⋯”按钮触发                                |
| 位置     | 固定在主弹窗左下角                                         |
| 搜索字段 | 名称、编号、条码（输入框）；分类、品牌、类型（下拉框）     |
| 搜索逻辑 | 点击“搜索”后关闭小弹窗，将条件传回主弹窗进行本地或接口筛选 |
| 扩展点   | 可将 `emit('search', params)` 替换为后端请求逻辑           |

---

### ⚡ 6️⃣ 接入后端接口的建议实现方式

后续若需接入接口，可参考以下方式：

#### 👉 商品列表接口

```ts
const loadData = async () => {
  const res = await api.get('/product/list', { params: { page, size, filters } })
  tableData.value = res.data.records
  total.value = res.data.total
}
```

#### 👉 搜索接口

```ts
const handleSearch = async (params: SearchParams) => {
  const res = await api.get('/product/search', { params })
  filteredData.value = res.data
}
```

> ⚠️ 提示：在接入接口时，可删除 `mockData.ts` 并替换为动态数据。

---

### 🧰 7️⃣ 常见问题（FAQ）

**Q1：弹窗显示位置不正确怎么办？**
A1：确认组件外层未使用 `overflow: hidden` 的容器；若使用 `<el-dialog>`，请设置 `append-to-body`。

**Q2：搜索小弹窗位置偏移？**
A2：确保小弹窗的定位是相对主弹窗容器 (`position: relative`) 而非全局。

**Q3：如何复用该组件？**
A3：可在任意页面引用，只需传入不同的 `dataSource` 即可实现多表复用。

**Q4：如何防止父组件刷新时弹窗被销毁？**
A4：可在父组件中使用 `<keep-alive>` 或通过状态管理（如 Pinia）缓存选中状态。

---

### 🧑‍🔧 8️⃣ 开发调试命令

```bash
# 安装依赖
pnpm install

# 启动开发环境
pnpm run dev

# 代码格式化
pnpm run lint

# 构建生产版本
pnpm run build
```

---

### 🧱 9️⃣ 组件规范建议

- 使用 **TypeScript** 严格类型定义；
- 遵守 `PascalCase` 组件命名规范；
- 每个组件目录中应包含：

  - `.vue` 文件；
  - `index.ts` 出口文件；
  - `README.md` 简要文档；

- 所有 `emit` 事件与外部 `props` 需在文档中说明；
- 每个复杂功能（如搜索、分页）建议拆分为独立子组件。

---

### 🧾 10️⃣ 更新日志（建议格式）

| 日期       | 版本   | 更新内容                                 |
| ---------- | ------ | ---------------------------------------- |
| 2025-10-21 | v1.0.0 | 初始化组件，完成本地数据展示与搜索功能   |
| 2025-10-22 | v1.1.0 | 增加父子节点联动选择逻辑、小弹窗交互完善 |
| 2025-10-25 | v1.2.0 | 预留后端 API 接口，优化分页与表格布局    |

---
