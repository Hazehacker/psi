<template>
  <div class="page-container">
    <!-- 商品表格区域 -->
    <div class="table-section">
      <!-- 表单和按钮行 -->
      <div class="header-row">
        <!-- 左侧表单字段 -->
        <div class="header-form">
          <!-- 客户 -->
          <div class="form-item">
            <label class="form-label">客户</label>
            <el-input
              v-model="headerForm.customer"
              placeholder="请选择客户"
              readonly
              clearable
              class="customer-select"
              style="cursor: pointer"
              @focus="showCustomerDialog = true"
              @clear="handleClearCustomer"
            >
              <template #suffix>
                <el-icon @click.stop="showCustomerDialog = true" style="cursor: pointer">
                  <ArrowDown />
                </el-icon>
              </template>
            </el-input>

            <!-- 客户搜索对话框 -->
            <el-dialog
              v-model="showCustomerDialog"
              title="客户搜索"
              width="720px"
              append-to-body
              :close-on-click-modal="false"
              :modal="true"
              destroy-on-close
            >
              <div @click.stop @mousedown.stop @mouseup.stop>
                <Customer @search="handleCustomerSearch" />
              </div>
              <template #footer>
                <div @click.stop @mousedown.stop @mouseup.stop>
                  <el-button @click="showCustomerDialog = false">关闭</el-button>
                </div>
              </template>
            </el-dialog>
          </div>

          <!-- 单据日期 -->
          <div class="form-item">
            <label class="form-label">单据日期</label>
            <el-date-picker
              v-model="headerForm.documentDate"
              type="date"
              placeholder="请选择日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              class="date-input"
            />
          </div>

          <!-- 单据编号 -->
          <div class="form-item">
            <label class="form-label">单据编号</label>
            <el-input
              v-model="headerForm.documentNumber"
              placeholder="请输入单据编号"
              clearable
              class="number-input"
            />
          </div>
        </div>

        <!-- 右侧操作按钮组 -->
        <div class="action-buttons-container">
          <!-- 未审核状态：显示保存和刷新 -->
          <ButtonGroup v-if="!isAudited" align="right" gap="0px">
            <BusinessButton action-type="save" @action="handleSave" />
            <BusinessButton action-type="refresh" @action="handleRefresh" />
          </ButtonGroup>

          <!-- 已审核状态：显示生成、核对/反核对、反审核和刷新 -->
          <ButtonGroup v-else align="right" gap="0px">
            <BusinessButton action-type="generate" @action="handleGenerate" />
            <BusinessButton v-if="!isChecked" action-type="check" @action="handleCheck" />
            <BusinessButton v-else action-type="uncheck" @action="handleUncheck" />
            <BusinessButton action-type="unaudit" @action="handleUnaudit" />
            <BusinessButton action-type="refresh" @action="handleRefresh" />
          </ButtonGroup>
        </div>
      </div>

      <!-- 表格（固定高度，内部滚动，不挤占下面状态栏） -->
      <div class="table-wrapper">
        <el-table :data="tableData" border highlight-current-row style="width: 100%">
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
              >
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button v-else type="text" size="small" @click="deleteRow(scope.$index)">
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
          <el-table-column v-if="visibleColumns.spec" prop="spec" label="规格型号" align="center" />
          <el-table-column v-if="visibleColumns.attr" prop="attr" label="辅助属性" align="center" />
          <el-table-column v-if="visibleColumns.unit" prop="unit" label="单位" align="center" />

          <!-- 仓库（表头带🔁批量按钮） -->
          <el-table-column v-if="visibleColumns.warehouse" label="仓库" align="center">
            <template #header>
              <div class="flex-center">
                <span>仓库</span>
                <!-- 批量选择按钮：打开批量仓库选择弹层 -->
                <el-popover
                  placement="bottom"
                  trigger="click"
                  :show-arrow="false"
                  :offset="6"
                  width="400"
                  v-model:visible="batchWarehouseVisible"
                >
                  <Warehouse @confirm="handleBatchWarehouseSelect" />
                  <template #reference>
                    <el-button type="text" size="small">
                      <el-icon><Refresh /></el-icon>
                    </el-button>
                  </template>
                </el-popover>
              </div>
            </template>

            <template #default="scope">
              <!-- 每行单独选择仓库 -->
              <el-popover
                placement="bottom"
                trigger="click"
                width="400"
                v-model:visible="rowPopoverVisible[scope.$index]"
                @show="currentWarehouseIndex = scope.$index"
              >
                <Warehouse @confirm="handleWarehouseConfirm" />
                <template #reference>
                  <el-button type="text" size="small">
                    {{ scope.row.warehouse || '选择仓库' }}
                  </el-button>
                </template>
              </el-popover>
            </template>
          </el-table-column>

          <!-- 单价 -->
          <el-table-column label="单价" align="center">
            <template #default="{ row }">
              <div
                contenteditable="true"
                class="editable-cell"
                @input="onEditableInput(row, 'price', $event)"
                @blur="onEditableBlur(row, 'price', $event)"
              >
                {{ row.price || '' }}
              </div>
            </template>
          </el-table-column>

          <!-- 数量 -->
          <el-table-column label="数量" align="center">
            <template #default="{ row }">
              <div
                contenteditable="true"
                class="editable-cell"
                @input="onEditableInput(row, 'quantity', $event)"
                @blur="onEditableBlur(row, 'quantity', $event)"
              >
                {{ row.quantity || '' }}
              </div>
            </template>
          </el-table-column>

          <el-table-column
            v-if="visibleColumns.amount"
            prop="amount"
            label="金额"
            align="center"
            width="100"
          />

          <!-- 税率(%) -->
          <el-table-column label="税率(%)" align="center">
            <!-- 表头：标题 + 批量按钮 -->
            <template #header>
              <div class="flex-center">
                <span>税率(%)</span>
                <el-button type="text" size="small" @click="showTaxBatch = true">
                  <el-icon><Refresh /></el-icon>
                </el-button>
              </div>
            </template>
            <template #default="{ row }">
              <div
                contenteditable="true"
                class="editable-cell"
                @input="onEditableInput(row, 'taxRate', $event)"
                @blur="onEditableBlur(row, 'taxRate', $event)"
              >
                {{ row.taxRate || '' }}
              </div>
            </template>
          </el-table-column>

          <el-table-column
            v-if="visibleColumns.taxAmount"
            prop="taxAmount"
            label="税额"
            align="center"
            width="100"
          />
          <el-table-column
            v-if="visibleColumns.total"
            prop="total"
            label="价税合计"
            align="center"
            width="110"
          />
          <!-- 备注信息 -->
          <el-table-column label="备注信息" align="center">
            <template #default="{ row }">
              <div
                contenteditable="true"
                class="editable-cell"
                @input="onEditableInput(row, 'remark', $event)"
                @blur="onEditableBlur(row, 'remark', $event)"
              >
                {{ row.remark || '' }}
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- 列显隐弹窗（⚙按钮触发） -->
      <el-dialog v-model="showColumnSetting" title="列显示设置" width="360px">
        <el-checkbox-group v-model="checkedColumns">
          <el-checkbox v-for="(label, key) in columnLabels" :key="key" :label="key">
            {{ label }}
          </el-checkbox>
        </el-checkbox-group>
        <template #footer>
          <el-button @click="showColumnSetting = false">关闭</el-button>
        </template>
      </el-dialog>

      <!-- 税率批量设置 -->
      <el-dialog v-model="showTaxBatch" title="批量设置税率" width="320px">
        <el-input v-model.number="batchTax" type="number" placeholder="请输入税率" />
        <template #footer>
          <el-button @click="showTaxBatch = false">取消</el-button>
          <el-button type="primary" @click="applyTaxBatch">保存</el-button>
        </template>
      </el-dialog>

      <!-- 你的商品选择弹窗（文件名：operationpoptable.vue） -->
      <OperationPopTable
        v-if="showProductPopup"
        @confirm="handleTableProductConfirm"
        @close="showProductPopup = false"
      />

      <!-- 状态栏 -->
      <div class="grid-status">
        <span class="status-item">总条数: {{ totalCount }}</span>
        <span class="status-item">总合计: {{ totalSum }}</span>
      </div>

      <!-- 已审核标记 -->
      <div v-if="isAudited" class="audit-stamp">已审核</div>
    </div>

    <!-- 底部表单 -->
    <DocumentForm :fields="purchaseOrderFields" />

    <!-- 生成单据弹窗 -->
    <el-dialog
      v-model="showGeneratePopup"
      title="生成单据"
      width="300px"
      :close-on-click-modal="false"
    >
      <div class="generate-options">
        <el-button class="generate-option-btn" @click="handleGeneratePurchaseReturn">
          销售退货单
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import DocumentForm from '@/components/documentform/DocumentForm.vue'
import OperationPopTable from '@/components/operationpoptable/OperationPopTable.vue'
import ButtonGroup from '@/components/mybutton/ButtonGroup.vue'
import BusinessButton from '@/components/mybutton/BusinessButton.vue'
import { ref, reactive, computed, onMounted, getCurrentInstance, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { AgGridVue } from 'ag-grid-vue3'
import { ModuleRegistry, AllCommunityModule } from 'ag-grid-community'
import 'ag-grid-community/styles/ag-grid.css'
import 'ag-grid-community/styles/ag-theme-alpine.css'
import type { GridReadyEvent, GridApi, ColDef, GridOptions } from 'ag-grid-community'
import { Check, Refresh, Search, Setting, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { usePurchaseStore } from '@/stores/purchase'

import axios from 'axios'

// 导入自定义 AG-Grid 组件
import Dispose from '@/components/ag-Grid/Dispose.vue'
import Scheme from '@/components/ag-Grid/Scheme.vue'
import Storeroom from '@/components/ag-Grid/Storeroom.vue'
import Allowance from '@/components/ag-Grid/Allowance.vue'
import TaxRate from '@/components/ag-Grid/TaxRate.vue'
import Setter from '@/components/ag-Grid/Setter.vue'
import OperationButton from '@/components/ag-Grid/OperationButton.vue'
// @ts-ignore
import QuickInput from '@/components/ag-Grid/QuickInput.vue'
import Company from '@/components/ag-Grid/Company.vue'
import Depot from '@/components/ag-Grid/Depot.vue'
import Storehouse from '@/components/ag-Grid/Storehouse.vue'
import Price from '@/components/ag-Grid/Price.vue'

import { nextTick } from 'vue'
import Warehouse from '@/components/goodSearchConpoent/Warehouse.vue'
import Customer from '@/components/goodSearchConpoent/Customer.vue'
import { ArrowDown } from '@element-plus/icons-vue'

// 导入销售单API
import { saleOrderApi } from '@/apis/sales/order'
import type { SaleDTO, SaleDetailItem } from '@/apis/sales/order/type'

// 当前选中的行索引
const currentWarehouseIndex = ref<number | null>(null)
// 批量弹层控制
const batchWarehouseVisible = ref(false)

/** 切换某行的弹层显示状态 */
function toggleRowPopover(index: number) {
  // 先关闭其他行的弹层
  rowPopoverVisible.value = rowPopoverVisible.value.map(() => false)
  // 再切换当前行
  rowPopoverVisible.value[index] = !rowPopoverVisible.value[index]
}

/** 单行选择确认 */
function handleWarehouseConfirm(selected: any) {
  if (!selected || currentWarehouseIndex.value === null) return
  const row = tableData.value[currentWarehouseIndex.value]
  row.warehouse = selected.name || selected.warehouseName || ''
  rowPopoverVisible.value[currentWarehouseIndex.value] = false
  ElMessage.success(`已选择仓库：${row.warehouse}`)
}

/** 批量选择确认 */
function handleBatchWarehouseSelect(selected: any) {
  if (!selected) return
  const selectedName = selected.name || selected.warehouseName || ''
  tableData.value.forEach((r) => {
    if (r.productName) r.warehouse = selectedName
  })
  batchWarehouseVisible.value = false
  ElMessage.success(`已批量设置仓库：${selectedName}`)
}

// 客户选择对话框
const showCustomerDialog = ref(false)

// 处理客户搜索
function handleCustomerSearch(params: any) {
  if (params && typeof params === 'object') {
    const name = params.name || params.customerName || ''
    headerForm.customer = name || ''
    form.supplier = name || null
  }
  showCustomerDialog.value = false
}

// 清除客户
function handleClearCustomer() {
  headerForm.customer = ''
  form.supplier = null
}

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
  // 确保有一个文本节点
  if (!el.firstChild) el.appendChild(document.createTextNode(''))
  const node = el.firstChild as Text
  const len = node.length
  const offset = Math.max(0, Math.min(pos, len))
  range.setStart(node, offset)
  range.collapse(true)
  selection.removeAllRanges()
  selection.addRange(range)
}

type EditableField = 'price' | 'quantity' | 'taxRate' | 'remark'

function onEditableInput(row: any, field: EditableField, e: Event) {
  const el = e.target as HTMLElement | null
  if (!el) return

  const caret = getCaretPosition(el)
  const raw = el.innerText ?? ''

  if (field === 'remark') {
    row.remark = raw
    // 备注不触发重算
    return
  }

  // 数值字段：清洗为数字/小数
  const cleaned = raw.replace(/[^\d.]/g, '')
  row[field] = cleaned

  // 只在内容变化时才重算（避免无谓渲染）
  recalculate(row)

  // 渲染完成后恢复光标
  nextTick(() => setCaretPosition(el, caret))
}

function onEditableBlur(row: any, field: EditableField, e: Event) {
  const el = e.target as HTMLElement | null
  if (!el) return
  const text = el.innerText ?? ''

  if (field === 'remark') {
    row.remark = text
    return
  }

  const num = parseFloat(text)
  // 失焦时把值规整为数值或空
  row[field] = Number.isFinite(num) ? String(num) : ''
  recalculate(row)
}
// 仓库下拉选项（由后端获取）
const warehouseOptions = ref<string[]>([])

// 总条数和总合计（状态栏数据）
const totalCount = computed(
  () => tableData.value.filter((r: any) => r.productName && r.quantity > 0).length
)

const totalSum = computed(() => {
  return tableData.value
    .filter((r) => r.productName)
    .reduce((sum, r) => sum + (Number(r.price) || 0) * (Number(r.quantity) || 0), 0)
    .toFixed(2)
})

// 仓库选择变化时触发
function handleWarehouseChange(row: any) {
  // 更新当前行仓库数据
  row.warehouse = row.warehouse || ''
  // 触发计算
  recalculate(row)
  // 显示提示
  ElMessage.success(`已选择仓库：${row.warehouse}`)
}

// 从后端加载仓库列表
async function loadWarehouseList() {
  // try {
  //   const { data } = await axios.get('/api/warehouse/list') // ✅ 你的后端接口
  //   if (Array.isArray(data)) {
  //     warehouseOptions.value = data.map((item: any) => item.name || item)
  //   } else if (data?.data) {
  //     warehouseOptions.value = data.data.map((item: any) => item.name || item)
  //   }
  //   console.log('✅ 仓库数据已加载：', warehouseOptions.value)
  // } catch (err) {
  //   console.error('❌ 仓库数据加载失败：', err)
  //   ElMessage.error('获取仓库列表失败')
  // }
}

// 页面加载时获取数据
onMounted(() => {
  loadWarehouseList()
})
const tableData = ref([
  {
    productName: '九八',
    productCode: '2233535',
    spec: '',
    attr: '',
    unit: '件',
    warehouse: '',
    price: 199,
    quantity: 1,
    amount: 199,
    taxRate: 13,
    taxAmount: 25.87,
    total: 224.87,
    remark: ''
  },
  {
    productName: '',
    productCode: '',
    spec: '',
    attr: '',
    unit: '',
    warehouse: '',
    price: '',
    quantity: '',
    amount: '',
    taxRate: '',
    taxAmount: '',
    total: '',
    remark: ''
  }
])
// ② 然后根据表格的行数，初始化一模一样长度的弹层可见性数组
const rowPopoverVisible = ref<boolean[]>(tableData.value.map(() => false))

// ====== 列显隐（设置⚙）======
const showColumnSetting = ref(false)
const columnLabels: Record<string, string> = {
  productName: '商品名称',
  productCode: '商品编号',
  spec: '规格型号',
  attr: '辅助属性',
  unit: '单位',
  warehouse: '仓库',
  price: '单价',
  quantity: '数量',
  taxRate: '税率(%)',
  amount: '金额',
  taxAmount: '税额',
  total: '价税合计',
  remark: '备注信息'
}
const checkedColumns = ref<string[]>(Object.keys(columnLabels))
const visibleColumns = reactive<Record<string, boolean>>({})
watch(
  checkedColumns,
  (val) => {
    for (const k in columnLabels) visibleColumns[k] = val.includes(k)
  },
  { immediate: true }
)

// ====== 常规/扫码 开关（放在"商品名称"表头）======
const scanMode = ref(false)

// ====== 批量设置：仓库 ======
const showWarehouseBatch = ref(false)
const batchWarehouse = ref<string>('')
function applyWarehouseBatch() {
  if (!batchWarehouse.value) return
  tableData.value.forEach((r) => {
    if (r.productName) r.warehouse = batchWarehouse.value
  })
  showWarehouseBatch.value = false
  ElMessage.success('批量设置仓库完成')
}

// ====== 批量设置：税率 ======
const showTaxBatch = ref(false)
const batchTax = ref<number | null>(null)
function applyTaxBatch() {
  if (batchTax.value == null) return
  tableData.value.forEach((r: any) => {
    if (r.productName) r.taxRate = Number(batchTax.value)
    recalculate(r)
  })
  showTaxBatch.value = false
  ElMessage.success('批量设置税率完成')
}

// ====== 金额/税额/合计 计算 ======
function recalculate(row: any) {
  const price = parseFloat(row.price) || 0
  const qty = parseFloat(row.quantity) || 0
  const rate = parseFloat(row.taxRate) || 0

  // 计算金额、税额、总计
  row.amount = +(price * qty).toFixed(2)
  row.taxAmount = +((row.amount * rate) / 100).toFixed(2)
  row.total = +(row.amount + row.taxAmount).toFixed(2)

  // 计算完后立即刷新状态栏
  updateStatusBar()
}
// === 状态栏更新 ===
function updateStatusBar() {
  // 重新计算总条数和总合计
  const validRows = tableData.value.filter((r) => r.productName)
  const totalCount = validRows.length
  const totalSum = validRows.reduce((sum, r) => sum + (Number(r.total) || 0), 0)

  // 如果你状态栏的数据是 grid.status 那种结构
  if (grid?.status) {
    grid.status = [
      { text: '总条数', type: 'count', value: totalCount },
      { text: '总合计', type: 'sum', value: totalSum.toFixed(2) }
    ]
  }

  console.log(`状态栏已更新：总条数=${totalCount}，总合计=${totalSum.toFixed(2)}`)
}

// ====== OperationPopTable -> 表格 的回填（不影响你 ag-grid 的 handleProductConfirm）======
function handleTableProductConfirm(selected: any[]) {
  if (!selected || selected.length === 0 || currentRowIndex.value === null) {
    showProductPopup.value = false
    return
  }
  const first = selected[0]
  const row = tableData.value[currentRowIndex.value]

  // 适配你的商品结构字段名（如有不同自己改一下）
  row.productName = first.name
  row.productCode = first.code
  row.spec = first.model || ''
  row.attr = first.category || ''
  row.unit = first.unit || ''
  row.warehouse = row.warehouse || '' // 不覆盖已有仓库
  row.price = first.purchasePrice ?? 0
  row.quantity = first.quantity ?? 1
  row.taxRate = row.taxRate ?? 13

  recalculate(row)
  showProductPopup.value = false

  // 保证最后一行始终为空行
  const last = tableData.value[tableData.value.length - 1]
  if (last && last.productName) {
    tableData.value.push({
      productName: '',
      productCode: '',
      spec: '',
      attr: '',
      unit: '',
      warehouse: '',
      price: '',
      quantity: '',
      amount: '',
      taxRate: '',
      taxAmount: '',
      total: '',
      remark: ''
    })
    // 👇 新增的这一行，也要有一个对应的"弹层是否显示"的位置
    rowPopoverVisible.value.push(false)
  }
}

// 仓库选项
// const warehouseOptions = ['1', '23', '九岁', '哈哈哈', '二仓']

// 点击"操作"按钮
function openProductPopup(index: number) {
  currentRowIndex.value = index
  showProductPopup.value = true
}

// 删除行
function deleteRow(index: number) {
  tableData.value.splice(index, 1)
  ElMessage.success('已删除该行')
}

// 注册 AG-Grid 社区模块
ModuleRegistry.registerModules([AllCommunityModule])

// 获取当前实例和路由
const instance = getCurrentInstance()
const router = useRouter()
const route = useRoute()

// 扩展 GridOptions 类型以支持 api
interface ExtendedGridOptions extends GridOptions {
  api?: GridApi
}

// 扩展 ColDef 类型以支持自定义属性
interface ExtendedColDef extends ColDef {
  dispose?: boolean
  [key: string]: any
}

// 销售单需要的字段
const purchaseOrderFields = [
  'documentCost', // 单据金额
  'actualAmount', // 实际金额
  'paidAmount', // 实付金额
  'documentFee', // 单据费用
  'settlementAccount', // 结算账户
  'relatedPerson', // 关联人员
  'logistics', // 物流信息
  'documentAttachment', // 单据附件
  'remarks' // 备注信息
]

// 头部表单数据
const headerForm = reactive({
  customer: '',
  supplier: null as string | null,
  documentDate: '2025-11-01',
  documentNumber: 'XSDD2511012119410'
})

// 初始化 Pinia store
const purchaseStore = usePurchaseStore()

// 定义响应式数据
const rowData = ref<any[]>([])

// 表单数据
const form = reactive({
  supplier: null as string | null // 供应商
})

// 商品弹窗控制
const showProductPopup = ref(false)
const currentRowIndex = ref<number | null>(null)

// 审核状态管理
const isAudited = ref(false)

// 核对状态管理
const isChecked = ref(false)

// 生成弹窗控制
const showGeneratePopup = ref(false)

// 生成唯一ID的辅助函数
const getUniqid = () => {
  return Date.now().toString(36) + Math.random().toString(36).substring(2)
}

// Grid 配置
const grid = reactive({
  options: {
    theme: 'legacy',
    animateRows: true,
    rowSelection: 'multiple',
    enableCellTextSelection: true,
    ensureDomOrder: true,
    suppressMenu: false,
    singleClickEdit: true,
    suppressColumnVirtualisation: true, // 禁用列虚拟化，确保所有列都渲染
    suppressHorizontalScroll: false, // 允许水平滚动
    onCellEditingStopped: (params: any) => {
      // 单元格编辑停止后的处理
      grid.context.runHandleGrid()
    },
    api: undefined
  } as ExtendedGridOptions,
  context: {
    row: { uniqid: getUniqid(), key: null },
    runHandleGrid: () => {
      if (grid.options.api == null) {
        return false
      } else {
        handleGrid()
      }
    },
    componentParent: null as any // 将在 mounted 时设置
  },
  coldef: {
    suppressMenu: false,
    resizable: false, // 禁用列宽调整，使列宽固定
    singleClickEdit: true,
    suppressMovable: true
  } as ExtendedColDef,
  components: {
    dispose: Dispose,
    scheme: Scheme,
    storeroom: Storeroom,
    allowance: Allowance,
    taxRate: TaxRate,
    setter: Setter,
    operationButton: OperationButton,
    quickInput: QuickInput,
    company: Company,
    depot: Depot,
    storehouse: Storehouse,
    price: Price
  },
  getRowNodeId(row: any) {
    return row.uniqid
  },
  column: [
    {
      headerName: '序号',
      width: 50,
      field: 'sequence',
      headerComponent: 'dispose',
      headerComponentParams: { dispose: { key: 'bor' } },
      valueGetter: (params: any) => params.node.rowIndex + 1,
      hide: false,
      dispose: false
    },
    {
      headerName: '商品名称',
      width: 150,
      field: 'name',
      headerComponent: 'scheme',
      headerComponentParams: { dispose: { title: '商品名称' } },
      editable: true,
      cellEditor: 'quickInput',
      hide: false,
      dispose: true
    },
    {
      headerName: '商品编号',
      width: 120,
      field: 'number',
      hide: false,
      dispose: true
    },
    {
      headerName: '规格型号',
      width: 120,
      field: 'spec',
      hide: false,
      dispose: true
    },
    {
      headerName: '辅助属性',
      width: 120,
      field: 'attr',
      hide: false,
      dispose: true
    },
    {
      headerName: '单位',
      width: 120,
      field: 'unit',
      editable: true,
      cellEditor: 'company',
      hide: false,
      dispose: true
    },
    {
      headerName: '仓库',
      width: 120,
      field: 'warehouse',
      headerComponent: 'storeroom',
      headerComponentParams: {
        dispose: { title: '仓库', key: 'warehouseId', text: 'warehouse' }
      },
      cellRenderer: 'depot',
      cellRendererParams: {
        dispose: { key: 'warehouseId', text: 'warehouse' }
      },
      editable: true,
      cellEditor: 'storehouse',
      cellEditorParams: {
        dispose: { key: 'warehouseId', text: 'warehouse' }
      },
      hide: false,
      dispose: true
    },
    {
      headerName: '单价',
      width: 90,
      field: 'price',
      editable: true,
      cellEditor: 'price',
      cellEditorParams: {
        dispose: {
          model: 'bor',
          source: () => {
            return form.supplier
          }
        }
      },
      hide: false,
      dispose: true
    },
    {
      headerName: '数量',
      width: 90,
      field: 'nums',
      editable: true,
      hide: false,
      dispose: true
    },
    {
      headerName: '入库数量',
      width: 90,
      field: 'handle',
      hide: true,
      dispose: false
    },
    {
      headerName: '折扣率(%)',
      width: 90,
      field: 'discount',
      headerComponent: 'allowance',
      headerComponentParams: {
        dispose: { title: '折扣率(%)', key: 'discount' }
      },
      editable: true,
      hide: true,
      dispose: true
    },
    {
      headerName: '折扣额',
      width: 90,
      field: 'dsc',
      hide: true,
      dispose: true
    },
    {
      headerName: '金额',
      width: 120,
      field: 'total',
      hide: false,
      dispose: true
    },
    {
      headerName: '税率(%)',
      width: 90,
      field: 'tax',
      headerComponent: 'taxRate',
      headerComponentParams: {
        dispose: { title: '税率(%)', key: 'tax' }
      },
      editable: true,
      hide: false,
      dispose: true
    },
    {
      headerName: '税额',
      width: 90,
      field: 'tat',
      hide: false,
      dispose: true
    },
    {
      headerName: '价税合计',
      width: 120,
      field: 'tpt',
      hide: true,
      dispose: false
    },
    {
      headerName: '备注信息',
      width: 120,
      field: 'data',
      editable: true,
      hide: false,
      dispose: true
    },
    {
      headerName: '操作',
      width: 68,
      field: 'operation',
      cellRenderer: 'operationButton',
      pinned: 'right', // 固定在右侧
      hide: false,
      dispose: false,
      suppressSizeToFit: true, // 防止自动调整大小
      lockPosition: true // 锁定位置
    }
  ] as ExtendedColDef[],
  status: [
    { text: '总条数', type: 'count' },
    { text: '总合计', type: 'sum', key: 'tpt' }
  ]
}) as {
  options: ExtendedGridOptions
  context: any
  coldef: ExtendedColDef
  components: any
  getRowNodeId: (row: any) => string
  column: ExtendedColDef[]
  status: any[]
}

// 网格就绪事件
const onGridReady = (params: GridReadyEvent) => {
  grid.options.api = params.api
  // Note: columnApi 在 ag-Grid v31+ 已被弃用，所有功能都合并到 api 中

  // 优先级1: 检查是否需要接收传输数据（从其他页面跳转过来）
  if (route.query.dataTransfer === 'true' && params.api) {
    console.log('🔍 检测到数据传输请求，尝试从 Store 获取数据...')

    // 从 Pinia Store 获取传输数据
    const transferData = purchaseStore.getTransferData

    if (transferData) {
      console.log('📥 [PurchaseOrder] 接收到传输数据:', transferData)
      console.log('  - 来源:', transferData.from)
      console.log('  - 数据条数:', transferData.tableData?.length || 0)
      console.log('  - 时间戳:', transferData.timestamp)

      // 填充表头数据
      if (transferData.supplier) {
        headerForm.customer = transferData.supplier
        form.supplier = transferData.supplier
      }
      if (transferData.documentDate) {
        headerForm.documentDate = transferData.documentDate
      }
      if (transferData.documentNumber) {
        headerForm.documentNumber = transferData.documentNumber
      }

      // 填充表格数据
      if (transferData.tableData && Array.isArray(transferData.tableData)) {
        console.log('📋 开始填充表格数据，共', transferData.tableData.length, '条')

        // 先清空现有数据
        const allRows: any[] = []
        params.api.forEachNode((node: any) => {
          if (node.data) {
            allRows.push(node.data)
          }
        })
        if (allRows.length > 0) {
          console.log('🧹 清空现有数据，共', allRows.length, '条')
          params.api.applyTransaction({ remove: allRows })
        }

        // 添加新数据，确保每行有唯一ID
        const newRows = transferData.tableData.map((item: any) => ({
          ...item,
          uniqid: item.uniqid || getUniqid() // 保留原有ID或生成新ID
        }))

        params.api.applyTransaction({ add: newRows })

        // 重新计算网格数据
        handleGrid()

        const fromText = transferData.from === 'purchase-booking' ? '采购订单' : '其他页面'
        ElMessage.success(`✅ 已从${fromText}导入 ${newRows.length} 条数据`)

        // 接收完成后清空 Store 中的传输数据
        console.log('🧹 [Store] 清空传输数据')
        purchaseStore.clearTransferData()
        return // ✅ 只有在成功接收数据后才 return
      } else {
        console.warn('⚠️ 传输数据中没有表格数据')
        // 清空 Store
        console.log('🧹 [Store] 清空传输数据（无数据）')
        purchaseStore.clearTransferData()
        // ❌ 不 return，继续执行优先级2
      }
    } else {
      console.warn('⚠️ 检测到 dataTransfer=true 但没有传输数据，可能是从其他页面返回')
      console.warn('⚠️ 尝试从 Store 恢复之前保存的数据...')
      // ❌ 不 return，继续执行优先级2
    }
  }

  // 优先级2: 尝试从 Store 恢复之前保存的数据（返回本页面）
  const savedData = purchaseStore.getPurchaseOrderData
  if (savedData && params.api) {
    console.log('🔄 [PurchaseOrder] 检测到已保存的数据，恢复中...')
    console.log('  - 数据条数:', savedData.tableData?.length || 0)

    // 恢复表头数据
    if (savedData.headerForm.supplier) {
      headerForm.customer = savedData.headerForm.supplier
      form.supplier = savedData.headerForm.supplier
    }
    if (savedData.headerForm.documentDate) {
      headerForm.documentDate = savedData.headerForm.documentDate
    }
    if (savedData.headerForm.documentNumber) {
      headerForm.documentNumber = savedData.headerForm.documentNumber
    }

    // 恢复审核和核对状态
    isAudited.value = savedData.isAudited || false
    isChecked.value = savedData.isChecked || false

    // 恢复表格数据
    if (
      savedData.tableData &&
      Array.isArray(savedData.tableData) &&
      savedData.tableData.length > 0
    ) {
      // 清空现有数据
      const allRows: any[] = []
      params.api.forEachNode((node: any) => {
        if (node.data) {
          allRows.push(node.data)
        }
      })
      if (allRows.length > 0) {
        params.api.applyTransaction({ remove: allRows })
      }

      // 恢复数据
      const restoredRows = savedData.tableData.map((item: any) => ({
        ...item,
        uniqid: item.uniqid || getUniqid()
      }))

      params.api.applyTransaction({ add: restoredRows })

      // 重新计算网格数据
      handleGrid()

      console.log('✅ [PurchaseOrder] 已恢复 ' + restoredRows.length + ' 条数据')
      // 静默恢复数据，不显示提示信息
    } else {
      // 如果没有表格数据，添加一行空白
      addGridRow(params.api)
    }
    return
  }

  // 优先级3: 正常初始化：添加一行空白数据
  if (params.api) {
    console.log('🆕 [PurchaseOrder] 初始化空白表格')
    addGridRow(params.api)
  }
}

// 添加新行
const addGridRow = (api: GridApi) => {
  const newRow = {
    uniqid: getUniqid(),
    key: null,
    sequence: 0,
    name: '',
    number: '',
    spec: '',
    attr: '',
    unit: '',
    warehouse: '',
    warehouseId: null,
    price: 0,
    nums: 0,
    handle: 0,
    discount: 0,
    dsc: 0,
    total: 0,
    tax: 13, // 默认税率 13%
    tat: 0,
    tpt: 0,
    data: '',
    goodsType: 0
  }
  api.applyTransaction({ add: [newRow] })
}

// 处理表格数据变化
const handleGrid = () => {
  if (!grid.options.api) return

  const allRows: any[] = []
  grid.options.api.forEachNode((node: any) => {
    if (node.data) {
      allRows.push(node.data)
    }
  })

  // 计算每行的金额、税额、价税合计
  allRows.forEach((row) => {
    if (row.key != null) {
      // 金额 = 单价 * 数量
      const price = parseFloat(row.price) || 0
      const nums = parseFloat(row.nums) || 0
      row.total = price * nums

      // 折扣额 = 金额 * 折扣率
      const discount = parseFloat(row.discount) || 0
      row.dsc = row.total * (discount / 100)

      // 折后金额
      const discountedTotal = row.total - row.dsc

      // 税额 = 折后金额 * 税率
      const taxRate = parseFloat(row.tax) || 0
      row.tat = discountedTotal * (taxRate / 100)

      // 价税合计 = 折后金额 + 税额
      row.tpt = discountedTotal + row.tat
    }
  })

  // 刷新表格显示
  grid.options.api.refreshCells()

  // 同步数据到 Pinia store，用于底部表单的单据金额计算
  syncGridDataToStore(allRows)

  // 确保至少有一个空行用于输入
  const hasEmptyRow = allRows.some((row) => row.key == null)
  if (!hasEmptyRow) {
    addGridRow(grid.options.api)
  }
}

/**
 * 同步表格数据到 Pinia store
 * 用于实时更新底部表单的单据金额
 */
const syncGridDataToStore = (allRows: any[]) => {
  // 只同步有效的商品行（key 不为 null 的行）
  const validRows = allRows.filter((row) => row.key != null)
  purchaseStore.setItems(validRows)
  console.log('同步数据到 store，有效商品数:', validRows.length)
}

// 获取状态栏的值
const getStatusValue = (item: any) => {
  if (!grid.options.api) return 0

  const allRows: any[] = []
  grid.options.api.forEachNode((node: any) => {
    if (node.data && node.data.key != null) {
      allRows.push(node.data)
    }
  })

  if (item.type === 'count') {
    return allRows.length
  } else if (item.type === 'sum' && item.key) {
    return allRows
      .reduce((sum, row) => {
        return sum + (parseFloat(row[item.key]) || 0)
      }, 0)
      .toFixed(2)
  }
  return 0
}

// Setter 组件的点击事件
const onSetterClick = (cell: any) => {
  console.log('Setter clicked:', cell)
  // 这里可以打开商品选择对话框
  // 示例：打开商品列表弹窗，选择商品后填充到当前行
}

// Scheme 组件的切换事件（扫码模式切换）
const onSchemeChange = (isScanning: boolean) => {
  console.log('Scheme changed to:', isScanning ? '扫码模式' : '常规模式')
  // 这里可以切换输入模式
}

// QuickInput 组件的回车事件
const onQuickEnter = (value: string, rowIndex: number) => {
  console.log('Quick enter:', value, 'at row:', rowIndex)
  // 这里可以根据输入值（商品编码、条码等）查询商品信息并填充
}

/**
 * 处理打开商品选择弹窗
 * @param payload 包含行索引、行数据和 API 的对象
 */
const handleOpenProductPopup = (payload: any) => {
  console.log('打开商品选择弹窗:', payload)
  currentRowIndex.value = payload.rowIndex
  showProductPopup.value = true
}

/**
 * 处理商品选择弹窗关闭
 */
const handleProductPopupClose = () => {
  console.log('关闭商品选择弹窗')
  showProductPopup.value = false
}

/**
 * 处理商品选择确认
 * @param selectedProducts 选中的商品列表
 */
const handleProductConfirm = (selectedProducts: any[]) => {
  console.log('=== 开始处理商品确认 ===')
  console.log('选中的商品:', selectedProducts)
  console.log('当前行索引:', currentRowIndex.value)
  console.log('Grid API 状态:', !!grid.options.api)

  if (!grid.options.api || currentRowIndex.value === null) {
    console.warn('Grid API 未初始化或未选择行')
    return
  }

  // 获取当前行节点
  const rowNode = grid.options.api.getDisplayedRowAtIndex(currentRowIndex.value)
  console.log('当前行节点:', rowNode)
  console.log('当前行数据:', rowNode?.data)

  if (rowNode && selectedProducts.length > 0) {
    // 取第一个选中的商品填充当前行
    const firstProduct = selectedProducts[0]
    console.log('第一个商品数据:', firstProduct)

    // 更新当前行数据
    const updatedData = {
      ...rowNode.data,
      key: firstProduct.id,
      name: firstProduct.name,
      number: firstProduct.code,
      spec: firstProduct.model,
      attr: firstProduct.category,
      unit: firstProduct.unit,
      price: firstProduct.purchasePrice,
      price2: firstProduct.salePrice,
      barcode: firstProduct.barcode,
      nums: firstProduct.quantity || 1
    }
    console.log('更新后的行数据:', updatedData)
    rowNode.setData(updatedData)

    // 如果选择了多个商品，为其他商品添加新行
    for (let i = 1; i < selectedProducts.length; i++) {
      const product = selectedProducts[i]
      const newRow = {
        uniqid: getUniqid(),
        key: product.id,
        name: product.name,
        number: product.code,
        spec: product.model,
        attr: product.category,
        unit: product.unit,
        price: product.purchasePrice,
        price2: product.salePrice,
        barcode: product.barcode,
        nums: product.quantity || 1
      }

      // 在当前行后插入新行
      console.log(`添加第 ${i} 个商品:`, newRow)
      const result = grid.options.api.applyTransaction({
        add: [newRow],
        addIndex: currentRowIndex.value + i
      })
      console.log('添加结果:', result)
    }

    // 重新计算网格数据
    console.log('调用 handleGrid 重新计算')
    handleGrid()
    console.log('=== 商品确认处理完成 ===')
  }

  // 关闭弹窗并重置状态
  showProductPopup.value = false
  currentRowIndex.value = null
}

/**
 * 打开供应商选择弹窗
 */
function handleOpenSupplierPopup() {
  console.log('打开客户选择弹窗')
  ElMessage.info('客户选择功能开发中')
}

/**
 * 保存按钮处理函数
 */
async function handleSave() {
  console.log('保存数据')
  console.log('头部表单数据:', headerForm)

  try {
    // 获取当前表格数据
    const tableData: any[] = []
    if (grid.options.api) {
      grid.options.api.forEachNode((node: any) => {
        if (node.data && node.data.key != null) {
          tableData.push(node.data)
        }
      })
    }

    // 构建销售单数据
    const saleData: SaleDTO = {
      customer: form.supplier || headerForm.customer,
      time: headerForm.documentDate,
      number: headerForm.documentNumber,
      examine: false, // 未审核
      check: isChecked.value,
      items: tableData.map((item) => ({
        pid: '', // 所属销售单ID，新建时为空字符串
        goods: item.key,
        goodsName: item.name,
        goodsCode: item.number,
        spec: item.spec,
        attr: item.attr,
        unit: item.unit,
        warehouse: item.warehouse,
        warehouseId: item.warehouseId,
        price: item.price,
        nums: item.nums,
        discount: item.discount,
        dsc: item.dsc,
        total: item.total,
        tax: item.tax,
        tat: item.tat,
        tpt: item.tpt,
        data: item.data
      }))
    }

    // 调用保存API
    const response = await saleOrderApi.addSale(saleData)

    if (response.code === 10000) {
      // 保存成功后，设置为已审核状态
      isAudited.value = true

      // 保存到 Pinia store
      purchaseStore.savePurchaseOrderData({
        headerForm: {
          supplier: form.supplier || headerForm.customer,
          documentDate: headerForm.documentDate,
          documentNumber: headerForm.documentNumber
        },
        tableData: tableData,
        isAudited: true,
        isChecked: isChecked.value
      })

      ElMessage.success(`保存成功，已保存 ${tableData.length} 条数据`)
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败，请重试')
  }
}

/**
 * 刷新按钮处理函数
 */
function handleRefresh() {
  console.log('刷新数据')

  // 从 store 恢复数据
  const savedData = purchaseStore.getPurchaseOrderData

  if (savedData && grid.options.api) {
    // 恢复表头数据
    if (savedData.headerForm.supplier) {
      headerForm.customer = savedData.headerForm.supplier
      form.supplier = savedData.headerForm.supplier
    }
    if (savedData.headerForm.documentDate) {
      headerForm.documentDate = savedData.headerForm.documentDate
    }
    if (savedData.headerForm.documentNumber) {
      headerForm.documentNumber = savedData.headerForm.documentNumber
    }

    // 清空现有表格数据
    const rowsToRemove: any[] = []
    grid.options.api.forEachNode((node: any) => {
      if (node.data) {
        rowsToRemove.push(node.data)
      }
    })
    if (rowsToRemove.length > 0) {
      grid.options.api.applyTransaction({ remove: rowsToRemove })
    }

    // 恢复表格数据
    if (savedData.tableData && savedData.tableData.length > 0) {
      const restoredRows = savedData.tableData.map((item: any) => ({
        ...item,
        uniqid: item.uniqid || getUniqid()
      }))
      grid.options.api.applyTransaction({ add: restoredRows })

      // 重新计算网格数据
      handleGrid()

      ElMessage.success(`已恢复 ${restoredRows.length} 条数据`)
    } else {
      // 如果没有保存的数据，添加一行空白
      addGridRow(grid.options.api)
      ElMessage.success('已刷新为空白数据')
    }

    // 恢复审核和核对状态
    isAudited.value = savedData.isAudited || false
    isChecked.value = savedData.isChecked || false
  } else {
    // 如果没有保存的数据，清空表格并添加一行空白
    if (grid.options.api) {
      const rowsToRemove: any[] = []
      grid.options.api.forEachNode((node: any) => {
        if (node.data) {
          rowsToRemove.push(node.data)
        }
      })
      if (rowsToRemove.length > 0) {
        grid.options.api.applyTransaction({ remove: rowsToRemove })
      }
      addGridRow(grid.options.api)
    }
    ElMessage.success('数据已刷新')
  }
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
async function handleUnaudit() {
  console.log('反审核')
  try {
    // 这里需要获取销售单ID，暂时用模拟数据
    const saleId = '1' // 实际应该从数据中获取
    const response = await saleOrderApi.examineSales([saleId], false)

    if (response.code === 10000) {
      isAudited.value = false
      // 同时重置核对状态
      isChecked.value = false
      ElMessage.success('已反审核，恢复到未审核状态')
    } else {
      ElMessage.error(response.message || '反审核失败')
    }
  } catch (error) {
    console.error('反审核失败:', error)
    ElMessage.error('反审核失败，请重试')
  }
}

/**
 * 核对按钮处理函数
 */
async function handleCheck() {
  console.log('核对数据')
  try {
    // 这里需要获取销售单ID，暂时用模拟数据
    const saleId = '1' // 实际应该从数据中获取
    const response = await saleOrderApi.checkSales([saleId], true)

    if (response.code === 10000) {
      isChecked.value = true
      ElMessage.success('数据核对完成')
    } else {
      ElMessage.error(response.message || '核对失败')
    }
  } catch (error) {
    console.error('核对失败:', error)
    ElMessage.error('核对失败，请重试')
  }
}

/**
 * 反核对按钮处理函数
 */
async function handleUncheck() {
  console.log('反核对数据')
  try {
    const saleId = '1' // 实际应该从数据中获取
    const response = await saleOrderApi.checkSales([saleId], false)

    if (response.code === 10000) {
      isChecked.value = false
      ElMessage.success('已取消核对')
    } else {
      ElMessage.error(response.message || '反核对失败')
    }
  } catch (error) {
    console.error('反核对失败:', error)
    ElMessage.error('反核对失败，请重试')
  }
}

/**
 * 生成采购退货单处理函数
 */
function handleGeneratePurchaseReturn() {
  console.log('🚀 [handleGeneratePurchaseReturn] 生成销售退货单')

  // 先保存当前页面的完整数据到 Store（用于返回时恢复）
  const allTableData: any[] = []
  if (grid.options.api) {
    grid.options.api.forEachNode((node: any) => {
      if (node.data) {
        // 保存所有数据，包括空行
        allTableData.push(node.data)
      }
    })
  }

  purchaseStore.savePurchaseOrderData({
    headerForm: {
      supplier: form.supplier || headerForm.customer,
      documentDate: headerForm.documentDate,
      documentNumber: headerForm.documentNumber
    },
    tableData: allTableData,
    isAudited: isAudited.value,
    isChecked: isChecked.value
  })
  console.log('💾 已保存当前页面数据到 Store，共', allTableData.length, '条')

  // 获取有效的表格数据用于传输
  const validTableData = allTableData.filter((item) => item.key != null)
  console.log('📊 有效数据条数:', validTableData.length)

  // 关闭弹窗
  showGeneratePopup.value = false

  // 使用 Pinia Store 存储传输数据
  purchaseStore.setTransferData({
    tableData: validTableData,
    supplier: form.supplier || headerForm.customer,
    documentDate: headerForm.documentDate,
    documentNumber: headerForm.documentNumber,
    from: 'purchase-order'
  })

  // 跳转到采购退货单页面
  router.push({
    path: '/sales-return',
    query: {
      from: 'sales-order',
      dataTransfer: 'true'
    }
  })

  // 显示提示信息
  if (validTableData.length === 0) {
    ElMessage.info('正在跳转到销售退货单页面（无商品数据）')
  } else {
    ElMessage.success(`正在跳转到销售退货单页面，共 ${validTableData.length} 条数据`)
  }
}

// 在组件挂载后的操作
onMounted(() => {
  // 设置 context 的 componentParent 为当前实例
  if (instance) {
    grid.context.componentParent = {
      quickEnter: onQuickEnter,
      handleOpenProductPopup: handleOpenProductPopup
    }
  }

  // 初始化一些示例数据（可选）
  // 如果需要从后端加载数据，可以在这里调用 API
})
</script>
<style scoped>
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
.table-wrapper {
  height: 500px; /* 固定表格区域高度，可按你页面布局调整，比如 400~500px */
  overflow-y: auto; /* 超出时出现纵向滚动条 */
  border: 1px solid #ebeef5; /* 与 el-table 边框融合 */
  border-radius: 4px;
  background-color: white;
}

.page-container {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 0; /* 去掉间隔，让表格和表单紧密连接 */
}

.table-section {
  position: relative; /* 为审核标记提供定位上下文 */
  background-color: white;
  border-radius: 8px 8px 0 0; /* 只保留顶部圆角 */
  padding: 20px;
  padding-bottom: 10px; /* 减少底部内边距 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.grid-status {
  margin-top: 15px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
  display: flex;
  gap: 20px;
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

/* 客户选择框 */
.customer-select {
  width: 240px;
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

/* AG-Grid 主题样式 */
.ag-theme-alpine {
  --ag-header-height: 40px;
  --ag-row-height: 40px;
  --ag-header-background-color: #f5f7fa;
  --ag-odd-row-background-color: #fafbfc;
  --ag-border-color: #e4e7ed;
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
</style>
