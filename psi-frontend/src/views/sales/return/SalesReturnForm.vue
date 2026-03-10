<template>
  <div class="sales-order-form">
    <!-- 顶部操作栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <!-- 左上角搜索组件 -->
        <GoodSearchForm
          :model-value="searchModel"
          @update:model-value="(val) => Object.assign(searchModel, val)"
          class="mr-8"
          @search="onGoodSearch"
          ref="goodSearchRef"
        />
      </div>
      <div class="toolbar-right">
        <div class="btn-join">
          <!-- 批量操作按钮组：当有选中项时显示 -->
          <template v-if="selected.length > 0">
            <el-popover
              class="btnGroupPopover"
              placement="bottom"
              popper-class="blockPopover"
              trigger="click"
            >
              <template #reference>
                <el-button type="info">操作</el-button>
              </template>
              <ul>
                <li @click="onBatchExamine(0)">审核</li>
                <li @click="onBatchExamine(1)">反审核</li>
                <li @click="onBatchCheck(0)">核对</li>
                <li @click="onBatchCheck(1)">反核对</li>
              </ul>
            </el-popover>
            <el-button @click="onBatchDelete" type="info">删除</el-button>
          </template>
          <OperationButtons
            :show-add="false"
            :show-batch="true"
            :show-refresh="false"
            :table-columns="columns"
            @batch="onBatch"
            @import-data="onImportData"
          />
          <el-button :loading="refreshing" type="primary" @click="onRefresh">刷新</el-button>
        </div>
      </div>
    </div>

    <!-- 表格 + 固定横向滚动条 + 状态栏 -->
    <div class="table-section">
      <!-- 表格滚动容器 -->
      <div ref="tableWrapper" class="table-wrapper">
        <el-table
          ref="tableRef"
          :data="pagedData"
          border
          style="width: 100%"
          :header-cell-style="{ background: '#fafafa' }"
          @selection-change="handleSelectionChange"
        >
          <!-- 左侧固定选择列 -->
          <el-table-column fixed="left" type="selection" width="48" align="center" />

          <!-- 所属组织 -->
          <el-table-column
            prop="org"
            label="所属组织"
            width="120"
            align="center"
            header-align="center"
          />

          <!-- 客户 -->
          <el-table-column
            prop="customer"
            label="客户"
            width="120"
            align="center"
            header-align="center"
          />

          <!-- 单据时间（可排序） -->
          <el-table-column label="单据时间" width="130" align="center" header-align="center">
            <template #header>
              <div class="header-sortable">
                <span class="col-title">单据时间</span>
                <div class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === 'billTime' && sortOrder === 'asc' }"
                    @click.stop="sortTable('billTime', 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === 'billTime' && sortOrder === 'desc' }"
                    @click.stop="sortTable('billTime', 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
            <template #default="{ row }">
              {{ row.billTime }}
            </template>
          </el-table-column>

          <!-- 单据编号（可排序） -->
          <el-table-column label="单据编号" width="200" align="center" header-align="center">
            <template #header>
              <div class="header-sortable">
                <span class="col-title">单据编号</span>
                <div class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === 'billNo' && sortOrder === 'asc' }"
                    @click.stop="sortTable('billNo', 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === 'billNo' && sortOrder === 'desc' }"
                    @click.stop="sortTable('billNo', 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
            <template #default="{ row }">
              {{ row.billNo }}
            </template>
          </el-table-column>

          <!-- 单据金额（可排序） -->
          <el-table-column label="单据金额" width="130" align="center" header-align="center">
            <template #header>
              <div class="header-sortable">
                <span class="col-title">单据金额</span>
                <div class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === 'billAmount' && sortOrder === 'asc' }"
                    @click.stop="sortTable('billAmount', 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === 'billAmount' && sortOrder === 'desc' }"
                    @click.stop="sortTable('billAmount', 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
            <template #default="{ row }">
              {{ formatNumber(row.billAmount) }}
            </template>
          </el-table-column>

          <!-- 实际金额（可排序） -->
          <el-table-column label="实际金额" width="130" align="center" header-align="center">
            <template #header>
              <div class="header-sortable">
                <span class="col-title">实际金额</span>
                <div class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === 'realAmount' && sortOrder === 'asc' }"
                    @click.stop="sortTable('realAmount', 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === 'realAmount' && sortOrder === 'desc' }"
                    @click.stop="sortTable('realAmount', 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
            <template #default="{ row }">
              {{ formatNumber(row.realAmount) }}
            </template>
          </el-table-column>

          <!-- 单据付款（可排序） -->
          <el-table-column label="单据收款" width="130" align="center" header-align="center">
            <template #header>
              <div class="header-sortable">
                <span class="col-title">单据付款</span>
                <div class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === 'receiptAmount' && sortOrder === 'asc' }"
                    @click.stop="sortTable('receiptAmount', 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === 'receiptAmount' && sortOrder === 'desc' }"
                    @click.stop="sortTable('receiptAmount', 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
            <template #default="{ row }">
              {{ formatNumber(row.receiptAmount) }}
            </template>
          </el-table-column>

          <!-- 核销金额（可排序） -->
          <el-table-column label="核销金额" width="130" align="center" header-align="center">
            <template #header>
              <div class="header-sortable">
                <span class="col-title">核销金额</span>
                <div class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === 'writeoffAmount' && sortOrder === 'asc' }"
                    @click.stop="sortTable('writeoffAmount', 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === 'writeoffAmount' && sortOrder === 'desc' }"
                    @click.stop="sortTable('writeoffAmount', 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
            <template #default="{ row }">
              {{ formatNumber(row.writeoffAmount) }}
            </template>
          </el-table-column>

          <!-- 单据费用（可排序） -->
          <el-table-column label="单据费用" width="130" align="center" header-align="center">
            <template #header>
              <div class="header-sortable">
                <span class="col-title">单据费用</span>
                <div class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === 'feeAmount' && sortOrder === 'asc' }"
                    @click.stop="sortTable('feeAmount', 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === 'feeAmount' && sortOrder === 'desc' }"
                    @click.stop="sortTable('feeAmount', 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
            <template #default="{ row }">
              {{ formatNumber(row.feeAmount) }}
            </template>
          </el-table-column>

          <!-- 关联人员 -->
          <el-table-column
            prop="relatedPerson"
            label="关联人员"
            width="130"
            align="center"
            header-align="center"
          />

          <!-- 审核状态（黑白文字） -->
          <el-table-column label="审核状态" width="100" align="center" header-align="center">
            <template #default="{ row }">
              <span>{{ row.auditStatus }}</span>
            </template>
          </el-table-column>

          <!-- 核销状态：已核销 / 部分核销 时带小表格弹出 -->
          <el-table-column label="核销状态" width="120" align="center" header-align="center">
            <template #default="{ row }">
              <div class="status-with-trigger" v-if="row.writeoffStatus">
                <span>{{ row.writeoffStatus }}</span>
                <el-popover
                  v-if="row.writeoffStatus === '已核销' || row.writeoffStatus === '部分核销'"
                  placement="bottom"
                  trigger="click"
                  width="520"
                >
                  <div class="inner-pop-table">
                    <table>
                      <thead>
                        <tr>
                          <th>单据类型</th>
                          <th>单据时间</th>
                          <th>单据编号</th>
                          <th>核销金额</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(d, i) in row.writeoffDetails" :key="i">
                          <td>{{ d.billType }}</td>
                          <td>{{ d.billTime }}</td>
                          <td>{{ d.billNo }}</td>
                          <td class="num">{{ formatNumber(d.amount) }}</td>
                        </tr>
                        <tr v-if="!row.writeoffDetails || row.writeoffDetails.length === 0">
                          <td colspan="4" class="empty">暂无数据</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <template #reference>
                    <el-button link class="mini-trigger">
                      <el-icon><ArrowDown /></el-icon>
                    </el-button>
                  </template>
                </el-popover>
              </div>
            </template>
          </el-table-column>

          <!-- 费用状态：未结算 时可展开 -->
          <el-table-column label="费用状态" width="120" align="center" header-align="center">
            <template #default="{ row }">
              <div class="status-with-trigger" v-if="row.feeStatus">
                <span>{{ row.feeStatus }}</span>
                <el-popover
                  v-if="row.feeStatus === '未结算'"
                  placement="bottom"
                  trigger="click"
                  width="520"
                >
                  <div class="inner-pop-table">
                    <table>
                      <thead>
                        <tr>
                          <th>支出类别</th>
                          <th>金额</th>
                          <th>结算金额</th>
                          <th>结算状态</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(d, i) in row.feeDetails" :key="i">
                          <td>{{ d.expenseType }}</td>
                          <td class="num">{{ formatNumber(d.amount) }}</td>
                          <td class="num">{{ formatNumber(d.settleAmount) }}</td>
                          <td>{{ d.settleStatus }}</td>
                        </tr>
                        <tr v-if="!row.feeDetails || row.feeDetails.length === 0">
                          <td colspan="4" class="empty">暂无数据</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <template #reference>
                    <el-button link class="mini-trigger">
                      <el-icon><ArrowDown /></el-icon>
                    </el-button>
                  </template>
                </el-popover>
              </div>
            </template>
          </el-table-column>

          <!-- 发票状态：已开具 时可展开 -->
          <el-table-column label="发票状态" width="120" align="center" header-align="center">
            <template #default="{ row }">
              <div class="status-with-trigger" v-if="row.invoiceStatus">
                <span>{{ row.invoiceStatus }}</span>
                <el-popover
                  v-if="row.invoiceStatus === '已开具'"
                  placement="bottom"
                  trigger="click"
                  width="520"
                >
                  <div class="inner-pop-table">
                    <table>
                      <thead>
                        <tr>
                          <th>开票日期</th>
                          <th>发票号码</th>
                          <th>发票抬头</th>
                          <th>发票金额</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(d, i) in row.invoiceDetails" :key="i">
                          <td>{{ d.invoiceDate }}</td>
                          <td>{{ d.invoiceNo }}</td>
                          <td>{{ d.invoiceTitle }}</td>
                          <td class="num">{{ formatNumber(d.invoiceAmount) }}</td>
                        </tr>
                        <tr v-if="!row.invoiceDetails || row.invoiceDetails.length === 0">
                          <td colspan="4" class="empty">暂无数据</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <template #reference>
                    <el-button link class="mini-trigger">
                      <el-icon><ArrowDown /></el-icon>
                    </el-button>
                  </template>
                </el-popover>
              </div>
            </template>
          </el-table-column>

          <!-- 核对状态（普通文字） -->
          <el-table-column
            prop="checkStatus"
            label="核对状态"
            width="100"
            align="center"
            header-align="center"
          />

          <!-- 制单人 -->
          <el-table-column
            prop="maker"
            label="制单人"
            width="100"
            align="center"
            header-align="center"
          />

          <!-- 备注信息 -->
          <el-table-column
            prop="remark"
            label="备注信息"
            width="160"
            align="center"
            header-align="center"
            :show-overflow-tooltip="true"
          />

          <!-- 操作列 -->
          <el-table-column fixed="right" label="相关操作" width="170" align="center">
            <template #default="scope">
              <div class="op-row">
                <el-button size="small" class="op-btn op-gray" @click="openDetail(scope.row)">
                  详情
                </el-button>
                <el-button size="small" class="op-btn op-gray" @click="onDelete(scope.row)">
                  删除
                </el-button>
                <el-dropdown trigger="click">
                  <el-button size="small" class="op-btn op-gray op-more">
                    <el-icon><ArrowDown /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu class="info-dropdown">
                      <el-dropdown-item>单据号：{{ scope.row.billNo }}</el-dropdown-item>
                      <el-dropdown-item>客户：{{ scope.row.customer }}</el-dropdown-item>
                      <el-dropdown-item
                        >金额：{{ formatNumber(scope.row.billAmount) }}</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 中：我们的自定义横向拖动条 -->
      <div ref="xScroll" class="x-scroll">
        <div ref="xInner" class="x-scroll-inner"></div>
      </div>

      <!-- 底部状态栏 -->
      <div class="status-bar">
        <div class="status-left">
          <el-pagination
            background
            layout="prev, pager, next, sizes, jumper, total"
            :total="tableData.length"
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 30, 50, 100]"
          />
        </div>
        <div class="status-right">
          <span>总单据金额：{{ totalBillAmount.toLocaleString() }}</span>
          <span class="ml-24">总实际金额：{{ totalRealAmount.toLocaleString() }}</span>
          <span class="ml-24">总单据收款：{{ totalReceipt.toLocaleString() }}</span>
          <span class="ml-24">总核销金额：{{ totalWriteoff.toLocaleString() }}</span>
          <span class="ml-24">总单据费用：{{ totalFee.toLocaleString() }}</span>
        </div>
      </div>
    </div>

    <!-- 销售单详情弹窗 -->
    <SalesReturnDetailDialog
      v-model:visible="detailVisible"
      :record="currentRow"
      @save="handleDetailSave"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, onBeforeUnmount, nextTick, type Ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import SalesReturnDetailDialog from './SalesReturnDetailDialog.vue'

