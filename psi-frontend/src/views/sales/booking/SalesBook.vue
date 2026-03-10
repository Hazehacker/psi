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
          <!-- 未审核状态：显示保存和审核 -->
          <ButtonGroup v-if="!isAudited" align="right" gap="0px">
            <BusinessButton action-type="save" @action="handleSave" :loading="saving" />
            <el-button
              :loading="auditing"
              @click="handleAudit"
              type="primary"
              class="business-button"
            >
              审核
            </el-button>
            <BusinessButton action-type="refresh" @action="handleRefresh" />
            <el-button type="primary" @click="handleOpenReport"> 报表 </el-button>
          </ButtonGroup>

          <!-- 已审核状态：显示生成、反审核和刷新 -->
          <ButtonGroup v-else align="right" gap="0px">
            <BusinessButton action-type="generate" @action="handleGenerate" />
            <BusinessButton action-type="unaudit" @action="handleUnaudit" :loading="auditing" />
            <BusinessButton action-type="refresh" @action="handleRefresh" />
            <el-button type="primary" @click="handleOpenReport"> 报表 </el-button>
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

      <!-- 商品选择弹窗 -->
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
    <DocumentForm :fields="purchaseBookFields" />

    <!-- 生成单据弹窗 -->
    <el-dialog
      v-model="showGeneratePopup"
      title="生成单据"
      width="300px"
      :close-on-click-modal="false"
    >
      <div class="generate-options">
        <el-button class="generate-option-btn" @click="handleGenerateSaleOrder"> 销售单 </el-button>
        <el-button class="generate-option-btn" @click="handleGeneratePurchaseOrder">
          采购订单
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
import { Check, Refresh, Search, Setting, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { usePurchaseStore } from '@/stores/purchase'
import { nextTick } from 'vue'
import Warehouse from '@/components/goodSearchConpoent/Warehouse.vue'
import Customer from '@/components/goodSearchConpoent/Customer.vue'
import { ArrowDown } from '@element-plus/icons-vue'

// 导入API和类型
import salesBookApi, {
  type SaleOrderData,
  type SaleOrderQuery,
  type SaleOrderVerifyRequest,
  type SaleOrderDetailData,
  type GenerateSaleOrderData,
  type GeneratePurchaseOrderData
} from '@/apis/sales/booking'

// 定义 headerForm 的接口
interface HeaderForm {
  supplier: string
  customer: string
  documentDate: string
  documentNumber: string
}

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
  }
  showCustomerDialog.value = false
}

