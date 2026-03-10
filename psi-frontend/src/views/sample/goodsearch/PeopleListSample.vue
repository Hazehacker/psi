<!--
 * @Description: 人员搜索组件演示
-->
<template>
  <div class="peoplelist-sample">
    <div class="panel-header">
      <h3>演示：人员搜索组件 (PeopleList)</h3>
      <p class="desc">包含人员名称、编号、性别、联系电话、联系地址、身份证号、备注等搜索条件</p>
    </div>
    <div class="panel-content">
      <div class="peoplelist-demo">
        <div style="flex: 1">
          <PeopleList 
            ref="peopleListRef" 
            :pagination="pagination" 
            @search="handleSearch" 
          />
        </div>
        <div class="peoplelist-actions">
          <el-button type="primary" @click="triggerSearch">触发搜索</el-button>
          <el-button type="warning" @click="resetSearch">重置搜索</el-button>
          <el-button type="info" @click="getFormData">获取数据</el-button>
        </div>
      </div>

      <el-divider />

      <div class="peoplelist-info">
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
              :total="150"
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
            <el-table-column prop="name" label="人员名称" width="120" />
            <el-table-column prop="number" label="人员编号" width="130" />
            <el-table-column prop="gender" label="性别" width="80" />
            <el-table-column prop="tel" label="联系电话" width="140" />
            <el-table-column prop="address" label="联系地址" width="220" show-overflow-tooltip />
            <el-table-column prop="idCard" label="身份证号" width="170" />
            <el-table-column prop="remark" label="备注" show-overflow-tooltip />
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { PeopleList } from '@/components/goodSearchConpoent'
import { ElMessage } from 'element-plus'

const peopleListRef = ref()
const pagination = reactive({
  pageIndex: 1,
  pageSize: 20
})

const searchParams = ref<any>({})
const tableData = ref([
  {
    name: '张三',
    number: 'PEO0001',
    gender: '男',
    tel: '13800138001',
    address: '广东省深圳市南山区科技园1号',
    idCard: '440300199001011234',
    remark: '技术部门负责人'
  },
  {
    name: '李四',
    number: 'PEO0002',
    gender: '女',
    tel: '13800138002',
    address: '广东省深圳市福田区华强北路2号',
    idCard: '440300199102025678',
    remark: '销售经理'
  },
  {
    name: '王五',
    number: 'PEO0003',
    gender: '男',
    tel: '13800138003',
    address: '广东省广州市天河区珠江新城3号',
    idCard: '440300198903039012',
    remark: ''
  },
  {
    name: '赵六',
    number: 'PEO0004',
    gender: '女',
    tel: '13800138004',
    address: '广东省深圳市龙岗区布吉街道4号',
    idCard: '440300199204043456',
    remark: '人事专员'
  },
  {
    name: '钱七',
    number: 'PEO0005',
    gender: '男',
    tel: '13800138005',
    address: '广东省深圳市宝安区西乡街道5号',
    idCard: '440300198805057890',
    remark: '财务总监'
  }
])

/**
 * 处理搜索
 */
function handleSearch(params: any) {
  searchParams.value = params
  ElMessage.success('人员搜索触发')
  console.log('人员搜索参数：', params)
}

/**
 * 手动触发搜索
 */
function triggerSearch() {
  if (peopleListRef.value?.triggerSearch) {
    peopleListRef.value.triggerSearch()
    ElMessage.info('手动触发搜索')
  }
}

/**
 * 重置搜索
 */
function resetSearch() {
  if (peopleListRef.value?.resetSearch) {
    peopleListRef.value.resetSearch()
    searchParams.value = {}
    ElMessage.success('搜索已重置')
  }
}

/**
 * 获取表单数据
 */
function getFormData() {
  if (peopleListRef.value?.getFormData) {
    const formData = peopleListRef.value.getFormData()
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
  if (peopleListRef.value?.triggerSearch) {
    peopleListRef.value.triggerSearch()
  }
}
</script>

<style scoped>
.peoplelist-sample {
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

.peoplelist-demo {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.peoplelist-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 140px;
}

.peoplelist-info {
  margin-top: 16px;
}

.peoplelist-info pre {
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
  .peoplelist-demo {
    flex-direction: column;
  }

  .peoplelist-actions {
    flex-direction: row;
    min-width: auto;
    margin-top: 16px;
  }
}
</style>