import GoodSearchForm from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import OperationButtons from '@/components/operationbuttons/OperationButtons.vue'

/** 表格行结构 */
interface WriteoffDetail {
  billType: string
  billTime: string
  billNo: string
  amount: number
}

interface FeeDetail {
  expenseType: string
  amount: number
  settleAmount: number
  settleStatus: string
}

interface InvoiceDetail {
  invoiceDate: string
  invoiceNo: string
  invoiceTitle: string
  invoiceAmount: number
}

interface RowData {
  org: string
  customer: string
  billTime: string
  billNo: string
  billAmount: number
  realAmount: number
  receiptAmount: number
  writeoffAmount: number
  feeAmount: number
  relatedPerson: string
  auditStatus: string
  writeoffStatus: string
  feeStatus: string
  invoiceStatus: string
  checkStatus: string
  maker: string
  remark: string
  writeoffDetails?: WriteoffDetail[]
  feeDetails?: FeeDetail[]
  invoiceDetails?: InvoiceDetail[]
}

/* 示例数据：你可以等接接口以后直接替换 */
const tableData = ref<RowData[]>([
  {
    org: '默认组织',
    customer: '楚渝帅',
    billTime: '2025-10-30',
    billNo: 'XSD2510302238442',
    billAmount: 720781,
    realAmount: 702335.5,
    receiptAmount: 675731.5,
    writeoffAmount: 675430.5,
    feeAmount: 0,
    relatedPerson: '崔渝帅',
    auditStatus: '已审核',
    writeoffStatus: '已核销',
    feeStatus: '无需结算',
    invoiceStatus: '未开具',
    checkStatus: '已核对',
    maker: '管理员',
    remark: '',
    writeoffDetails: [
      {
        billType: '采购退货单',
        billTime: '2025-10-30',
        billNo: 'CGTHD2510302238442',
        amount: 31
      }
    ]
  },
  {
    org: '默认组织',
    customer: 'jack',
    billTime: '2025-10-30',
    billNo: 'XSD251030111001',
    billAmount: 98,
    realAmount: 97,
    receiptAmount: 97,
    writeoffAmount: 0,
    feeAmount: 0,
    relatedPerson: 'jack',
    auditStatus: '已审核',
    writeoffStatus: '部分核销',
    feeStatus: '无需结算',
    invoiceStatus: '未开具',
    checkStatus: '未核对',
    maker: '管理员',
    remark: '',
    writeoffDetails: [
      { billType: '销售收款单', billTime: '2025-10-29', billNo: 'SKD2510290001', amount: 50 },
      { billType: '销售收款单', billTime: '2025-10-30', billNo: 'SKD2510300006', amount: 30 }
    ]
  },
  {
    org: '默认组织',
    customer: 'ruih',
    billTime: '2025-10-29',
    billNo: 'XSD251029888000',
    billAmount: 3000,
    realAmount: 3000,
    receiptAmount: 2500,
    writeoffAmount: 0,
    feeAmount: 200,
    relatedPerson: 'ruih',
    auditStatus: '未审核',
    writeoffStatus: '未核销',
    feeStatus: '未结算',
    invoiceStatus: '已开具',
    checkStatus: '未核对',
    maker: '管理员',
    remark: '演示',
    feeDetails: [{ expenseType: '物流费', amount: 200, settleAmount: 0, settleStatus: '未结算' }],
    invoiceDetails: [
      {
        invoiceDate: '2025-10-29',
        invoiceNo: 'FP20251029001',
        invoiceTitle: '湖北点可云科技有限公司',
        invoiceAmount: 3000
      }
    ]
  }
])
// 给 OperationButtons 用的列描述（不是表格必须按这个渲染）
const columns = [
  { prop: 'org', label: '所属组织', width: 120 },
  { prop: 'customer', label: '客户', width: 120 },
  { prop: 'billTime', label: '单据时间', width: 130 },
  { prop: 'billNo', label: '单据编号', width: 200 },
  { prop: 'billAmount', label: '单据金额', width: 130 },
  { prop: 'realAmount', label: '实际金额', width: 130 },
  { prop: 'receiptAmount', label: '单据收款', width: 130 },
  { prop: 'writeoffAmount', label: '核销金额', width: 130 },
  { prop: 'feeAmount', label: '单据费用', width: 130 },
  { prop: 'relatedPerson', label: '关联人员', width: 120 },
  { prop: 'auditStatus', label: '审核状态', width: 100 },
  { prop: 'writeoffStatus', label: '核销状态', width: 120 },
  { prop: 'feeStatus', label: '费用状态', width: 120 },
  { prop: 'invoiceStatus', label: '发票状态', width: 120 },
  { prop: 'checkStatus', label: '核对状态', width: 100 },
  { prop: 'maker', label: '制单人', width: 100 },
  { prop: 'remark', label: '备注信息', width: 160 }
]

