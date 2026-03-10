<!-- src/components/goodSearchConpoent/UserListSample.vue -->
<template>
  <div class="userlist-sample">
    <div class="panel-header">
      <h3>演示6：用户列表搜索组件（UserList.vue - 带分页功能）</h3>
    </div>
    <div class="panel-content">
      <div class="userlist-demo">
        <UserList
          ref="userListRef"
          v-model:pagination="userListPagination"
          @search="handleUserListSearch"
        />
        <div class="userlist-actions">
          <el-button @click="handleResetUserListForm"> 重置表单 </el-button>
        </div>
      </div>

      <!-- 分页控件 -->
      <div class="pagination-demo">
        <el-divider content-position="left">分页控件演示</el-divider>
        <div class="pagination-controls">
          <el-pagination
            v-model:current-page="userListPagination.pageIndex"
            v-model:page-size="userListPagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="userListTotal"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleUserListSizeChange"
            @current-change="handleUserListCurrentChange"
          />
          <div class="pagination-info">
            <el-tag>当前页码: {{ userListPagination.pageIndex }}</el-tag>
            <el-tag>每页条数: {{ userListPagination.pageSize }}</el-tag>
            <el-tag>总条数: {{ userListTotal }}</el-tag>
          </div>
        </div>
      </div>

      <el-divider />
      <div class="result-box">
        <strong>用户列表搜索结果：</strong>
        <pre>{{ JSON.stringify(userListSearchData, null, 2) }}</pre>
      </div>
      <div class="userlist-info">
        <el-alert
          title="用户列表组件说明"
          type="info"
          :closable="false"
          description="此组件专门用于用户列表信息的搜索，包含用户名称、用户账号、手机号码和备注信息等字段，支持分页参数自动传递。"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import UserList from '@/components/goodSearchConpoent/UserList.vue'
import { ElMessage } from 'element-plus'

// 响应式数据
const userListRef = ref()
const userListPagination = ref({
  pageIndex: 1,
  pageSize: 20
})
const userListTotal = ref(150) // 模拟总数据量
const userListSearchData = ref({})

// 处理用户列表搜索
function handleUserListSearch(data: any) {
  ElMessage.success(`用户列表搜索 - 执行搜索 (第${data.pageIndex}页, ${data.pageSize}条/页)`)
  console.log('用户列表搜索数据：', data)
  userListSearchData.value = data

  // 更新总数据量（模拟）
  userListTotal.value = Math.max(userListTotal.value, data.pageIndex * data.pageSize)
}

// 重置用户列表表单
function handleResetUserListForm() {
  if (userListRef.value) {
    userListRef.value.resetSearch()
    userListSearchData.value = {}
    // 重置分页参数
    userListPagination.value = {
      pageIndex: 1,
      pageSize: 20
    }
    ElMessage.success('用户列表表单已重置')
  } else {
    ElMessage.warning('用户列表组件未加载')
  }
}

// 处理用户列表每页条数变化
function handleUserListSizeChange(newSize: number) {
  console.log('每页条数变化:', newSize)
  userListPagination.value.pageSize = newSize
  userListPagination.value.pageIndex = 1 // 重置到第一页
  userListRef.value.triggerSearch()
}

// 处理用户列表页码变化
function handleUserListCurrentChange(newPage: number) {
  console.log('页码变化:', newPage)
  userListPagination.value.pageIndex = newPage
  userListRef.value.triggerSearch()
}
</script>

<style scoped>
.userlist-sample {
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

.userlist-demo {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.userlist-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 140px;
}

.userlist-info {
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
  .userlist-demo {
    flex-direction: column;
  }

  .userlist-actions {
    flex-direction: row;
    min-width: auto;
    margin-top: 16px;
  }
}
</style>
