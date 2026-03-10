<template>
  <el-dialog
    v-model="innerVisible"
    fullscreen
    :close-on-click-modal="false"
    class="pb-detail-dialog"
    title="采购订单详情"
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
        <!-- 审核通过时：显示 生成 + 反审核 -->
        <template v-if="isApproved">
          <el-dropdown>
            <el-button type="primary">
              生成
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="generateTo('purchaseOrder')">采购单</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

          <el-button class="ml8" @click="toggleApprove">反审核</el-button>
        </template>

        <!-- 未审核时：显示 生成 + 审核 -->
        <template v-else>
          <el-dropdown>
            <el-button type="primary">
              生成
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleGeneratePurchaseOrder">采购单</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

          <el-button class="ml8" @click="toggleApprove">审核</el-button>
        </template>
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
            prop="warenumber"
            label="入库数量"
            align="center"
            width="100"
          />

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

        <!-- 生成单据弹窗 -->
        <el-dialog
          v-model="showGeneratePopup"
          title="生成单据"
          width="300px"
          :close-on-click-modal="false"
        >
          <div class="generate-options">
            <el-button class="generate-option-btn" @click="handleGeneratePurchaseOrder">
              采购单
            </el-button>
          </div>
        </el-dialog>

        <!-- 你的商品选择弹窗（文件名：operationpoptable.vue） -->
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
      <DocumentForm ref="documentFormRef" :fields="purchaseBookFields" />
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="innerVisible = false">关闭</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { ArrowDown, Setting, Edit, Delete, Refresh } from '@element-plus/icons-vue'
import Supplier from '@/components/goodSearchConpoent/Supplier.vue'
import OperationPopTable from '@/components/operationpoptable/OperationPopTable.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { nextTick } from 'vue'
import DocumentForm from '@/components/documentform/DocumentForm.vue'
import { useRouter } from 'vue-router'
import Warehouse from '@/components/goodSearchConpoent/Warehouse.vue'
import type { PurchaseOrderDetailResponse, PurchaseOrderDetailItem, PurchaseOrderUpdateDTO } from '@/apis/purchase/PurchaseBooking/order'
import { PurchaseOrderAPI } from '@/apis/purchase/PurchaseBooking/order'
import type { ComponentPublicInstance } from 'vue'
import { usePurchaseStore } from '@/stores/purchase'

/* props & emits */
interface Props {
  visible: boolean
  record?: any
}
const props = defineProps<Props>()
const emit = defineEmits(['update:visible', 'save'])

const router = useRouter()

// 初始化 Pinia store
const purchaseStore = usePurchaseStore()

// DocumentForm 组件引用
const documentFormRef = ref<ComponentPublicInstance<{ formdata: any }> | null>(null)

/* v-model:visible */
const innerVisible = computed({
  get: () => props.visible,
  set: (v) => emit('update:visible', v)
})

/* 供应商组件 */
const supplierModel = reactive({
  name: '',
  id: '' // 保存供应商ID
})
const header = reactive({
  documentDate: '',
  documentNumber: ''
})

// 保存订单ID（用于更新）
const orderId = ref<string | number>('')

// 供应商选择对话框
const showSupplierDialog = ref(false)

// 处理供应商搜索
function handleSupplierSearch(params: any) {
  if (params && typeof params === 'object') {
    const name = params.name || params.supplierName || ''
    const id = params.id || params.key || params.value || ''
    supplierModel.name = name || ''
    supplierModel.id = id || ''
  }
  showSupplierDialog.value = false
}

// 清除供应商
function handleClearSupplier() {
  supplierModel.name = ''
  supplierModel.id = ''
}

/* 底部表单 */
const footer = reactive({
  documentCost: '16.95',
  actualAmount: '1',
  relatedPerson: 'NTJK_test_DD001',
  arrivalDate: '',
  logistics: '',
  remark: ''
})

