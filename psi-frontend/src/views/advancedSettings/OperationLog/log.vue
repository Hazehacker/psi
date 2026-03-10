<template>
  <div class="frame area log-manage">
    <div class="layout">
      <!-- 搜索筛选弹窗 -->
      <el-popover 
        ref="searchPopover" 
        popper-class="search-popover" 
        placement="bottom-start" 
        trigger="click" 
        width="520"
        :visible="popoverVisible"
        @show="onPopoverShow"
        @hide="onPopoverHide"
      >
        <div class="search-form-container">
          <!-- 搜索表单-->
          <el-form class="search-form" ref="searchFromRef" :model="searchForm" inline>
            <!-- 第一行：操作内容和操作用户 -->
            <div class="form-row">
              <el-form-item class="form-item">
                <el-input 
                  placeholder="请输入操作内容" 
                  v-model="searchForm.info" 
                  clearable 
                  @keyup.enter="fetchLogs(1)"
                />
              </el-form-item>
              <el-form-item class="form-item">
                <!-- 使用自定义用户选择器，避免事件冒泡问题 -->
                <div class="custom-user-selector">
                  <el-input 
                    v-model="searchForm.user"
                    placeholder="请选择操作用户"
                    readonly
                    suffix-icon="ArrowDown"
                    @click="toggleUserDropdown"
                    ref="userInputRef"
                  />
                  
                  <!-- 用户下拉框 -->
                  <div v-if="userDropdownVisible" class="user-dropdown">
                    <div class="user-search-box">
                      <el-input
                        v-model="userSearchKeyword"
                        placeholder="F1 输入内容"
                        clearable
                        @keyup.enter="searchUsers"
                        @clear="resetUserSearch"
                        size="small"
                        ref="userSearchInputRef"
                      >
                        <template #append>
                          <el-button :icon="Search" @click="searchUsers" size="small" />
                        </template>
                      </el-input>
                    </div>
                    
                    <div class="user-list">
                      <!-- 用户列表加载状态 -->
                      <div v-if="userLoading" class="loading-state">
                        <el-icon class="is-loading"><Loading /></el-icon>
                        <span>加载中...</span>
                      </div>
                      <!-- 用户列表数据 -->
                      <div
                        v-else
                        v-for="user in paginatedUsers"
                        :key="user.id"
                        class="user-item"
                        :class="{ active: searchForm.user === user.name }"
                        @click="selectUser(user)"
                      >
                        {{ user.name }}
                      </div>
                      <!-- 空状态 -->
                      <div v-if="!userLoading && paginatedUsers.length === 0" class="no-data">
                        未查询到数据
                      </div>
                    </div>
                    
                    <div class="user-pagination-container">
                      <el-pagination
                        small
                        :current-page="userPage.current"
                        :total="userPage.total"
                        :page-size="userPage.size"
                        @current-change="onUserPageChange"
                        layout="prev, pager, next"
                        :disabled="userLoading"
                      />
                      <span class="user-count">共 {{ userPage.total }} 条</span>
                    </div>
                  </div>
                </div>
              </el-form-item>
            </div>
            
            <!-- 第二行：开始日期和结束日期 -->
            <div class="form-row">
              <el-form-item class="form-item">
                <el-date-picker 
                  v-model="searchForm.startTime" 
                  placeholder="请输入开始日期" 
                  value-format="YYYY-MM-DD"
                  format="YYYY-MM-DD"
                  type="date" 
                  style="width: 100%"
                  @change="handleStartDateChange"
                />
              </el-form-item>
              <el-form-item class="form-item">
                <el-date-picker 
                  v-model="searchForm.endTime" 
                  placeholder="请输入结束日期" 
                  value-format="YYYY-MM-DD"
                  format="YYYY-MM-DD"
                  type="date" 
                  style="width: 100%"
                  @change="handleEndDateChange"
                />
              </el-form-item>
            </div>
            
            <!-- 第三行：搜索按钮 -->
            <div class="form-row">
              <el-form-item class="form-item">
                <el-button class="searchBtn" type="primary" :icon="Search" @click="fetchLogs(1)">搜索</el-button>
              </el-form-item>
            </div>
          </el-form>
        </div>
        <template #reference>
          <!-- 筛选按钮 -->
          <el-button :icon="MoreFilled" @click="togglePopover" />
        </template>
      </el-popover>

      <!-- 操作按钮组 -->
      <el-button-group>
        <el-button type="danger" @click="handleClear">清空</el-button>
        <el-button @click="handleRefresh">刷新</el-button>
      </el-button-group>
    </div>

    <el-divider />

    <!-- 日志表格 -->
    <el-table :data="tableRows" height="calc(100% - 90px)" border>
      <el-table-column prop="time" label="操作时间" align="center" width="200" />
      <el-table-column prop="userName" label="操作用户" align="center" width="160" />
      <el-table-column prop="info" label="操作内容" align="center" />
    </el-table>

    <!-- 分页组件 -->
    <el-pagination 
      class="tablePagination" 
      :current-page="page.current" 
      :total="page.total" 
      :page-size="page.size" 
      :page-sizes="page.sizes" 
      :pager-count="page.count" 
      @size-change="onPageSizeChange" 
      @current-change="onPageChange" 
      layout="prev,pager,next,jumper,sizes,total" 
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { MoreFilled, Search, Loading } from '@element-plus/icons-vue'
import { queryLog, clearLogs } from '@/apis/advancedSettings/OperationLog'

