<template>
  <div class="other-outbound-form">
    <!-- 顶部操作栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <!-- 搜索组件 -->
        <GoodSearchForm
          v-model="searchModel"
          class="mr-8"
          @search="onGoodSearch"
          ref="goodSearchRef"
          :config="{
            // 隐藏所有默认字段
            showGoods: false,
            showNumber: false,
            showSupplier: false,
            showCustomer: false,
            showWarehouse: false,
            showAccount: false,
            showPeople: false,
            showBillDate: false,
            showArrivalDate: false,
            showUser: false,
            showExamine: false,
            showState: false,
            showRemark: false,
            // 自定义所有字段，按要求顺序排列
            customFields: [
              {
                key: 'productName',
                type: 'input',
                label: '请输入商品名称',
                placeholder: '请输入商品名称'
              },
              {
                key: 'billNo',
                type: 'input',
                label: '请输入单据编号',
                placeholder: '请输入单据编号'
              },
              {
                key: 'customer',
                type: 'input',
                label: '请选择客户',
                placeholder: '请选择客户'
              },
              {
                key: 'creator',
                type: 'input',
                label: '请选择制单人',
                placeholder: '请选择制单人'
              },
              {
                key: 'startDate',
                type: 'date',
                label: '单据开始日期',
                placeholder: '单据开始日期'
              },
              {
                key: 'endDate',
                type: 'date',
                label: '单据结束日期',
                placeholder: '单据结束日期'
              },
              {
                key: 'contactPerson',
                type: 'input',
                label: '请选择关联人员',
                placeholder: '请选择关联人员'
              },
              {
                key: 'checkStatus',
                type: 'select',
                label: '请选择审核状态',
                placeholder: '请选择审核状态',
                clearable: true,
                options: [
                  { label: '未审核', value: '未审核' },
                  { label: '已审核', value: '已审核' }
                ]
              },
              {
                key: 'expenseStatus',
                type: 'select',
                label: '请选择费用状态',
                placeholder: '请选择费用状态',
                clearable: true,
                options: [
                  { label: '无需结算', value: '无需结算' },
                  { label: '待结算', value: '待结算' },
                  { label: '已结算', value: '已结算' }
                ]
              },
              {
                key: 'auditStatus',
                type: 'select',
                label: '请选择核对状态',
                placeholder: '请选择核对状态',
                clearable: true,
                options: [
                  { label: '未核对', value: '未核对' },
                  { label: '已核对', value: '已核对' }
                ]
              },
              {
                key: 'billType',
                type: 'select',
                label: '请选择单据类型',
                placeholder: '请选择单据类型',
                clearable: true,
                options: [{ label: '其他出库单', value: '其他出库单' }]
              },
              {
                key: 'remark',
                type: 'input',
                label: '请输入备注信息',
                placeholder: '请输入备注信息'
              }
            ]
          }"
        />
      </div>
      <div class="toolbar-right">
        <div class="btn-join">
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
          <el-table-column fixed="left" type="selection" width="48" align="center" />

          <!-- 动态列 -->
          <el-table-column
            v-for="col in columns"
            :key="col.prop"
            :prop="col.prop"
            :label="col.label"
            :width="col.width"
            align="center"
            header-align="center"
            :show-overflow-tooltip="true"
          >
            <template #header="{ column }">
              <div class="header-sortable">
                <span class="col-title">{{ column.label }}</span>
                <div v-if="col.sortable" class="sort-vertical">
                  <el-icon
                    :class="{ active: sortKey === col.prop && sortOrder === 'asc' }"
                    title="升序"
                    @click.stop="sortTable(col.prop, 'asc')"
                    ><ArrowUp
                  /></el-icon>
                  <el-icon
                    :class="{ active: sortKey === col.prop && sortOrder === 'desc' }"
                    title="降序"
                    @click.stop="sortTable(col.prop, 'desc')"
                    ><ArrowDown
                  /></el-icon>
                </div>
              </div>
            </template>
          </el-table-column>

          <!-- 操作列：灰描边、黑白配、无缝拼接、更多仅图标 -->
          <el-table-column fixed="right" label="相关操作" width="170" align="center">
            <template #default="scope">
              <div class="op-row">
                <el-button size="small" class="op-btn op-gray" @click="openDetail(scope.row)"
                  >详情</el-button
                >
                <el-button size="small" class="op-btn op-gray" @click="onDelete(scope.row)"
                  >删除</el-button
                >
                <el-dropdown trigger="click">
                  <el-button size="small" class="op-btn op-gray op-more">
                    <el-icon><ArrowDown /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click="handlePrint(scope.row)">打印</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 固定横向拖动条（紧贴状态栏上方；与表格联动） -->
      <div ref="xScroll" class="x-scroll scrollbar">
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
          <span>总单据成本：{{ totalCost.toLocaleString() }}</span>
          <span class="ml-24">总单据费用：{{ totalExpense.toLocaleString() }}</span>
        </div>
      </div>
    </div>

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="单据详情"
      fullscreen
      :before-close="handleDialogClose"
    >
      <div class="detail-content">
        <!-- 商品表格区域 -->
        <div class="table-section">
          <!-- 表单和按钮行 -->
          <div class="header-row">
            <!-- 左侧表单字段 -->
            <div class="header-form">
              <!-- 客户（修改后：带搜索+分页的下拉选择器） -->
              <div class="form-item">
                <label class="form-label">客户</label>
                <!-- 下拉选择器容器 -->
                <div class="customer-select" @click.stop>
                  <!-- 输入框（显示选中值，点击打开下拉） -->
                  <el-input
                    v-model="selectedCustomerName"
                    placeholder="请选择客户"
                    readonly
                    clearable
                    @clear="handleCustomerClear"
                    @click="customerDropdownVisible = !customerDropdownVisible"
                    class="customer-input"
                    :disabled="detailData.checkStatus === '已审核'"
                  />
                  <!-- 下拉面板（搜索+列表+分页） -->
                  <div v-if="customerDropdownVisible" class="customer-dropdown" @click.stop>
                    <!-- 搜索框 -->
                    <el-input
                      v-model="customerSearchKey"
                      placeholder="输入客户名称搜索"
                      @keyup.enter="fetchCustomers"
                      prefix-icon="Search"
                      class="customer-search"
                    />
                    <!-- 客户列表 -->
                    <div class="customer-list">
                      <div
                        v-for="customer in customerList"
                        :key="customer.id"
                        class="customer-item"
                        @click="selectCustomer(customer)"
                      >
                        {{ customer.name }}
                      </div>
                      <div v-if="customerList.length === 0" class="empty-tip">未查询到客户数据</div>
                    </div>
                    <!-- 分页控件 -->
                    <el-pagination
                      @current-change="handleCustomerPageChange"
                      @size-change="handleCustomerSizeChange"
                      :current-page="customerPage"
                      :page-size="customerPageSize"
                      :total="customerTotal"
                      layout="prev, pager, next"
                      class="customer-pagination"
                    />
                  </div>
                </div>
              </div>

              <!-- 单据日期 -->
              <div class="form-item">
                <label class="form-label">单据日期</label>
                <el-date-picker
                  v-model="detailData.billDate"
                  type="date"
                  placeholder="选择日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  class="date-input"
                  :disabled="detailData.checkStatus === '已审核'"
                />
              </div>

              <!-- 单据编号 -->
              <div class="form-item">
                <label class="form-label">单据编号</label>
                <el-input
                  v-model="detailData.billNo"
                  placeholder="请输入单据编号"
                  clearable
                  class="number-input"
                  :disabled="true"
                />
              </div>
            </div>

            <!-- 右侧操作按钮组 -->
            <div class="action-buttons-container">
              <!-- 未审核状态：显示保存和刷新 -->
              <ButtonGroup v-if="detailData.checkStatus !== '已审核'" align="right" gap="0px">
                <BusinessButton action-type="save" @action="handleSaveDetail" />
                <BusinessButton action-type="refresh" @action="handleDetailRefresh" />
              </ButtonGroup>

              <!-- 已审核状态：显示生成、反审核和刷新 -->
              <ButtonGroup v-else align="right" gap="0px">
                <BusinessButton action-type="generate" @action="handleGenerate" />
                <BusinessButton action-type="unaudit" @action="handleUnaudit" />
                <BusinessButton action-type="refresh" @action="handleDetailRefresh" />
              </ButtonGroup>
            </div>
          </div>

          <!-- 表格（固定高度，内部滚动，不挤占下面状态栏） -->
          <div class="table-wrapper">
            <el-table :data="detailTableData" border highlight-current-row style="width: 100%">
              <!-- 第一列：设置按钮 + 序号 -->
              <el-table-column label="设置" width="70" align="center">
                <template #header>
                  <el-button type="text" size="small" @click="showColumnSetting = true">
                    <el-icon><Setting /></el-icon>
                  </el-button>
                </template>
                <template #default="scope">
                  {{ scope.$index + 1 }}
                </template>
              </el-table-column>

              <!-- 第二列：操作 -->
              <el-table-column label="操作" width="100" align="center">
                <template #default="scope">
                  <el-button
                    v-if="!scope.row.productName"
                    type="text"
                    size="small"
                    @click="openProductPopup(scope.$index)"
                    :disabled="detailData.checkStatus === '已审核'"
                  >
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button
                    v-else
                    type="text"
                    size="small"
                    @click="deleteRow(scope.$index)"
                    :disabled="detailData.checkStatus === '已审核'"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </template>
              </el-table-column>

              <!-- 商品名称（表头带常规/扫码开关） -->
              <el-table-column v-if="visibleColumns.productName" label="商品名称" align="center">
                <template #header>
                  <div class="flex-center">
                    <span>商品名称</span>
                    <el-switch
                      v-model="scanMode"
                      size="small"
                      inline-prompt
                      active-text="扫码"
                      inactive-text="常规"
                      style="margin-left: 6px"
                      :disabled="detailData.checkStatus === '已审核'"
                    />
                  </div>
                </template>
                <template #default="{ row }">{{ row.productName }}</template>
              </el-table-column>

              <el-table-column
                v-if="visibleColumns.productCode"
                prop="productCode"
                label="商品编号"
                align="center"
              />
              <el-table-column
                v-if="visibleColumns.spec"
                prop="spec"
                label="规格型号"
                align="center"
              />
              <el-table-column v-if="visibleColumns.unit" prop="unit" label="单位" align="center" />
              <el-table-column
                v-if="visibleColumns.attr"
                prop="attr"
                label="辅助属性"
                align="center"
              />

              <!-- 新仓库列（支持表头批量选择+单元格下拉） -->
              <el-table-column v-if="visibleColumns.warehouse" label="仓库" align="center">
                <template #header>
                  <div class="flex-center">
                    <!-- 表头批量选择弹窗 -->
                    <el-popover
                      trigger="click"
                      placement="bottom-start"
                      v-model:visible="warehousePopVisible"
                      :disabled="detailData.checkStatus === '已审核'"
                    >
                      <template #reference>
                        <div class="depot-header-trigger">
                          <span>仓库</span>
                          <el-icon class="depot-icon clickable-icon"><ArrowDown /></el-icon>
                        </div>
                      </template>
                      <div class="depot-options-no-search">
                        <div
                          v-for="warehouse in warehouseOptions"
                          :key="warehouse.value"
                          class="depot-option-item"
                          @click="handleBatchWarehouseSelect(warehouse.value)"
                        >
                          {{ warehouse.label }}
                        </div>
                      </div>
                    </el-popover>
                  </div>
                </template>

                <!-- 单元格下拉选择器 -->
                <template #default="scope">
                  <el-select
                    v-model="scope.row.warehouse"
                    placeholder="选择"
                    clearable
                    filterable
                    size="small"
                    class="cell-depot-select"
                    :disabled="detailData.checkStatus === '已审核'"
                  >
                    <el-option
                      v-for="warehouse in warehouseOptions"
                      :key="warehouse.value"
                      :label="warehouse.label"
                      :value="warehouse.value"
                    />
                  </el-select>
                </template>
              </el-table-column>

              <!-- 成本 -->
              <el-table-column label="成本" align="center">
                <template #default="{ row }">
                  <div
                    class="editable-cell"
                    @input="onEditableInput(row, 'cost', $event)"
                    @blur="onEditableBlur(row, 'cost', $event)"
                    :contenteditable="detailData.checkStatus !== '已审核'"
                  >
                    {{ row.cost || '' }}
                  </div>
                </template>
              </el-table-column>

              <!-- 数量 -->
              <el-table-column label="数量" align="center">
                <template #default="{ row }">
                  <div
                    class="editable-cell"
                    @input="onEditableInput(row, 'quantity', $event)"
                    @blur="onEditableBlur(row, 'quantity', $event)"
                    :contenteditable="detailData.checkStatus !== '已审核'"
                  >
                    {{ row.quantity || '' }}
                  </div>
                </template>
              </el-table-column>

              <el-table-column
                v-if="visibleColumns.totalCost"
                prop="totalCost"
                label="总成本"
                align="center"
                width="100"
              />

              <!-- 备注信息 -->
              <el-table-column label="备注信息" align="center">
                <template #default="{ row }">
                  <div
                    class="editable-cell"
                    @input="onEditableInput(row, 'remark', $event)"
                    @blur="onEditableBlur(row, 'remark', $event)"
                    :contenteditable="detailData.checkStatus !== '已审核'"
                  >
                    {{ row.remark || '' }}
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 列显隐弹窗（⚙按钮触发） -->
          <el-dialog v-model="showColumnSetting" title="字段配置" width="400px">
            <el-table
              :data="Object.entries(columnLabels).map(([key, label]) => ({ key, label }))"
              border
              style="width: 100%"
            >
              <el-table-column prop="label" label="名称" width="180" align="center" />
              <el-table-column label="显示" align="center">
                <template #default="{ row }">
                  <el-switch v-model="visibleColumns[row.key]" />
                </template>
              </el-table-column>
            </el-table>
          </el-dialog>

          <!-- 商品选择弹窗 -->
          <OperationPopTable
            v-if="showProductPopup"
            :show-search-button="false"
            @confirm="handleTableProductConfirm"
            @close="showProductPopup = false"
          />

          <!-- 状态栏 -->
          <div class="grid-status">
            <span class="status-item">总条数: {{ totalCount }}</span>
            <span class="status-item">总合计: {{ totalSum }}</span>
          </div>

          <!-- 已审核标记 -->
          <div v-if="detailData.checkStatus === '已审核'" class="audit-stamp">已审核</div>
        </div>

        <!-- 底部表单 -->
        <DocumentForm
          :fields="otherOutboundFields"
          :form-data="detailFormData"
          :disabled="detailData.checkStatus === '已审核'"
        />

        <!-- 生成单据弹窗 -->
        <el-dialog
          v-model="showGeneratePopup"
          title="生成单据"
          width="300px"
          :close-on-click-modal="false"
        >
          <div class="generate-options">
            <el-button class="generate-option-btn" @click="handleGenerateOtherOutbound">
              其他出库单
            </el-button>
          </div>
        </el-dialog>
      </div>

      <!-- 对话框底部按钮 -->
      <template #footer>
        <span v-if="detailData.checkStatus !== '已审核'">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSaveDetail">保存</el-button>
          <el-button type="success" @click="handleAudit">审核</el-button>
        </span>
        <span v-else>
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="warning" @click="handleUnaudit">反审核</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 批量操作由OperationButtons组件内部处理，不再使用单独的对话框 -->
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowUp,
  ArrowDown,
  Plus,
  Delete,
  Download,
  Setting,
  Edit,
  MoreFilled,
  Refresh,
  Search,
  ArrowDown as ArrowDownIcon
} from '@element-plus/icons-vue'
import Logistics from '@/components/lib/Logistics.vue'
import CustomUpload from '@/components/CustomUpload/CustomUpload.vue'
import DocumentForm from '@/components/documentform/DocumentForm.vue'
import OperationPopTable from '@/components/operationpoptable/OperationPopTable.vue'
import ButtonGroup from '@/components/mybutton/ButtonGroup.vue'
import BusinessButton from '@/components/mybutton/BusinessButton.vue'
import Customer from '@/components/goodSearchConpoent/Customer.vue'
import axios from 'axios'

