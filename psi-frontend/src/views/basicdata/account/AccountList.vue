<template>
  <div class="sys-area">
    <!-- 操作按钮区域 -->
    <div class="operation-bar">
      <div class="operation-left">
        <!-- 三点下拉菜单 -->
        <div class="dropdown-menu">
          <button class="dropdown-button" @click="dropdownVisible = !dropdownVisible">
            <span class="dots-menu">•••</span>
          </button>
          <div v-if="dropdownVisible" class="dropdown-content" @click.stop>
            <!-- 搜索表单 -->
            <div class="search-form">
              <div class="form-row">
                <el-input
                  v-model="searchForm.name"
                  placeholder="请输入账户名称"
                  clearable
                  size="large"
                  class="form-input"
                />
                <el-input
                  v-model="searchForm.number"
                  placeholder="请输入账户编号"
                  clearable
                  size="large"
                  class="form-input"
                />
              </div>
              <div class="form-row">
                <el-input
                  v-model="searchForm.data"
                  placeholder="请输入备注信息"
                  clearable
                  size="large"
                  class="form-input"
                />
              </div>
              <div class="form-row search-button-row">
                <el-button circle @click="handleSearch">
                  <el-icon><Search /></el-icon>
                </el-button>
                <el-button @click="handleResetSearch">重置</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="operation-right">
        <el-button
          :type="'primary'"
          style="background-color: white; color: #409eff; border-color: #dcdfe6"
          @click="handleAdd"
          class="action-btn"
        >
          <el-icon><Plus /></el-icon>
          新增
        </el-button>
        <el-button
          style="background-color: white; border-color: #dcdfe6"
          @click="handleRefresh"
          class="action-btn"
        >
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <el-divider class="custom-divider" />

    <!-- 主内容区域：表格 -->
    <div class="main-content">
      <!-- 数据表格 -->
      <div class="table-section">
        <!-- 表格容器 - 添加滚动条 -->
        <div class="table-container">
          <NormalTable
            :tabdata="tablePageData"
            :tabdatacolumns="tabdatacolumns"
            :taboperbtns="taboperbtns"
            :istabmultiple="true"
            @selection-change="handleSelectionChange"
            @taboper-click="handleTableOperClick"
            @page-change="handlePageChange"
            :tabattr="tableAttr"
          >
            <!-- 自定义单元格内容 -->
            <template #customercell="{ column, prop, row }">
              <!-- 账户余额显示 -->
              <template v-if="prop === 'balance'">
                {{ formatPrice(row.balance) }}
              </template>
              <!-- 操作按钮显示 -->
              <template v-else-if="prop === 'operate'">
                <div class="operate-buttons">
                  <el-button
                    :type="'primary'"
                    size="small"
                    @click="handleTableOperClick(0, row, 'view')"
                    style="
                      background-color: white;
                      color: #409eff;
                      border-color: #dcdfe6;
                      margin-right: 4px;
                    "
                  >
                    详情
                  </el-button>
                  <el-button
                    :type="'danger'"
                    size="small"
                    @click="handleTableOperClick(0, row, 'delete')"
                    style="background-color: white; color: #f56c6c; border-color: #dcdfe6"
                  >
                    删除
                  </el-button>
                </div>
              </template>
              <!-- 默认显示 -->
              <template v-else>
                {{ row[prop] || '-' }}
              </template>
            </template>
          </NormalTable>
        </div>
      </div>
    </div>
  </div>

  <!-- 账户详情对话框 -->
  <el-dialog
    v-model="dialogVisible"
    :title="dialogType === 'add' ? '新增账户' : '账户详情'"
    width="720px"
    :before-close="handleDialogClose"
  >
    <div class="form-content">
      <div class="form-grid">
        <div class="form-item">
          <label class="required">账户名称</label>
          <el-input v-model="accountForm.name" placeholder="请输入账户名称" />
        </div>
        <div class="form-item">
          <label class="required">账户编号</label>
          <el-input v-model="accountForm.number" placeholder="请输入账户编号" />
        </div>
      </div>
      <div class="form-grid">
        <div class="form-item">
          <label class="required">所属组织</label>
          <el-select 
            v-model="accountForm.frame" 
            placeholder="请选择所属组织"
            filterable
            remote
            :remote-method="handleOrgRemoteSearch"
            @visible-change="handleOrgVisibleChange"
            ref="orgSelectRef"
            class="org-select"
            popper-class="org-select-popper"
          >
            <template #default>
              <div class="search-tip">
                🔍 F1 输入内容回车搜索
              </div>
              <div v-for="org in filteredOrgList" :key="org" class="user-select-item">
                <el-option :label="org" :value="org" />
              </div>
              <div class="pagination-container">
                <el-button 
                  size="small" 
                  :disabled="orgPageData.currentPage === 1"
                  @click="handleOrgPageChange(-1)"
                >
                  <i>←</i>
                </el-button>
                <span class="pagination-info">共{{ orgPageData.total }}条</span>
                <el-button 
                  size="small" 
                  :disabled="orgPageData.currentPage >= orgPageData.totalPages"
                  @click="handleOrgPageChange(1)"
                >
                  <i>→</i>
                </el-button>
              </div>
            </template>
          </el-select>
        </div>
        <div class="form-item">
          <label class="required">时间</label>
          <el-date-picker
            v-model="accountForm.time"
            type="date"
            placeholder="请选择时间"
            style="width: 100%"
          />
        </div>
      </div>
      <div class="form-grid">
        <div class="form-item">
          <label class="required">期初余额</label>
          <el-input-number
            v-model="accountForm.initial"
            :precision="2"
            :step="0.01"
            :min="0"
            style="width: 100%"
          />
        </div>
        <div class="form-item">
          <label class="required">当前余额</label>
          <el-input-number
            v-model="accountForm.balance"
            :precision="2"
            :step="0.01"
            :min="0"
            style="width: 100%"
          />
        </div>
      </div>
      <div class="form-item full-width">
        <label>备注信息</label>
        <el-input
          v-model="accountForm.data"
          placeholder="请输入备注信息"
          type="textarea"
          :rows="3"
        />
      </div>
    </div>

    <template #footer>
      <div style="display: flex; justify-content: flex-end; gap: 10px">
        <el-button style="background-color: white; border-color: #dcdfe6" @click="handleDialogClose"
          >取消</el-button
        >
        <el-button
          :type="'primary'"
          style="background-color: white; color: #409eff; border-color: #dcdfe6"
          @click="handleSave"
          >保存</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import '../styles/common-styles.css'
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Refresh } from '@element-plus/icons-vue'
import NormalTable from '@/components/normaltable/NormalTable.vue'
import {
  type PageDTO,
  createPageDTO,
  type MyTableColumn,
  type MyTableOperationsBtn
} from '@/components/normaltable/type'

