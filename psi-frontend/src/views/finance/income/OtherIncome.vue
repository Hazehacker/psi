<template>
  <div class="page-container">
    <!-- 商品表格区域 -->
    <div class="table-section">
      <!-- 表单和按钮行 -->
      <div class="header-row">
        <!-- 左侧表单字段 -->
        <div class="header-form">
          <!-- 客户 - 使用搜索组件中的客户搜索框 -->
          <div class="form-item">
            <label class="form-label">客户</label>
            <el-input
              v-model="formData.customer"
              placeholder="请选择客户"
              readonly
              clearable
              style="cursor: pointer; width: 240px"
              @focus="handleCustomerInputFocus"
              @clear="handleCustomerClear"
            >
              <template #suffix>
                <el-icon @click.stop="handleCustomerInputFocus" style="cursor: pointer">
                  <Search />
                </el-icon>
              </template>
            </el-input>
          </div>

          <!-- 单据日期 -->
          <div class="form-item">
            <label class="form-label">单据日期</label>
            <el-date-picker
              v-model="headerForm.documentDate"
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
              v-model="headerForm.documentNumber"
              placeholder="自动生成"
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

          <!-- 已审核状态：显示核对/反核对、反审核和刷新 -->
          <ButtonGroup v-else align="right" gap="0px">
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
              <el-button v-if="scope.$index === 0" type="text" size="small" @click="addNewRow">
                <el-icon><Plus /></el-icon>
              </el-button>
              <el-button v-else type="text" size="small" @click="deleteRow(scope.$index)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </template>
          </el-table-column>

          <!-- 收入类别 -->
          <el-table-column
            v-if="visibleColumns.incomeType"
            label="收入类别"
            align="center"
            width="150"
          >
            <template #default="scope">
              <el-select
                v-model="scope.row.incomeType"
                placeholder="点击选择"
                clearable
                @change="handleIncomeTypeChange(scope.row)"
              >
                <el-option label="销售收入" value="销售收入" />
                <el-option label="服务收入" value="服务收入" />
                <el-option label="其他业务收入" value="其他业务收入" />
                <el-option label="营业外收入" value="营业外收入" />
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

      <!-- 状态栏 -->
      <div class="grid-status">
        <span class="status-item">总条数: {{ totalCount }}</span>
        <span class="status-item">总金额: {{ totalSum }}</span>
      </div>

      <!-- 已审核标记 -->
      <div v-if="isAudited" class="audit-stamp">已审核</div>
    </div>

    <!-- 底部表单 - 自己实现 -->
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

        <!-- 实收金额 -->
        <el-form-item label="实收金额" prop="receivedAmount">
          <el-input
            v-model="documentFormData.receivedAmount"
            placeholder="请输入实收金额"
            style="width: 200px"
          />
        </el-form-item>

        <!-- 结算账户 - 使用搜索框 -->
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

        <!-- 关联人员 - 使用搜索框 -->
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

  <!-- 客户搜索弹窗 -->
  <el-dialog
    v-model="showCustomerDialog"
    title="客户搜索"
    width="720px"
    append-to-body
    :close-on-click-modal="false"
  >
    <Customer @search="handleCustomerDialogSearch" />
    <template #footer>
      <el-button @click="closeCustomerDialog">关闭</el-button>
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
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, nextTick } from 'vue'
import {
  ElMessage,
  ElMessageBox,
  type FormInstance,
  type FormRules,
  type UploadProps,
  type UploadUserFile
} from 'element-plus'
import { Refresh, Setting, Delete, Plus, Search } from '@element-plus/icons-vue'

// 导入 Finance Store
import {
  useFinanceStore,
  type OtherIncomeTableRow,
  type DocumentFormFields
} from '@/stores/finance'

// 导入搜索组件
import Customer from '@/components/goodSearchConpoent/Customer.vue'
import Account from '@/components/goodSearchConpoent/Account.vue'
import PeopleList from '@/components/goodSearchConpoent/PeopleList.vue'

// 导入API
import { addOtherIncome, examineOtherIncome } from '@/apis/finance/income'

// 初始化 Finance store
const financeStore = useFinanceStore()

// 表单引用
const documentFormRef = ref<FormInstance>()

// 搜索表单数据
const formData = reactive({
  customer: ''
})

// 弹窗控制
const showCustomerDialog = ref(false)
const showRelatedPersonDialog = ref(false)
const showAccountDialog = ref(false)

// 防止重复打开弹窗的标志
const isClosingDialog = ref(false)

