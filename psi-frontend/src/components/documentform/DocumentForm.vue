<template>
  <section class="root-container">
    <!-- 单据表单 -->
    <div class="form-section">
      <el-form ref="formRef" :model="formdata" :rules="rules" v-bind="formattr">
        <LineFeed tag=".el-form-item" :rule="3">
          <!-- 单据金额（只读样式近似禁用） -->
          <el-form-item v-if="visibleFields.has('documentCost')" label="单据金额" prop="documentCost" :rules="[{ required: true, message: '请输入单据金额', trigger: 'blur' }]">
            <el-input
              v-model="formdata.documentCost"
              placeholder="自动计算的单据金额"
              type="text"
              :readonly="true"
              class="readonly-like-disabled"
              style="width: 100%"
            />
          </el-form-item>

          <!-- 单据成本（只读样式近似禁用） -->
          <el-form-item v-if="visibleFields.has('documentBaseCost')" label="单据成本" prop="documentBaseCost" :rules="[{ required: true, message: '请输入单据成本', trigger: 'blur' }]">
            <el-input
              v-model="formdata.documentBaseCost"
              placeholder="自动计算的单据成本"
              type="text"
              :readonly="true"
              class="readonly-like-disabled"
              style="width: 100%"
            />
          </el-form-item>

          <!-- 单据类型 -->
          <el-form-item v-if="visibleFields.has('documentType')" label="单据类型" prop="documentType" :rules="[{ required: true, message: '请选择单据类型', trigger: 'change' }]">
            <el-select v-model="formdata.documentType" placeholder="请选择单据类型" clearable style="width: 100%">
              <el-option
                v-for="option in documentTypeOptions"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
          </el-form-item>

          <!-- 实际金额（带复制图标） -->
          <el-form-item v-if="visibleFields.has('actualAmount')" label="实际金额" prop="actualAmount" :rules="[{ required: true, message: '请输入实际金额', trigger: 'blur' }]">
            <el-input
              v-model="formdata.actualAmount"
              placeholder="请输入实际金额"
              type="text"
              style="width: 100%"
              clearable
            >
              <template #suffix>
                <i class="el-icon-copy-document" @click="copyTotalToActual"></i>
              </template>
            </el-input>
          </el-form-item>

          <!-- 实付金额 -->
          <el-form-item v-if="visibleFields.has('paidAmount')" label="实付金额" prop="paidAmount" :rules="[{ required: true, message: '请输入实付金额', trigger: 'blur' }]">
            <el-input
              v-model="formdata.paidAmount"
              placeholder="请输入实付金额"
              type="text"
              style="width: 100%"
            />
          </el-form-item>

          <!-- 实收金额 -->
          <el-form-item v-if="visibleFields.has('receivedAmount')" label="实收金额" prop="receivedAmount" :rules="[{ required: true, message: '请输入实收金额', trigger: 'blur' }]">
            <el-input
              v-model="formdata.receivedAmount"
              placeholder="请输入实收金额"
              type="text"
              style="width: 100%"
            />
          </el-form-item>

          <!-- 单据费用 -->
          <el-form-item v-if="visibleFields.has('documentFee')" label="单据费用" prop="documentFee" :rules="[{ required: true, message: '请输入单据费用', trigger: 'blur' }]">
            <el-input
              v-model="formdata.documentFee"
              placeholder="请输入单据费用"
              type="text"
              style="width: 100%"
            />
          </el-form-item>

          <!-- 关联人员 -->
          <el-form-item v-if="visibleFields.has('relatedPerson')" label="关联人员" prop="relatedPerson" :rules="[{ required: true, message: '请选择关联人员', trigger: 'change' }]">
            <el-input
              :model-value="formdata.relatedPersonName || ''"
              placeholder="请选择关联人员"
              readonly
              clearable
              style="width: 100%"
              @focus="showPeopleDialog = true"
              @clear="handleClearRelatedPerson"
            >
              <template #suffix>
                <el-icon @click.stop="showPeopleDialog = true" style="cursor: pointer">
                  <ArrowDown />
                </el-icon>
              </template>
            </el-input>

            <!-- 人员搜索对话框 -->
            <el-dialog
              v-model="showPeopleDialog"
              title="人员搜索"
              width="720px"
              append-to-body
              :close-on-click-modal="false"
              :modal="true"
              destroy-on-close
            >
              <div @click.stop @mousedown.stop @mouseup.stop>
                <PeopleList @search="handlePeopleSearch" />
              </div>
              <template #footer>
                <div @click.stop @mousedown.stop @mouseup.stop>
                  <el-button @click="showPeopleDialog = false">关闭</el-button>
                </div>
              </template>
            </el-dialog>
          </el-form-item>

          <!-- 结算账户 -->
          <el-form-item v-if="visibleFields.has('settlementAccount')" label="结算账户" prop="settlementAccount" :rules="[{ required: true, message: '请选择结算账户', trigger: 'change' }]">
            <el-input
              :model-value="formdata.settlementAccountName || ''"
              placeholder="请选择结算账户"
              readonly
              clearable
              style="width: 100%"
              @focus="showAccountDialog = true"
              @clear="handleClearSettlementAccount"
            >
              <template #suffix>
                <el-icon @click.stop="showAccountDialog = true" style="cursor: pointer">
                  <ArrowDown />
                </el-icon>
              </template>
            </el-input>

            <!-- 结算账户搜索对话框 -->
            <el-dialog
              v-model="showAccountDialog"
              title="资金账户搜索"
              width="720px"
              append-to-body
              :close-on-click-modal="false"
              :modal="true"
              destroy-on-close
            >
              <div @click.stop @mousedown.stop @mouseup.stop>
                <Account @search="handleAccountSearch" />
              </div>
              <template #footer>
                <div @click.stop @mousedown.stop @mouseup.stop>
                  <el-button @click="showAccountDialog = false">关闭</el-button>
                </div>
              </template>
            </el-dialog>
          </el-form-item>

          <!-- 到货日期 -->
          <el-form-item v-if="visibleFields.has('arrivalDate')" label="到货日期" prop="arrivalDate" :rules="[{ required: true, message: '请选择到货日期', trigger: 'change' }]">
            <el-date-picker
              v-model="formdata.arrivalDate"
              type="date"
              placeholder="请选择到货日期"
              style="width: 100%"
            />
          </el-form-item>

          <!-- 物流信息 -->
          <el-form-item v-if="visibleFields.has('logistics')" label="物流信息" prop="logistics" :rules="[{ required: true, message: '请完善物流信息', trigger: 'change' }]">
            <Logistics v-model="formdata.logistics" :more="supplierMore" />
          </el-form-item>

          <!-- 单据附件 -->
          <el-form-item v-if="visibleFields.has('documentAttachment')" label="单据附件" prop="documentAttachment">
            <div class="upload-container">
              <el-upload
                v-model:file-list="formdata.documentAttachment"
                :action="uploadAction"
                :multiple="true"
                :show-file-list="true"
                :before-upload="beforeUpload"
                style="width: 100%"
              >
                <el-button type="primary" class="upload-button">上传附件</el-button>
              </el-upload>
            </div>
          </el-form-item>

          <!-- 备注信息（调整为与实际金额相同输入尺寸） -->
          <el-form-item v-if="visibleFields.has('remarks')" label="备注信息" prop="remarks">
            <el-input
              v-model="formdata.remarks"
              type="text"
              placeholder="请输入备注信息"
              style="width: 100%"
            />
          </el-form-item>

        </LineFeed>
      </el-form>
    </div>
    
    
  </section>
