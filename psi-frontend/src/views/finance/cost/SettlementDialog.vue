<template>
  <el-dialog
    v-model="innerVisible"
    fullscreen
    :close-on-click-modal="false"
    class="sb-detail-dialog"
    title="结算"
  >
    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <div class="left">
        <!-- 供应商选择 -->
        <div class="form-item">
          <label class="form-label">供应商</label>
          <el-input
            v-model="formData.supplier"
            placeholder="请选择供应商"
            readonly
            clearable
            style="cursor: pointer; width: 240px"
            @focus="handleSupplierInputFocus"
            @clear="handleSupplierClear"
          >
            <template #suffix>
              <el-icon @click.stop="handleSupplierInputFocus" style="cursor: pointer">
                <Search />
              </el-icon>
            </template>
          </el-input>
        </div>

        <!-- 单据日期 -->
        <div class="form-item">
          <label class="form-label">单据日期</label>
          <el-date-picker
            v-model="header.documentDate"
            type="date"
            placeholder="选择日期"
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
            placeholder="自动生成"
            clearable
            class="number-input"
          />
        </div>
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
              <el-button v-if="scope.$index === 0" type="text" size="small" @click="addNewRow">
                <el-icon><Plus /></el-icon>
              </el-button>
              <el-button v-else type="text" size="small" @click="deleteRow(scope.$index)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </template>
          </el-table-column>

          <!-- 支出类别 -->
          <el-table-column
            v-if="visibleColumns.expenseType"
            label="支出类别"
            align="center"
            width="150"
          >
            <template #default="scope">
              <el-select
                v-model="scope.row.expenseType"
                placeholder="点击选择"
                clearable
                @change="handleExpenseTypeChange(scope.row)"
              >
                <el-option label="采购支出" value="采购支出" />
                <el-option label="人力成本" value="人力成本" />
                <el-option label="租金支出" value="租金支出" />
                <el-option label="水电物业费" value="水电物业费" />
                <el-option label="运输物流费" value="运输物流费" />
              </el-select>
            </template>
          </el-table-column>

          <!-- 结算金额 -->
          <el-table-column
            v-if="visibleColumns.settlementAmount"
            label="结算金额"
            align="center"
            width="150"
          >
            <template #default="{ row }">
              <div
                contenteditable="true"
                class="editable-cell"
                @input="onEditableInput(row, 'settlementAmount', $event)"
                @blur="onEditableBlur(row, 'settlementAmount', $event)"
              >
                {{ row.settlementAmount || '' }}
              </div>
            </template>
          </el-table-column>

          <!-- 备注信息 -->
          <el-table-column v-if="visibleColumns.remark" label="备注信息" align="center">
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

        <!-- 列显隐弹窗 -->
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

        <!-- 状态栏 -->
        <div class="grid-status">
          <span class="status-item">总条数: {{ totalCount }}</span>
          <span class="status-item">总金额: {{ totalSum }}</span>
        </div>
      </div>

      <div class="divider"></div>

      <!-- 底部表单 -->
      <div class="document-form-container">
        <el-form
          :model="documentFormData"
          :rules="formRules"
          ref="documentFormRef"
          label-width="120px"
          inline
          class="bottom-form"
        >
          <!-- 单据金额 -->
          <el-form-item label="单据金额" prop="documentCost">
            <el-input
              v-model="documentFormData.documentCost"
              placeholder="单据金额"
              style="width: 200px"
              readonly
            />
          </el-form-item>

          <!-- 实际金额 -->
          <el-form-item label="实际金额" prop="actualAmount">
            <el-input
              v-model="documentFormData.actualAmount"
              placeholder="请输入实际金额"
              style="width: 200px"
            />
          </el-form-item>

          <!-- 实付金额 -->
          <el-form-item label="实付金额" prop="paidAmount">
            <el-input
              v-model="documentFormData.paidAmount"
              placeholder="请输入实付金额"
              style="width: 200px"
            />
          </el-form-item>

          <!-- 结算账户 -->
          <el-form-item label="结算账户" prop="settlementAccount">
            <el-input
              v-model="documentFormData.settlementAccount"
              placeholder="请选择结算账户"
              readonly
              clearable
              style="cursor: pointer; width: 200px"
              @focus="handleAccountInputFocus"
              @clear="handleAccountClear"
            >
              <template #suffix>
                <el-icon @click.stop="handleAccountInputFocus" style="cursor: pointer">
                  <Search />
                </el-icon>
              </template>
            </el-input>
          </el-form-item>

          <!-- 关联人员 -->
          <el-form-item label="关联人员" prop="relatedPerson">
            <el-input
              v-model="documentFormData.relatedPerson"
              placeholder="请选择关联人员"
              readonly
              clearable
              style="cursor: pointer; width: 200px"
              @focus="handleRelatedPersonInputFocus"
              @clear="handleRelatedPersonClear"
            >
              <template #suffix>
                <el-icon @click.stop="handleRelatedPersonInputFocus" style="cursor: pointer">
                  <Search />
                </el-icon>
              </template>
            </el-input>
          </el-form-item>

          <!-- 单据附件 -->
          <el-form-item label="单据附件">
            <el-upload
              class="upload-demo"
              action="/api/upload"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :before-remove="beforeRemove"
              :limit="3"
              :on-exceed="handleExceed"
              :file-list="fileList"
            >
              <el-button type="primary">点击上传</el-button>
              <template #tip>
                <div class="el-upload__tip">最多上传3个文件，且单个文件不超过5MB</div>
              </template>
            </el-upload>
          </el-form-item>

          <!-- 备注信息 -->
          <el-form-item label="备注信息">
            <el-input
              v-model="documentFormData.remarks"
              placeholder="备注信息"
              style="width: 200px"
              type="textarea"
              :rows="2"
            />
          </el-form-item>
        </el-form>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="innerVisible = false">关闭</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </div>
    </template>

    <!-- 供应商搜索弹窗 -->
    <el-dialog
      v-model="showSupplierDialog"
      title="供应商搜索"
      width="720px"
      append-to-body
      :close-on-click-modal="false"
    >
      <Supplier @search="handleSupplierDialogSearch" />
      <template #footer>
        <el-button @click="closeSupplierDialog">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 关联人员搜索弹窗 -->
    <el-dialog
      v-model="showRelatedPersonDialog"
      title="关联人员搜索"
      width="720px"
      append-to-body
      :close-on-click-modal="false"
    >
      <PeopleList @search="handleRelatedPersonDialogSearch" />
      <template #footer>
        <el-button @click="closeRelatedPersonDialog">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 结算账户搜索弹窗 -->
    <el-dialog
      v-model="showAccountDialog"
      title="资金账户搜索"
      width="720px"
      append-to-body
      :close-on-click-modal="false"
    >
      <Account @search="handleAccountDialogSearch" />
      <template #footer>
        <el-button @click="closeAccountDialog">关闭</el-button>
      </template>
    </el-dialog>
  </el-dialog>