/** 外部组件 */
import GoodSearchForm from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import OperationButtons from '@/components/operationbuttons/OperationButtons.vue'

// 搜索模型
const searchModel = ref({
  productName: '',
  billNo: '',
  customer: '',
  startDate: '2020-01-01',
  endDate: '2026-12-31',
  contactPerson: '',
  checkStatus: '',
  expenseStatus: '',
  creator: '',
  billType: '',
  remark: ''
})

// 表格数据
interface RowData {
  id: string
  organizationName: string
  customer: string
  billType: string
  billDate: string
  billNo: string
  totalCost: number
  expense: number
  contactPerson: string
  checkStatus: string
  expenseStatus: string
  creator: string
  remark: string
  logistics: any
  attachments: any[]
  details: any[]
}

const tableData = ref<RowData[]>([
  {
    id: '1',
    organizationName: '默认组织',
    customer: '测试客户001',
    billType: '其他出库单',
    billDate: '2025-10-31',
    billNo: 'QTCD251031113091',
    totalCost: 23,
    expense: 0,
    contactPerson: '',
    checkStatus: '已审核',
    expenseStatus: '无需结算',
    creator: '管理员',
    remark: '',
    logistics: { key: 'auto', name: '自动识别', number: '' },
    attachments: [],
    details: [
      {
        productName: 'HY738U3-黑色4号',
        productCode: 'HY738U3-KX004',
        spec: 'HY738U3',
        extendAttribute: '',
        unit: '件',
        warehouse: '西湖仓库',
        cost: 23,
        quantity: 1,
        totalCost: 23,
        remark: ''
      }
    ]
  },
  {
    id: '2',
    organizationName: '默认组织',
    customer: '',
    billType: '其他出库单',
    billDate: '2025-10-31',
    billNo: 'QTCD251031112727',
    totalCost: 23,
    expense: 0,
    contactPerson: '',
    checkStatus: '未审核',
    expenseStatus: '无需结算',
    creator: '管理员',
    remark: '',
    logistics: { key: 'auto', name: '自动识别', number: '' },
    attachments: [],
    details: [
      {
        productName: 'HY738U3-黑色4号',
        productCode: 'HY738U3-KX004',
        spec: 'HY738U3',
        extendAttribute: '',
        unit: '件',
        warehouse: '西湖仓库',
        cost: 23,
        quantity: 1,
        totalCost: 23,
        remark: ''
      }
    ]
  }
])

