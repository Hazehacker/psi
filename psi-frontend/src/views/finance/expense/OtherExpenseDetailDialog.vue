<template>
  <el-dialog
    v-model="innerVisible"
    fullscreen
    :close-on-click-modal="false"
    class="sb-detail-dialog"
    title="其它支出单详情"
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

      <div class="right">
        <!-- 审核通过时：显示 反审核 -->
        <template v-if="isApproved">
          <el-button class="ml8" @click="toggleApprove">反审核</el-button>
        </template>

        <!-- 未审核时：显示 保存 + 审核 -->
        <template v-else>
          <el-button type="primary" @click="save">保存</el-button>
          <el-button class="ml8" @click="toggleApprove">审核</el-button>
        </template>

        <!-- 核对 / 反核对按钮 -->
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

        <!-- 已审核印章 -->
        <transition name="fade-zoom">
          <div v-if="isApproved" class="approved-stamp">已审核</div>
        </transition>

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

// 导入API
import {
  updateOtherExpense,
  examineOtherExpense,
  getOtherExpenseDetail
} from '@/apis/finance/expense'
import type {
  OtherExpenseDTO,
  OtherExpenseData,
  OtherExpenseInfoData,
  OtherExpenseDetail
} from '@/apis/finance/expense/type'

// 导入类型定义
interface OtherExpenseTableRow {
  expenseType: string
  settlementAmount: string
  remark: string
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

/* props & emits */
interface Props {
  visible: boolean
  record?: any
}
const props = defineProps<Props>()
const emit = defineEmits(['update:visible', 'save'])

/* v-model:visible */
const innerVisible = computed({
  get: () => props.visible,
  set: (v) => emit('update:visible', v)
})

/* 审核状态 */
const isApproved = ref(false)

/* 核对状态 */
const isChecked = ref(false)

/* 头部表单数据 */
const header = reactive({
  documentDate: new Date().toISOString().split('T')[0],
  documentNumber: `SE${new Date().getTime().toString().slice(-8)}` // 单据编号前缀改为SE（支出）
})

/* 供应商相关 */
const formData = reactive({
  supplier: ''
})

/* 表格数据 */
const tableData = ref<OtherExpenseTableRow[]>([
  {
    expenseType: '', // 改为支出类别
    settlementAmount: '',
    remark: ''
  }
])

/* 底部表单数据 */
const documentFormData = reactive<DocumentFormFields>({
  documentCost: '',
  actualAmount: '',
  paidAmount: '', // 实付金额
  settlementAccount: '',
  relatedPerson: '',
  documentAttachment: '',
  remarks: ''
})

// 当前支出单ID
const currentExpenseId = ref<string>('')

// 表单引用
const documentFormRef = ref()

// 表单验证规则 - 实付金额设为必填
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
  return tableData.value.reduce((sum, r) => sum + (Number(r.settlementAmount) || 0), 0).toFixed(2)
})