/* 搜索部分（你已经把类型中 customer 改成 string | null 了，就保持一致） */
type SearchFormData = {
  customer?: string | null
  number?: string
  startTime?: string
  endTime?: string
  [k: string]: any
}
const searchModel = reactive<SearchFormData>({})
const goodSearchRef = ref<InstanceType<typeof GoodSearchForm> | null>(null)

const originalAllData = ref<RowData[]>([])

/* 选择 */
const selected = ref<RowData[]>([])
const handleSelectionChange = (rows: RowData[]) => {
  selected.value = rows
}

/* 排序 */
const sortKey = ref<string>('')
const sortOrder = ref<'asc' | 'desc' | ''>('')
const sortTable = (key: string, order: 'asc' | 'desc') => {
  sortKey.value = key
  sortOrder.value = order
  tableData.value.sort((a, b) => {
    const va = (a as any)[key]
    const vb = (b as any)[key]
    if (order === 'asc') return va > vb ? 1 : va < vb ? -1 : 0
    return va < vb ? 1 : va > vb ? -1 : 0
  })
}

/* 合计 / 分页 */
const totalBillAmount = computed(() => tableData.value.reduce((s, r) => s + (r.billAmount || 0), 0))
const totalRealAmount = computed(() => tableData.value.reduce((s, r) => s + (r.realAmount || 0), 0))
const totalReceipt = computed(() => tableData.value.reduce((s, r) => s + (r.receiptAmount || 0), 0))
const totalWriteoff = computed(() =>
  tableData.value.reduce((s, r) => s + (r.writeoffAmount || 0), 0)
)
const totalFee = computed(() => tableData.value.reduce((s, r) => s + (r.feeAmount || 0), 0))

