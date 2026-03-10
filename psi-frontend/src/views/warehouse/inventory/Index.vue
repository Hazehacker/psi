<!--
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-26 01:49:50
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-12 17:46:08
 * @FilePath: \psi-frontend\src\views\warehouse\inventory\InventoryQuery.vue
 * @Description: 仓库管理-库存查询主页面
-->
<template>
  <div class="stock area">
    <div class="layout">
      <SearchPopover v-model="searchFrom" @search="handleSearch(1)" />
      <el-button-group>
        <el-button @click="handleExport">导出</el-button>
        <el-button @click="handleReload">刷新</el-button>
      </el-button-group>
    </div>
    <el-divider />

    <NormalTable
      :tabdata="tablePageData"
      :taboperbtns="tableOperBtns"
      :istabexpand="false"
      :istabmultiple="false"
      :tabattr="tableAttr"
      @page-change="handlePageChange"
      @taboper-click="handleTableOperClick"
    >
      <!-- 多级表头定义 -->
      <template #columns>
        <el-table-column prop="name" label="商品名称" width="160px" fixed="left" />
        <el-table-column prop="totalStock" label="库存数量" width="120px" />
        <el-table-column label="仓库">
          <template v-for="wh in tableColumn" :key="wh.warehouseId">
            <el-table-column
              :prop="`warehouse_${wh.warehouseId}.stockNum`"
              :label="wh.name"
              width="90px"
            />
          </template>
        </el-table-column>
        <el-table-column prop="threshold" label="预警阈值" min-width="120px" />
        <el-table-column prop="number" label="商品编号" min-width="160px" />
        <el-table-column prop="spec" label="规格型号" min-width="160px" />
        <el-table-column prop="categoryName" label="商品分类" min-width="120px" />
        <el-table-column prop="brand" label="商品品牌" min-width="120px" />
        <el-table-column prop="unit" label="商品单位" min-width="120px" />
        <el-table-column prop="code" label="商品条码" min-width="160px" />
        <el-table-column prop="remark" label="商品备注" min-width="160px" />

        <!-- 操作列 - 使用自定义模板 -->
        <el-table-column label="相关操作" min-width="100px" fixed="right">
          <template #default="scope">
            <el-button @click="showStockDetail(scope.row)" size="small">详情</el-button>
          </template>
        </el-table-column>
      </template>

      <!-- 自定义单元格内容 -->
      <template #customercell="{ column, prop, row }">
        <!-- 仓库库存显示 -->
        <template v-if="prop.startsWith('warehouse_') && prop.endsWith('.stockNum')">
          <template v-if="row.attrs && row.attrs.length > 0">
            <span>-</span>
          </template>
          <template v-else>
            {{ row[prop] }}
          </template>
        </template>

        <!-- 商品分类显示 -->
        <template v-else-if="prop === 'categoryData.name'">
          {{ row.categoryData?.name || '-' }}
        </template>

        <!-- 辅助属性名称显示 -->
        <template v-else-if="prop === 'name' && row.hasOwnProperty('pid')">
          <span style="color: #909399">└─ {{ row.attrName || row.name }}</span>
        </template>

        <!-- 辅助属性编号显示 -->
        <template v-else-if="prop === 'number' && row.hasOwnProperty('pid')">
          <span style="color: #909399">{{ row.attrCode || '-' }}</span>
        </template>

        <!-- 辅助属性库存显示 -->
        <template v-else-if="prop === 'totalStock' && row.hasOwnProperty('pid')">
          <span style="color: #909399">{{ row.totalStock || '-' }}</span>
        </template>

        <!-- 默认显示 -->
        <template v-else>
          {{ row[prop] || '-' }}
        </template>
      </template>
    </NormalTable>

    <!-- 库存详情弹窗 -->
    <DetailDialog
      v-model:visible="stockDetailDialog.visible"
      :goods-data="stockDetailDialog.goodsData"
      @closed="handleDetailDialogClose"
      @export="handleDetailExport"
    />
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import NormalTable from '@/components/normaltable/NormalTable.vue'
import type { MyTableOperationsBtn, PageDTO } from '@/components/normaltable/type'
import SearchPopover from './SearchPopover.vue'
import DetailDialog from './DetailDialog.vue'
import { getInventoryList, exportInventoryExcel } from '@/apis/warehouse/inventory/index'
import type {
  InventoryList,
  AttrStockDTO,
  WarehouseStockDTO,
  InventoryListRequest
} from '@/apis/warehouse/inventory/type'