function recalculateTotals() {
  const totalAmount = tableData.value.reduce((sum, row) => {
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
  expenseType: '支出类别', // 改为支出类别
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
const toggleApprove = async () => {
  if (!currentExpenseId.value) {
    ElMessage.warning('请先保存数据')
    return
  }

  try {
    const result = await examineOtherExpense({
      ids: [currentExpenseId.value],
      approve: !isApproved.value // true 表示审核，false 表示反审核
    })

    if (result.code === 10000) {
      isApproved.value = !isApproved.value
      ElMessage.success(isApproved.value ? '审核成功' : '反审核成功')
    } else {
      ElMessage.error(result.message || '操作失败')
    }
  } catch (error) {
    console.error('审核/反审核失败:', error)
    ElMessage.error('操作失败')
  }
}

const toggleCheck = () => {
  isChecked.value = !isChecked.value
  ElMessage.success(isChecked.value ? '核对成功' : '反核对成功')
}

/* 保存 */
const save = async () => {
  // 验证表单
  if (!documentFormRef.value) return

  const valid = await documentFormRef.value.validate().catch(() => false)
  if (!valid) {
    ElMessage.error('请完善必填信息')
    return
  }

  try {
    // 准备数据
    const requestData: OtherExpenseDTO = {
      otherExpense: {
        id: currentExpenseId.value,
        account: documentFormData.settlementAccount,
        actual: parseFloat(documentFormData.actualAmount) || 0,
        data: documentFormData.remarks,
        examine: isApproved.value ? 2 : 1, // 1: 未审核, 2: 已审核
        file: documentFormData.documentAttachment,
        frame: '默认组织', // 根据实际情况设置
        money: parseFloat(documentFormData.paidAmount) || 0,
        more: '', // 扩展信息
        nucleus: isChecked.value ? 1 : 0, // 核销状态
        number: header.documentNumber,
        people: documentFormData.relatedPerson,
        supplier: formData.supplier,
        time: header.documentDate,
        total: parseFloat(documentFormData.documentCost) || 0,
        user: '管理员' // 根据实际情况设置
      },
      otherExpenseInfoList: tableData.value.map((row, index) => ({
        id: `info_${index}_${new Date().getTime()}`,
        data: row.remark,
        iet: row.expenseType,
        money: parseFloat(row.settlementAmount) || 0,
        pid: currentExpenseId.value,
        source: '费用' // 根据实际情况设置
      }))
    }

    const result = await updateOtherExpense(requestData)

    if (result.code === 10000) {
      emit('save', {
        header,
        supplier: formData.supplier,
        tableData: tableData.value,
        footer: documentFormData,
        isApproved: isApproved.value,
        isChecked: isChecked.value
      })

      ElMessage.success('保存成功')
    } else {
      ElMessage.error(result.message || '保存失败')
    }
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败，请稍后重试')
  }
}

// 监听record变化，加载数据
watch(
  () => props.record,
  async (newRecord) => {
    if (newRecord) {
      await loadRecordData(newRecord)
    }
  },
  { immediate: true }
)

async function loadRecordData(record: any) {
  try {
    // 如果有ID，调用详情接口
    if (record.id) {
      const result = await getOtherExpenseDetail(record.id)
      if (result.code === 10000 && result.data) {
        const detailData: OtherExpenseDetail = result.data
        loadDetailData(detailData)
        currentExpenseId.value = record.id
      }
    } else {
      // 如果没有ID，使用record数据
      loadBasicData(record)
    }
  } catch (error) {
    console.error('加载详情失败:', error)
    // 如果接口调用失败，使用record数据
    loadBasicData(record)
  }
}

// 在 loadDetailData 函数中，修复 nucleus 属性访问
function loadDetailData(detailData: OtherExpenseDetail) {
  // 加载主表数据
  if (detailData.supplier) formData.supplier = detailData.supplier
  if (detailData.time) header.documentDate = detailData.time
  if (detailData.number) header.documentNumber = detailData.number
  if (detailData.total) documentFormData.documentCost = detailData.total.toString()
  if (detailData.actual) documentFormData.actualAmount = detailData.actual.toString()
  if (detailData.money) documentFormData.paidAmount = detailData.money.toString()
  if (detailData.account) documentFormData.settlementAccount = detailData.account
  if (detailData.people) documentFormData.relatedPerson = detailData.people
  if (detailData.data) documentFormData.remarks = detailData.data

  // 加载明细数据
  if (detailData.info && detailData.info.length > 0) {
    tableData.value = detailData.info.map((item: OtherExpenseInfoData) => ({
      expenseType: item.iet || '',
      settlementAmount: item.money?.toString() || '',
      remark: item.data || ''
    }))
  }

  // 设置审核状态 - 使用可选链操作符
  isApproved.value = detailData.examine === 2
  isChecked.value = detailData.nucleus === 1 // 现在 nucleus 属性已存在

  // 重新计算总计
  recalculateTotals()
}

function loadBasicData(record: any) {
  // 这里根据实际的数据结构来加载记录数据
  if (record.supplier) formData.supplier = record.supplier
  if (record.billDate) header.documentDate = record.billDate
  if (record.billNo) header.documentNumber = record.billNo
  if (record.billAmount) documentFormData.documentCost = record.billAmount.toString()
  if (record.actualAmount) documentFormData.actualAmount = record.actualAmount.toString()
  if (record.paymentAmount) documentFormData.paidAmount = record.paymentAmount.toString()
  if (record.settlementAccount) documentFormData.settlementAccount = record.settlementAccount
  if (record.relatedPerson) documentFormData.relatedPerson = record.relatedPerson
  if (record.remark) documentFormData.remarks = record.remark

  // 设置审核状态
  isApproved.value = record.auditStatus === 2
  isChecked.value = record.checkStatus === '已核对'

  // 重新计算总计
  recalculateTotals()
}

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

/* 审核印章 */
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
