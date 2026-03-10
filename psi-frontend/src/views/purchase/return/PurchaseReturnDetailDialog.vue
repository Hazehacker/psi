<template>
  <el-dialog
    v-model="innerVisible"
    fullscreen
    :close-on-click-modal="false"
    class="purchase-return-detail-dialog"
    title="单据详情"
  >
    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <div class="left">
        <!-- 供应商 -->
        <div class="form-item">
          <label class="form-label">供应商</label>
          <el-input
            v-model="supplierModel.name"
            placeholder="请选择供应商"
            readonly
            clearable
            class="supplier-input"
            style="cursor: pointer"
            @focus="showSupplierDialog = true"
            @clear="handleClearSupplier"
          >
            <template #suffix>
              <el-icon @click.stop="showSupplierDialog = true" style="cursor: pointer">
                <ArrowDown />
              </el-icon>
            </template>
          </el-input>

          <!-- 供应商搜索对话框 -->
          <el-dialog
            v-model="showSupplierDialog"
            title="供应商搜索"
            width="720px"
            append-to-body
            :close-on-click-modal="false"
            :modal="true"
            destroy-on-close
          >
            <div @click.stop @mousedown.stop @mouseup.stop>
              <Supplier @search="handleSupplierSearch" />
            </div>
            <template #footer>
              <div @click.stop @mousedown.stop @mouseup.stop>
                <el-button @click="showSupplierDialog = false">关闭</el-button>
              </div>
            </template>
          </el-dialog>
        </div>

        <!-- 单据日期 -->
        <div class="form-item">
          <label class="form-label">单据日期</label>
          <el-date-picker
            v-model="header.documentDate"
            type="date"
            placeholder="2025-10-26"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            class="date-input"
          />
        </div>

        <!-- 单据编号 -->
        <div class="form-item">
          <label class="form-label">单据编号</label>
          <el-input
            v-model="header.documentNumber"
            placeholder="CGDD2510262244385"
            clearable
            class="number-input"
          />
        </div>
      </div>

      <div class="right">
        <!-- 审核通过时：显示 反审核 -->
        <template v-if="isApproved">
          <el-button class="ml8" @click="toggleApprove">反审核</el-button>
        </template>

        <!-- 未审核时：显示 保存 + 审核 -->
        <template v-else>
          <el-button type="primary" @click="handleSave">保存</el-button>
          <el-button class="ml8" @click="toggleApprove">审核</el-button>
        </template>
        <!-- 无论是否审核，都显示这个"核对 / 反核对" -->
        <el-button class="ml8" @click="toggleCheck">
          {{ isChecked ? '反核对' : '核对' }}
        </el-button>
      </div>
    </div>

    <!-- 分割线 -->
    <div class="divider"></div>

    <!-- 主体内容 -->
    <div class="main">
      <!-- 表格部分 -->
      <div class="table-wrap">
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

            <!-- 单行编辑 -->
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

        <!-- 已审核印章 -->
        <transition name="fade-zoom">
          <div v-if="isApproved" class="approved-stamp">已审核</div>
        </transition>

        <!-- 状态栏 -->
        <div class="grid-status">
          <span class="status-item">总条数: {{ totalCount }}</span>
          <span class="status-item">总合计: {{ totalSum }}</span>
        </div>
      </div>

      <div class="divider"></div>

      <!-- 底部表单 -->
      <DocumentForm :fields="purchaseBookFields" />
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="innerVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch, onMounted, nextTick } from 'vue'
import Supplier from '@/components/goodSearchConpoent/Supplier.vue'
import OperationPopTable from '@/components/operationpoptable/OperationPopTable.vue'
import { ArrowDown, Setting, Edit, Delete, Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import DocumentForm from '@/components/documentform/DocumentForm.vue'
import { useRouter } from 'vue-router'
import Warehouse from '@/components/goodSearchConpoent/Warehouse.vue'

interface Props {
  visible: boolean
  record: any
}

interface Emits {
  (e: 'update:visible', value: boolean): void
  (e: 'save', data: any): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const router = useRouter()

const innerVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
})