// 响应式数据
interface TableColumn {
  warehouseId?: string
  name?: string
  key?: string
}
const tableColumn = ref<TableColumn[]>([])

// 库存详情弹窗
const stockDetailDialog = reactive({
  visible: false,
  goodsData: null as any
})

// 分页数据 - 使用 NormalTable 的 PageDTO 格式
const tablePageData = reactive<PageDTO<any>>({
  rows: [],
  total: 0,
  pageIndex: 1,
  pageSize: 30
})

// 搜索表单
const searchFrom = ref<InventoryListRequest>({
  goodsName: '',
  goodsNumber: '',
  goodsSpec: '',
  goodsCategoryId: '',
  goodsBrand: '',
  goodsCode: '',
  goodsRemark: '',
  warehouseId: [],
  stockState: undefined
})

// 表格属性配置
const tableAttr = reactive({
  'header-cell-style': { textAlign: 'center', backgroundColor: '#f5f7fa' },
  'cell-style': { textAlign: 'center' },
  'tree-props': { children: 'attrs' },
  'row-key': 'id'
})

// 表格操作按钮配置
const tableOperBtns = ref<MyTableOperationsBtn[]>([
  { text: '详情', evtname: 'detail', attr: { size: 'small', type: 'primary' } }
])

// 处理数据，将仓库记录转换为表格列数据
const processInventoryData = (data: InventoryList[]) => {
  return data.map((item) => {
    const processedItem: any = {
      ...item,
      // 添加商品ID字段，用于详情弹窗
      goodsId: item.id
    }

    // 处理仓库数据 - 使用新的 goodsWarehouses 字段
    if (item.goodsWarehouses && Array.isArray(item.goodsWarehouses)) {
      item.goodsWarehouses.forEach(function (wh: WarehouseStockDTO) {
        // 为每个仓库创建对应的属性
        if (wh.warehouseId) {
          processedItem[`warehouse_${wh.warehouseId}.stockNum`] = wh.stockNum
        }
      })
    }

    // 处理辅助属性的仓库数据
    if (item.attrs && Array.isArray(item.attrs)) {
      processedItem.attrs = item.attrs.map(function (attr: AttrStockDTO) {
        const processedAttr: Record<string, any> = {
          ...attr,
          // 设置父级ID用于树形结构
          pid: item.id,
          // 辅助属性使用attrName作为显示名称
          name: attr.attrName,
          // 辅助属性没有分类、品牌等信息
          categoryName: '-',
          brand: '-',
          spec: '-',
          unit: '-',
          code: '-',
          remark: '-',
          threshold: '-'
        }

        // 处理辅助属性的仓库数据
        if (attr.warehouses && Array.isArray(attr.warehouses)) {
          attr.warehouses.forEach(function (wh: WarehouseStockDTO) {
            if (wh.warehouseId) {
              processedAttr[`warehouse_${wh.warehouseId}.stockNum`] = wh.stockNum
            }
          })
        }

        return processedAttr
      })
    }

    return processedItem
  })
}

