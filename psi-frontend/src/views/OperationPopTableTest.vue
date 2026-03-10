<template>
  <div class="test-page">
    <h2>商品选择弹窗测试页</h2>
    <el-button type="primary" @click="openDialog">打开商品弹窗</el-button>

    <!-- 引入主组件 -->
    <OperationPopTable v-if="showDialog" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import OperationPopTable from '@/components/operationpoptable/OperationPopTable.vue'

const showDialog = ref(false)
const openDialog = () => {
  showDialog.value = true
}
</script>

<style scoped>
.test-page {
  padding: 40px;
}
</style>
