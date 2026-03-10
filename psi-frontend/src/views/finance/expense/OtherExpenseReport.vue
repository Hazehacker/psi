<template>
  <div class="other-expense-report">
    <!-- 顶部操作栏 -->
    <div class="header-actions">
      <div class="left-actions">
        <!-- 直接使用搜索组件的按钮 -->
        <good-search-form
          v-model="searchFormData"
          :config="searchConfig"
          @search="handleSearchCompatible"
        />

        <!-- 自定义批量按钮（替代按钮组件中的批量按钮） -->
        <el-button type="warning" @click="openCustomBatchDialog" class="custom-batch-btn">
          批量
        </el-button>
      </div>

      <div class="right-actions">
        <!-- 右上角操作按钮组（当有选中行时显示） -->
        <div v-if="selectedRows.length > 0" class="selection-actions">
          <el-dropdown @command="handleOperationCommand" class="operation-dropdown">
            <el-button type="primary" class="operation-btn">
              操作<el-icon class="el-icon--right"><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="approve">审核</el-dropdown-item>
                <el-dropdown-item command="unapprove">反审核</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <el-button type="danger" @click="handleBatchDelete" class="batch-delete-btn">
            删除
          </el-button>
        </div>

        <!-- 使用按钮组件（隐藏批量按钮） -->
        <ActionButtons
          :show-add="false"
          :show-batch="false"
          :show-refresh="true"
          export-file-name="其它支出单报表"
          :table-columns="tableColumns"
          template-file-name="其它支出单导入模板"
          :import-api="importOtherExpense"
          :export-simple-report-api="handleExportSimpleReportForActionButtons"
          :export-detail-report-api="handleExportDetailReportForActionButtons"
          :selected-order-ids="selectedIds"
          @refresh="handleRefresh"
          @import-data="handleImportData"
        />
      </div>
    </div>

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
              <span class="summary-label">总单据付款:</span>
              <span class="summary-value">{{ totalPaymentAmount }}</span>
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
          <el-tag :type="getAuditStatusType(row.examine) as any">
            {{ getAuditStatusText(row.examine) }}
          </el-tag>
        </template>

        <!-- 自定义核销状态显示 -->
        <template #verificationStatus="{ row }">
          <el-tag :type="getVerificationStatusType(row.nucleus) as any">
            {{ getVerificationStatusText(row.nucleus) }}
          </el-tag>
        </template>

        <!-- 自定义操作列 -->
        <template #operation="{ row }">
          <div class="op-row">
            <el-button size="small" class="op-btn op-gray" @click="openDetail(row)">
              详情
            </el-button>
            <el-button size="small" class="op-btn op-gray" @click="onDelete(row)"> 删除 </el-button>
            <el-dropdown trigger="click">
              <el-button size="small" class="op-btn op-gray op-more">
                <el-icon><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu class="info-dropdown">
                  <el-dropdown-item>单据号：{{ row.number }}</el-dropdown-item>
                  <el-dropdown-item>供应商：{{ row.supplier }}</el-dropdown-item>
                  <el-dropdown-item>金额：{{ formatNumber(row.total) }}</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </template>
      </report-button-table>
    </div>

    <!-- 其它支出单详情弹窗 -->
    <OtherExpenseDetailDialog
      v-model:visible="detailVisible"
      :record="currentRow"
      @save="handleDetailSave"
    />

    <!-- 自定义批量操作弹窗 -->
    <el-dialog title="批量操作" v-model="customBatchDialogVisible" width="600px" :show-close="true">
      <div class="batch-content">
        <el-tabs v-model="activeTab" class="batch-tabs">
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
                  accept=".xlsx,.xls,.csv"
                  :on-change="handleFileChange"
                  :disabled="uploading"
                  style="display: inline-block"
                >
                  <el-button type="primary" class="upload-btn" :loading="uploading">
                    {{ uploading ? '上传中...' : '上传模板' }}
                  </el-button>
                </el-upload>
              </div>

              <!-- 上传数据预览 -->
              <div v-if="uploadedData.length > 0" class="upload-preview">
                <h4>上传数据预览 ({{ uploadedData.length }} 条记录)</h4>
                <el-table
                  :data="uploadedData.slice(0, 5)"
                  border
                  style="width: 100%; margin-top: 10px"
                >
                  <el-table-column
                    v-for="col in matchedColumns"
                    :key="col.prop"
                    :prop="col.prop"
                    :label="col.label"
                    :width="col.width"
                  />
                </el-table>
                <div v-if="uploadedData.length > 5" class="preview-note">
                  仅显示前5条记录，共{{ uploadedData.length }}条
                </div>
                <div class="preview-actions">
                  <el-button type="primary" @click="confirmImport">确认导入</el-button>
                  <el-button @click="clearUploadedData">取消</el-button>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="导出数据" name="export">
            <div class="tab-content">
              <div class="export-content">
                <el-button
                  type="primary"
                  class="export-btn"
                  @click="exportSimpleReportInDialog"
                  :disabled="selectedIds.length === 0"
                >
                  导出简单报表
                </el-button>
                <el-button
                  type="success"
                  class="export-btn"
                  @click="exportDetailReportInDialog"
                  :disabled="selectedIds.length === 0"
                >
                  导出详细报表
                </el-button>
              </div>
              <div v-if="selectedIds.length === 0" class="export-tip">请先选择要导出的数据</div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'