const columns = [
  { prop: 'organizationName', label: '所属组织', width: 120 },
  { prop: 'customer', label: '客户', width: 180 },
  { prop: 'billType', label: '单据类型', width: 120 },
  { prop: 'billDate', label: '单据时间', width: 120, sortable: true },
  { prop: 'billNo', label: '单据编号', width: 180, sortable: true },
  { prop: 'totalCost', label: '单据成本', width: 100, align: 'right', sortable: true },
  { prop: 'expense', label: '单据费用', width: 100, align: 'right', sortable: true },
  { prop: 'contactPerson', label: '关联人员', width: 120 },
  { prop: 'checkStatus', label: '审核状态', width: 100 },
  { prop: 'expenseStatus', label: '费用状态', width: 100 },
  { prop: 'creator', label: '制单人', width: 100 },
  { prop: 'remark', label: '备注信息', width: 200 }
]

/* 选择 / 排序 */
const selected = ref<RowData[]>([])
const handleSelectionChange = (rows: RowData[]) => {
  selected.value = rows
}

const sortKey = ref<string>('')
const sortOrder = ref<'asc' | 'desc' | ''>('')
const sortTable = (key: string, order: 'asc' | 'desc') => {
  sortKey.value = key
  sortOrder.value = order
  tableData.value.sort((a, b) => {
    const va = a[key as keyof RowData] as any
    const vb = b[key as keyof RowData] as any
    if (order === 'asc') return va > vb ? 1 : va < vb ? -1 : 0
    return va < vb ? 1 : va > vb ? -1 : 0
  })
}

