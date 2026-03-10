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
                  placeholder="请输入供应商名称"
                  clearable
                  size="large"
                  class="form-input"
                />
                <el-input
                  v-model="searchForm.code"
                  placeholder="请输入供应商编号"
                  clearable
                  size="large"
                  class="form-input"
                />
              </div>
              <div class="form-row">
                <el-select
                  v-model="searchForm.category"
                  placeholder="请选择供应商类别"
                  clearable
                  size="large"
                  class="form-select"
                >
                  <el-option label="常规类别" value="常规类别" />
                  <el-option label="特殊类别" value="特殊类别" />
                </el-select>
                <el-select
                  v-model="searchForm.belongUser"
                  placeholder="请选择所属用户"
                  clearable
                  size="large"
                  class="form-select"
                >
                  <el-option label="管理员" value="管理员" />
                  <el-option label="操作员" value="操作员" />
                </el-select>
                <el-cascader
                  v-model="searchForm.orgName"
                  :options="organizationOptions"
                  placeholder="请选择所属组织"
                  clearable
                  size="large"
                  class="form-select"
                  :props="{ expandTrigger: 'hover', multiple: false }"
                  @change="handleOrgNameChange"
                />
              </div>
              <div class="form-row">
                <el-input
                  v-model="searchForm.remark"
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
        <el-button type="primary" @click="handleAdd" class="action-btn">
          <el-icon><Plus /></el-icon>
          新增
        </el-button>
        <el-button @click="handleBatch" class="action-btn">
          <el-icon><Collection /></el-icon>
          批量
        </el-button>
        <el-button @click="handleRefresh" class="action-btn">
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
        <!-- 表格容器 -->
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
          />
        </div>
      </div>
    </div>
  </div>

  <!-- 供应商编辑对话框 -->
  <el-dialog
    v-model="dialogVisible"
    :title="dialogType === 'add' ? '新增供应商' : '供应商详情'"
    width="800px"
    :before-close="handleDialogClose"
  >
    <el-tabs v-model="activeTab" type="card">
      <!-- 基础资料标签页 -->
      <el-tab-pane label="基础资料" name="basic">
        <div class="form-content">
          <div class="form-grid">
            <div class="form-item">
              <label class="required">供应商名称</label>
              <el-input v-model="supplierForm.name" placeholder="请输入供应商名称" :disabled="dialogType === 'view'" />
            </div>
            <div class="form-item">
              <label class="required">供应商编号</label>
              <el-input v-model="supplierForm.code" placeholder="请输入供应商编号" :disabled="dialogType === 'view'" />
            </div>
          </div>
          <div class="form-grid">
            <div class="form-item">
              <label class="required">所属组织</label>
              <el-cascader
                v-model="supplierForm.orgName"
                :options="organizationOptions"
                placeholder="请选择所属组织"
                clearable
                :props="{ expandTrigger: 'hover', multiple: false }"
                :disabled="dialogType === 'view'"
              />
            </div>
            <div class="form-item">
              <label class="required">所属用户</label>
              <el-select 
                v-model="supplierForm.belongUser" 
                placeholder="请选择所属用户"
                filterable
                remote
                :remote-method="handleUserRemoteSearch"
                @visible-change="handleUserVisibleChange"
                ref="userSelectRef"
                class="user-select"
                popper-class="user-select-popper"
                :disabled="dialogType === 'view'"
              >
                <template #default>
                  <div class="search-tip">
                    🔍 F1 输入内容回车搜索
                  </div>
                  <div v-for="user in filteredUserList" :key="user" class="user-select-item">
                    <el-option :label="user" :value="user" />
                  </div>
                  <div class="pagination-container">
                    <el-button 
                      size="small" 
                      :disabled="userPageData.currentPage === 1"
                      @click="handleUserPageChange(-1)"
                    >
                      <i>←</i>
                    </el-button>
                    <span class="pagination-info">共{{ userPageData.total }}条</span>
                    <el-button 
                      size="small" 
                      :disabled="userPageData.currentPage >= userPageData.totalPages"
                      @click="handleUserPageChange(1)"
                    >
                      <i>→</i>
                    </el-button>
                  </div>
                </template>
              </el-select>
            </div>
          </div>
          <div class="form-grid">
            <div class="form-item">
              <label>供应商类别</label>
              <el-select v-model="supplierForm.category" placeholder="请选择供应商类别" :disabled="dialogType === 'view'">
                <el-option label="常规类别" value="常规类别" />
                <el-option label="特殊类别" value="特殊类别" />
              </el-select>
            </div>
            <div class="form-item">
              <label>应付款余额</label>
              <el-input v-model.number="supplierForm.应付余额" placeholder="0" type="number" :disabled="dialogType === 'view'" />
            </div>
          </div>
          <div class="form-grid">
            <div class="form-item">
              <label>开户银行</label>
              <el-input v-model="supplierForm.bank" placeholder="请输入开户银行" :disabled="dialogType === 'view'" />
            </div>
            <div class="form-item">
              <label>银行账号</label>
              <el-input v-model="supplierForm.account" placeholder="请输入银行账号" :disabled="dialogType === 'view'" />
            </div>
          </div>
          <div class="form-grid">
            <div class="form-item">
              <label>纳税号码</label>
              <el-input v-model="supplierForm.taxNo" placeholder="请输入纳税人识别号" :disabled="dialogType === 'view'" />
            </div>
            <div class="form-item">
              <label>备注信息</label>
              <el-input v-model="supplierForm.remark" placeholder="请输入备注信息" :disabled="dialogType === 'view'" />
            </div>
          </div>
        </div>
      </el-tab-pane>

      <!-- 联系资料标签页 -->
      <el-tab-pane label="联系资料" name="contact">
        <div class="form-content">
          <div class="contact-section">
            <div class="section-header">
              <span>联系人信息</span>
            </div>
            <div class="contact-table-container">
              <NormalTable
                :tabdata="{ rows: contactList, total: contactList.length, pageIndex: 1, pageSize: 10 }"
                :tabdatacolumns="contactTableColumns"
                :taboperbtns="contactTableOperBtns"
                :istabmultiple="false"
                @taboper-click="handleContactTableOperClick"
                :tabattr="{
                  'header-cell-style': {
                    textAlign: 'center',
                    backgroundColor: '#f5f7fa',
                    fontWeight: 600
                  },
                  'cell-style': {
                    textAlign: 'center',
                    padding: '12px 0'
                  }
                }"
              >
                <template #header>
                  <el-button v-if="dialogType !== 'view'" size="small" type="primary" icon="Plus" @click="handleAddContact">
                    新增
                  </el-button>
                </template>
              </NormalTable>
              <div v-if="contactList.length === 0" class="no-data">暂无数据</div>
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <template #footer>
      <div style="display: flex; justify-content: flex-end; gap: 10px">
        <el-button @click="handleDialogClose">取消</el-button>
          <el-button v-if="dialogType !== 'view'" type="primary" @click="handleSave">保存</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 联系资料编辑对话框 -->
  <el-dialog
    v-model="contactDialogVisible"
    title="联系资料"
    width="600px"
    @close="handleContactDialogClose"
  >
    <div class="contact-form-content">
      <!-- 主联系人开关 -->
      <div style="margin-bottom: 20px">
        <el-switch
          v-model="contactForm.isMain"
          active-text="主联系人"
          @change="handleMainContactChange"
        />
      </div>

      <!-- 垂直排列的表单项 -->
      <div class="form-item">
        <label class="required">联系人员</label>
        <el-input v-model="contactForm.name" placeholder="请输入联系人员" />
      </div>

      <div class="form-item">
        <label class="required">联系号码</label>
        <el-input v-model="contactForm.phone" placeholder="请输入联系号码" />
      </div>

      <div class="form-item">
        <label>联系地址</label>
        <el-input v-model="contactForm.address" placeholder="请输入联系地址" />
      </div>

      <div class="form-item">
        <label>备注信息</label>
        <el-input v-model="contactForm.remark" placeholder="请输入备注信息" />
      </div>
    </div>

    <template #footer>
      <div style="display: flex; justify-content: flex-end; gap: 10px">
        <el-button @click="handleContactDialogClose">取消</el-button>
        <el-button type="primary" @click="handleSaveContact">保存</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 批量操作对话框 -->
  <el-dialog
    v-model="batchDialogVisible"
    title="批量操作"
    width="400px"
    @close="handleBatchDialogClose"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-tabs v-model="batchActiveTab" class="batch-tabs">
      <!-- 导入数据标签页 -->
      <el-tab-pane label="导入数据" name="import">
        <div class="import-content">
          <ol class="import-instructions">
            <li>该功能适用于批量导入数据。</li>
            <li>您需要下载数据模板后使用Excel录入数据。</li>
            <li>录入数据时，请勿修改首行数据标题以及排序。</li>
            <li>请查询使用文档获取字段格式内容以及相关导入须知。</li>
            <li>点击下方上传模板，选择您编辑好的模板文件即可。</li>
          </ol>
          <div class="import-actions">
            <el-button @click="handleDownloadTemplate">下载模板</el-button>
            <el-button type="primary" @click="handleUploadTemplate">上传模板</el-button>
          </div>
        </div>
      </el-tab-pane>

      <!-- 导出数据标签页 -->
      <el-tab-pane label="导出数据" name="export">
        <div class="export-content" @click="handleExportData">
          <div class="export-button-container">
            <el-icon class="export-icon"><Refresh /></el-icon>
            <span class="export-text">导出数据</span>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script setup lang="ts">
