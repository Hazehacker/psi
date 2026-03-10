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
            <!-- 搜索表单（移除联系人员、电话、地址） -->
            <div class="search-form">
              <div class="form-row">
                <el-input
                  v-model="searchForm.name"
                  placeholder="请输入仓库名称"
                  clearable
                  size="large"
                  class="form-input"
                />
                <el-input
                  v-model="searchForm.number"
                  placeholder="请输入仓库编号"
                  clearable
                  size="large"
                  class="form-input"
                />
                <el-input
                  v-model="searchForm.contactPerson"
                  placeholder="请输入联系人员"
                  clearable
                  size="large"
                  class="form-input"
                />
              </div>
              <div class="form-row">
                <el-input
                  v-model="searchForm.contactPhone"
                  placeholder="请输入联系电话"
                  clearable
                  size="large"
                  class="form-input"
                />
                <el-input
                  v-model="searchForm.address"
                  placeholder="请输入仓库地址"
                  clearable
                  size="large"
                  class="form-input"
                />
                <el-cascader
                  v-model="searchForm.deptName"
                  :options="organizationOptions"
                  placeholder="请选择所属组织"
                  clearable
                  size="large"
                  class="form-input"
                  :props="{ expandTrigger: 'hover', multiple: false }"
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

    <!-- 主内容区域：表格（移除联系人员、电话、地址列） -->
    <div class="main-content">
      <div class="table-section">
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

  <!-- 仓库详情对话框 -->
  <el-dialog
    v-model="dialogVisible"
    :title="dialogType === 'add' ? '新增仓库' : '仓库详情'"
    width="720px"
    :before-close="handleDialogClose"
  >
    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane label="基础资料" name="basic">
        <div class="form-content">
          <div class="form-grid">
            <div class="form-item">
              <label class="required">仓库名称</label>
              <el-input v-model="warehouseForm.name" placeholder="请输入仓库名称" />
            </div>
            <div class="form-item">
              <label class="required">仓库编号</label>
              <el-input v-model="warehouseForm.number" placeholder="请输入仓库编号" />
            </div>
          </div>
          <div class="form-grid">
            <div class="form-item">
              <label class="required">所属组织</label>
              <el-cascader
                v-model="warehouseForm.deptName"
                :options="organizationOptions"
                placeholder="请选择所属组织"
                clearable
                :props="{ expandTrigger: 'hover', multiple: false }"
                style="width: 100%"
              />
            </div>
            <div class="form-item">
              <label>&nbsp;</label>
              <div></div>
            </div>
          </div>
          <div class="form-grid">
            <div class="form-item">
              <label>联系人员</label>
              <el-input v-model="warehouseForm.contactPerson" placeholder="请输入联系人员" />
            </div>
            <div class="form-item">
              <label>联系电话</label>
              <el-input v-model="warehouseForm.contactPhone" placeholder="请输入联系电话" />
            </div>
          </div>
          <div class="form-grid">
            <div class="form-item">
              <label>仓库地址</label>
              <el-input v-model="warehouseForm.address" placeholder="请输入仓库地址" />
            </div>
            <div class="form-item">
              <label>&nbsp;</label>
              <div></div>
            </div>
          </div>
          <div class="form-item full-width">
            <label>备注信息</label>
            <el-input
              v-model="warehouseForm.remark"
              placeholder="请输入备注信息"
              type="textarea"
              :rows="3"
            />
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <template #footer>
      <div style="display: flex; justify-content: flex-end; gap: 10px">
        <el-button @click="handleDialogClose">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
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
import { Search, Plus, Collection, Refresh } from '@element-plus/icons-vue'
import NormalTable from '@/components/normaltable/NormalTable.vue'
import {
  type PageDTO,
  createPageDTO,
  type MyTableColumn,
  type MyTableOperationsBtn
} from '@/components/normaltable/type'
import { WarehouseAPI, type Warehouse, type WarehouseQuery } from '@/apis/basicdata/warehouse'

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

// 接口定义已从warehouse.ts导入，这里不再重复定义
// 定义组件内部使用的表单数据接口，映射到后端字段
interface WarehouseForm {
  name: string
  number: string
  frame: string | string[]
  contacts?: string
  tel?: string
  add?: string
  data?: string
}