// 显示库存详情
const showStockDetail = (row: any) => {
  console.log('显示详情:', row)

  // 如果是辅助属性，则显示其父级商品的详情
  const goodsData = row.hasOwnProperty('pid')
    ? (tablePageData.rows || []).find((item) => item.id === row.pid) || row
    : row

  stockDetailDialog.goodsData = {
    id: goodsData.id,
    name: goodsData.name,
    number: goodsData.number,
    spec: goodsData.spec,
    categoryName: goodsData.categoryName,
    brand: goodsData.brand,
    unit: goodsData.unit,
    code: goodsData.code,
    remark: goodsData.remark,
    totalStock: goodsData.totalStock,
    threshold: goodsData.threshold,
    // 如果是辅助属性，传递属性信息
    attrId: row.hasOwnProperty('pid') ? row.attrId : undefined,
    attrName: row.hasOwnProperty('pid') ? row.attrName : undefined
  }
  stockDetailDialog.visible = true
}

// 处理弹窗关闭
const handleDetailDialogClose = () => {
  stockDetailDialog.visible = false
  stockDetailDialog.goodsData = null
}

// 处理详情弹窗的导出事件
const handleDetailExport = (exportData: any) => {
  console.log('处理详情导出:', exportData)
  ElMessage({
    type: 'info',
    message: '正在导出库存详情数据...'
  })
}

// 获取库存数据
const fetchInventoryData = async (): Promise<void> => {
  try {
    const params: InventoryListRequest = {
      pageIndex: tablePageData.pageIndex,
      pageSize: tablePageData.pageSize,
      ...searchFrom.value
    }

    console.log('请求参数:', params)

    // 清理空值参数
    Object.keys(params).forEach(function (key) {
      if (
        params[key as keyof InventoryListRequest] === '' ||
        params[key as keyof InventoryListRequest] === null ||
        params[key as keyof InventoryListRequest] === undefined
      ) {
        delete params[key as keyof InventoryListRequest]
      }
    })

    // 使用新的API调用方式
    await new Promise((resolve, reject) => {
      getInventoryList(
        params,
        (data) => {
          console.log('API响应数据:', data)

          // 处理数据格式
          const processedData = processInventoryData(data.rows || [])

          // 转换数据格式适配 NormalTable
          tablePageData.rows = processedData
          tablePageData.total = data.total || 0

          console.log('更新后的表格数据:', {
            pageIndex: tablePageData.pageIndex,
            pageSize: tablePageData.pageSize,
            total: tablePageData.total,
            rowsCount: tablePageData.rows.length,
            hasAttrs: processedData.some((item) => item.attrs && item.attrs.length > 0)
          })

          // 设置表格列（仓库列）
          extractWarehouseColumns(data.rows || [])

          resolve(data)
        },
        (error) => {
          console.error('API请求失败:', error)
          ElMessage({
            type: 'error',
            message: '获取库存数据失败'
          })
          reject(error)
        }
      )
    })
  } catch (error) {
    console.error('Error fetching inventory data:', error)
    ElMessage({
      type: 'error',
      message: '服务器响应超时!'
    })
  }
}

// 从数据中提取仓库列信息
function extractWarehouseColumns(data: InventoryList[]) {
  const warehouseMap = new Map<string, TableColumn>()

  data.forEach(function (item) {
    // 处理主商品的仓库 - 使用新的 goodsWarehouses 字段
    if (item.goodsWarehouses && Array.isArray(item.goodsWarehouses)) {
      item.goodsWarehouses.forEach(function (wh) {
        if (wh.warehouseId && wh.warehouseName) {
          warehouseMap.set(wh.warehouseId, {
            warehouseId: wh.warehouseId,
            name: wh.warehouseName,
            key: `warehouse_${wh.warehouseId}`
          })
        }
      })
    }

    // 处理辅助属性的仓库（如果有的话）
    if (item.attrs && Array.isArray(item.attrs)) {
      item.attrs.forEach(function (attr) {
        if (attr.warehouses && Array.isArray(attr.warehouses)) {
          attr.warehouses.forEach(function (wh) {
            if (wh.warehouseId && wh.warehouseName) {
              warehouseMap.set(wh.warehouseId, {
                warehouseId: wh.warehouseId,
                name: wh.warehouseName,
                key: `warehouse_${wh.warehouseId}`
              })
            }
          })
        }
      })
    }
  })

  tableColumn.value = Array.from(warehouseMap.values())
}