import '../styles/common-styles.css'
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { CascaderValue } from 'element-plus'
import { Search, Plus, Collection, Refresh } from '@element-plus/icons-vue'
import NormalTable from '@/components/normaltable/NormalTable.vue'
import {
  type PageDTO,
  createPageDTO,
  type MyTableColumn,
  type MyTableOperationsBtn
} from '@/components/normaltable/type'

// 组织树形数据
const organizationOptions = ref([
  {
    value: '默认组织',
    label: '默认组织',
    children: [
      { value: '公司1', label: '公司1' },
      { value: '法师', label: '法师' },
      { value: '超级管理员', label: '超级管理员' },
      { value: '四川', label: '四川' },
      { value: '二', label: '二', children: [
        { value: 'NTJK', label: 'NTJK' },
        { value: 'SCS', label: 'SCS' }
      ]}
    ]
  },
  {
    value: '其他组织',
    label: '其他组织',
    children: [
      { value: '子组织1', label: '子组织1' },
      { value: '子组织2', label: '子组织2' }
    ]
  }
])

// 在文件开头添加API导入
import { SupplierAPI } from '@/apis/basicdata/supplier'
import type { Supplier as ApiSupplier, SupplierQuery, AddSupplierRequest, SupplierExportQuery } from '@/apis/basicdata/supplier'