// 定义表格行数据类型
interface TableRowData {
  productName: string
  productCode: string
  goodsId?: string // 商品ID（用于更新接口）
  spec: string
  attr: string
  unit: string
  warehouse: string // 仓库名称（用于显示）
  warehouseId?: string // 仓库ID（用于更新接口）
  price: string | number
  quantity: string | number
  warenumber: string | number
  amount: string | number
  taxRate: string | number
  taxAmount: string | number
  total: string | number
  remark: string
}

// ① 先定义表格数据
const tableData = ref<TableRowData[]>([
  {
    productName: '九九',
    productCode: '2233535',
    goodsId: '2233535',
    spec: '',
    attr: '',
    unit: '件',
    warehouse: '',
    warehouseId: '',
    price: 199,
    quantity: 1,
    warenumber: 1,
    amount: 199,
    taxRate: 13,
    taxAmount: 25.87,
    total: 224.87,
    remark: ''
  },
  {
    productName: '',
    productCode: '',
    goodsId: '',
    spec: '',
    attr: '',
    unit: '',
    warehouse: '',
    warehouseId: '',
    price: '',
    quantity: '',
    warenumber: '',
    amount: '',
    taxRate: '',
    taxAmount: '',
    total: '',
    remark: ''
  }
])

// ② 然后根据表格的行数，初始化一模一样长度的弹层可见性数组
const rowPopoverVisible = ref<boolean[]>(tableData.value.map(() => false))

// 当前选中的行索引
const currentWarehouseIndex = ref<number | null>(null)
// 批量弹层控制
const batchWarehouseVisible = ref(false)

// 商品弹窗控制
const showProductPopup = ref(false)
const currentRowIndex = ref<number | null>(null)

// 生成弹窗控制
const showGeneratePopup = ref(false)

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
  // 保存仓库名称（用于显示）
  row.warehouse = selected.name || selected.warehouseName || ''
  // 保存仓库ID（用于更新接口）
  row.warehouseId = selected.id || selected.key || selected.value || ''
  rowPopoverVisible.value[currentWarehouseIndex.value] = false
  ElMessage.success(`已选择仓库：${row.warehouse}`)
}

/** 批量选择确认 */
function handleBatchWarehouseSelect(selected: any) {
  if (!selected) return
  const selectedName = selected.name || selected.warehouseName || ''
  const selectedId = selected.id || selected.key || selected.value || ''
  tableData.value.forEach((r) => {
    if (r.productName) {
      r.warehouse = selectedName
      r.warehouseId = selectedId
    }
  })
  batchWarehouseVisible.value = false
  ElMessage.success(`已批量设置仓库：${selectedName}`)
}

/* 审核状态 */
const isApproved = ref(false)
const toggleApprove = () => {
  // 已审核 -> 反审核：切到未审核（显示"保存+审核"）
  // 未审核 -> 审核：切回已审核（显示"生成+反审核"）
  isApproved.value = !isApproved.value
}

/**
 * 将接口返回的商品数据转换为表格数据格式
 */
function convertDetailItemToTableRow(item: PurchaseOrderDetailItem): TableRowData {
  return {
    productName: item.name || '',
    productCode: item.goods || '', // 商品ID
    goodsId: item.goods || '', // 保存商品ID（用于更新）
    spec: item.spec || '',
    attr: item.attr || '',
    unit: item.unit || '',
    warehouse: item.warehouse || '', // 仓库ID或名称
    warehouseId: item.warehouse || '', // 保存仓库ID（用于更新）
    price: item.price ? String(item.price) : '',
    quantity: item.nums ? String(item.nums) : '',
    warenumber: item.handle ? String(item.handle) : '',
    amount: item.amount ? Number(item.amount) : 0,
    taxRate: item.tax ? String(item.tax) : '',
    taxAmount: item.tat ? Number(item.tat) : 0,
    total: item.tpt ? Number(item.tpt) : 0,
    remark: item.data || ''
  }
}

/**
 * 填充详情数据到表单和表格
 */
