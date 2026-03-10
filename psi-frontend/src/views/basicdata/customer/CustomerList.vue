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
                  placeholder="请输入客户名称"
                  clearable
                  size="large"
                  class="form-input"
                />
                <el-input
                  v-model="searchForm.code"
                  placeholder="请输入客户编号"
                  clearable
                  size="large"
                  class="form-input"
                />
              </div>
              <div class="form-row">
                <el-select
                  v-model="searchForm.category"
                  placeholder="请选择客户类别"
                  clearable
                  size="large"
                  class="form-select"
                >
                  <el-option label="常规类别" value="常规类别" />
                  <el-option label="特殊类别" value="特殊类别" />
                </el-select>
                <el-select
                  v-model="searchForm.level"
                  placeholder="请选择客户等级"
                  clearable
                  size="large"
                  class="form-select"
                >
                  <el-option label="常规等级" value="常规等级" />
                  <el-option label="VIP等级" value="VIP等级" />
                </el-select>
              </div>
              <div class="form-row">
                <el-input
                  v-model="searchForm.contactPerson"
                  placeholder="请输入联系人"
                  clearable
                  size="large"
                  class="form-input"
                />
                <el-input
                  v-model="searchForm.phone"
                  placeholder="请输入联系电话"
                  clearable
                  size="large"
                  class="form-input"
                />
              </div>
              <div class="form-row">
                <el-cascader
                  v-model="searchForm.ownerUser"
                  :options="userTreeData"
                  placeholder="请选择所属用户"
                  clearable
                  size="large"
                  class="form-select"
                  :props="{
                    multiple: false,
                    checkStrictly: true,
                    label: 'label',
                    value: 'value'
                  }"
                />
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

  <!-- 客户编辑对话框 -->
  <el-dialog
    v-model="dialogVisible"
    :title="dialogType === 'add' ? '新增客户' : '客户详情'"
    width="800px"
    :before-close="handleDialogClose"
  >
    <el-tabs v-model="activeTab" type="card">
      <!-- 基础信息标签页 -->
      <el-tab-pane label="基础资料" name="basic">
        <div class="form-content">
          <div class="form-grid">
            <div class="form-item">
              <label class="required">客户名称</label>
              <el-input v-model="customerForm.name" placeholder="请输入客户名称" />
            </div>
            <div class="form-item">
              <label class="required">客户编号</label>
              <el-input v-model="customerForm.code" placeholder="请输入客户编号" />
            </div>
          </div>
          <div class="form-grid">
            <div class="form-item">
              <label class="required">所属组织</label>
              <el-cascader
                v-model="customerForm.organization"
                :options="organizationTreeData"
                placeholder="请选择所属组织"
                class="form-select"
                :props="{
                  multiple: false,
                  checkStrictly: true,
                  label: 'label',
                  value: 'value'
                }"
              />
            </div>
            <div class="form-item">
              <label class="required">所属用户</label>
              <el-cascader
                v-model="customerForm.ownerUser"
                :options="userTreeData"
                placeholder="请选择所属用户"
                class="form-select"
                :props="{
                  multiple: false,
                  checkStrictly: true,
                  label: 'label',
                  value: 'value'
                }"
              />
            </div>
          </div>
          <div class="form-grid">
            <div class="form-item">
              <label>客户类别</label>
              <el-select v-model="customerForm.category" placeholder="请选择客户类别">
                <el-option label="常规类别" value="常规类别" />
                <el-option label="特殊类别" value="特殊类别" />
              </el-select>
            </div>
            <div class="form-item">
              <label>客户等级</label>
              <el-select v-model="customerForm.level" placeholder="请选择客户等级">
                <el-option label="常规等级" value="常规等级" />
                <el-option label="VIP等级" value="VIP等级" />
              </el-select>
            </div>
          </div>
          <div class="form-grid">
            <div class="form-item">
              <label>开户银行</label>
              <el-input v-model="customerForm.bank" placeholder="请输入开户银行" />
            </div>
            <div class="form-item">
              <label>银行账号</label>
              <el-input v-model="customerForm.bankAccount" placeholder="请输入银行账号" />
            </div>
          </div>
          <div class="form-grid">
            <div class="form-item">
              <label>纳税号码</label>
              <el-input v-model="customerForm.taxNumber" placeholder="请输入纳税人识别号" />
            </div>
            <div class="form-item">
              <label>备注信息</label>
              <el-input
                v-model="customerForm.remark"
                placeholder="请输入备注信息"
                type="textarea"
                :rows="2"
              />
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
                  <el-button size="small" type="primary" icon="Plus" @click="handleAddContact">
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
        <el-button type="primary" @click="handleSave">保存</el-button>
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
import { ref, reactive, computed, onMounted, onUnmounted, h } from 'vue'
import { ElMessage, ElMessageBox, ElNotification } from 'element-plus'
import type { ElForm } from 'element-plus'
import { Search, Plus, Collection, Refresh } from '@element-plus/icons-vue'
import NormalTable from '@/components/normaltable/NormalTable.vue'
import {
  type PageDTO,
  createPageDTO,
  type MyTableColumn,
  type MyTableOperationsBtn
} from '@/components/normaltable/type'
import { CustomerAPI, type CustomerQuery } from '@/apis/basicdata/customer'

