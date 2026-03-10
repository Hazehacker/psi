<template>
  <el-dialog
    :model-value="visible"
    @update:model-value="handleDialogUpdate"
    :title="dialogTitle"
    width="600px"
    :close-on-click-modal="false"
  >
    <el-form :model="form" label-width="100px" ref="formRef" :rules="rules">
      <!-- 显示合计金额信息 -->
      <div class="amount-summary">
        <div class="amount-item">
          <span class="amount-label">单据总金额：</span>
          <span class="amount-value">{{ formatCurrency(totalBillAmount) }}</span>
        </div>
        <div class="amount-item">
          <span class="amount-label">本次开票金额：</span>
          <span class="amount-value primary">{{ formatCurrency(totalInvoiceAmount) }}</span>
        </div>
      </div>

      <el-divider />

      <el-form-item label="开票时间" prop="invoiceDate">
        <el-date-picker
          v-model="form.invoiceDate"
          type="date"
          placeholder="选择开票时间"
          value-format="YYYY-MM-DD"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="发票号码" prop="invoiceNumber">
        <el-input v-model="form.invoiceNumber" placeholder="请输入发票号码" />
      </el-form-item>

      <el-form-item label="发票抬头" prop="invoiceTitle">
        <el-input v-model="form.invoiceTitle" placeholder="请输入发票抬头" />
      </el-form-item>

      <el-form-item label="发票附件" prop="attachment">
        <el-upload
          action="#"
          :auto-upload="false"
          :on-change="handleFileChange"
          :file-list="fileList"
          :before-upload="beforeUpload"
        >
          <el-button type="primary">点击上传</el-button>
          <template #tip>
            <div class="el-upload__tip">支持PDF、JPG、PNG格式文件，大小不超过10MB</div>
          </template>
        </el-upload>
      </el-form-item>

      <el-form-item label="备注信息" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
      </el-form-item>

      <!-- 选中的单据列表 -->
      <el-form-item label="开票单据">
        <div class="selected-records">
          <div v-for="(record, index) in selectedRecords" :key="index" class="record-item">
            <div class="record-info">
              <span class="record-type">{{ getOrderTypeDisplayName(record.type) }}</span>
              <span class="record-number">{{ record.number }}</span>
              <span class="record-amount">{{ formatCurrency(getRecordAmount(record)) }}</span>
            </div>
          </div>
        </div>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading" :disabled="!isFormValid">
        {{ isViewMode ? '确定' : '开票' }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'
import type { UploadFile } from 'element-plus'
import { addInvoice } from '@/apis/finance/invoice'
import type { InvoiceAddRequest, InvoiceItem } from '@/apis/finance/invoice/type'

interface InvoiceForm {
  invoiceDate: string
  invoiceNumber: string
  invoiceTitle: string
  attachment?: File
  remark: string
}

interface Props {
  visible: boolean
  selectedRecords: any[]
  isViewMode?: boolean // 是否为查看模式
}

const props = withDefaults(defineProps<Props>(), {
  isViewMode: false
})

const emit = defineEmits(['update:visible', 'invoice-success'])

const formRef = ref()
const loading = ref(false)
const fileList = ref<UploadFile[]>([])

const form = reactive<InvoiceForm>({
  invoiceDate: '',
  invoiceNumber: '',
  invoiceTitle: '',
  remark: ''
})

const rules = {
  invoiceDate: [{ required: true, message: '请选择开票时间', trigger: 'change' }],
  invoiceNumber: [{ required: true, message: '请输入发票号码', trigger: 'blur' }],
  invoiceTitle: [{ required: true, message: '请输入发票抬头', trigger: 'blur' }]
}

// 计算属性
const dialogTitle = computed(() => {
  return props.isViewMode ? '发票详情' : '发票开具'
})

const totalBillAmount = computed(() => {
  return props.selectedRecords.reduce((sum, record) => {
    return sum + (getRecordBillAmount(record) || 0)
  }, 0)
})

const totalInvoiceAmount = computed(() => {
  return props.selectedRecords.reduce((sum, record) => {
    return sum + (getRecordAmount(record) || 0)
  }, 0)
})

const isFormValid = computed(() => {
  return form.invoiceDate && form.invoiceNumber && form.invoiceTitle
})

// 获取单据金额（用于开票）
const getRecordAmount = (record: any): number => {
  // 如果是查看模式，显示发票金额；如果是开票模式，显示未开票金额
  if (props.isViewMode) {
    return record.invMoney || record.invoiceAmount || 0
  } else {
    return record.disinvoicedCount || record.uninvoicedAmount || 0
  }
}

// 获取单据原始金额
const getRecordBillAmount = (record: any): number => {
  return record.totalCount || record.billAmount || record.amount || 0
}

// 处理对话框显示状态更新
const handleDialogUpdate = (value: boolean) => {
  emit('update:visible', value)
}

// 监听可见性变化
watch(
  () => props.visible,
  (val) => {
    if (val) {
      // 重置表单
      resetForm()

      // 如果是查看模式，填充现有数据
      if (props.isViewMode && props.selectedRecords.length > 0) {
        const firstRecord = props.selectedRecords[0]
        form.invoiceDate = firstRecord.invTime || ''
        form.invoiceNumber = firstRecord.invNumber || ''
        form.invoiceTitle = firstRecord.title || ''
        form.remark = firstRecord.data || ''
      } else {
        // 开票模式：设置默认开票时间为今天
        const today = new Date()
        form.invoiceDate = today.toISOString().split('T')[0]
      }
    }
  }
)

// 重置表单
const resetForm = () => {
  form.invoiceDate = ''
  form.invoiceNumber = ''
  form.invoiceTitle = ''
  form.remark = ''
  fileList.value = []
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

const handleFileChange = (file: UploadFile) => {
  form.attachment = file.raw
}

const beforeUpload = (file: File) => {
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过 10MB!')
    return false
  }
  return false // 返回false，手动上传
}

const handleClose = () => {
  emit('update:visible', false)
}

const handleConfirm = async () => {
  if (!formRef.value) return

  try {
    const valid = await formRef.value.validate()
    if (!valid) return

    // 如果是查看模式，直接关闭对话框
    if (props.isViewMode) {
      handleClose()
      return
    }

    loading.value = true

    // 构建开票请求数据
    const invoiceData: InvoiceAddRequest = {
      data: form.remark,
      number: form.invoiceNumber,
      time: form.invoiceDate + ' 00:00:00',
      title: form.invoiceTitle,
      infos: props.selectedRecords.map((record) => ({
        type: mapOrderTypeToApiType(record.type),
        id: record.id || record.number,
        amount: getRecordAmount(record)
      }))
    }

    // 处理附件
    if (form.attachment) {
      // 这里需要根据实际需求处理文件上传
      // 假设文件上传后返回URL，这里简化处理
      invoiceData.file = JSON.stringify({
        urls: ['https://example.com/uploaded-file.pdf']
      })
    }

    // 调用开票API
    addInvoice(
      invoiceData,
      (res) => {
        ElMessage.success('开票成功')
        emit('invoice-success')
        handleClose()
      },
      (err) => {
        console.error('开票失败:', err)
        ElMessage.error('开票失败：' + (err.message || '未知错误'))
      }
    )
  } catch (error) {
    console.error('开票失败:', error)
    ElMessage.error('开票失败')
  } finally {
    loading.value = false
  }
}

const formatCurrency = (value: number) => {
  return (
    '¥' +
    value.toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    })
  )
}