const currentPage = ref(1)
const pageSize = ref(10)
const pagedData = computed(() => {
  const s = (currentPage.value - 1) * pageSize.value
  return tableData.value.slice(s, s + pageSize.value)
})

/* 搜索过滤（这里还是本地的） */
const includes = (src?: string, kw?: string | null) =>
  (src ?? '').toLowerCase().includes((kw ?? '').trim().toLowerCase())

const onGoodSearch = (payload: SearchFormData) => {
  const cond = payload ?? {}
  const list = originalAllData.value.filter((row) => {
    if (cond.customer && !includes(row.customer, cond.customer)) return false
    if (cond.number && !includes(row.billNo, cond.number)) return false
    // 时间区间你可以按需要再补
    return true
  })
  tableData.value = list
  currentPage.value = 1
}

/* 刷新 */
const refreshing = ref(false)
const onRefresh = () => {
  if (refreshing.value) return
  refreshing.value = true
  setTimeout(() => {
    tableData.value = JSON.parse(JSON.stringify(originalAllData.value))
    currentPage.value = 1
    goodSearchRef.value?.resetForm?.()
    refreshing.value = false
    sortKey.value = ''
    sortOrder.value = ''
    ElMessage.success('已刷新')
  }, 600)
}

/* 批量 */
const onBatch = () => {
  ElMessage.info('打开批量操作窗口（示例）')
}