// 导入组件
import GoodSearchForm from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import ReportButtonTable from '@/components/report/reportButtonTable.vue'
import ActionButtons from '@/components/operationbuttons/OperationButtons.vue'
import OtherExpenseDetailDialog from './OtherExpenseDetailDialog.vue'

// 导入API
import {
  getOtherExpenseList,
  batchDeleteOtherExpense,
  examineOtherExpense,
  exportOtherExpense,
  exportOtherExpenseDetail,
  importOtherExpense,
  getOtherExpenseDetail,
  updateOtherExpense
} from '@/apis/finance/expense'
import type {
  OtherExpenseQuery,
  OtherExpenseListData,
  OtherExpenseDetail,
  ExamineParams
} from '@/apis/finance/expense/type'

// ========== 类型定义 ==========
// 表格行数据类型
interface TableRow extends OtherExpenseListData {
  organization?: string
  checkStatus?: string
  creator?: string
}

// 搜索表单数据类型
interface SearchFormData {
  supplier?: string | null
  number?: string
  startTime?: string
  endTime?: string
  people?: string | null
  user?: string | null
  examine?: string
  data?: string
  account?: string | null
  nucleus?: string
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

// 搜索参数类型
interface SearchParams {
  supplier?: string
  number?: string
  startDate?: string
  endDate?: string
  people?: string
  user?: string
  examine?: number
  data?: string
  account?: string
  nucleus?: number
  pageIndex?: number
  pageSize?: number
}

// 自定义字段配置类型
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

// ========== 搜索相关 ==========
const searchFormData = ref<SearchFormData>({
  supplier: null,
  number: '',
  startTime: '',
  endTime: '',
  people: null,
  user: null,
  examine: '',
  data: '',
  account: null,
  nucleus: ''
})

// 搜索配置
const searchConfig = reactive<SearchConfig>({
  inline: false,
  showGoods: false,
  showSupplier: true,
  showCustomer: false,
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
      key: 'examine',
      label: '请选择审核状态',
      type: 'select',
      options: [
        { label: '未审核', value: 1 },
        { label: '已审核', value: 2 }
      ],
      clearable: true
    },
    {
      key: 'nucleus',
      label: '请选择核销状态',
      type: 'select',
      options: [
        { label: '未核销', value: 1 },
        { label: '部分核销', value: 2 },
        { label: '已核销', value: 3 }
      ],
      clearable: true
    }
  ]
})

// 搜索参数
const currentSearchParams = ref<SearchParams>({})

