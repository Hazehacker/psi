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
// 接口说明：封装于 src/apis/advancedSettings/period/index.ts，路径采用 /period /period/settle /period/reverse
import { queryPeriods, savePeriod, deletePeriod } from '@/apis/advancedSettings/period'
import type { SavePeriodDTO } from '@/apis/advancedSettings/period/type'

const settleDate = ref<string>('')
const tableRows = ref<any[]>([])
const page = reactive({ current: 1, total: 0, size: 30, sizes: [30, 60, 90, 150, 300], count: 5 })

/**
 * 获取结账记录列表
 * - 解析 data.rows 或 data 为数组的两种返回形式
 */
const fetchList = (goFirst?: number) => {
  if (goFirst === 1) page.current = 1
  queryPeriods({ pageIndex: page.current, pageSize: page.size }).then(res => {
    if (res.state === 'success' || res.info) {
      const rows = res.info || []
      tableRows.value = rows.map((item: any) => ({
        settleDate: item.date,
        operateTime: item.time,
        operatorName: item.user || ''
      }))
      page.total = res.count || rows.length
    } else {
      ElMessage.error('获取结账记录失败')
    }
  }).catch(() => ElMessage.error('获取结账记录失败'))
}

/** 结账 */
const handleSettle = () => {
  if (!settleDate.value) {
    ElMessage.warning('请先选择结账日期')
    return
  }
  const settleData: SavePeriodDTO = { data: settleDate.value }
  savePeriod(settleData).then(res => {
    if (res.code === 10000) {
      ElMessage.success('结账成功!')
      fetchList()
    } else {
      ElMessage.error(res.message || '结账失败')
    }
  }).catch(() => ElMessage('请刷新'))
}

/** 反结账 */
const handleReverse = () => {
  ElMessageBox.confirm('您确定要反结账吗?', '提示', { type: 'warning' })
    .then(() => deletePeriod())
    .then(res => {
      if (res.code === 10000) {
        ElMessage.success('反结账成功!')
        fetchList()
      } else {
        ElMessage.error(res.message || '反结账失败')
      }
    })
    .catch(() => ElMessage('请刷新'))
}

const handleRefresh = () => { fetchList(); ElMessage.success('刷新成功!') }
const onPageSizeChange = (size: number) => { page.size = size; fetchList(1) }
const onPageChange = (p: number) => { page.current = p; fetchList() }

const formatDate = (date: Date) => {
  const y = date.getFullYear()
  const m = `${date.getMonth() + 1}`.padStart(2, '0')
  const d = `${date.getDate()}`.padStart(2, '0')
  return `${y}-${m}-${d}`
}

onMounted(() => {
  settleDate.value = formatDate(new Date())
  fetchList(1)
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

