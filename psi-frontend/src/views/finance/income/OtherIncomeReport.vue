<template>
  <div class="other-income-report">
    <!-- 顶部操作栏 -->
    <div class="header-actions">
      <div class="left-actions">
        <!-- 直接使用搜索组件的按钮 -->
        <good-search-form
          v-model="searchFormData"
          :config="searchConfig"
          @search="handleSearchCompatible"
        />
      </div>

      <div class="right-actions">
        <!-- 新增：操作和删除按钮（仅在选中数据时显示） -->
        <div v-if="selectedRows.length > 0" class="batch-action-buttons">
          <el-dropdown @command="handleBatchAction" trigger="click">
            <el-button type="primary" class="batch-action-btn">
              操作<el-icon class="el-icon--right"><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="examine">审核</el-dropdown-item>
                <el-dropdown-item command="unexamine">反审核</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <el-button type="danger" @click="handleBatchDelete" class="batch-delete-btn">
            删除
          </el-button>
        </div>

        <!-- 独立的批量操作按钮 -->
        <el-button type="warning" @click="openBatchDialog" class="custom-batch-btn">
          批量
        </el-button>

        <!-- 刷新按钮 -->
        <el-button type="info" @click="handleRefresh" class="custom-refresh-btn"> 刷新 </el-button>
      </div>
    </div>

    <!-- 批量操作弹窗 -->
    <el-dialog title="批量操作" v-model="batchDialogVisible" width="600px" :show-close="true">
      <div class="batch-content">
        <el-tabs v-model="activeBatchTab" class="batch-tabs">
          <el-tab-pane label="导入数据" name="import">
            <div class="tab-content">
              <div class="tips-list">
                <div class="tip-item">
                  <span class="tip-number">1.</span>
                  <span class="tip-text">该功能适用于批量导入数据。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">2.</span>
                  <span class="tip-text">您需要下载数据模板后使用Excel录入数据。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">3.</span>
                  <span class="tip-text">录入数据时，请勿修改首行数据标题以及排序。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">4.</span>
                  <span class="tip-text">请查阅使用文档获取字段格式内容以及相关导入须知。</span>
                </div>
                <div class="tip-item">
                  <span class="tip-number">5.</span>
                  <span class="tip-text">点击下方上传模板，选择您编辑好的模板文件即可。</span>
                </div>
              </div>
              <div class="button-group">
                <el-button class="download-btn" @click="downloadTemplate">下载模板</el-button>
                <el-upload
                  ref="uploadRef"
                  :auto-upload="false"
                  :show-file-list="false"
                  accept=".xlsx,.xls"
                  :on-change="handleFileChange"
                  :disabled="uploading"
                  style="display: inline-block"
                >
                  <el-button type="primary" class="upload-btn" :loading="uploading">
                    {{ uploading ? '上传中...' : '上传模板' }}
                  </el-button>
                </el-upload>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="导出数据" name="export">
            <div class="tab-content">
              <div class="export-content">
                <el-button type="primary" class="export-btn" @click="handleExportSimple"
                  >导出简单报表</el-button
                >
                <el-button type="success" class="export-btn" @click="handleExportDetail"
                  >导出详细报表</el-button
                >
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-dialog>

    <!-- 表格区域 -->
    <div class="table-container">
      <report-button-table
        ref="reportTable"
        :columns="tableColumns"
        :data="tableData"
        :total="total"
        :current-page="currentPage"
        :page-size="pageSize"
        :loading="loading"
        :show-selection="true"
        :show-operations="true"
        :summary-data="summaryData"
        :row-logs="getRowLogs"
        @selection-change="handleSelectionChange"
        @page-change="handlePageChange"
        @view="handleView"
        @delete="handleDelete"
        @edit="handleEdit"
      >
        <!-- 自定义页脚右侧内容 -->
        <template #footer-right>
          <div class="custom-summary">
            <span class="summary-item">
              <span class="summary-label">总单据金额:</span>
              <span class="summary-value">{{ totalBillAmount }}</span>
            </span>
            <span class="summary-divider">|</span>
            <span class="summary-item">
              <span class="summary-label">总实际金额:</span>
              <span class="summary-value">{{ totalActualAmount }}</span>
            </span>
            <span class="summary-divider">|</span>
            <span class="summary-item">
              <span class="summary-label">总单据收款:</span>
              <span class="summary-value">{{ totalReceiptAmount }}</span>
            </span>
            <span class="summary-divider">|</span>
            <span class="summary-item">
              <span class="summary-label">总核销金额:</span>
              <span class="summary-value">{{ totalVerificationAmount }}</span>
            </span>
          </div>
        </template>

        <!-- 自定义审核状态显示 -->
        <template #auditStatus="{ row }">
          <el-tag :type="getAuditStatusType(row.examine)" as any>
            {{ getAuditStatusText(row.examine) }}
          </el-tag>
        </template>

        <!-- 自定义核销状态显示 -->
        <template #verificationStatus="{ row }">
          <el-tag :type="getVerificationStatusType(row.nucleus)" as any>
            {{ getVerificationStatusText(row.nucleus) }}
          </el-tag>
        </template>

        <!-- 自定义操作列 -->
        <template #operation="{ row }">
          <div class="op-row">
            <el-button size="small" class="op-btn op-gray" @click="openDetail(row)">
              详情
            </el-button>
            <el-button size="small" class="op-btn op-gray" @click="handleSingleDelete(row)">
              删除
            </el-button>
            <el-dropdown trigger="click">
              <el-button size="small" class="op-btn op-gray op-more">
                <el-icon><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu class="info-dropdown">
                  <el-dropdown-item>单据号：{{ row.number }}</el-dropdown-item>
                  <el-dropdown-item>客户：{{ row.customer }}</el-dropdown-item>
                  <el-dropdown-item>金额：{{ formatNumber(row.total) }}</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </template>
      </report-button-table>
    </div>

    <!-- 其它收入单详情弹窗 -->
    <OtherIncomeDetailDialog
      v-model:visible="detailVisible"
      :record="currentRow"
      @save="handleDetailSave"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'