/* 供应商组件 */
const supplierModel = reactive({
  name: ''
})
const header = reactive({
  documentDate: '',
  documentNumber: ''
})

// 供应商选择对话框
const showSupplierDialog = ref(false)

// 处理供应商搜索
function handleSupplierSearch(params: any) {
  if (params && typeof params === 'object') {
    const name = params.name || params.supplierName || ''
    supplierModel.name = name || ''
  }
  showSupplierDialog.value = false
}

// 清除供应商
function handleClearSupplier() {
  supplierModel.name = ''
}

/* 审核状态 */
const isApproved = ref(true)
const toggleApprove = () => {
  isApproved.value = !isApproved.value
}

/* 核对状态 */
const isChecked = ref(false)
const toggleCheck = () => {
  isChecked.value = !isChecked.value
  ElMessage.success(isChecked.value ? '核对成功' : '反核对成功')
}

const handleSave = () => {
  emit('save', {
    header,
    supplier: { name: supplierModel.name },
    tableData: tableData.value,
    footer,
    isApproved: isApproved.value
  })
}

// 表格数据
const tableData = ref([
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

// 根据表格的行数，初始化弹层可见性数组
const rowPopoverVisible = ref<boolean[]>(tableData.value.map(() => false))

// 当前选中的行索引（仓库选择）
const currentWarehouseIndex = ref<number | null>(null)
// 批量弹层控制
const batchWarehouseVisible = ref(false)

// 商品弹窗控制
const showProductPopup = ref(false)
const currentRowIndex = ref<number | null>(null)

// 列显隐
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
  amount: '金额',
  taxRate: '税率(%)',
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

// 常规/扫码开关
const scanMode = ref(false)

// 批量设置税率
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

// 金额/税额/合计计算
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
  // 重新计算总条数和总合计（通过 computed 自动更新）
  const validRows = tableData.value.filter((r) => r.productName)
  const totalCount = validRows.length
  const totalSum = validRows.reduce((sum, r) => sum + (Number(r.total) || 0), 0)

  console.log(`状态栏已更新：总条数=${totalCount}，总合计=${totalSum.toFixed(2)}`)
}

// 总条数和总合计
const totalCount = computed(
  () => tableData.value.filter((r: any) => r.productName && r.quantity > 0).length
)

const totalSum = computed(() => {
  const sum = tableData.value
    .filter((r) => r.productName)
    .reduce((acc, r) => acc + (Number(r.total) || 0), 0)
  return sum.toFixed(2)
})

// 底部表单字段
const purchaseBookFields = [
  'documentCost', // 单据金额
  'actualAmount', // 实际金额
  'receivedAmount', //实收金额
  'documentFee', // 单据费用
  'settlementAccount', // 结算账户
  'relatedPerson', // 关联人员
  'logistics', // 物流信息
  'documentAttachment', // 单据附件
  'remarks' // 备注信息
]

/* 底部表单数据 */
const footer = reactive({
  documentCost: '',
  actualAmount: '',
  receivedAmount: '',
  documentFee: '',
  settlementAccount: '',
  relatedPerson: '',
  logistics: '',
  documentAttachment: '',
  remarks: ''
})

// 仓库选择
function handleWarehouseConfirm(selected: any) {
  if (!selected || currentWarehouseIndex.value === null) return
  const row = tableData.value[currentWarehouseIndex.value]
  row.warehouse = selected.name || selected.warehouseName || ''
  rowPopoverVisible.value[currentWarehouseIndex.value] = false
  ElMessage.success(`已选择仓库：${row.warehouse}`)
}

function handleBatchWarehouseSelect(selected: any) {
  if (!selected) return
  const selectedName = selected.name || selected.warehouseName || ''
  tableData.value.forEach((r) => {
    if (r.productName) r.warehouse = selectedName
  })
  batchWarehouseVisible.value = false
  ElMessage.success(`已批量设置仓库：${selectedName}`)
}

// 商品选择
function openProductPopup(index: number) {
  currentRowIndex.value = index
  showProductPopup.value = true
}

function handleTableProductConfirm(selected: any[]) {
  if (!selected || selected.length === 0 || currentRowIndex.value === null) {
    showProductPopup.value = false
    return
  }
  const first = selected[0]
  const row = tableData.value[currentRowIndex.value]

  row.productName = first.name
  row.productCode = first.code
  row.spec = first.model || ''
  row.attr = first.category || ''
  row.unit = first.unit || ''
  row.warehouse = row.warehouse || ''
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
    rowPopoverVisible.value.push(false)
  }
}

