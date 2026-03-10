<template>
  <div class="detail-page">
    <h3>销售单据详情</h3>
    <div class="detail-content">
      <div class="line">单据编号：{{ code }}</div>
      <div class="line">所属组织：默认组织</div>
      <div class="line">供应商：示例供应商</div>
      <div class="line">单据时间：2025-10-20</div>
      <div class="line">单据金额：1000.00</div>
      <div class="line">实际金额：950.00</div>
    </div>
    
    <div class="action-buttons">
      <el-button type="success" @click="handleApprove">审核通过</el-button>
      <el-button type="warning" @click="handleReject">审核拒绝</el-button>
      <el-button type="info" @click="handleEdit">编辑单据</el-button>
      <el-button type="danger" @click="handleDelete">删除单据</el-button>
      <el-button type="primary" @click="$router.back()">返回列表</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useOperationLogsStore } from '@/stores/operationLogs'

const route = useRoute()
const router = useRouter()
const code = route.query.code as string

// 使用操作日志管理器
const operationLogsStore = useOperationLogsStore()

// 审核通过
const handleApprove = () => {
  operationLogsStore.addLog(code, '审核通过')
  ElMessage.success('审核通过成功')
}

// 审核拒绝
const handleReject = () => {
  operationLogsStore.addLog(code, '审核拒绝')
  ElMessage.warning('审核拒绝')
}

// 编辑单据
const handleEdit = () => {
  operationLogsStore.addLog(code, '编辑单据')
  ElMessage.info('编辑单据功能')
}

// 删除单据
const handleDelete = () => {
  ElMessageBox.confirm('您确定要删除此单据吗?', '提示', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(() => {
    operationLogsStore.addLog(code, '删除单据')
    ElMessage.success('删除成功')
    // 可以在这里添加实际的删除逻辑
  }).catch(() => {
    // 用户取消删除
  })
}
</script>

<style scoped>
.detail-page {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.detail-content {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
  margin: 20px 0;
}

.line {
  margin: 12px 0;
  font-size: 14px;
  color: #606266;
}

.action-buttons {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-top: 20px;
}

.action-buttons .el-button {
  min-width: 100px;
}
</style>
