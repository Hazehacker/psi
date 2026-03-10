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
                  <el-option label="按客户" value="customer" />
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

              <!-- 客户选择 - 改为搜索组件样式 -->
              <div class="form-item">
                <el-input
                  v-model="searchForm.customer"
                  placeholder="请选择客户"
                  readonly
                  clearable
                  style="cursor: pointer; width: 100%"
                  @focus="handleCustomerInputFocus"
                  @clear="handleClearCustomer"
                  :disabled="customerDisabled"
                >
                  <template #suffix>
                    <el-icon @click.stop="handleCustomerInputFocus" style="cursor: pointer">
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

    <!-- 客户高级搜索弹窗 -->
    <el-dialog
      :model-value="showCustomerDialog"
      @update:model-value="handleCustomerDialogUpdate"
      title="客户搜索"
      width="720px"
      append-to-body
      :close-on-click-modal="false"
      :modal="true"
      destroy-on-close
    >
      <Customer @search="handleCustomerDialogSearch" />
      <template #footer>
        <el-button @click="handleCloseCustomerDialog">关闭</el-button>
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
      <el-table
        :data="filteredTableData"
        style="width: 100%"
        v-loading="loading"
        border
        class="grid-table"
      >
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

        <!-- 客户列 -->
        <el-table-column
          prop="customerName"
          label="客户"
          width="150"
          v-if="showCustomerColumn && !hideColumnsForProduct"
        >
          <template #default="scope">
            {{ scope.row.customerName }}
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

        <!-- 销售单 -->
        <el-table-column label="销售单" align="center">
          <el-table-column prop="saleUnitPrice" label="单价" width="100" align="right">
            <template #default="scope">
              {{ formatCurrency(scope.row.saleUnitPrice) }}
            </template>
          </el-table-column>

          <el-table-column prop="saleQuantity" label="数量" width="100" align="right">
            <template #default="scope">
              {{ scope.row.saleQuantity }}
            </template>
          </el-table-column>

          <el-table-column prop="saleAmount" label="金额" width="120" align="right">
            <template #default="scope">
              <span class="positive-amount">
                {{ formatCurrency(scope.row.saleAmount) }}
              </span>
            </template>
          </el-table-column>
        </el-table-column>

        <!-- 销售退货单 -->
        <el-table-column label="销售退货单" align="center">
          <el-table-column prop="saleReturnUnitPrice" label="单价" width="100" align="right">
            <template #default="scope">
              {{ formatCurrency(scope.row.saleReturnUnitPrice) }}
            </template>
          </el-table-column>

          <el-table-column prop="saleReturnQuantity" label="数量" width="100" align="right">
            <template #default="scope">
              {{ scope.row.saleReturnQuantity }}
            </template>
          </el-table-column>

          <el-table-column prop="saleReturnAmount" label="金额" width="120" align="right">
            <template #default="scope">
              <span class="negative-amount">
                {{ formatCurrency(scope.row.saleReturnAmount) }}
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
            <span class="amount-label">销售总金额：</span>
            <span class="amount-value sale-amount">{{ formatCurrency(saleTotalAmount) }}</span>
          </div>
          <div class="amount-item">
            <span class="amount-label">销售退货总金额：</span>
            <span class="amount-value sale-return-amount">{{
              formatCurrency(saleReturnTotalAmount)
            }}</span>
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
// 引入仓库组件和客户组件
import Warehouse from '@/components/goodSearchConpoent/Warehouse.vue'
import Customer from '@/components/goodSearchConpoent/Customer.vue'
import PeopleList from '@/components/goodSearchConpoent/PeopleList.vue'
import UserList from '@/components/goodSearchConpoent/UserList.vue'

import {
  salesSummaryUsingGet,
  salesSummaryOpenApiExportUsingGet
} from '@/apis/report/sales/xiaoshouhuizongbiao'
import type {
  SalesSummaryUsingGetParams,
  SalesSummaryOpenApiExportUsingGetParams
} from '@/apis/report/sales/types'

type FilterType = 'product' | 'customer' | 'user' | 'person'

interface SearchFormModel {
  filterType: FilterType
  warehouse: string
  goods: string
  customer: string
  user: string
  people: string
  startTime: string
  endTime: string
  // 下面这些是传给后端用的 id（弹窗选择后再填充）
  warehouseId?: number | null
  customerId?: string | null
  userId?: string | null
  peopleId?: string | null
}

type SalesSummaryRow = {
  id?: number | string
  personName?: string
  userName?: string
  customerName?: string
  productName?: string
  auxiliaryAttr?: string
  warehouse?: string
  unit?: string
  billDate?: string
  saleUnitPrice?: number
  saleQuantity?: number
  saleAmount?: number
  saleReturnUnitPrice?: number
  saleReturnQuantity?: number
  saleReturnAmount?: number
  totalQuantity?: number
  totalAmount?: number
}

