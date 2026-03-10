# CustomUpload 使用说明

该组件提供一个“自定义样式的文件选择触发器”。它不直接上传文件，仅负责触发系统文件选择并通过事件把选择结果抛出，方便与你的上传逻辑或 `el-upload` 组合使用。

## 导入

```vue
<script setup lang="ts">
import CustomUpload from '@/components/CustomUpload/CustomUpload.vue'
</script>
```

## 属性（Props）
- multiple?: 是否支持多选，默认关闭
- accept?: 接受文件类型（MIME 或扩展名），如 `image/*`、`.png,.jpg,.pdf`
- disabled?: 是否禁用点击触发

## 事件（Emits）
- change(files: FileList | null): 文件选择变化时触发；取消选择或未选中文件时可能为 `null`

## 基本用法

```vue
<template>
  <CustomUpload
    :multiple="true"
    accept=".png,.jpg,.pdf"
    @change="onFilesChange"
  />
</template>

<script setup lang="ts">
function onFilesChange(files: FileList | null) {
  if (!files || files.length === 0) return
  // 在这里发起上传请求，或交给 el-upload/自定义逻辑处理
  // 例如：Array.from(files).forEach(uploadFile)
}
</script>
```

## 与 Element Plus el-upload 组合

`CustomUpload` 只负责选择文件。若需“选择 + 上传 + 文件列表展示”的完整体验，可将 `CustomUpload` 的回调与 `el-upload` 的方法结合：

```vue
<template>
  <div style="display:flex; gap:12px;">
    <CustomUpload accept="image/*" @change="handlePick" />
    <el-upload ref="uploader" :action="uploadAction" :auto-upload="false" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const uploadAction = '/api/upload'
const uploader = ref()

function handlePick(files: FileList | null) {
  if (!files) return
  // 将文件添加到 el-upload 并手动触发上传（根据你的场景调整）
  // uploader.value?.uploadRef?.handleStart(file) ...
}
</script>
```

## 设计取舍
- 保持“无上传、副作用少”的职责边界，便于在不同业务里复用
- UI 使用 Element Plus 图标，外观轻量且可覆盖自定义样式