// 主要的搜索处理函数
const handleSearch = (formData: SearchFormData) => {
  const searchParams: SearchParams = {
    supplier: formData.supplier || undefined,
    number: formData.number,
    startDate: formData.startTime,
    endDate: formData.endTime,
    people: formData.people || undefined,
    user: formData.user || undefined,
    examine: formData.examine ? parseInt(formData.examine) : undefined,
    data: formData.data,
    account: formData.account || undefined,
    nucleus: formData.nucleus ? parseInt(formData.nucleus) : undefined,
    pageIndex: 1,
    pageSize: pageSize.value
  }

  console.log('搜索表单数据:', formData)
  console.log('转换后的搜索参数:', searchParams)

  currentSearchParams.value = searchParams
  currentPage.value = 1
  fetchTableData(searchParams)
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

// 选中的ID列表
const selectedIds = computed(() => selectedRows.value.map((row) => row.id!))

// 表格列配置
const tableColumns = ref<TableColumn[]>([
  { prop: 'frame', label: '所属组织', width: '120', align: 'center' },
  { prop: 'supplier', label: '供应商', width: '120', align: 'center' },
  { prop: 'time', label: '单据时间', width: '120', align: 'center', sortable: true },
  { prop: 'number', label: '单据编号', width: '150', align: 'center' },
  { prop: 'total', label: '单据金额', width: '100', align: 'right', sortable: true },
  { prop: 'actual', label: '实际金额', width: '100', align: 'right', sortable: true },
  { prop: 'money', label: '实付金额', width: '100', align: 'right', sortable: true },
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
  { prop: 'checkStatus', label: '核对状态', width: '100', align: 'center' },
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
    { label: '总实付金额', value: totalPaymentAmount.value },
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

// 计算总实付金额
const totalPaymentAmount = computed(() => {
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

// 数据获取函数 - 添加更好的错误处理
const fetchTableData = async (searchParams: SearchParams = {}) => {
  loading.value = true
  try {
    const queryParams: OtherExpenseQuery = {
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      ...searchParams
    }

    const response = await getOtherExpenseList(queryParams)

    if (response.code === 10000 && response.data) {
      tableData.value = (response.data.rows || []) as TableRow[]
      total.value = response.data.total || 0
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// ========== 状态显示函数 ==========
// 审核状态显示函数
const getAuditStatusText = (status: number | undefined): string => {
  // 确保处理所有可能的状态值
  if (status === undefined || status === null) {
    return '未知状态'
  }

  const statusMap: { [key: number]: string } = {
    1: '未审核',
    2: '已审核'
  }

  return statusMap[status] || '未知状态'
}

const getAuditStatusType = (status: number | undefined): string => {
  if (status === undefined || status === null) {
    return 'info'
  }

  const typeMap: { [key: number]: string } = {
    1: 'warning', // 未审核 - 黄色
    2: 'success' // 已审核 - 绿色
  }

  return typeMap[status] || 'info'
}

// 核销状态显示函数
const getVerificationStatusText = (status: number | undefined): string => {
  if (status === undefined || status === null) {
    return '未知状态'
  }

  const statusMap: { [key: number]: string } = {
    1: '未核销',
    2: '部分核销',
    3: '已核销'
  }

  return statusMap[status] || '未知状态'
}

const getVerificationStatusType = (status: number | undefined): string => {
  if (status === undefined || status === null) {
    return 'info'
  }

  const typeMap: { [key: number]: string } = {
    1: 'danger', // 未核销 - 红色
    2: 'warning', // 部分核销 - 黄色
    3: 'success' // 已核销 - 绿色
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

// 详情保存后的处理 - 修复状态同步
const handleDetailSave = async (data: any) => {
  try {
    // 调用更新接口
    const result = await updateOtherExpense(data)

    if (result.code === 10000) {
      ElMessage.success('详情已保存')
      detailVisible.value = false

      // 如果详情中包含审核状态变化，也更新本地数据
      if (data.otherExpense && data.otherExpense.id) {
        const updatedId = data.otherExpense.id
        const newExamineStatus = data.otherExpense.examine

        // 更新本地数据
        tableData.value = tableData.value.map((row) => {
          if (row.id === updatedId) {
            return {
              ...row,
              examine: newExamineStatus
            }
          }
          return row
        })
      }

      // 同时刷新表格数据确保数据一致性
      await fetchTableData(currentSearchParams.value)
    } else {
      ElMessage.error(result.message || '保存失败')
    }
  } catch (error: any) {
    console.error('保存失败:', error)
    let errorMessage = '保存失败'
    if (error?.data?.message) {
      errorMessage = error.data.message
    } else if (error?.message) {
      errorMessage = error.message
    }
    ElMessage.error(errorMessage)
  }
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
      action: `供应商: ${row.supplier}`
    },
    {
      time: new Date().toLocaleString(),
      user: '系统',
      action: `金额: ${row.total?.toFixed(2)}`
    }
  ]
}

// ========== 右上角操作按钮功能 ==========
// 操作命令处理 - 修复审核状态同步问题
const handleOperationCommand = async (command: string) => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要操作的数据')
    return
  }

  try {
    const params: ExamineParams = {
      ids: selectedIds.value,
      approve: command === 'approve'
    }

    console.log('执行审核操作:', command, 'IDs:', selectedIds.value)

    const result = await examineOtherExpense(params)

    if (result.code === 10000) {
      ElMessage.success(`${command === 'approve' ? '审核' : '反审核'}成功`)

      // 立即更新本地数据状态，避免等待网络请求
      updateLocalDataStatus(selectedIds.value, command === 'approve')

      // 同时刷新表格数据确保数据一致性
      await fetchTableData(currentSearchParams.value)
    } else {
      ElMessage.error(result.message || '操作失败')
    }
  } catch (error: any) {
    console.error('操作失败:', error)
    // 提供更具体的错误信息
    let errorMessage = '操作失败'
    if (error?.data?.message) {
      errorMessage = error.data.message
    } else if (error?.message) {
      errorMessage = error.message
    }
    ElMessage.error(errorMessage)
  }
}
// 更新本地数据状态（避免等待网络请求刷新）
const updateLocalDataStatus = (ids: string[], isApprove: boolean) => {
  const newExamineStatus = isApprove ? 2 : 1 // 2=已审核, 1=未审核

  tableData.value = tableData.value.map((row) => {
    if (ids.includes(row.id!)) {
      return {
        ...row,
        examine: newExamineStatus
      }
    }
    return row
  })

  console.log(`本地数据状态已更新: ${ids.length} 条数据设置为 ${isApprove ? '已审核' : '未审核'}`)
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

    await batchDeleteOtherExpense(selectedIds.value)
    ElMessage.success('删除成功')
    // 刷新数据
    fetchTableData(currentSearchParams.value)
    // 清空选中
    selectedRows.value = []
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// ========== 自定义批量操作功能 ==========
const customBatchDialogVisible = ref(false)
const activeTab = ref<'import' | 'export'>('import')
const uploading = ref(false)
const uploadedData = ref<any[]>([])
const matchedColumns = ref<any[]>([])

// 打开自定义批量对话框
const openCustomBatchDialog = () => {
  customBatchDialogVisible.value = true
  activeTab.value = 'import'
}

// 下载模板
const downloadTemplate = () => {
  // 这里可以调用后端接口下载模板，或者使用前端生成的模板
  ElMessage.info('模板下载功能待实现')
}

// 处理文件上传
const handleFileChange = async (file: any) => {
  if (!file.raw) return

  uploading.value = true
  try {
    const result = await importOtherExpense(file.raw)
    if (result.code === 10000) {
      ElMessage.success(
        `导入成功: ${result.data?.successCount} 条，失败: ${result.data?.failCount} 条`
      )
      customBatchDialogVisible.value = false
      // 刷新数据
      fetchTableData(currentSearchParams.value)
    } else {
      ElMessage.error(result.message || '导入失败')
    }
  } catch (error) {
    console.error('导入失败:', error)
    ElMessage.error('导入失败')
  } finally {
    uploading.value = false
  }
}

// 确认导入
const confirmImport = () => {
  // 这里处理确认导入逻辑
  ElMessage.info('确认导入功能待实现')
}

// 清空上传数据
const clearUploadedData = () => {
  uploadedData.value = []
  matchedColumns.value = []
}

// ========== 导出功能 ==========
// 导出简单报表 - 适配 ActionButtons 组件的函数签名（接收单个 orderId）
const handleExportSimpleReportForActionButtons = async (orderId: string | number): Promise<any> => {
  console.log('导出简单报表，orderId:', orderId)

  // 如果传入了单个 orderId，就使用它；否则使用选中的所有 ID
  const ids = orderId ? [orderId] : selectedIds.value

  if (ids.length === 0) {
    ElMessage.warning('请先选择要导出的数据')
    return
  }

  return handleExportSimpleReport(ids)
}

// 导出详细报表 - 适配 ActionButtons 组件的函数签名（接收数组）
const handleExportDetailReportForActionButtons = async (ids: (string | number)[]): Promise<any> => {
  console.log('导出详细报表，ids:', ids)

  if (ids.length === 0) {
    ElMessage.warning('请先选择要导出的数据')
    return
  }

  return handleExportDetailReport(ids)
}

// 导出简单报表
const handleExportSimpleReport = async (ids?: (string | number)[]) => {
  const exportIds = ids || selectedIds.value
  if (exportIds.length === 0) {
    ElMessage.warning('请先选择要导出的数据')
    return
  }

  try {
    const blob = await exportOtherExpense(exportIds as string[])
    // 创建下载链接
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `其它支出单报表_${new Date().getTime()}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success(`导出成功，共 ${exportIds.length} 条数据`)
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}

// 导出详细报表
const handleExportDetailReport = async (ids?: (string | number)[]) => {
  const exportIds = ids || selectedIds.value
  if (exportIds.length === 0) {
    ElMessage.warning('请先选择要导出的数据')
    return
  }

  try {
    const blob = await exportOtherExpenseDetail(exportIds as string[])
    // 创建下载链接
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `其它支出单明细报表_${new Date().getTime()}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success(`导出成功，共 ${exportIds.length} 条数据`)
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}

// 弹窗中的导出按钮处理函数
const exportSimpleReportInDialog = () => {
  handleExportSimpleReport()
}

const exportDetailReportInDialog = () => {
  handleExportDetailReport()
}

// ========== 事件处理 ==========
// 刷新
const handleRefresh = () => {
  searchFormData.value = {
    supplier: null,
    number: '',
    startTime: '',
    endTime: '',
    people: null,
    user: null,
    examine: '',
    data: '',
    account: null,
    nucleus: ''
  }

  currentSearchParams.value = {}
  currentPage.value = 1
  fetchTableData()
  ElMessage.success('数据已刷新')
}

// 导入数据
const handleImportData = (importedData: any[]) => {
  ElMessage.success(`成功导入 ${importedData.length} 条数据`)
  handleRefresh()
}

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

// 删除
// 删除函数 - 添加状态检查和更好的错误处理
const handleDelete = async (rows: TableRow[]) => {
  try {
    // 检查是否有已审核的单据
    const cannotDeleteRows = rows.filter((row) => row.examine === 2) // examine=2 表示已审核
    if (cannotDeleteRows.length > 0) {
      const cannotDeleteNumbers = cannotDeleteRows.map((row) => row.number).join('、')
      ElMessage.warning(`以下单据已审核，无法删除：${cannotDeleteNumbers}`)

      // 如果所有选中的单据都不能删除，直接返回
      if (cannotDeleteRows.length === rows.length) {
        return
      }

      // 询问用户是否继续删除未审核的单据
      try {
        await ElMessageBox.confirm(
          `有 ${cannotDeleteRows.length} 张已审核单据无法删除，是否继续删除剩余的 ${rows.length - cannotDeleteRows.length} 张未审核单据？`,
          '确认删除',
          {
            confirmButtonText: '继续删除',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
      } catch (confirmError) {
        // 用户取消删除
        return
      }

      // 只删除未审核的单据
      const canDeleteRows = rows.filter((row) => row.examine !== 2)
      const ids = canDeleteRows.map((row) => row.id!)

      if (ids.length === 0) {
        return
      }

      await batchDeleteOtherExpense(ids)
      ElMessage.success(`删除成功，共删除 ${ids.length} 张未审核单据`)
    } else {
      // 所有单据都可以删除
      await ElMessageBox.confirm(`确定要删除选中的 ${rows.length} 条数据吗？`, '确认删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })

      const ids = rows.map((row) => row.id!)
      await batchDeleteOtherExpense(ids)
      ElMessage.success('删除成功')
    }

    // 刷新数据
    fetchTableData(currentSearchParams.value)
  } catch (error: any) {
    if (error === 'cancel') {
      // 用户取消操作，不处理
      return
    }

    // 处理其他错误
    console.error('删除失败:', error)

    // 从错误响应中提取具体错误信息
    let errorMessage = '删除失败'
    if (error?.data?.data) {
      errorMessage = error.data.data
    } else if (error?.data?.message) {
      errorMessage = error.data.message
    } else if (error?.message) {
      errorMessage = error.message
    }

    ElMessage.error(errorMessage)
  }
}

// 编辑
const handleEdit = (row: TableRow) => {
  console.log('编辑:', row)
  ElMessage.info(`编辑单据: ${row.number}`)
}

// 工具函数：数字格式化
const formatNumber = (n: number | undefined | null) => {
  const num = Number(n ?? 0)
  return Number.isFinite(num) ? num.toLocaleString() : '0'
}

// 单独删除函数
const onDelete = (row: TableRow) => {
  // 单独删除时直接检查审核状态
  if (row.examine === 2) {
    ElMessage.warning(`单据 ${row.number} 已审核，无法删除`)
    return
  }

  handleDelete([row])
}

// ========== 生命周期 ==========
onMounted(() => {
  fetchTableData()
})
</script>

<style scoped lang="scss">
.other-expense-report {
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
    display: flex;
    align-items: center;
    gap: 12px;

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

    .custom-batch-btn {
      margin-left: 8px;
    }
  }

  .right-actions {
    display: flex;
    gap: 8px;
    align-items: center;
  }
}

/* 右上角选择操作按钮组 */
.selection-actions {
  display: flex;
  gap: 8px;
  align-items: center;
  margin-right: 12px;

  .operation-dropdown {
    .operation-btn {
      background: #409eff;
      border-color: #409eff;
      color: #fff;

      &:hover {
        background: #66b1ff;
        border-color: #66b1ff;
      }
    }
  }

  .batch-delete-btn {
    background: #f56c6c;
    border-color: #f56c6c;
    color: #fff;

    &:hover {
      background: #f78989;
      border-color: #f78989;
    }
  }
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

.export-tip {
  text-align: center;
  color: #f56c6c;
  margin-top: 10px;
  font-size: 14px;
}

/* 上传预览样式 */
.upload-preview {
  margin-top: 20px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #e9ecef;
}

.upload-preview h4 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 14px;
  font-weight: 600;
}

.preview-note {
  margin-top: 10px;
  font-size: 12px;
  color: #666;
  text-align: center;
}

.preview-actions {
  margin-top: 15px;
  text-align: center;
}

.preview-actions .el-button {
  margin: 0 5px;
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

    .selection-actions {
      margin-right: 0;
      justify-content: center;
    }
  }

  .custom-summary {
    flex-wrap: wrap;
    justify-content: center;

    .summary-item {
      margin: 0 4px;
    }
  }
}
</style>
