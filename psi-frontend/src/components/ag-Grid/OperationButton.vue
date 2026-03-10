<template>
  <div class="operation-button">
    <!-- 如果是空行，显示添加图标 -->
    <el-icon 
      v-if="!hasData" 
      @click="openPopup" 
      style="font-size: 18px; cursor: pointer; color: #409EFF;"
      title="添加商品"
    >
      <Plus />
    </el-icon>
    <!-- 如果是已填充的行，显示删除图标 -->
    <el-icon 
      v-else 
      @click="deleteRow" 
      style="font-size: 18px; cursor: pointer; color: #F56C6C;"
      title="删除商品"
    >
      <Delete />
    </el-icon>
  </div>
</template>

<script setup>
/**
 * Cell Component | 操作按钮组件 (Vue 3)
 * 用途：在单元格中显示添加/删除图标
 * - 空行：显示添加图标，点击后触发商品选择弹窗
 * - 已填充行：显示删除图标，点击后删除该行
 * 参数：props.params 由 ag-Grid 注入，包含 node、data、api、context 等
 */
import { computed } from 'vue'
import { Plus, Delete } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'

// ag-Grid 传入的参数对象
const props = defineProps({ 
  params: { type: Object, required: true } 
})

// 判断当前行是否有数据（通过 key 字段判断）
const hasData = computed(() => {
  return props.params?.data?.key != null
})

/**
 * 打开商品选择弹窗
 * 通过 ag-Grid 的 context.componentParent 来调用父组件的方法
 */
function openPopup() {
  console.log('OperationButton clicked', props.params)
  
  // 方法1: 通过 context.componentParent
  if (props.params?.context?.componentParent) {
    props.params.context.componentParent.handleOpenProductPopup({
      rowIndex: props.params.node.rowIndex,
      rowData: props.params.data,
      api: props.params.api
    })
  }
  // 方法2: 通过 gridOptions api 触发事件
  else if (props.params?.api) {
    props.params.api.dispatchEvent({
      type: 'openProductPopup',
      rowIndex: props.params.node.rowIndex,
      rowData: props.params.data,
      api: props.params.api
    })
  }
}

/**
 * 删除当前行
 */
function deleteRow() {
  console.log('Delete button clicked', props.params)
  
  // 确认删除
  ElMessageBox.confirm(
    '确定要删除这条商品记录吗？',
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      // 用户点击确定，执行删除
      if (props.params?.api && props.params?.node) {
        props.params.api.applyTransaction({
          remove: [props.params.data]
        })
        console.log('Row deleted:', props.params.data)
        
        // 触发 handleGrid 重新计算
        if (props.params?.context?.runHandleGrid) {
          props.params.context.runHandleGrid()
        }
      }
    })
    .catch(() => {
      // 用户点击取消，不执行任何操作
      console.log('Delete cancelled')
    })
}
</script>

<style scoped>
.operation-button {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}
</style>

