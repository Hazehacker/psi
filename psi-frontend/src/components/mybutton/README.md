# MyButton 组件使用指南

## 快速使用

### 基础按钮

```vue
<template>
  <MyButton text="点击我" type="primary" @click="handleClick" />
</template>
```

### 业务按钮

```vue
<template>
  <BusinessButton action-type="save" @action="handleSave" />
</template>
```

### 按钮组

```vue
<template>
  <ButtonGroup align="right" gap="16px">
    <BusinessButton action-type="save" />
    <BusinessButton action-type="refresh" />
  </ButtonGroup>
</template>
```

## 参数说明

### MyButton 基础属性

```vue
<MyButton
  text="按钮文字"
  type="primary"     // primary | success | warning | danger | info
  size="default"     // large | default | small
  :loading="false"   // 加载状态
  :disabled="false"  // 禁用状态
  confirm-message="确认提示"  // 确认对话框
  @click="handleClick"
/>
```

### BusinessButton 业务类型

```vue
<!-- 保存按钮 -->
<BusinessButton action-type="save" />

<!-- 刷新按钮 -->
<BusinessButton action-type="refresh" />

<!-- 反审核按钮 -->
<BusinessButton action-type="unaudit" />

<!-- 核对按钮 -->
<BusinessButton action-type="check" />

<!-- 生成按钮 -->
<BusinessButton action-type="generate" />
```

### ButtonGroup 布局

```vue
<ButtonGroup
  direction="horizontal"  // horizontal | vertical
  align="left"           // left | center | right
  gap="12px"             // 按钮间距
>
  <!-- 按钮内容 -->
</ButtonGroup>
```

## 完整示例

### 采购审核页面

```vue
<template>
  <div>
    <ButtonGroup align="right" gap="16px">
      <BusinessButton
        action-type="unaudit"
        :disabled="selectedRows.length === 0"
        :data="selectedRows"
        @action="handleBatchUnaudit"
      />
      <BusinessButton
        action-type="check"
        :disabled="selectedRows.length === 0"
        :data="selectedRows"
        @action="handleBatchCheck"
      />
      <BusinessButton action-type="refresh" @action="handleRefresh" />
    </ButtonGroup>

    <MyTable :tabdata="tableData" @selection-change="handleSelectionChange" />
  </div>
</template>

<script setup>
const selectedRows = ref([])

const handleBatchUnaudit = (actionType, data) => {
  // 处理批量反审核
  console.log('反审核数据:', data)
}

const handleBatchCheck = (actionType, data) => {
  // 处理批量核对
  console.log('核对数据:', data)
}

const handleRefresh = () => {
  // 刷新数据
}
</script>
```

### 销售审核页面

```vue
<template>
  <ButtonGroup align="right" gap="16px">
    <BusinessButton
      action-type="generate"
      :disabled="selectedRows.length === 0"
      :data="selectedRows"
      @action="handleBatchGenerate"
    />
    <BusinessButton
      action-type="unaudit"
      :disabled="selectedRows.length === 0"
      :data="selectedRows"
      @action="handleBatchUnaudit"
    />
    <BusinessButton action-type="refresh" @action="handleRefresh" />
  </ButtonGroup>
</template>
```

## 事件处理

### 基础按钮事件

```vue
<MyButton text="保存" @click="(e, data) => console.log('点击事件', data)" />
```

### 业务按钮事件

```vue
<BusinessButton
  action-type="save"
  :data="formData"
  @action="
    (actionType, data) => {
      if (actionType === 'save') {
        console.log('保存数据:', data)
      }
    }
  "
/>
```

### 确认对话框

```vue
<MyButton text="删除" type="danger" confirm-message="确定要删除吗？" @confirm="handleDelete" />
```

## 状态控制

### 加载状态

```vue
<BusinessButton action-type="save" :loading="isSaving" />
```

### 禁用状态

```vue
<BusinessButton action-type="unaudit" :disabled="!canUnaudit" />
```

## 组件说明

- **MyButton**: 基础按钮组件，封装了 Element Plus 的 ElButton
- **BusinessButton**: 业务按钮，预设了常用的业务操作类型
- **ButtonGroup**: 按钮布局容器，统一管理按钮间距和对齐

## 查看演示

运行项目查看完整演示：`/view-business-buttons`

---

**提示**: 所有按钮都支持插槽自定义内容，如需要特殊样式可直接使用 MyButton 组件。
