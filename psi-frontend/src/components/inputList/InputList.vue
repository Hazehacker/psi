<!--
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-10-19 16:14:08
 * @Description: 通用标签管理组件
-->
<template>
  <el-form-item :label="props.label" class="input-list">
    <el-input
      class="input-line"
      v-model="inputValue"
      :placeholder="props.placeholder"
      clearable
      :validate-event="false"
      @keyup.enter="handleAdd"
    >
      <template #append>
        <el-icon @click="handleAdd"><CirclePlus /></el-icon>
      </template>
    </el-input>
    <ul v-if="hasItems" class="list">
      <li v-for="(item, index) in props.modelValue" :key="index">
        {{ item }}
        <el-icon v-if="!isProtected(item)" @click="handleRemove(index)"><Remove /></el-icon>
      </li>
    </ul>
  </el-form-item>
</template>

<script setup lang="ts">
import { computed } from 'vue'

// 定义组件接口
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

// 定义事件接口
interface TagManagerEmits {
  (e: 'update:modelValue', value: string[]): void
  (e: 'update:inputText', value: string): void
  (e: 'add', value: string): void
  (e: 'remove', index: number): void
  (e: 'validation-error', message: string): void
}

const props = withDefaults(defineProps<TagManagerProps>(), {
  placeholder: '请输入内容',
  protectedItems: () => ['常规类别', '常规等级'],
  reservedItems: () => [],
  type: 'brand'
})

const emit = defineEmits<TagManagerEmits>()

// 计算属性
const inputValue = computed({
  get: (): string => props.inputText,
  set: (value: string): void => {
    emit('update:inputText', value)
  }
})

const hasItems = computed((): boolean => {
  return props.modelValue.length > 0
})

// 验证输入内容
function validateInput(value: string): boolean {
  if (!value || value.trim() === '') {
    emit('validation-error', `${props.label}名称不可为空!`)
    return false
  }

  // 根据类型进行特殊验证
  switch (props.type) {
    case 'unit':
      if (value === '多单位' || value === '-1') {
        emit('validation-error', '计量单位[ 多单位 ]为保留文本!')
        return false
      }
      break
    case 'crCategory':
      if (value === '常规类别') {
        emit('validation-error', '客户类别[ 常规类别 ]为保留文本!')
        return false
      }
      break
    case 'crGrade':
      if (value === '常规等级') {
        emit('validation-error', '客户等级[ 常规等级 ]为保留文本!')
        return false
      }
      break
    case 'srCategory':
      if (value === '常规类别') {
        emit('validation-error', '供应商类别[ 常规类别 ]为保留文本!')
        return false
      }
      break
  }

  // 检查保留文本
  if (props.reservedItems.includes(value)) {
    emit('validation-error', `${props.label}[ ${value} ]为保留文本!`)
    return false
  }

  // 检查是否已存在
  if (props.modelValue.includes(value)) {
    emit('validation-error', `${props.label}[ ${value} ]已存在!`)
    return false
  }

  return true
}

// 方法
function isProtected(item: string): boolean {
  return props.protectedItems.includes(item)
}

function handleAdd(): void {
  const value = inputValue.value?.trim()

  if (validateInput(value)) {
    emit('add', value)
    emit('update:inputText', '')
  }
}

function handleRemove(index: number): void {
  const item = props.modelValue[index]
  if (!isProtected(item)) {
    emit('remove', index)
  }
}
</script>

<style scoped>
:deep(.el-form-item__content) {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.input-line {
  width: 360px;
}

.list {
  padding: 0;
  width: 360px;
  margin-top: 6px;
  list-style: none;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
}
.list li {
  color: #606266;
  position: relative;
  padding: 8px 24px;
  line-height: 1.5;
}
.list li:nth-child(2n + 1) {
  background: #f4f4f5;
}
.list li .el-icon {
  position: absolute;
  right: 6px;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  color: #606266;
}
.list li .el-icon:hover {
  color: #f56c6c; /* 移除：红色悬停效果 */
}
/* 为输入框的追加图标添加悬停效果 */
:deep(.el-input-group__append) .el-icon {
  cursor: pointer;
  transition: color 0.2s;
}
:deep(.el-input-group__append) .el-icon:hover {
  color: #409eff; /* 添加：蓝色悬停效果 */
}
</style>
