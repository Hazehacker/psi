<!--
 * @Description: 输入列表配置组件
 * 作为InputList组件的包装器，提供数据管理和事件处理功能
 * 用于管理字符串列表的配置，支持添加、删除和验证操作
-->
<template>
  <div class="input-list-config">
    <!-- 
      输入列表组件 
      接收配置属性并绑定事件处理函数
    -->
    <InputList
      :label="label"                          
      :placeholder="placeholder"              
      :model-value="items"                    
      :input-text="inputText"                 
      :protected-items="protectedItems"       
      :reserved-items="reservedItems"         
      @update:model-value="handleItemsChange" 
      @update:input-text="handleInputTextChange" 
      @add="handleAdd"                        
      @remove="handleRemove"                  
      @validation-error="handleValidationError" 
    />
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import InputList from '@/components/inputList/InputList.vue'

/**
 * 组件属性接口定义
 * 定义组件接收的props及其类型
 */
interface Props {
  label: string           // 列表标签文本
  placeholder: string     // 输入框占位文本
  modelValue: string[]    // 双向绑定的字符串数组
  protectedItems?: string[] // 受保护的项（不可删除），默认空数组
  reservedItems?: string[]  // 保留项（不允许添加），默认空数组
}

/**
 * 设置组件属性的默认值
 */
const props = withDefaults(defineProps<Props>(), {
  protectedItems: () => [],  // 默认受保护项为空数组
  reservedItems: () => []    // 默认保留项为空数组
})

/**
 * 组件事件定义
 * 定义组件向父组件发出的事件类型
 */
const emit = defineEmits<{
  (e: 'update:modelValue', value: string[]): void  // 列表数据更新事件
}>()

/**
 * 响应式数据
 * 存储当前列表数据和输入文本
 */
const items = ref([...props.modelValue])  // 列表项数据，初始化为props的副本
const inputText = ref('')                 // 输入框文本内容

/**
 * 监听父组件传递的modelValue变化
 * 当父组件更新数据时，同步更新本地数据
 */
watch(
  () => props.modelValue,
  (newVal) => {
    items.value = [...newVal]  // 使用展开运算符创建新数组，确保响应式更新
  },
  { deep: true }  // 深度监听，确保数组内容变化也能被捕获
)

/**
 * 处理列表数据变更事件
 * 当InputList组件中的列表数据发生变化时调用
 * @param value - 更新后的列表数据
 */
function handleItemsChange(value: string[]) {
  items.value = value                    // 更新本地数据
  emit('update:modelValue', value)       // 通知父组件数据已更新
}

/**
 * 处理输入文本变更事件
 * 当InputList组件中的输入框文本发生变化时调用
 * @param value - 更新后的输入文本
 */
function handleInputTextChange(value: string) {
  inputText.value = value  // 更新本地输入文本
}

/**
 * 处理添加项事件
 * 当用户在InputList组件中添加新项时调用
 * @param value - 要添加的新项
 */
function handleAdd(value: string) {
  items.value.push(value)                    // 将新项添加到本地列表
  emit('update:modelValue', items.value)     // 通知父组件列表已更新
}

/**
 * 处理删除项事件
 * 当用户在InputList组件中删除项时调用
 * @param index - 要删除的项的索引
 */
function handleRemove(index: number) {
  items.value.splice(index, 1)               // 从本地列表中删除指定项
  emit('update:modelValue', items.value)     // 通知父组件列表已更新
}

/**
 * 处理验证错误事件
 * 当InputList组件中发生验证错误时调用
 * @param message - 错误消息
 */
function handleValidationError(message: string) {
  // 使用Element Plus的消息组件显示警告提示
  ElMessage({ type: 'warning', message })
}
</script>

<style scoped lang="scss">
.input-list-config {
  width: 100%;  /* 组件宽度占满父容器 */
}
</style>