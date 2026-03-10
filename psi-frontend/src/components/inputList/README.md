# 通用标签管理组件使用说明

主要将标签的输入、添加、删除、验证等功能封装到组件中，方便实现各种标签管理场景，如商品品牌、计量单位、客户类别、客户等级、供应商类别等。

**封装的主要思路是通过数据驱动标签的渲染和管理，提供统一的验证逻辑和用户体验**，组件演示代码在测试页面中已经书写，可以自己运行观察效果。

## 1 基础使用

### 1.1 导入组件

导入组件关键代码如下

```html
<script setup lang="ts">
  // 导入组件和类型
  import InputList from '@/components/inputList/InputList.vue'
</script>
```

### 1.2 定义属性

必须定义的属性包括：标签数据（`modelValue`）、输入文本（`inputText`）和标签（`label`），其他属性属于可选属性，具体属性模型定义如下：

```typescript
interface TagManagerProps {
  /** 标签文本 */
  label: string
  /** 输入框占位符 */
  placeholder?: string
  /** 标签数据数组 */
  modelValue: string[]
  /** 输入框文本 */
  inputText: string
  /** 受保护的标签项（不可删除） */
  protectedItems?: string[]
  /** 保留文本项（不允许添加） */
  reservedItems?: string[]
  /** 组件类型，用于特殊验证逻辑 */
  type?: 'brand' | 'unit' | 'crCategory' | 'crGrade' | 'srCategory'
}
```

下面是定义组件属性的示例代码

```vue
<script setup lang="ts">
import { reactive } from 'vue'

// 定义表单数据
const form = reactive({
  brand: ['苹果', '华为', '小米'],
  unit: ['个', '箱', '包'],
  crCategory: ['常规类别', 'VIP客户'],
  crGrade: ['常规等级', '金牌客户'],
  srCategory: ['常规类别', '一级供应商']
})

// 定义输入文本
const text = reactive({
  brand: '',
  unit: '',
  crCategory: '',
  crGrade: '',
  srCategory: ''
})
</script>

<template>
  <!-- 商品品牌 -->
  <InputList
    label="商品品牌"
    placeholder="请输入品牌名称"
    v-model:model-value="form.brand"
    v-model:input-text="text.brand"
    type="brand"
  />

  <!-- 计量单位 -->
  <InputList
    label="计量单位"
    placeholder="请输入计量单位名称"
    v-model:model-value="form.unit"
    v-model:input-text="text.unit"
    type="unit"
    :reserved-items="['多单位', '-1']"
  />

  <!-- 客户类别 -->
  <InputList
    label="客户类别"
    placeholder="请输入客户类别名称"
    v-model:model-value="form.crCategory"
    v-model:input-text="text.crCategory"
    type="crCategory"
    :protected-items="['常规类别']"
    :reserved-items="['常规类别']"
  />
</template>
```

**_TIPS：组件属性大部分是可选属性，根据你实际需求指定属性即可_**

### 1.3 定义事件

定义的事件包括：

- `@update:model-value`：标签数据更新事件
- `@update:input-text`：输入文本更新事件
- `@add`：添加标签事件
- `@remove`：删除标签事件
- `@validation-error`：验证错误事件

下面是事件处理逻辑函数示例

```vue
<script setup lang="ts">
import { ElMessage } from 'element-plus'

// 事件处理函数
const handleAddBrand = (value: string) => {
  console.log('添加品牌:', value)
  // 这里可以添加API调用等业务逻辑
}

const handleRemoveBrand = (index: number) => {
  console.log('删除品牌索引:', index)
  // 这里可以添加API调用等业务逻辑
}

// 统一处理验证错误
const handleValidationError = (message: string) => {
  ElMessage({ type: 'warning', message })
}
</script>

<template>
  <InputList
    label="商品品牌"
    placeholder="请输入品牌名称"
    v-model:model-value="form.brand"
    v-model:input-text="text.brand"
    type="brand"
    @add="handleAddBrand"
    @remove="handleRemoveBrand"
    @validation-error="handleValidationError"
  />
</template>
```

关于组件上事件绑定：

- 通过 `v-model:model-value` 绑定标签数据
- 通过 `v-model:input-text` 绑定输入文本
- 通过 `@add` 绑定添加事件
- 通过 `@remove` 绑定删除事件
- 通过 `@validation-error` 绑定验证错误事件

## 2 功能特性

### 2.1 类型特定的验证逻辑

组件根据 `type` 属性提供类型特定的验证：

- **unit**：禁止添加"多单位"和"-1"
- **crCategory**：禁止添加"常规类别"
- **crGrade**：禁止添加"常规等级"
- **srCategory**：禁止添加"常规类别"

### 2.2 受保护标签

通过 `protected-items` 属性设置受保护的标签，这些标签将显示删除图标但不可删除：

```html
<InputList
  label="客户等级"
  v-model:model-value="form.crGrade"
  v-model:input-text="text.crGrade"
  :protected-items="['常规等级']"
/>
```

### 2.3 保留文本

通过 `reserved-items` 属性设置保留文本，这些文本不允许被添加为标签：

```html
<InputList
  label="计量单位"
  v-model:model-value="form.unit"
  v-model:input-text="text.unit"
  :reserved-items="['多单位', '-1']"
/>
```