// 导入API类和类型定义
import { AccountAPI, type FundsDTO, type AccountDetailDTO, type AddAccountDTO, type AccountQuery } from '@/apis/basicdata/account'
import type { JsonVO } from '@/apis/http'

// 资金账户接口 - 与account.ts保持一致
interface Account {
  id: string
  name: string
  number: string
  frame: string
  time: string
  balance: number
  initial?: number
  data?: string
}

// 响应式数据
const dropdownVisible = ref(false)
const accountList = ref<Account[]>([])
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const currentAccount = ref<Account | null>(null)
const selectedAccounts = ref<Account[]>([])
const loading = ref(false)

// 组织列表数据
const orgList = ref<string[]>([
  '默认组织',
  '四川',
  '达利西亚',
  '华北区域',
  '华东区域',
  '华南区域',
  '西南区域',
  '西北区域',
  '东北区域',
  '总部',
  '研发中心',
  '销售中心',
  '财务部',
  '人力资源部'
])

// 组织分页数据
const orgPageData = reactive({
  currentPage: 1,
  pageSize: 6,
  total: 0,
  totalPages: 0
})

// 组织搜索关键词
const orgSearchQuery = ref('')

// 组织选择框引用
const orgSelectRef = ref<InstanceType<typeof import('element-plus')['ElSelect']>>()

// 分页数据 - 使用 NormalTable 的 PageDTO 格式
const tablePageData = ref<PageDTO<Account>>(createPageDTO())