/* 合计 / 分页 */
const totalCost = computed(() => tableData.value.reduce((s, r) => s + (r.totalCost || 0), 0))
const totalExpense = computed(() => tableData.value.reduce((s, r) => s + (r.expense || 0), 0))
const currentPage = ref(1)
const pageSize = ref(10)
const pagedData = computed(() => {
  const s = (currentPage.value - 1) * pageSize.value
  return tableData.value.slice(s, s + pageSize.value)
})

/** ---------------- 搜索组件对接（本地筛选） ---------------- */
type SearchFormData = {
  productName?: string
  billNo?: string
  customer?: any
  contactPerson?: any
  creator?: any
  startDate?: string
  endDate?: string
  checkStatus?: string
  expenseStatus?: string
  billType?: string
  remark?: string
  [k: string]: any
}

const originalAllData = ref<RowData[]>([])

const toTime = (d?: string) => (d ? new Date(d).getTime() : NaN)
const toText = (v: any) => {
  if (v == null) return ''
  if (typeof v === 'object') return v.label || v.name || v.text || String(v.id ?? '')
  return String(v)
}
const includes = (src?: string, kw?: string) =>
  (src ?? '').toLowerCase().includes((kw ?? '').trim().toLowerCase())

const applyLocalFilter = (cond: SearchFormData) => {
  const list = originalAllData.value.filter((row) => {
    // 商品名称过滤
    if (
      cond.productName &&
      !row.details.some((d: any) => includes(d.productName, cond.productName))
    ) {
      return false
    }
    // 单据编号过滤
    if (cond.billNo && !includes(row.billNo, cond.billNo)) {
      return false
    }
    // 客户过滤
    if (cond.customer && !includes(row.customer, toText(cond.customer))) {
      return false
    }
    // 日期范围过滤
    if (cond.startDate && row.billDate < cond.startDate) {
      return false
    }
    if (cond.endDate && row.billDate > cond.endDate) {
      return false
    }
    // 关联人员过滤
    if (cond.contactPerson && !includes(row.contactPerson, toText(cond.contactPerson))) {
      return false
    }
    // 审核状态过滤
    if (cond.checkStatus && row.checkStatus !== cond.checkStatus) {
      return false
    }
    // 费用状态过滤
    if (cond.expenseStatus && row.expenseStatus !== cond.expenseStatus) {
      return false
    }
    // 制单人过滤
    if (cond.creator && !includes(row.creator, toText(cond.creator))) {
      return false
    }
    // 单据类型过滤
    if (cond.billType && row.billType !== cond.billType) {
      return false
    }
    // 备注过滤
    if (cond.remark && !includes(row.remark, cond.remark)) {
      return false
    }
    return true
  })

  tableData.value = list
  currentPage.value = 1
}

const onGoodSearch = (payload: SearchFormData) => {
  applyLocalFilter(payload && Object.keys(payload).length ? payload : searchModel)
}

/** ---------------- 批量/导入/刷新 ---------------- */
const refreshing = ref(false)
const onRefresh = () => {
  if (refreshing.value) return
  refreshing.value = true
  setTimeout(() => {
    // 恢复原始数据 + 重置分页
    tableData.value = JSON.parse(JSON.stringify(originalAllData.value))
    currentPage.value = 1
    // 重置搜索组件
    goodSearchRef.value?.resetForm?.()
    refreshing.value = false
    ElMessage.success('已刷新')
  }, 700)
}

// 批量操作由OperationButtons组件内部处理，不再使用单独的弹窗
const onBatch = () => {
  // 空函数，避免显示多余的弹窗
}

/** 承接 OperationButtons 的导入数据 */
const onImportData = (rows: any[]) => {
  const mapped = rows.map((r, i) => ({
    id: 'import-' + Date.now() + i,
    organizationName: r.organizationName ?? r.所属组织 ?? '默认组织',
    customer: r.customer ?? r.客户 ?? '',
    billType: r.billType ?? r.单据类型 ?? '其他出库单',
    billDate: r.billDate ?? r.单据时间 ?? new Date().toISOString().slice(0, 10),
    billNo: r.billNo ?? r.单据编号 ?? 'QTCD' + Date.now() + i,
    totalCost: Number(r.totalCost ?? r.单据成本 ?? 0),
    expense: Number(r.expense ?? r.单据费用 ?? 0),
    contactPerson: r.contactPerson ?? r.关联人员 ?? '',
    checkStatus: r.checkStatus ?? r.审核状态 ?? '未审核',
    expenseStatus: r.expenseStatus ?? r.费用状态 ?? '无需结算',
    creator: r.creator ?? r.制单人 ?? '导入',
    remark: r.remark ?? r.备注信息 ?? '',
    logistics: { key: 'auto', name: '自动识别', number: '' },
    attachments: [],
    details: [
      {
        productName: r.productName ?? r.商品名称 ?? '导入商品',
        productCode: r.productCode ?? r.商品编号 ?? '',
        spec: r.spec ?? r.规格型号 ?? '',
        unit: r.unit ?? r.单位 ?? '',
        extendAttribute: r.extendAttribute ?? r.辅助属性 ?? '',
        warehouse: r.warehouse ?? r.仓库 ?? '',
        cost: Number(r.cost ?? r.成本 ?? 0),
        quantity: Number(r.quantity ?? r.数量 ?? 1),
        totalCost: Number(r.totalCost ?? r.总成本 ?? 0),
        remark: r.remark ?? r.备注 ?? ''
      }
    ]
  })) as RowData[]

  // 合并到数据源
  tableData.value = [...tableData.value, ...mapped]
  originalAllData.value = JSON.parse(JSON.stringify(tableData.value))
  ElMessage.success(`已导入 ${mapped.length} 条记录`)
}

/* 删除 */
const onDelete = (row: RowData) => {
  ElMessageBox.confirm(`确定删除单据【${row.billNo}】吗？`, '提示', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消'
  })
    .then(() => {
      const i = tableData.value.findIndex((r) => r.id === row.id)
      if (i > -1) tableData.value.splice(i, 1)
      // 更新原始集合，避免刷新后"复活"
      originalAllData.value = JSON.parse(JSON.stringify(tableData.value))
      ElMessage.success('删除成功')
    })
    .catch(() => {})
}

// ====== 详情对话框相关代码 ======
const detailVisible = ref(false)
const detailData = reactive({
  id: '',
  billDate: '',
  billNo: '',
  customer: '',
  totalCost: 0,
  expense: 0,
  contactPerson: '',
  checkStatus: '',
  logistics: { key: 'auto', name: '自动识别', number: '' },
  remark: '',
  attachments: [] as any[],
  details: [] as any[]
})