function fillDetailData(detailData: PurchaseOrderDetailResponse) {
  if (!detailData) return

  // 将详情数据转换为数组（处理单个对象或数组的情况）
  const items: PurchaseOrderDetailItem[] = Array.isArray(detailData) ? detailData : [detailData]

  if (items.length === 0) return

  // 获取第一个商品的其他详情信息（所有商品共享同一份订单信息）
  const firstItem = items[0]
  const otherDetail = firstItem.purchaseOrderOtherDetailDTO

  // 填充头部信息
  if (otherDetail) {
    header.documentDate = otherDetail.time || ''
    header.documentNumber = otherDetail.number || ''
    supplierModel.name = otherDetail.supplier || ''
    // 注意：otherDetail.supplier 可能是名称，需要从原始数据中获取ID
    // 如果接口返回的是ID，则直接使用；如果是名称，需要查找对应的ID
    // 这里假设接口返回的是名称，我们需要保存供应商ID（如果有的话）
    
    // 填充底部表单数据到 footer（用于保存时使用）
    footer.actualAmount = otherDetail.actual ? String(otherDetail.actual) : ''
    footer.arrivalDate = otherDetail.arrivalTime || ''
    footer.logistics = otherDetail.logistics || ''
    footer.remark = otherDetail.data || ''
    footer.documentCost = otherDetail.total ? String(otherDetail.total) : ''
    footer.relatedPerson = otherDetail.people || ''
    
    // 填充底部表单（DocumentForm 组件）
    nextTick(() => {
      if (documentFormRef.value?.formdata) {
        const formdata = documentFormRef.value.formdata
        if (otherDetail.actual !== undefined && otherDetail.actual !== null) {
          formdata.actualAmount = Number(otherDetail.actual)
        }
        if (otherDetail.arrivalTime) {
          formdata.arrivalDate = otherDetail.arrivalTime
        }
        if (otherDetail.logistics) {
          // logistics 可能是字符串或对象，需要根据实际情况处理
          formdata.logistics = typeof otherDetail.logistics === 'string' 
            ? { key: 'auto', name: '自动识别', number: otherDetail.logistics }
            : otherDetail.logistics
        }
        if (otherDetail.data) {
          formdata.remarks = otherDetail.data
        }
        if (otherDetail.total !== undefined && otherDetail.total !== null) {
          formdata.documentCost = Number(otherDetail.total)
          formdata.documentBaseCost = Number(otherDetail.total)
        }
        if (otherDetail.people) {
          formdata.relatedPerson = otherDetail.people
          formdata.relatedPersonName = otherDetail.people // 如果没有名称，使用ID作为显示
        }
      }
    })
  }

  // 填充表格数据
  const rows = items.map(convertDetailItemToTableRow)
  
  // 确保最后一行始终为空行（用于添加新商品）
  tableData.value = [...rows, {
    productName: '',
    productCode: '',
    goodsId: '',
    spec: '',
    attr: '',
    unit: '',
    warehouse: '',
    warehouseId: '',
    price: '',
    quantity: '',
    warenumber: '',
    amount: '',
    taxRate: '',
    taxAmount: '',
    total: '',
    remark: ''
  }]

  // 同步更新弹层可见性数组
  rowPopoverVisible.value = tableData.value.map(() => false)
}

// 监听 record 变化，当有详情数据时填充
watch(
  () => props.record,
  (newRecord) => {
    if (newRecord && newRecord.detailData) {
      // 保存订单ID
      if (newRecord.id) {
        orderId.value = newRecord.id
      }
      
      fillDetailData(newRecord.detailData)
      
      // 如果有审核状态，设置审核状态
      if (newRecord.auditStatus) {
        const status = String(newRecord.auditStatus)
        isApproved.value = status === '已审核' || status === '1' || status === 'true'
      }
    } else if (newRecord && !newRecord.detailData) {
      // 如果没有详情数据，重置表单（可能是新建）
      resetForm()
    }
  },
  { immediate: true, deep: true }
)

// 监听对话框显示状态，关闭时重置表单
watch(
  () => props.visible,
  (visible) => {
    if (!visible) {
      // 对话框关闭时，延迟重置表单（避免关闭动画时数据闪烁）
      setTimeout(() => {
        resetForm()
      }, 300)
    }
  }
)

/**
 * 重置表单数据
 */