// 扩展 Supplier 类型以包含前端使用的字段
interface Supplier extends ApiSupplier {
  orgName?: string[] | string
  belongUser?: string
}

// 修改响应式数据的初始值
const supplierList = ref<Supplier[]>([])
// 添加loading状态变量
const loading = ref(false)
const contactLoading = ref(false)
const saveLoading = ref(false)
const deleteLoading = ref(false)
const importLoading = ref(false)
const exportLoading = ref(false)

// 表格分页数据
const tablePageData = ref<{
  pageIndex: number
  pageSize: number
  total: number
  rows: Supplier[]
}>({
  pageIndex: 1,
  pageSize: 10,
  total: 0,
  rows: []
})

// 下拉菜单显示状态
const dropdownVisible = ref(false)

// 搜索表单
const searchForm = reactive<{
  name: string
  code: string
  category: string
  belongUser: string
  remark: string
  orgName: string[]
  pageIndex: number
  pageSize: number
}>({
  name: '',
  code: '',
  category: '',
  belongUser: '',
  remark: '',
  orgName: [],
  // 分页参数 - 确保始终有合理的默认值
  pageIndex: 1,
  pageSize: 10
})

// 对话框相关
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit' | 'view'>('add')
const currentSupplierId = ref('') // 添加缺失的变量定义
const activeTab = ref('basic') // 添加缺失的标签页变量定义
const selectedSuppliers = ref<Supplier[]>([]) // 添加缺失的选中供应商列表定义
const supplierForm = reactive<Partial<Supplier>>({
  name: '',
  code: '',
  orgName: [],
  belongUser: '',
  category: '',
  bank: '',
  account: '',
  taxNo: '',
  remark: '',
  应付余额: 0
})

// 批量操作对话框
const batchDialogVisible = ref(false)
const batchActiveTab = ref('import')

// 用户搜索相关
const userList = ref<string[]>(['管理员', '操作员'])
const userSearchQuery = ref('')
const userPageData = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
  totalPages: 0
})

// 表格配置
const tabdatacolumns = ref<MyTableColumn[]>([])
const taboperbtns = ref<MyTableOperationsBtn[]>([])
const tableAttr = reactive({})

// 联系人接口定义
interface SupplierContact {
  id: string
  name: string
  phone: string
  address: string
  remark: string
  isMain: boolean
}

// 联系人列表
const contactList = ref<SupplierContact[]>([
  {
    id: Date.now().toString(),
    name: '',
    phone: '',
    address: '',
    remark: '',
    isMain: true
  }
])

// 联系资料编辑弹窗相关
const contactDialogVisible = ref(false)
const contactDialogType = ref<'add' | 'edit'>('add')
const currentContactIndex = ref(-1)
const contactForm = reactive<Partial<SupplierContact>>({
  name: '',
  phone: '',
  address: '',
  remark: '',
  isMain: false
})

// 联系人表格列配置
const contactTableColumns = ref<MyTableColumn[]>([
  { prop: 'name', label: '联系人员', width: 150 },
  { prop: 'phone', label: '联系号码', width: 150 },
  { prop: 'address', label: '联系地址', width: 200 },
  { prop: 'remark', label: '备注信息', width: 200 },
  {
    prop: 'operate',
    label: '相关操作',
    width: 180,
    fixed: 'right'
  }
])

// 联系人表格操作按钮
const contactTableOperBtns = computed<MyTableOperationsBtn[]>(() => {
  // 检查是否有有效的联系人数据且不是只读模式
  const hasValidContacts = contactList.value.length > 0 && 
    contactList.value.some(contact => contact.name || contact.phone || contact.address);
    
  if (hasValidContacts && dialogType.value !== 'view') {
    return [
      {
        text: '详情',
        attr: {
          size: 'small',
          type: 'primary' as const,
          style: {
            backgroundColor: 'white',
            color: '#409eff',
            borderColor: '#dcdfe6',
            marginRight: '4px'
          }
        },
        evtname: 'detail'
      },
      {
        text: '删除',
        attr: {
          size: 'small',
          type: 'danger' as const,
          style: {
            backgroundColor: 'white',
            color: '#f56c6c',
            borderColor: '#dcdfe6'
          }
        },
        evtname: 'delete'
      }
    ]
  }
  return []
})

// 计算属性
const filteredSuppliers = computed(() => {
  return supplierList.value.filter((supplier) => {
    const nameMatch =
      !searchForm.name || supplier.name.toLowerCase().includes(searchForm.name.toLowerCase())
    // 支持code和number字段，确保兼容后端数据
    const codeOrNumber = supplier.code || supplier.number || ''
    const codeMatch =
      !searchForm.code || codeOrNumber.toLowerCase().includes(searchForm.code.toLowerCase())
    const categoryMatch = !searchForm.category || supplier.category === searchForm.category
    const belongUserMatch =
      !searchForm.belongUser || (supplier.belongUser && supplier.belongUser.includes(searchForm.belongUser))
    const remarkMatch =
      !searchForm.remark || (supplier.remark && supplier.remark.includes(searchForm.remark))
    
    // 处理组织过滤
    const orgNameMatch = !searchForm.orgName || searchForm.orgName.length === 0 || 
      (Array.isArray(searchForm.orgName) && searchForm.orgName.length > 0 && 
       (Array.isArray(supplier.orgName) && supplier.orgName.some(item => searchForm.orgName!.includes(item)) || 
        supplier.orgName === searchForm.orgName[searchForm.orgName.length - 1]))

    return (
      nameMatch &&
      codeMatch &&
      categoryMatch &&
      belongUserMatch &&
      remarkMatch &&
      orgNameMatch
    )
  })
})