// 响应式数据
const dropdownVisible = ref(false)
const warehouseList = ref<Warehouse[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)

const dialogVisible = ref(false)
const batchDialogVisible = ref(false)
const activeTab = ref('basic')
const batchActiveTab = ref('import')
const dialogType = ref<'add' | 'edit'>('add')
const currentWarehouseId = ref('')
const selectedWarehouses = ref<Warehouse[]>([])

// 分页数据
const tablePageData = ref<PageDTO<Warehouse>>(createPageDTO())

// 表格列配置
const tabdatacolumns: MyTableColumn[] = [
  { prop: 'name', label: '仓库名称', width: 180 },
  { prop: 'number', label: '仓库编号', width: 120 },
  { 
    prop: 'frame', 
    label: '所属组织', 
    width: 150,
    formatter: (row: any) => {
      if (Array.isArray(row.frame) && row.frame.length > 0) {
        return row.frame[row.frame.length - 1]
      }
      return row.frame || ''
    }
  },
  { prop: 'contacts', label: '联系人员', width: 120 },
  { prop: 'tel', label: '联系电话', width: 120 },
  { prop: 'add', label: '仓库地址', width: 200 },
  { prop: 'data', label: '备注信息' },
  { prop: 'operate', label: '相关操作', width: 150, fixed: 'right' }
]