/* 导入（保持原逻辑） */
const onImportData = (rows: any[]) => {
  const mapped: RowData[] = rows.map((r, i) => ({
    org: r.org ?? '默认组织',
    customer: r.customer ?? r.客户 ?? '导入客户' + (i + 1),
    billTime: r.billTime ?? r.单据时间 ?? new Date().toISOString().slice(0, 10),
    billNo: r.billNo ?? r.单据编号 ?? 'XSD' + Date.now() + i,
    billAmount: Number(r.billAmount ?? r.单据金额 ?? 0),
    realAmount: Number(r.realAmount ?? r.实际金额 ?? r.billAmount ?? 0),
    receiptAmount: Number(r.receiptAmount ?? r.单据收款 ?? 0),
    writeoffAmount: Number(r.writeoffAmount ?? r.核销金额 ?? 0),
    feeAmount: Number(r.feeAmount ?? r.单据费用 ?? 0),
    relatedPerson: r.relatedPerson ?? r.关联人员 ?? '',
    auditStatus: r.auditStatus ?? r.审核状态 ?? '未审核',
    writeoffStatus: r.writeoffStatus ?? r.核销状态 ?? '未核销',
    feeStatus: r.feeStatus ?? r.费用状态 ?? '无需结算',
    invoiceStatus: r.invoiceStatus ?? r.发票状态 ?? '未开具',
    checkStatus: r.checkStatus ?? r.核对状态 ?? '未核对',
    maker: r.maker ?? r.制单人 ?? '导入',
    remark: r.remark ?? r.备注信息 ?? '',
    writeoffDetails: [],
    feeDetails: [],
    invoiceDetails: []
  }))
  tableData.value = [...tableData.value, ...mapped]
  originalAllData.value = JSON.parse(JSON.stringify(tableData.value))
  ElMessage.success(`已导入 ${mapped.length} 条记录`)
}