// 清除客户
function handleClearCustomer() {
  headerForm.customer = ''
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

// 总条数和总合计（状态栏数据）
const totalCount = computed(
  () => tableData.value.filter((r: any) => r.productName && r.quantity > 0).length
)

const totalSum = computed(() => {
  return tableData.value
    .filter((r) => r.productName)
    .reduce((sum, r) => sum + (Number(r.total) || 0), 0)
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

// ========== 后端接口集成 ==========

// 当前订单ID
const currentOrderId = ref<string>('')
// 加载状态
const saving = ref(false)
const auditing = ref(false)

// ① 先定义表格数据
const tableData = ref([
  {
    productName: '九九',
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
  console.log(`状态栏已更新：总条数=${totalCount}，总合计=${totalSum.toFixed(2)}`)
}

// ====== OperationPopTable -> 表格 的回填 ======
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

// 获取当前实例和路由
const instance = getCurrentInstance()
const router = useRouter()
const route = useRoute()

// 销售订单需要的字段
const purchaseBookFields = [
  'documentCost', // 单据金额
  'actualAmount', // 实际金额
  'relatedPerson', // 关联人员
  'arrivalDate', // 到货日期
  'logistics', // 物流信息
  'documentAttachment', // 单据附件
  'remarks' // 备注信息
]

// 头部表单数据 - 使用明确的类型定义
const headerForm = reactive<HeaderForm>({
  supplier: '',
  customer: '',
  documentDate: '2025-11-01',
  documentNumber: 'XSDD2511012119410'
})

// 初始化 Pinia store
const purchaseStore = usePurchaseStore()

// 商品弹窗控制
const showProductPopup = ref(false)
const currentRowIndex = ref<number | null>(null)

// 审核状态管理
const isAudited = ref(false)

// 生成弹窗控制
const showGeneratePopup = ref(false)

// 生成唯一ID的辅助函数
const getUniqid = () => {
  return Date.now().toString(36) + Math.random().toString(36).substring(2)
}

// ========== 后端接口实现 ==========

/**
 * 保存销售订单
 */
const saveSaleOrder = async () => {
  if (saving.value) return

  try {
    saving.value = true

    // 获取有效的商品数据
    const validTableData = tableData.value.filter(
      (row) =>
        row.productName && row.productName.trim() !== '' && row.quantity && Number(row.quantity) > 0
    )

    if (validTableData.length === 0) {
      ElMessage.warning('请添加至少一个商品')
      return
    }

    if (!headerForm.customer) {
      ElMessage.warning('请选择客户')
      return
    }

    if (!headerForm.documentDate) {
      ElMessage.warning('请选择单据日期')
      return
    }

    if (!headerForm.documentNumber) {
      ElMessage.warning('请输入单据编号')
      return
    }

    // 构建保存数据
    const saveData: SaleOrderData = {
      customer: headerForm.customer,
      time: `${headerForm.documentDate}T00:00:00`,
      number: headerForm.documentNumber,
      total: parseFloat(totalSum.value) || 0,
      actual: parseFloat(totalSum.value) || 0,
      examine: isAudited.value ? 1 : 0,
      state: 0, // 未出库
      user: '当前用户', // 需要从用户信息获取
      people: '', // 关联人员
      logistics: '', // 物流信息
      data: '', // 备注信息
      frame: '默认组织', // 所属组织
      arrival: '' // 到货日期
    }

    console.log('📦 保存数据:', saveData)
    console.log('📊 商品数据条数:', validTableData.length)

    // 如果有ID则是修改，否则是新增
    let response
    if (currentOrderId.value) {
      saveData.id = currentOrderId.value
      response = await salesBookApi.updateSaleOrder(saveData)
    } else {
      response = await salesBookApi.addSaleOrder(saveData)
    }

    if (response.code === 10000) {
      // 保存成功，记录订单ID
      if (!currentOrderId.value) {
        currentOrderId.value = saveData.id || ''
      }

      // 保存到 Pinia store
      purchaseStore.savePurchaseBookData({
        headerForm: {
          supplier: headerForm.supplier,
          customer: headerForm.customer,
          documentDate: headerForm.documentDate,
          documentNumber: headerForm.documentNumber
        },
        tableData: tableData.value,
        isAudited: isAudited.value
      })

      ElMessage.success(`✅ 保存成功，共 ${validTableData.length} 条商品数据`)
      console.log('✅ 保存成功，订单ID:', currentOrderId.value)
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    console.error('❌ 保存失败:', error)
    ElMessage.error('保存失败，请检查网络连接')
  } finally {
    saving.value = false
  }
}

/**
 * 审核销售订单
 */
const auditSaleOrder = async () => {
  if (auditing.value) return

  if (!currentOrderId.value) {
    ElMessage.warning('请先保存订单再进行审核')
    return
  }

  try {
    auditing.value = true

    const request: SaleOrderVerifyRequest = {
      ids: [currentOrderId.value],
      num: 1 // 审核
    }

    const response = await salesBookApi.verifySaleOrders(request)
    if (response.code === 10000) {
      isAudited.value = true
      ElMessage.success('✅ 审核成功')
      console.log('✅ 审核成功')
    } else {
      ElMessage.error(response.message || '审核失败')
    }
  } catch (error) {
    console.error('❌ 审核失败:', error)
    ElMessage.error('审核失败')
  } finally {
    auditing.value = false
  }
}

/**
 * 反审核销售订单
 */
const unauditSaleOrder = async () => {
  if (auditing.value) return

  if (!currentOrderId.value) {
    ElMessage.warning('订单ID不存在')
    return
  }

  try {
    auditing.value = true

    const request: SaleOrderVerifyRequest = {
      ids: [currentOrderId.value],
      num: 0 // 反审核
    }

    const response = await salesBookApi.verifySaleOrders(request)
    if (response.code === 10000) {
      isAudited.value = false
      ElMessage.success('✅ 反审核成功')
      console.log('✅ 反审核成功')
    } else {
      ElMessage.error(response.message || '反审核失败')
    }
  } catch (error) {
    console.error('❌ 反审核失败:', error)
    ElMessage.error('反审核失败')
  } finally {
    auditing.value = false
  }
}

/**
 * 删除销售订单
 */
const deleteSaleOrder = async () => {
  if (!currentOrderId.value) {
    ElMessage.warning('没有可删除的订单')
    return
  }

  try {
    await ElMessageBox.confirm('确定删除当前销售订单吗？', '提示', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消'
    })

    const response = await salesBookApi.deleteSaleOrders([currentOrderId.value])
    if (response.code === 10000) {
      ElMessage.success('✅ 删除成功')
      // 重置表单
      resetForm()
      console.log('✅ 删除成功')
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('❌ 删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

/**
 * 获取生成销售单数据
 */
const getGenerateSaleOrderData = async () => {
  if (!currentOrderId.value) {
    ElMessage.warning('请先保存订单')
    return null
  }

  try {
    const response = await salesBookApi.getGenerateSaleOrderData(currentOrderId.value)
    if (response.code === 10000) {
      return response.data
    } else {
      ElMessage.error(response.message || '获取生成数据失败')
      return null
    }
  } catch (error) {
    console.error('❌ 获取生成销售单数据失败:', error)
    ElMessage.error('获取生成数据失败')
    return null
  }
}

/**
 * 获取生成采购订单数据
 */
const getGeneratePurchaseOrderData = async () => {
  if (!currentOrderId.value) {
    ElMessage.warning('请先保存订单')
    return null
  }

  try {
    const response = await salesBookApi.getGeneratePurchaseOrderData(currentOrderId.value)
    if (response.code === 10000) {
      return response.data
    } else {
      ElMessage.error(response.message || '获取生成数据失败')
      return null
    }
  } catch (error) {
    console.error('❌ 获取生成采购订单数据失败:', error)
    ElMessage.error('获取生成数据失败')
    return null
  }
}

// ========== 按钮处理函数 ==========

/**
 * 保存按钮处理函数
 */
function handleSave() {
  saveSaleOrder()
}

/**
 * 审核按钮处理函数
 */
function handleAudit() {
  auditSaleOrder()
}

/**
 * 反审核按钮处理函数
 */
function handleUnaudit() {
  unauditSaleOrder()
}

/**
 * 刷新按钮处理函数
 */
function handleRefresh() {
  // 重置表单
  resetForm()
  ElMessage.success('已刷新')
}

/**
 * 重置表单
 */
const resetForm = () => {
  headerForm.customer = ''
  headerForm.documentDate = '2025-11-01' // 默认日期
  headerForm.documentNumber = `XSDD${new Date().getTime()}`
  tableData.value = [createEmptyRow()]
  currentOrderId.value = ''
  isAudited.value = false
  rowPopoverVisible.value = [false]
}

/**
 * 创建空行
 */
const createEmptyRow = () => ({
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

/**
 * 生成销售单
 */
const handleGenerateSaleOrder = async () => {
  const generateData = await getGenerateSaleOrderData()
  if (generateData) {
    // 使用 Pinia Store 存储传输数据
    const transferData = {
      tableData: tableData.value.filter((row) => row.productName),
      customer: headerForm.customer,
      documentDate: headerForm.documentDate,
      documentNumber: headerForm.documentNumber,
      from: 'sales-booking',
      generateData: generateData
    }

    purchaseStore.setTransferData(transferData)

    // 跳转到销售单页面
    router.push({
      path: '/sales-order',
      query: {
        from: 'sales-booking',
        dataTransfer: 'true'
      }
    })

    ElMessage.success('正在跳转到销售单页面')
  }
}

/**
 * 生成采购订单
 */
const handleGeneratePurchaseOrder = async () => {
  const generateData = await getGeneratePurchaseOrderData()
  if (generateData) {
    // 使用 Pinia Store 存储传输数据
    const transferData = {
      tableData: tableData.value.filter((row) => row.productName),
      supplier: headerForm.customer, // 注意：采购订单用 supplier
      documentDate: headerForm.documentDate,
      documentNumber: headerForm.documentNumber,
      from: 'sales-booking',
      generateData: generateData
    }

    purchaseStore.setTransferData(transferData)

    // 跳转到采购订单页面
    router.push({
      path: '/purchase-booking',
      query: {
        from: 'sales-booking',
        dataTransfer: 'true'
      }
    })

    ElMessage.success('正在跳转到采购订单页面')
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
 * 打开报表页面
 */
function handleOpenReport() {
  console.log('打开销售订单报表')
  router.push({
    path: '/sales-booking-report'
  })
}

// 在组件挂载后的操作
onMounted(() => {
  // 如果有路由参数，加载订单数据
  if (route.query.orderId) {
    loadOrderData(route.query.orderId as string)
  }
})

/**
 * 加载订单数据（用于编辑现有订单）
 */
const loadOrderData = async (orderId: string) => {
  try {
    // 加载订单基本信息
    const query: SaleOrderQuery = {
      pageIndex: 1,
      pageSize: 1,
      id: orderId
    }

    const listResponse = await salesBookApi.getSaleOrdersList(query)
    if (
      listResponse.code === 10000 &&
      listResponse.data?.rows &&
      listResponse.data.rows.length > 0
    ) {
      const order = listResponse.data.rows[0]
      currentOrderId.value = order.id || ''

      // 填充表头数据
      headerForm.customer = order.customer || ''
      headerForm.documentDate = order.time ? order.time.split('T')[0] : ''
      headerForm.documentNumber = order.number || ''

      // 设置审核状态
      isAudited.value = order.examine === 1

      // 加载订单详情
      const detailResponse = await salesBookApi.getSaleOrderInfo({
        number: order.number
      })

      if (detailResponse.code === 10000 && detailResponse.data) {
        // 处理详情数据，填充到表格
        processOrderDetails(detailResponse.data)
      }

      ElMessage.success('✅ 订单数据加载成功')
    }
  } catch (error) {
    console.error('❌ 加载订单数据失败:', error)
    ElMessage.error('加载订单数据失败')
  }
}

/**
 * 处理订单详情数据
 */
const processOrderDetails = (detailData: any) => {
  // 根据后端返回的数据结构处理详情数据
  // 这里需要根据实际数据结构进行调整
  if (Array.isArray(detailData) && detailData.length > 0) {
    const details = detailData[0] // 取第一个数组
    tableData.value = details.map((item: any) => ({
      productName: item.goods || '',
      productCode: item.code || '',
      spec: item.spec || '',
      attr: item.attr || '',
      unit: item.unit || '',
      warehouse: item.warehouse || '',
      price: item.price || 0,
      quantity: item.nums || 0,
      amount: item.total || 0,
      taxRate: item.tax || 0,
      taxAmount: item.tat || 0,
      total: item.tpt || 0,
      remark: item.data || ''
    }))

    // 确保最后有一个空行
    if (tableData.value.length > 0 && tableData.value[tableData.value.length - 1].productName) {
      tableData.value.push(createEmptyRow())
    }

    // 同步更新弹层可见性数组
    rowPopoverVisible.value = tableData.value.map(() => false)
  }
}
</script>

<style scoped>
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

.page-container {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 0; /* 去掉间隔，让表格和表单紧密连接 */
}

.table-section {
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