// 导入组件
import GoodSearchForm from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import ReportButtonTable from '@/components/report/reportButtonTable.vue'
import OtherIncomeDetailDialog from './OtherIncomeDetailDialog.vue'

// 导入API
import {
  getOtherIncomeList,
  batchDeleteOtherIncome,
  examineOtherIncome,
  exportSimpleReportExcel,
  exportDetailReportExcel,
  importIncomeData,
  type OtherIncomeItem,
  type OtherIncomeQueryParams
} from '@/apis/finance/income'

// ========== 类型定义 ==========
// 表格行数据类型 - 修复：直接使用 OtherIncomeItem 并扩展必要的字段
interface TableRow extends OtherIncomeItem {
  // 添加前端需要的额外字段
  checkStatus?: string
}

// 搜索表单数据类型
interface SearchFormData {
  customer?: string | null
  number?: string
  startTime?: string
  endTime?: string
  people?: string | null
  user?: string | null
  examine?: string
  data?: string
  account?: string | null
  verificationStatus?: string
  auditStatus?: string
  [key: string]: any
}

// 表格列配置类型
interface TableColumn {
  prop: string
  label: string
  width?: string
  minWidth?: string
  sortable?: boolean
  align?: 'left' | 'center' | 'right'
  slot?: string
  showOverflowTooltip?: boolean
  fixed?: 'left' | 'right'
}

// 搜索配置类型
interface SearchConfig {
  inline: boolean
  showGoods: boolean
  showSupplier: boolean
  showCustomer: boolean
  showWarehouse: boolean
  showAccount: boolean
  showPeople: boolean
  showUser: boolean
  showNumber: boolean
  showBillDate: boolean
  showArrivalDate: boolean
  showExamine: boolean
  showState: boolean
  showRemark: boolean
  customFields: CustomFieldConfig[]
}

interface CustomFieldConfig {
  key: string
  label: string
  type: 'select' | 'input' | 'date' | 'nodList' | 'treeSelect'
  options?: Array<{ label: string; value: string | number }>
  multiple?: boolean
  collapseTags?: boolean
  nodListConfig?: any
  treeData?: any[]
  checkStrictly?: boolean
  renderAfterExpand?: boolean
  clearable?: boolean
}

// ========== 搜索相关 ==========
const searchFormData = ref<SearchFormData>({
  customer: null,
  number: '',
  startTime: '',
  endTime: '',
  people: null,
  user: null,
  examine: '',
  data: '',
  account: null,
  verificationStatus: '',
  auditStatus: ''
})