/* 详情 */
const detailVisible = ref(false)
const currentRow = ref<RowData | null>(null)
const openDetail = (row: RowData) => {
  currentRow.value = row
  detailVisible.value = true
}
const handleDetailSave = () => {
  ElMessage.success('详情已保存（示例）')
  detailVisible.value = false
}

/* 删除 */
const onDelete = (row: RowData) => {
  ElMessageBox.confirm(`确定删除单据【${row.billNo}】吗？`, '提示', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消'
  })
    .then(() => {
      const i = tableData.value.findIndex((r) => r.billNo === row.billNo)
      if (i > -1) tableData.value.splice(i, 1)
      originalAllData.value = JSON.parse(JSON.stringify(tableData.value))
      ElMessage.success('删除成功')
    })
    .catch(() => {})
}

/* 批量审核/反审核 */
const onBatchExamine = (type: 0 | 1) => {
  // type: 0=审核, 1=反审核
  const validRows: RowData[] = []
  
  selected.value.forEach((row) => {
    const currentStatus = row.auditStatus
    // 审核：只处理未审核的
    if (type === 0 && currentStatus === '未审核') {
      validRows.push(row)
    }
    // 反审核：只处理已审核的
    if (type === 1 && currentStatus === '已审核') {
      validRows.push(row)
    }
  })

  if (validRows.length === 0) {
    ElMessage.warning('无可操作单据！')
    return
  }

  // 执行批量操作
  validRows.forEach((row) => {
    row.auditStatus = type === 0 ? '已审核' : '未审核'
  })

  // 更新原始集合
  originalAllData.value = JSON.parse(JSON.stringify(tableData.value))
  
  // 清空选中项
  selected.value = []
  if (tableRef.value) {
    tableRef.value.clearSelection()
  }

  ElMessage.success(`操作成功！共处理 ${validRows.length} 条单据`)
}

