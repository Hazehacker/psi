<template>
  <div class="page-container">
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

          <!-- 已审核状态：显示生成、反审核和刷新 -->
          <ButtonGroup v-else align="right" gap="0px">
            <BusinessButton action-type="generate" @action="handleGenerate" />
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
          <el-table-column v-if="visibleColumns.unit" prop="unit" label="单位" align="center" />
          <el-table-column v-if="visibleColumns.attr" prop="attr" label="辅助属性" align="center" />

          <!-- 原仓库列（删除） -->
          <!-- <el-table-column
  v-if="visibleColumns.warehouse"
  prop="warehouse"
  label="仓库"
  align="center"
/> -->

          <!-- 新仓库列（支持表头批量选择+单元格下拉） -->
          <el-table-column v-if="visibleColumns.warehouse" label="仓库" align="center">
            <template #header>
              <div class="flex-center">
                <!-- 表头批量选择弹窗 -->
                <el-popover
                  trigger="click"
                  placement="bottom-start"
                  v-model:visible="warehousePopVisible"
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
                contenteditable="true"
                class="editable-cell"
                @input="onEditableInput(row, 'cost', $event)"
                @blur="onEditableBlur(row, 'cost', $event)"
              >
                {{ row.cost || '' }}
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
    <DocumentForm :fields="otherOutboundFields" />

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
</template>

