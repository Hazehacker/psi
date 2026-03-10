<template>
  <div class="supplier-document-page">
    <div class="page-header">
      <h1>供应商单据展示</h1>
      <el-button type="primary" @click="() => fetchData()">刷新数据</el-button>
    </div>

    <div style="background-color: #fff; padding: 10px 5px">
      <NormalTable
        ref="normalTableRef"
        :tabdata="tableData"
        :tabdatacolumns="tableColumns"
        @page-change="handlePageChange"
      >
        <!-- 可以通过插槽自定义单元格内容 -->
        <template #customercell="{ column, row }">
          <template v-if="column.prop === 'documentNo'">
            <el-tag type="primary">{{ row.documentNo }}</el-tag>
          </template>
          <template v-else>
            {{ row[column.prop] }}
          </template>
        </template>
      </NormalTable>
    </div>
  </div>

  <div class="customer-record-section">
    <h1>客户交易记录（展示树形结构）</h1>
    <tree-table-example />
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import NormalTable from '@/components/normaltable/NormalTable.vue'
import TreeTableExample from './TreeTableExample.vue'
import { createPageDTO, type MyTableColumn, type PageDTO } from '@/components/normaltable/type'

// 定义供应商单据数据类型
interface SupplierDocument {
  id: number
  belongOrg: string
  supplier: string
  documentTime: string
  documentNo: string
}

// 表格数据
const tableData = ref<PageDTO<SupplierDocument>>(
  createPageDTO<SupplierDocument>({
    pageIndex: 1,
    pageSize: 20,
    total: 0,
    rows: []
  })
)

// 表格列配置
const tableColumns = ref<MyTableColumn[]>([
  { prop: 'belongOrg', label: '所属组织', 'min-width': 120, align: 'center' },
  { prop: 'supplier', label: '供应商', 'min-width': 120, align: 'center' },
  { prop: 'documentTime', label: '单据时间', 'min-width': 120, align: 'center' },
  { prop: 'documentNo', label: '单据编号', 'min-width': 180, align: 'center' }
])

// 模拟获取供应商单据数据
const getSupplierDocuments = async () => {
  return new Promise((resolve) => {
    setTimeout(() => {
      const mockData = {
        content: Array.from({ length: 5 }, (_, index) => ({
          id: index + 1,
          belongOrg: '默认组织',
          supplier: `供应商${index + 1}`,
          documentTime: '2024-10-24',
          documentNo: `CGDD2410241254${index.toString().padStart(3, '0')}`
        })),
        totalElements: 5
      }

      resolve({
        data: mockData
      })
    }, 500)
  })
}

// 分页变化处理
const handlePageChange = async (pageInfo: PageDTO<SupplierDocument>) => {
  console.log('分页变化:', pageInfo)
  await fetchData(pageInfo)
}

// 获取数据
const fetchData = async (pageInfo: Partial<PageDTO<SupplierDocument>> = {}) => {
  try {
    const result = await getSupplierDocuments() as { data: { content: SupplierDocument[]; totalElements: number } }
    const data = result.data

    // 更新表格数据
    tableData.value = createPageDTO({
      pageIndex: pageInfo.pageIndex || tableData.value.pageIndex,
      pageSize: pageInfo.pageSize || tableData.value.pageSize,
      total: data.totalElements,
      rows: data.content
    })
  } catch (error) {
    console.error('获取数据失败:', error)
  }
}

const normalTableRef = ref()

// 组件挂载时加载数据
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.supplier-document-page {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  flex-shrink: 0;
}
.page-header h1 {
  margin: 0;
  color: #303133;
}
.customer-record-section {
  margin-top: 30px;
  padding: 20px;
}
.customer-record-section h1 {
  color: #303133;
  margin-bottom: 16px;
}
</style>