/* 批量核对/反核对 */
const onBatchCheck = (type: 0 | 1) => {
  // type: 0=核对, 1=反核对
  const validRows: RowData[] = []
  
  selected.value.forEach((row) => {
    const currentStatus = row.checkStatus
    // 核对：只处理未核对的
    if (type === 0 && currentStatus === '未核对') {
      validRows.push(row)
    }
    // 反核对：只处理已核对的
    if (type === 1 && currentStatus === '已核对') {
      validRows.push(row)
    }
  })

  if (validRows.length === 0) {
    ElMessage.warning('无可操作单据！')
    return
  }

  // 执行批量操作
  validRows.forEach((row) => {
    row.checkStatus = type === 0 ? '已核对' : '未核对'
  })

  // 更新原始集合
  originalAllData.value = JSON.parse(JSON.stringify(tableData.value))
  
  // 清空选中项
  selected.value = []
  if (tableRef.value) {
    tableRef.value.clearSelection()
  }

  ElMessage.success(`操作成功！共处理 ${validRows.length} 条单据`)
}

/* 批量删除 */
const onBatchDelete = () => {
  if (selected.value.length === 0) {
    ElMessage.warning('未选择要删除的数据！')
    return
  }

  ElMessageBox.confirm(
    `确定删除选中的 ${selected.value.length} 条单据吗？`,
    '提示',
    {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消'
    }
  )
    .then(() => {
      // 获取要删除的单据编号
      const billNos = selected.value.map((r) => r.billNo)
      
      // 从数据中删除
      tableData.value = tableData.value.filter((r) => !billNos.includes(r.billNo))
      
      // 更新原始集合
      originalAllData.value = JSON.parse(JSON.stringify(tableData.value))
      
      // 清空选中项
      selected.value = []
      if (tableRef.value) {
        tableRef.value.clearSelection()
      }

      ElMessage.success(`删除成功！共删除 ${billNos.length} 条单据`)
    })
    .catch(() => {})
}

/* 固定横向滚动条联动 */
const tableRef = ref()
const bodyWrapper = ref<HTMLElement | null>(null)
const headerWrapper = ref<HTMLElement | null>(null)
const xScroll = ref<HTMLElement | null>(null)
const xInner = ref<HTMLElement | null>(null)
let ro: ResizeObserver | null = null

const setWidths = () => {
  if (!bodyWrapper.value || !xInner.value) return
  xInner.value.style.width = bodyWrapper.value.scrollWidth + 'px'
}
const bodyScroll = () => {
  if (!xScroll.value || !bodyWrapper.value) return
  if (xScroll.value.scrollLeft !== bodyWrapper.value.scrollLeft) {
    xScroll.value.scrollLeft = bodyWrapper.value.scrollLeft
  }
  if (headerWrapper.value) headerWrapper.value.scrollLeft = bodyWrapper.value.scrollLeft
}
const xScrollScroll = () => {
  if (!xScroll.value || !bodyWrapper.value) return
  if (bodyWrapper.value.scrollLeft !== xScroll.value.scrollLeft) {
    bodyWrapper.value.scrollLeft = xScroll.value.scrollLeft
    if (headerWrapper.value) headerWrapper.value.scrollLeft = xScroll.value.scrollLeft
  }
}

onMounted(async () => {
  // 初始化原始数据
  originalAllData.value = JSON.parse(JSON.stringify(tableData.value))

  await nextTick()
  const tableEl = (tableRef.value?.$el ?? null) as HTMLElement | null
  bodyWrapper.value = tableEl?.querySelector('.el-table__body-wrapper') as HTMLElement | null
  headerWrapper.value = tableEl?.querySelector('.el-table__header-wrapper') as HTMLElement | null

  if (bodyWrapper.value) {
    bodyWrapper.value.addEventListener('scroll', bodyScroll, { passive: true })
  }
  if (xScroll.value) {
    xScroll.value.addEventListener('scroll', xScrollScroll, { passive: true })
  }
  setWidths()
  if (bodyWrapper.value) {
    ro = new ResizeObserver(() => setWidths())
    ro.observe(bodyWrapper.value)
  }
})