// 搜索配置
const searchConfig = reactive<SearchConfig>({
  inline: false,
  showGoods: false,
  showSupplier: false,
  showCustomer: true,
  showWarehouse: false,
  showAccount: true,
  showPeople: true,
  showUser: true,
  showNumber: true,
  showBillDate: true,
  showArrivalDate: false,
  showExamine: false,
  showState: false,
  showRemark: true,
  customFields: [
    {
      key: 'auditStatus',
      label: '请选择审核状态',
      type: 'select',
      options: [
        { label: '未审核', value: '0' },
        { label: '已审核', value: '1' }
      ],
      clearable: true
    },
    {
      key: 'verificationStatus',
      label: '请选择核销状态',
      type: 'select',
      options: [
        { label: '未核销', value: '0' },
        { label: '部分核销', value: '1' },
        { label: '已核销', value: '2' }
      ],
      clearable: true
    }
  ]
})

// 搜索参数 - 修复：提供默认值
const currentSearchParams = ref<Partial<OtherIncomeQueryParams>>({})

// 主要的搜索处理函数
const handleSearch = (formData: SearchFormData) => {
  const searchParams: Partial<OtherIncomeQueryParams> = {
    customer: formData.customer || undefined,
    number: formData.number,
    startDate: formData.startTime,
    endDate: formData.endTime,
    people: formData.people || undefined,
    user: formData.user || undefined,
    examine: formData.auditStatus ? parseInt(formData.auditStatus) : undefined,
    data: formData.data,
    account: formData.account || undefined,
    nucleus: formData.verificationStatus ? parseInt(formData.verificationStatus) : undefined
  }

  console.log('搜索表单数据:', formData)
  console.log('转换后的搜索参数:', searchParams)

  currentSearchParams.value = searchParams
  currentPage.value = 1
  fetchTableData(searchParams)
  ElMessage.success('搜索成功')
}

// 兼容的事件处理函数
const handleSearchCompatible = (value: any) => {
  handleSearch(value as SearchFormData)
}