// 表格列配置
const tabdatacolumns: MyTableColumn[] = [
  { prop: 'name', label: '账户名称', width: 180 },
  { prop: 'number', label: '账户编号', width: 150 },
  { prop: 'frame', label: '所属组织', width: 120 },
  { prop: 'time', label: '时间', width: 120 },
  { prop: 'balance', label: '账户余额', width: 150 },
  { prop: 'data', label: '备注信息' },
  { prop: 'operate', label: '相关操作', width: 150, fixed: 'right' }
]

// 表格操作按钮配置
const taboperbtns: MyTableOperationsBtn[] = [
  {
    text: '详情',
    attr: { size: 'small', type: 'primary' },
    evtname: 'view'
  },
  {
    text: '删除',
    attr: { size: 'small', type: 'danger' },
    evtname: 'delete'
  }
]

// 表格属性配置
const tableAttr = reactive({
  'header-cell-style': {
    textAlign: 'center',
    backgroundColor: '#f5f7fa',
    fontWeight: 600
  },
  'cell-style': {
    textAlign: 'center',
    padding: '12px 0'
  }
})

// 表单数据
const searchForm = reactive<AccountQuery>({
  pageIndex: 1,
  pageSize: 10,
  name: '',
  number: '',
  data: ''
})

// 账户表单数据 - 使用API要求的字段名
const accountForm = reactive({
  name: '',
  number: '',
  frame: '默认组织',
  time: new Date().toISOString().split('T')[0],
  initial: 0,
  balance: 0,
  data: ''
})

// 安全的数据转换工具函数
function safeParseData(data: any): any {
  if (data === null || data === undefined) return null
  
  // 处理日期对象
  if (data instanceof Date) {
    return data.toISOString().split('T')[0]
  }
  
  // 处理数组
  if (Array.isArray(data)) {
    return data.map(item => safeParseData(item))
  }
  
  // 处理对象
  if (typeof data === 'object') {
    const parsedObj: Record<string, any> = {}
    Object.keys(data).forEach(key => {
      // 确保所有ID字段都转换为字符串
      if (key.toLowerCase().includes('id') || key === 'number') {
        parsedObj[key] = String(data[key] || '')
      } else {
        parsedObj[key] = safeParseData(data[key])
      }
    })
    return parsedObj
  }
  
  // 确保数字类型正确处理
  if (typeof data === 'number' && isNaN(data)) {
    return 0
  }
  
  return data
}

// 从API获取账户列表数据
const fetchAccountList = async () => {
  try {
    loading.value = true
    // 准备查询参数，移除空字符串和无效值，避免JSON解析错误
    const params: any = {
      pageIndex: Number(tablePageData.value.pageIndex) || 1,
      pageSize: Number(tablePageData.value.pageSize) || 10
    }
    
    if (searchForm.name && searchForm.name.trim()) {
      params.name = searchForm.name.trim()
    }
    if (searchForm.number && searchForm.number.trim()) {
      params.number = searchForm.number.trim()
    }
    if (searchForm.data && searchForm.data.trim()) {
      params.data = searchForm.data.trim()
    }
    
    // 记录请求参数，用于调试
    console.log('Fetching account list with params:', params)
    
    const response = await AccountAPI.getAccountList(params)
    
    // 安全解析响应数据
    const safeResponse = safeParseData(response)
    
    // 验证响应结构
    if (!safeResponse || typeof safeResponse !== 'object') {
      throw new Error('无效的响应数据结构')
    }
    
    if (safeResponse.code === 0 && safeResponse.data) {
      // 转换数据格式，确保字段名称匹配
      accountList.value = safeResponse.data.rows.map((item: any) => ({
        id: String(item.id) || '',
        name: String(item.name) || '',
        number: String(item.number) || '',
        frame: String(item.frame) || '',
        time: String(item.time) || '',
        balance: Number(item.balance) || 0,
        initial: Number(item.initial) || 0,
        data: String(item.data) || ''
      }))
      
      // 更新分页数据
      tablePageData.value = {
        ...tablePageData.value,
        total: Number(safeResponse.data.total) || 0,
        pages: Number(safeResponse.data.pages) || 0,
        rows: accountList.value
      }
    } else {
      ElMessage.error(safeResponse?.message || '获取账户列表失败')
    }
  } catch (error) {
    accountList.value = []
    tablePageData.value.rows = []
    console.error('获取账户列表异常:', error)
    
    // 提供更详细的错误信息
    const errorMessage = error instanceof Error ? error.message : '未知错误'
    
    // 针对不同错误类型提供更友好的提示
    if (errorMessage.includes('Cannot use \'in\' operator') || errorMessage.includes('null')) {
      ElMessage.error('网络请求异常，请检查服务器连接')
    } else if (errorMessage.includes('JSON') || errorMessage.includes('parse')) {
      ElMessage.error('数据格式错误，请稍后重试')
    } else if (errorMessage.includes('timeout')) {
      ElMessage.error('请求超时，请检查网络连接')
    } else {
      ElMessage.error(`获取账户列表失败: ${errorMessage}`)
    }
  } finally {
    loading.value = false
  }
}