</template>

<script setup lang="ts">
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { ref, reactive, computed, watch, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Setting, Delete, Plus } from '@element-plus/icons-vue'

// 导入搜索组件
import Supplier from '@/components/goodSearchConpoent/Supplier.vue'
import Account from '@/components/goodSearchConpoent/Account.vue'
import PeopleList from '@/components/goodSearchConpoent/PeopleList.vue'

// 定义接口类型
interface OtherExpenseTableRow {
  expenseType: string
  settlementAmount: string
  remark: string
  costId?: string
}

interface DocumentFormFields {
  documentCost: string
  actualAmount: string
  paidAmount: string
  settlementAccount: string
  relatedPerson: string
  documentAttachment: string
  remarks: string
}

// 选中的记录类型（来自表格行）
interface SelectedRecord {
  id?: string
  documentType?: string
  orderType?: string
  organization?: string
  orgName?: string
  partnerUnit?: string
  contactUnit?: string
  documentDate?: string
  orderDate?: string
  documentNumber?: string
  orderNo?: string
  expenseType?: string
  settlementStatus?: string
  amount?: number
  settledAmount?: number
  unsettledAmount?: number
  settlementAmount?: number
}

// 结算数据类型（来自后端API）
interface SettlementDataItem {
  id?: string
  money?: number
  type?: string
  data?: string
}

