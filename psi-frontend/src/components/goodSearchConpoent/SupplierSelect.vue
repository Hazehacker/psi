<!--
 * @Description: 供应商选择组件（包装 Supplier 搜索组件，样式类似 el-select）
-->
<template>
  <el-input
    v-model="displayValue"
    :placeholder="placeholder"
    readonly
    clearable
    :class="inputClass"
    style="cursor: pointer"
    @focus="handleFocus"
    @clear="handleClear"
  >
    <template #suffix>
      <el-icon @click.stop="handleFocus" style="cursor: pointer" class="select-icon">
        <ArrowDown />
      </el-icon>
    </template>
  </el-input>

  <!-- 供应商搜索对话框 -->
  <el-dialog
    v-model="showDialog"
    title="供应商搜索"
    width="720px"
    append-to-body
    :close-on-click-modal="false"
    :modal="true"
    destroy-on-close
  >
    <div @click.stop @mousedown.stop @mouseup.stop>
      <Supplier @search="handleSupplierSearch" />
    </div>
    <template #footer>
      <div @click.stop @mousedown.stop @mouseup.stop>
        <el-button @click="showDialog = false">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { ArrowDown } from '@element-plus/icons-vue'
import Supplier from './Supplier.vue'

interface Props {
  modelValue?: string | null
  placeholder?: string
  inputClass?: string
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: null,
  placeholder: '请选择供应商',
  inputClass: ''
})

const emit = defineEmits<{
  'update:modelValue': [value: string | null]
  change: [value: string | null]
}>()

const showDialog = ref(false)
const displayValue = ref<string | null>(props.modelValue || null)

// 监听外部值变化
watch(() => props.modelValue, (newVal) => {
  displayValue.value = newVal || null
}, { immediate: true })

// 监听内部值变化
watch(displayValue, (newVal) => {
  emit('update:modelValue', newVal)
  emit('change', newVal)
})

function handleFocus() {
  showDialog.value = true
}

function handleClear() {
  displayValue.value = null
  emit('update:modelValue', null)
  emit('change', null)
}

function handleSupplierSearch(params: any) {
  // 供应商组件返回的参数格式：{ name, number, ... }
  if (params && typeof params === 'object') {
    // 优先使用 name，其次尝试 supplierName
    const name = params.name || params.supplierName || ''
    displayValue.value = name || null
    emit('update:modelValue', displayValue.value)
    emit('change', displayValue.value)
  }
  showDialog.value = false
}
</script>

<style scoped>
/* 样式保持与原 el-select 一致 */
.select-icon {
  transition: transform 0.3s;
}

.select-icon:hover {
  color: var(--el-color-primary);
}
</style>