// 更新表格数据
const updateTableData = () => {
  fetchAccountList()
}

// 生命周期
onMounted(() => {
  fetchAccountList()
  document.addEventListener('click', handleDocumentClick)
})

onUnmounted(() => {
  document.removeEventListener('click', handleDocumentClick)
})

// 计算属性 - 过滤后的组织列表
const filteredOrgList = computed(() => {
  const filtered = orgSearchQuery.value
    ? orgList.value.filter(org => 
        org.toLowerCase().includes(orgSearchQuery.value.toLowerCase())
      )
    : orgList.value
  
  // 更新总数和总页数
  orgPageData.total = filtered.length
  orgPageData.totalPages = Math.ceil(filtered.length / orgPageData.pageSize)
  
  // 确保当前页不超过总页数
  if (orgPageData.currentPage > orgPageData.totalPages && orgPageData.totalPages > 0) {
    orgPageData.currentPage = orgPageData.totalPages
  }
  
  // 返回当前页的数据
  const startIndex = (orgPageData.currentPage - 1) * orgPageData.pageSize
  const endIndex = startIndex + orgPageData.pageSize
  return filtered.slice(startIndex, endIndex)
})

// 组织下拉框可见性变化处理
const handleOrgVisibleChange = (visible: boolean) => {
  if (visible) {
    // 重置搜索和分页状态
    orgSearchQuery.value = ''
    orgPageData.currentPage = 1
    
    // 添加键盘事件监听
    setTimeout(() => {
      const input = document.querySelector('.org-select .el-input__inner')
      if (input) {
        input.addEventListener('keydown', handleOrgInputKeydown as EventListener)
      }
    }, 0)
  } else {
    // 移除键盘事件监听
    const input = document.querySelector('.org-select .el-input__inner')
    if (input) {
      input.removeEventListener('keydown', handleOrgInputKeydown as EventListener)
    }
  }
}

// 组织输入框键盘事件处理
const handleOrgInputKeydown = (event: Event) => {
  const keyboardEvent = event as KeyboardEvent
  if (keyboardEvent.key === 'Enter') {
    handleOrgRemoteSearch((keyboardEvent.target as HTMLInputElement).value)
  }
}

// 组织远程搜索处理
const handleOrgRemoteSearch = (query: string) => {
  // 模拟远程搜索延迟
  setTimeout(() => {
    orgSearchQuery.value = query
    orgPageData.currentPage = 1
  }, 300)
}

// 组织分页变化处理
const handleOrgPageChange = (direction: number) => {
  orgPageData.currentPage += direction
  // 滚动到顶部
  setTimeout(() => {
    const popper = document.querySelector('.org-select-popper .el-select-dropdown__wrap')
    if (popper) {
      popper.scrollTop = 0
    }
  }, 0)
}

// 事件处理函数
function handleDocumentClick(event: MouseEvent) {
  const target = event.target as HTMLElement
  if (!target.closest('.dropdown-menu')) {
    dropdownVisible.value = false
  }
}

function handleSearch() {
  tablePageData.value.pageIndex = 1
  dropdownVisible.value = false
  fetchAccountList()
}

function handleResetSearch() {
  searchForm.name = ''
  searchForm.number = ''
  searchForm.data = ''
  tablePageData.value.pageIndex = 1
  fetchAccountList()
}

// 处理选中项变化
const handleSelectionChange = (val: Account[]) => {
  selectedAccounts.value = val
}

// 分页处理函数
const handlePageChange = async (data: PageDTO<Account>) => {
  tablePageData.value = data
  await fetchAccountList()
}

