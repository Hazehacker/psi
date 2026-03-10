<!--
 * @Description: 客户搜索组件演示 (Customer)
-->
<template>
  <div class="customer-sample">
    <div class="panel-header">
      <h3>演示：客户搜索组件 (Customer)</h3>
      <p class="desc">包含客户名称、编号、类别、等级、联系人、联系电话、所属用户、备注等搜索条件，支持分页参数透传</p>
    </div>
    <div class="panel-content">
      <div class="customer-demo">
        <div style="flex: 1">
          <Customer
            ref="customerRef"
            :pagination="pagination"
            @search="handleSearch"
          />
        </div>
        <div class="customer-actions">
          <el-button type="primary" @click="triggerSearch">触发搜索</el-button>
          <el-button type="warning" @click="resetSearch">重置搜索</el-button>
          <el-button type="info" @click="getFormData">获取数据</el-button>
        </div>
      </div>

      <el-divider />

      <div class="customer-info">
        <el-alert title="搜索参数" type="info" :closable="false" style="margin-bottom: 16px">
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
              :total="180"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handlePaginationChange"
              @current-change="handlePaginationChange"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { Customer } from '@/components/goodSearchConpoent'
import { ElMessage } from 'element-plus'

const customerRef = ref()
const pagination = reactive({
  pageIndex: 1,
  pageSize: 20
})

const searchParams = ref<any>({})

function handleSearch(params: any) {
  searchParams.value = params
  ElMessage.success('客户搜索触发')
  console.log('客户搜索参数：', params)
}

function triggerSearch() {
  if (customerRef.value?.triggerSearch) {
    customerRef.value.triggerSearch()
    ElMessage.info('手动触发搜索')
  }
}

function resetSearch() {
  if (customerRef.value?.resetSearch) {
    customerRef.value.resetSearch()
    searchParams.value = {}
    pagination.pageIndex = 1
    pagination.pageSize = 20
    ElMessage.success('搜索已重置')
  }
}

function getFormData() {
  if (customerRef.value?.getFormData) {
    const formData = customerRef.value.getFormData()
    console.log('当前表单数据：', formData)
    ElMessage({ message: '表单数据已输出到控制台', type: 'info' })
  }
}

function handlePaginationChange() {
  ElMessage.info(`分页变化: 第${pagination.pageIndex}页，每页${pagination.pageSize}条`)
  if (customerRef.value?.triggerSearch) {
    customerRef.value.triggerSearch()
  }
}
</script>

<style scoped>
.customer-sample { height: 100%; }
.panel-header { margin-bottom: 20px; padding-bottom: 10px; border-bottom: 1px solid #e4e7ed; }
.panel-header h3 { margin: 0 0 8px 0; color: #303133; font-size: 18px; font-weight: 600; }
.panel-header .desc { margin: 0; color: #909399; font-size: 14px; }
.panel-content { height: calc(100% - 60px); overflow-y: auto; }
.customer-demo { display: flex; justify-content: space-between; align-items: flex-start; gap: 16px; }
.customer-actions { display: flex; flex-direction: column; gap: 8px; min-width: 140px; }
.customer-info { margin-top: 16px; }
.customer-info pre { margin: 0; font-family: 'Courier New', Courier, monospace; font-size: 13px; color: #606266; }
.pagination-demo { margin-top: 20px; }
.pagination-demo h4 { margin: 0 0 12px 0; color: #606266; font-size: 15px; }
.pagination-controls { display: flex; flex-direction: column; gap: 16px; align-items: center; }
.pagination-info { display: flex; gap: 8px; justify-content: center; }
@media (max-width: 768px) {
  .customer-demo { flex-direction: column; }
  .customer-actions { flex-direction: row; min-width: auto; margin-top: 16px; }
}
</style>