// 接口定义 - 本地使用的Customer类型（与API返回的Customer不同）
interface Customer {
  id: string
  name: string
  code: string
  category: string
  level: string
  organization: string[]
  ownerUser: string[]
  balance?: number
  bank?: string
  bankAccount?: string
  taxNumber?: string
  remark?: string
}

interface Contact {
  id: string
  name: string
  phone: string
  email?: string
  address: string
  remark?: string
  isMain: boolean
}

// 响应式数据
const dropdownVisible = ref(false)
const organizationTreeData = ref([
  {
    value: '默认组织',
    label: '默认组织',
    children: [
      { value: '销售部', label: '销售部' },
      { value: '市场部', label: '市场部' },
      { value: '技术部', label: '技术部' }
    ]
  },
  {
    value: '其他组织',
    label: '其他组织',
    children: [
      { value: '分公司A', label: '分公司A' },
      { value: '分公司B', label: '分公司B' }
    ]
  }
])
const customerList = ref<Customer[]>([])

// 使用静态方法而不是实例方法

// 安全数据访问函数
function safeGet<T>(obj: any, path: string | string[], defaultValue: T): T {
  try {
    if (!obj || typeof obj !== 'object') return defaultValue;
    
    const keys = Array.isArray(path) ? path : path.split('.');
    let result: any = obj;
    
    for (const key of keys) {
      // 处理数组索引访问
      const arrayMatch = key.match(/^(\w+)\[(\d+)\]$/);
      if (arrayMatch) {
        const arrayName = arrayMatch[1];
        const index = parseInt(arrayMatch[2], 10);
        
        if (!Array.isArray(result[arrayName]) || index < 0 || index >= result[arrayName].length) {
          return defaultValue;
        }
        result = result[arrayName][index];
      } else {
        if (result === null || result === undefined || !Object.prototype.hasOwnProperty.call(result, key)) {
          return defaultValue;
        }
        result = result[key];
      }
    }
    
    return result !== undefined ? result : defaultValue;
  } catch (error) {
    console.error('安全属性访问错误:', error);
    return defaultValue;
  }
}

// 安全解析JSON
function safeParseJSON<T>(jsonString: string | null | undefined, defaultValue: T): T {
  if (!jsonString || typeof jsonString !== 'string') return defaultValue;
  
  try {
    const parsed = JSON.parse(jsonString);
    return parsed === null ? defaultValue : parsed;
  } catch (error) {
    console.error('JSON解析错误:', error, '\n输入内容:', jsonString);
    return defaultValue;
  }
}

const dialogVisible = ref(false)
const batchDialogVisible = ref(false)
const activeTab = ref('basic')
const batchActiveTab = ref('import')
const dialogType = ref<'add' | 'edit'>('add')
const currentCustomerId = ref('')
const selectedCustomers = ref<Customer[]>([])
// 新增加载状态，提高用户体验
const loadingDetail = ref(false)
const saveLoading = ref(false)

