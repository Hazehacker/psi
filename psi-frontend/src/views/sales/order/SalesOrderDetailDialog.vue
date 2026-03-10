<template>
  <el-dialog
    v-model="innerVisible"
    fullscreen
    :close-on-click-modal="false"
    class="sb-detail-dialog"
    title="单据详情"
  >
    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <div class="left">
        <!-- 客户选择 -->
        <div class="form-item">
          <label class="form-label">客户</label>
          <el-input
            v-model="customerModel.name"
            placeholder="请选择客户"
            readonly
            clearable
            class="customer-input"
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
            v-model="header.documentDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="选择日期"
            class="date-input"
          />
        </div>

        <!-- 单据编号 -->
        <div class="form-item">
          <label class="form-label">单据编号</label>
          <el-input
            v-model="header.documentNumber"
            placeholder="请输入单据编号"
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
                <el-dropdown-item @click="generateSalesReturn"> 销售退货单 </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

          <el-button class="ml8" @click="toggleApprove">反审核</el-button>
        </template>

        <!-- 未审核时：显示 保存 + 审核（隐藏“生成”） -->
        <template v-else>
          <el-button type="primary" @click="save">保存</el-button>
          <el-button class="ml8" @click="toggleApprove">审核</el-button>
        </template>
        <!-- ✅ 无论是否审核，都显示这个“核对 / 反核对” -->
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
      <DocumentForm :fields="purchaseBookFields" />
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
import { ref, reactive, computed, watch, onMounted, getCurrentInstance } from 'vue'
import { ArrowDown } from '@element-plus/icons-vue'
import Customer from '@/components/goodSearchConpoent/Customer.vue'
import OperationPopTable from '@/components/operationpoptable/OperationPopTable.vue'
import { Setting, Edit, Delete, Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { nextTick } from 'vue'
import DocumentForm from '@/components/documentform/DocumentForm.vue'
import { useRouter } from 'vue-router'
import Warehouse from '@/components/goodSearchConpoent/Warehouse.vue'

// 导入销售单API
import { saleOrderApi } from '@/apis/sales/order'
import type { SaleDTO, SaleDetailItem } from '@/apis/sales/order/type'

// 新增：核对状态
const isChecked = ref(false)

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

// 核对 / 反核对
async function toggleCheck() {
  try {
    if (!props.record?.id) {
      ElMessage.warning('请先保存单据')
      return
    }

    const response = await saleOrderApi.checkSales([props.record.id], !isChecked.value)

    if (response.code === 10000) {
      isChecked.value = !isChecked.value
      ElMessage.success(isChecked.value ? '核对单据成功' : '反核对成功')
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('核对操作失败:', error)
    ElMessage.error('操作失败，请重试')
  }
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

const router = useRouter()

/* 审核状态 */
const isApproved = ref(true)

// 审核/反审核
const toggleApprove = async () => {
  try {
    if (!props.record?.id) {
      ElMessage.warning('请先保存单据')
      return
    }

    const response = await saleOrderApi.examineSales([props.record.id], !isApproved.value)

    if (response.code === 10000) {
      isApproved.value = !isApproved.value
      ElMessage.success(isApproved.value ? '审核成功' : '反审核成功')
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('审核操作失败:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 生成：携带数据并跳转到【销售退货单】
function generateSalesReturn() {
  // 有效商品行（过滤掉空行）
  const validRows = tableData.value.filter((r) => r.productName)

  // 要带过去的数据，结构我们保持跟原来一样，方便目标页复用
  const payload = {
    from: 'sales-book-detail', // 来源标记，你原来就是这个
    businessType: 'sales-return', // ✅ 可选：给目标页一个业务类型
    header: { ...header },
    customer: customerModel.value,
    tableData: validRows,
    footer: { ...footer },
    isApproved: isApproved.value,
    timestamp: Date.now()
  }

  // 存到 sessionStorage 里，目标页去拿
  sessionStorage.setItem('SB_DETAIL_TRANSFER', JSON.stringify(payload))

  // 跳转到销售退货单页面
  const targetRoute = {
    path: '/sales-return', // ✅ 这里按你项目实际路由名称改
    query: {
      from: 'sales-booking',
      dataTransfer: 'true'
    }
  }

  console.log('🎯 准备跳转到销售退货单:', targetRoute)

  router
    .push(targetRoute)
    .then(() => {
      console.log('✅ 路由跳转成功')
      if (validRows.length === 0) {
        ElMessage.info('正在跳转到销售退货单页面（无商品数据）')
      } else {
        ElMessage.success(`正在跳转到销售退货单页面，共 ${validRows.length} 条数据`)
      }
    })
    .catch((error: any) => {
      console.error('❌ 路由跳转失败:', error)
      ElMessage.error('路由跳转失败，请检查路由配置或路径 /sales-return 是否存在')
    })

  console.log('🏁 [generateSalesReturn] 执行完成')
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
// 仓库选择变化时触发
function handleWarehouseChange(row: any) {
  // 更新当前行仓库数据
  row.warehouse = row.warehouse || ''
  // 触发计算
  recalculate(row)
  // 显示提示
  ElMessage.success(`已选择仓库：${row.warehouse}`)
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

  // 👇 同步把这一行的 popover 状态也删掉
  rowPopoverVisible.value.splice(index, 1)

  ElMessage.success('已删除该行')
}

// 商品弹窗控制
const showProductPopup = ref(false)
const currentRowIndex = ref<number | null>(null)

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

/* 客户组件 */
const customerModel = ref({ name: '' })
const header = reactive({
  documentDate: '',
  documentNumber: ''
})

// 监听对话框打开，初始化数据
watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      // 对话框打开时，如果有 record，加载数据
      if (props.record) {
        // 确保 customerModel 始终有值
        if (!customerModel.value) {
          customerModel.value = { name: '' }
        }
        // 加载记录数据（如果需要）
        if (props.record.customer) {
          customerModel.value.name = props.record.customer || ''
        }
        if (props.record.billTime) {
          header.documentDate = props.record.billTime || ''
        }
        if (props.record.billNo) {
          header.documentNumber = props.record.billNo || ''
        }
      } else {
        // 新建时，确保所有数据都有默认值
        customerModel.value = customerModel.value || { name: '' }
        header.documentDate = header.documentDate || ''
        header.documentNumber = header.documentNumber || ''
      }
    }
  },
  { immediate: true }
)

// 客户选择对话框
const showCustomerDialog = ref(false)

// 处理客户搜索
function handleCustomerSearch(params: any) {
  if (params && typeof params === 'object') {
    const name = params.name || params.customerName || ''
    customerModel.value = { ...customerModel.value, name: name || '' }
  }
  showCustomerDialog.value = false
}

// 清除客户
function handleClearCustomer() {
  customerModel.value = { name: '' }
}

// 总条数和总合计（状态栏数据）
const totalCount = computed(() => {
  if (!tableData.value || !Array.isArray(tableData.value)) return 0
  return tableData.value.filter((r: any) => r && r.productName && r.quantity > 0).length
})

const totalSum = computed(() => {
  if (!tableData.value || !Array.isArray(tableData.value)) return '0.00'
  const sum = tableData.value
    // 只统计有商品的行
    .filter((r) => r && r.productName)
    // 累加每一行的价税合计
    .reduce((acc, r) => acc + (Number(r.total) || 0), 0)

  return (sum || 0).toFixed(2)
})

/* 底部表单 */
const footer = reactive({
  documentCost: '16.95',
  actualAmount: '1',
  relatedPerson: 'NTJK_test_DD001',
  arrivalDate: '',
  logistics: '',
  remark: ''
})

/* 保存 */
const save = async () => {
  try {
    // 构建销售单数据
    const orderId = props.record?.id || '' // 如果是编辑，传递ID
    const saleData: SaleDTO = {
      id: orderId,
      customer: customerModel.value.name,
      time: header.documentDate,
      number: header.documentNumber,
      examine: isApproved.value,
      check: isChecked.value,
      items: tableData.value
        .filter((item) => item.productName) // 只包含有商品的行
        .map((item) => ({
          pid: orderId, // 所属销售单ID
          goodsName: item.productName,
          goodsCode: item.productCode,
          spec: item.spec,
          attr: item.attr,
          unit: item.unit,
          warehouse: item.warehouse,
          price: typeof item.price === 'string' ? parseFloat(item.price) || 0 : item.price || 0,
          nums:
            typeof item.quantity === 'string' ? parseFloat(item.quantity) || 0 : item.quantity || 0,
          total: typeof item.amount === 'string' ? parseFloat(item.amount) || 0 : item.amount || 0,
          tax: typeof item.taxRate === 'string' ? parseFloat(item.taxRate) || 0 : item.taxRate || 0,
          tat:
            typeof item.taxAmount === 'string'
              ? parseFloat(item.taxAmount) || 0
              : item.taxAmount || 0,
          tpt: typeof item.total === 'string' ? parseFloat(item.total) || 0 : item.total || 0,
          data: item.remark
        }))
    }

    let response
    if (saleData.id) {
      // 更新现有销售单
      response = await saleOrderApi.updateSale(saleData)
    } else {
      // 新增销售单
      response = await saleOrderApi.addSale(saleData)
    }

    if (response.code === 10000) {
      emit('save', {
        header,
        customer: customerModel.value,
        tableData: tableData.value,
        footer,
        isApproved: isApproved.value
      })
      ElMessage.success('保存成功')
      innerVisible.value = false
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败，请重试')
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

.customer-input :deep(.el-input__inner) {
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

/* ===== 弹窗整体布局 ===== */
.sb-detail-dialog :deep(.el-dialog__body) {
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
}
.square-btn {
  width: 36px;
  height: 30px;
  border: 1px solid #dcdfe6;
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