// 详情表格数据
const detailTableData = computed(() => detailData.details)

// 详情表单数据
const detailFormData = computed(() => ({
  documentType: '其他出库单',
  documentCost: detailData.totalCost,
  documentExpense: detailData.expense,
  relatedPerson: detailData.contactPerson,
  logistics: detailData.logistics,
  documentAttachment: detailData.attachments,
  remarks: detailData.remark
}))

// 其他出库单需要的字段
const otherOutboundFields = [
  'documentType', // 单据类型
  'documentCost', // 单据成本
  'documentExpense', // 单据费用
  'relatedPerson', // 关联人员
  'logistics', // 物流信息
  'documentAttachment', // 单据附件
  'remarks' // 备注信息
]

// ====== 客户选择器功能 ======
const customerDropdownVisible = ref(false)
const selectedCustomerName = ref('')
const customerSearchKey = ref('')
interface Customer {
  id: number
  name: string
}
const customerList = ref<Customer[]>([])
const customerTotal = ref(0)
const customerPage = ref(1)
const customerPageSize = ref(10)

// 获取客户列表
const fetchCustomers = async () => {
  try {
    const params = {
      page: customerPage.value,
      pageSize: customerPageSize.value,
      name: customerSearchKey.value.trim()
    }

    const res = await axios.get('/api/customers', { params })

    if (res.data.code === 200) {
      customerList.value = res.data.data.list
      customerTotal.value = res.data.data.total
    } else {
      ElMessage.warning('获取客户数据失败：' + res.data.message)
    }
  } catch (error: any) {
    console.error('客户接口请求失败：', error)
    ElMessage.error(`加载客户失败：${error.response?.data?.message || '网络异常'}`)
  }
}

// 选择客户
const selectCustomer = (customer: any) => {
  selectedCustomerName.value = customer.name
  detailData.customer = customer.name
  customerDropdownVisible.value = false
}

// 清空选中的客户
const handleCustomerClear = () => {
  selectedCustomerName.value = ''
  detailData.customer = ''
}

// 分页页码切换
const handleCustomerPageChange = (page: number) => {
  customerPage.value = page
  fetchCustomers()
}

// 每页条数切换
const handleCustomerSizeChange = (size: number) => {
  customerPageSize.value = size
  customerPage.value = 1
  fetchCustomers()
}

// ====== 仓库选择功能 ======
const warehousePopVisible = ref(false)
const warehouseOptions = ref<Array<{ value: string; label: string }>>([])

// 加载仓库列表
async function loadWarehouseList() {
  try {
    // 模拟数据
    warehouseOptions.value = [
      { value: 'warehouse1', label: '主仓库' },
      { value: 'warehouse2', label: '分仓库A' },
      { value: 'warehouse3', label: '分仓库B' },
      { value: 'warehouse4', label: '临时仓库' },
      { value: 'warehouse5', label: '退货仓库' },
      { value: 'warehouse6', label: '样品仓库' },
      { value: 'warehouse7', label: '成品仓库' },
      { value: 'warehouse8', label: '原材料仓库' }
    ]
    console.log('✅ 仓库数据已加载：', warehouseOptions.value)
  } catch (err) {
    console.error('❌ 仓库数据加载失败：', err)
    ElMessage.error('获取仓库列表失败')
  }
}

// 批量设置仓库
function handleBatchWarehouseSelect(value: string) {
  if (!value) return
  const selectedWarehouse = warehouseOptions.value.find((w) => w.value === value)
  if (selectedWarehouse) {
    detailTableData.value.forEach((r) => {
      if (r.productName) r.warehouse = selectedWarehouse.label
    })
    ElMessage.success(`已批量设置仓库：${selectedWarehouse.label}`)
  }
  warehousePopVisible.value = false
}

// ====== 表格相关功能 ======
/** 计算 el 内（纯文本）的光标位置 */
function getCaretPosition(el: HTMLElement): number {
  const sel = window.getSelection()
  if (!sel || sel.rangeCount === 0) return 0
  const range = sel.getRangeAt(0)
  const preRange = range.cloneRange()
  preRange.selectNodeContents(el)
  preRange.setEnd(range.endContainer, range.endOffset)
  return preRange.toString().length
}

/** 将光标设置到 el 的第 pos 个字符后 */
function setCaretPosition(el: HTMLElement, pos: number) {
  const selection = window.getSelection()
  if (!selection) return
  const range = document.createRange()
  if (!el.firstChild) el.appendChild(document.createTextNode(''))
  const node = el.firstChild as Text
  const len = node.length
  const offset = Math.max(0, Math.min(pos, len))
  range.setStart(node, offset)
  range.collapse(true)
  selection.removeAllRanges()
  selection.addRange(range)
}

type EditableField = 'cost' | 'quantity' | 'remark'

function onEditableInput(row: any, field: EditableField, e: Event) {
  if (detailData.checkStatus === '已审核') return

  const el = e.target as HTMLElement | null
  if (!el) return

  const caret = getCaretPosition(el)
  const raw = el.innerText ?? ''

  if (field === 'remark') {
    row.remark = raw
    return
  }

  const cleaned = raw.replace(/[^\d.]/g, '')
  row[field] = cleaned

  recalculate(row)

  nextTick(() => setCaretPosition(el, caret))
}

function onEditableBlur(row: any, field: EditableField, e: Event) {
  if (detailData.checkStatus === '已审核') return

  const el = e.target as HTMLElement | null
  if (!el) return
  const text = el.innerText ?? ''

  if (field === 'remark') {
    row.remark = text
    return
  }

  const num = parseFloat(text)
  row[field] = Number.isFinite(num) ? String(num) : ''
  recalculate(row)
}

// 总条数和总合计（状态栏数据）
const totalCount = computed(
  () => detailTableData.value.filter((r: any) => r.productName && r.quantity > 0).length
)

const totalSum = computed(() => {
  return detailTableData.value
    .filter((r) => r.productName)
    .reduce((sum, r) => sum + (Number(r.totalCost) || 0), 0)
    .toFixed(2)
})

// ====== 列显隐（设置⚙）======
const showColumnSetting = ref(false)
const columnLabels: Record<string, string> = {
  productName: '商品名称',
  productCode: '商品编号',
  spec: '规格型号',
  unit: '单位',
  attr: '辅助属性',
  warehouse: '仓库',
  cost: '成本',
  quantity: '数量',
  totalCost: '总成本',
  remark: '备注信息'
}
// 初始化所有列都显示
const visibleColumns = reactive<Record<string, boolean>>({})
for (const k in columnLabels) {
  visibleColumns[k] = true
}

// ====== 常规/扫码 开关（放在"商品名称"表头）======
const scanMode = ref(false)

