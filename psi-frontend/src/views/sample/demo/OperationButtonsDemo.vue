<template>
  <div class="operation-buttons-demo">
    <div class="demo-header">
      <h1>操作按钮组件演示</h1>
      <p>这是一个演示页面，展示操作按钮组件的四个主要功能：新增、批量操作、导出、刷新</p>
    </div>

    <!-- 操作按钮组件 -->
    <div class="demo-section">
      <h2>操作按钮组件</h2>
      <OperationButtons
        :show-add="true"
        :show-batch="true"
        :show-refresh="true"
        :export-data="tableData"
        :export-file-name="'用户数据导出'"
        :table-columns="tableColumns"
        :template-file-name="'用户数据导入模板'"
        @add="handleAdd"
        @batch="handleBatch"
        @refresh="handleRefresh"
        @export="handleExport"
      />
    </div>

    <!-- 快捷配置演示 -->
    <div class="demo-section">
      <h2>快捷配置演示</h2>
      <div class="config-demo">
        <h3>全部按钮 (all)</h3>
        <div class="flex gap-2 justify-end">
          <el-button type="primary" @click="handleAdd">新增</el-button>
          <el-button type="warning" @click="handleBatch">批量</el-button>
          <el-button type="info" @click="handleRefresh">刷新</el-button>
          <el-button type="success" @click="handleExport">导出</el-button>
        </div>
      </div>

      <div class="config-demo">
        <h3>基础按钮 (basic)</h3>
        <div class="flex gap-2 justify-end">
          <el-button type="primary" @click="handleAdd">新增</el-button>
          <el-button type="info" @click="handleRefresh">刷新</el-button>
        </div>
      </div>

      <div class="config-demo">
        <h3>数据处理按钮 (data)</h3>
        <div class="flex gap-2 justify-end">
          <el-button type="warning" @click="handleBatch">批量</el-button>
          <el-button type="success" @click="handleExport">导出</el-button>
        </div>
      </div>

      <div class="config-demo">
        <h3>自定义配置 (custom)</h3>
        <div class="flex gap-2 justify-end">
          <el-button type="primary" @click="handleAdd">新增</el-button>
          <el-button type="success" @click="handleExport">导出</el-button>
        </div>
      </div>
    </div>

    <!-- 动态控制演示 -->
    <div class="demo-section">
      <h2>动态控制演示</h2>
      <div class="control-panel">
        <el-checkbox v-model="showAdd">显示新增按钮</el-checkbox>
        <el-checkbox v-model="showBatch">显示批量操作按钮</el-checkbox>
        <el-checkbox v-model="showExport">显示导出按钮</el-checkbox>
        <el-checkbox v-model="showRefresh">显示刷新按钮</el-checkbox>
      </div>
      <div class="flex gap-2 justify-end">
        <el-button v-if="showAdd" type="primary" @click="handleAdd">新增</el-button>
        <el-button v-if="showBatch" type="warning" @click="handleBatch">批量</el-button>
        <el-button v-if="showRefresh" type="info" @click="handleRefresh">刷新</el-button>
        <el-button v-if="showExport" type="success" @click="handleExport">导出</el-button>
      </div>
    </div>

    <!-- 模拟数据表格 -->
    <div class="demo-section">
      <h2>模拟数据表格</h2>
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="department" label="部门" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
              {{ row.status === 'active' ? '激活' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
      </el-table>
    </div>

    <!-- 操作日志 -->
    <div class="demo-section">
      <h2>操作日志</h2>
      <div class="log-container">
        <div v-for="(log, index) in operationLogs" :key="index" class="log-item">
          <span class="log-time">{{ log.time }}</span>
          <span class="log-action">{{ log.action }}</span>
          <span class="log-details">{{ log.details }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import OperationButtons from '@/components/operationbuttons/OperationButtons.vue'

// 表格列配置
const tableColumns = [
  { prop: 'id', label: 'ID', width: 80 },
  { prop: 'name', label: '姓名', width: 120 },
  { prop: 'email', label: '邮箱', width: 200 },
  { prop: 'department', label: '部门', width: 120 },
  { prop: 'status', label: '状态', width: 100 },
  { prop: 'createTime', label: '创建时间', width: 180 }
]

// 模拟表格数据
const tableData = ref([
  {
    id: 1,
    name: '张三',
    email: 'zhangsan@example.com',
    department: '技术部',
    status: 'active',
    createTime: '2024-01-15 10:30:00'
  },
  {
    id: 2,
    name: '李四',
    email: 'lisi@example.com',
    department: '产品部',
    status: 'inactive',
    createTime: '2024-01-16 14:20:00'
  },
  {
    id: 3,
    name: '王五',
    email: 'wangwu@example.com',
    department: '设计部',
    status: 'active',
    createTime: '2024-01-17 09:15:00'
  }
])

// 导出数据
const exportData = ref([
  { id: 1, name: '张三', email: 'zhangsan@example.com', department: '技术部', status: '激活', createTime: '2024-01-15 10:30:00' },
  { id: 2, name: '李四', email: 'lisi@example.com', department: '产品部', status: '禁用', createTime: '2024-01-16 14:20:00' },
  { id: 3, name: '王五', email: 'wangwu@example.com', department: '设计部', status: '激活', createTime: '2024-01-17 09:15:00' }
])

// 动态控制状态
const showAdd = ref(true)
const showBatch = ref(true)
const showExport = ref(true)
const showRefresh = ref(true)

// 操作日志
const operationLogs = ref([
  {
    time: '2024-01-20 10:00:00',
    action: '页面加载',
    details: '操作按钮演示页面初始化完成'
  }
])

// 添加日志
const addLog = (action: string, details: string) => {
  operationLogs.value.unshift({
    time: new Date().toLocaleString(),
    action,
    details
  })
}

// 事件处理函数
const handleAdd = () => {
  ElMessage.success('新增按钮被点击')
  addLog('新增操作', '用户点击了新增按钮')
  
  // 模拟添加新数据
  const newId = Math.max(...tableData.value.map(item => item.id)) + 1
  const newItem = {
    id: newId,
    name: `新用户${newId}`,
    email: `user${newId}@example.com`,
    department: '新部门',
    status: 'active',
    createTime: new Date().toLocaleString()
  }
  tableData.value.push(newItem)
  exportData.value.push({
    ...newItem,
    status: '激活'
  })
}

const handleBatch = () => {
  ElMessage.info('批量操作按钮被点击')
  addLog('批量操作', '用户点击了批量操作按钮，可以进行批量删除、批量修改等操作')
}

const handleExport = () => {
  ElMessage.success('导出按钮被点击，数据已导出')
  addLog('导出操作', `成功导出 ${exportData.value.length} 条数据`)
}

const handleRefresh = () => {
  ElMessage.info('刷新按钮被点击')
  addLog('刷新操作', '用户点击了刷新按钮，数据已刷新')
  
  // 模拟数据刷新
  tableData.value.forEach(item => {
    item.createTime = new Date().toLocaleString()
  })
}
</script>

<style scoped>
.operation-buttons-demo {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.demo-header {
  text-align: center;
  margin-bottom: 30px;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 10px;
}

.demo-header h1 {
  margin: 0 0 10px 0;
  font-size: 28px;
}

.demo-header p {
  margin: 0;
  font-size: 16px;
  opacity: 0.9;
}

.demo-section {
  margin-bottom: 40px;
  padding: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background: #fafafa;
}

.demo-section h2 {
  margin: 0 0 20px 0;
  color: #303133;
  font-size: 20px;
  border-bottom: 2px solid #409eff;
  padding-bottom: 10px;
}

.config-demo {
  margin-bottom: 20px;
  padding: 15px;
  background: white;
  border-radius: 6px;
  border: 1px solid #dcdfe6;
}

.config-demo h3 {
  margin: 0 0 15px 0;
  color: #606266;
  font-size: 16px;
}

.control-panel {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  padding: 15px;
  background: white;
  border-radius: 6px;
  border: 1px solid #dcdfe6;
}

.log-container {
  max-height: 300px;
  overflow-y: auto;
  background: white;
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  padding: 10px;
}

.log-item {
  display: flex;
  gap: 15px;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.log-item:last-child {
  border-bottom: none;
}

.log-time {
  color: #909399;
  font-size: 12px;
  min-width: 150px;
}

.log-action {
  color: #409eff;
  font-weight: bold;
  min-width: 100px;
}

.log-details {
  color: #606266;
  flex: 1;
}

/* 按钮样式 */
.flex {
  display: flex;
}

.gap-2 {
  gap: 8px;
}

.justify-end {
  justify-content: flex-end;
}

.config-demo {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: #fafafa;
}

.config-demo h3 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .operation-buttons-demo {
    padding: 10px;
  }
  
  .control-panel {
    flex-direction: column;
    gap: 10px;
  }
  
  .log-item {
    flex-direction: column;
    gap: 5px;
  }
  
  .log-time,
  .log-action {
    min-width: auto;
  }
}
</style>