// 底部表单数据 - 修复：提供默认值
const documentFormData = reactive<DocumentFormFields>({
  documentCost: '0',
  actualAmount: '0',
  receivedAmount: '0',
  settlementAccount: '',
  relatedPerson: '',
  documentAttachment: '',
  remarks: ''
})

// 表单验证规则
const formRules: FormRules = {
  actualAmount: [
    { required: true, message: '请输入实际金额', trigger: 'blur' },
    { pattern: /^\d+(\.\d{1,2})?$/, message: '请输入正确的金额格式', trigger: 'blur' }
  ],
  receivedAmount: [
    { required: true, message: '请输入实收金额', trigger: 'blur' },
    { pattern: /^\d+(\.\d{1,2})?$/, message: '请输入正确的金额格式', trigger: 'blur' }
  ],
  settlementAccount: [{ required: true, message: '请选择结算账户', trigger: 'change' }],
  relatedPerson: [{ required: true, message: '请选择关联人员', trigger: 'change' }]
}

// 文件上传相关
const fileList = ref<UploadUserFile[]>([])

const handleRemove: UploadProps['onRemove'] = (file, uploadFiles) => {
  console.log(file, uploadFiles)
}

const handlePreview: UploadProps['onPreview'] = (uploadFile) => {
  console.log(uploadFile)
}

const handleExceed: UploadProps['onExceed'] = (files, uploadFiles) => {
  ElMessage.warning(
    `当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + uploadFiles.length} 个文件`
  )
}

const beforeRemove: UploadProps['beforeRemove'] = (uploadFile, uploadFiles) => {
  return ElMessageBox.confirm(`确定移除 ${uploadFile.name}？`).then(
    () => true,
    () => false
  )
}

/** 处理客户输入框获得焦点 */
function handleCustomerInputFocus() {
  if (isClosingDialog.value) return
  showCustomerDialog.value = true
}

/** 关闭客户弹窗 */
function closeCustomerDialog() {
  isClosingDialog.value = true
  showCustomerDialog.value = false
  setTimeout(() => {
    isClosingDialog.value = false
  }, 300)
}

/** 处理客户清空 */
function handleCustomerClear() {
  formData.customer = ''
}

/** 处理客户弹窗内的搜索 */
function handleCustomerDialogSearch(params: any) {
  if (params && typeof params === 'object') {
    const name = params.name || params.customerName || ''
    formData.customer = name
    ElMessage.success(`已选择客户：${name}`)
  }
  closeCustomerDialog()
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
  financeStore.setDocumentFormData({ relatedPerson: '' })
}