// ====== 金额/税额/合计 计算 ======
function recalculate(row: any) {
  const cost = parseFloat(row.cost) || 0
  const qty = parseFloat(row.quantity) || 0
  row.totalCost = +(cost * qty).toFixed(2)
  updateStatusBar()
}

// === 状态栏更新 ===
function updateStatusBar() {
  const validRows = detailTableData.value.filter((r) => r.productName)
  const totalCount = validRows.length
  const totalSum = validRows.reduce((sum, r) => sum + (Number(r.totalCost) || 0), 0)
  console.log(`状态栏已更新：总条数=${totalCount}，总合计=${totalSum.toFixed(2)}`)
}

// ====== OperationPopTable -> 表格 的回填 ======
function handleTableProductConfirm(selected: any[]) {
  if (!selected || selected.length === 0 || currentRowIndex.value === null) {
    showProductPopup.value = false
    return
  }
  const first = selected[0]
  const row = detailTableData.value[currentRowIndex.value]

  row.productName = first.name
  row.productCode = first.code
  row.spec = first.model || ''
  row.attr = first.category || ''
  row.unit = first.unit || ''
  row.warehouse = row.warehouse || ''
  row.cost = first.purchasePrice ?? ''
  row.quantity = first.quantity ?? ''

  recalculate(row)
  showProductPopup.value = false

  const last = detailTableData.value[detailTableData.value.length - 1]
  if (last && last.productName) {
    detailTableData.value.push({
      productName: '',
      productCode: '',
      spec: '',
      unit: '',
      attr: '',
      warehouse: '',
      cost: '',
      quantity: '',
      totalCost: '',
      remark: ''
    })
  }
}

// 点击"操作"按钮
function openProductPopup(index: number) {
  currentRowIndex.value = index
  showProductPopup.value = true
}

// 删除行
function deleteRow(index: number) {
  detailTableData.value.splice(index, 1)
  ElMessage.success('已删除该行')
}

// 商品弹窗控制
const showProductPopup = ref(false)
const currentRowIndex = ref<number | null>(null)

// 生成弹窗控制
const showGeneratePopup = ref(false)

// 生成单据编号
function generateBillNo(): string {
  const now = new Date()
  const year = now.getFullYear().toString().slice(2)
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const random = Math.floor(Math.random() * 100000)
    .toString()
    .padStart(5, '0')
  return `QTCD${year}${month}${day}${random}`
}

/**
 * 保存按钮处理函数
 */
function handleSaveDetail() {
  console.log('保存数据')
  console.log('头部表单数据:', detailData)

  const hasInvalidRow = detailTableData.value.some((row) => {
    return row.productName && (!row.warehouse || !row.cost || !row.quantity)
  })

  if (hasInvalidRow) {
    ElMessage.warning('请填写所有必填字段')
    return
  }

  const saveData = {
    headerForm: {
      customer: detailData.customer,
      documentDate: detailData.billDate,
      documentNumber: detailData.billNo
    },
    tableData: detailTableData.value.filter((row) => row.productName),
    isAudited: true
  }

  console.log('已保存:', saveData.tableData.length, '条数据')

  detailData.checkStatus = '已审核'

  const index = tableData.value.findIndex((item) => item.id === detailData.id)
  if (index !== -1) {
    tableData.value[index] = JSON.parse(JSON.stringify(detailData))
  }

  ElMessage.success(`保存成功，已保存 ${saveData.tableData.length} 条数据`)
}

/**
 * 刷新按钮处理函数
 */
function handleDetailRefresh() {
  console.log('刷新数据')

  detailData.billDate = new Date().toISOString().split('T')[0]
  detailData.billNo = generateBillNo()

  detailData.details = [
    {
      productName: '',
      productCode: '',
      spec: '',
      unit: '',
      attr: '',
      warehouse: '',
      cost: '',
      quantity: '',
      totalCost: '',
      remark: ''
    }
  ]

  detailData.checkStatus = '未审核'

  ElMessage.success('数据已刷新')
}

/**
 * 生成按钮处理函数
 */
function handleGenerate() {
  console.log('打开生成弹窗')
  showGeneratePopup.value = true
}

/**
 * 反审核按钮处理函数
 */
function handleUnaudit() {
  console.log('反审核')
  detailData.checkStatus = '未审核'
  ElMessage.success('已反审核，恢复到未审核状态')
}

/**
 * 生成其他出库单处理函数
 */
function handleGenerateOtherOutbound() {
  console.log('生成其他出库单')

  const validTableData = detailTableData.value.filter((item) => item.productName)
  console.log('有效数据条数:', validTableData.length)

  showGeneratePopup.value = false

  if (validTableData.length === 0) {
    ElMessage.info('没有有效的商品数据')
  } else {
    ElMessage.success(`已生成 ${validTableData.length} 条数据的其他出库单`)
  }
}

// 打开详情
const openDetail = (row: RowData) => {
  Object.assign(detailData, JSON.parse(JSON.stringify(row)))
  if (!detailData.details) {
    detailData.details = []
  }
  if (detailData.details.length === 0) {
    detailData.details.push({
      productName: '',
      productCode: '',
      spec: '',
      unit: '',
      extendAttribute: '',
      warehouse: '',
      cost: '',
      quantity: '',
      totalCost: '',
      remark: ''
    })
  }

  selectedCustomerName.value = detailData.customer || ''

  detailVisible.value = true
}

// 关闭对话框
function handleDialogClose() {
  detailVisible.value = false
  Object.keys(detailData).forEach((key) => {
    if (key === 'details') {
      detailData.details = []
    } else if (key === 'logistics') {
      detailData.logistics = { key: 'auto', name: '自动识别', number: '' }
    } else if (key === 'attachments') {
      detailData.attachments = []
    } else {
      ;(detailData as any)[key] = ''
    }
  })
  selectedCustomerName.value = ''
}

// 处理附件上传
function handleAttachmentUpload(files: FileList | null) {
  if (files) {
    const validTypes = ['.jpg', '.png', '.pdf']
    for (let i = 0; i < files.length; i++) {
      const file = files[i]
      const ext = file.name.substring(file.name.lastIndexOf('.'))
      if (!validTypes.includes(ext.toLowerCase())) {
        ElMessage.warning(`文件 ${file.name} 类型不支持，请上传jpg、png或pdf格式`)
        continue
      }
      if (!detailData.attachments) {
        detailData.attachments = []
      }
      detailData.attachments.push(file)
    }
    ElMessage.success('文件添加成功')
  }
}

// 移除附件
function handleRemoveAttachment(index: number) {
  if (detailData.attachments && detailData.attachments.length > 0) {
    detailData.attachments.splice(index, 1)
    ElMessage.success('附件已移除')
  }
}