// 分页数据
const tablePageData = ref<PageDTO<Customer>>(createPageDTO())

// 表格列配置
const tabdatacolumns: MyTableColumn[] = [
  { prop: 'name', label: '客户名称', width: 180 },
  { prop: 'code', label: '客户编号', width: 120 },
  { prop: 'category', label: '客户类别', width: 100 },
  { prop: 'level', label: '客户等级', width: 100 },
  { prop: 'organization', label: '所属组织', width: 120 },
  { prop: 'ownerUser', label: '所属用户', width: 120 },
  { prop: 'remark', label: '备注信息', flex: 1 }, // 改为flex: 1，自动填充剩余宽度
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

// 用户树形数据
const userTreeData = ref([
  {
    value: '管理员',
    label: '管理员',
    children: [
      {
        value: '张三',
        label: '张三'
      },
      {
        value: '李四',
        label: '李四'
      }
    ]
  },
  {
    value: '运营组',
    label: '运营组',
    children: [
      {
        value: '王五',
        label: '王五'
      },
      {
        value: '赵六',
        label: '赵六'
      }
    ]
  }
])

// 搜索表单
const searchForm = reactive({
  name: '',
  code: '',
  category: '',
  level: '',
  contactPerson: '',
  phone: '',
  ownerUser: [],
  remark: ''
})

// 客户表单
const customerForm = reactive<Partial<Customer>>({
  name: '',
  code: '',
  organization: ['默认组织'],
  ownerUser: ['管理员'],
  category: '常规类别',
  level: '常规等级',
  bank: '',
  bankAccount: '',
  taxNumber: '',
  remark: ''
})

// 联系人列表
const contactList = ref<Contact[]>([
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
const contactForm = reactive<Partial<Contact>>({
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
const contactTableOperBtns = ref<MyTableOperationsBtn[]>([
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
    evtname: 'detail'
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
])

// 计算属性
const filteredCustomers = computed(() => {
  return customerList.value.filter((customer) => {
    // 安全访问属性，避免空引用
    const name = safeGet(customer, 'name', '').toLowerCase();
    const code = safeGet(customer, 'code', '').toLowerCase();
    const category = safeGet(customer, 'category', '');
    const level = safeGet(customer, 'level', '');
    const ownerUser = safeGet(customer, 'ownerUser[0]', '');
    const remark = safeGet(customer, 'remark', '');
    
    const nameMatch = !searchForm.name || name.includes(searchForm.name.toLowerCase());
    const codeMatch = !searchForm.code || code.includes(searchForm.code.toLowerCase());
    const categoryMatch = !searchForm.category || category === searchForm.category;
    const levelMatch = !searchForm.level || level === searchForm.level;
    const ownerUserMatch = !searchForm.ownerUser.length || ownerUser === safeGet(searchForm.ownerUser, '[0]', '');
    const remarkMatch = !searchForm.remark || remark.includes(searchForm.remark);

    return nameMatch && codeMatch && categoryMatch && levelMatch && ownerUserMatch && remarkMatch;
  });
})

// 更新表格数据
const updateTableData = () => {
  const filtered = filteredCustomers.value
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
  loadCustomerList()
  document.addEventListener('click', handleDocumentClick)
})

// 加载客户列表
async function loadCustomerList() {
  try {
    // 记录请求开始时间，用于性能监控
    const startTime = Date.now();
    
    // 构建查询参数，使用安全访问并进行数据验证
    const query: CustomerQuery = {
      pageIndex: Math.max(1, parseInt(String(tablePageData.value.pageIndex || 1), 10)),
      pageSize: Math.max(1, Math.min(100, parseInt(String(tablePageData.value.pageSize || 10), 10))),
      page: Math.max(1, parseInt(String(tablePageData.value.pageIndex || 1), 10)), // 兼容page参数
      name: safeGet(searchForm, 'name', '').trim(),
      number: safeGet(searchForm, 'code', '').trim(),
      category: safeGet(searchForm, 'category', '') || undefined,
      level: safeGet(searchForm, 'level', '') || undefined,
      contact_person: safeGet(searchForm, 'contactPerson', '').trim() || undefined,
      telephone: safeGet(searchForm, 'phone', '').trim() || undefined
    }
    
    console.log('请求客户列表参数:', query);
    
    const response = await CustomerAPI.getCustomerList(query)
    
    // 记录请求耗时
    const endTime = Date.now();
    console.log('获取客户列表耗时:', endTime - startTime, 'ms');
    
    // 确保response是有效的JSON对象
    const safeResponse = typeof response === 'string' ? safeParseJSON(response, {}) : response;
    
    // 安全处理响应 - 适配C++后端（使用code 10000或200作为成功状态码）
    const responseCode = safeGet(safeResponse, 'code', -1) as number;
    const responseData = safeGet(safeResponse, 'data', null);
    if ((responseCode === 10000 || responseCode === 200) && responseData) {
      // 安全转换数据格式 - 使用rows而不是items以适配C++后端
      const rows = safeGet(safeResponse, 'data.rows', []);
      if (Array.isArray(rows)) {
        // 转换API返回的数据格式以适应前端展示
        customerList.value = rows.map((row: any): Customer | null => {
          try {
            return {
              id: safeGet(row, 'id', ''),
              name: safeGet(row, 'name', ''),
              code: safeGet(row, 'number', ''),
              category: safeGet(row, 'category', '常规类别'),
              level: safeGet(row, 'level', '常规等级'),
              organization: [safeGet(row, 'organization', '默认组织')],
              ownerUser: [safeGet(row, 'owner_user', '管理员')],
              remark: safeGet(row, 'remark', '')
            };
          } catch (itemError) {
            console.error('处理客户数据项失败:', itemError, '数据项:', row);
            return null;
          }
        }).filter((item): item is Customer => item !== null); // 过滤无效数据
      }
      tablePageData.value.total = Math.max(0, parseInt(String(safeGet(safeResponse, 'data.total', 0)), 10))
    } else {
      const errorMsg = safeGet(safeResponse, 'message', '获取客户列表失败');
      console.warn('API返回错误:', errorMsg);
      ElMessage.warning(errorMsg);
    }
  } catch (error) {
    console.error('加载客户列表失败:', error);
    // 更详细的错误信息处理
    let errorMessage = '加载客户列表失败';
    
    if (error instanceof Error) {
      if (error.name === 'TypeError' || error.name === 'SyntaxError') {
        errorMessage = '数据格式错误，请联系管理员';
      } else if (error.message.includes('timeout')) {
        errorMessage = '请求超时，请稍后重试';
      }
    }
    
    ElMessage.error(errorMessage);
  } finally {
    updateTableData()
  }
}

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
  tablePageData.value.pageIndex = 1
  dropdownVisible.value = false
  loadCustomerList()
}

function handleResetSearch() {
  Object.assign(searchForm, {
    name: '',
    code: '',
    category: '',
    level: '',
    contactPerson: '',
    phone: '',
    ownerUser: [],
    remark: ''
  })
  tablePageData.value.pageIndex = 1
  loadCustomerList()
}

// 处理选中项变化
const handleSelectionChange = (val: Customer[]) => {
  selectedCustomers.value = val
}

// 分页处理函数
const handlePageChange = (data: PageDTO<Customer>) => {
  tablePageData.value = data
  loadCustomerList()
}

// 表格操作处理函数
const handleTableOperClick = (index: number, row: Customer, evtname: string) => {
  if (evtname === 'view') {
    handleView(row)
  } else if (evtname === 'delete') {
    handleDelete(row)
  }
}

function handleAdd() {
  dialogType.value = 'add'
  currentCustomerId.value = ''
  Object.assign(customerForm, {
    name: '',
    code: '',
    organization: '默认组织',
    ownerUser: ['管理员'],
    category: '常规类别',
    level: '常规等级',
    bank: '',
    bankAccount: '',
    taxNumber: '',
    remark: ''
  })
  // 初始化为空数组，不显示空联系人
  contactList.value = []
  activeTab.value = 'basic'
  dialogVisible.value = true
}

async function handleView(row: Customer) {
  try {
    // 确保row.id存在
    if (!safeGet(row, 'id', '')) {
      ElMessage.error('无效的客户数据')
      return
    }
    
    // 新增加载状态
    loadingDetail.value = true
    const response = await CustomerAPI.getCustomerDetail(row.id)
    
    // 确保response是有效的JSON对象
    const safeResponse = typeof response === 'string' ? safeParseJSON(response, {}) : response;
    
    const detailCode = safeGet(safeResponse, 'code', -1) as number;
    const detailData = safeGet(safeResponse, 'data', null);
    if (detailCode === 0 && detailData) {
      dialogType.value = 'edit'
      currentCustomerId.value = safeGet(row, 'id', '')
      const customer = detailData as any
      
      // 安全转换数据格式以适应表单
      Object.assign(customerForm, {
        name: safeGet(customer, 'name', ''),
        code: safeGet(customer, 'number', ''),
        organization: [safeGet(customer, 'organization', '默认组织')],
        ownerUser: [safeGet(customer, 'owner_user', '管理员')],
        category: safeGet(customer, 'category', '常规类别'),
        level: safeGet(customer, 'level', '常规等级'),
        bank: safeGet(customer, 'bank_name', ''),
        bankAccount: safeGet(customer, 'bank_account', ''),
        taxNumber: safeGet(customer, 'tax_number', ''),
        remark: safeGet(customer, 'remark', '')
      })
      
      // 安全加载联系人数据，确保contacts是数组
      const contacts = safeGet(customer, 'contacts', []);
      contactList.value = Array.isArray(contacts) ? contacts.map((contact: any): Contact => ({
        id: safeGet(contact, 'id', ''),
        name: safeGet(contact, 'name', ''),
        phone: safeGet(contact, 'phone', ''),
        email: safeGet(contact, 'email', ''),
        address: safeGet(contact, 'address', ''),
        remark: safeGet(contact, 'remark', ''),
        isMain: safeGet(contact, 'isMain', false)
      })).filter(item => item.name || item.phone) : [];
      
      activeTab.value = 'basic'
      dialogVisible.value = true
    } else {
      ElMessage.error(safeGet(safeResponse, 'message', '获取客户详情失败'))
    }
  } catch (error) {
    console.error('获取客户详情失败:', error)
    let errorMessage = '获取客户详情失败';
    
    if (error instanceof Error) {
      if (error.name === 'TypeError' || error.name === 'SyntaxError') {
        errorMessage = '数据格式错误，请联系管理员';
      } else if (error.message.includes('timeout')) {
        errorMessage = '请求超时，请稍后重试';
      }
    }
    
    ElMessage.error(errorMessage)
  } finally {
    // 无论成功失败都要重置加载状态
    loadingDetail.value = false
  }
}

async function handleDelete(row: Customer) {
  try {
    // 确保ID存在
    const customerId = safeGet(row, 'id', '')
    if (!customerId) {
      ElMessage.error('无效的客户数据')
      return
    }
    
    // 增加二次确认
    await ElMessageBox.confirm(
      `确定要删除客户「${safeGet(row, 'name', '')}」吗？此操作不可撤销。`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await CustomerAPI.deleteCustomer(customerId)
    
    // 安全检查响应
    if ((safeGet(response, 'code', -1) as number) === 0) {
      ElMessage.success('删除成功')
      loadCustomerList() // 重新加载列表
    } else {
      ElMessage.error(safeGet(response, 'message', '删除失败'))
    }
  } catch (error) {
    // 处理用户取消操作和其他错误
    if (error !== 'cancel') {
      console.error('删除客户失败:', error)
      ElMessage.error('删除失败，请检查网络连接或稍后重试')
    }
  }
}

function handleAddContact() {
  contactDialogType.value = 'add'
  currentContactIndex.value = -1
  Object.assign(contactForm, {
    name: '',
    phone: '',
    address: '',
    remark: '',
    isMain: false // 默认不设为主联系人
  })
  contactDialogVisible.value = true
}

function handleContactTableOperClick(index: number, row: Contact, evtname: string) {
  if (evtname === 'add') {
    handleAddContact()
  } else if (evtname === 'detail') {
    handleEditContact(index, row)
  } else if (evtname === 'delete') {
    handleDeleteContact(index)
  }
}

function handleEditContact(index: number, row: Contact) {
  contactDialogType.value = 'edit'
  currentContactIndex.value = index
  Object.assign(contactForm, { ...row })
  contactDialogVisible.value = true
}

function handleDeleteContact(index: number) {
  ElMessageBox.confirm('确定要删除该联系人吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      contactList.value.splice(index, 1)
      ElMessage.success('删除成功')
    })
    .catch(() => {
      // 取消删除
    })
}

function handleMainContactChange() {
  // 如果设置为主联系人，将其他联系人设置为非主联系人
  if (contactForm.isMain && contactDialogType.value === 'edit' && currentContactIndex.value !== -1) {
    contactList.value.forEach((contact, index) => {
      if (index !== currentContactIndex.value) {
        contact.isMain = false
      }
    })
  }
}

function handleSaveContact() {
  // 验证必填项
  if (!contactForm.name || !contactForm.phone) {
    ElMessage.error('请填写必填项（联系人员和联系号码）')
    return
  }

  if (contactDialogType.value === 'add') {
    // 添加新联系人
    const newContact: Contact = {
      id: Date.now().toString(),
      name: contactForm.name!,
      phone: contactForm.phone!,
      address: contactForm.address || '',
      remark: contactForm.remark,
      isMain: contactForm.isMain ?? false
    }

    // 如果新联系人设为主联系人，将其他联系人设为非主联系人
    if (newContact.isMain) {
      contactList.value.forEach((contact) => {
        contact.isMain = false
      })
    }

    contactList.value.push(newContact)
    ElMessage.success('添加成功')
  } else {
    // 编辑现有联系人
    if (currentContactIndex.value !== -1) {
      const updatedContact: Contact = {
        ...contactList.value[currentContactIndex.value],
        name: contactForm.name!,
        phone: contactForm.phone!,
        address: contactForm.address || '',
        remark: contactForm.remark,
        isMain: contactForm.isMain ?? false
      }

      // 如果更新后的联系人设为主联系人，将其他联系人设为非主联系人
      if (updatedContact.isMain) {
        contactList.value.forEach((contact, index) => {
          if (index !== currentContactIndex.value) {
            contact.isMain = false
          }
        })
      }

      contactList.value[currentContactIndex.value] = updatedContact
      ElMessage.success('更新成功')
    }
  }

  contactDialogVisible.value = false
}

function handleContactDialogClose() {
  contactDialogVisible.value = false
}

async function handleSave() {
  // 安全的参数验证
  const name = safeGet(customerForm, 'name', '').trim();
  const code = safeGet(customerForm, 'code', '').trim();
  
  if (!name) {
    ElMessage.error('请填写客户名称');
    return;
  }
  
  if (!code) {
    ElMessage.error('请填写客户编号');
    return;
  }

  try {
    // 新增加载状态
    saveLoading.value = true
    
    // 安全构建请求数据 - 只包含API需要的字段，避免传递多余字段
    const customerData = {
      name: name,
      number: code,
      category: safeGet(customerForm, 'category', '常规类别'),
      level: safeGet(customerForm, 'level', '常规等级'),
      organization: Array.isArray(safeGet(customerForm, 'organization', [])) 
        ? safeGet(customerForm, 'organization[0]', '默认组织')
        : safeGet(customerForm, 'organization', '默认组织'),
      ownerUser: Array.isArray(safeGet(customerForm, 'ownerUser', []))
        ? safeGet(customerForm, 'ownerUser[0]', '管理员')
        : safeGet(customerForm, 'ownerUser', '管理员'),
      bank_name: safeGet(customerForm, 'bank', ''),
      bank_account: safeGet(customerForm, 'bankAccount', ''),
      tax_number: safeGet(customerForm, 'taxNumber', ''),
      // 安全处理联系人数据，过滤无效联系人
      contacts: Array.isArray(contactList.value) 
        ? contactList.value.filter((contact: any) => contact.name || contact.phone)
        : []
    }

    let response
    if (dialogType.value === 'add') {
      // 新增客户 - 确保传递正确的数据结构
      console.log('新增客户数据:', customerData);
      response = await CustomerAPI.addCustomer(customerData)
    } else {
      // 更新客户 - 确保ID存在且传递正确的数据结构
      const customerId = currentCustomerId.value;
      if (!customerId) {
        ElMessage.error('客户信息不完整，无法更新')
        return
      }
      console.log('更新客户数据:', customerData);
      response = await CustomerAPI.updateCustomer(customerId, customerData)
    }

    // 确保response是有效的JSON对象
    const safeResponse = typeof response === 'string' ? safeParseJSON(response, {}) : response;
    
    if ((safeGet(safeResponse, 'code', -1) as number) === 0) {
      ElMessage.success(dialogType.value === 'add' ? '新增成功' : '更新成功')
      dialogVisible.value = false
      loadCustomerList() // 重新加载列表
    } else {
      const errorMsg = safeGet(safeResponse, 'message', dialogType.value === 'add' ? '新增失败' : '更新失败');
      console.warn('保存客户失败:', errorMsg);
      ElMessage.error(errorMsg);
    }
  } catch (error) {
    console.error(dialogType.value === 'add' ? '新增客户失败:' : '更新客户失败:', error)
    
    let errorMessage = dialogType.value === 'add' ? '新增失败' : '更新失败';
    
    if (error instanceof Error) {
      if (error.name === 'TypeError' || error.name === 'SyntaxError') {
        errorMessage = '数据格式错误，请联系管理员';
      } else if (error.message.includes('timeout')) {
        errorMessage = '请求超时，请稍后重试';
      } else if (error.message.includes('network')) {
        errorMessage = '网络连接异常，请检查网络';
      }
    }
    
    ElMessage.error(errorMessage)
  } finally {
    // 无论成功失败都要重置加载状态
    saveLoading.value = false
  }
}

function handleDialogClose() {
  dialogVisible.value = false
}

function handleRefresh() {
  ElMessage.info('正在刷新数据...')
  loadCustomerList()
}

// 批量操作相关函数
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
      // 模拟上传过程
      setTimeout(() => {
        ElMessage.success('文件上传成功')
      }, 1000)
    }
  }
  fileInput.click()
}

function handleExportData() {
  if (selectedCustomers.value.length === 0) {
    ElMessage.warning('请先选择要导出的客户')
    return
  }
  ElMessage.success(`正在导出${selectedCustomers.value.length}条数据...`)
  setTimeout(() => {
    ElMessage.success('数据导出成功')
  }, 1000)
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

/* 确保搜索按钮没有背景色 */
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

.form-item label {
  font-size: 14px;
  color: #606266;
}

.form-item label.required::before {
  content: '*';
  color: #f56c6c;
  margin-right: 4px;
}

/* 联系人部分样式 */
.contact-section {
  margin-top: 10px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #ebeef5;
}

.contact-table-container {
  overflow-x: auto;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

.no-data {
  text-align: center;
  padding: 60px 0;
  color: #909399;
  font-size: 14px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  background-color: #fafafa;
}

/* 联系资料表单样式 */
.contact-form-content {
  padding: 10px 0;
}

.main-contact-switch {
  margin-top: 20px;
  display: flex;
  align-items: center;
}

/* 批量操作对话框样式 */
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

/* 导出数据样式 */
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

  .contact-grid {
    grid-template-columns: 1fr;
  }
}

/* 表格样式 */
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
</style>