/* props & emits */
interface Props {
  visible: boolean
  selectedRecords?: SelectedRecord[]
  settlementData?: SettlementDataItem[]
}
const props = defineProps<Props>()
const emit = defineEmits(['update:visible', 'save', 'settlement-success'])

/* v-model:visible */
const innerVisible = computed({
  get: () => props.visible,
  set: (v) => emit('update:visible', v)
})

/* 头部表单数据 */
const header = reactive({
  documentDate: new Date().toISOString().split('T')[0],
  documentNumber: `SE${new Date().getTime().toString().slice(-8)}`
})

/* 供应商相关 */
const formData = reactive({
  supplier: ''
})

/* 表格数据 */
const tableData = ref<OtherExpenseTableRow[]>([
  {
    expenseType: '',
    settlementAmount: '',
    remark: ''
  }
])

/* 底部表单数据 */
const documentFormData = reactive<DocumentFormFields>({
  documentCost: '',
  actualAmount: '',
  paidAmount: '',
  settlementAccount: '',
  relatedPerson: '',
  documentAttachment: '',
  remarks: ''
})

// 表单引用
const documentFormRef = ref()

// 表单验证规则
const formRules = {
  actualAmount: [
    { required: true, message: '请输入实际金额', trigger: 'blur' },
    { pattern: /^\d+(\.\d{1,2})?$/, message: '请输入正确的金额格式', trigger: 'blur' }
  ],
  paidAmount: [
    { required: true, message: '请输入实付金额', trigger: 'blur' },
    { pattern: /^\d+(\.\d{1,2})?$/, message: '请输入正确的金额格式', trigger: 'blur' }
  ],
  settlementAccount: [{ required: true, message: '请选择结算账户', trigger: 'change' }],
  relatedPerson: [{ required: true, message: '请选择关联人员', trigger: 'change' }]
}

// 文件上传相关
const fileList = ref([])

const handleRemove = (file: any, uploadFiles: any) => {
  console.log(file, uploadFiles)
}

const handlePreview = (uploadFile: any) => {
  console.log(uploadFile)
}

const handleExceed = (files: any, uploadFiles: any) => {
  ElMessage.warning(
    `当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + uploadFiles.length} 个文件`
  )
}

// eslint-disable-next-line @typescript-eslint/no-unused-vars
const beforeRemove = (uploadFile: any, uploadFiles: any) => {
  return ElMessageBox.confirm(`确定移除 ${uploadFile.name}？`).then(
    () => true,
    () => false
  )
}

// ====== 弹窗控制 ======
const showSupplierDialog = ref(false)
const showRelatedPersonDialog = ref(false)
const showAccountDialog = ref(false)
const isClosingDialog = ref(false)

/** 处理供应商输入框获得焦点 */
function handleSupplierInputFocus() {
  if (isClosingDialog.value) return
  showSupplierDialog.value = true
}

/** 关闭供应商弹窗 */
function closeSupplierDialog() {
  isClosingDialog.value = true
  showSupplierDialog.value = false
  setTimeout(() => {
    isClosingDialog.value = false
  }, 300)
}

/** 处理供应商清空 */
function handleSupplierClear() {
  formData.supplier = ''
}

/** 处理供应商弹窗内的搜索 */
function handleSupplierDialogSearch(params: any) {
  if (params && typeof params === 'object') {
    const name = params.name || params.supplierName || ''
    formData.supplier = name
    ElMessage.success(`已选择供应商：${name}`)
  }
  closeSupplierDialog()
}

