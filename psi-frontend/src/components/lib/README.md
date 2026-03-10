# Lib 组件库使用说明

Lib 组件库包含了一系列可复用的基础组件，这些组件已经转换为 Vue 3 Composition API 版本。

## 组件列表

### 1. FieldForm.vue - 动态表单组件
**功能**: 根据配置动态生成表单字段，支持拖拽排序
**特点**:
- 支持多种字段类型：文本、时间、文本域、选择器、复选框、文件上传
- 支持拖拽排序（可选）
- 支持表单验证规则

**使用示例**:
```vue
<template>
  <FieldForm 
    v-model="formData" 
    :rule="fieldRules" 
    :drag="true"
    prefix="form"
  />
</template>

<script setup>
import FieldForm from '@/components/lib/FieldForm.vue'

const formData = ref({})
const fieldRules = [
  {
    label: '用户名',
    prop: 'username',
    model: 'username',
    type: 'text',
    placeholder: '请输入用户名',
    rules: [{ required: true, message: '请输入用户名' }]
  },
  {
    label: '生日',
    prop: 'birthday',
    model: 'birthday',
    type: 'time',
    placeholder: '请选择生日'
  }
]
</script>
```

**Props**:
- `value` (Object, required): 表单数据对象
- `rule` (Array, required): 字段配置规则
- `prefix` (String, default: ""): 字段名前缀
- `drag` (Boolean, default: false): 是否启用拖拽排序

### 2. NodList.vue - 下拉选择列表组件
**功能**: 带搜索和分页的下拉选择组件
**特点**:
- 支持搜索过滤
- 支持分页加载
- 支持键盘导航
- 支持远程数据加载

**使用示例**:
```vue
<template>
  <NodList 
    v-model="selectedValue"
    :url="'/api/users'"
    :params="searchParams"
    placeholder="请选择用户"
    @change="handleChange"
  />
</template>

<script setup>
import NodList from '@/components/lib/NodList.vue'

const selectedValue = ref('')
const searchParams = ref({})

const handleChange = (value) => {
  console.log('选中值:', value)
}
</script>
```

**Props**:
- `value` (String/Number, required): 选中的值
- `url` (String, required): 数据接口地址
- `params` (Object): 查询参数
- `placeholder` (String): 占位符文本
- `disabled` (Boolean): 是否禁用

### 3. NodEditor.vue - 富文本编辑器组件
**功能**: 基于 Quill 的富文本编辑器
**特点**:
- 支持多种文本格式
- 支持图片上传
- 支持工具栏自定义
- 支持内容变化监听

**使用示例**:
```vue
<template>
  <NodEditor 
    v-model="content"
    :height="300"
    @change="handleContentChange"
  />
</template>

<script setup>
import NodEditor from '@/components/lib/NodEditor.vue'

const content = ref('')

const handleContentChange = (value) => {
  console.log('内容变化:', value)
}
</script>
```

**Props**:
- `value` (String, required): 编辑器内容
- `height` (Number, default: 300): 编辑器高度

### 4. NodIet.vue - 输入框组件
**功能**: 增强的输入框组件，支持多种输入类型
**特点**:
- 支持数字输入
- 支持金额格式化
- 支持输入验证
- 支持单位显示

**使用示例**:
```vue
<template>
  <NodIet 
    v-model="amount"
    type="money"
    unit="元"
    :precision="2"
  />
</template>

<script setup>
import NodIet from '@/components/lib/NodIet.vue'

const amount = ref(0)
</script>
```

**Props**:
- `value` (String/Number, required): 输入值
- `type` (String): 输入类型 (text, number, money)
- `unit` (String): 单位文本
- `precision` (Number): 小数位数

### 5. NodTree.vue - 树形选择组件
**功能**: 树形结构数据选择组件
**特点**:
- 支持多选
- 支持搜索过滤
- 支持懒加载
- 支持节点展开/收起

**使用示例**:
```vue
<template>
  <NodTree 
    v-model="selectedNodes"
    :data="treeData"
    :multiple="true"
    :check-strictly="false"
  />
</template>

<script setup>
import NodTree from '@/components/lib/NodTree.vue'

const selectedNodes = ref([])
const treeData = ref([
  {
    id: 1,
    label: '父节点',
    children: [
      { id: 2, label: '子节点1' },
      { id: 3, label: '子节点2' }
    ]
  }
])
</script>
```

**Props**:
- `value` (Array, required): 选中的节点
- `data` (Array, required): 树形数据
- `multiple` (Boolean): 是否多选
- `check-strictly` (Boolean): 是否严格模式

### 6. NodUpload.vue - 文件上传组件
**功能**: 文件上传组件，支持多文件上传
**特点**:
- 支持多文件上传
- 支持文件预览
- 支持文件删除
- 支持上传进度

**使用示例**:
```vue
<template>
  <NodUpload 
    v-model="fileList"
    :action="'/api/upload'"
    @change="handleFileChange"
  />
</template>

<script setup>
import NodUpload from '@/components/lib/NodUpload.vue'

const fileList = ref([])

const handleFileChange = (files) => {
  console.log('文件列表:', files)
}
</script>
```

**Props**:
- `value` (Array, required): 文件列表
- `action` (String, required): 上传接口地址

### 7. PageStatus.vue - 页面状态统计组件
**功能**: 显示页面数据统计信息
**特点**:
- 支持多种统计类型
- 支持自定义计算函数
- 支持实时更新

**使用示例**:
```vue
<template>
  <PageStatus 
    :config="statusConfig"
    :model="tableData"
  />
</template>

<script setup>
import PageStatus from '@/components/lib/PageStatus.vue'

const tableData = ref([])
const statusConfig = [
  { text: '汇总数据', type: 'text' },
  { text: '商品总数', type: 'count' },
  { text: '总数量', type: 'sum', key: 'nums' },
  { text: '总价格', type: 'sum', key: 'total' },
  { text: '平均价', type: 'avg', key: 'total' }
]
</script>
```

**Props**:
- `config` (Array, required): 状态配置
- `model` (Array, required): 数据模型

### 8. LineFeed.vue - 换行组件
**功能**: 简单的换行显示组件

### 9. Logistics.vue - 物流组件
**功能**: 物流信息显示组件

## 注意事项

1. 所有组件都已转换为 Vue 3 Composition API
2. 使用前请确保已正确安装 Element Plus
3. 部分组件依赖全局属性（如 `$store`, `$lib`, `$calc`），请确保这些属性已正确配置
4. 组件支持 TypeScript，建议在 TypeScript 项目中使用以获得更好的类型提示

## 依赖

- Vue 3.x
- Element Plus
- vuedraggable (FieldForm 组件)
- quill (NodEditor 组件)