// 计算属性 - 过滤后的用户列表
const filteredUserList = computed(() => {
  const filtered = userSearchQuery.value
    ? userList.value.filter(user => 
        user.toLowerCase().includes(userSearchQuery.value.toLowerCase())
      )
    : userList.value
  
  // 更新总数和总页数
  userPageData.total = filtered.length
  userPageData.totalPages = Math.ceil(filtered.length / userPageData.pageSize)
  
  // 确保当前页不超过总页数
  if (userPageData.currentPage > userPageData.totalPages && userPageData.totalPages > 0) {
    userPageData.currentPage = userPageData.totalPages
  }
  
  // 返回当前页的数据
  const startIndex = (userPageData.currentPage - 1) * userPageData.pageSize
  const endIndex = startIndex + userPageData.pageSize
  return filtered.slice(startIndex, endIndex)
})

// 更新表格数据
const updateTableData = () => {
  const filtered = filteredSuppliers.value
  tablePageData.value = {
    ...tablePageData.value,
    rows: filtered.slice(
      (tablePageData.value.pageIndex - 1) * tablePageData.value.pageSize,
      tablePageData.value.pageIndex * tablePageData.value.pageSize
    ),
    total: filtered.length
  }
}

// 生命周期
onMounted(() => {
  // 初始化表格配置
  initTableConfig()
  // 加载供应商列表数据
  updateTableData()
  // 从后端API获取供应商数据
  loadSupplierList()
  // 添加全局点击事件监听
  document.addEventListener('click', handleDocumentClick)
})

onUnmounted(() => {
  // 移除全局点击事件监听
  document.removeEventListener('click', handleDocumentClick)
})

// 事件处理函数
function handleDocumentClick(event: MouseEvent) {
  const target = event.target as HTMLElement
  if (!target.closest('.dropdown-menu')) {
    dropdownVisible.value = false
  }
}

// 批量操作函数
function handleBatch() {
  batchDialogVisible.value = true
  batchActiveTab.value = 'import'
}

// 批量对话框关闭处理
function handleBatchDialogClose() {
  batchDialogVisible.value = false
}

// handleUploadTemplate功能已移至下方定义的函数

// handleExportData功能已移至下方定义的函数

// 下载模板处理
function handleDownloadTemplate() {
  // 这里应该调用后端接口下载模板
  // 暂时使用模拟实现
  ElMessage.info('模板下载功能开发中')
}

// loadSupplierList功能已移至下方定义的函数

// handlePageChange功能已移至下方使用const声明的函数

// handleAdd功能已移至下方定义的函数

// handleSave功能已移至下方定义的函数

// 对话框关闭处理
function handleDialogClose() {
  dialogVisible.value = false
}

// 组织名称变化处理
function handleOrgNameChange(value: CascaderValue | null | undefined) {
  // 处理组织选择变化
  console.log('组织选择:', value)
}

// 刷新数据
async function handleRefresh() {
  await loadSupplierList()
}

// 用户远程搜索
function handleUserRemoteSearch(query: string) {
  userSearchQuery.value = query
  // 这里可以调用后端接口搜索用户
}

// 用户选择框可见性变化
function handleUserVisibleChange(visible: boolean) {
  if (visible) {
    userPageData.currentPage = 1
    userSearchQuery.value = ''
  }
}

// 用户分页变化
function handleUserPageChange(direction: number) {
  userPageData.currentPage += direction
}

// handleTableOperClick功能已移至下方使用const声明的函数

// 查看详情
async function handleViewDetail(row: Supplier) {
  dialogType.value = 'view'
  try {
    const response = await SupplierAPI.getSupplierDetail(row.id)
    if (response.code === 10000 && response.data) {
      Object.assign(supplierForm, response.data)
      // 安全解析联系人数据
      try {
        const contactsData = response.data.contacts
        if (typeof contactsData === 'string' && contactsData.trim()) {
          const parsedContacts = JSON.parse(contactsData)
          contactList.value = Array.isArray(parsedContacts) ? parsedContacts : []
        } else if (Array.isArray(contactsData)) {
          contactList.value = contactsData
        } else {
          contactList.value = []
        }
      } catch (e) {
        console.error('解析联系人数据失败:', e)
        contactList.value = []
      }
    }
  } catch (error) {
    console.error('获取详情失败:', error)
  } finally {
    dialogVisible.value = true
  }
}

// 编辑
async function handleEdit(row: Supplier) {
  dialogType.value = 'edit'
  currentSupplierId.value = row.id // 设置当前编辑的供应商ID
  try {
    const response = await SupplierAPI.getSupplierDetail(row.id)
    if (response.code === 10000 && response.data) {
      Object.assign(supplierForm, response.data)
      // 解析联系人数据
      try {
        contactList.value = JSON.parse(response.data.contacts || '[]')
      } catch (e) {
        contactList.value = []
      }
    }
  } catch (error) {
    console.error('获取详情失败:', error)
  } finally {
    dialogVisible.value = true
  }
}