</template>

<script setup lang="ts">
defineOptions({ name: 'DocumentForm' })
import { reactive, ref, computed, watch } from 'vue'
import { storeToRefs } from 'pinia'
import { usePurchaseStore, type PurchaseItem } from '../../stores/purchase'
import { ElMessage, type FormInstance } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'
import Logistics from '@/components/lib/Logistics.vue'
import PeopleList from '@/components/goodSearchConpoent/PeopleList.vue'
import Account from '@/components/goodSearchConpoent/Account.vue'
import type { PropType } from 'vue'

// 定义FormData的模型
interface LogisticsInfo {
  key: string
  name: string
  number: string
}

interface FormData extends Record<string, any> {
  documentType?: string
  relatedPerson?: string // 保存人员ID
  relatedPersonName?: string // 保存人员名称用于显示
  documentAttachment?: any[]
  remarks?: string
  documentCost?: number
  documentBaseCost?: number
  documentFee?: number
  logistics?: LogisticsInfo
  actualAmount?: number
  paidAmount?: number
  arrivalDate?: string
  receivedAmount?: number
  settlementAccount?: string // 保存账户ID（用于提交到后端）
  settlementAccountName?: string // 保存账户名称用于显示
}

// 动态可见字段
const props = defineProps({
  fields: {
    type: Array as PropType<string[]>,
    default: () => [
      'documentCost',
      'documentBaseCost',
      'documentType',
      'actualAmount',
      'receivedAmount',
      'documentFee',
      'relatedPerson',
      'settlementAccount',
      'arrivalDate',
      'logistics',
      'documentAttachment',
      'remarks'
    ]
  }
})

const visibleFields = computed<Set<string>>(() => new Set(props.fields))

