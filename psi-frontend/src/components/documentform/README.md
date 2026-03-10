# 单据表单组件（DocumentForm）使用说明

该组件用于渲染业务“单据”相关的表单域，内置金额联动、物流信息、附件上传等常见业务字段，开箱即用，可通过 `fields` 控制显示哪些表单项。

## 1 导入组件

```html
<script setup lang="ts">
import DocumentForm from '@/components/forms/DocumentForm.vue'
</script>
```

## 2 可选/必选属性

```ts
/**
 * 组件可见字段控制
 * 通过传入字段数组决定渲染哪些表单项
 */
interface Props {
  /** 可见字段，不传则使用组件内置的默认集合 */
  fields?: string[]
}
```

默认可见字段集合：

```ts
[
  'documentCost',
  'documentBaseCost',
  'documentType',
  'actualAmount',
  'receivedAmount',
  'documentFee',
  'relatedPerson',
  'settlementAccount',
  'arrivalDate',
  'logistics',
  'documentAttachment',
  'remarks'
]
```

## 3 组件行为说明

- 金额字段自动联动：组件会基于 Pinia 采购明细中的价税合计之和，自动计算并回填 `documentCost`、`documentBaseCost`（空值时显示为空）。
- “实际金额”支持一键复制：在实际金额输入框的后缀处，点击复制图标可将单据金额复制到实际金额。
- 物流信息：通过内置的 `Logistics` 子组件收集物流信息，并根据“关联人员”推导部分默认信息。
- 附件上传：使用 Element Plus `el-upload`，默认上传地址为 `/api/upload`，单文件不超过 10MB。

## 4 基本用法示例

```html
<template>
  <!-- 传入需要显示的字段，未传则显示默认集合 -->
  <DocumentForm :fields="['documentType','relatedPerson','arrivalDate','remarks']" />
</template>

<script setup lang="ts">
import DocumentForm from '@/components/forms/DocumentForm.vue'
</script>
```

## 5 字段含义速览

- documentCost：单据金额（自动计算，只读外观）
- documentBaseCost：单据成本（自动计算，只读外观）
- documentType：单据类型（下拉）
- actualAmount：实际金额（可复制自单据金额）
- receivedAmount：实收金额
- documentFee：单据费用
- relatedPerson：关联人员（下拉）
- settlementAccount：结算账户（下拉）
- arrivalDate：到货日期（日期选择器）
- logistics：物流信息（复合对象，由 `Logistics` 子组件维护）
- documentAttachment：单据附件（上传列表）
- remarks：备注信息

## 6 表单校验

组件内部已对关键字段设置了基础校验规则（必填/触发时机等），如需扩展复杂校验，请在上层页面围绕 `DocumentForm` 外层再包裹一层 `el-form` 并追加自定义校验，或 Fork 本组件进行增强。


## 7 动态使用指南（按需显示/隐藏与交互）

以下示例展示如何在页面中“动态控制组件内部元素”，无需修改组件源码。

### 7.1 基于 `fields` 的显示/隐藏切换

`fields` 支持响应式数组。你可以使用 `ref` 管理字段集合，并在交互时增删字段名，组件会即时响应。

```vue
<template>
  <div style="display:flex; gap:12px; margin-bottom:12px;">
    <el-switch v-model="showFee" active-text="显示单据费用" />
    <el-switch v-model="showLogistics" active-text="显示物流信息" />
  </div>
  <DocumentForm :fields="visibleFields" />
  
  <!-- 也可以直接内联：:fields="['documentType', ...(showFee?['documentFee']:[])]" -->
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import DocumentForm from '@/components/forms/DocumentForm.vue'

const showFee = ref(true)
const showLogistics = ref(false)

// 基于开关动态拼装字段集合
const visibleFields = computed(() => {
  const base = [
    'documentCost', 'documentBaseCost', 'documentType',
    'actualAmount', 'receivedAmount', 'relatedPerson',
    'settlementAccount', 'arrivalDate', 'documentAttachment', 'remarks'
  ]
  if (showFee.value) base.splice(5, 0, 'documentFee')
  if (showLogistics.value) base.splice(base.length - 2, 0, 'logistics')
  return base
})
</script>
```

### 7.2 从外部触发“实际金额”复制逻辑

组件内部已经提供点击后缀图标复制 `documentCost -> actualAmount` 的交互。若你想从外部响应某个动作触发同样效果，可通过 v-model 同步到父级后实现：

```vue
<template>
  <DocumentForm ref="docRef" />
  <el-button type="primary" @click="copy">复制单据金额到实际金额</el-button>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { ComponentPublicInstance } from 'vue'
import DocumentForm from '@/components/forms/DocumentForm.vue'

// 如果需要调用内部逻辑，推荐在包一层父组件中维护相同逻辑，或 Fork 本组件后通过 expose 暴露方法
const docRef = ref<ComponentPublicInstance | null>(null)

function copy() {
  // 方式A：建议在父组件中复用同等逻辑
  // 方式B（需改造）：在 DocumentForm 内部通过 defineExpose 暴露 copyTotalToActual 再调用
  // 这里给出 A 的做法：监听 Pinia 合计或读取父级模型进行赋值
}
</script>
```

若你确实需要从外部直接调用组件内部方法，可在业务中对 `DocumentForm.vue` 做轻量修改：

```diff
// DocumentForm.vue
<script setup lang="ts">
// ... 现有代码
function copyTotalToActual() { /* ... */ }
defineExpose({ copyTotalToActual })
</script>
```

然后在父组件中：

```ts
docRef.value?.copyTotalToActual?.()
```

### 7.3 物流信息的联动（示例）

当你在外层选择“关联人员”时，组件会自动通过内部 `computed` 推导物流默认值（如承运商、编号前缀）。如果需要扩展该逻辑，可：

- 在父层将 `relatedPerson` 放入你的全局/页面状态，交由 `DocumentForm` 使用默认逻辑；
- 或 Fork 本组件，在 `supplierMore` 计算属性中根据你的业务规则丰富默认值来源。