// handleDelete功能已移至下方定义的函数

// 联系人表格操作处理
function handleContactTableOperClick(data: any) {
  const { row, evtname, index } = data
  
  switch (evtname) {
    case 'detail':
      handleContactDetail(row, index)
      break
    case 'delete':
      handleDeleteContact(index)
      break
    default:
      break
  }
}

// 添加联系人
function handleAddContact() {
  contactDialogType.value = 'add'
  Object.assign(contactForm, {
    name: '',
    phone: '',
    address: '',
    remark: '',
    isMain: contactList.value.length === 0
  })
  currentContactIndex.value = -1
  contactDialogVisible.value = true
}

// 联系人详情
function handleContactDetail(row: SupplierContact, index: number) {
  contactDialogType.value = 'edit'
  Object.assign(contactForm, row)
  currentContactIndex.value = index
  contactDialogVisible.value = true
}

// 删除联系人
function handleDeleteContact(index: number) {
  contactList.value.splice(index, 1)
  // 如果删除的是主联系人，设置第一个联系人为主联系人
  if (contactList.value.length > 0 && !contactList.value.some(c => c.isMain)) {
    contactList.value[0].isMain = true
  }
}

// 保存联系人
function handleSaveContact() {
  if (contactDialogType.value === 'add') {
    // 如果设置为主联系人，将其他联系人的isMain设为false
    if (contactForm.isMain) {
      contactList.value.forEach(c => c.isMain = false)
    }
    contactList.value.push({
      id: Date.now().toString(),
      name: contactForm.name || '',
      phone: contactForm.phone || '',
      address: contactForm.address || '',
      remark: contactForm.remark || '',
      isMain: contactForm.isMain || contactList.value.length === 0
    })
  } else if (contactDialogType.value === 'edit' && currentContactIndex.value >= 0) {
    // 如果设置为主联系人，将其他联系人的isMain设为false
    if (contactForm.isMain) {
      contactList.value.forEach(c => c.isMain = false)
    }
    Object.assign(contactList.value[currentContactIndex.value], contactForm)
  }
  handleContactDialogClose()
}

// 联系人对话框关闭
function handleContactDialogClose() {
  contactDialogVisible.value = false
}

// 主联系人变化处理
function handleMainContactChange(value: string | number | boolean) {
  if (value) {
    // 如果设置为主联系人，将其他联系人的isMain设为false
    contactList.value.forEach(c => c.isMain = false)
  }
}

// 表格选择变化功能已移至下方使用const声明的handleSelectionChange函数