/** 处理关联人员输入框获得焦点 */
function handleRelatedPersonInputFocus() {
  if (isClosingDialog.value) return
  showRelatedPersonDialog.value = true
}

/** 关闭关联人员弹窗 */
function closeRelatedPersonDialog() {
  isClosingDialog.value = true
  showRelatedPersonDialog.value = false
  setTimeout(() => {
    isClosingDialog.value = false
  }, 300)
}

/** 处理关联人员清空 */
function handleRelatedPersonClear() {
  documentFormData.relatedPerson = ''
}

/** 处理关联人员弹窗内的搜索 */
function handleRelatedPersonDialogSearch(params: any) {
  if (params && typeof params === 'object') {
    const name = params.name || params.peopleName || ''
    documentFormData.relatedPerson = name
    ElMessage.success(`已选择关联人员：${name}`)
  }
  closeRelatedPersonDialog()
}

/** 处理账户输入框获得焦点 */
function handleAccountInputFocus() {
  if (isClosingDialog.value) return
  showAccountDialog.value = true
}

/** 关闭账户弹窗 */
function closeAccountDialog() {
  isClosingDialog.value = true
  showAccountDialog.value = false
  setTimeout(() => {
    isClosingDialog.value = false
  }, 300)
}

/** 处理账户清空 */
function handleAccountClear() {
  documentFormData.settlementAccount = ''
}

/** 处理账户弹窗内的搜索 */
function handleAccountDialogSearch(params: any) {
  if (params && typeof params === 'object') {
    const name = params.name || params.accountName || ''
    documentFormData.settlementAccount = name
    ElMessage.success(`已选择结算账户：${name}`)
  }
  closeAccountDialog()
}

// ====== 表格操作 ======
function addNewRow() {
  tableData.value.push({
    expenseType: '',
    settlementAmount: '',
    remark: ''
  })
}

function deleteRow(index: number) {
  if (tableData.value.length > 1) {
    tableData.value.splice(index, 1)
    ElMessage.success('已删除该行')
    recalculateTotals()
  } else {
    ElMessage.warning('至少保留一行数据')
  }
}

function handleExpenseTypeChange(row: OtherExpenseTableRow) {
  console.log('支出类别选择:', row.expenseType)
  recalculateTotals()
}

// ====== 可编辑单元格 ======
type EditableField = 'settlementAmount' | 'remark'

function onEditableInput(row: OtherExpenseTableRow, field: EditableField, e: Event) {
  const el = e.target as HTMLElement | null
  if (!el) return

  const raw = el.innerText ?? ''

  if (field === 'remark') {
    row.remark = raw
    return
  }

  // 数值字段：清洗为数字/小数
  const cleaned = raw.replace(/[^\d.]/g, '')
  row[field] = cleaned
  recalculateTotals()
}

function onEditableBlur(row: OtherExpenseTableRow, field: EditableField, e: Event) {
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
  recalculateTotals()
}

// ====== 计算和状态 ======
const totalCount = computed(() => tableData.value.length)

const totalSum = computed(() => {
  return tableData.value
    .reduce((sum: number, r: OtherExpenseTableRow) => sum + (Number(r.settlementAmount) || 0), 0)
    .toFixed(2)
})

function recalculateTotals() {
  const totalAmount = tableData.value.reduce((sum: number, row: OtherExpenseTableRow) => {
    return sum + (parseFloat(row.settlementAmount) || 0)
  }, 0)

  documentFormData.documentCost = totalAmount.toFixed(2)

  // 如果实际金额和实付金额为空，自动填充
  if (!documentFormData.actualAmount) {
    documentFormData.actualAmount = totalAmount.toFixed(2)
  }
  if (!documentFormData.paidAmount) {
    documentFormData.paidAmount = totalAmount.toFixed(2)
  }
}