// 分页参数
const page = reactive({ 
  current: 1,     // 当前页码
  total: 0,       // 总记录数
  size: 30,       // 每页显示数量
  sizes: [30, 60, 90, 150, 300], // 每页显示数量选项
  count: 5        // 页码按钮数量
})

// 表格数据
const tableRows = ref<any[]>([])

// 搜索表单数据
const searchForm = reactive({ 
  info: '',       // 操作内容
  user: '',       // 操作用户
  startTime: '',  // 开始日期
  endTime: ''     // 结束日期
})

// 弹窗可见性控制
const popoverVisible = ref(false)
const searchPopover = ref()

// 用户选择器相关状态
const userDropdownVisible = ref(false)
const userSearchKeyword = ref('')
const userInputRef = ref()
const userSearchInputRef = ref()
const allUsers = ref<any[]>([])
const userPage = reactive({ current: 1, total: 0, size: 6 })
const userLoading = ref(false)

// 处理开始日期变化
const handleStartDateChange = (date: string) => {
  console.log('开始日期选择:', {
    原始值: date,
    显示值: searchForm.startTime
  })
  // 不需要额外处理，v-model 已经绑定了
}

// 处理结束日期变化
const handleEndDateChange = (date: string) => {
  console.log('结束日期选择:', {
    原始值: date,
    显示值: searchForm.endTime
  })
  // 不需要额外处理，v-model 已经绑定了
}

/**
 * 获取用户列表 - 从接口获取用户数据
 * 这里预留了接口调用的位置，实际使用时需要替换为真实的API调用
 */
const fetchUserList = async () => {
  userLoading.value = true
  try {
    // TODO: 替换为实际的用户列表API调用
    // 示例：const response = await getUserList({ keyword: userSearchKeyword.value })
    
    // 模拟API调用延迟
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 如果有搜索关键词，过滤用户列表
    if (userSearchKeyword.value) {
      allUsers.value = allUsers.value.filter(user => 
        user.name.toLowerCase().includes(userSearchKeyword.value.toLowerCase())
      )
    }
    
    // TODO: 实际使用时替换为以下代码
    // allUsers.value = response.data.users || []
    // userPage.total = response.data.total || allUsers.value.length
    
    
    updateUserList()
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败')
    allUsers.value = []
    userPage.total = 0
  } finally {
    userLoading.value = false
  }
}

// 更新用户列表显示
const updateUserList = () => {
  const start = (userPage.current - 1) * userPage.size
  const end = start + userPage.size
  paginatedUsers.value = allUsers.value.slice(start, end)
}

// 分页后的用户列表
const paginatedUsers = ref<any[]>([])

// 切换用户下拉框
const toggleUserDropdown = () => {
  userDropdownVisible.value = !userDropdownVisible.value
  if (userDropdownVisible.value) {
    // 当下拉框打开时，获取用户列表
    fetchUserList()
    nextTick(() => {
      userSearchInputRef.value?.focus()
    })
  }
}

// 搜索用户
const searchUsers = () => {
  userPage.current = 1
  fetchUserList()
}

// 重置用户搜索
const resetUserSearch = () => {
  userSearchKeyword.value = ''
  userPage.current = 1
  fetchUserList()
}

// 选择用户
const selectUser = (user: any) => {
  searchForm.user = user.name
  userDropdownVisible.value = false
}

// 用户分页变化
const onUserPageChange = (page: number) => {
  userPage.current = page
  updateUserList()
}

// 切换筛选弹窗
const togglePopover = () => {
  popoverVisible.value = !popoverVisible.value
}

// 弹窗显示事件
const onPopoverShow = () => {
  popoverVisible.value = true
}

// 弹窗隐藏事件
const onPopoverHide = () => {
  popoverVisible.value = false
  userDropdownVisible.value = false
}