// 定义选项类型
interface SelectOption {
  label: string
  value: string
}

// 定义表单数据
const formdata = reactive<FormData>({
  documentType: 'other_inbound',
  relatedPerson: '', // 保存人员ID
  relatedPersonName: '', // 保存人员名称用于显示
  documentAttachment: [],
  remarks: '',
  documentCost: undefined,
  documentBaseCost: undefined,
  documentFee: undefined,
  logistics: { key: 'auto', name: '自动识别', number: '' },
  actualAmount: undefined,
  paidAmount: undefined,
  arrivalDate: '',
  receivedAmount: undefined,
  settlementAccount: '', // 保存账户ID（用于提交到后端）
  settlementAccountName: '' // 保存账户名称用于显示
})

// 定义单据类型选项
const documentTypeOptions = ref<SelectOption[]>([
  { label: '其它入库单', value: 'other_inbound' },
  { label: '采购入库单', value: 'purchase_inbound' },
  { label: '销售出库单', value: 'sales_outbound' },
  { label: '调拨单', value: 'transfer' },
  { label: '盘点单', value: 'inventory' }
])

// 人员选择对话框控制
const showPeopleDialog = ref(false)

// 结算账户选择对话框控制
const showAccountDialog = ref(false)

// 表单实例
const formRef = ref<FormInstance>()

// 表单属性
const formattr = reactive<{ size: '' | 'small' | 'default' | 'large'; 'label-width': string }>({
  size: 'default',
  'label-width': '120px'
})

// 表单验证规则
const rules = reactive({
  documentType: [{ required: true, message: '请选择单据类型', trigger: 'change' }],
  relatedPerson: [{ required: true, message: '请选择关联人员', trigger: 'change' }],
  documentCost: [{ required: true, message: '请输入单据金额', trigger: 'blur' }],
  documentBaseCost: [{ required: true, message: '请输入单据成本', trigger: 'blur' }],
  documentFee: [{ required: true, message: '请输入单据费用', trigger: 'blur' }],
  logistics: [{ required: true, message: '请完善物流信息', trigger: 'change' }],
  actualAmount: [{ required: true, message: '请输入实际金额', trigger: 'blur' }],
  paidAmount: [{ required: true, message: '请输入实付金额', trigger: 'blur' }],
  arrivalDate: [{ required: true, message: '请选择到货日期', trigger: 'change' }],
  receivedAmount: [{ required: true, message: '请输入实收金额', trigger: 'blur' }],
  settlementAccount: [{ required: true, message: '请选择结算账户', trigger: 'change' }]
})

// 上传配置
const uploadAction = '/api/upload'

// 上传前验证
const beforeUpload = (file: File) => {
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('上传文件大小不能超过 10MB!')
    return false
  }
  return true
}

// 提交/重置入口已移除

/**
 * 将单据金额复制到实际金额
 */
function copyTotalToActual() {
  if (formdata.documentCost == null || Number.isNaN(Number(formdata.documentCost))) {
    ElMessage.warning('当前无可复制的单据金额')
    return
    }
  formdata.actualAmount = Number(formdata.documentCost)
  ElMessage.success('已复制单据金额到实际金额')
}

// 通过 Pinia 获取表格中的商品数据，并计算单据金额（价税合计之和）
const purchaseStore = usePurchaseStore()
const { items: purchaseItems } = storeToRefs(purchaseStore)
const documentAmountTotal = computed<number>(() => {
  return (purchaseItems.value as PurchaseItem[]).reduce((sum: number, item: PurchaseItem) => {
    const tpt = Number((item as PurchaseItem)?.tpt ?? 0)
    return sum + (Number.isFinite(tpt) ? tpt : 0)
  }, 0)
})

// 同步计算结果到表单模型，保持禁用输入框显示与表单数据一致
watch(documentAmountTotal, (total) => {
  // 合计为 0 时保持空；有值时保留两位小数
  if (!total) {
    formdata.documentCost = undefined
    formdata.documentBaseCost = undefined
    return
  }
  formdata.documentCost = Number(total.toFixed(2))
  formdata.documentBaseCost = Number(total.toFixed(2))
}, { immediate: true })

// 与供应商信息联动：根据关联人员或供应商信息构建 more 数据
const supplierMore = computed(() => {
  // 示例：根据 relatedPersonName 推导默认承运商/编号前缀
  const personName = formdata.relatedPersonName
  if (!personName) return []
  // 可以根据实际的人员名称进行匹配
  if (personName.includes('张三') || personName.includes('zhangsan')) {
    return ['默认承运商: 顺丰速运', '编号前缀: SF-']
  }
  if (personName.includes('李四') || personName.includes('lisi')) {
    return ['默认承运商: 中通快递', '编号前缀: ZTO-']
  }
  return []
})