// ========== 表格相关 ==========
const reportTable = ref()
const tableData = ref<TableRow[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(20)
const loading = ref(false)
const selectedRows = ref<TableRow[]>([])

// 计算选中的ID列表
const selectedIds = computed(() => selectedRows.value.map((row) => row.id))

// 表格列配置 - 修复：移除不存在的 checkStatus 字段
const tableColumns = ref<TableColumn[]>([
  { prop: 'frame', label: '所属组织', width: '120', align: 'center' },
  { prop: 'customer', label: '客户', width: '120', align: 'center' },
  { prop: 'time', label: '单据时间', width: '120', align: 'center', sortable: true },
  { prop: 'number', label: '单据编号', width: '150', align: 'center' },
  { prop: 'total', label: '单据金额', width: '100', align: 'right', sortable: true },
  { prop: 'actual', label: '实际金额', width: '100', align: 'right', sortable: true },
  { prop: 'money', label: '单据收款', width: '100', align: 'right', sortable: true },
  { prop: 'writeOffAmount', label: '核销金额', width: '100', align: 'right', sortable: true },
  { prop: 'account', label: '结算账户', width: '120', align: 'center' },
  { prop: 'people', label: '关联人员', width: '100', align: 'center' },
  {
    prop: 'examine',
    label: '审核状态',
    width: '100',
    align: 'center',
    slot: 'auditStatus'
  },
  {
    prop: 'nucleus',
    label: '核销状态',
    width: '100',
    align: 'center',
    slot: 'verificationStatus'
  },
  // 移除不存在的 checkStatus 字段
  { prop: 'user', label: '制单人', width: '100', align: 'center' },
  { prop: 'data', label: '备注信息', width: '150', align: 'left', showOverflowTooltip: true },
  {
    prop: 'operation',
    label: '相关操作',
    width: '170',
    align: 'center',
    slot: 'operation',
    fixed: 'right' as const
  }
])

// 汇总数据
const summaryData = computed(() => {
  return [
    { label: '总单据金额', value: totalBillAmount.value },
    { label: '总实际金额', value: totalActualAmount.value },
    { label: '总单据收款', value: totalReceiptAmount.value },
    { label: '总核销金额', value: totalVerificationAmount.value }
  ]
})

// 计算总单据金额
const totalBillAmount = computed(() => {
  return tableData.value
    .reduce((sum: number, item: TableRow) => sum + (item.total || 0), 0)
    .toFixed(2)
})

// 计算总实际金额
const totalActualAmount = computed(() => {
  return tableData.value
    .reduce((sum: number, item: TableRow) => sum + (item.actual || 0), 0)
    .toFixed(2)
})

// 计算总单据收款
const totalReceiptAmount = computed(() => {
  return tableData.value
    .reduce((sum: number, item: TableRow) => sum + (item.money || 0), 0)
    .toFixed(2)
})

// 计算总核销金额
const totalVerificationAmount = computed(() => {
  return tableData.value
    .reduce((sum: number, item: TableRow) => sum + (item.writeOffAmount || 0), 0)
    .toFixed(2)
})

// ========== 批量操作相关 ==========
const batchDialogVisible = ref(false)
const activeBatchTab = ref<'import' | 'export'>('import')
const uploading = ref(false)
const uploadRef = ref()

// 打开批量操作弹窗
const openBatchDialog = () => {
  batchDialogVisible.value = true
  activeBatchTab.value = 'import'
}

// 下载模板
const downloadTemplate = () => {
  // 这里可以实现模板下载逻辑
  ElMessage.info('模板下载功能待实现')
}

// 处理文件上传
const handleFileChange = async (file: any) => {
  if (!file.raw) return

  uploading.value = true
  try {
    const response = await importIncomeData(file.raw)
    if (response.code === 10000) {
      ElMessage.success('导入成功')
      batchDialogVisible.value = false
      handleRefresh()
    } else {
      ElMessage.error(response.message || '导入失败')
    }
  } catch (error) {
    console.error('导入失败:', error)
    ElMessage.error('导入失败，请稍后重试')
  } finally {
    uploading.value = false
  }
}

// 导出简单报表
const handleExportSimple = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要导出的数据')
    return
  }

  try {
    const orderId = selectedRows.value[0].id
    const response = await exportSimpleReportExcel([orderId])

    if (response instanceof Blob) {
      // 创建下载链接
      const url = window.URL.createObjectURL(response)
      const link = document.createElement('a')
      link.href = url
      link.download = `其他收入单简单报表_${new Date().getTime()}.xlsx`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
      ElMessage.success('导出成功')
      batchDialogVisible.value = false
    } else {
      ElMessage.error('导出失败：响应格式不正确')
    }
  } catch (error) {
    console.error('导出简单报表失败:', error)
    ElMessage.error('导出失败，请稍后重试')
  }
}

// 导出详细报表
const handleExportDetail = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要导出的数据')
    return
  }

  try {
    const ids = selectedRows.value.map((row) => row.id)
    const response = await exportDetailReportExcel(ids)

    if (response instanceof Blob) {
      // 创建下载链接
      const url = window.URL.createObjectURL(response)
      const link = document.createElement('a')
      link.href = url
      link.download = `其他收入单详细报表_${new Date().getTime()}.zip`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
      ElMessage.success('导出成功')
      batchDialogVisible.value = false
    } else {
      ElMessage.error('导出失败：响应格式不正确')
    }
  } catch (error) {
    console.error('导出详细报表失败:', error)
    ElMessage.error('导出失败，请稍后重试')
  }
}