// 搜索弹框显示状态
const searchPopoverVisible = ref(false)

// 搜索表单数据 - 默认筛选类型为"按商品"
const searchForm = reactive<SearchFormModel>({
  filterType: 'product',
  warehouse: '',
  goods: '',
  customer: '',
  user: '',
  people: '',
  startTime: '',
  endTime: '',
  warehouseId: null,
  customerId: null,
  userId: null,
  peopleId: null
})

// 计算字段禁用状态
const customerDisabled = computed(() => searchForm.filterType !== 'customer')
const userDisabled = computed(() => searchForm.filterType !== 'user')
const peopleDisabled = computed(() => searchForm.filterType !== 'person')

// 仓库弹窗控制
const showWarehouseDialog = ref(false)
const isClosingWarehouseDialog = ref(false)

// 客户弹窗控制
const showCustomerDialog = ref(false)
const isClosingCustomerDialog = ref(false)

// 人员弹窗控制
const showPeopleDialog = ref(false)
const isClosingPeopleDialog = ref(false)

// 用户弹窗控制
const showUserDialog = ref(false)
const isClosingUserDialog = ref(false)

// 分页相关
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)

// 列表数据（当前页）
const originalTableData = ref<SalesSummaryRow[]>([])

// 计算表格列的显示状态（保持原有行为）
const showPersonColumn = computed(() => searchForm.filterType === 'person')
const showUserColumn = computed(() => searchForm.filterType === 'user')
const showCustomerColumn = computed(() => searchForm.filterType === 'customer')

// 当按商品筛选时，隐藏人员相关列
const hideColumnsForProduct = computed(() => searchForm.filterType === 'product')

// 过滤后的表格数据 - 保留你原来的前端过滤 + 日期筛选逻辑
const filteredTableData = computed(() => {
  let result = [...originalTableData.value]
  const { filterType, warehouse, goods, customer, user, people, startTime, endTime } = searchForm

  // 仓库过滤
  if (warehouse) {
    result = result.filter((item) => (item.warehouse || '').includes(warehouse))
  }

  // 商品名称过滤
  if (goods) {
    result = result.filter((item) => (item.productName || '').includes(goods))
  }

  // 根据筛选类型进行其他过滤
  if (filterType === 'customer' && customer) {
    result = result.filter((item) => (item.customerName || '').includes(customer))
  }

  if (filterType === 'user' && user) {
    result = result.filter((item) => (item.userName || '').includes(user))
  }

  if (filterType === 'person' && people) {
    result = result.filter((item) => (item.personName || '').includes(people))
  }

  // 日期范围过滤
  if (startTime && endTime) {
    result = result.filter((item) => {
      if (!item.billDate) return false
      const billDate = new Date(item.billDate)
      const startDate = new Date(startTime)
      const endDateObj = new Date(endTime)
      return billDate >= startDate && billDate <= endDateObj
    })
  } else if (startTime) {
    // 只有开始日期
    result = result.filter((item) => {
      if (!item.billDate) return false
      const billDate = new Date(item.billDate)
      const startDate = new Date(startTime)
      return billDate >= startDate
    })
  } else if (endTime) {
    // 只有结束日期
    result = result.filter((item) => {
      if (!item.billDate) return false
      const billDate = new Date(item.billDate)
      const endDateObj = new Date(endTime)
      return billDate <= endDateObj
    })
  }

  return result
})

// 计算销售总金额、销售退货总金额和汇总金额（兼容后端返回 null/undefined）
const saleTotalAmount = computed(() =>
  filteredTableData.value.reduce((sum, item) => sum + (Number(item.saleAmount) || 0), 0)
)

const saleReturnTotalAmount = computed(() =>
  filteredTableData.value.reduce((sum, item) => sum + (Number(item.saleReturnAmount) || 0), 0)
)

const totalAmount = computed(() => saleTotalAmount.value - saleReturnTotalAmount.value)

// 筛选类型改变处理
const handleFilterTypeChange = () => {
  // 重置相关字段（名称 + id）
  if (searchForm.filterType !== 'customer') {
    searchForm.customer = ''
    searchForm.customerId = null
  }
  if (searchForm.filterType !== 'user') {
    searchForm.user = ''
    searchForm.userId = null
  }
  if (searchForm.filterType !== 'person') {
    searchForm.people = ''
    searchForm.peopleId = null
  }
}

