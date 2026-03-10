<!--
 * @Author: f1-duanhong 1271736670@qq.com
 * @Date: 2025-10-26 01:49:50
 * @LastEditors: f1-duanhong 1271736670@qq.com
 * @LastEditTime: 2025-11-12 09:26:25
 * @FilePath: \psi-frontend\src\views\warehouse\batch\BatchQuery.vue
 * @Description: 仓库管理-批次查询主页面
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
      @update:tabdata="handleTableDataUpdate"
      @taboper-click="handleTableOperClick"
    >
      <!-- 多级表头定义 - 根据批次查询需求调整 -->
      <template #columns>
        <el-table-column prop="name" label="商品名称" width="220px" fixed="left" />
        <el-table-column prop="totalStock" label="库存数量" width="120px" />
        <el-table-column label="仓库">
          <template v-for="column in tableColumn" :key="column.key">
            <el-table-column :prop="column.key" :label="column.name" width="90px" />
          </template>
        </el-table-column>
        <el-table-column prop="protect" label="保质期(天)" width="120px" />
        <el-table-column prop="startTime" label="生产日期" width="160px" />
        <el-table-column prop="endTime" label="过期日期" width="160px" />
        <el-table-column prop="number" label="商品编号" width="160px" />
        <el-table-column prop="spec" label="规格型号" width="160px" />
        <el-table-column prop="categoryData.name" label="商品分类" width="120px" />
        <el-table-column prop="brand" label="商品品牌" width="120px" />
        <el-table-column prop="unit" label="商品单位" width="120px" />
        <el-table-column prop="code" label="商品条码" width="160px" />
        <el-table-column prop="remark" label="商品备注" width="160px" />

        <!-- 操作列 - 使用自定义模板 -->
        <el-table-column label="相关操作" min-width="120px" fixed="right">
          <template #default="scope">
            <el-button @click="showBatchDetail(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </template>

      <!-- 自定义单元格内容 -->
      <template #customercell="{ column, prop, row }">
        <!-- 仓库库存显示 -->
        <template v-if="tableColumn.some((col) => col.key === prop)">
          <template v-if="row.batches && row.batches.length > 0">
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

        <!-- 生产日期显示 -->
        <template v-else-if="prop === 'startTime'">
          {{ getBatchProduceDate(row) }}
        </template>

        <!-- 过期日期显示 -->
        <template v-else-if="prop === 'endTime'">
          {{ getBatchExpireDate(row) }}
        </template>

        <!-- 默认显示 -->
        <template v-else>
          {{ row[prop] || '-' }}
        </template>
      </template>
    </NormalTable>

    <!-- 批次详情弹窗 -->
    <DetailDialog
      v-model:visible="batchDetailDialog.visible"
      :batch-data="batchDetailDialog.batchData"
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
import { getBatchList } from '@/apis/warehouse/batch/index'
import type { BatchDTO, BatchNumberDTO } from '@/apis/warehouse/batch/type'

// 响应式数据
interface TableColumn {
  key: string
  name: string
}
const tableColumn = ref<TableColumn[]>([])

// 批次详情弹窗
const batchDetailDialog = reactive({
  visible: false,
  batchData: null as any
})

// 分页数据 - 使用 NormalTable 的 PageDTO 格式
const tablePageData = reactive<PageDTO<any>>({
  rows: [],
  total: 0,
  pageIndex: 1,
  pageSize: 30
})

// 搜索表单 - 根据批次查询API调整
const searchFrom = ref({
  goodsName: '', // 商品名称
  goodsNumber: '', // 商品编号
  batchNumber: '', // 批次号码
  productDate: '', // 生产日期
  goodsSpec: '', // 商品型号
  goodsCategoryId: undefined as number | undefined, // 商品类别ID
  goodsBrand: '', // 商品品牌
  goodsCode: '', // 商品条码
  warehouseIds: [] as number[], // 仓库ID（多选）
  batchState: undefined as number | undefined // 批次类型
})

