<template>
  <div class="sys area">
    <!-- 操作按钮区域 -->
    <div class="operation-bar">
      <div class="operation-left">
        <!-- 搜索按钮和弹框 -->
        <el-popover
          placement="bottom-start"
          :width="600"
          trigger="click"
          v-model:visible="searchPopoverVisible"
          :popper-options="{ modifiers: [{ name: 'eventListeners', options: { scroll: false } }] }"
        >
          <template #reference>
            <el-button class="search-btn">
              <el-icon><MoreFilled /></el-icon>
            </el-button>
          </template>
          <div class="search-popover">
            <div class="search-form">
              <!-- 筛选类型下拉框 -->
              <div class="form-item">
                <el-select
                  v-model="searchForm.filterType"
                  placeholder="请选择筛选类型"
                  clearable
                  style="width: 100%"
                  @change="handleFilterTypeChange"
                >
                  <el-option label="按商品" value="product" />
                  <el-option label="按供应商" value="supplier" />
                  <el-option label="按用户" value="user" />
                  <el-option label="按人员" value="person" />
                </el-select>
              </div>

              <!-- 仓库选择 - 改为搜索组件样式 -->
              <div class="form-item">
                <el-input
                  v-model="searchForm.warehouse"
                  placeholder="请选择仓库"
                  readonly
                  clearable
                  style="cursor: pointer; width: 100%"
                  @focus="handleWarehouseInputFocus"
                  @clear="handleClearWarehouse"
                >
                  <template #suffix>
                    <el-icon @click.stop="handleWarehouseInputFocus" style="cursor: pointer">
                      <Search />
                    </el-icon>
                  </template>
                </el-input>
              </div>

              <!-- 商品名称 -->
              <div class="form-item">
                <el-input
                  v-model="searchForm.goods"
                  placeholder="请输入商品名称"
                  clearable
                  style="width: 100%"
                />
              </div>

              <!-- 供应商选择 - 改为搜索组件样式 -->
              <div class="form-item">
                <el-input
                  v-model="searchForm.supplier"
                  placeholder="请选择供应商"
                  readonly
                  clearable
                  style="cursor: pointer; width: 100%"
                  @focus="handleSupplierInputFocus"
                  @clear="handleClearSupplier"
                  :disabled="supplierDisabled"
                >
                  <template #suffix>
                    <el-icon @click.stop="handleSupplierInputFocus" style="cursor: pointer">
                      <Search />
                    </el-icon>
                  </template>
                </el-input>
              </div>

              <!-- 用户选择 - 改为搜索组件样式 -->
              <div class="form-item">
                <el-input
                  v-model="searchForm.user"
                  placeholder="请选择用户"
                  readonly
                  clearable
                  style="cursor: pointer; width: 100%"
                  @focus="handleUserInputFocus"
                  @clear="handleClearUser"
                  :disabled="userDisabled"
                >
                  <template #suffix>
                    <el-icon @click.stop="handleUserInputFocus" style="cursor: pointer">
                      <Search />
                    </el-icon>
                  </template>
                </el-input>
              </div>

              <!-- 关联人员选择 - 改为搜索组件样式 -->
              <div class="form-item">
                <el-input
                  v-model="searchForm.people"
                  placeholder="请选择关联人员"
                  readonly
                  clearable
                  style="cursor: pointer; width: 100%"
                  @focus="handlePeopleInputFocus"
                  @clear="handleClearPeople"
                  :disabled="peopleDisabled"
                >
                  <template #suffix>
                    <el-icon @click.stop="handlePeopleInputFocus" style="cursor: pointer">
                      <Search />
                    </el-icon>
                  </template>
                </el-input>
              </div>

              <!-- 单据日期范围 - 修复为两个独立的日期选择器 -->
              <div class="form-item">
                <div class="date-range-container">
                  <el-date-picker
                    v-model="searchForm.startTime"
                    placeholder="单据开始日期"
                    value-format="YYYY-MM-DD"
                    type="date"
                    style="width: 48%; margin-right: 4%"
                    :append-to-body="false"
                    :teleported="false"
                  />
                  <el-date-picker
                    v-model="searchForm.endTime"
                    placeholder="单据结束日期"
                    value-format="YYYY-MM-DD"
                    type="date"
                    style="width: 48%"
                    :append-to-body="false"
                    :teleported="false"
                  />
                </div>
              </div>

              <!-- 搜索按钮 -->
              <div class="form-item">
                <el-button type="primary" @click="handleSearch" style="width: 100%">
                  <el-icon><Search /></el-icon>
                  搜索
                </el-button>
              </div>
            </div>
          </div>
        </el-popover>
      </div>

      <div class="operation-right">
        <el-button type="primary" @click="handleExport" class="action-btn">
          <el-icon><Download /></el-icon>
          导出
        </el-button>
        <el-button type="info" @click="handleRefresh" class="action-btn">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <!-- 仓库高级搜索弹窗 -->
    <el-dialog
      :model-value="showWarehouseDialog"
      @update:model-value="handleWarehouseDialogUpdate"
      title="仓库搜索"
      width="720px"
      append-to-body
      :close-on-click-modal="false"
      :modal="true"
      destroy-on-close
    >
      <Warehouse @search="handleWarehouseDialogSearch" />
      <template #footer>
        <el-button @click="handleCloseWarehouseDialog">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 供应商高级搜索弹窗 -->
    <el-dialog
      :model-value="showSupplierDialog"
      @update:model-value="handleSupplierDialogUpdate"
      title="供应商搜索"
      width="720px"
      append-to-body
      :close-on-click-modal="false"
      :modal="true"
      destroy-on-close
    >
      <Supplier @search="handleSupplierDialogSearch" />
      <template #footer>
        <el-button @click="handleCloseSupplierDialog">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 人员高级搜索弹窗 -->
    <el-dialog
      :model-value="showPeopleDialog"
      @update:model-value="handlePeopleDialogUpdate"
      title="人员搜索"
      width="720px"
      append-to-body
      :close-on-click-modal="false"
      :modal="true"
      destroy-on-close
    >
      <PeopleList @search="handlePeopleDialogSearch" />
      <template #footer>
        <el-button @click="handleClosePeopleDialog">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 用户高级搜索弹窗 -->
    <el-dialog
      :model-value="showUserDialog"
      @update:model-value="handleUserDialogUpdate"
      title="用户搜索"
      width="720px"
      append-to-body
      :close-on-click-modal="false"
      :modal="true"
      destroy-on-close
    >
      <UserList @search="handleUserDialogSearch" />
      <template #footer>
        <el-button @click="handleCloseUserDialog">关闭</el-button>
      </template>
    </el-dialog>

    <el-divider class="custom-divider" />

    <!-- 表格区域 - 占据主要空间 -->
    <div class="table-container">
      <el-table :data="tableData" style="width: 100%" v-loading="loading" border class="grid-table">
        <!-- 关联人员列 -->
        <el-table-column
          prop="personName"
          label="关联人员"
          width="120"
          align="center"
          v-if="showPersonColumn && !hideColumnsForProduct"
        >
          <template #default="scope">
            {{ scope.row.personName }}
          </template>
        </el-table-column>

        <!-- 用户列 -->
        <el-table-column
          prop="userName"
          label="用户"
          width="120"
          align="center"
          v-if="showUserColumn && !hideColumnsForProduct"
        >
          <template #default="scope">
            {{ scope.row.userName }}
          </template>
        </el-table-column>

        <!-- 供应商列 -->
        <el-table-column
          prop="supplierName"
          label="供应商"
          width="150"
          v-if="showSupplierColumn && !hideColumnsForProduct"
        >
          <template #default="scope">
            {{ scope.row.supplierName }}
          </template>
        </el-table-column>

        <!-- 商品名称 -->
        <el-table-column prop="productName" label="商品名称" width="150">
          <template #default="scope">
            {{ scope.row.productName }}
          </template>
        </el-table-column>

        <!-- 辅助属性 -->
        <el-table-column prop="auxiliaryAttr" label="辅助属性" width="120">
          <template #default="scope">
            {{ scope.row.auxiliaryAttr }}
          </template>
        </el-table-column>

        <!-- 仓库 -->
        <el-table-column prop="warehouse" label="仓库" width="100" align="center">
          <template #default="scope">
            {{ scope.row.warehouse }}
          </template>
        </el-table-column>

        <!-- 单位 -->
        <el-table-column prop="unit" label="单位" width="80" align="center">
          <template #default="scope">
            {{ scope.row.unit }}
          </template>
        </el-table-column>

        <!-- 单据日期 -->
        <el-table-column prop="billDate" label="单据日期" width="120" align="center">
          <template #default="scope">
            {{ scope.row.billDate }}
          </template>
        </el-table-column>

        <!-- 采购单 -->
        <el-table-column label="采购单" align="center">
          <el-table-column prop="purchaseUnitPrice" label="单价" width="100" align="right">
            <template #default="scope">
              {{ formatCurrency(scope.row.purchaseUnitPrice) }}
            </template>
          </el-table-column>

          <el-table-column prop="purchaseQuantity" label="数量" width="100" align="right">
            <template #default="scope">
              {{ scope.row.purchaseQuantity }}
            </template>
          </el-table-column>

          <el-table-column prop="purchaseAmount" label="金额" width="120" align="right">
            <template #default="scope">
              <span class="positive-amount">
                {{ formatCurrency(scope.row.purchaseAmount) }}
              </span>
            </template>
          </el-table-column>
        </el-table-column>

        <!-- 采购退货单 -->
        <el-table-column label="采购退货单" align="center">
          <el-table-column prop="returnUnitPrice" label="单价" width="100" align="right">
            <template #default="scope">
              {{ formatCurrency(scope.row.returnUnitPrice) }}
            </template>
          </el-table-column>

          <el-table-column prop="returnQuantity" label="数量" width="100" align="right">
            <template #default="scope">
              {{ scope.row.returnQuantity }}
            </template>
          </el-table-column>

          <el-table-column prop="returnAmount" label="金额" width="120" align="right">
            <template #default="scope">
              <span class="negative-amount">
                {{ formatCurrency(scope.row.returnAmount) }}
              </span>
            </template>
          </el-table-column>
        </el-table-column>

        <!-- 汇总 -->
        <el-table-column label="汇总" align="center">
          <el-table-column prop="totalQuantity" label="数量" width="100" align="right">
            <template #default="scope">
              {{ scope.row.totalQuantity }}
            </template>
          </el-table-column>

          <el-table-column prop="totalAmount" label="金额" width="120" align="right">
            <template #default="scope">
              <span :class="getTotalAmountClass(scope.row)">
                {{ formatCurrency(scope.row.totalAmount) }}
              </span>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>

      <!-- 分页区域和金额统计 -->
      <div class="pagination-container">
        <div class="pagination-left">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 30, 50]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
        <div class="amount-summary">
          <div class="amount-item">
            <span class="amount-label">采购总金额：</span>
            <span class="amount-value purchase-amount">{{
              formatCurrency(purchaseTotalAmount)
            }}</span>
          </div>
          <div class="amount-item">
            <span class="amount-label">退货总金额：</span>
            <span class="amount-value return-amount">{{ formatCurrency(returnTotalAmount) }}</span>
          </div>
          <div class="amount-item">
            <span class="amount-label">汇总金额：</span>
            <span class="amount-value total-amount">{{ formatCurrency(totalAmount) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Download, Refresh, Search, MoreFilled } from '@element-plus/icons-vue'
// 引入仓库、供应商、人员、用户组件
import Warehouse from '@/components/goodSearchConpoent/Warehouse.vue'
import Supplier from '@/components/goodSearchConpoent/Supplier.vue'
import PeopleList from '@/components/goodSearchConpoent/PeopleList.vue'
import UserList from '@/components/goodSearchConpoent/UserList.vue'

import {
  purchaseOrderSummaryFormUsingGet,
  purchaseOrderSummaryFormOpenApiExportUsingGet
} from '@/apis/report/purchase/caigouhuizongbiao'
import type {
  PurchaseOrderSummaryFormUsingGetParams,
  PurchaseOrderSummaryFormOpenApiExportUsingGetParams,
  PageDTO10
} from '@/apis/report/purchase/types'

// ===================== 1. 类型 & 基础状态 =====================

type FilterType = 'product' | 'supplier' | 'user' | 'person'

interface PurchaseSummaryRow {
  id?: number | string
  personName?: string
  userName?: string
  supplierName?: string
  productName: string
  auxiliaryAttr?: string
  warehouse?: string
  unit?: string
  billDate?: string
  purchaseUnitPrice: number
  purchaseQuantity: number
  purchaseAmount: number
  returnUnitPrice: number
  returnQuantity: number
  returnAmount: number
  totalQuantity: number
  totalAmount: number
}

// 搜索弹框显示状态
const searchPopoverVisible = ref(false)

// 搜索表单数据 - 默认筛选类型为"按商品"
const searchForm = reactive({
  filterType: 'product' as FilterType, // 默认选择"按商品"
  warehouse: '', // 仓库
  goods: '', // 商品名称
  supplier: '', // 供应商
  user: '', // 用户
  people: '', // 关联人员
  startTime: '', // 开始日期
  endTime: '' // 结束日期
})

// 计算字段禁用状态
const supplierDisabled = computed(() => searchForm.filterType !== 'supplier')
const userDisabled = computed(() => searchForm.filterType !== 'user')
const peopleDisabled = computed(() => searchForm.filterType !== 'person')

// 表格 & 分页
const tableData = ref<PurchaseSummaryRow[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)

// ===================== 2. 弹窗控制（原逻辑保留） =====================

// 仓库弹窗控制
const showWarehouseDialog = ref(false)
const isClosingWarehouseDialog = ref(false)

// 供应商弹窗控制
const showSupplierDialog = ref(false)
const isClosingSupplierDialog = ref(false)

// 人员弹窗控制
const showPeopleDialog = ref(false)
const isClosingPeopleDialog = ref(false)

// 用户弹窗控制
const showUserDialog = ref(false)
const isClosingUserDialog = ref(false)

// 筛选类型改变处理
const handleFilterTypeChange = () => {
  if (searchForm.filterType !== 'supplier') searchForm.supplier = ''
  if (searchForm.filterType !== 'user') searchForm.user = ''
  if (searchForm.filterType !== 'person') searchForm.people = ''
}

// 仓库相关处理函数
const handleWarehouseInputFocus = () => {
  if (!isClosingWarehouseDialog.value) {
    showWarehouseDialog.value = true
  }
}
const handleClearWarehouse = () => {
  searchForm.warehouse = ''
}
const handleCloseWarehouseDialog = () => {
  showWarehouseDialog.value = false
}
const handleWarehouseDialogUpdate = (val: boolean) => {
  showWarehouseDialog.value = val
}
const handleWarehouseDialogSearch = (params: any) => {
  if (params && typeof params === 'object') {
    const name = params.name || params.warehouseName || ''
    searchForm.warehouse = name
  }
  showWarehouseDialog.value = false
}

// 供应商相关处理函数
const handleSupplierInputFocus = () => {
  if (!isClosingSupplierDialog.value && !supplierDisabled.value) {
    showSupplierDialog.value = true
  }
}
const handleClearSupplier = () => {
  searchForm.supplier = ''
}
const handleCloseSupplierDialog = () => {
  showSupplierDialog.value = false
}
const handleSupplierDialogUpdate = (val: boolean) => {
  showSupplierDialog.value = val
}
const handleSupplierDialogSearch = (params: any) => {
  if (params && typeof params === 'object') {
    const name = params.name || params.supplierName || ''
    searchForm.supplier = name
  }
  showSupplierDialog.value = false
}

// 人员相关处理函数
const handlePeopleInputFocus = () => {
  if (!isClosingPeopleDialog.value && !peopleDisabled.value) {
    showPeopleDialog.value = true
  }
}
const handleClearPeople = () => {
  searchForm.people = ''
}
const handleClosePeopleDialog = () => {
  showPeopleDialog.value = false
}
const handlePeopleDialogUpdate = (val: boolean) => {
  showPeopleDialog.value = val
}
const handlePeopleDialogSearch = (params: any) => {
  if (params && typeof params === 'object') {
    const name = params.name || params.peopleName || ''
    searchForm.people = name
  }
  showPeopleDialog.value = false
}

// 用户相关处理函数
const handleUserInputFocus = () => {
  if (!isClosingUserDialog.value && !userDisabled.value) {
    showUserDialog.value = true
  }
}
const handleClearUser = () => {
  searchForm.user = ''
}
const handleCloseUserDialog = () => {
  showUserDialog.value = false
}
const handleUserDialogUpdate = (val: boolean) => {
  showUserDialog.value = val
}
const handleUserDialogSearch = (params: any) => {
  if (params && typeof params === 'object') {
    const name = params.name || params.userName || ''
    searchForm.user = name
  }
  showUserDialog.value = false
}

// ===================== 3. 查询参数 & 数据映射 =====================

// 将 filterType 转成接口需要的 summaryType
const getSummaryType = (): string | undefined => {
  switch (searchForm.filterType) {
    case 'product':
      return 'goods'
    case 'supplier':
      return 'supplier'
    case 'user':
      return 'user'
    case 'person':
      return 'people'
    default:
      return undefined
  }
}

// 将前端搜索条件转换为后端查询参数
const buildSearchParams = (): Partial<PurchaseOrderSummaryFormUsingGetParams> => {
  const params: Partial<PurchaseOrderSummaryFormUsingGetParams> = {}

  if (searchForm.warehouse) params.warehouse = searchForm.warehouse
  if (searchForm.goods) params.goods = searchForm.goods
  if (searchForm.supplier) params.supplier = searchForm.supplier
  if (searchForm.user) params.user = searchForm.user
  if (searchForm.people) params.people = searchForm.people
  if (searchForm.startTime) params.startTime = searchForm.startTime
  if (searchForm.endTime) params.endTime = searchForm.endTime

  const summaryType = getSummaryType()
  if (summaryType) params.summaryType = summaryType

  return params
}

// 后端 row → 前端表格行
const mapBackendRowToItem = (item: any): PurchaseSummaryRow => {
  const row: PurchaseSummaryRow = {
    id: item.id ?? undefined,
    personName: item.personName ?? item.people ?? '',
    userName: item.userName ?? item.user ?? '',
    supplierName: item.supplierName ?? item.supplier ?? '',
    productName: item.productName ?? item.goods ?? '',
    auxiliaryAttr: item.auxiliaryAttr ?? item.attr ?? '',
    warehouse: item.warehouse ?? '',
    unit: item.unit ?? '',
    billDate: item.billDate ?? item.date ?? '',

    purchaseUnitPrice: Number(item.buy?.price ?? 0),
    purchaseQuantity: Number(item.buy?.nums ?? 0),
    purchaseAmount: Number(item.buy?.money ?? 0),

    returnUnitPrice: Number(item.bor?.price ?? 0),
    returnQuantity: Number(item.bor?.nums ?? 0),
    returnAmount: Number(item.bor?.money ?? 0),

    totalQuantity: Number(item.summary?.nums ?? 0),
    totalAmount: Number(item.summary?.money ?? 0)
  }

  return row
}

// ===================== 4. 调用查询接口 =====================

const fetchTableData = async () => {
  loading.value = true
  try {
    const params: PurchaseOrderSummaryFormUsingGetParams = {
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      ...(buildSearchParams() as any)
    }

    const res = await purchaseOrderSummaryFormUsingGet({ params })
    console.log('【采购汇总表接口返回】', res)

    const anyRes: any = res
    let pageWrapper: PageDTO10 | undefined

    // 兼容 JsonVO<JsonVO<PageDTO10>> / JsonVO<PageDTO10> / PageDTO10
    if (anyRes?.data?.data?.rows || anyRes?.data?.data?.total != null) {
      pageWrapper = anyRes.data.data as PageDTO10
    } else if (anyRes?.data?.rows || anyRes?.data?.total != null) {
      pageWrapper = anyRes.data as PageDTO10
    } else if (anyRes?.rows || anyRes?.total != null) {
      pageWrapper = anyRes as PageDTO10
    } else {
      console.warn('⚠ 未识别的分页结构，请查看上面的接口返回 log', anyRes)
    }

    const list = pageWrapper?.rows ?? []
    const totalCount = pageWrapper?.total ?? 0

    tableData.value = list.map((item: any) => mapBackendRowToItem(item))
    total.value = totalCount
  } catch (error: any) {
    console.error('获取采购汇总表失败', error)
    ElMessage.error(error?.message || '获取采购汇总表失败')
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// ===================== 5. 搜索 / 重置 / 刷新 =====================

const handleSearch = () => {
  // 只是为了提示，保留你原来的“搜索条件”拼接逻辑
  const searchConditions: string[] = []
  if (searchForm.warehouse) searchConditions.push(`仓库：${searchForm.warehouse}`)
  if (searchForm.goods) searchConditions.push(`商品：${searchForm.goods}`)
  if (searchForm.supplier) searchConditions.push(`供应商：${searchForm.supplier}`)
  if (searchForm.user) searchConditions.push(`用户：${searchForm.user}`)
  if (searchForm.people) searchConditions.push(`人员：${searchForm.people}`)
  if (searchForm.startTime && searchForm.endTime) {
    searchConditions.push(`日期：${searchForm.startTime} 至 ${searchForm.endTime}`)
  }

  const searchInfo =
    searchConditions.length > 0 ? `搜索条件：${searchConditions.join('，')}` : '显示全部数据'

  currentPage.value = 1
  searchPopoverVisible.value = false
  fetchTableData()
  ElMessage.success(`搜索完成，${searchInfo}`)
}

// 重置搜索
const handleResetSearch = () => {
  Object.assign(searchForm, {
    filterType: 'product' as FilterType,
    warehouse: '',
    goods: '',
    supplier: '',
    user: '',
    people: '',
    startTime: '',
    endTime: ''
  })
  ElMessage.info('已重置搜索条件')
}

// 刷新
const handleRefresh = () => {
  handleResetSearch()
  currentPage.value = 1
  fetchTableData()
  ElMessage.success('数据已刷新')
}

// ===================== 6. 导出接口 =====================

const handleExport = async () => {
  try {
    ElMessage.info('正在导出数据...')

    const commonParams = buildSearchParams()

    const params: PurchaseOrderSummaryFormOpenApiExportUsingGetParams = {
      // 当前条件下全部数据
      pageIndex: 1,
      pageSize: 99999,
      ...(commonParams as any)
    }

    const res = await purchaseOrderSummaryFormOpenApiExportUsingGet({ params })
    console.log('【采购汇总表导出接口返回】', res)

    const anyRes: any = res
    const blob: Blob =
      anyRes instanceof Blob
        ? anyRes
        : anyRes?.data instanceof Blob
          ? anyRes.data
          : new Blob([anyRes], {
              type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
            })

    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `采购汇总表_${new Date().toISOString().split('T')[0]}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    URL.revokeObjectURL(url)

    ElMessage.success('导出成功，已开始下载文件')
  } catch (error: any) {
    console.error('导出采购汇总表失败', error)
    ElMessage.error(error?.message || '导出采购汇总表失败')
  }
}

// ===================== 7. 金额展示 & 列显示控制 =====================

const formatCurrency = (value: number | string | null | undefined) => {
  const num = Number(value) || 0
  return (
    '¥' +
    num.toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    })
  )
}

const getTotalAmountClass = (row: PurchaseSummaryRow) => {
  return row.totalAmount >= 0 ? 'positive-amount' : 'negative-amount'
}

const showPersonColumn = computed(() => searchForm.filterType === 'person')
const showUserColumn = computed(() => searchForm.filterType === 'user')
const showSupplierColumn = computed(() => searchForm.filterType === 'supplier')
const hideColumnsForProduct = computed(() => searchForm.filterType === 'product')

// 金额统计（直接从 tableData 算）
const purchaseTotalAmount = computed(() =>
  tableData.value.reduce((sum, item) => sum + (item.purchaseAmount || 0), 0)
)
const returnTotalAmount = computed(() =>
  tableData.value.reduce((sum, item) => sum + (item.returnAmount || 0), 0)
)
const totalAmount = computed(() => purchaseTotalAmount.value - returnTotalAmount.value)

// ===================== 8. 分页事件 =====================

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchTableData()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchTableData()
}

// ===================== 9. 弹窗关闭节流（原逻辑保留） =====================

watch(showWarehouseDialog, (newVal, oldVal) => {
  if (!newVal && oldVal) {
    isClosingWarehouseDialog.value = true
    setTimeout(() => {
      isClosingWarehouseDialog.value = false
    }, 200)
  }
})
watch(showSupplierDialog, (newVal, oldVal) => {
  if (!newVal && oldVal) {
    isClosingSupplierDialog.value = true
    setTimeout(() => {
      isClosingSupplierDialog.value = false
    }, 200)
  }
})
watch(showPeopleDialog, (newVal, oldVal) => {
  if (!newVal && oldVal) {
    isClosingPeopleDialog.value = true
    setTimeout(() => {
      isClosingPeopleDialog.value = false
    }, 200)
  }
})
watch(showUserDialog, (newVal, oldVal) => {
  if (!newVal && oldVal) {
    isClosingUserDialog.value = true
    setTimeout(() => {
      isClosingUserDialog.value = false
    }, 200)
  }
})

// ===================== 10. 初始化 =====================

onMounted(() => {
  // 默认筛选类型为“按商品”
  searchForm.filterType = 'product'

  // 默认日期范围：最近 30 天
  const endDate = new Date()
  const startDate = new Date()
  startDate.setDate(endDate.getDate() - 30)

  const formatDate = (date: Date): string => {
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  }

  searchForm.startTime = formatDate(startDate)
  searchForm.endTime = formatDate(endDate)

  // 初始化加载一次数据
  fetchTableData()
})
</script>

<style scoped>
.sys.area {
  position: relative;
  padding: 16px;
  height: calc(100vh - 32px);
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

/* 操作栏样式 */
.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  background: white;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  margin-bottom: 0;
}

.operation-left,
.operation-right {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 16px;
}

.search-btn,
.action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 搜索弹框样式 */
.search-popover {
  padding: 0;
}

.search-form {
  padding: 16px;
}

.form-item {
  margin-bottom: 16px;
}

.date-range-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.form-label {
  display: block;
  margin-bottom: 6px;
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

/* 分割线样式 */
.custom-divider {
  margin: 8px 0;
  border-color: #e4e7ed;
}

/* 表格容器 - 占据剩余空间 */
.table-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
  background: white;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 表格样式 */
:deep(.el-table) {
  flex: 1;
}

:deep(.el-table .el-table__row) {
  cursor: pointer;
}

/* 网状表格样式 */
:deep(.grid-table) {
  border: 1px solid #ebeef5;
}

:deep(.grid-table .el-table__header-wrapper) {
  border-bottom: 1px solid #ebeef5;
}

:deep(.grid-table .el-table__cell) {
  border-right: 1px solid #ebeef5;
}

:deep(.grid-table .el-table__row) {
  border-bottom: 1px solid #ebeef5;
}

/* 分页容器样式 */
.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-top: 1px solid #ebeef5;
  background: #fafafa;
  flex-wrap: wrap;
  gap: 16px;
}

.amount-summary {
  display: flex;
  gap: 20px;
  align-items: center;
}

.amount-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.amount-label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
  white-space: nowrap;
}

.amount-value {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  padding: 5px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #ffffff;
  min-width: 120px;
  text-align: center;
  display: inline-block;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
}

.amount-value:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border-color: #c0c4cc;
}

/* .purchase-amount {
  color: #67c23a;
  border-color: #67c23a;
}

.return-amount {
  color: #f56c6c;
  border-color: #f56c6c;
}

.total-amount {
  color: #409eff;
  border-color: #409eff;
}

.positive-amount {
  color: #67c23a;
}

.negative-amount {
  color: #f56c6c;
} */

/* 美化滚动条 */
:deep(.el-table__body-wrapper)::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sys.area {
    padding: 8px;
    height: calc(100vh - 16px);
  }

  .operation-bar {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .operation-left,
  .operation-right {
    justify-content: center;
  }

  .operation-left {
    border-bottom: 1px solid #e4e7ed;
    padding-bottom: 12px;
  }

  .search-popover {
    width: 280px !important;
  }

  .date-range-container {
    flex-direction: column;
    gap: 10px;
  }

  .date-range-container .el-date-picker {
    width: 100% !important;
    margin-right: 0 !important;
  }

  .pagination-container {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .amount-summary {
    justify-content: center;
    flex-wrap: wrap;
  }
}

/* 修复时间选择器在弹框内的问题 */
:deep(.el-popper) {
  z-index: 9999 !important;
}
</style>