// 映射订单类型
const mapOrderTypeToApiType = (orderType: string): 'buy' | 'bre' | 'sell' | 'sre' => {
  const typeMap: Record<string, 'buy' | 'bre' | 'sell' | 'sre'> = {
    采购单: 'buy',
    采购退货单: 'bre',
    销售单: 'sell',
    销售退货单: 'sre',
    buy: 'buy',
    bre: 'bre',
    sell: 'sell',
    sre: 'sre'
  }
  return typeMap[orderType] || 'buy'
}

const getOrderTypeDisplayName = (type: string): string => {
  const typeMap: Record<string, string> = {
    buy: '采购单',
    bre: '采购退货单',
    sell: '销售单',
    sre: '销售退货单'
  }
  return typeMap[type] || type
}
</script>

<style scoped>
.amount-summary {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 6px;
  margin-bottom: 16px;
}

.amount-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.amount-item:last-child {
  margin-bottom: 0;
}

.amount-label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

.amount-value {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.amount-value.primary {
  color: #409eff;
}

.selected-records {
  max-height: 200px;
  overflow-y: auto;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 8px;
}

.record-item {
  padding: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.record-item:last-child {
  border-bottom: none;
}

.record-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.record-type {
  font-size: 12px;
  color: #909399;
  min-width: 80px;
}

.record-number {
  font-size: 14px;
  color: #303133;
  flex: 1;
  margin: 0 12px;
}

.record-amount {
  font-size: 14px;
  font-weight: 600;
  color: #409eff;
  min-width: 100px;
  text-align: right;
}

/* 美化滚动条 */
.selected-records::-webkit-scrollbar {
  width: 6px;
}

.selected-records::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.selected-records::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.selected-records::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

:deep(.el-upload__tip) {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
</style>