// 表格属性配置 - 启用树形表格
const tableAttr = reactive({
  'row-key': 'key',
  'tree-props': { children: 'batches' }, // 使用batches作为子节点
  'header-cell-style': { textAlign: 'center', backgroundColor: '#f5f7fa' },
  'cell-style': { textAlign: 'center' }
})

// 表格操作按钮配置
const tableOperBtns = ref<MyTableOperationsBtn[]>([
  { text: '详情', evtname: 'detail', attr: { size: 'small', type: 'primary' } }
])

// 获取批次生产日期（从批次单据中获取）
function getBatchProduceDate(row: BatchDTO): string {
  if (!row.batches || row.batches.length === 0) return '-'
  // 取第一个批次的生产日期
  const firstBatch = row.batches[0]
  if (firstBatch.batchDocuments && firstBatch.batchDocuments.length > 0) {
    return firstBatch.batchDocuments[0].produceDate || '-'
  }
  return '-'
}

// 获取批次过期日期（从批次单据中获取）
function getBatchExpireDate(row: BatchDTO): string {
  if (!row.batches || row.batches.length === 0) return '-'
  // 取第一个批次的过期日期
  const firstBatch = row.batches[0]
  if (firstBatch.batchDocuments && firstBatch.batchDocuments.length > 0) {
    return firstBatch.batchDocuments[0].expireDate || '-'
  }
  return '-'
}

// 处理数据，将仓库记录转换为表格列数据
const processBatchData = (data: BatchDTO[]) => {
  return data.map((item) => {
    const processedItem: Record<string, any> = {
      ...item,
      key: `goods_${item.id}`, // 商品级别的key
      // 处理批次数据作为子节点
      batches:
        item.batches?.map((batch: BatchNumberDTO) => ({
          ...batch,
          key: `batch_${batch.id}`, // 批次级别的key
          name: `${item.name} - ${batch.batchNumber}`, // 批次显示名称
          number: item.number,
          spec: item.spec,
          brand: item.brand,
          categoryData: item.categoryData,
          unit: item.unit,
          code: item.code,
          remark: item.remark,
          protect: item.protect,
          // 批次的生产日期和过期日期
          startTime: batch.batchDocuments?.[0]?.produceDate || '-',
          endTime: batch.batchDocuments?.[0]?.expireDate || '-'
        })) || []
    }

    // 处理仓库数据
    if (item.warehouses && Array.isArray(item.warehouses)) {
      item.warehouses.forEach((wh) => {
        // 为每个仓库创建对应的属性
        if (wh.warehouseId) {
          processedItem[wh.warehouseId] = wh.stockNum
        }
      })
    }

    // 处理批次的仓库数据
    if (item.batches) {
      item.batches.forEach((batch: any) => {
        if (batch.warehouses) {
          batch.warehouses.forEach((wh: any) => {
            batch[wh.warehouseId!] = wh.stockNum
          })
        }
      })
    }

    return processedItem
  })
}

// 显示批次详情
function showBatchDetail(row: any) {
  console.log('显示批次详情:', row)

  // 判断是商品级别还是批次级别
  const isBatchLevel = row.key?.startsWith('batch_')

  batchDetailDialog.batchData = {
    id: isBatchLevel ? row.id : undefined,
    batchId: isBatchLevel ? row.id : undefined,
    goodsId: isBatchLevel ? undefined : row.id,
    name: row.name,
    number: row.number,
    batchNumber: isBatchLevel ? row.batchNumber : undefined,
    spec: row.spec,
    categoryName: row.categoryData?.name,
    brand: row.brand,
    unit: row.unit,
    code: row.code,
    remark: row.remark,
    totalStock: row.totalStock,
    protect: row.protect,
    startTime: row.startTime,
    endTime: row.endTime
  }
  batchDetailDialog.visible = true
}

// 处理弹窗关闭
function handleDetailDialogClose() {
  batchDetailDialog.visible = false
  batchDetailDialog.batchData = null
}