// 点击页面其他区域关闭用户下拉框
const handleClickOutside = (event: Event) => {
  const userSelector = document.querySelector('.custom-user-selector')
  if (userSelector && !userSelector.contains(event.target as Node)) {
    userDropdownVisible.value = false
  }
}

/**
 * 获取日志列表
 * - 与 API 的 pageIndex/pageSize 对齐；支持两种返回结构（分页对象或扁平数组）
 * @param goFirst 是否跳转到第一页
 */
const fetchLogs = (goFirst?: number) => {
if (goFirst === 1) page.current = 1

popoverVisible.value = false

queryLog({ 
  pageIndex: page.current, 
  pageSize: page.size,
  info: searchForm.info, 
  user: searchForm.user, 
  begintime: searchForm.startTime, 
  endtime: searchForm.endTime
})
  .then((res) => {
    // 使用类型断言
    const response = res as any
    
    if (response.code === 10000) {
      const data = response.data || response.info
      if (data) {
        const rows = data.rows || []
        tableRows.value = rows.map((r: any) => ({
          time: r.time,
          userName: r.user,
          info: r.info
        }))
        page.total = data.total || 0
      }
    } else {
      ElMessage.error(response.message || '获取日志失败')
    }
  })
  .catch((error) => {
    console.error('获取日志失败:', error)
    ElMessage.error('获取日志失败')
  })
}
/** 
 * 清空日志 
 * - 显示确认对话框，确认后调用清空API
 */
const handleClear = () => {
  ElMessageBox.confirm('您确定要清空日志吗?', '提示', { type: 'warning' })
    .then(() => clearLogs('confirm'))
    .then((res) => {
      if (res.code === 10000) {
        fetchLogs(1) // 清空后重新加载第一页
        ElMessage.success('清空日志成功!')
      } else {
        ElMessage.error(res.message || '清空失败')
      }
    })
    .catch(() => {
      // 用户取消操作，不做任何处理
    })
}

/** 刷新日志列表 */
const handleRefresh = () => { 
  fetchLogs(); 
  ElMessage.success('刷新成功') 
}

/** 每页显示数量变化 */
const onPageSizeChange = (size: number) => { 
  page.size = size; 
  fetchLogs(1) // 切换每页数量后跳转到第一页
}

/** 页码变化 */
const onPageChange = (p: number) => { 
  page.current = p; 
  fetchLogs() 
}

// 组件挂载后加载数据
onMounted(() => { 
  fetchLogs(1)
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
/* 布局容器：左右分布筛选按钮和操作按钮 */
.layout { 
  display: flex; 
  justify-content: space-between; 
}

/* 自定义用户选择器样式 */
.custom-user-selector {
  position: relative;
  width: 100%;
}

/* 用户下拉框样式 */
.user-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  z-index: 2000;
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-top: 4px;
}

.user-search-box {
  padding: 8px;
  border-bottom: 1px solid #e4e7ed;
}

.user-list {
  max-height: 216px; /* 6个用户项，每个36px */
  overflow-y: auto;
  min-height: 100px;
}

.user-item {
  padding: 8px 12px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
  height: 36px;
  line-height: 20px;
  display: flex;
  align-items: center;
}

.user-item:hover {
  background-color: #f5f7fa;
}

.user-item.active {
  background-color: #ecf5ff;
  color: #409eff;
}

.user-item:last-child {
  border-bottom: none;
}

/* 加载状态样式 */
.loading-state {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  color: #909399;
}

.loading-state .el-icon {
  margin-right: 8px;
}

.no-data {
  padding: 8px 12px;
  text-align: center;
  color: #909399;
  font-size: 14px;
}

.user-pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  border-top: 1px solid #e4e7ed;
}

.user-count {
  font-size: 12px;
  color: #606266;
}

/* 调整分页样式 */
.user-pagination-container .el-pagination {
  padding: 0;
}

.user-pagination-container .el-pagination__total {
  display: none;
}

.user-pagination-container .el-pagination button,
.user-pagination-container .el-pagination li {
  margin: 0;
  min-width: 28px;
  height: 28px;
  line-height: 28px;
}
</style>

<style>
/* 搜索弹窗样式 - 全局样式 */
.search-popover .search-form-container {
  padding: 16px;
}

/* 表单行布局 */
.search-popover .form-row {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  gap: 12px;
}

/* 表单项等宽分布 */
.search-popover .form-item {
  flex: 1;
  margin-right: 0;
  margin-bottom: 0;
}

/* 全宽度表单项 */
.search-popover .full-width {
  flex: 0 0 100%;
}

/* 确保输入框和日期选择器宽度一致 */
.search-popover .el-input,
.search-popover .el-date-picker,
.search-popover .el-select {
  width: 100%;
}
</style>