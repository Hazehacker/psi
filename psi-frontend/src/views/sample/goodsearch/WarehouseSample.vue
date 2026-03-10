<!--
 * @Description: 仓库搜索组件演示
-->
<template>
  <div class="warehouse-sample">
    <div class="panel-header">
      <h3>演示：仓库搜索组件 (Warehouse)</h3>
      <p class="desc">包含仓库唯一标识、仓库名称、仓库编号等搜索条件</p>
    </div>
    <div class="panel-content">
      <div class="warehouse-demo">
        <div style="flex: 1">
          <Warehouse 
            ref="warehouseRef" 
            :pagination="pagination" 
            @search="handleSearch" 
          />
        </div>
        <div class="warehouse-actions">
          <el-button type="primary" @click="triggerSearch">触发搜索</el-button>
          <el-button type="warning" @click="resetSearch">重置搜索</el-button>
          <el-button type="info" @click="getFormData">获取数据</el-button>
        </div>
      </div>

      <el-divider />

      <div class="warehouse-info">
        <el-alert
          title="搜索参数"
          type="info"
          :closable="false"
          style="margin-bottom: 16px"
        >
          <pre>{{ JSON.stringify(searchParams, null, 2) }}</pre>
        </el-alert>

        <div class="pagination-demo">
          <h4>分页演示</h4>
          <div class="pagination-controls">
            <div class="pagination-info">
              <el-tag>当前页: {{ pagination.pageIndex }}</el-tag>
              <el-tag type="success">每页: {{ pagination.pageSize }}</el-tag>
            </div>
            <el-pagination
              v-model:current-page="pagination.pageIndex"
              v-model:page-size="pagination.pageSize"
              :page-sizes="[10, 20, 50, 100]"
              :total="100"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handlePaginationChange"
              @current-change="handlePaginationChange"
            />
          </div>
        </div>

        <el-divider />

        <div class="result-table">
          <h4>搜索结果表格</h4>
          <el-table :data="tableData" border style="width: 100%; margin-top: 10px">
            <el-table-column prop="id" label="唯一标识" width="120" />
            <el-table-column prop="name" label="仓库名称" width="180" />
            <el-table-column prop="number" label="仓库编号" width="130" />
            <el-table-column prop="manager" label="负责人" width="120" />
            <el-table-column prop="location" label="仓库位置" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === '启用' ? 'success' : 'info'">
                  {{ row.status }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { Warehouse } from '@/components/goodSearchConpoent'
import { ElMessage } from 'element-plus'

const warehouseRef = ref()
const pagination = reactive({
  pageIndex: 1,
  pageSize: 20
})

const searchParams = ref<any>({})
const tableData = ref([
  {
    id: 'WH-UUID-001',
    name: '主仓库A',
    number: 'WH0001',
    manager: '张三',
    location: '广东省深圳市南山区科技园A栋',
    status: '启用'
  },
  {
    id: 'WH-UUID-002',
    name: '副仓库B',
    number: 'WH0002',
    manager: '李四',
    location: '广东省深圳市福田区华强北B栋',
    status: '启用'
  },
  {
    id: 'WH-UUID-003',
    name: '分仓库C',
    number: 'WH0003',
    manager: '王五',
    location: '广东省广州市天河区珠江新城C栋',
    status: '停用'
  }
])

/**
 * 处理搜索
 */
function handleSearch(params: any) {
  searchParams.value = params
  ElMessage.success('仓库搜索触发')
  console.log('仓库搜索参数：', params)
}

/**
 * 手动触发搜索
 */
function triggerSearch() {
  if (warehouseRef.value?.triggerSearch) {
    warehouseRef.value.triggerSearch()
    ElMessage.info('手动触发搜索')
  }
}

/**
 * 重置搜索
 */
function resetSearch() {
  if (warehouseRef.value?.resetSearch) {
    warehouseRef.value.resetSearch()
    searchParams.value = {}
    ElMessage.success('搜索已重置')
  }
}

/**
 * 获取表单数据
 */
function getFormData() {
  if (warehouseRef.value?.getFormData) {
    const formData = warehouseRef.value.getFormData()
    console.log('当前表单数据：', formData)
    ElMessage({
      message: '表单数据已输出到控制台',
      type: 'info'
    })
  }
}

/**
 * 处理分页变化
 */
function handlePaginationChange() {
  ElMessage.info(`分页变化: 第${pagination.pageIndex}页，每页${pagination.pageSize}条`)
  // 触发搜索
  if (warehouseRef.value?.triggerSearch) {
    warehouseRef.value.triggerSearch()
  }
}
</script>

<style scoped>
.warehouse-sample {
  height: 100%;
}

.panel-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e4e7ed;
}

.panel-header h3 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.panel-header .desc {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.panel-content {
  height: calc(100% - 60px);
  overflow-y: auto;
}

.warehouse-demo {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.warehouse-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 140px;
}

.warehouse-info {
  margin-top: 16px;
}

.warehouse-info pre {
  margin: 0;
  font-family: 'Courier New', Courier, monospace;
  font-size: 13px;
  color: #606266;
}

.pagination-demo {
  margin-top: 20px;
}

.pagination-demo h4 {
  margin: 0 0 12px 0;
  color: #606266;
  font-size: 15px;
}

.pagination-controls {
  display: flex;
  flex-direction: column;
  gap: 16px;
  align-items: center;
}

.pagination-info {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.result-table h4 {
  margin: 16px 0 0 0;
  color: #606266;
  font-size: 15px;
}

@media (max-width: 768px) {
  .warehouse-demo {
    flex-direction: column;
  }

  .warehouse-actions {
    flex-direction: row;
    min-width: auto;
    margin-top: 16px;
  }
}
</style>