onBeforeUnmount(() => {
  if (bodyWrapper.value) bodyWrapper.value.removeEventListener('scroll', bodyScroll)
  if (xScroll.value) xScroll.value.removeEventListener('scroll', xScrollScroll)
  if (ro && bodyWrapper.value) ro.unobserve(bodyWrapper.value)
  ro = null
})

/* 工具函数：数字加千分位 */
const formatNumber = (n: number | undefined | null) => {
  const num = Number(n ?? 0)
  return Number.isFinite(num) ? num.toLocaleString() : '0'
}
</script>

<style scoped>
.sales-order-form {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: #fff;
}
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
}
.table-section {
  flex: 1 1 auto;
  min-height: 0;
  display: flex;
  flex-direction: column;
  position: relative;
}
.table-wrapper {
  flex: 1 1 auto;
  min-height: 0;
  overflow-y: auto;
  overflow-x: hidden;
  padding-bottom: 14px;
  border-bottom: 1px solid #ebeef5;
}
.table-wrapper :deep(.el-table__body-wrapper) {
  overflow-x: hidden !important;
}
.table-wrapper :deep(.el-table__header-wrapper) {
  overflow: hidden !important;
}

.bottom-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #f7f8fa;
  border-top: 1px solid #ebeef5;
  padding: 4px 10px;
  height: 40px;
  /* 贴底 */
  position: sticky;
  bottom: 0;
  z-index: 5;
}

/* 左边分页不挤 */
.status-left {
  flex: 0 0 auto;
}

/* 中间的横向拖动条占剩余空间 */
.x-scroll {
  flex: 1 1 auto;
  height: 12px;
  overflow-x: auto;
  overflow-y: hidden;
  background: #f0f2f5;
  border-radius: 6px;
  /* 让滚动条更好点 */
}
.x-scroll-inner {
  height: 1px;
}
.scrollbar::-webkit-scrollbar,
.table-wrapper::-webkit-scrollbar {
  height: 12px;
  width: 12px;
}
.scrollbar::-webkit-scrollbar-thumb,
.table-wrapper::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 6px;
  border: 2px solid #f0f2f5;
}

/* 状态栏 */
.status-bar {
  position: sticky;
  bottom: 0;
  z-index: 5;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #f7f8fa;
  border-top: 1px solid #ebeef5;
  height: 28px;
  padding: 4px 10px;
  font-size: 13px;
  color: #606266;
}
/* 右边合计不换行 */
.status-right {
  flex: 0 0 auto;
  white-space: nowrap;
  font-size: 13px;
  color: #606266;
}
.ml-24 {
  margin-left: 24px;
}

/* 顶部按钮联动 */
.btn-join {
  display: inline-flex;
  align-items: center;
}
.btn-join > * + * {
  margin-left: -1px;
}

/* 表头排序样式 */
.header-sortable {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
.col-title {
  white-space: nowrap;
}
.sort-vertical {
  display: inline-flex;
  flex-direction: column;
  margin-left: 4px;
  line-height: 10px;
}
.sort-vertical .el-icon {
  font-size: 14px;
  color: #c0c4cc;
  cursor: pointer;
}
.sort-vertical .el-icon.active {
  color: #409eff;
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

/* 状态 + 小按钮 */
.status-with-trigger {
  display: inline-flex;
  align-items: center;
  gap: 2px;
}
.mini-trigger {
  padding: 0;
  min-height: 20px;
}

/* 内部弹窗表格 */
.inner-pop-table {
  width: 100%;
}
.inner-pop-table table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
}
.inner-pop-table th,
.inner-pop-table td {
  border: 1px solid #ebeef5;
  padding: 4px 6px;
  text-align: center;
  white-space: nowrap;
}
.inner-pop-table th {
  background: #f7f8fa;
  font-weight: 500;
}
.inner-pop-table .num {
  text-align: right;
}
.inner-pop-table .empty {
  text-align: center;
  color: #999;
}

.mr-8 {
  margin-right: 8px;
}
</style>