// ========== 数据获取 ==========
const fetchTableData = async (searchParams: Partial<OtherIncomeQueryParams> = {}) => {
  loading.value = true
  try {
    const response = await getOtherIncomeList({
      ...searchParams,
      pageIndex: currentPage.value,
      pageSize: pageSize.value
    })

    if (response.code === 10000 && response.data) {
      // 修复：直接使用响应数据，添加前端需要的字段
      tableData.value = (response.data.rows || []).map((item) => ({
        ...item,
        // 如果后端没有返回 checkStatus，可以在这里设置默认值
        checkStatus: item.checkStatus || '未核对'
      }))
      total.value = response.data.total || 0
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// ========== 状态显示函数 ==========
// 审核状态
const getAuditStatusText = (status: number): string => {
  const statusMap: { [key: number]: string } = {
    0: '未审核',
    1: '已审核'
  }
  return statusMap[status] || '未知状态'
}

// 修复：返回 Element Plus 支持的标签类型
const getAuditStatusType = (
  status: number
): 'warning' | 'success' | 'info' | 'danger' | 'primary' => {
  const typeMap: { [key: number]: 'warning' | 'success' | 'info' | 'danger' | 'primary' } = {
    0: 'warning',
    1: 'success'
  }
  return typeMap[status] || 'info'
}

// 核销状态
const getVerificationStatusText = (status: number): string => {
  const statusMap: { [key: number]: string } = {
    0: '未核销',
    1: '部分核销',
    2: '已核销'
  }
  return statusMap[status] || '未知状态'
}

// 修复：返回 Element Plus 支持的标签类型
const getVerificationStatusType = (
  status: number
): 'warning' | 'success' | 'info' | 'danger' | 'primary' => {
  const typeMap: { [key: number]: 'warning' | 'success' | 'info' | 'danger' | 'primary' } = {
    0: 'danger',
    1: 'warning',
    2: 'success'
  }
  return typeMap[status] || 'info'
}

// ========== 详情弹框相关 ==========
const detailVisible = ref(false)
const currentRow = ref<TableRow | null>(null)

const openDetail = (row: TableRow) => {
  currentRow.value = row
  detailVisible.value = true
}

const handleDetailSave = (data: any) => {
  console.log('保存详情数据:', data)
  ElMessage.success('详情已保存')
  detailVisible.value = false
  // 刷新表格数据
  fetchTableData(currentSearchParams.value)
}

// ========== 操作日志功能 ==========
const getRowLogs = (row: TableRow) => {
  return [
    {
      time: new Date().toLocaleString(),
      user: '系统',
      action: `单据号: ${row.number}`
    },
    {
      time: new Date().toLocaleString(),
      user: '系统',
      action: `客户: ${row.customer}`
    },
    {
      time: new Date().toLocaleString(),
      user: '系统',
      action: `金额: ${row.total.toFixed(2)}`
    }
  ]
}

// ========== 新增：批量操作处理 ==========
// 批量操作处理
const handleBatchAction = async (command: string) => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要操作的数据')
    return
  }

  try {
    const ids = selectedRows.value.map((row) => row.id)
    const examine = command === 'examine' ? 1 : 0
    const actionText = command === 'examine' ? '审核' : '反审核'

    await ElMessageBox.confirm(
      `确定要${actionText}选中的 ${selectedRows.value.length} 条数据吗？`,
      `确认${actionText}`,
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await examineOtherIncome({ ids, examine })

    if (response.code === 10000) {
      ElMessage.success(`${actionText}成功`)
      // 刷新数据
      fetchTableData(currentSearchParams.value)
      // 清空选择
      selectedRows.value = []
    } else {
      ElMessage.error(response.message || `${actionText}失败`)
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量操作失败:', error)
      ElMessage.error('操作失败，请稍后重试')
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要删除的数据')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 条数据吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const ids = selectedRows.value.map((row) => row.id)
    const response = await batchDeleteOtherIncome(ids)

    if (response.code === 10000) {
      ElMessage.success('删除成功')
      // 刷新数据
      fetchTableData(currentSearchParams.value)
      // 清空选择
      selectedRows.value = []
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('删除失败，请稍后重试')
    }
  }
}

// 单独删除行数据
const handleSingleDelete = async (row: TableRow) => {
  try {
    await ElMessageBox.confirm(`确定要删除单据 "${row.number}" 吗？`, '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const response = await batchDeleteOtherIncome([row.id])

    if (response.code === 10000) {
      ElMessage.success('删除成功')
      // 刷新数据
      fetchTableData(currentSearchParams.value)
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败，请稍后重试')
    }
  }
}

// ========== 事件处理 ==========
// 选择变化
const handleSelectionChange = (selection: TableRow[]) => {
  selectedRows.value = selection
}

// 分页变化
const handlePageChange = (page: number, size: number) => {
  currentPage.value = page
  pageSize.value = size
  fetchTableData(currentSearchParams.value)
}

// 查看详情 - 兼容性函数
const handleView = (row: TableRow) => {
  openDetail(row)
}

// 删除单行
const handleDelete = async (rows: TableRow[]) => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${rows.length} 条数据吗？`, '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const ids = rows.map((row) => row.id)
    const response = await batchDeleteOtherIncome(ids)

    if (response.code === 10000) {
      ElMessage.success('删除成功')
      fetchTableData(currentSearchParams.value)
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败，请稍后重试')
    }
  }
}

// 编辑
const handleEdit = (row: TableRow) => {
  console.log('编辑:', row)
  ElMessage.info(`编辑单据: ${row.number}`)
}

// 刷新
const handleRefresh = () => {
  searchFormData.value = {
    customer: null,
    number: '',
    startTime: '',
    endTime: '',
    people: null,
    user: null,
    examine: '',
    data: '',
    account: null,
    verificationStatus: '',
    auditStatus: ''
  }

  currentSearchParams.value = {}
  currentPage.value = 1
  fetchTableData()
  ElMessage.success('数据已刷新')
}

// 工具函数：数字格式化
const formatNumber = (n: number | undefined | null) => {
  const num = Number(n ?? 0)
  return Number.isFinite(num) ? num.toLocaleString() : '0'
}

// ========== 生命周期 ==========
onMounted(() => {
  fetchTableData()
})
</script>
<style scoped lang="scss">
.other-income-report {
  padding: 16px;
  background: #f5f7fa;
  min-height: 100vh;
}

.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
  padding: 16px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .left-actions {
    :deep(.el-popover__reference) {
      .el-button {
        background: #409eff;
        border-color: #409eff;
        color: #fff;

        &:hover {
          background: #66b1ff;
          border-color: #66b1ff;
        }
      }
    }
  }

  .right-actions {
    display: flex;
    gap: 8px;
    align-items: center;
  }
}

.batch-action-buttons {
  display: flex;
  gap: 8px;
  margin-right: 8px;

  .batch-action-btn,
  .batch-delete-btn {
    height: 32px;
    padding: 8px 15px;
  }
}

.custom-batch-btn,
.custom-refresh-btn {
  margin-left: 8px;
}

.table-container {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.custom-summary {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #606266;

  .summary-item {
    display: flex;
    align-items: center;

    .summary-label {
      margin-right: 4px;
      color: #606266;
    }

    .summary-value {
      color: #303133;
      font-weight: 500;
    }
  }

  .summary-divider {
    margin: 0 8px;
    color: #dcdfe6;
  }
}

/* 操作列按钮 */
.op-row {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
.op-btn {
  height: 28px;
  line-height: 26px;
  background: #fff;
  color: #606266;
  border: 1px solid #dcdfe6;
  border-radius: 0;
  padding: 0 10px;
  transition: all 0.15s ease;
}
.op-row > * + * {
  margin-left: -1px;
}
.op-row :deep(.el-dropdown .el-button) {
  border-radius: 0 !important;
}
.op-row .op-btn:hover {
  border-color: #409eff;
  color: #409eff;
}

/* 批量操作弹窗样式 */
.batch-content {
  padding: 0;
}

.batch-tabs {
  margin-top: 0;
}

.tab-content {
  padding: 20px 0;
}

.tips-list {
  margin-bottom: 30px;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
  line-height: 1.5;
}

.tip-number {
  color: #409eff;
  font-weight: 600;
  margin-right: 8px;
  min-width: 20px;
}

.tip-text {
  color: #333;
  font-size: 14px;
  flex: 1;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 20px;
}

.download-btn {
  background-color: #f5f5f5;
  border-color: #d9d9d9;
  color: #333;
  min-width: 100px;
}

.upload-btn {
  min-width: 100px;
}

.export-content {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  min-height: 200px;
}

.export-btn {
  min-width: 120px;
  height: 40px;
  font-size: 16px;
}

/* 标签页样式调整 */
:deep(.el-tabs__header) {
  margin: 0 0 20px 0;
}

:deep(.el-tabs__nav-wrap::after) {
  background-color: #e4e7ed;
}

:deep(.el-tabs__active-bar) {
  background-color: #409eff;
}

:deep(.el-tabs__item.is-active) {
  color: #409eff;
}

:deep(.el-tabs__item) {
  font-size: 14px;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-actions {
    flex-direction: column;
    gap: 12px;

    .left-actions,
    .right-actions {
      width: 100%;
      justify-content: center;
    }

    .right-actions {
      flex-wrap: wrap;
    }
  }

  .batch-action-buttons {
    margin-right: 0;
    margin-bottom: 8px;
    justify-content: center;
  }

  .custom-summary {
    flex-wrap: wrap;
    justify-content: center;

    .summary-item {
      margin: 0 4px;
    }
  }

  .export-content {
    flex-direction: column;
    gap: 12px;
  }
}
</style>