/** 处理关联人员弹窗内的搜索 */
function handleRelatedPersonDialogSearch(params: any) {
  if (params && typeof params === 'object') {
    const name = params.name || params.peopleName || ''
    documentFormData.relatedPerson = name
    financeStore.setDocumentFormData({ relatedPerson: name })
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
  financeStore.setDocumentFormData({ settlementAccount: '' })
}

/** 处理账户弹窗内的搜索 */
function handleAccountDialogSearch(params: any) {
  if (params && typeof params === 'object') {
    const name = params.name || params.accountName || ''
    documentFormData.settlementAccount = name
    financeStore.setDocumentFormData({ settlementAccount: name })
    ElMessage.success(`已选择结算账户：${name}`)
  }
  closeAccountDialog()
}

// 表格数据
const tableData = ref<OtherIncomeTableRow[]>([
  {
    incomeType: '',
    settlementAmount: '',
    remark: ''
  }
])

// 添加新行
function addNewRow() {
  tableData.value.push({
    incomeType: '',
    settlementAmount: '',
    remark: ''
  })
}

// 删除行
function deleteRow(index: number) {
  if (tableData.value.length > 1) {
    tableData.value.splice(index, 1)
    ElMessage.success('已删除该行')
    recalculateTotals()
  } else {
    ElMessage.warning('至少保留一行数据')
  }
}

// 处理收入类别变化
function handleIncomeTypeChange(row: OtherIncomeTableRow) {
  console.log('收入类别选择:', row.incomeType)
  recalculateTotals()
}

// 重新计算总计
function recalculateTotals() {
  // 更新 store 中的金额计算
  financeStore.updateDocumentAmounts(tableData.value)

  // 更新底部表单的金额字段
  const totalAmount = tableData.value.reduce((sum, row) => {
    return sum + (parseFloat(row.settlementAmount) || 0)
  }, 0)

  documentFormData.documentCost = totalAmount.toFixed(2)

  // 如果实际金额和实收金额为空，自动填充
  if (!documentFormData.actualAmount || documentFormData.actualAmount === '0') {
    documentFormData.actualAmount = totalAmount.toFixed(2)
  }
  if (!documentFormData.receivedAmount || documentFormData.receivedAmount === '0') {
    documentFormData.receivedAmount = totalAmount.toFixed(2)
  }
}

// 总条数和总金额（状态栏数据）
const totalCount = computed(() => tableData.value.length)
const totalSum = computed(() => {
  return tableData.value.reduce((sum, r) => sum + (Number(r.settlementAmount) || 0), 0).toFixed(2)
})

// 头部表单数据
const headerForm = reactive({
  customer: '',
  documentDate: new Date().toISOString().split('T')[0],
  documentNumber: `SR${new Date().getTime().toString().slice(-8)}`
})

// 审核状态管理
const isAudited = ref(false)

// 核对状态管理
const isChecked = ref(false)

// 存储收入单ID
const currentIncomeId = ref<string>('')

/**
 * 保存按钮处理函数
 */
async function handleSave() {
  // 验证表单
  if (!documentFormRef.value) return

  const valid = await documentFormRef.value.validate().catch(() => false)
  if (!valid) {
    ElMessage.error('请完善必填信息')
    return
  }

  try {
    // 构建请求数据 - 修复：确保所有必填字段都有值
    const requestData = {
      account: documentFormData.settlementAccount || '',
      actual: parseFloat(documentFormData.actualAmount || '0') || 0,
      customer: formData.customer || '',
      data: documentFormData.remarks || '',
      file: documentFormData.documentAttachment || '',
      frame: '', // 根据实际情况设置
      info: tableData.value.map((row) => ({
        data: row.remark || '',
        iet: mapIncomeTypeToCode(row.incomeType), // 映射收入类型编码
        money: parseFloat(row.settlementAmount) || 0
      })),
      money: parseFloat(documentFormData.receivedAmount || '0') || 0,
      people: documentFormData.relatedPerson || '',
      time: headerForm.documentDate,
      total: parseFloat(documentFormData.documentCost || '0') || 0,
      user: '' // 根据实际情况设置制单人
    }

    // 调用新增接口
    const response = await addOtherIncome(requestData)

    if (response.code === 10000) {
      // 保存成功后自动审核
      const incomeId = response.data
      if (incomeId) {
        const examineResponse = await examineOtherIncome({
          ids: [incomeId], // 修复：确保incomeId不为undefined
          examine: 1
        })

        if (examineResponse.code === 10000) {
          // 保存收入单ID
          currentIncomeId.value = incomeId

          // 保存到 Finance store
          financeStore.saveOtherIncomeData({
            headerForm: {
              customer: formData.customer || headerForm.customer,
              documentDate: headerForm.documentDate,
              documentNumber: headerForm.documentNumber
            },
            tableData: tableData.value,
            isAudited: true,
            isChecked: isChecked.value
          })

          // 保存底部表单数据
          financeStore.setDocumentFormData(documentFormData)

          // 设置为已审核状态
          isAudited.value = true
          ElMessage.success(`保存并审核成功，已保存 ${tableData.value.length} 条数据`)
        } else {
          ElMessage.error(examineResponse.message || '审核失败')
        }
      } else {
        ElMessage.error('保存成功但未返回收入单ID')
      }
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败，请稍后重试')
  }
}

/**
 * 映射收入类型到编码
 */
function mapIncomeTypeToCode(incomeType: string): string {
  const typeMap: Record<string, string> = {
    销售收入: 'INC001',
    服务收入: 'INC002',
    其他业务收入: 'INC003',
    营业外收入: 'INC004'
  }
  return typeMap[incomeType] || 'INC001'
}

/**
 * 刷新按钮处理函数 - 清除所有新增的行数据
 */
function handleRefresh() {
  console.log('刷新数据 - 清除新增行')

  // 只保留第一行，重置第一行数据
  tableData.value = [
    {
      incomeType: '',
      settlementAmount: '',
      remark: ''
    }
  ]

  // 重置表单数据
  documentFormData.documentCost = '0'
  documentFormData.actualAmount = '0'
  documentFormData.receivedAmount = '0'

  // 重新计算总计
  recalculateTotals()

  ElMessage.success('已清除所有新增行数据')
}

/**
 * 反审核按钮处理函数
 */
async function handleUnaudit() {
  try {
    if (!currentIncomeId.value) {
      ElMessage.error('未找到收入单ID')
      return
    }

    // 调用反审核接口
    const response = await examineOtherIncome({
      ids: [currentIncomeId.value],
      examine: 0
    })

    if (response.code === 10000) {
      // 恢复到未审核状态
      isAudited.value = false
      // 同时重置核对状态
      isChecked.value = false
      ElMessage.success('反审核成功')
    } else {
      ElMessage.error(response.message || '反审核失败')
    }
  } catch (error) {
    console.error('反审核失败:', error)
    ElMessage.error('反审核失败，请稍后重试')
  }
}

/**
 * 核对按钮处理函数
 */
function handleCheck() {
  console.log('核对数据')
  // 这里可以添加核对逻辑
  // 设置为已核对状态
  isChecked.value = true
  ElMessage.success('数据核对完成')
}

/**
 * 反核对按钮处理函数
 */
function handleUncheck() {
  console.log('反核对数据')
  // 取消核对状态
  isChecked.value = false
  ElMessage.success('已取消核对')
}

// 可编辑单元格处理
type EditableField = 'settlementAmount' | 'remark'

function onEditableInput(row: OtherIncomeTableRow, field: EditableField, e: Event) {
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

  // 重新计算总合计
  recalculateTotals()
}

function onEditableBlur(row: OtherIncomeTableRow, field: EditableField, e: Event) {
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

// ====== 列显隐（设置⚙）======
const showColumnSetting = ref(false)
const columnLabels: Record<string, string> = {
  incomeType: '收入类别',
  settlementAmount: '结算金额',
  remark: '备注信息'
}
const checkedColumns = ref<string[]>(Object.keys(columnLabels))
const visibleColumns = reactive<Record<string, boolean>>({})

// 监听 checkedColumns 变化，更新 visibleColumns
watch(
  checkedColumns,
  (newVal) => {
    // 先清空所有列
    Object.keys(columnLabels).forEach((key) => {
      visibleColumns[key] = false
    })
    // 设置选中的列为显示
    newVal.forEach((key) => {
      visibleColumns[key] = true
    })
  },
  { immediate: true }
)

// 在组件挂载后的操作
onMounted(() => {
  // 初始化底部表单数据
  const savedFormData = financeStore.getDocumentFormData
  if (savedFormData) {
    Object.assign(documentFormData, savedFormData)
  }

  // 自动生成单据编号（如果为空）
  if (!headerForm.documentNumber) {
    headerForm.documentNumber = `SR${new Date().getTime().toString().slice(-8)}`
  }

  // 初始计算金额
  recalculateTotals()
})
</script>
<style scoped>
/* 样式保持不变 */
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

.table-wrapper {
  height: 500px;
  overflow-y: auto;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  background-color: white;
}

.page-container {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 0;
}

.table-section {
  position: relative;
  background-color: white;
  border-radius: 8px 8px 0 0;
  padding: 20px;
  padding-bottom: 10px;
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

.header-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
  gap: 20px;
}

.header-form {
  display: flex;
  gap: 16px;
  align-items: flex-start;
  flex: 1;
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

.customer-input {
  width: 240px;
}

.customer-input :deep(.el-input__inner) {
  cursor: pointer;
}

.search-icon {
  cursor: pointer;
  color: #409eff;
}

.date-input {
  width: 160px;
}

.number-input {
  width: 180px;
}

.action-buttons-container {
  display: flex;
  justify-content: flex-end;
  padding: 0;
}

.action-buttons-container :deep(.button-group) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  overflow: hidden;
}

.action-buttons-container :deep(.el-button) {
  margin: 0 !important;
  border-radius: 0 !important;
  transition: all 0.3s ease;
}

.action-buttons-container :deep(.el-button + .el-button) {
  border-left: 1px solid rgba(255, 255, 255, 0.3);
}

.action-buttons-container :deep(.el-button:first-child) {
  border-top-left-radius: 4px !important;
  border-bottom-left-radius: 4px !important;
}

.action-buttons-container :deep(.el-button:last-child) {
  border-top-right-radius: 4px !important;
  border-bottom-right-radius: 4px !important;
}

.action-buttons-container :deep(.el-button:hover) {
  background-color: #66b1ff;
  border-color: #66b1ff;
  z-index: 1;
}

.action-buttons-container :deep(.el-button:active) {
  background-color: #3a8ee6;
  border-color: #3a8ee6;
}

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

.document-form-container {
  background-color: white;
  padding: 20px;
  border-radius: 0 0 8px 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-top: 0;
}

.bottom-form {
  margin-bottom: 0;
}

.upload-demo {
  width: 200px;
}
</style>
