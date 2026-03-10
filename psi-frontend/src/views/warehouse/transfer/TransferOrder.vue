<template>
  <div class="page-container">
    <!-- 商品表格区域 -->
    <div class="table-section">
      <!-- 表单和按钮行 -->
      <div class="header-row">
        <!-- 左侧表单字段 -->
        <div class="header-form">
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

          <!-- 调出仓库表头 -->
          <el-table-column v-if="visibleColumns.outDepot" label="调出仓库" align="center">
            <template #header>
              <div class="flex-center">
                <!-- 表头文字+下拉图标 -->
                <el-popover
                  trigger="click"
                  placement="bottom-start"
                  v-model:visible="outDepotPopVisible"
                >
                  <template #reference>
                    <div class="depot-header-trigger">
                      <span>调出仓库</span>
                      <el-icon class="depot-icon clickable-icon"><ArrowDown /></el-icon>
                    </div>
                  </template>
                  <div class="depot-options-no-search">
                    <div
                      v-for="warehouse in warehouseOptions"
                      :key="warehouse.value"
                      class="depot-option-item"
                      @click="handleBatchOutDepotSelect(warehouse.value)"
                    >
                      {{ warehouse.label }}
                    </div>
                  </div>
                </el-popover>
              </div>
            </template>

            <!-- 单元格选择器（保持el-select但优化样式） -->
            <template #default="scope">
              <el-select
                v-model="scope.row.outDepot"
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

          <!-- 调入仓库表头 -->
          <el-table-column v-if="visibleColumns.inDepot" label="调入仓库" align="center">
            <template #header>
              <div class="flex-center">
                <el-popover
                  trigger="click"
                  placement="bottom-start"
                  v-model:visible="inDepotPopVisible"
                >
                  <template #reference>
                    <div class="depot-header-trigger">
                      <span>调入仓库</span>
                      <el-icon class="depot-icon clickable-icon"><ArrowDown /></el-icon>
                    </div>
                  </template>
                  <div class="depot-options-no-search">
                    <div
                      v-for="warehouse in warehouseOptions"
                      :key="warehouse.value"
                      class="depot-option-item"
                      @click="handleBatchInDepotSelect(warehouse.value)"
                    >
                      {{ warehouse.label }}
                    </div>
                  </div>
                </el-popover>
              </div>
            </template>

            <!-- 单元格选择器 -->
            <template #default="scope">
              <el-select
                v-model="scope.row.inDepot"
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
    <DocumentForm :fields="transferOrderFields" />

    <!-- 生成单据弹窗 -->
    <el-dialog
      v-model="showGeneratePopup"
      title="生成单据"
      width="300px"
      :close-on-click-modal="false"
    >
      <div class="generate-options">
        <el-button class="generate-option-btn" @click="handleGenerateTransferOrder">
          调拨单
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
import { Check, Refresh, Search, Setting, Edit, Delete, MoreFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { nextTick } from 'vue'
// 调出/调入仓库弹出层显隐控制
const outDepotPopVisible = ref(false)
const inDepotPopVisible = ref(false)
// 仓库选项数据
const warehouseOptions = ref<Array<{ value: string; label: string }>>([])

// 批量选择值
const batchOutDepotValue = ref('')
const batchInDepotValue = ref('')

/** 调出仓库选择变化 */
function onOutDepotChange(row: any) {
  if (row.outDepot) {
    console.log(`已选择调出仓库：${row.outDepot}`)
  }
}

/** 调入仓库选择变化 */
function onInDepotChange(row: any) {
  if (row.inDepot) {
    console.log(`已选择调入仓库：${row.inDepot}`)
  }
}

/** 调出仓库批量选择确认 */
function handleBatchOutDepotSelect(value: string) {
  if (!value) return
  const selectedWarehouse = warehouseOptions.value.find((w) => w.value === value)
  if (selectedWarehouse) {
    tableData.value.forEach((r) => {
      if (r.productName) r.outDepot = selectedWarehouse.label
    })
    ElMessage.success(`已批量设置调出仓库：${selectedWarehouse.label}`)
  }
  batchOutDepotValue.value = '' // 清空选择
}

/** 调入仓库批量选择确认 */
function handleBatchInDepotSelect(value: string) {
  if (!value) return
  const selectedWarehouse = warehouseOptions.value.find((w) => w.value === value)
  if (selectedWarehouse) {
    tableData.value.forEach((r) => {
      if (r.productName) r.inDepot = selectedWarehouse.label
    })
    ElMessage.success(`已批量设置调入仓库：${selectedWarehouse.label}`)
  }
  batchInDepotValue.value = '' // 清空选择
}

const headerForm = reactive({
  documentDate: new Date().toISOString().split('T')[0],
  documentNumber: generateBillNo()
})

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

// 从后端加载仓库列表
async function loadWarehouseList() {
  try {
    // 模拟从后端获取仓库数据
    // 实际使用时请取消注释下面的代码
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

    // 模拟数据
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

// 页面加载时获取仓库数据
onMounted(() => {
  loadWarehouseList()
})

// 表格数据
const tableData = ref([
  {
    productName: '',
    productCode: '',
    spec: '',
    unit: '',
    attr: '',
    outDepot: '',
    inDepot: '',
    cost: 0,
    quantity: 0,
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
  outDepot: '调出仓库',
  inDepot: '调入仓库',
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

  // 计算总成本，当cost和quantity都为0时显示空
  row.totalCost = cost > 0 && qty > 0 ? +(cost * qty).toFixed(2) : ''

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
  row.outDepot = row.outDepot || '' // 不覆盖已有调出仓库
  row.inDepot = row.inDepot || '' // 不覆盖已有调入仓库
  row.cost = first.purchasePrice ?? 0
  row.quantity = first.quantity ?? 1

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
      outDepot: '',
      inDepot: '',
      cost: 0,
      quantity: 0,
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
  ElMessage.success('已删除该行')
}

// 商品弹窗控制
const showProductPopup = ref(false)
const currentRowIndex = ref<number | null>(null)

// 调拨单需要的字段
const transferOrderFields = [
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
// 仓库搜索关键词
// const outDepotSearchKey = ref('')
// const inDepotSearchKey = ref('')

// 过滤后的调出仓库列表
// const filteredOutDepots = computed(() => {
//   if (!outDepotSearchKey.value) return warehouseOptions.value

//   const keyword = outDepotSearchKey.value.toLowerCase()
//   return warehouseOptions.value.filter((item) => item.label.toLowerCase().includes(keyword))
// })

// // 过滤后的调入仓库列表
// const filteredInDepots = computed(() => {
//   if (!inDepotSearchKey.value) return warehouseOptions.value

//   const keyword = inDepotSearchKey.value.toLowerCase()
//   return warehouseOptions.value.filter((item) => item.label.toLowerCase().includes(keyword))
// })

// 生成单据编号
function generateBillNo(): string {
  const now = new Date()
  const year = now.getFullYear().toString().slice(2)
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const random = Math.floor(Math.random() * 100000)
    .toString()
    .padStart(5, '0')
  return `DBD${year}${month}${day}${random}`
}

/**
 * 保存按钮处理函数
 */
function handleSave() {
  console.log('保存数据')
  console.log('头部表单数据:', headerForm)

  // 验证必填项
  const hasInvalidRow = tableData.value.some((row) => {
    return row.productName && (!row.outDepot || !row.inDepot || !row.cost || !row.quantity)
  })

  if (hasInvalidRow) {
    ElMessage.warning('请填写所有必填字段')
    return
  }

  // 构造保存数据
  const saveData = {
    headerForm: {
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
      outDepot: '',
      inDepot: '',
      cost: 0,
      quantity: 0,
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
 * 生成调拨单处理函数
 */
function handleGenerateTransferOrder() {
  console.log('生成调拨单')

  // 获取有效的表格数据
  const validTableData = tableData.value.filter((item) => item.productName)
  console.log('有效数据条数:', validTableData.length)

  // 关闭弹窗
  showGeneratePopup.value = false

  if (validTableData.length === 0) {
    ElMessage.info('没有有效的商品数据')
  } else {
    ElMessage.success(`已生成 ${validTableData.length} 条数据的调拨单`)
  }
}

// 页面加载时获取仓库数据
onMounted(() => {
  loadWarehouseList()
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

/* 仓库选择下拉框样式 */
.warehouse-select {
  width: 100%;
  min-width: 120px;
}

:deep(.warehouse-select .el-select__wrapper) {
  border: none;
  box-shadow: none;
  background: transparent;
}

:deep(.warehouse-select .el-select__wrapper:hover) {
  background-color: #f5f7fa;
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

/* 表头触发区（文字+图标） */
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

/* 下拉面板样式 */
.depot-dropdown-panel {
  width: 240px;
  padding: 12px;
  box-sizing: border-box;
}

.depot-search {
  margin-bottom: 8px;
}

.depot-options {
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

.depot-empty {
  padding: 12px;
  text-align: center;
  color: #909399;
  font-size: 13px;
}

/* 单元格选择器样式 */
.cell-depot-select {
  width: 100%;
}

/* 隐藏选择器边框，仅在聚焦时显示 */
:deep(.cell-depot-select .el-select__wrapper) {
  border: none !important;
  box-shadow: none !important;
}

:deep(.cell-depot-select .el-select__wrapper:focus-within) {
  box-shadow: 0 0 0 1px #409eff !important;
  border-radius: 4px;
}

/* 调整选择器图标位置 */
:deep(.cell-depot-select .el-select__icon) {
  margin-left: 4px;
  color: #909399;
}

.depot-options-no-search {
  max-height: 200px;
  overflow-y: auto;
  border-radius: 4px;
  border: 1px solid #ebeef5;
}
</style>