// 搜索方法
const handleSearch = (resetPage?: number): void => {
  if (resetPage) {
    tablePageData.pageIndex = 1
  }
  fetchInventoryData()
}

// NormalTable 分页变化处理
function handlePageChange(data: PageDTO<any>): void {
  tablePageData.pageIndex = data.pageIndex
  tablePageData.pageSize = data.pageSize
  fetchInventoryData()
}

// NormalTable 操作按钮点击处理
const handleTableOperClick = (index: number, row: any, evtname: string): void => {
  switch (evtname) {
    case 'detail':
      showStockDetail(row)
      break
    default:
      console.warn('未知的操作类型:', evtname)
  }
}

// 导出主表格数据
function handleExport(): void {
  // 构建导出参数（排除分页参数）
  const exportParams: InventoryListRequest = {
    goodsName: searchFrom.value.goodsName,
    goodsNumber: searchFrom.value.goodsNumber,
    goodsSpec: searchFrom.value.goodsSpec,
    goodsCategoryId: searchFrom.value.goodsCategoryId,
    goodsBrand: searchFrom.value.goodsBrand,
    goodsCode: searchFrom.value.goodsCode,
    goodsRemark: searchFrom.value.goodsRemark,
    warehouseId: searchFrom.value.warehouseId,
    stockState: searchFrom.value.stockState
  }

  // 清理空值参数
  Object.keys(exportParams).forEach(function (key) {
    const paramKey = key as keyof InventoryListRequest
    const value = exportParams[paramKey]

    if (Array.isArray(value) && value.length === 0) {
      delete exportParams[paramKey]
    } else if (value === '' || value === null || value === undefined) {
      delete exportParams[paramKey]
    }
  })

  // 处理 warehouseId 参数
  if (exportParams.warehouseId && Array.isArray(exportParams.warehouseId)) {
    exportParams.warehouseId = [...exportParams.warehouseId]
  }

  console.log('导出参数:', exportParams)

  ElMessage({
    type: 'info',
    message: '正在导出数据...'
  })

  // 调用导出API - 使用适配器处理回调
  exportInventoryExcel(
    exportParams,
    // 适配器：将单参数回调转换为多参数回调
    (data: any) => {
      // 这里需要获取到完整的响应对象
      // 如果 handleRequest 不支持传递响应对象，需要修改基础请求方法
      handleExportSuccess(data)
    },
    (error) => {
      console.error('完整错误对象:', error)
      console.error('响应数据:', error.response?.data)
      console.error('状态码:', error.response?.status)
      console.error('响应头:', error.response?.headers)
      ElMessage({
        type: 'error',
        message: error?.message || '导出失败，请稍后重试'
      })
    }
  )
}

// 处理导出成功的函数
function handleExportSuccess(blobData: any) {
  console.log('导出返回的Blob数据:', blobData)

  try {
    // 由于无法从回调获取响应头，使用默认文件名
    const filename = `库存列表_${new Date().toISOString().slice(0, 19).replace(/:/g, '-')}.xlsx`

    // 创建下载链接
    const blob = new Blob([blobData], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = filename
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)

    ElMessage({
      type: 'success',
      message: '导出成功'
    })
  } catch (error) {
    console.error('文件下载错误:', error)
    ElMessage({
      type: 'error',
      message: '文件下载失败'
    })
  }
}

// 刷新
const handleReload = (): void => {
  fetchInventoryData()
  ElMessage({
    type: 'success',
    message: '页面刷新成功!'
  })
}

// 生命周期
onMounted(() => {
  fetchInventoryData()
})
</script>

<style scoped>
.layout {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