/**
 * 处理人员搜索：从 PeopleList 组件接收搜索结果
 */
function handlePeopleSearch(params: any) {
  if (params && typeof params === 'object') {
    // 保存人员ID
    const personId = params.id || params.key || ''
    // 保存人员名称用于显示
    const personName = params.name || params.peopleName || ''
    formdata.relatedPerson = personId || ''
    formdata.relatedPersonName = personName || ''
  }
  showPeopleDialog.value = false
}

/**
 * 清除关联人员
 */
function handleClearRelatedPerson() {
  formdata.relatedPerson = ''
  formdata.relatedPersonName = ''
}

/**
 * 处理结算账户搜索：从 Account 组件接收搜索结果
 * 注意：Account 组件会在两种情况下触发 search 事件：
 * 1. 执行搜索时 - 传递搜索参数（name, number, data 等）
 * 2. 点击表格行选择账户时 - 传递账户对象（包含 id, name 等）
 */
function handleAccountSearch(params: any) {
  if (params && typeof params === 'object') {
    // 判断是否是选择账户（包含 id 字段）
    if (params.id) {
      // 保存账户ID（AccountListItem.id 是字符串类型，如 'ACC001'）
      // 如果后端需要数字类型，尝试转换；如果转换失败，保持字符串
      const accountIdStr = String(params.id)
      let accountId: string | number = accountIdStr
      
      // 尝试转换为数字（如果 id 是纯数字字符串）
      const accountIdNum = Number(accountIdStr)
      if (!isNaN(accountIdNum) && accountIdNum > 0) {
        accountId = accountIdNum
      }
      
      // 保存账户名称用于显示
      const accountName = params.name || params.accountName || ''
      
      if (accountId && accountName) {
        formdata.settlementAccount = accountId as any
        formdata.settlementAccountName = accountName
        // 选择账户后关闭对话框
        showAccountDialog.value = false
      }
    }
    // 如果是搜索操作（不包含 id），不关闭对话框，继续显示搜索结果
  }
}

/**
 * 清除结算账户
 */
function handleClearSettlementAccount() {
  formdata.settlementAccount = ''
  formdata.settlementAccountName = ''
}

// 暴露 formdata 给父组件使用
defineExpose({
  formdata
})
</script>

<style scoped>
.root-container {
  display: flex;
  flex-direction: column;
  gap: 0; /* 去掉内部间隔 */
  padding: 0;
  background-color: transparent;
}

.form-section {
  background-color: white;
  border-radius: 0 0 8px 8px; /* 只保留底部圆角，与上面的表格形成一体 */
  padding: 20px;
  padding-top: 10px; /* 减少顶部内边距 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-top: 1px solid #e4e7ed; /* 添加顶部分隔线 */
}

/* 顶部标题与按钮已移除 */

.table-section {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.table-section h3 {
  margin-bottom: 15px;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.table-actions {
  margin: 15px 0;
  display: flex;
  gap: 10px;
}

.total-section {
  margin-top: 20px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
}

.total-section .el-statistic {
  text-align: center;
}

/* 表单行间距 */
.el-row {
  margin-bottom: 15px;
}

.el-row:last-child {
  margin-bottom: 0;
}

/* 表单项样式 */
.el-form-item {
  margin-bottom: 0;
}

/* 上传组件样式 */
.upload-container {
  width: 100%;
}

.el-upload {
  width: 100%;
  display: block;
}

.upload-button {
  width: 100% !important;
  height: 32px !important;
  border-radius: 4px !important;
  font-size: 14px !important;
  padding: 0 12px !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
}

/* 让只读输入框外观接近禁用（灰色背景），但仍可选中内容 */
.readonly-like-disabled:deep(.el-input__wrapper) {
  background-color: var(--el-fill-color-light);
  box-shadow: 0 0 0 1px var(--el-border-color) inset;
  cursor: default;
}
.readonly-like-disabled:deep(.el-input__wrapper:hover),
.readonly-like-disabled:deep(.el-input__wrapper.is-focus),
.readonly-like-disabled:deep(.el-input__wrapper.is-focus:hover) {
  background-color: var(--el-fill-color-light);
  box-shadow: 0 0 0 1px var(--el-border-color) inset;
}
.readonly-like-disabled:deep(.el-input__inner) {
  color: var(--el-text-color-regular);
}
</style>
<style scoped>
/* Hide spinner arrows in number-like inputs across browsers */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  appearance: none;
  margin: 0;
}

input[type='number'] {
  -moz-appearance: textfield;
  appearance: textfield;
}
</style>