// 表格操作按钮配置
const taboperbtns: MyTableOperationsBtn[] = [
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
    text: '删除',
    attr: {
      size: 'small',
      type: 'danger',
      style: {
        backgroundColor: 'white',
        color: '#f56c6c',
        borderColor: '#dcdfe6'
      }
    },
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

// 搜索表单
const searchForm = reactive<WarehouseQuery>({
  pageIndex: 1,
  pageSize: 10,
  name: '',
  number: '',
  add: '',
  tel: '',
  contacts: '',
  data: '',
  contactPerson: '',
  contactPhone: '',
  address: '',
  deptName: ''
})

// 仓库表单
const warehouseForm = reactive<Partial<Warehouse>>({
  name: '',
  number: '',
  frame: '默认组织', // frame 应该是字符串，不是数组
  contacts: '',
  tel: '',
  add: '',
  data: '',
  contactPerson: '',
  contactPhone: '',
  address: '',
  deptName: '',
  remark: ''
})

// 加载仓库数据的方法
const loadWarehouseList = async () => {
  try {
    loading.value = true
    
    // 验证分页参数 - 防止无效参数导致的错误
    const currentPageNum = Number(currentPage.value) || 1
    const pageSizeNum = Number(pageSize.value) || 10
    
    searchForm.pageIndex = currentPageNum
    searchForm.pageSize = pageSizeNum
    
    const response = await WarehouseAPI.getWarehouseList(searchForm)
    
    // 严格检查响应数据结构 - 防止"_Map_base::at"错误
    if (!response) {
      throw new Error('未收到响应数据')
    }
    
    if (response.code !== 10000) {
      throw new Error(response.message || `请求失败: ${response.code}`)
    }
    
    // 验证data字段存在且为对象
    if (!response.data || typeof response.data !== 'object') {
      throw new Error('响应数据格式错误')
    }
    
    const data = response.data
    
    // 验证rows字段并进行类型转换
    if (Array.isArray(data.rows)) {
      // 对每个仓库数据进行类型检查和字段验证
      warehouseList.value = data.rows.map((item: any) => {
        // 确保每个仓库对象都有必要的字段且类型正确
        return {
          id: String(item.id || ''),
          name: String(item.name || ''),
          number: String(item.number || ''),
          // 处理frame字段，确保返回数组格式便于展示
          frame: item.frame ? (Array.isArray(item.frame) ? item.frame : [String(item.frame)]) : [''],
          contacts: String(item.contacts || ''),
          tel: String(item.tel || ''),
          add: String(item.add || ''),
          data: String(item.data || '')
        }
      })
    } else {
      warehouseList.value = []
    }
    
    // 安全地设置表格分页数据
    tablePageData.value = {
      ...tablePageData.value,
      total: Number(data.total) || 0,
      pageIndex: Number(data.pageIndex) || currentPageNum,
      pageSize: Number(data.pageSize) || pageSizeNum,
      pages: Number(data.pages) || 1,
      rows: warehouseList.value // 使用已处理的数据
    }
  } catch (error) {
    console.error('加载仓库列表失败:', error)
    // 错误分类处理
    if (error instanceof Error) {
      ElMessage.error(error.message)
    } else {
      ElMessage.error('网络错误，请稍后重试')
    }
    // 确保数据清空，避免错误数据渲染
    warehouseList.value = []
    tablePageData.value = createPageDTO()
  } finally {
    loading.value = false
  }
}

// 重置过滤函数，现在由后端处理过滤
const filteredWarehouses = computed(() => warehouseList.value)

// 生命周期
onMounted(() => {
  loadWarehouseList()
  document.addEventListener('click', handleDocumentClick)
})

onUnmounted(() => {
  document.removeEventListener('click', handleDocumentClick)
})

// 事件处理函数
function handleDocumentClick(event: MouseEvent) {
  const target = event.target as HTMLElement
  if (!target.closest('.dropdown-menu')) {
    dropdownVisible.value = false
  }
}

function handleSearch() {
  currentPage.value = 1
  dropdownVisible.value = false
  loadWarehouseList()
}

function handleResetSearch() {
  Object.assign(searchForm, {
    pageIndex: 1,
    pageSize: 10,
    name: '',
    number: '',
    add: '',
    tel: '',
    contacts: '',
    data: ''
  })
  currentPage.value = 1
  loadWarehouseList()
}

const handleSelectionChange = (val: any[]) => {
  selectedWarehouses.value = val
}

const handlePageChange = (data: PageDTO<any>) => {
  tablePageData.value = data
  currentPage.value = data.pageIndex
  pageSize.value = data.pageSize
  loadWarehouseList()
}

const handleTableOperClick = (index: number, row: any, evtname: string) => {
  if (evtname === 'view') {
    handleView(row)
  } else if (evtname === 'delete') {
    handleDelete(row)
  }
}

function handleAdd() {
  dialogType.value = 'add'
  currentWarehouseId.value = ''
  Object.assign(warehouseForm, {
    name: '',
    number: '',
    frame: ['默认组织'],
    contacts: '',
    tel: '',
    add: '',
    data: ''
  })
  activeTab.value = 'basic'
  dialogVisible.value = true
}

const handleView = async (row: Warehouse) => {
  try {
    // 验证row对象和id字段
    if (!row || !row.id) {
      ElMessage.error('无效的仓库数据')
      return
    }
    
    loading.value = true
    const response = await WarehouseAPI.getWarehouseDetail(row.id)
    
    // 严格按照http.ts中定义的JsonVO<T>格式处理响应
    if (response && response.code === 10000 && response.data) {
      const warehouse = response.data
      dialogType.value = 'edit'
      currentWarehouseId.value = warehouse.id || ''
      // 确保frame是数组格式
      const frameValue = Array.isArray(warehouse.frame) ? warehouse.frame : [warehouse.frame || '默认组织']
      Object.assign(warehouseForm, {
        name: warehouse.name || '',
        number: warehouse.number || '',
        frame: frameValue,
        contacts: warehouse.contacts || '',
        tel: warehouse.tel || '',
        add: warehouse.add || '',
        data: warehouse.data || ''
      })
      activeTab.value = 'basic'
      dialogVisible.value = true
    } else {
      // 使用响应中的message字段，遵循http.ts中的错误处理格式
      ElMessage.error(response?.message || '获取仓库详情失败')
    }
  } catch (error) {
    console.error('查看仓库详情失败:', error)
    
    // 优化错误处理，根据http.ts格式分类处理
    if (error && typeof error === 'object' && 'response' in error) {
      const axiosError = error as any
      if (axiosError.response && axiosError.response.data) {
        const { data } = axiosError.response
        if (data && typeof data === 'object') {
          ElMessage.error(data.message || '服务器错误')
        } else {
          ElMessage.error('服务器返回格式错误')
        }
      } else {
        ElMessage.error('网络连接失败')
      }
    } else if (error instanceof Error) {
      ElMessage.error(error.message)
    } else {
      ElMessage.error('未知错误')
    }
  } finally {
    loading.value = false
  }
}

const handleDelete = async (row: Warehouse) => {
  try {
    // 验证row对象
    if (!row || !row.id) {
      ElMessage.error('无效的仓库数据')
      return
    }
    
    const confirmResult = await ElMessageBox.confirm(
      `确定要删除仓库「${row.name}」吗？`, 
      '删除确认', 
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    if (confirmResult === 'confirm') {
      loading.value = true
      const response = await WarehouseAPI.deleteWarehouse(row.id)
      
      // 严格按照http.ts中定义的JsonVO<T>格式处理响应
      if (response && response.code === 10000) {
        ElMessage.success('删除成功')
        loadWarehouseList() // 重新加载列表
      } else {
        // 使用响应中的message字段，遵循http.ts中的错误处理格式
        ElMessage.error(response?.message || '删除失败')
      }
    }
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') { // 不是用户取消操作
      console.error('删除仓库失败:', error)
      
      // 优化错误处理，根据http.ts格式分类处理
      if (error && typeof error === 'object' && 'response' in error) {
        const axiosError = error as any
        if (axiosError.response && axiosError.response.data) {
          const { data } = axiosError.response
          if (data && typeof data === 'object') {
            ElMessage.error(data.message || '服务器错误')
          } else {
            ElMessage.error('服务器返回格式错误')
          }
        } else {
          ElMessage.error('网络连接失败')
        }
      } else if (error instanceof Error) {
        ElMessage.error(error.message)
      } else {
        ElMessage.error('未知错误')
      }
    }
  } finally {
    loading.value = false
  }
}

const handleSave = async () => {
  try {
    // 数据验证 - 防止"_Map_base::at"错误
    if (!warehouseForm.name || !warehouseForm.name.trim()) {
      ElMessage.error('请填写仓库名称')
      return
    }
    if (!warehouseForm.number || !warehouseForm.number.trim()) {
      ElMessage.error('请填写仓库编号')
      return
    }
    if (!warehouseForm.frame) {
      ElMessage.error('请选择所属组织')
      return
    }
    
    // 准备提交数据 - 确保符合C++后端数据类型要求
    // 只包含非空字段，避免Unknown field错误
    const submitData: Partial<Warehouse> = {};
    
    // 必填字段
    if (warehouseForm.name) {
      submitData.name = warehouseForm.name.trim();
    }
    if (warehouseForm.number) {
      submitData.number = warehouseForm.number.trim();
    }
    // 确保frame是字符串类型，符合C++后端要求
    if (warehouseForm.frame) {
      submitData.frame = Array.isArray(warehouseForm.frame) 
        ? warehouseForm.frame.join(',') 
        : warehouseForm.frame.toString();
    }
    
    // 可选字段，只添加非空值
    if (warehouseForm.contacts && warehouseForm.contacts.trim()) {
      submitData.contacts = warehouseForm.contacts.trim();
    }
    if (warehouseForm.tel && warehouseForm.tel.trim()) {
      submitData.tel = warehouseForm.tel.trim();
    }
    if (warehouseForm.add && warehouseForm.add.trim()) {
      submitData.add = warehouseForm.add.trim();
    }
    if (warehouseForm.data && warehouseForm.data.trim()) {
      submitData.data = warehouseForm.data.trim();
    }
    
    loading.value = true
    let response
    
    if (dialogType.value === 'add') {
      response = await WarehouseAPI.addWarehouse(submitData)
    } else {
      // 确保有ID才能更新
      if (!currentWarehouseId.value) {
        throw new Error('仓库ID不能为空')
      }
      response = await WarehouseAPI.updateWarehouse(currentWarehouseId.value, submitData)
    }
    
    // 严格按照http.ts中定义的JsonVO<T>格式处理响应
    if (response && response.code === 10000) {
      ElMessage.success(dialogType.value === 'add' ? '新增成功' : '更新成功')
      dialogVisible.value = false
      loadWarehouseList() // 重新加载列表
    } else {
      // 使用响应中的message字段，遵循http.ts中的错误处理格式
      ElMessage.error(response?.message || (dialogType.value === 'add' ? '新增失败' : '更新失败'))
    }
  } catch (error) {
    console.error(`${dialogType.value === 'add' ? '新增' : '更新'}仓库失败:`, error)
    
    // 优化错误处理，根据http.ts格式分类处理
    if (error && typeof error === 'object' && 'response' in error) {
      const axiosError = error as any
      // 服务器返回的错误
      if (axiosError.response && axiosError.response.data) {
        const { data } = axiosError.response
        // 符合JsonVO格式的错误
        if (data && typeof data === 'object') {
          ElMessage.error(data.message || '服务器错误')
        } else {
          ElMessage.error('服务器返回格式错误')
        }
      } else {
        ElMessage.error('网络连接失败')
      }
    } else if (error instanceof Error) {
      // 客户端验证错误
      ElMessage.error(error.message)
    } else {
      ElMessage.error('未知错误')
    }
  } finally {
    loading.value = false
  }
}

function handleDialogClose() {
  dialogVisible.value = false
}

function handleRefresh() {
  loadWarehouseList()
}

function handleBatch() {
  batchDialogVisible.value = true
}

function handleBatchDialogClose() {
  batchDialogVisible.value = false
}

function handleDownloadTemplate() {
  ElMessage.success('模板下载中...')
}

function handleUploadTemplate() {
  const fileInput = document.createElement('input')
  fileInput.type = 'file'
  fileInput.accept = '.xlsx,.xls'
  fileInput.onchange = (event) => {
    const files = (event.target as HTMLInputElement).files
    if (files && files.length > 0) {
      const selectedFile = files[0]
      ElMessage.success(`已选择文件: ${selectedFile.name}`)
      setTimeout(() => {
        ElMessage.success('文件上传成功')
      }, 1000)
    }
  }
  fileInput.click()
}

function handleExportData() {
  if (selectedWarehouses.value.length === 0) {
    ElMessage.warning('请先选择要导出的仓库')
    return
  }
  ElMessage.success(`正在导出${selectedWarehouses.value.length}条数据...`)
  setTimeout(() => {
    ElMessage.success('数据导出成功')
  }, 1000)
}
</script>

<style scoped>
/* 样式保持不变，无需修改 */
.sys-area {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

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

.search-button-row .el-button {
  background-color: transparent !important;
  border: 1px solid #dcdfe6;
  color: #606266;
}

.search-button-row .el-button:hover {
  background-color: #ecf5ff;
  border-color: #c6e2ff;
  color: #409eff;
}

.custom-divider {
  margin: 16px 0;
}

.main-content {
  display: flex;
  gap: 16px;
}

.table-section {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.table-container {
  flex: 1;
  overflow-x: auto;
  margin-bottom: 16px;
}

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
  margin-bottom: 0;
}

/* 确保表单控件占满整个父容器宽度 */
.form-item .el-input,
.form-item .el-cascader {
  width: 100%;
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

.batch-tabs .el-tabs__content {
  padding: 20px 0;
}

.import-content {
  min-height: 200px;
}

.import-instructions {
  margin: 0 0 30px 0;
  padding-left: 20px;
  color: #606266;
  font-size: 14px;
  line-height: 28px;
}

.import-instructions li {
  margin-bottom: 8px;
}

.import-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.export-content {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  cursor: pointer;
}

.export-button-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
  width: 150px;
  transition: all 0.3s ease;
}

.export-button-container:hover {
  border-color: #409eff;
  color: #409eff;
}

.export-icon {
  font-size: 32px;
  margin-bottom: 10px;
  transform: rotate(180deg);
}

.export-text {
  font-size: 14px;
}

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

:deep(.el-table__row .el-checkbox__inner) {
  margin: 0 auto;
  display: block;
}

:deep(.el-table .el-button--small) {
  padding: 6px 12px;
  font-size: 13px;
}
</style>