// 删除行
function deleteRow(index: number) {
  tableData.value.splice(index, 1)
  rowPopoverVisible.value.splice(index, 1)
  ElMessage.success('已删除该行')
}

// 可编辑单元格
type EditableField = 'price' | 'quantity' | 'taxRate' | 'remark'

function getCaretPosition(el: HTMLElement): number {
  const sel = window.getSelection()
  if (!sel || sel.rangeCount === 0) return 0
  const range = sel.getRangeAt(0)
  const preRange = range.cloneRange()
  preRange.selectNodeContents(el)
  preRange.setEnd(range.endContainer, range.endOffset)
  return preRange.toString().length
}

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

function onEditableInput(row: any, field: EditableField, e: Event) {
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

const formatNumber = (n: number | undefined | null) => {
  const num = Number(n ?? 0)
  return Number.isFinite(num) ? num.toLocaleString() : '0'
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

/* ===== 弹窗整体布局 ===== */
.purchase-return-detail-dialog :deep(.el-dialog__body) {
  display: flex;
  flex-direction: column;
  height: 100vh; /* 全屏高度 */
  padding: 0 16px;
  box-sizing: border-box;
}

/* 顶部工具栏固定 */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  flex-shrink: 0;
  border-bottom: 1px solid #ebeef5;
}
.toolbar .left {
  display: flex;
  align-items: flex-start;
  gap: 16px;
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

/* 供应商输入框 */
.supplier-input {
  width: 240px;
}

.supplier-input :deep(.el-input__inner) {
  cursor: pointer;
}

/* 单据日期 */
.date-input {
  width: 160px;
}

/* 单据编号 */
.number-input {
  width: 180px;
}

.ml8 {
  margin-left: 8px;
}

/* 分割线 */
.divider {
  border-top: 1px solid #ebeef5;
}

/* 主体部分（表格 + 底部） */
.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: #fff;
}

/* 表格区域（自动占满剩余空间） */
.table-wrap {
  flex: 1 1 auto;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border-bottom: 1px solid #ebeef5;
  position: relative;
}
/* el-table 区域允许滚动 */
.table-wrap :deep(.el-table) {
  flex: 1;
  overflow-y: auto;
  min-height: 300px;
}

/* 状态栏固定在表格底部 */
.grid-status {
  flex-shrink: 0;
  border-top: 1px solid #ebeef5;
  background: #fafafa;
  text-align: left;
  padding: 6px 16px;
  font-size: 13px;
  color: #606266;
}

/* 底部表单固定底部 */
:deep(.document-form) {
  flex-shrink: 0;
  border-top: 1px solid #ebeef5;
  padding: 12px 16px;
  background: #f9fafb;
}

/* 审核印章位置随全屏调整 */
.approved-stamp {
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

/* 动画 */
.fade-zoom-enter-active,
.fade-zoom-leave-active {
  transition: all 0.3s ease;
}
.fade-zoom-enter-from,
.fade-zoom-leave-to {
  transform: scale(0.8);
  opacity: 0;
}

/* 底部按钮 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  padding: 10px 0;
  gap: 10px;
}
</style>
