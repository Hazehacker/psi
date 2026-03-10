<!-- src/components/goodSearchConpoent/SupplierSample.vue -->
<template>
  <div class="supplier-sample">
    <div class="panel-header">
      <h3>演示5：供应商搜索组件（Supplier.vue - 带分页功能）</h3>
    </div>
    <div class="panel-content">
      <div class="supplier-demo">
        <Supplier
          ref="supplierRef"
          v-model:pagination="supplierPagination"
          @search="handleSupplierSearch"
        />
        <div class="supplier-actions">
          <el-button @click="handleResetSupplierForm"> 重置表单 </el-button>
        </div>
      </div>

      <!-- 分页控件 -->
      <div class="pagination-demo">
        <el-divider content-position="left">分页控件演示</el-divider>
        <div class="pagination-controls">
          <el-pagination
            v-model:current-page="supplierPagination.pageIndex"
            v-model:page-size="supplierPagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="supplierTotal"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSupplierSizeChange"
            @current-change="handleSupplierCurrentChange"
          />
          <div class="pagination-info">
            <el-tag>当前页码: {{ supplierPagination.pageIndex }}</el-tag>
            <el-tag>每页条数: {{ supplierPagination.pageSize }}</el-tag>
            <el-tag>总条数: {{ supplierTotal }}</el-tag>
          </div>
        </div>
      </div>

      <el-divider />
      <div class="result-box">
        <strong>供应商搜索结果：</strong>
        <pre>{{ JSON.stringify(supplierSearchData, null, 2) }}</pre>
      </div>
      <div class="supplier-info">
        <el-alert
          title="供应商组件说明"
          type="info"
          :closable="false"
          description="此组件专门用于供应商信息的搜索，包含供应商名称、编号、类别、联系人员、联系电话、所属用户和备注信息等字段，支持分页参数自动传递。"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Supplier from '@/components/goodSearchConpoent/Supplier.vue'
import { ElMessage } from 'element-plus'

// 响应式数据
const supplierRef = ref()
const supplierPagination = ref({
  pageIndex: 1,
  pageSize: 20
})
const supplierTotal = ref(200) // 模拟总数据量
const supplierSearchData = ref({})

// 处理供应商搜索
function handleSupplierSearch(data: any) {
  ElMessage.success(`供应商搜索 - 执行搜索 (第${data.pageIndex}页, ${data.pageSize}条/页)`)
  console.log('供应商搜索数据：', data)
  supplierSearchData.value = data

  // 更新总数据量（模拟）
  supplierTotal.value = Math.max(supplierTotal.value, data.pageIndex * data.pageSize)
}

// 重置供应商表单
function handleResetSupplierForm() {
  if (supplierRef.value) {
    supplierRef.value.resetSearch()
    supplierSearchData.value = {}
    // 重置分页参数
    supplierPagination.value = {
      pageIndex: 1,
      pageSize: 20
    }
    ElMessage.success('供应商表单已重置')
  } else {
    ElMessage.warning('供应商组件未加载')
  }
}

// 处理供应商每页条数变化
function handleSupplierSizeChange(newSize: number) {
  console.log('每页条数变化:', newSize)
  supplierPagination.value.pageSize = newSize
  supplierPagination.value.pageIndex = 1 // 重置到第一页
  supplierRef.value.triggerSearch()
}

// 处理供应商页码变化
function handleSupplierCurrentChange(newPage: number) {
  console.log('页码变化:', newPage)
  supplierPagination.value.pageIndex = newPage
  supplierRef.value.triggerSearch()
}
</script>

<style scoped>
.supplier-sample {
  height: 100%;
}

.panel-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e4e7ed;
}

.panel-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.panel-content {
  height: calc(100% - 60px);
  overflow-y: auto;
}

.result-box {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  margin-top: 10px;
}

.result-box pre {
  margin: 10px 0 0 0;
  font-family: 'Courier New', Courier, monospace;
  font-size: 13px;
  color: #606266;
}

.supplier-demo {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.supplier-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 140px;
}

.supplier-info {
  margin-top: 16px;
}

.pagination-demo {
  margin-top: 40px;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .supplier-demo {
    flex-direction: column;
  }

  .supplier-actions {
    flex-direction: row;
    min-width: auto;
    margin-top: 16px;
  }
}
</style>
