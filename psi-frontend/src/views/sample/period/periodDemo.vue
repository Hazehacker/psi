<template>
  <div class="period area">
    <div class="layout">
      <el-date-picker v-model="settleDate" placeholder="结账日期" value-format="YYYY-MM-DD" type="date" class="date" />
      <el-button-group>
        <el-button type="primary" @click="handleSettle">结账</el-button>
        <el-button type="warning" @click="handleReverse">反结账</el-button>
        <el-button @click="handleRefresh">刷新</el-button>
      </el-button-group>
    </div>

    <el-divider />

    <el-table :data="tableRows" height="calc(100% - 90px)" border>
      <el-table-column prop="settleDate" label="结账日期" align="center" width="200" />
      <el-table-column prop="operateTime" label="操作日期" align="center" width="160" />
      <el-table-column prop="operatorName" label="操作人" align="center" width="160" />
    </el-table>

    <el-pagination
      class="tablePagination"
      :current-page="page.current"
      :total="page.total"
      :page-size="page.size"
      :page-sizes="page.sizes"
      :pager-count="page.count"
      @size-change="onPageSizeChange"
      @current-change="onPageChange"
      layout="prev,pager,next,jumper,sizes,total"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const settleDate = ref('')
const tableRows = ref<any[]>([])
const page = reactive({ current: 1, total: 0, size: 30, sizes: [30, 60, 90, 150, 300], count: 5 })

const initDemo = () => {
  tableRows.value = [
    { settleDate: '2025-10-01', operateTime: '2025-10-01 09:00:00', operatorName: '管理员' },
    { settleDate: '2025-09-01', operateTime: '2025-09-01 09:05:00', operatorName: '管理员' }
  ]
  page.total = tableRows.value.length
}

const handleSettle = () => {
  if (!settleDate.value) {
    ElMessage.warning('请先选择结账日期')
    return
  }
  tableRows.value.unshift({ settleDate: settleDate.value, operateTime: formatDateTime(new Date()), operatorName: '演示用户' })
  page.total = tableRows.value.length
  ElMessage.success('结账成功(演示)')
}

const handleReverse = () => {
  ElMessageBox.confirm('您确定要反结账吗？(演示)', '提示', { type: 'warning' })
    .then(() => {
      tableRows.value.shift()
      page.total = tableRows.value.length
      ElMessage.success('反结账成功(演示)')
    })
    .catch(() => {})
}

const handleRefresh = () => { ElMessage.success('刷新成功(演示)') }
const onPageSizeChange = (size: number) => { page.size = size }
const onPageChange = (p: number) => { page.current = p }

const formatDate = (date: Date) => {
  const y = date.getFullYear()
  const m = `${date.getMonth() + 1}`.padStart(2, '0')
  const d = `${date.getDate()}`.padStart(2, '0')
  return `${y}-${m}-${d}`
}

const formatDateTime = (date: Date) => {
  const h = `${date.getHours()}`.padStart(2, '0')
  const mi = `${date.getMinutes()}`.padStart(2, '0')
  const s = `${date.getSeconds()}`.padStart(2, '0')
  return `${formatDate(date)} ${h}:${mi}:${s}`
}

onMounted(() => {
  settleDate.value = formatDate(new Date())
  initDemo()
})
</script>

<style scoped>
.layout {
  display: flex;
  justify-content: space-between;
}
.date {
  width: 150px;
}
</style>