// 表格操作处理函数
const handleTableOperClick = (index: number, row: Account, evtname: string) => {
  if (evtname === 'view') {
    handleView(row)
  } else if (evtname === 'delete') {
    handleDelete(row)
  }
}

function handleAdd() {
  dialogType.value = 'add'
  currentAccount.value = null
  Object.assign(accountForm, {
    name: '',
    number: '',
    frame: '默认组织',
    time: new Date().toISOString().split('T')[0],
    initial: 0,
    balance: 0,
    data: ''
  })
  dialogVisible.value = true
}

function handleView(row: Account) {
  dialogType.value = 'edit'
  currentAccount.value = row
  Object.assign(accountForm, {
    name: row.name,
    number: row.number,
    frame: row.frame,
    time: row.time,
    initial: row.initial || 0,
    balance: row.balance,
    data: row.data || ''
  })
  dialogVisible.value = true
}

async function handleDelete(row: Account) {
  try {
    // 确保row对象和id存在
    if (!row || !row.id) {
      ElMessage.warning('无效的账户数据')
      return
    }
    
    await ElMessageBox.confirm(`确定要删除账户「${row.name || '未知'}」吗？`, '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    loading.value = true
    
    // 确保id转换为字符串，防止C++后端解析错误
    const response = await AccountAPI.deleteAccount(String(row.id))
    
    // 安全解析响应
    const safeResponse = safeParseData(response)
    
    // 处理不同可能的响应格式
    if (safeResponse) {
      if (safeResponse.code === 0 || safeResponse.success === true || safeResponse.ok === true) {
        ElMessage.success('删除成功')
        await fetchAccountList() // 重新加载列表
      } else {
        ElMessage.error(safeResponse.message || safeResponse.msg || '删除失败')
      }
    } else {
      throw new Error('无响应数据')
    }
  } catch (error: any) {
    console.error('删除账户异常:', error)
    // 避免处理取消操作的错误
    if (error !== 'cancel') {
      const errorMessage = error instanceof Error ? error.message : '未知错误'
      
      // 针对不同错误类型提供更友好的提示
      if (errorMessage.includes('Cannot use \'in\' operator') || errorMessage.includes('null')) {
        ElMessage.error('网络请求异常，请检查服务器连接')
      } else if (errorMessage.includes('权限') || errorMessage.includes('Permission')) {
        ElMessage.error('没有删除权限，请联系管理员')
      } else if (errorMessage.includes('JSON') || errorMessage.includes('parse')) {
        ElMessage.error('数据格式错误，请稍后重试')
      } else if (errorMessage.includes('timeout')) {
        ElMessage.error('请求超时，请检查网络连接')
      } else {
        ElMessage.error(`删除账户失败: ${errorMessage}`)
      }
    }
  } finally {
    loading.value = false
  }
}

// 表单验证
function validateForm(): string[] {
  const errors: string[] = []
  
  // 安全检查表单数据
  if (!accountForm) {
    errors.push('表单数据异常')
    return errors
  }
  
  if (!accountForm.name || accountForm.name.trim() === '') {
    errors.push('请输入账户名称')
  }
  
  if (!accountForm.number || accountForm.number.trim() === '') {
    errors.push('请输入账户编号')
  }
  
  if (!accountForm.frame || accountForm.frame.trim() === '') {
    errors.push('请选择所属组织')
  }
  
  if (!accountForm.time) {
    errors.push('请选择时间')
  }
  
  // 验证数字字段
  const initialValue = Number(accountForm.initial || 0)
  const balanceValue = Number(accountForm.balance || 0)
  
  if (isNaN(initialValue) || initialValue < 0) {
    errors.push('期初余额必须是非负数')
  }
  
  if (isNaN(balanceValue) || balanceValue < 0) {
    errors.push('当前余额必须是非负数')
  }
  
  return errors
}

async function handleSave() {
  try {
    // 表单验证
    const errors = validateForm()
    if (errors.length > 0) {
      errors.forEach(msg => ElMessage.error(msg))
      return
    }
    
    loading.value = true
    
    if (dialogType.value === 'add') {
      // 准备新增数据，进行数据转换和安全处理
      const addData = {
        accountname: accountForm.name?.trim() || '',
        accountnumber: accountForm.number?.trim() || '',
        frame: accountForm.frame?.trim() || '',
        time: accountForm.time || new Date().toISOString().split('T')[0],
        initial: Number(accountForm.initial || 0),
        balance: Number(accountForm.balance || 0),
        data: accountForm.data?.trim() || ''
      }
      
      const response = await AccountAPI.addAccount(addData)
      
      // 安全解析和验证响应
      const safeResponse = safeParseData(response)
      
      // 处理不同可能的响应格式
      if (safeResponse && 
          (safeResponse.code === 0 || 
           safeResponse.success === true || 
           safeResponse.ok === true || 
           (safeResponse.data && safeResponse.data.code === 0))) {
        
        ElMessage.success('新增成功')
        dialogVisible.value = false
        await fetchAccountList() // 重新加载列表
      } else {
        // 提取错误消息
        const errorMsg = safeResponse?.message || 
                        safeResponse?.msg || 
                        safeResponse?.data?.message || 
                        '新增失败'
        ElMessage.error(errorMsg)
      }
    } else if (currentAccount.value) {
      // 准备更新数据，进行数据转换和安全处理
      const updateData = {
        accountname: accountForm.name?.trim() || '',
        accountnumber: accountForm.number?.trim() || '',
        frame: accountForm.frame?.trim() || '',
        time: accountForm.time || new Date().toISOString().split('T')[0],
        initial: Number(accountForm.initial || 0),
        balance: Number(accountForm.balance || 0),
        data: accountForm.data?.trim() || ''
      }
      
      // 确保id转换为字符串
      const response = await AccountAPI.updateAccount(String(currentAccount.value.id), updateData)
      
      // 安全解析和验证响应
      const safeResponse = safeParseData(response)
      
      // 处理不同可能的响应格式
      if (safeResponse && 
          (safeResponse.code === 0 || 
           safeResponse.success === true || 
           safeResponse.ok === true || 
           (safeResponse.data && safeResponse.data.code === 0))) {
        
        ElMessage.success('更新成功')
        dialogVisible.value = false
        await fetchAccountList() // 重新加载列表
      } else {
        // 提取错误消息
        const errorMsg = safeResponse?.message || 
                        safeResponse?.msg || 
                        safeResponse?.data?.message || 
                        '更新失败'
        ElMessage.error(errorMsg)
      }
    }
  } catch (error) {
    console.error('保存账户异常:', error)
    
    // 提供更详细的错误信息
    const errorMessage = error instanceof Error ? 
                        error.message : 
                        ((error as any)?.message || '未知错误')
    
    // 针对不同错误类型提供更友好的提示
    if (errorMessage.includes('Cannot use \'in\' operator') || errorMessage.includes('null')) {
      ElMessage.error('网络请求异常，请检查服务器连接')
    } else if (errorMessage.includes('JSON') || 
               errorMessage.includes('parse') || 
               errorMessage.includes('Map_base::at')) {
      ElMessage.error('数据格式错误，请检查输入内容')
    } else if (errorMessage.includes('权限') || errorMessage.includes('Permission')) {
      ElMessage.error('没有保存权限，请联系管理员')
    } else if (errorMessage.includes('timeout')) {
      ElMessage.error('请求超时，请检查网络连接')
    } else if (errorMessage.includes('账户名称') || errorMessage.includes('accountname')) {
      ElMessage.error('账户名称已存在或格式不正确')
    } else if (errorMessage.includes('账户编号') || errorMessage.includes('accountnumber')) {
      ElMessage.error('账户编号已存在或格式不正确')
    } else {
      ElMessage.error(`保存账户失败: ${errorMessage}`)
    }
  } finally {
    loading.value = false
  }
}

function handleDialogClose() {
  dialogVisible.value = false
}

async function handleRefresh() {
  ElMessage.info('正在刷新数据...')
  await fetchAccountList()
  ElMessage.success('数据刷新成功')
}

function formatPrice(price: number) {
  return new Intl.NumberFormat('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(price)
}
</script>

<style scoped>
.sys-area {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

/* 操作栏样式 */
.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 16px;
}

.operation-left {
  display: flex;
  align-items: center;
}

.operation-right {
  display: flex;
  gap: 10px;
}

.action-btn {
  min-width: 80px;
  font-size: 14px;
}

/* 三点下拉菜单样式 */
.dropdown-menu {
  position: relative;
  display: inline-block;
}

.dropdown-button {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.dropdown-button:hover {
  background-color: #f5f5f5;
}

.dropdown-content {
  position: absolute;
  left: 0;
  top: 100%;
  background-color: white;
  min-width: 800px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-radius: 8px;
  padding: 16px;
  z-index: 1000;
  margin-top: 4px;
  max-height: calc(100vh - 100px);
  overflow-y: auto;
}

/* 搜索表单样式 */
.search-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-row {
  display: flex;
  gap: 16px;
}

.form-input,
.form-select {
  flex: 1;
}

.search-button-row {
  justify-content: flex-end;
  gap: 10px;
}

/* 确保搜索按钮为白色 */
.search-button-row .el-button {
  background-color: white !important;
  border: 1px solid #dcdfe6;
  color: #606266;
}

.search-button-row .el-button:hover {
  background-color: #ecf5ff;
  border-color: #c6e2ff;
  color: #409eff;
}

/* 分割线样式 */
.custom-divider {
  margin: 16px 0;
}

/* 主内容区域样式 */
.main-content {
  /* 移除flex布局，避免左侧空白过大 */
}

.table-section {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 表格容器样式 */
.table-container {
  padding: 0;
  max-height: calc(100vh - 240px);
  overflow-y: auto;
  font-size: 14px;
}

/* 表单样式 */
.form-content {
  padding: 20px 0;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.form-item.full-width {
  grid-column: span 2;
}

.form-item label {
  font-size: 14px;
  color: #606266;
}

.form-item label.required::before {
  content: '*';
  color: #f56c6c;
  margin-right: 4px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .operation-bar {
    flex-direction: column;
    align-items: stretch;
    gap: 16px;
  }

  .operation-right {
    justify-content: center;
    flex-wrap: wrap;
  }

  .form-row {
    flex-direction: column;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-item.full-width {
    grid-column: span 1;
  }
}

/* 滚动条样式优化 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 确保文字清晰可见的样式 */
:deep(.el-input__inner),
:deep(.el-select__input),
:deep(.el-button),
:deep(.el-table__header th),
:deep(.el-table__body td) {
  font-size: 14px;
  line-height: 1.5;
  color: #303133;
}

:deep(.el-table__header th) {
  font-weight: 600;
  background-color: #f5f7fa;
}

:deep(.el-table) {
  border: 1px solid #ebeef5;
}

:deep(.el-table__header th) {
  text-align: center;
  background-color: #f5f7fa;
  padding: 12px 0;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

:deep(.el-table__body td) {
  text-align: center;
  padding: 12px 0;
  font-size: 14px;
  color: #606266;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-table__body tr:hover > td) {
  background-color: #f5f7fa;
}

/* 确保选择框居中 */
:deep(.el-table__row .el-checkbox__inner) {
  margin: 0 auto;
  display: block;
}

/* 操作按钮样式 */
:deep(.el-table .el-button--small) {
  padding: 6px 12px;
  font-size: 13px;
}

/* 操作按钮间距 */
:deep(.el-table__body .el-button--small) {
  margin: 0 4px;
}

/* 操作列居中 */
:deep(.el-table__column--fixed-right) {
  text-align: center;
}

/* 组织选择下拉框样式 */
.org-select-popper {
  width: 280px !important;
  padding: 8px 0;
}

.org-select-popper .search-tip {
  padding: 10px 15px;
  font-size: 12px;
  color: #909399;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 5px;
}

.org-select-popper .search-tip::before {
  content: '🔍 ';
  margin-right: 4px;
}

.org-select-popper .user-select-item {
  padding: 0 15px;
}

.org-select-popper .user-select-item:hover {
  background-color: #f5f7fa;
}

.org-select-popper .pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px 15px;
  border-top: 1px solid #ebeef5;
  margin-top: 5px;
  gap: 10px;
}

.org-select-popper .pagination-info {
  font-size: 12px;
  color: #909399;
}

.org-select-popper .pagination-container .el-button {
  padding: 4px 8px;
  min-width: 32px;
  height: 28px;
  font-size: 12px;
  line-height: 1;
}

.org-select-popper .pagination-container .el-button i {
  font-style: normal;
  font-size: 12px;
}
</style>
