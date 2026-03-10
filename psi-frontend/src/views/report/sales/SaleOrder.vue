<template>
  <div class="sys area">
    <!-- 操作按钮区域 -->
    <div class="operation-bar">
      <div class="operation-left">
        <!-- 使用搜索组件 -->
        <GoodSearch
          :modelValue="searchFormData"
          :config="searchConfig"
          @update:modelValue="handleSearchFormUpdate"
          @search="handleGoodSearch"
          ref="goodSearchRef"
        />
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

    <el-divider class="custom-divider" />

    <!-- 表格区域 - 占据主要空间 -->
    <div class="table-container">
      <el-table
        :data="currentPageData"
        style="width: 100%"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        v-loading="loading"
        :default-expand-all="false"
        border
        class="grid-table"
      >
        <!-- 商品排序模式下的列顺序 -->
        <template v-if="isProductSort">
          <!-- 商品名称列 -->
          <el-table-column prop="productName" label="商品名称" width="150">
            <template #default="scope">
              <span class="parent-row">
                {{ scope.row.productName }}
              </span>
            </template>
          </el-table-column>

          <!-- 辅助属性列 -->
          <el-table-column prop="auxiliaryAttr" label="辅助属性" width="120">
            <template #default="scope">
              <span class="parent-row">
                {{ scope.row.auxiliaryAttr }}
              </span>
            </template>
          </el-table-column>

          <!-- 仓库列 -->
          <el-table-column prop="warehouse" label="仓库" width="120">
            <template #default="scope">
              <span class="parent-row">
                {{ scope.row.warehouse }}
              </span>
            </template>
          </el-table-column>

          <!-- 所属组织列 -->
          <el-table-column prop="orgName" label="所属组织" width="120" align="center">
            <template #default="scope">
              <span v-if="!scope.row.isChild">
                {{ getOrgSummary(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.orgName }}
              </span>
            </template>
          </el-table-column>

          <!-- 客户列 -->
          <el-table-column prop="customerName" label="客户" width="150">
            <template #default="scope">
              <span v-if="!scope.row.isChild">
                {{ getCustomerSummary(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.customerName }}
              </span>
            </template>
          </el-table-column>

          <!-- 单据时间列 -->
          <el-table-column prop="orderDate" label="单据时间" width="120" align="center">
            <template #default="scope">
              <span v-if="!scope.row.isChild">
                {{ getDateRange(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.orderDate }}
              </span>
            </template>
          </el-table-column>

          <!-- 单据编号列 -->
          <el-table-column prop="orderNo" label="单据编号" width="150">
            <template #default="scope">
              <span v-if="!scope.row.isChild">
                {{ getOrderNoSummary(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.orderNo }}
              </span>
            </template>
          </el-table-column>

          <!-- 单位列 -->
          <el-table-column prop="unit" label="单位" width="80" align="center">
            <template #default="scope">
              <span v-if="scope.row.isChild">
                {{ scope.row.unit }}
              </span>
              <span v-else>-</span>
            </template>
          </el-table-column>

          <!-- 单价列 -->
          <el-table-column prop="unitPrice" label="单价" width="100" align="right">
            <template #default="scope">
              <span v-if="!scope.row.isChild" class="parent-row">
                {{ getAverageUnitPrice(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.unitPrice }}
              </span>
            </template>
          </el-table-column>

          <!-- 数量列 -->
          <el-table-column prop="quantity" label="数量" width="100" align="right">
            <template #default="scope">
              <span v-if="!scope.row.isChild" class="parent-row">
                {{ getTotalQuantity(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.quantity }}
              </span>
            </template>
          </el-table-column>

          <!-- 金额列 -->
          <el-table-column prop="amount" label="金额" width="120" align="right">
            <template #default="scope">
              <span v-if="!scope.row.isChild" class="parent-row">
                {{ getTotalAmount(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.amount }}
              </span>
            </template>
          </el-table-column>

          <!-- 出库状态列 -->
          <el-table-column prop="storageStatus" label="出库状态" width="100" align="center">
            <template #default="scope">
              <span v-if="!scope.row.isChild">
                {{ getStorageStatusSummary(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.storageStatus }}
              </span>
            </template>
          </el-table-column>

          <!-- 未出库数量列 -->
          <el-table-column prop="unstockedQuantity" label="未出库数量" width="120" align="right">
            <template #default="scope">
              <span v-if="!scope.row.isChild" class="parent-row">
                {{ getTotalUnstockedQuantity(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.unstockedQuantity }}
              </span>
            </template>
          </el-table-column>

          <!-- 未出库金额列 -->
          <el-table-column prop="unstockedAmount" label="未出库金额" width="120" align="right">
            <template #default="scope">
              <span v-if="!scope.row.isChild" class="parent-row">
                {{ getTotalUnstockedAmount(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.unstockedAmount }}
              </span>
            </template>
          </el-table-column>

          <!-- 发货日期列 -->
          <el-table-column prop="deliveryDate" label="发货日期" width="120" align="center">
            <template #default="scope">
              <span v-if="!scope.row.isChild">
                {{ getDeliveryDateRange(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.deliveryDate }}
              </span>
            </template>
          </el-table-column>

          <!-- 备注信息列 -->
          <el-table-column prop="remark" label="备注信息" min-width="150">
            <template #default="scope">
              <span v-if="scope.row.isChild">
                {{ scope.row.remark }}
              </span>
              <span v-else>-</span>
            </template>
          </el-table-column>
        </template>

        <!-- 单据排序模式下的列顺序（原顺序） -->
        <template v-else>
          <!-- 所属组织列 -->
          <el-table-column prop="orgName" label="所属组织" width="120" align="center">
            <template #default="scope">
              <span v-if="!scope.row.isChild" class="parent-row">
                {{ scope.row.orgName }}
              </span>
              <span v-else class="child-row">
                {{ scope.row.orgName }}
              </span>
            </template>
          </el-table-column>

          <!-- 客户列 -->
          <el-table-column prop="customerName" label="客户" width="150">
            <template #default="scope">
              <span v-if="!scope.row.isChild" class="parent-row">
                {{ scope.row.customerName }}
              </span>
              <span v-else class="child-row">
                {{ scope.row.customerName }}
              </span>
            </template>
          </el-table-column>

          <!-- 其他数据列 - 在父级行显示汇总信息，子级行显示详细信息 -->
          <el-table-column prop="orderDate" label="单据时间" width="120" align="center">
            <template #default="scope">
              <span v-if="!scope.row.isChild">
                {{ getDateRange(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.orderDate }}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="orderNo" label="单据编号" width="150">
            <template #default="scope">
              <span v-if="!scope.row.isChild">
                {{ getOrderNoSummary(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.orderNo }}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="productName" label="商品名称" width="150">
            <template #default="scope">
              <span v-if="!scope.row.isChild">
                {{ getProductSummary(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.productName }}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="auxiliaryAttr" label="辅助属性" width="120">
            <template #default="scope">
              <span v-if="scope.row.isChild">
                {{ scope.row.auxiliaryAttr }}
              </span>
              <span v-else>-</span>
            </template>
          </el-table-column>

          <el-table-column prop="warehouse" label="仓库" width="120">
            <template #default="scope">
              <span v-if="!scope.row.isChild">
                {{ getWarehouseSummary(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.warehouse }}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="unit" label="单位" width="80" align="center">
            <template #default="scope">
              <span v-if="scope.row.isChild">
                {{ scope.row.unit }}
              </span>
              <span v-else>-</span>
            </template>
          </el-table-column>

          <el-table-column prop="unitPrice" label="单价" width="100" align="right">
            <template #default="scope">
              <span v-if="scope.row.isChild">
                {{ scope.row.unitPrice }}
              </span>
              <span v-else>-</span>
            </template>
          </el-table-column>

          <el-table-column prop="quantity" label="数量" width="100" align="right">
            <template #default="scope">
              <span v-if="!scope.row.isChild" class="parent-row">
                {{ getTotalQuantity(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.quantity }}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="amount" label="金额" width="120" align="right">
            <template #default="scope">
              <span v-if="!scope.row.isChild" class="parent-row">
                {{ getTotalAmount(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.amount }}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="storageStatus" label="出库状态" width="100" align="center">
            <template #default="scope">
              <span v-if="!scope.row.isChild">
                {{ getStorageStatusSummary(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.storageStatus }}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="unstockedQuantity" label="未出库数量" width="120" align="right">
            <template #default="scope">
              <span v-if="!scope.row.isChild" class="parent-row">
                {{ getTotalUnstockedQuantity(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.unstockedQuantity }}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="unstockedAmount" label="未出库金额" width="120" align="right">
            <template #default="scope">
              <span v-if="!scope.row.isChild" class="parent-row">
                {{ getTotalUnstockedAmount(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.unstockedAmount }}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="deliveryDate" label="发货日期" width="120" align="center">
            <template #default="scope">
              <span v-if="!scope.row.isChild">
                {{ getDeliveryDateRange(scope.row) }}
              </span>
              <span v-else>
                {{ scope.row.deliveryDate }}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="remark" label="备注信息" min-width="150">
            <template #default="scope">
              <span v-if="scope.row.isChild">
                {{ scope.row.remark }}
              </span>
              <span v-else>-</span>
            </template>
          </el-table-column>
        </template>
      </el-table>

      <!-- 分页区域 -->
      <div class="pagination-container pagination-left">
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
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Download, Refresh } from '@element-plus/icons-vue'
import GoodSearch from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent/type'

// 搜索组件引用
const goodSearchRef = ref()

// 原始数据 - 保持不变
const originalTableData = ref([
  {
    id: 1,
    orgName: '总部',
    customerName: '上海客户A',
    isChild: false,
    children: [
      {
        id: 11,
        orgName: '总部',
        customerName: '上海客户A',
        orderDate: '2023-10-01',
        orderNo: 'SO20231001001',
        productName: '成品B-批次1',
        auxiliaryAttr: '规格A',
        warehouse: '一号仓库',
        unit: '件',
        unitPrice: 500,
        quantity: 50,
        amount: 25000,
        storageStatus: '已出库',
        unstockedQuantity: 0,
        unstockedAmount: 0,
        deliveryDate: '2023-10-10',
        remark: '首批发货',
        isChild: true
      },
      {
        id: 12,
        orgName: '总部',
        customerName: '上海客户A',
        orderDate: '2023-10-01',
        orderNo: 'SO20231001001',
        productName: '成品B-批次2',
        auxiliaryAttr: '规格A',
        warehouse: '二号仓库',
        unit: '件',
        unitPrice: 500,
        quantity: 50,
        amount: 25000,
        storageStatus: '未出库',
        unstockedQuantity: 50,
        unstockedAmount: 25000,
        deliveryDate: '2023-10-15',
        remark: '第二批发货',
        isChild: true
      }
    ]
  },
  {
    id: 2,
    orgName: '分部',
    customerName: '北京客户B',
    isChild: false,
    children: [
      {
        id: 21,
        orgName: '分部',
        customerName: '北京客户B',
        orderDate: '2023-10-05',
        orderNo: 'SO20231005001',
        productName: '成品A-批次1',
        auxiliaryAttr: '型号B',
        warehouse: '一号仓库',
        unit: '套',
        unitPrice: 400,
        quantity: 100,
        amount: 40000,
        storageStatus: '已出库',
        unstockedQuantity: 0,
        unstockedAmount: 0,
        deliveryDate: '2023-10-12',
        remark: '首批发货',
        isChild: true
      },
      {
        id: 22,
        orgName: '分部',
        customerName: '北京客户B',
        orderDate: '2023-10-05',
        orderNo: 'SO20231005001',
        productName: '成品A-批次2',
        auxiliaryAttr: '型号B',
        warehouse: '二号仓库',
        unit: '套',
        unitPrice: 400,
        quantity: 100,
        amount: 40000,
        storageStatus: '未出库',
        unstockedQuantity: 100,
        unstockedAmount: 40000,
        deliveryDate: '2023-10-20',
        remark: '第二批发货',
        isChild: true
      }
    ]
  },
  {
    id: 3,
    orgName: '总部',
    customerName: '广州客户C',
    isChild: false,
    children: [
      {
        id: 31,
        orgName: '总部',
        customerName: '广州客户C',
        orderDate: '2023-10-08',
        orderNo: 'SO20231008001',
        productName: '半成品C-批次1',
        auxiliaryAttr: '规格C',
        warehouse: '一号仓库',
        unit: '套',
        unitPrice: 600,
        quantity: 30,
        amount: 18000,
        storageStatus: '已出库',
        unstockedQuantity: 0,
        unstockedAmount: 0,
        deliveryDate: '2023-10-10',
        remark: '首批发货',
        isChild: true
      },
      {
        id: 32,
        orgName: '总部',
        customerName: '广州客户C',
        orderDate: '2023-10-08',
        orderNo: 'SO20231008001',
        productName: '半成品C-批次2',
        auxiliaryAttr: '规格C',
        warehouse: '二号仓库',
        unit: '套',
        unitPrice: 600,
        quantity: 20,
        amount: 12000,
        storageStatus: '已出库',
        unstockedQuantity: 0,
        unstockedAmount: 0,
        deliveryDate: '2023-10-12',
        remark: '第二批发货',
        isChild: true
      }
    ]
  }
])

// 当前显示的表格数据
const currentTableData = ref(JSON.parse(JSON.stringify(originalTableData.value)))

// 搜索表单数据 - 设置默认排序为"单据排序"
const searchFormData = reactive<SearchFormData>({
  goods: '',
  number: '',
  supplier: '', // 使用供应商字段来搜索客户
  customer: null,
  people: null,
  user: null,
  startTime: '',
  endTime: '',
  startArrival: '', // 这里代表发货日期
  endArrival: '',
  examine: '',
  state: '',
  data: '',
  warehouse: '', // 仓库字段
  sortType: '单据排序' // 默认选择单据排序
})

// 搜索配置 - 改为与采购跟踪表一致的配置
const searchConfig = reactive<GoodSearchConfig>({
  showGoods: true,
  showNumber: true,
  showSupplier: true, // 显示供应商字段（实际用于搜索客户）
  showCustomer: false, // 隐藏客户字段
  showPeople: false,
  showBillDate: true,
  showArrivalDate: true, // 这里代表发货日期
  showUser: false,
  showExamine: false,
  showState: false,
  showRemark: false,
  showWarehouse: true, // 启用默认的仓库搜索
  inline: false,
  customFields: [
    {
      key: 'sortType',
      type: 'select',
      label: '排列方式',
      options: [
        { label: '单据排序', value: '单据排序' },
        { label: '商品排序', value: '商品排序' }
      ]
    }
  ]
})

// 计算是否处于商品排序模式
const isProductSort = computed(() => {
  return searchFormData.sortType === '商品排序'
})

// 处理搜索表单更新
const handleSearchFormUpdate = (newValue: SearchFormData) => {
  Object.assign(searchFormData, newValue)
}

// 处理搜索组件的搜索事件
const handleGoodSearch = (formData: SearchFormData) => {
  console.log('搜索条件:', formData)
  applySearchFilters(formData)
  ElMessage.success('搜索完成')
}

// 将原始数据转换为商品排序模式的数据结构
const transformToProductStructure = (data: any[]) => {
  // 首先收集所有子节点
  const allChildren: any[] = []
  data.forEach((parent) => {
    if (parent.children && parent.children.length > 0) {
      parent.children.forEach((child: any) => {
        allChildren.push({ ...child })
      })
    }
  })

  // 按商品名称、辅助属性、仓库分组
  const productGroups: { [key: string]: any[] } = {}

  allChildren.forEach((child) => {
    const key = `${child.productName}_${child.auxiliaryAttr}_${child.warehouse}`
    if (!productGroups[key]) {
      productGroups[key] = []
    }
    productGroups[key].push(child)
  })

  // 创建商品分组数据结构
  const productData = Object.entries(productGroups).map(([key, children], index) => {
    const firstChild = children[0]
    return {
      id: `product_${index + 1}`,
      productName: firstChild.productName,
      auxiliaryAttr: firstChild.auxiliaryAttr,
      warehouse: firstChild.warehouse,
      isChild: false,
      children: children.map((child, childIndex) => ({
        ...child,
        id: `product_${index + 1}_child_${childIndex + 1}`,
        // 子数据的商品名称、辅助属性、仓库与根数据一致
        productName: firstChild.productName,
        auxiliaryAttr: firstChild.auxiliaryAttr,
        warehouse: firstChild.warehouse,
        isChild: true
      }))
    }
  })

  // 按商品名称拼音首字母排序
  return productData.sort((a, b) => a.productName.localeCompare(b.productName, 'zh-CN'))
}

// 应用搜索过滤
const applySearchFilters = (formData: SearchFormData) => {
  let filteredData = JSON.parse(JSON.stringify(originalTableData.value))

  // 根据搜索条件过滤数据
  filteredData = filteredData.filter(
    (parent: { customerName: string | string[]; children: any[] }) => {
      // 过滤客户（在销售表中对应customerName，搜索条件用supplier字段）
      if (formData.supplier && !parent.customerName.includes(formData.supplier as string)) {
        return false
      }

      // 过滤商品名称（在子级中查找）
      if (formData.goods) {
        const hasMatchingChild = parent.children?.some(
          (child: { productName: string | string[] }) =>
            child.productName.includes(formData.goods as string)
        )
        if (!hasMatchingChild) return false
      }

      // 过滤单据编号
      if (formData.number) {
        const hasMatchingChild = parent.children?.some((child: { orderNo: string | string[] }) =>
          child.orderNo.includes(formData.number as string)
        )
        if (!hasMatchingChild) return false
      }

      // 过滤仓库 - 使用默认的仓库搜索（返回字符串）
      if (formData.warehouse) {
        const warehouseValue = formData.warehouse
        const filteredChildren = parent.children?.filter(
          (child: { warehouse: any }) => child.warehouse === warehouseValue
        )
        if (!filteredChildren || filteredChildren.length === 0) return false
        parent.children = filteredChildren
      }

      // 过滤单据日期范围
      if (formData.startTime || formData.endTime) {
        const filteredChildren = parent.children?.filter(
          (child: { orderDate: string | number | Date }) => {
            const orderDate = new Date(child.orderDate)
            const startDate = formData.startTime ? new Date(formData.startTime) : null
            const endDate = formData.endTime ? new Date(formData.endTime) : null

            if (startDate && orderDate < startDate) return false
            if (endDate && orderDate > endDate) return false
            return true
          }
        )
        if (!filteredChildren || filteredChildren.length === 0) return false
        parent.children = filteredChildren
      }

      // 过滤发货日期范围（使用arrival字段代表发货日期）
      if (formData.startArrival || formData.endArrival) {
        const filteredChildren = parent.children?.filter(
          (child: { deliveryDate: string | number | Date }) => {
            const deliveryDate = new Date(child.deliveryDate)
            const startDate = formData.startArrival ? new Date(formData.startArrival) : null
            const endDate = formData.endArrival ? new Date(formData.endArrival) : null

            if (startDate && deliveryDate < startDate) return false
            if (endDate && deliveryDate > endDate) return false
            return true
          }
        )
        if (!filteredChildren || filteredChildren.length === 0) return false
        parent.children = filteredChildren
      }

      return true
    }
  )

  // 排序功能
  if (formData.sortType === '商品排序') {
    // 商品排序：转换为商品分组结构并按商品名称拼音排序
    filteredData = transformToProductStructure(filteredData)
  } else if (formData.sortType === '单据排序') {
    // 单据排序：按单据编号从小到大排列
    filteredData = filteredData.map((parent: { children: any }) => {
      const sortedChildren = [...(parent.children || [])].sort((a, b) =>
        a.orderNo.localeCompare(b.orderNo)
      )
      return { ...parent, children: sortedChildren }
    })

    // 同时按单据编号对父级行进行排序
    filteredData.sort((a: any, b: any) => {
      const aFirstOrder = a.children?.[0]?.orderNo || ''
      const bFirstOrder = b.children?.[0]?.orderNo || ''
      return aFirstOrder.localeCompare(bFirstOrder)
    })
  }

  // 更新当前显示的数据
  currentTableData.value = filteredData
  currentPage.value = 1 // 重置到第一页
}

// 刷新功能
const handleRefresh = () => {
  // 重置搜索表单
  if (goodSearchRef.value) {
    goodSearchRef.value.resetForm()
  }

  // 重置搜索条件，但保持默认排序为"单据排序"
  Object.assign(searchFormData, {
    goods: '',
    number: '',
    supplier: '', // 改为供应商字段
    startTime: '',
    endTime: '',
    startArrival: '',
    endArrival: '',
    warehouse: '', // 重置仓库字段
    sortType: '单据排序' // 重置为默认排序
  })

  // 恢复显示所有原始数据
  currentTableData.value = JSON.parse(JSON.stringify(originalTableData.value))
  currentPage.value = 1 // 重置到第一页

  ElMessage.success('已刷新数据，显示全部记录')
}

// 导出功能实现
const handleExport = () => {
  const exportData = prepareExportData()
  ElMessage.success('正在导出数据...')
  setTimeout(() => {
    downloadCSV(exportData)
    ElMessage.success('导出成功，已开始下载文件')
  }, 1000)
}

// 准备导出数据
const prepareExportData = () => {
  const flatData: any[] = []

  if (isProductSort.value) {
    // 商品排序模式下的导出数据
    currentTableData.value.forEach((parent: any) => {
      // 添加父级数据（商品信息）
      flatData.push({
        商品名称: parent.productName,
        辅助属性: parent.auxiliaryAttr,
        仓库: parent.warehouse,
        所属组织: getOrgSummary(parent),
        客户: getCustomerSummary(parent),
        单据时间: getDateRange(parent),
        单据编号: getOrderNoSummary(parent),
        单位: '-',
        单价: getAverageUnitPrice(parent),
        数量: getTotalQuantity(parent),
        金额: getTotalAmount(parent),
        出库状态: getStorageStatusSummary(parent),
        未出库数量: getTotalUnstockedQuantity(parent),
        未出库金额: getTotalUnstockedAmount(parent),
        发货日期: getDeliveryDateRange(parent),
        备注信息: '-'
      })

      // 添加子级数据
      if (parent.children && parent.children.length > 0) {
        parent.children.forEach((child: any) => {
          flatData.push({
            商品名称: child.productName,
            辅助属性: child.auxiliaryAttr,
            仓库: child.warehouse,
            所属组织: child.orgName,
            客户: child.customerName,
            单据时间: child.orderDate,
            单据编号: child.orderNo,
            单位: child.unit,
            单价: child.unitPrice,
            数量: child.quantity,
            金额: child.amount,
            出库状态: child.storageStatus,
            未出库数量: child.unstockedQuantity,
            未出库金额: child.unstockedAmount,
            发货日期: child.deliveryDate,
            备注信息: child.remark
          })
        })
      }
    })
  } else {
    // 单据排序模式下的导出数据
    currentTableData.value.forEach((parent: any) => {
      // 添加父级数据
      flatData.push({
        所属组织: parent.orgName,
        客户: parent.customerName,
        单据时间: getDateRange(parent),
        单据编号: getOrderNoSummary(parent),
        商品名称: getProductSummary(parent),
        辅助属性: '-',
        仓库: getWarehouseSummary(parent),
        单位: '-',
        单价: '-',
        数量: getTotalQuantity(parent),
        金额: getTotalAmount(parent),
        出库状态: getStorageStatusSummary(parent),
        未出库数量: getTotalUnstockedQuantity(parent),
        未出库金额: getTotalUnstockedAmount(parent),
        发货日期: getDeliveryDateRange(parent),
        备注信息: '-'
      })

      // 添加子级数据
      if (parent.children && parent.children.length > 0) {
        parent.children.forEach((child: any) => {
          flatData.push({
            所属组织: child.orgName,
            客户: child.customerName,
            单据时间: child.orderDate,
            单据编号: child.orderNo,
            商品名称: child.productName,
            辅助属性: child.auxiliaryAttr,
            仓库: child.warehouse,
            单位: child.unit,
            单价: child.unitPrice,
            数量: child.quantity,
            金额: child.amount,
            出库状态: child.storageStatus,
            未出库数量: child.unstockedQuantity,
            未出库金额: child.unstockedAmount,
            发货日期: child.deliveryDate,
            备注信息: child.remark
          })
        })
      }
    })
  }

  return flatData
}

// 下载CSV文件
const downloadCSV = (data: any[]) => {
  const headers = Object.keys(data[0]).join(',')
  const rows = data.map((row) =>
    Object.values(row)
      .map((value) => `"${value}"`)
      .join(',')
  )
  const csvContent = [headers, ...rows].join('\n')
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  const url = URL.createObjectURL(blob)
  link.setAttribute('href', url)
  link.setAttribute('download', `销售订单跟踪表_${new Date().toISOString().split('T')[0]}.csv`)
  link.style.visibility = 'hidden'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)

// 计算总数据条数（只计算父级行）
const total = computed(() => {
  return currentTableData.value.length
})

// 当前页数据（只包含父级行）
const currentPageData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return currentTableData.value.slice(start, end)
})

// 分页处理
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
}

// 辅助函数 - 父级行显示汇总信息
const getDateRange = (parent: any) => {
  if (!parent.children || parent.children.length === 0) return '-'
  const dates = parent.children.map((child: any) => child.orderDate)
  const minDate = Math.min(...dates.map((date: string) => new Date(date).getTime()))
  const maxDate = Math.max(...dates.map((date: string) => new Date(date).getTime()))
  return `${new Date(minDate).toISOString().split('T')[0]} ~ ${new Date(maxDate).toISOString().split('T')[0]}`
}

const getOrderNoSummary = (parent: any) => {
  if (!parent.children || parent.children.length === 0) return '-'
  const orderNos = [...new Set(parent.children.map((child: any) => child.orderNo))]
  return orderNos.length === 1 ? orderNos[0] : `${orderNos.length}个单据`
}

const getProductSummary = (parent: any) => {
  if (!parent.children || parent.children.length === 0) return '-'
  const products = [...new Set(parent.children.map((child: any) => child.productName))]
  return products.length === 1 ? products[0] : `${products.length}种商品`
}

const getOrgSummary = (parent: any) => {
  if (!parent.children || parent.children.length === 0) return '-'
  const orgs = [...new Set(parent.children.map((child: any) => child.orgName))]
  return orgs.length === 1 ? orgs[0] : `${orgs.length}个组织`
}

const getCustomerSummary = (parent: any) => {
  if (!parent.children || parent.children.length === 0) return '-'
  const customers = [...new Set(parent.children.map((child: any) => child.customerName))]
  return customers.length === 1 ? customers[0] : `${customers.length}个客户`
}

const getWarehouseSummary = (parent: any) => {
  if (!parent.children || parent.children.length === 0) return '-'
  const warehouses = [...new Set(parent.children.map((child: any) => child.warehouse))]
  return warehouses.length === 1 ? warehouses[0] : `${warehouses.length}个仓库`
}

const getTotalQuantity = (parent: any) => {
  if (!parent.children || parent.children.length === 0) return 0
  return parent.children.reduce((sum: number, child: any) => sum + child.quantity, 0)
}

const getTotalAmount = (parent: any) => {
  if (!parent.children || parent.children.length === 0) return 0
  return parent.children.reduce((sum: number, child: any) => sum + child.amount, 0)
}

const getStorageStatusSummary = (parent: any) => {
  if (!parent.children || parent.children.length === 0) return '-'
  const statusCount: Record<string, number> = {}
  parent.children.forEach((child: any) => {
    statusCount[child.storageStatus] = (statusCount[child.storageStatus] || 0) + 1
  })
  return Object.entries(statusCount)
    .map(([status, count]) => `${status}(${count})`)
    .join(', ')
}

const getTotalUnstockedQuantity = (parent: any) => {
  if (!parent.children || parent.children.length === 0) return 0
  return parent.children.reduce((sum: number, child: any) => sum + child.unstockedQuantity, 0)
}

const getTotalUnstockedAmount = (parent: any) => {
  if (!parent.children || parent.children.length === 0) return 0
  return parent.children.reduce((sum: number, child: any) => sum + child.unstockedAmount, 0)
}

const getDeliveryDateRange = (parent: any) => {
  if (!parent.children || parent.children.length === 0) return '-'
  const dates = parent.children.map((child: any) => child.deliveryDate)
  const minDate = Math.min(...dates.map((date: string) => new Date(date).getTime()))
  const maxDate = Math.max(...dates.map((date: string) => new Date(date).getTime()))
  return `${new Date(minDate).toISOString().split('T')[0]} ~ ${new Date(maxDate).toISOString().split('T')[0]}`
}

// 商品排序模式下的新增辅助函数
const getAverageUnitPrice = (parent: any) => {
  if (!parent.children || parent.children.length === 0) return 0
  const totalAmount = parent.children.reduce((sum: number, child: any) => sum + child.amount, 0)
  const totalQuantity = parent.children.reduce((sum: number, child: any) => sum + child.quantity, 0)
  return totalQuantity > 0 ? (totalAmount / totalQuantity).toFixed(2) : 0
}

// 初始化
onMounted(() => {
  // 初始化数据
  currentTableData.value = JSON.parse(JSON.stringify(originalTableData.value))
  // 应用默认排序
  applySearchFilters(searchFormData)
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

.custom-divider {
  margin: 8px 0;
  border-color: #e4e7ed;
}

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

.pagination-container {
  padding: 12px 16px;
  border-top: 1px solid #ebeef5;
  background: #fafafa;
  display: flex;
}

.pagination-left {
  justify-content: flex-start;
}

:deep(.el-table) {
  flex: 1;
}

.parent-row {
  font-weight: 600;
  color: #303133;
}

.child-row {
  color: #606266;
}

.parent-placeholder {
  color: #c0c4cc;
  font-style: italic;
}
</style>