// 审核单据
function handleAudit() {
  detailData.checkStatus = '已审核'
  ElMessage.success('审核成功')
  const index = tableData.value.findIndex((item) => item.id === detailData.id)
  if (index !== -1) {
    tableData.value[index].checkStatus = '已审核'
  }
  detailVisible.value = false
}

// 打印单据
function handlePrint(row: any) {
  ElMessage.success('打印功能开发中')
}

/** ---------------- 搜索组件引用 ---------------- */
const goodSearchRef = ref<InstanceType<typeof GoodSearchForm> | null>(null)

/* —— 固定横向滚动条：仅显示这一个，隐藏表格内部横向条 —— */
const tableRef = ref()
const tableWrapper = ref<HTMLElement | null>(null)
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
  // 备份原始数据用于重置
  originalAllData.value = JSON.parse(JSON.stringify(tableData.value))

  // 加载客户和仓库数据
  fetchCustomers()
  loadWarehouseList()

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

  // 点击页面其他区域关闭客户下拉面板
  document.addEventListener('click', (e) => {
    const target = e.target as HTMLElement
    if (!target.closest('.customer-select')) {
      customerDropdownVisible.value = false
    }
  })
})

onBeforeUnmount(() => {
  if (bodyWrapper.value) bodyWrapper.value.removeEventListener('scroll', bodyScroll)
  if (xScroll.value) xScroll.value.removeEventListener('scroll', xScrollScroll)
  if (ro && bodyWrapper.value) ro.unobserve(bodyWrapper.value)
  ro = null
})
</script>

<style scoped>
/* 批量 + 刷新贴合为一组 */
.btn-join {
  display: inline-flex;
  align-items: center;
}

/* 相邻边界合并成 1px，无缝贴合 */
.btn-join > * + * {
  margin-left: -1px;
}

/* 统一圆角：中间不圆角，仅两端有圆角 */
.btn-join :deep(.el-button) {
  border-radius: 0;
}
.btn-join :deep(.el-button:first-child) {
  border-top-left-radius: 4px;
  border-bottom-left-radius: 4px;
}
.btn-join :deep(.el-button:last-child) {
  border-top-right-radius: 4px;
  border-bottom-right-radius: 4px;
}

:root {
  --status-h: 20px;
} /* 状态栏高度（同步 x-scroll 粘附位置） */

.other-outbound-form {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: #fff;
}

/* 顶部工具栏 */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
}

/* 主体布局 */
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

/* ——隐藏表格自带横向滚动条—— */
.table-wrapper :deep(.el-table__body-wrapper) {
  overflow-x: hidden !important;
}
.table-wrapper :deep(.el-table__header-wrapper) {
  overflow: hidden !important;
}

/* 固定横向拖动条（紧贴状态栏上方） */
.x-scroll {
  position: absolute;
  left: 0;
  right: 0;
  bottom: var(--status-h);
  z-index: 6;
  height: 12px;
  overflow-x: auto;
  overflow-y: hidden;
  background: #f0f2f5;
  border-top: 1px solid #ebeef5;
}
.x-scroll-inner {
  height: 1px;
}

/* 统一滚动条样式（横/纵一致） */
.scrollbar::-webkit-scrollbar,
.table-wrapper::-webkit-scrollbar {
  height: 12px;
  width: 12px;
}
.scrollbar::-webkit-scrollbar-track,
.table-wrapper::-webkit-scrollbar-track {
  background: #f0f2f5;
  border-radius: 6px;
}
.scrollbar::-webkit-scrollbar-thumb,
.table-wrapper::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 6px;
  border: 2px solid #f0f2f5;
}
.scrollbar::-webkit-scrollbar-thumb:hover,
.table-wrapper::-webkit-scrollbar-thumb:hover {
  background: #a8abb2;
}

/* 表头：列名 + 垂直排序箭头（列名右侧上下排列） */
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

/* 相关操作三按钮：灰描边、黑白配、无缝拼接、hover 蓝高亮 */
.op-row {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

/* 通用按钮外观 */
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
/* 消除相邻边框双线，确保删除与下拉"贴合"（el-dropdown 也参与） */
.op-row > * + * {
  margin-left: -1px;
}
.op-btn + .op-btn {
  margin-left: -1px;
}

/* 仅两端保留圆角：第一个按钮左圆角，最后一个按钮右圆角 */
.op-row > :first-child .op-btn,
.op-row > :first-child.op-btn {
  border-top-left-radius: 4px;
  border-bottom-left-radius: 4px;
}
/* 下拉按钮去左圆角 */
:deep(.op-row .el-dropdown .el-button) {
  border-radius: 0 !important;
}
/* 下拉按钮位于末端时给右圆角 */
:deep(.op-row .el-dropdown:last-child .el-button) {
  border-top-right-radius: 4px !important;
  border-bottom-right-radius: 4px !important;
}

/* hover / focus 高亮 */
.op-btn:hover,
.op-btn:focus {
  border-color: #409eff;
  color: #409eff;
}
:deep(.op-row .el-dropdown .el-button:hover),
:deep(.op-row .el-dropdown .el-button:focus) {
  border-color: #409eff;
  color: #409eff;
}

/* 底部状态栏（贴底） */
.status-bar {
  position: sticky;
  bottom: 0;
  z-index: 5;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #f7f8fa;
  border-top: 1px solid #ebeef5;
  height: var(--status-h);
  padding: 4px 10px;
}
.status-right {
  font-size: 13px;
  color: #606266;
}
.ml-24 {
  margin-left: 24px;
}

/* 小工具类 */
.mr-8 {
  margin-right: 8px;
}

/* 详情对话框样式 */
.detail-content {
  padding: 20px;
  max-height: calc(100vh - 150px);
  overflow-y: auto;
  position: relative;
}

/* ===== 按钮图标黑白化风格 ===== */
:deep(.el-button) {
  color: #333; /* 图标和文字黑色 */
}

:deep(.el-button:hover) {
  color: #000; /* 悬浮时稍微深一点 */
  background-color: #f5f5f5;
}

:deep(.el-icon svg) {
  width: 16px;
  height: 16px;
  fill: currentColor; /* 继承字体颜色 */
}

/* 特别让危险删除按钮也保持黑白 */
:deep(.el-button.is-text.el-button--danger) {
  color: #333;
}
:deep(.el-button.is-text.el-button--danger:hover) {
  color: #000;
  background-color: #f5f5f5;
}
:deep(.el-button.is-text) {
  border: none;
  box-shadow: none;
  padding: 4px;
  color: #444;
}

:deep(.el-button.is-text:hover) {
  background-color: #eee;
}

.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.table-wrapper {
  height: 500px; /* 固定表格区域高度，可按你页面布局调整，比如 400~500px */
  overflow-y: auto; /* 超出时出现纵向滚动条 */
  border: 1px solid #ebeef5; /* 与 el-table 边框融合 */
  border-radius: 4px;
  background-color: white;
}

.detail-content .table-section {
  position: relative; /* 为审核标记提供定位上下文 */
  background-color: rgb(255, 255, 255);
  border-radius: 8px 8px 0 0; /* 只保留顶部圆角 */
  padding: 20px;
  padding-bottom: 10px; /* 减少底部内边距 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.grid-status {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 20px;
  font-size: 14px;
  color: #333;
  background-color: #f9fafb;
  padding: 8px 12px;
  border-radius: 4px;
}

.status-item {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

/* 表单和按钮行 */
.header-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
  gap: 20px;
}

/* 左侧表单字段 */
.header-form {
  display: flex;
  gap: 16px;
  align-items: flex-start;
  flex: 1;
}

/* 单个表单项 */
.form-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

/* 表单标签 */
.form-label {
  font-size: 13px;
  color: #606266;
  font-weight: 500;
  white-space: nowrap;
}

/* 客户输入框 */
.customer-input {
  width: 240px;
}
/* 客户下拉选择器样式 */
.customer-select {
  position: relative;
  width: 240px; /* 和原有输入框宽度一致，不破坏布局 */
}

/* 下拉面板样式 */
.customer-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  z-index: 1000; /* 确保在最上层 */
  padding: 10px;
  margin-top: 2px;
}