// 仓库相关处理函数
const handleWarehouseInputFocus = () => {
  if (!isClosingWarehouseDialog.value) {
    showWarehouseDialog.value = true
  }
}

const handleClearWarehouse = () => {
  searchForm.warehouse = ''
  searchForm.warehouseId = null
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
    const id = params.id ?? params.warehouseId ?? params.value
    searchForm.warehouse = name
    searchForm.warehouseId = typeof id === 'number' ? id : id ? Number(id) : null
  }
  showWarehouseDialog.value = false
}

// 客户相关处理函数
const handleCustomerInputFocus = () => {
  if (!isClosingCustomerDialog.value && !customerDisabled.value) {
    showCustomerDialog.value = true
  }
}

const handleClearCustomer = () => {
  searchForm.customer = ''
  searchForm.customerId = null
}

const handleCloseCustomerDialog = () => {
  showCustomerDialog.value = false
}

const handleCustomerDialogUpdate = (val: boolean) => {
  showCustomerDialog.value = val
}

const handleCustomerDialogSearch = (params: any) => {
  if (params && typeof params === 'object') {
    const name = params.name || params.customerName || ''
    const id = params.id ?? params.customerId ?? params.value
    searchForm.customer = name
    searchForm.customerId = id != null ? String(id) : null
  }
  showCustomerDialog.value = false
}

// 人员相关处理函数
const handlePeopleInputFocus = () => {
  if (!isClosingPeopleDialog.value && !peopleDisabled.value) {
    showPeopleDialog.value = true
  }
}

const handleClearPeople = () => {
  searchForm.people = ''
  searchForm.peopleId = null
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
    const id = params.id ?? params.peopleId ?? params.value
    searchForm.people = name
    searchForm.peopleId = id != null ? String(id) : null
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
  searchForm.userId = null
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
    const id = params.id ?? params.userId ?? params.value
    searchForm.user = name
    searchForm.userId = id != null ? String(id) : null
  }
  showUserDialog.value = false
}

// 映射筛选类型到后端 groupBy 参数
const mapFilterTypeToGroupBy = (filterType: FilterType): SalesSummaryUsingGetParams['groupBy'] => {
  if (filterType === 'customer') return 'customer'
  if (filterType === 'user') return 'user'
  if (filterType === 'person') return 'people' // 注意：后端是 people
  return 'product'
}

// 构建公共查询参数（搜索 + 导出共用）
const buildCommonParams = () => {
  const params: Partial<SalesSummaryUsingGetParams> = {
    groupBy: mapFilterTypeToGroupBy(searchForm.filterType)
  }

  if (searchForm.startTime) params.startDate = searchForm.startTime
  if (searchForm.endTime) params.endDate = searchForm.endTime
  if (searchForm.goods) params.productName = searchForm.goods
  if (searchForm.warehouseId != null) params.warehouseId = searchForm.warehouseId ?? undefined
  if (searchForm.customerId) params.customerId = searchForm.customerId ?? undefined
  if (searchForm.userId) params.userId = searchForm.userId ?? undefined
  if (searchForm.peopleId) params.peopleId = searchForm.peopleId ?? undefined

  return params
}