// ====== 列显隐设置 ======
const showColumnSetting = ref(false)
const columnLabels: Record<string, string> = {
  expenseType: '支出类别',
  settlementAmount: '结算金额',
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

// ====== 按钮功能 ======
/* 保存 */
const save = async () => {
  // 验证表单
  if (!documentFormRef.value) return

  const valid = await documentFormRef.value.validate().catch(() => false)
  if (!valid) {
    ElMessage.error('请完善必填信息')
    return
  }

  // 构建结算数据
  const settlementData = {
    header,
    supplier: formData.supplier,
    tableData: tableData.value,
    footer: documentFormData
  }

  // 发送结算成功事件
  emit('settlement-success', settlementData)
  emit('save', settlementData)

  ElMessage.success('结算成功')
  innerVisible.value = false
}

// 监听选中的记录和结算数据变化，填充数据
watch(
  () => [props.selectedRecords, props.settlementData],
  ([newRecords, newSettlementData]) => {
    // 使用类型断言确保类型正确
    const selectedRecords = newRecords as SelectedRecord[] | undefined
    const settlementData = newSettlementData as SettlementDataItem[] | undefined

    if (selectedRecords && selectedRecords.length > 0) {
      // 填充供应商信息（使用第一个记录的往来单位）
      const firstRecord = selectedRecords[0]
      if (firstRecord.partnerUnit || firstRecord.contactUnit) {
        formData.supplier = firstRecord.partnerUnit || firstRecord.contactUnit || ''
      }

      // 将结算数据与选中的记录合并
      tableData.value = selectedRecords.map((record: SelectedRecord, index: number) => {
        // 获取对应的结算数据
        const settlementItem =
          settlementData && settlementData[index]
            ? settlementData[index]
            : ({} as SettlementDataItem)

        return {
          expenseType: settlementItem.type || record.expenseType || '采购支出',
          settlementAmount: settlementItem.money
            ? Math.abs(settlementItem.money).toString()
            : Math.abs(record.unsettledAmount || 0).toString(),
          remark: settlementItem.data || `结算单据：${record.documentNumber || record.orderNo}`,
          costId: record.id || settlementItem.id
        }
      })

      // 重新计算金额
      recalculateTotals()
    }
  },
  { immediate: true, deep: true }
)

onMounted(() => {
  // 初始计算金额
  recalculateTotals()
})
</script>
<style scoped>
/* 按钮图标黑白化风格 */
:deep(.el-button) {
  color: #333;
}

:deep(.el-button:hover) {
  color: #000;
  background-color: #f5f5f5;
}

:deep(.el-icon svg) {
  width: 16px;
  height: 16px;
  fill: currentColor;
}

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

/* ===== 弹窗整体布局 ===== */
.sb-detail-dialog :deep(.el-dialog__body) {
  display: flex;
  flex-direction: column;
  height: 100vh;
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
  align-items: center;
  gap: 12px;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-label {
  font-size: 13px;
  color: #606266;
  font-weight: 500;
  white-space: nowrap;
}

.date-input {
  width: 160px;
}

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

/* 主体部分 */
.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: #fff;
}

/* 表格区域 */
.table-wrap {
  flex: 1 1 auto;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border-bottom: 1px solid #ebeef5;
  position: relative;
}

.table-wrap :deep(.el-table) {
  flex: 1;
  overflow-y: auto;
  min-height: 300px;
}

/* 状态栏 */
.grid-status {
  flex-shrink: 0;
  border-top: 1px solid #ebeef5;
  background: #fafafa;
  text-align: left;
  padding: 6px 16px;
  font-size: 13px;
  color: #606266;
  display: flex;
  gap: 20px;
}

.status-item {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

/* 底部表单 */
.document-form-container {
  flex-shrink: 0;
  border-top: 1px solid #ebeef5;
  padding: 12px 16px;
  background: #f9fafb;
}

.bottom-form {
  margin-bottom: 0;
}

.upload-demo {
  width: 200px;
}

/* 可编辑单元格 */
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
  cursor: text;
}

:deep(.el-select) {
  width: 100%;
}

/* 底部按钮 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  padding: 10px 0;
  gap: 10px;
}
</style>