### 2.4 多种添加方式

支持两种添加标签的方式：

- **点击加号图标**
- **在输入框中按回车键**

### 2.5 自动验证

组件会自动进行以下验证：

- 非空验证
- 重复项验证
- 保留文本验证
- 类型特定验证

## 3 在 Tabs 中使用

组件可以很方便地在 Tabs 布局中使用：

```html
<template>
  <el-tabs type="border-card">
    <el-tab-pane label="商品品牌">
      <InputList
        label="商品品牌"
        placeholder="请输入品牌名称"
        v-model:model-value="form.brand"
        v-model:input-text="text.brand"
        type="brand"
        @add="handleAddBrand"
        @remove="handleRemoveBrand"
      />
    </el-tab-pane>

    <el-tab-pane label="计量单位">
      <InputList
        label="计量单位"
        placeholder="请输入计量单位名称"
        v-model:model-value="form.unit"
        v-model:input-text="text.unit"
        type="unit"
        :reserved-items="['多单位', '-1']"
        @add="handleAddUnit"
        @remove="handleRemoveUnit"
      />
    </el-tab-pane>

    <!-- 更多标签页 -->
  </el-tabs>
</template>
```

## 4 样式定制

组件提供了一些 CSS 类用于样式定制：

- `.input-list`：表单项容器
- `.input-line`：输入框样式
- `.list`：标签列表容器

你可以通过这些类名覆盖默认样式：

```css
/* 自定义样式 */
.input-list .list {
  border-color: #e4e7ed;
}

.input-list .list li {
  background-color: #f8f9fa;
}
```

## 5 完整示例

除了在项目演示页面的“通用标签管理组件”外，下面还列举了一个完整的使用示例：

```vue
<template>
  <div class="sys area">
    <el-form :model="form" ref="formRef" label-width="90px" class="formAdapt">
      <el-tabs tab-position="left">
        <el-tab-pane label="商品品牌">
          <InputList
            label="商品品牌"
            placeholder="请输入品牌名称"
            v-model:model-value="form.brand"
            v-model:input-text="text.brand"
            type="brand"
            @add="handleAddBrand"
            @remove="handleRemoveBrand"
            @validation-error="handleValidationError"
          />
        </el-tab-pane>

        <el-tab-pane label="计量单位">
          <InputList
            label="计量单位"
            placeholder="请输入计量单位名称"
            v-model:model-value="form.unit"
            v-model:input-text="text.unit"
            type="unit"
            :reserved-items="['多单位', '-1']"
            @add="handleAddUnit"
            @remove="handleRemoveUnit"
            @validation-error="handleValidationError"
          />
        </el-tab-pane>

        <el-tab-pane label="客户类别">
          <InputList
            label="客户类别"
            placeholder="请输入客户类别名称"
            v-model:model-value="form.crCategory"
            v-model:input-text="text.crCategory"
            type="crCategory"
            :protected-items="['常规类别']"
            :reserved-items="['常规类别']"
            @add="handleAddCrCategory"
            @remove="handleRemoveCrCategory"
            @validation-error="handleValidationError"
          />
        </el-tab-pane>

        <el-tab-pane label="客户等级">
          <InputList
            label="客户等级"
            placeholder="请输入客户等级名称"
            v-model:model-value="form.crGrade"
            v-model:input-text="text.crGrade"
            type="crGrade"
            :protected-items="['常规等级']"
            :reserved-items="['常规等级']"
            @add="handleAddCrGrade"
            @remove="handleRemoveCrGrade"
            @validation-error="handleValidationError"
          />
        </el-tab-pane>

        <el-tab-pane label="供应商类别">
          <InputList
            label="供应商类别"
            placeholder="请输入供应商类别名称"
            v-model:model-value="form.srCategory"
            v-model:input-text="text.srCategory"
            type="srCategory"
            :protected-items="['常规类别']"
            :reserved-items="['常规类别']"
            @add="handleAddSrCategory"
            @remove="handleRemoveSrCategory"
            @validation-error="handleValidationError"
          />
        </el-tab-pane>
      </el-tabs>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import InputList from '@/components/inputList/InputList.vue'

// 表单数据
const form = reactive({
  brand: ['苹果', '华为'],
  unit: ['个', '箱'],
  crCategory: ['常规类别', 'VIP客户'],
  crGrade: ['常规等级', '金牌客户'],
  srCategory: ['常规类别', '一级供应商']
})

const text = reactive({
  brand: '',
  unit: '',
  crCategory: '',
  crGrade: '',
  srCategory: ''
})

// 表单引用
const formRef = ref()

// 事件处理函数
const handleAddBrand = (value: string) => {
  form.brand.push(value)
  ElMessage.success(`成功添加品牌: ${value}`)
}

const handleRemoveBrand = (index: number) => {
  const brandName = form.brand[index]
  form.brand.splice(index, 1)
  ElMessage.warning(`已删除品牌: ${brandName}`)
}

// 其他事件处理函数...

// 统一处理验证错误
const handleValidationError = (message: string) => {
  ElMessage({ type: 'warning', message })
}
</script>
```

**_TIPS：以上是通用标签管理组件的基本使用步骤，可以满足大部分标签管理需求_**