// 初始化表格配置
function initTableConfig() {
  // 初始化表格列配置
  tabdatacolumns.value = [
    {
      type: 'selection' as const,
      width: 55,
      label: '',
      prop: ''
    },
    {
      prop: 'name',
      label: '供应商名称',
      width: 180,
      sortable: true
    },
    {
      prop: 'code',
      label: '供应商编号',
      width: 150,
      sortable: true
    },
    {
      prop: 'category',
      label: '供应商类别',
      width: 120
    },
    {
      prop: 'bank',
      label: '开户银行',
      width: 150
    },
    {
      prop: 'account',
      label: '银行账号',
      width: 200
    },
    {
      prop: 'taxNo',
      label: '纳税号码',
      width: 180
    },
    {
      prop: 'status',
      label: '状态',
      width: 100,
      formatter: (row: any) => {
        return row.status === 1 ? '正常' : '禁用'
      }
    },
    {
      prop: 'operate',
      label: '操作',
      width: 180,
      fixed: 'right'
    }
  ]
  
  // 初始化表格操作按钮
  taboperbtns.value = [
    {
      text: '详情',
      attr: {
        size: 'small',
        type: 'primary',
        style: {
          backgroundColor: 'white',
          color: '#409eff',
          borderColor: '#dcdfe6',
          marginRight: '4px'
        }
      },
      evtname: 'view'
    },
    {
      text: '编辑',
      attr: {
        size: 'small',
        type: 'primary',
        style: {
          backgroundColor: '#ecf5ff',
          color: '#409eff',
          borderColor: '#d9ecff',
          marginRight: '4px'
        }
      },
      evtname: 'edit'
    },
    {
      text: '删除',
      attr: {
        size: 'small',
        type: 'danger',
        style: {
          backgroundColor: 'white',
          color: '#f56c6c',
          borderColor: '#fbc4c4'
        }
      },
      evtname: 'delete'
    }
  ]
  
  // 初始化表格属性
  Object.assign(tableAttr, {
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
}

function handleSearch() {
  tablePageData.value.pageIndex = 1
  dropdownVisible.value = false
  updateTableData()
}

function handleResetSearch() {
  Object.assign(searchForm, {
    name: '',
    code: '',
    category: '',
    belongUser: '',
    remark: '',
    orgName: []
  })
  tablePageData.value.pageIndex = 1
  updateTableData()
}

// 处理选中项变化
const handleSelectionChange = (val: Supplier[]) => {
  selectedSuppliers.value = val
}

// 分页处理函数
const handlePageChange = (data: PageDTO<Supplier>) => {
  tablePageData.value = {
    pageIndex: data.pageIndex,
    pageSize: data.pageSize,
    total: data.total,
    rows: data.rows || []
  }
  updateTableData()
}

// 表格操作处理函数
const handleTableOperClick = (index: number, row: Supplier, evtname: string) => {
  if (evtname === 'view') {
    handleView(row)
  } else if (evtname === 'delete') {
    handleDelete(row)
  }
}

function handleAdd() {
  dialogType.value = 'add'
  currentSupplierId.value = ''
  Object.assign(supplierForm, {
    name: '',
    code: '', // 使用code字段，与supplierForm定义一致
    orgName: ['默认组织'],
    belongUser: '管理员',
    category: '常规类别',
    balance: 0, // 使用balance而不是中文属性名
    bank: '',
    account: '',
    taxNo: '',
    remark: ''
  })
  contactList.value = [
    {
      id: Date.now().toString(),
      name: '',
      phone: '',
      address: '',
      remark: '',
      isMain: true
    }
  ]
  activeTab.value = 'basic'
  dialogVisible.value = true
}

// 更新loadSupplierList函数的错误处理和加载状态
async function loadSupplierList() {
  loading.value = true
  try {
    // 确保searchForm包含必要的分页参数
    const queryParams = {
      ...searchForm,
      pageIndex: searchForm.pageIndex || 1,
      pageSize: searchForm.pageSize || 10
    }
    
    const response = await SupplierAPI.getSupplierList(queryParams)
    
    // 确保响应数据格式正确，注意API返回的是ApiResponse格式，需要通过data属性获取实际数据
    if (response && response.code === 10000 && response.data) {
      // 确保后端返回的data字段是正确的分页结果
      const pageData = response.data
      // 处理可能的字段映射问题，确保字段兼容
      const formattedRows = Array.isArray(pageData.rows) ? pageData.rows.map(item => ({
        ...item,
        // 确保字段映射正确，后端可能返回code而不是number
        number: item.number || item.code || '',
        // 确保balance字段存在，处理可能的中文字段名
        balance: typeof item.balance === 'number' ? item.balance : item['应付余额'] || 0
      })) : []
      
      supplierList.value = formattedRows
      tablePageData.value = {
        rows: formattedRows,
        total: typeof pageData.total === 'number' ? pageData.total : 0,
        pageIndex: queryParams.pageIndex,
        pageSize: queryParams.pageSize
      }
    } else {
      throw new Error(`获取供应商列表失败: ${response?.message || '未知错误'}`)
    }
  } catch (error) {
    console.error('Failed to load supplier list:', error)
    // 根据错误类型给出更具体的错误信息
    if (error instanceof Error) {
      ElMessage.error(`获取供应商列表失败: ${error.message}`)
    } else {
      ElMessage.error('获取供应商列表失败，请稍后重试')
    }
    // 清空列表以避免显示可能的数据
    supplierList.value = []
    tablePageData.value.rows = []
    tablePageData.value.total = 0
  } finally {
    loading.value = false
  }
}

async function handleView(row: Supplier) {
  dialogType.value = 'view'
  currentSupplierId.value = row.id
  contactLoading.value = true
  
  try {
    const response = await SupplierAPI.getSupplierDetail(row.id)
    
    // 验证返回数据
    if (!response || !response.data) {
      throw new Error('获取到的供应商详情数据无效')
    }
    
    const detail = response.data
    
    // 确保orgName是数组格式
    const orgNameValue = Array.isArray((detail as any).orgName) ? (detail as any).orgName : [(detail as any).orgName]
    Object.assign(supplierForm, {
      name: detail.name || '',
      code: detail.number || detail.code || '', // 同时检查number和code字段
      orgName: orgNameValue,
      belongUser: (detail as any).belongUser || '',
      category: detail.category || '常规类别',
      balance: typeof detail.balance === 'number' ? detail.balance : 0,
      bank: detail.bank || '',
      account: detail.account || '',
      taxNo: detail.taxNo || '',
      remark: detail.remark || ''
    })
    
    // 安全解析联系人数据，与handleEdit保持一致的处理逻辑
    try {
      const contactsData = detail.contacts
      if (typeof contactsData === 'string' && contactsData.trim()) {
        const parsedContacts = JSON.parse(contactsData)
        contactList.value = Array.isArray(parsedContacts) ? parsedContacts : []
      } else if (Array.isArray(contactsData)) {
        contactList.value = contactsData
      } else {
        // 没有联系人数据，使用默认值
        contactList.value = [{
          id: Date.now().toString(),
          name: '',
          phone: '',
          address: '',
          remark: '',
          isMain: true
        }]
      }
    } catch (parseError) {
      console.error('解析联系人数据失败:', parseError)
      // 使用默认联系人数据
      contactList.value = [{
        id: Date.now().toString(),
        name: '',
        phone: '',
        address: '',
        remark: '',
        isMain: true
      }]
      ElMessage.warning('联系人数据解析失败，已使用默认数据')
    }
  } catch (error) {
    console.error('Failed to load supplier detail:', error)
    if (error instanceof Error) {
      ElMessage.error(`获取供应商详情失败: ${error.message}`)
    } else {
      ElMessage.error('获取供应商详情失败')
    }
    // 重置表单以避免显示可能的错误数据
    Object.assign(supplierForm, {
      name: '',
      number: '',
      orgName: ['默认组织'],
      belongUser: '管理员',
      category: '常规类别',
      balance: 0,
      bank: '',
      account: '',
      taxNo: '',
      remark: ''
    })
    contactList.value = [{
      id: Date.now().toString(),
      name: '',
      phone: '',
      address: '',
      remark: '',
      isMain: true
    }]
  } finally {
    contactLoading.value = false
  }
  
  activeTab.value = 'basic'
  dialogVisible.value = true
}

function handleDelete(row: Supplier) {
  ElMessageBox.confirm(`确定要删除供应商「${row.name}」吗？`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      deleteLoading.value = true
      try {
        await SupplierAPI.deleteSupplier([row.id]) // 确保传递数组格式，防止Map_base::at错误
        ElMessage.success('删除成功')
        await loadSupplierList()
      } catch (error) {
        console.error('Failed to delete supplier:', error)
        ElMessage.error('删除失败，请稍后重试')
      } finally {
        deleteLoading.value = false
      }
    })
    .catch(() => {
      // 取消删除
    })
}

async function handleSave() {
  // 表单验证
  if (!supplierForm.name) {
    ElMessage.error('请输入供应商名称')
    return
  }
  if (!supplierForm.code) {
    ElMessage.error('请输入供应商编号')
    return
  }
  if (!supplierForm.orgName || (Array.isArray(supplierForm.orgName) && supplierForm.orgName.length === 0)) {
    ElMessage.error('请选择所属组织')
    return
  }
  if (!supplierForm.belongUser) {
    ElMessage.error('请选择所属用户')
    return
  }

  saveLoading.value = true
  try {
    // 确保数据格式正确，符合后端C++要求，特别是防止JSON解析错误
    // 安全地处理联系人数据的JSON字符串化
    let contactsJson = ''
    try {
      // 确保contactList.value是数组类型
      const contactsArray = Array.isArray(contactList.value) ? contactList.value : []
      
      // 过滤掉空的联系人记录，只保留有实际内容的联系人
      const validContacts = contactsArray.filter(contact => {
        // 确保contact是对象类型
        if (!contact || typeof contact !== 'object') return false
        return contact.name || contact.phone || contact.address || contact.remark
      })
      
      // 确保只有一个主联系人
      const mainContactIndex = validContacts.findIndex(contact => contact.isMain)
      if (mainContactIndex > 0) {
        // 如果有多个主联系人，只保留第一个
        for (let i = 1; i < validContacts.length; i++) {
          validContacts[i].isMain = false
        }
      } else if (validContacts.length > 0 && mainContactIndex === -1) {
        // 如果没有主联系人但有联系人，将第一个设为主联系人
        validContacts[0].isMain = true
      }
      
      // 确保JSON字符串不会过大，限制联系人数量
      const safeContacts = validContacts.slice(0, 50) // 限制最多50个联系人
      
      contactsJson = JSON.stringify(safeContacts)
      
      // 验证生成的JSON字符串
      try {
        JSON.parse(contactsJson) // 验证JSON格式是否正确
      } catch (parseError) {
        throw new Error('生成的联系人JSON格式无效')
      }
    } catch (jsonError) {
      console.error('联系人数据JSON序列化失败:', jsonError)
      ElMessage.error('联系人数据格式错误，请检查联系人信息')
      return
    }
    
    // 构建请求数据，确保类型正确，严格按照AddSupplierRequest接口定义
    // 避免使用中文属性名，防止后端C++服务Map_base::at错误
    // 只包含接口中定义的字段，避免传递多余字段导致C++后端错误
    const requestData: AddSupplierRequest = {
      name: String(supplierForm.name),
      contacts: contactsJson, // 严格按照接口定义使用contacts字段
      address: '',
      bank: supplierForm.bank || '',
      account: supplierForm.account || '',
      taxNo: supplierForm.taxNo || '',
      type: '一般供应商',
      category: supplierForm.category || '常规类别',
      status: 1,
      remark: supplierForm.remark || ''
    }
    
    // 严格按照AddSupplierRequest接口定义使用requestData，不添加任何额外字段
    // 避免C++后端出现"Unknown field"错误
    
    if (dialogType.value === 'add') {
      await SupplierAPI.addSupplier(requestData)
      ElMessage.success('新增成功')
    } else if (dialogType.value === 'edit' && currentSupplierId.value) {
      // 更新时使用currentSupplierId确保有正确的id值
      const updateData = { ...requestData, id: currentSupplierId.value }
      await SupplierAPI.updateSupplier(currentSupplierId.value, updateData)
      ElMessage.success('更新成功')
    }
    
    await loadSupplierList()
    dialogVisible.value = false
  } catch (error) {
    console.error('保存失败:', error)
    if (error instanceof Error) {
      // 针对可能的特定错误类型提供更具体的提示
      if (error.message.includes('JSON')) {
        ElMessage.error('数据格式错误，请检查输入内容')
      } else {
        ElMessage.error(`保存失败: ${error.message}`)
      }
    } else {
      ElMessage.error('保存失败，请稍后重试')
    }
  } finally {
    saveLoading.value = false
  }
}

function handleUploadTemplate() {
  const fileInput = document.createElement('input')
  fileInput.type = 'file'
  fileInput.accept = '.xlsx,.xls'
  fileInput.onchange = async (event) => {
    const files = (event.target as HTMLInputElement).files
    if (files && files.length > 0) {
      const selectedFile = files[0]
      ElMessage.info(`正在导入文件: ${selectedFile.name}`)
      
      importLoading.value = true
      try {
        const formData = new FormData()
        formData.append('file', selectedFile)
        
        await SupplierAPI.importSupplier(formData)
        ElMessage.success('文件导入成功')
        await loadSupplierList()
        batchDialogVisible.value = false
      } catch (error) {
        console.error('Failed to import suppliers:', error)
        ElMessage.error('文件导入失败，请检查文件格式是否正确')
      } finally {
        importLoading.value = false
      }
    }
  }
  fileInput.click()
}

async function handleExportData() {
  if (selectedSuppliers.value.length === 0) {
    ElMessage.warning('请先选择要导出的供应商')
    return
  }
  
  exportLoading.value = true
  try {
    ElMessage.info(`正在导出${selectedSuppliers.value.length}条数据...`)
    
    // 调用导出接口 - 使用搜索表单的值构建导出查询参数
    const exportParams: SupplierExportQuery = {
      code: searchForm.code || undefined,
      name: searchForm.name || undefined,
      category: searchForm.category || undefined
    }
    const response = await SupplierAPI.exportSupplier(exportParams)
    
    // 验证响应
    if (!response) {
      throw new Error('导出响应为空')
    }
    
    // 处理文件下载
    const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `供应商数据_${new Date().toLocaleDateString()}.xlsx`
    document.body.appendChild(link)
    link.click()
    
    // 清理资源
    setTimeout(() => {
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    }, 100)
    
    ElMessage.success('数据导出成功')
    batchDialogVisible.value = false
  } catch (error) {
    console.error('Failed to export suppliers:', error)
    ElMessage.error('数据导出失败，请稍后重试')
  } finally {
    exportLoading.value = false
  }
}

</script>

<style scoped>
/* 保留原有样式内容 */
.sys-area {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  background-color: white;
  border-radius: 8px;
  padding: 16px;
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
  margin-left: 8px;
}

.dropdown-menu {
  position: relative;
}

.dropdown-button {
  background: none;
  border: 1px solid #dcdfe6;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.dots-menu {
  font-size: 18px;
  letter-spacing: 2px;
}

.dropdown-content {
  position: absolute;
  top: 100%;
  left: 0;
  margin-top: 5px;
  background-color: white;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 1000;
  min-width: 500px;
}

.search-form {
  padding: 16px;
}

.form-row {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  align-items: center;
}

.form-input {
  width: 200px;
}

.form-select {
  width: 200px;
}

.search-button-row {
  justify-content: center;
  margin-top: 20px;
}

.custom-divider {
  margin: 16px 0;
}

.main-content {
  background-color: white;
  border-radius: 8px;
  padding: 16px;
}

.table-section {
  width: 100%;
}

.table-container {
  width: 100%;
  overflow-x: auto;
}

/* 对话框样式 */
.form-content {
  padding: 10px 0;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-item label {
  font-weight: 500;
}

.required::before {
  content: '*';
  color: #f56c6c;
  margin-right: 4px;
}

/* 联系资料样式 */
.contact-section {
  margin-bottom: 20px;
}

.section-header {
  font-weight: 600;
  margin-bottom: 10px;
}

.contact-table-container {
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

.no-data {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}

/* 联系表单样式 */
.contact-form-content {
  padding: 10px 0;
}

.contact-form-content .form-item {
  margin-bottom: 20px;
}

/* 批量操作对话框样式 */
.batch-tabs .el-tabs__header {
  margin-bottom: 20px;
}

.import-content {
  padding: 20px;
}

.import-instructions {
  margin-bottom: 20px;
  padding-left: 20px;
}

.import-instructions li {
  margin-bottom: 10px;
  color: #606266;
}

.import-actions {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin-top: 20px;
}

.export-content {
  padding: 20px;
  text-align: center;
  cursor: pointer;
}

.export-button-container {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 10px 20px;
  border: 1px solid #409eff;
  border-radius: 4px;
  color: #409eff;
  transition: all 0.3s;
}

.export-button-container:hover {
  background-color: #409eff;
  color: white;
}

.export-icon {
  font-size: 20px;
}

.export-text {
  font-size: 16px;
  font-weight: 500;
}

/* 用户选择器样式 */
.user-select {
  width: 100%;
}

.user-select-popper {
  width: 400px;
}

.search-tip {
  padding: 10px;
  background-color: #f5f7fa;
  text-align: center;
  font-size: 14px;
  color: #606266;
  border-bottom: 1px solid #ebeef5;
}

.user-select-item {
  padding: 8px 16px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.user-select-item:hover {
  background-color: #f5f7fa;
}

.pagination-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 10px;
  border-top: 1px solid #ebeef5;
}

.pagination-info {
  font-size: 14px;
  color: #606266;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .operation-bar {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }
  
  .operation-left,
  .operation-right {
    justify-content: center;
  }
  
  .dropdown-content {
    min-width: 100%;
    left: 50%;
    transform: translateX(-50%);
  }
}

/* 表格样式调整 */
:deep(.el-table) {
  .el-table__header-wrapper,
  .el-table__body-wrapper {
    overflow-x: hidden;
  }
  
  .el-table__header,
  .el-table__body {
    table-layout: auto;
  }
}
</style>