<script setup lang="ts">
import DocumentForm from '@/components/documentform/DocumentForm.vue'
import OperationPopTable from '@/components/operationpoptable/OperationPopTable.vue'
import ButtonGroup from '@/components/mybutton/ButtonGroup.vue'
import BusinessButton from '@/components/mybutton/BusinessButton.vue'
import { ref, reactive, computed, onMounted, getCurrentInstance, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  Check,
  Refresh,
  Search,
  Setting,
  Edit,
  Delete,
  MoreFilled,
  ArrowDown
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { nextTick } from 'vue'
import Customer from '@/components/goodSearchConpoent/Customer.vue'
// 仓库弹窗显隐控制（表头批量选择用）
const warehousePopVisible = ref(false)
// 仓库选项数据（与调拨单一致的仓库列表）
const warehouseOptions = ref<Array<{ value: string; label: string }>>([])
// 批量选择仓库的值
const batchWarehouseValue = ref('')
/* 客户组件 */
const customerModel = ref({})
const headerForm = reactive({
  documentDate: new Date().toISOString().split('T')[0],
  documentNumber: generateBillNo()
})
// 客户下拉选择器核心变量（API请求版）
const customerDropdownVisible = ref(false) // 下拉面板显隐
const selectedCustomerName = ref('') // 选中的客户名称
const customerSearchKey = ref('') // 搜索关键词
interface Customer {
  id: number
  name: string
}
const customerList = ref<Customer[]>([]) // 客户列表（当前页数据）
const customerTotal = ref(0) // 客户总条数
const customerPage = ref(1) // 当前页码
const customerPageSize = ref(10) // 每页条数

// 调用API获取客户列表（带搜索+分页）
const fetchCustomers = async () => {
  try {
    // 构造请求参数（和后端接口对齐，可根据实际调整参数名）
    const params = {
      page: customerPage.value,
      pageSize: customerPageSize.value,
      name: customerSearchKey.value.trim() // 搜索关键词
      // 若后端需要其他参数（如状态），可在此添加
    }

    // 调用客户列表API（替换为你的真实接口地址）
    const res = await axios.get('/api/customers', { params })

    // 假设接口返回格式：{ code: 200, data: { list: [], total: 0 } }
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

// 选择客户（同步到原customerModel）
const selectCustomer = (customer: any) => {
  selectedCustomerName.value = customer.name
  customerModel.value = customer // 保持和原有表单逻辑一致
  customerDropdownVisible.value = false
}

// 清空选中的客户
const handleCustomerClear = () => {
  selectedCustomerName.value = ''
  customerModel.value = {}
}

// 分页页码切换
const handleCustomerPageChange = (page: number) => {
  customerPage.value = page
  fetchCustomers()
}

// 每页条数切换
const handleCustomerSizeChange = (size: number) => {
  customerPageSize.value = size
  customerPage.value = 1 // 切换页大小时重置到第一页
  fetchCustomers()
}

// 页面挂载时加载第一页客户数据
onMounted(() => {
  fetchCustomers()

  // 点击页面其他区域关闭下拉面板
  document.addEventListener('click', (e) => {
    const target = e.target as HTMLElement
    if (!target.closest('.customer-select')) {
      customerDropdownVisible.value = false
    }
  })
})
// 加载仓库列表（与调拨单逻辑完全一致）
async function loadWarehouseList() {
  try {
    // 实际项目中替换为真实接口（需与调拨单使用的仓库接口一致）
    /*
    const { data } = await axios.get('/api/warehouse/list')
    if (Array.isArray(data)) {
      warehouseOptions.value = data.map((item: any) => ({
        value: item.id || item.name,
        label: item.name || item
      }))
    } else if (data?.data) {
      warehouseOptions.value = data.data.map((item: any) => ({
        value: item.id || item.name,
        label: item.name || item
      }))
    }
    */

    // 模拟数据（与调拨单保持一致）
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

// 批量设置仓库（表头批量选择后执行）
function handleBatchWarehouseSelect(value: string) {
  if (!value) return
  const selectedWarehouse = warehouseOptions.value.find((w) => w.value === value)
  if (selectedWarehouse) {
    // 只给有商品名称的行设置仓库（避免空行被赋值）
    tableData.value.forEach((r) => {
      if (r.productName) r.warehouse = selectedWarehouse.label
    })
    ElMessage.success(`已批量设置仓库：${selectedWarehouse.label}`)
  }
  batchWarehouseValue.value = '' // 清空选择
  warehousePopVisible.value = false // 关闭弹窗
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

type EditableField = 'cost' | 'quantity' | 'remark'

function onEditableInput(row: any, field: EditableField, e: Event) {
  const el = e.target as HTMLElement | null
  if (!el) return

  const caret = getCaretPosition(el)
  const raw = el.innerText ?? ''

  if (field === 'remark') {
    row.remark = raw
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
    .reduce((sum, r) => sum + (Number(r.totalCost) || 0), 0)
    .toFixed(2)
})

// 仓库选择功能已简化，不再需要handleWarehouseChange函数

// 表格数据
const tableData = ref([
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
])

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

  // 计算总成本
  row.totalCost = +(cost * qty).toFixed(2)

  // 计算完后立即刷新状态栏
  updateStatusBar()
}

// === 状态栏更新 ===
function updateStatusBar() {
  // 重新计算总条数和总合计
  const validRows = tableData.value.filter((r) => r.productName)
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
  const row = tableData.value[currentRowIndex.value]

  // 适配商品结构字段名
  row.productName = first.name
  row.productCode = first.code
  row.spec = first.model || ''
  row.attr = first.category || ''
  row.unit = first.unit || ''
  row.warehouse = row.warehouse || '' // 不覆盖已有仓库
  row.cost = first.purchasePrice ?? ''
  row.quantity = first.quantity ?? ''

  recalculate(row)
  showProductPopup.value = false

  // 保证最后一行始终为空行
  const last = tableData.value[tableData.value.length - 1]
  if (last && last.productName) {
    tableData.value.push({
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
  tableData.value.splice(index, 1)
  // 同步把这一行的 popover 状态也删掉

  ElMessage.success('已删除该行')
}

// 商品弹窗控制
const showProductPopup = ref(false)
const currentRowIndex = ref<number | null>(null)

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

// 获取当前实例和路由
const instance = getCurrentInstance()
const router = useRouter()
const route = useRoute()

// 审核状态管理
const isAudited = ref(false)

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
  return `QTCKD${year}${month}${day}${random}`
}

/**
 * 保存按钮处理函数
 */
function handleSave() {
  console.log('保存数据')
  console.log('头部表单数据:', headerForm)

  // 验证必填项
  const hasInvalidRow = tableData.value.some((row) => {
    return row.productName && (!row.warehouse || !row.cost || !row.quantity)
  })

  if (hasInvalidRow) {
    ElMessage.warning('请填写所有必填字段')
    return
  }

  // 构造保存数据
  const saveData = {
    headerForm: {
      customer: customerModel.value,
      documentDate: headerForm.documentDate,
      documentNumber: headerForm.documentNumber
    },
    tableData: tableData.value.filter((row) => row.productName), // 只保存有商品数据的行
    isAudited: true
  }

  console.log('已保存:', saveData.tableData.length, '条数据')

  // 保存成功后，设置为已审核状态
  isAudited.value = true
  ElMessage.success(`保存成功，已保存 ${saveData.tableData.length} 条数据`)
}

/**
 * 刷新按钮处理函数
 */
function handleRefresh() {
  console.log('刷新数据')

  // 重置表单数据
  headerForm.documentDate = new Date().toISOString().split('T')[0]
  headerForm.documentNumber = generateBillNo()

  // 重置表格数据
  tableData.value = [
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

  // 重置审核状态
  isAudited.value = false

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
  // 恢复到未审核状态
  isAudited.value = false
  ElMessage.success('已反审核，恢复到未审核状态')
}

/**
 * 生成其他出库单处理函数
 */
function handleGenerateOtherOutbound() {
  console.log('生成其他出库单')

  // 获取有效的表格数据
  const validTableData = tableData.value.filter((item) => item.productName)
  console.log('有效数据条数:', validTableData.length)

  // 关闭弹窗
  showGeneratePopup.value = false

  if (validTableData.length === 0) {
    ElMessage.info('没有有效的商品数据')
  } else {
    ElMessage.success(`已生成 ${validTableData.length} 条数据的其他出库单`)
  }
}
// 原onMounted函数中添加仓库加载逻辑
onMounted(() => {
  fetchCustomers() // 原有客户加载逻辑保留

  // 新增：加载仓库列表
  loadWarehouseList()

  // 原有点击外部关闭客户下拉面板的逻辑保留
  document.addEventListener('click', (e) => {
    const target = e.target as HTMLElement
    if (!target.closest('.customer-select')) {
      customerDropdownVisible.value = false
    }
  })
})
// 在组件挂载后的操作
onMounted(() => {
  // 初始化一些示例数据（可选）
})
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
/* 仓库选择相关样式（与调拨单完全一致） */
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