// 加载表格数据（真正调用后端接口）
const loadTableData = async () => {
  loading.value = true
  try {
    const params: SalesSummaryUsingGetParams = {
      pageIndex: currentPage.value,
      pageSize: pageSize.value,
      ...buildCommonParams()
    } as SalesSummaryUsingGetParams

    const res = await salesSummaryUsingGet({ params })
    const anyRes: any = res

    let pageWrapper: any

    if (anyRes?.data?.data?.rows || anyRes?.data?.data?.total != null) {
      pageWrapper = anyRes.data.data
    } else if (anyRes?.data?.rows || anyRes?.data?.total != null) {
      pageWrapper = anyRes.data
    } else if (anyRes?.rows || anyRes?.total != null) {
      pageWrapper = anyRes
    } else {
      console.warn('【销售汇总】未识别的分页结构', anyRes)
      pageWrapper = { rows: [], total: 0 }
    }

    const list = pageWrapper?.rows ?? []
    const totalCount = pageWrapper?.total ?? 0

    // 不做字段映射，直接认为后端字段与表格字段一致
    originalTableData.value = list as SalesSummaryRow[]
    total.value = totalCount
    // ✅ 在这里把后端嵌套结构展开成表格用的字段
    originalTableData.value = list.map((item: any) => {
      const row: SalesSummaryRow = {
        // 分组字段（看你需要用到的话，可以放到某个显示字段中）
        // groupField: item.groupField,

        // 基本信息
        productName: item.productName,
        auxiliaryAttr: item.attribute, // 后端是 attribute
        warehouse: item.warehouse,
        unit: item.unit,

        // 如果后端有日期字段就用，没有就留空
        billDate: item.billDate ?? '',

        // 销售单
        saleUnitPrice: item.sales?.price ?? 0,
        saleQuantity: item.sales?.quantity ?? 0,
        saleAmount: item.sales?.amount ?? 0,

        // 销售退货单
        saleReturnUnitPrice: item.returnSales?.price ?? 0,
        saleReturnQuantity: item.returnSales?.quantity ?? 0,
        saleReturnAmount: item.returnSales?.amount ?? 0,

        // 汇总
        totalQuantity: item.summary?.totalQuantity ?? 0,
        totalAmount: item.summary?.totalAmount ?? 0
      }

      return row
    })

    total.value = totalCount
  } catch (error: any) {
    console.error('获取销售汇总失败', error)
    ElMessage.error(error?.message || '获取销售汇总失败')
    originalTableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 搜索处理：保留原来的提示文案 + 触发接口请求
const handleSearch = () => {
  const searchConditions: string[] = []
  if (searchForm.warehouse) {
    searchConditions.push(`仓库：${searchForm.warehouse}`)
  }
  if (searchForm.goods) {
    searchConditions.push(`商品：${searchForm.goods}`)
  }
  if (searchForm.customer) {
    searchConditions.push(`客户：${searchForm.customer}`)
  }
  if (searchForm.user) {
    searchConditions.push(`用户：${searchForm.user}`)
  }
  if (searchForm.people) {
    searchConditions.push(`人员：${searchForm.people}`)
  }
  if (searchForm.startTime && searchForm.endTime) {
    searchConditions.push(`日期：${searchForm.startTime} 至 ${searchForm.endTime}`)
  }

  const searchInfo =
    searchConditions.length > 0 ? `搜索条件：${searchConditions.join('，')}` : '显示全部数据'

  searchPopoverVisible.value = false
  ElMessage.success(`搜索完成，${searchInfo}`)

  currentPage.value = 1
  loadTableData()
}

// 重置搜索
const handleResetSearch = () => {
  Object.assign(searchForm, {
    filterType: 'product',
    warehouse: '',
    goods: '',
    customer: '',
    user: '',
    people: '',
    startTime: '',
    endTime: '',
    warehouseId: null,
    customerId: null,
    userId: null,
    peopleId: null
  } as SearchFormModel)
  ElMessage.info('已重置搜索条件')
}

// 刷新
const handleRefresh = () => {
  handleResetSearch()
  currentPage.value = 1
  loadTableData()
  ElMessage.success('数据已刷新')
}

// 导出功能联调后端接口
const handleExport = async () => {
  try {
    ElMessage.info('正在导出数据...')

    const commonParams = buildCommonParams()
    const params: SalesSummaryOpenApiExportUsingGetParams = {
      pageIndex: 1,
      pageSize: 99999,
      ...(commonParams as any)
    }

    const res = await salesSummaryOpenApiExportUsingGet({ params })
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
    link.download = `销售汇总表_${new Date().toISOString().split('T')[0]}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    URL.revokeObjectURL(url)

    ElMessage.success('导出成功，已开始下载文件')
  } catch (error: any) {
    console.error('导出销售汇总失败', error)
    ElMessage.error(error?.message || '导出销售汇总失败')
  }
}

// 金额格式化
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

// 获取金额显示样式
const getTotalAmountClass = (row: any) => {
  const val = Number(row?.totalAmount) || 0
  return val >= 0 ? 'positive-amount' : 'negative-amount'
}

// 分页处理：触发重新请求
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  loadTableData()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadTableData()
}

// 监听弹窗状态，防止重复弹出
watch(showWarehouseDialog, (newVal, oldVal) => {
  if (!newVal && oldVal) {
    isClosingWarehouseDialog.value = true
    setTimeout(() => {
      isClosingWarehouseDialog.value = false
    }, 200)
  }
})

watch(showCustomerDialog, (newVal, oldVal) => {
  if (!newVal && oldVal) {
    isClosingCustomerDialog.value = true
    setTimeout(() => {
      isClosingCustomerDialog.value = false
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

// 组件挂载时设置默认值并加载一次数据
onMounted(() => {
  searchForm.filterType = 'product'

  const endDate = new Date()
  const startDate = new Date()
  startDate.setDate(endDate.getDate() - 30)

  searchForm.startTime = formatDate(startDate)
  searchForm.endTime = formatDate(endDate)

  loadTableData()
})

// 日期格式化函数
const formatDate = (date: Date): string => {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}
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

/* .sale-amount {
  color: #67c23a;
  border-color: #67c23a;
}

.sale-return-amount {
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
