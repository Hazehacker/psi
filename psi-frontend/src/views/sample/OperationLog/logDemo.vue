<template>
    <div class="frame area log-demo">
      <div class="layout">
        <el-popover ref="searchPopover" popper-class="search-popover" placement="bottom-start" trigger="click" width="520">
          <div class="search-form-container">
            <el-form class="search-form" ref="searchFromRef" :model="searchForm" inline>
              <!-- 第一行 -->
              <div class="form-row">
                <el-form-item class="form-item">
                  <el-input 
                    placeholder="请输入操作内容" 
                    v-model="searchForm.info" 
                    clearable 
                    @keyup.enter="applySearch"
                  />
                </el-form-item>
                <el-form-item class="form-item">
                  <!-- 自定义用户选择器 -->
                  <div class="custom-user-select">
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
                          placeholder="F1 输入内容回车搜索"
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
                        <div
                          v-for="user in paginatedUsers"
                          :key="user.id"
                          class="user-item"
                          :class="{ active: searchForm.user === user.name }"
                          @click="selectUser(user)"
                        >
                          {{ user.name }}
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
                        />
                        <span class="user-count">共 {{ userPage.total }} 条</span>
                      </div>
                    </div>
                  </div>
                </el-form-item>
              </div>
              
              <!-- 第二行 -->
              <div class="form-row">
                <el-form-item class="form-item">
                  <el-date-picker 
                    v-model="searchForm.startTime" 
                    placeholder="请输入开始日期" 
                    value-format="yyyy-MM-dd" 
                    type="date" 
                  />
                </el-form-item>
                <el-form-item class="form-item">
                  <el-date-picker 
                    v-model="searchForm.endTime" 
                    placeholder="请输入结束日期" 
                    value-format="yyyy-MM-dd" 
                    type="date" 
                  />
                </el-form-item>
              </div>
              
              <!-- 搜索按钮 -->
              <div class="form-row button-row">
                <el-button class="searchBtn" type="primary" :icon="Search" @click="applySearch">搜索</el-button>
              </div>
            </el-form>
          </div>
          <template #reference>
            <el-button :icon="MoreFilled" />
          </template>
        </el-popover>
  
        <el-button-group>
          <el-button type="danger" @click="handleClear">清空</el-button>
          <el-button @click="handleRefresh">刷新</el-button>
        </el-button-group>
      </div>
  
      <el-divider />
  
      <el-table :data="filteredRows" height="calc(100% - 90px)" border>
        <el-table-column prop="time" label="操作时间" align="center" width="200" />
        <el-table-column prop="userName" label="操作用户" align="center" width="160" />
        <el-table-column prop="info" label="操作内容" align="center" />
      </el-table>
  
      <el-pagination class="tablePagination" :current-page="page.current" :total="page.total" :page-size="page.size" :page-sizes="page.sizes" :pager-count="page.count" @size-change="onPageSizeChange" @current-change="onPageChange" layout="prev,pager,next,jumper,sizes,total" />
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, reactive, computed, onMounted, nextTick, onUnmounted } from 'vue'
  import { ElMessage } from 'element-plus'
  import { MoreFilled, Search } from '@element-plus/icons-vue'
  
  const page = reactive({ current: 1, total: 0, size: 30, sizes: [30, 60, 90, 150, 300], count: 5 })
  const tableRows = ref<any[]>([])
  const searchForm = reactive({ info: '', user: '', startTime: '', endTime: '' })
  
  // 用户选择器相关状态
  const userSearchKeyword = ref('')
  const userLoading = ref(false)
  const allUsers = ref<any[]>([])
  const userPage = reactive({ current: 1, total: 0, size: 6 })
  const userDropdownVisible = ref(false)
  const userInputRef = ref()
  const userSearchInputRef = ref()
  
  // 初始化用户数据
  const initUserData = () => {
    allUsers.value = [
      { id: 1, name: 'q111' },
      { id: 2, name: 'csa' },
      { id: 3, name: 'test001' },
      { id: 4, name: 'NTJK_test' },
      { id: 5, name: 'test001a' },
      { id: 6, name: '001' },
      { id: 7, name: 'admin' },
      { id: 8, name: 'user01' },
      { id: 9, name: 'user02' },
      { id: 10, name: 'user03' },
      { id: 11, name: 'user04' },
      { id: 12, name: 'user05' },
      { id: 13, name: 'user06' },
      { id: 14, name: 'user07' },
      { id: 15, name: 'user08' },
      { id: 16, name: 'user09' },
      { id: 17, name: 'user10' },
      { id: 18, name: 'user11' },
      { id: 19, name: 'user12' },
      { id: 20, name: 'user13' },
      { id: 21, name: 'user14' },
      { id: 22, name: 'user15' }
    ]
    userPage.total = allUsers.value.length
  }
  
  // 过滤后的用户列表
  const filteredUsers = computed(() => {
    if (!userSearchKeyword.value) {
      return allUsers.value
    }
    return allUsers.value.filter(user => 
      user.name.toLowerCase().includes(userSearchKeyword.value.toLowerCase())
    )
  })
  
  // 分页后的用户列表
  const paginatedUsers = computed(() => {
    const start = (userPage.current - 1) * userPage.size
    const end = start + userPage.size
    return filteredUsers.value.slice(start, end)
  })
  
  // 切换用户下拉框
  const toggleUserDropdown = () => {
    userDropdownVisible.value = !userDropdownVisible.value
    if (userDropdownVisible.value) {
      nextTick(() => {
        userSearchInputRef.value?.focus()
      })
    }
  }
  
  // 搜索用户
  const searchUsers = () => {
    userPage.current = 1
    userPage.total = filteredUsers.value.length
  }
  
  // 重置用户搜索
  const resetUserSearch = () => {
    userSearchKeyword.value = ''
    userPage.current = 1
    userPage.total = allUsers.value.length
  }
  
  // 选择用户
  const selectUser = (user: any) => {
    searchForm.user = user.name
    userDropdownVisible.value = false
  }
  
  // 用户分页变化
  const onUserPageChange = (page: number) => {
    userPage.current = page
  }
  
  // 点击页面其他区域关闭下拉框
  const handleClickOutside = (event: Event) => {
    const userSelect = document.querySelector('.custom-user-select')
    if (userSelect && !userSelect.contains(event.target as Node)) {
      userDropdownVisible.value = false
    }
  }
  
  const initDemo = () => {
    tableRows.value = [
      { time: '2025-10-31 10:00:00', userName: '管理员', info: '登录系统' },
      { time: '2025-10-31 10:05:00', userName: '管理员', info: '新增用户 张三' },
      { time: '2025-10-31 23:39:26', userName: 'q111', info: '操作记录1' },
      { time: '2025-10-31 23:00:27', userName: 'csa', info: '操作记录2' },
      { time: '2025-10-31 23:00:26', userName: 'test001', info: '操作记录3' },
      { time: '2025-10-31 22:54:43', userName: 'NTJK_test', info: '操作记录4' },
      { time: '2025-10-31 22:53:50', userName: 'test001a', info: '操作记录5' }
    ]
    page.total = tableRows.value.length
  }
  
  const filteredRows = computed(() => {
    return tableRows.value.filter(r => {
      const infoOk = !searchForm.info || r.info.includes(searchForm.info)
      const userOk = !searchForm.user || r.userName === searchForm.user
      // 简化日期过滤：仅校验起止存在时判断
      const startOk = !searchForm.startTime || r.time >= searchForm.startTime
      const endOk = !searchForm.endTime || r.time <= searchForm.endTime
      return infoOk && userOk && startOk && endOk
    })
  })
  
  const applySearch = () => { 
    page.total = filteredRows.value.length; 
    page.current = 1; 
    ElMessage.success('筛选完成') 
  }
  
  const handleClear = () => { 
    tableRows.value = []; 
    page.total = 0; 
    ElMessage.success('清空日志(演示)') 
  }
  
  const handleRefresh = () => { 
    initDemo();
    ElMessage.success('刷新成功') 
  }
  
  const onPageSizeChange = (s: number) => { page.size = s }
  const onPageChange = (p: number) => { page.current = p }
  
  onMounted(() => {
    initDemo()
    initUserData()
    document.addEventListener('click', handleClickOutside)
  })
  
  onUnmounted(() => {
    document.removeEventListener('click', handleClickOutside)
  })
  </script>
  
  <style scoped>
  .layout { display: flex; justify-content: space-between; }
  
  /* 表单容器样式 */
  .search-form-container {
    padding: 16px;
  }
  
  /* 表单行布局 */
  .search-form-container .form-row {
    display: flex;
    align-items: center;
    margin-bottom: 16px;
    gap: 12px;
  }
  
  /* 按钮行布局 */
  .search-form-container .button-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 8px;
    margin-bottom: 0;
  }
  
  /* 搜索提示样式 */
  .search-tips {
    font-size: 12px;
    color: #909399;
  }
  
  /* 表单项等宽分布 */
  .search-form-container .form-item {
    flex: 1;
    margin-right: 0;
    margin-bottom: 0;
  }
  
  /* 确保输入框和日期选择器宽度一致 */
  .search-form-container .el-input,
  .search-form-container .el-date-picker {
    width: 100%;
  }
  
  /* 自定义用户选择器样式 */
  .custom-user-select {
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