// 处理详情弹窗的导出事件
function handleDetailExport(exportData: any) {
  console.log('处理批次详情导出:', exportData)
  ElMessage({
    type: 'info',
    message: '正在导出批次详情数据...'
  })
}

// 获取批次数据
async function fetchBatchData(): Promise<void> {
  try {
    const params: Record<string, any> = {
      pageIndex: tablePageData.pageIndex,
      pageSize: tablePageData.pageSize,
      ...searchFrom.value
    }

    console.log('批次查询请求参数:', params)

    // 清理空值参数
    Object.keys(params).forEach(function (key) {
      if (
        params[key] === '' ||
        params[key] === null ||
        params[key] === undefined ||
        (Array.isArray(params[key]) && params[key].length === 0)
      ) {
        delete params[key]
      }
    })

    console.log('清理后的参数:', params)

    // 调用API接口（使用回调模式）
    await getBatchList(
      params,
      (data) => {
        console.log('批次API响应:', data)
        // 处理数据格式
        const processedData = processBatchData(data.rows || [])

        // 转换数据格式适配 NormalTable
        tablePageData.rows = processedData
        tablePageData.total = data.total || 0

        console.log('更新后的表格数据:', {
          pageIndex: tablePageData.pageIndex,
          pageSize: tablePageData.pageSize,
          total: tablePageData.total,
          rowsCount: tablePageData.rows.length
        })

        // 设置表格列（仓库列）
        extractWarehouseColumns(data.rows || [])
      },
      (error) => {
        console.error('获取批次数据失败:', error)
        ElMessage({
          type: 'warning',
          message: (error as any)?.message || '获取批次数据失败'
        })
      }
    )
  } catch (error) {
    ElMessage({
      type: 'error',
      message: '服务器响应超时!'
    })
    console.error('Error fetching batch data:', error)
  }
}

// 从数据中提取仓库列信息
function extractWarehouseColumns(data: BatchDTO[]) {
  const warehouseMap = new Map()

  data.forEach((item) => {
    if (item.warehouses && Array.isArray(item.warehouses)) {
      item.warehouses.forEach((wh) => {
        if (wh.warehouseId && wh.warehouseName) {
          warehouseMap.set(wh.warehouseId, {
            key: wh.warehouseId, // 直接使用warehouseId作为key
            name: wh.warehouseName
          })
        }
      })
    }
  })

  tableColumn.value = Array.from(warehouseMap.values())
}

// 搜索方法
function handleSearch(resetPage?: number): void {
  if (resetPage) {
    tablePageData.pageIndex = 1
  }
  fetchBatchData()
}

// NormalTable 分页变化处理
function handlePageChange(data: PageDTO<any>): void {
  console.log('分页变化事件:', data)
  Object.assign(tablePageData, data)
  fetchBatchData()
}

// NormalTable 表格数据更新处理
function handleTableDataUpdate(newData: PageDTO<any>): void {
  console.log('表格数据更新事件:', newData)
  Object.assign(tablePageData, newData)
  fetchBatchData()
}

// NormalTable 操作按钮点击处理
function handleTableOperClick(index: number, row: any, evtname: string): void {
  switch (evtname) {
    case 'detail':
      showBatchDetail(row)
      break
    default:
      console.warn('未知的操作类型:', evtname)
  }
}

// 导出主表格数据
function handleExport(): void {
  ElMessage({
    type: 'info',
    message: '导出功能开发中...'
  })
}

// 刷新
function handleReload(): void {
  fetchBatchData()
  ElMessage({
    type: 'success',
    message: '页面刷新成功!'
  })
}

// 生命周期
onMounted(() => {
  fetchBatchData()
})
</script>

<style scoped>
.layout {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
/* 覆盖表格样式，让第一列居左，其他列居中 */
:deep(.el-table .el-table__header-wrapper .el-table__header .el-table__cell:first-child),
:deep(.el-table .el-table__body-wrapper .el-table__body .el-table__cell:first-child) {
  text-align: left !important;
}
</style>