/* 搜索框样式 */
.customer-search {
  margin-bottom: 10px;
}

/* 客户列表样式（滚动） */
.customer-list {
  max-height: 200px; /* 限制高度，超出滚动 */
  overflow-y: auto;
  margin-bottom: 10px;
}

/* 列表项样式 */
.customer-item {
  padding: 8px 10px;
  cursor: pointer;
  transition: background 0.2s;
}
.customer-item:hover {
  background-color: #f5f7fa; /* 悬浮高亮 */
}

/* 空数据提示 */
.empty-tip {
  text-align: center;
  color: #909399;
  padding: 10px;
  font-size: 14px;
}

/* 分页控件样式 */
.customer-pagination {
  text-align: right;
  font-size: 12px;
}
.customer-input :deep(.el-input__inner) {
  cursor: pointer;
}

.search-icon {
  cursor: pointer;
  color: #409eff;
}

/* 单据日期 */
.date-input {
  width: 160px;
}

/* 单据编号 */
.number-input {
  width: 180px;
}

/* 操作按钮容器 - 在表格上方 */
.action-buttons-container {
  display: flex;
  justify-content: flex-end;
  padding: 0;
}

/* 按钮组样式 - 使按钮连在一起 */
.action-buttons-container :deep(.button-group) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  overflow: hidden;
}

/* 按钮样式 */
.action-buttons-container :deep(.el-button) {
  margin: 0 !important;
  border-radius: 0 !important;
  transition: all 0.3s ease;
}

/* 按钮之间的分隔线 */
.action-buttons-container :deep(.el-button + .el-button) {
  border-left: 1px solid rgba(255, 255, 255, 0.3);
}

/* 第一个按钮左侧圆角 */
.action-buttons-container :deep(.el-button:first-child) {
  border-top-left-radius: 4px !important;
  border-bottom-left-radius: 4px !important;
}

/* 最后一个按钮右侧圆角 */
.action-buttons-container :deep(.el-button:last-child) {
  border-top-right-radius: 4px !important;
  border-bottom-right-radius: 4px !important;
}

/* 按钮悬浮效果 */
.action-buttons-container :deep(.el-button:hover) {
  background-color: #66b1ff;
  border-color: #66b1ff;
  z-index: 1;
}

/* 按钮激活效果 */
.action-buttons-container :deep(.el-button:active) {
  background-color: #3a8ee6;
  border-color: #3a8ee6;
}

/* 已审核标记样式 */
.audit-stamp {
  position: absolute;
  bottom: 80px;
  right: 40px;
  padding: 12px 32px;
  font-size: 28px;
  font-weight: bold;
  color: #e74c3c;
  border: 4px solid #e74c3c;
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.95);
  transform: rotate(-15deg);
  box-shadow: 0 4px 12px rgba(231, 76, 60, 0.3);
  pointer-events: none;
  z-index: 10;
  letter-spacing: 4px;
  font-family: 'Microsoft YaHei', 'SimHei', sans-serif;
  animation: stamp-appear 0.4s ease-out;
}

@keyframes stamp-appear {
  0% {
    opacity: 0;
    transform: rotate(-15deg) scale(0.5);
  }
  50% {
    transform: rotate(-15deg) scale(1.1);
  }
  100% {
    opacity: 1;
    transform: rotate(-15deg) scale(1);
  }
}

/* 生成弹窗样式 */
.generate-options {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 10px 0;
}

.generate-option-btn {
  width: 100%;
  height: 50px;
  font-size: 16px;
  font-weight: 500;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.generate-option-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
  background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
}

.generate-option-btn:active {
  transform: translateY(0);
  box-shadow: 0 2px 10px rgba(102, 126, 234, 0.3);
}

/* 可编辑单元格通用样式 */
.editable-cell {
  min-height: 32px;
  line-height: 32px;
  text-align: center;
  border: none;
  outline: none;
  background-color: transparent;
  padding: 0;
  font-size: 14px;
  color: #303133;
}

.square-btn {
  width: 36px;
  height: 30px;
  border: 1px solid #dcdfe6;
}

/* 仓库选择相关样式 */
/* 表头触发区（文字+下拉图标） */
.depot-header-trigger {
  display: flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  padding: 2px 4px;
  border-radius: 3px;
}

.depot-header-trigger:hover {
  background-color: #f5f7fa;
}

.depot-icon {
  font-size: 14px;
  color: #909399;
}

.clickable-icon {
  color: #409eff;
  transition: transform 0.2s ease;
}

.depot-header-trigger:hover .clickable-icon {
  transform: rotate(180deg);
  color: #66b1ff;
}

/* 仓库下拉面板样式 */
.depot-options-no-search {
  max-height: 200px;
  overflow-y: auto;
  border-radius: 4px;
  border: 1px solid #ebeef5;
}

.depot-option-item {
  padding: 8px 12px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.depot-option-item:hover {
  background-color: #f5f7fa;
}

/* 单元格仓库选择器样式 */
.cell-depot-select {
  width: 100%;
}

/* 优化选择器边框样式（仅聚焦时显示） */
:deep(.cell-depot-select .el-select__wrapper) {
  border: none !important;
  box-shadow: none !important;
}

:deep(.cell-depot-select .el-select__wrapper:focus-within) {
  box-shadow: 0 0 0 1px #409eff !important;
  border-radius: 4px;
}

:deep(.cell-depot-select .el-select__icon) {
  margin-left: 4px;
  color: #909399;
}
</style>