function resetForm() {
  supplierModel.name = ''
  supplierModel.id = ''
  header.documentDate = ''
  header.documentNumber = ''
  footer.documentCost = ''
  footer.actualAmount = ''
  footer.relatedPerson = ''
  footer.arrivalDate = ''
  footer.logistics = ''
  footer.remark = ''
  isApproved.value = false
  orderId.value = ''
  
  // 重置表格数据为初始状态（一行空行）
  tableData.value = [{
    productName: '',
    productCode: '',
    goodsId: '',
    spec: '',
    attr: '',
    unit: '',
    warehouse: '',
    warehouseId: '',
    price: '',
    quantity: '',
    warenumber: '',
    amount: '',
    taxRate: '',
    taxAmount: '',
    total: '',
    remark: ''
  }]
  
  rowPopoverVisible.value = [false]
}

/* 生成下拉：携带数据并跳转（已审核状态下使用） */
function generateTo(type: 'purchaseOrder') {
  handleGeneratePurchaseOrder()
}

/**
 * 生成采购单处理函数（调用接口获取数据）
 */
async function handleGeneratePurchaseOrder() {
  console.log('🚀 [handleGeneratePurchaseOrder] 开始执行')

  // 检查订单ID
  if (!orderId.value) {
    ElMessage.error('订单ID不能为空，请先保存订单')
    return
  }

  // 检查前端是否有明细行数据
  const validRows = tableData.value.filter((r) => r.productName && r.goodsId)
  if (validRows.length === 0) {
    ElMessage.warning('订单中没有商品明细，请先添加商品明细后再生成采购单')
    return
  }

  // 检查订单是否已审核（只有审核通过的订单才能生成采购单）
  if (!isApproved.value) {
    ElMessage.warning('订单尚未审核，请先审核订单后再生成采购单')
    return
  }

  // 提示：如果前端有未保存的明细行数据，建议先保存
  // 注意：这里不强制保存，因为可能用户已经保存过了，只是前端数据有变化
  // 如果后端返回"明细行不存在"错误，会在 catch 中提示用户

  try {
    // 调用接口获取生成采购单数据
    const response = await PurchaseOrderAPI.getGeneratePurchaseData(orderId.value)
    
    if (response.code === 10000 && response.data) {
      console.log('📥 获取到生成采购单数据:', response.data)
      
      // 将接口返回的数据转换为传输格式
      // 注意：接口返回的 data 可能是数组或单个对象
      const generateDataList = Array.isArray(response.data) ? response.data : [response.data]
      
      // 获取第一个商品的其他信息（所有商品共享同一份订单信息）
      const firstItem = generateDataList[0]
      const otherData = firstItem?.purchaseNoteOtherGenerateDataDTO
      
      // 将生成数据转换为表格数据格式
      const transferTableData = generateDataList.map((item) => ({
        key: item.goods || null,
        name: item.name || '',
        number: item.goods || '',
        spec: item.spec || '',
        attr: item.attr || '',
        unit: item.unit || '',
        warehouse: item.warehouse || '',
        warehouseId: item.warehouse || null,
        price: Number(item.price) || 0,
        nums: Number(item.nums) || 0,
        handle: 0,
        discount: 0,
        dsc: 0,
        total: Number(item.amount) || 0,
        tax: Number(item.tax) || 0,
        tat: Number(item.tat) || 0,
        tpt: Number(item.tpt) || 0,
        data: item.data || '',
        goodsType: 0
      }))

      // 使用 Pinia Store 存储传输数据
      const transferData = {
        tableData: transferTableData,
        supplier: otherData?.supplier || supplierModel.name || '',
        documentDate: otherData?.time || header.documentDate || '',
        documentNumber: otherData?.number || header.documentNumber || '',
        from: 'purchase-book-detail'
      }
      console.log('📦 准备传输的数据:', transferData)

      // 检查方法是否存在
      if (typeof purchaseStore.setTransferData === 'function') {
        purchaseStore.setTransferData(transferData)
        console.log('✅ 传输数据已存储到 Store')
      } else {
        console.error('❌ setTransferData 方法不存在，使用备用方案')
        // 备用方案：直接设置 state（使用类型断言避免类型检查错误）
        purchaseStore.$patch({
          transferData: {
            ...transferData,
            timestamp: Date.now()
          } as any
        })
        console.log('✅ 使用 $patch 存储传输数据')
      }

      // 跳转到采购单页面
      const targetRoute = {
        path: '/purchase-order',
        query: {
          from: 'purchase-booking',
          dataTransfer: 'true'
        }
      }
      console.log('🎯 准备跳转到:', targetRoute)

      router
        .push(targetRoute)
        .then(() => {
          console.log('✅ 路由跳转成功')
          if (transferTableData.length === 0) {
            ElMessage.info('正在跳转到采购单页面（无商品数据）')
          } else {
            ElMessage.success(`正在跳转到采购单页面，共 ${transferTableData.length} 条数据`)
          }
        })
        .catch((error: any) => {
          console.error('❌ 路由跳转失败:', error)
          ElMessage.error('路由跳转失败，请检查路由配置')
        })
    } else {
      // 优先显示 data 字段的错误信息（如果 data 是字符串）
      const errorData = response.data
      const errorMessage = typeof errorData === 'string' && errorData 
        ? errorData 
        : (response.message || '获取生成采购单数据失败')
      ElMessage.error(errorMessage)
      
      // 如果是明细行不存在的错误，给出更详细的提示
      if (typeof errorData === 'string' && (errorData as string).includes('明细行')) {
        console.warn('⚠️ 订单没有明细行数据，请先添加商品明细后再生成采购单')
      }
    }
  } catch (error: any) {
    console.error('❌ 获取生成采购单数据失败:', error)
    
    // 处理不同类型的错误响应
    // 注意：响应拦截器 reject 的是整个 response 对象，所以 error 可能是 response 对象本身
    let errorMessage = '获取生成采购单数据失败，请稍后重试'
    
    // 情况1：error 是被 reject 的 response 对象（响应拦截器 reject 的情况）
    if (error?.data) {
      const errorData = error.data
      // 如果 error.data 是响应体对象，包含 code、message、data 字段
      if (errorData && typeof errorData === 'object' && 'code' in errorData) {
        // 优先使用 data.data 中的错误信息（如果它是字符串且不为空）
        if (typeof errorData.data === 'string' && errorData.data.trim()) {
          errorMessage = errorData.data
        } else if (errorData.message) {
          errorMessage = errorData.message
        }
      } else if (typeof errorData === 'string' && errorData) {
        // 如果 error.data 直接是字符串
        errorMessage = errorData
      }
    } 
    // 情况2：error 是标准的 AxiosError 对象（网络错误等情况）
    else if (error?.response?.data) {
      const errorData = error.response.data
      // 如果 data 是字符串，优先使用它
      if (typeof errorData === 'string' && errorData) {
        errorMessage = errorData
      } else if (errorData && typeof errorData === 'object' && 'code' in errorData) {
        // 如果 data 是响应体对象
        if (typeof errorData.data === 'string' && errorData.data.trim()) {
          errorMessage = errorData.data
        } else if (errorData.message) {
          errorMessage = errorData.message
        }
      }
    } 
    // 情况3：其他错误
    else if (error?.message) {
      errorMessage = error.message
    }
    
    // 注意：响应拦截器已经显示了错误信息，这里不再重复显示
    // 只在响应拦截器没有处理的情况下才显示（比如网络错误）
    // 如果错误已经被响应拦截器处理过（code 9994），则不再显示
    if (error?.data?.code !== 9994) {
      ElMessage.error(errorMessage)
    }
    
    // 如果是明细行不存在的错误，给出更详细的提示和解决方案
    if (typeof errorMessage === 'string' && errorMessage.includes('明细行')) {
      console.warn('⚠️ 订单没有明细行数据，请先添加商品明细后再生成采购单')
      // 给出更明确的解决方案提示
      ElMessageBox.confirm(
        '后端数据库中找不到该订单的明细行数据。可能的原因：\n' +
        '1. 订单明细尚未保存到后端\n' +
        '2. 订单明细已被删除\n\n' +
        '解决方案：\n' +
        '1. 如果前端有商品明细，请先点击"保存"按钮保存订单\n' +
        '2. 如果订单已保存，请检查订单明细是否正确',
        '明细行不存在',
        {
          confirmButtonText: '我知道了',
          type: 'warning',
          showCancelButton: false
        }
      ).catch(() => {})
    }
  }

  console.log('🏁 [handleGeneratePurchaseOrder] 执行完成')
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

function onEditableInput(row: TableRowData, field: EditableField, e: Event) {
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

function onEditableBlur(row: TableRowData, field: EditableField, e: Event) {
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

// 页面加载时获取仓库数据
onMounted(() => {
  loadWarehouseList()
})

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
  warenumber: '入库数量',
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
  tableData.value.forEach((r) => {
    if (r.productName) r.taxRate = Number(batchTax.value)
    recalculate(r)
  })
  showTaxBatch.value = false
  ElMessage.success('批量设置税率完成')
}

// ====== 金额/税额/合计 计算 ======
function recalculate(row: TableRowData) {
  const price = parseFloat(String(row.price)) || 0
  const qty = parseFloat(String(row.quantity)) || 0
  const rate = parseFloat(String(row.taxRate)) || 0

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
  row.productCode = first.code || ''
  row.goodsId = first.id || first.key || first.value || first.code || '' // 保存商品ID
  row.spec = first.model || ''
  row.attr = first.category || ''
  row.unit = first.unit || ''
  row.warehouse = row.warehouse || '' // 不覆盖已有仓库
  row.warehouseId = row.warehouseId || '' // 不覆盖已有仓库ID
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
      goodsId: '',
      spec: '',
      attr: '',
      unit: '',
      warehouse: '',
      warehouseId: '',
      price: '',
      quantity: '',
      warenumber: '',
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

  // 👇 同步把这一行的 popover 状态也删掉
  rowPopoverVisible.value.splice(index, 1)

  ElMessage.success('已删除该行')
}

const purchaseBookFields = [
  'documentCost', // 单据金额
  'actualAmount', // 实际金额
  'relatedPerson', // 关联人员
  'arrivalDate', // 到货日期
  'logistics', // 物流信息
  'documentAttachment', // 单据附件
  'remarks' // 备注信息
]

// 总条数和总合计（状态栏数据）
const totalCount = computed(
  () => tableData.value.filter((r) => r.productName && Number(r.quantity) > 0).length
)

const totalSum = computed(() => {
  const sum = tableData.value
    // 只统计有商品的行
    .filter((r) => r.productName)
    // 累加每一行的价税合计
    .reduce((acc, r) => acc + (Number(r.total) || 0), 0)

  return sum.toFixed(2)
})

/**
 * 将表格行数据转换为接口格式
 */
function convertTableRowToOrderInfo(row: TableRowData): any {
  return {
    goods: row.goodsId || row.productCode || '', // 商品ID
    warehouse: row.warehouseId || row.warehouse || '', // 仓库ID
    unit: row.unit || '',
    nums: parseFloat(String(row.quantity)) || 0,
    price: parseFloat(String(row.price)) || 0,
    total: parseFloat(String(row.amount)) || 0,
    tpt: parseFloat(String(row.total)) || 0,
    tax: parseFloat(String(row.taxRate)) || 0,
    tat: parseFloat(String(row.taxAmount)) || 0,
    attr: row.attr || '',
    data: row.remark || '',
    discount: 0,
    dsc: 0
  }
}

/* 保存 */
const save = async () => {
  try {
    // 验证必填字段
    if (!header.documentNumber) {
      ElMessage.error('单据编号不能为空')
      return
    }
    if (!supplierModel.name) {
      ElMessage.error('供应商不能为空')
      return
    }
    if (!header.documentDate) {
      ElMessage.error('单据日期不能为空')
      return
    }

    // 获取底部表单数据
    const formdata = documentFormRef.value?.formdata
    if (!formdata) {
      ElMessage.error('表单数据获取失败')
      return
    }

    // 验证实际金额
    if (formdata.actualAmount === undefined || formdata.actualAmount === null) {
      ElMessage.error('实际金额不能为空')
      return
    }

    // 验证到货日期
    if (!formdata.arrivalDate) {
      ElMessage.error('到货日期不能为空')
      return
    }

    // 验证关联人员
    if (!formdata.relatedPerson) {
      ElMessage.error('关联人员不能为空')
      return
    }

    // 获取有效商品行（过滤掉空行）
    const validRows = tableData.value.filter((r) => r.productName && r.goodsId)
    
    if (validRows.length === 0) {
      ElMessage.error('请至少添加一条商品数据')
      return
    }

    // 验证每行的必填字段
    for (let i = 0; i < validRows.length; i++) {
      const row = validRows[i]
      if (!row.goodsId && !row.productCode) {
        ElMessage.error(`第 ${i + 1} 行的商品ID不能为空`)
        return
      }
      if (!row.warehouseId && !row.warehouse) {
        ElMessage.error(`第 ${i + 1} 行的仓库不能为空`)
        return
      }
      if (!row.unit) {
        ElMessage.error(`第 ${i + 1} 行的单位不能为空`)
        return
      }
    }

    // 计算单据金额（价税合计之和）
    const total = validRows.reduce((sum, r) => sum + (Number(r.total) || 0), 0)

    // 构建更新数据
    const updateData: PurchaseOrderUpdateDTO = {
      id: orderId.value ? String(orderId.value) : undefined,
      number: header.documentNumber,
      supplier: supplierModel.id || supplierModel.name, // 优先使用ID，如果没有则使用名称
      time: header.documentDate,
      arrival: formdata.arrivalDate,
      actual: Number(formdata.actualAmount) || 0,
      total: total,
      people: formdata.relatedPerson,
      data: formdata.remarks || '',
      logistics: typeof formdata.logistics === 'object' 
        ? formdata.logistics?.number || formdata.logistics?.name || '' 
        : formdata.logistics || '',
      file: '', // 单据附件，需要根据实际情况处理
      purchaseOrderInfoDTOList: validRows.map(convertTableRowToOrderInfo)
    }

    // 调用更新接口
    const response = await PurchaseOrderAPI.updatePurchaseOrder(updateData)
    
    if (response.code === 10000) {
      ElMessage.success('订单修改成功')
      // 触发保存事件，让父组件刷新列表
      emit('save', {
        header,
        supplier: { name: supplierModel.name, id: supplierModel.id },
        tableData: tableData.value,
        footer,
        isApproved: isApproved.value
      })
      // 关闭对话框
      innerVisible.value = false
    } else {
      ElMessage.error(response.message || '订单修改失败')
    }
  } catch (error: any) {
    console.error('保存订单失败:', error)
    const errorMessage = error?.message || error?.response?.data?.message || '订单修改失败，请稍后重试'
    ElMessage.error(errorMessage)
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
/* 单个表单项 */
.form-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

/* ===== 弹窗整体布局 ===== */
.pb-detail-dialog :deep(.el-dialog__body) {
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

/* 表格底部统计栏 */
.table-footer {
  text-align: right;
  padding: 6px 16px;
  border-top: 1px solid #ebeef5;
  background: #f9fafb;
  font-size: 13px;
  color: #606266;
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
.footer-form,
:deep(.document-form) {
  flex-shrink: 0;
  border-top: 1px solid #ebeef5;
  padding: 12px 16px;
  background: #f9fafb;
}

.footer-form .row {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 12px;
}
.footer-form .cell--wide {
  grid-column: 1 / -1;
}
/* 统一输入框高度 */
.footer-form :deep(.el-input__wrapper) {
  height: 36px;
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
  transform: scale(0.8) rotate(-15deg);
  opacity: 0;
}

@keyframes stamp-appear {
  from {
    transform: scale(0.8) rotate(-15deg);
    opacity: 0;
  }
  to {
    transform: scale(1) rotate(-15deg);
    opacity: 1;
  }
}

/* 底部按钮 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  padding: 10px 0;
  gap: 10px;
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
</style>