<template>
    <div class="writeoff-report page-container">
      <!-- 顶部操作栏 -->
      <div class="layout">
        <!-- 左侧：筛选按钮 -->
        <div class="left-actions">
          <el-button type="primary" icon="Search" @click="toggleFilterPanel">
            筛选
          </el-button>
        </div>
        
        <!-- 右侧：批量操作和刷新按钮 -->
        <div class="right-actions">
          <el-button-group>
            <!-- 有选中项时显示批量操作 -->
            <template v-if="selection.length">
              <el-popover placement="bottom" trigger="click">
                <template #reference>
                  <el-button>操作</el-button>
                </template>
                <div class="operation-menu">
                  <div class="menu-item" @click="batchExamine(0)">审核</div>
                  <div class="menu-item" @click="batchExamine(1)">反审核</div>
                </div>
              </el-popover>
              <el-button @click="batchDelete">删除</el-button>
            </template>
            <!-- 批量操作和刷新按钮 -->
            <el-button @click="showBatchDialog = true">批量</el-button>
            <el-button @click="reload">刷新</el-button>
          </el-button-group>
        </div>
      </div>
  
      <!-- 筛选面板 -->
      <div v-if="showFilterPanel" class="filter-panel">
        <div class="filter-section">
          <!-- 第一行筛选条件 -->
          <div class="filter-row">
            <div class="filter-item">
              <label>客户</label>
              <el-input 
                v-model="searchForm.customerName" 
                placeholder="请选择客户" 
                readonly 
                @click="showCustomerDialog = true"
              />
            </div>
            <div class="filter-item">
              <label>供应商</label>
              <el-input 
                v-model="searchForm.supplierName" 
                placeholder="请选择供应商" 
                readonly 
                @click="showSupplierDialog = true"
              />
            </div>
            <div class="filter-item">
              <label>单据编号</label>
              <el-input v-model="searchForm.number" placeholder="请输入单据编号" />
            </div>
          </div>
          
          <!-- 第二行筛选条件 -->
          <div class="filter-row">
            <div class="filter-item">
              <label>单据日期</label>
              <el-date-picker
                v-model="searchForm.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="YYYY-MM-DD"
              />
            </div>
            <div class="filter-item">
              <label>关联人员</label>
              <el-input 
                v-model="searchForm.peopleName" 
                placeholder="请选择关联人员" 
                readonly 
                @click="showPeopleDialog = true"
              />
            </div>
            <div class="filter-item">
              <label>审核状态</label>
              <el-select v-model="searchForm.examine" placeholder="请选择审核状态">
                <el-option label="已审核" value="1" />
                <el-option label="未审核" value="0" />
              </el-select>
            </div>
          </div>
          
          <!-- 第三行筛选条件 -->
          <div class="filter-row">
            <div class="filter-item">
              <label>核销类型</label>
              <el-select v-model="searchForm.writeOffType" placeholder="请选择核销类型">
                <el-option label="预收冲应收" value="preReceiptToReceivable" />
                <el-option label="预付冲应付" value="prePaymentToPayable" />
                <el-option label="应收冲应付" value="receivableToPayable" />
                <el-option label="销退冲销售" value="salesReturnToSales" />
                <el-option label="购退冲采购" value="purchaseReturnToPurchase" />
              </el-select>
            </div>
            <div class="filter-item">
              <label>制单人</label>
              <el-input 
                v-model="searchForm.userName" 
                placeholder="请选择制单人" 
                readonly 
                @click="showUserDialog = true"
              />
            </div>
            <div class="filter-item">
              <label>备注信息</label>
              <el-input v-model="searchForm.data" placeholder="请输入备注信息" />
            </div>
          </div>
          
          <!-- 筛选操作按钮 -->
          <div class="filter-row">
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
            <el-button @click="showFilterPanel = false">关闭</el-button>
          </div>
        </div>
      </div>
  
      <el-divider />
  
      <!-- 报表表格组件 -->
      <ReportTable
        title="核销单"
        :columns="columns"
        :data="rows"
        :total="page.total"
        :current-page="page.current"
        :page-size="page.size"
        :summary-data="summaryData"
        :row-logs="getRowLogs"
        :loading="loading"
        @page-change="onPageChange"
        @view="onView"
        @delete="onDelete"
        @selection-change="onSelectionChange"
      >
        <!-- 自定义单据编号列，添加点击查看详情功能 -->
        <template #billNo="{ row }">
          <span class="link" @click="onView(row)">{{ row.number }}</span>
        </template>
      </ReportTable>
  
      <!-- 客户选择弹窗 -->
      <el-dialog 
        v-model="showCustomerDialog" 
        title="选择客户" 
        width="80%" 
        @close="resetCustomerSelection"
        append-to-body
      >
        <el-table 
          :data="customerList" 
          @selection-change="handleCustomerSelection"
          @row-click="handleCustomerRowClick"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="name" label="客户名称" width="180" />
          <el-table-column prop="code" label="客户编号" width="120" />
          <el-table-column prop="category" label="客户类别" width="120" />
          <el-table-column prop="level" label="客户等级" width="120" />
          <el-table-column prop="balance" label="应收款余额" width="120" align="right">
            <template #default="scope">
              {{ formatCurrency(scope.row.balance) }}
            </template>
          </el-table-column>
        </el-table>
        <template #footer>
          <el-button @click="showCustomerDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmCustomerSelection">确定</el-button>
        </template>
      </el-dialog>
  
      <!-- 供应商选择弹窗 -->
      <el-dialog 
        v-model="showSupplierDialog" 
        title="选择供应商" 
        width="80%" 
        @close="resetSupplierSelection"
        append-to-body
      >
        <el-table 
          :data="supplierList" 
          @selection-change="handleSupplierSelection"
          @row-click="handleSupplierRowClick"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="name" label="供应商名称" width="180" />
          <el-table-column prop="code" label="供应商编号" width="120" />
          <el-table-column prop="category" label="供应商类别" width="120" />
          <el-table-column prop="level" label="供应商等级" width="120" />
          <el-table-column prop="balance" label="应付款余额" width="120" align="right">
            <template #default="scope">
              {{ formatCurrency(scope.row.balance) }}
            </template>
          </el-table-column>
        </el-table>
        <template #footer>
          <el-button @click="showSupplierDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmSupplierSelection">确定</el-button>
        </template>
      </el-dialog>
  
      <!-- 关联人员选择弹窗 -->
      <el-dialog 
        v-model="showPeopleDialog" 
        title="选择关联人员" 
        width="80%" 
        @close="resetPeopleSelection"
        append-to-body
      >
        <el-table 
          :data="peopleList" 
          @selection-change="handlePeopleSelection"
          @row-click="handlePeopleRowClick"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="name" label="人员名称" width="120" />
          <el-table-column prop="code" label="人员编号" width="100" />
          <el-table-column prop="organization" label="所属组织" width="120" />
          <el-table-column prop="gender" label="人员性别" width="100" />
          <el-table-column prop="phone" label="联系电话" width="120" />
          <el-table-column prop="address" label="联系地址" />
          <el-table-column prop="idCard" label="身份证号" width="180" />
        </el-table>
        <template #footer>
          <el-button @click="showPeopleDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmPeopleSelection">确定</el-button>
        </template>
      </el-dialog>
  
      <!-- 制单人选择弹窗 -->
      <el-dialog 
        v-model="showUserDialog" 
        title="选择制单人" 
        width="80%" 
        @close="resetUserSelection"
        append-to-body
      >
        <el-table 
          :data="userList" 
          @selection-change="handleUserSelection"
          @row-click="handleUserRowClick"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="name" label="用户名称" width="120" />
          <el-table-column prop="structure" label="所属架构" width="120" />
          <el-table-column prop="role" label="用户角色" width="120" />
          <el-table-column prop="account" label="用户账号" width="120" />
          <el-table-column prop="phone" label="手机号码" width="120" />
        </el-table>
        <template #footer>
          <el-button @click="showUserDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmUserSelection">确定</el-button>
        </template>
      </el-dialog>
  
      <!-- 批量操作对话框 -->
      <el-dialog v-model="showBatchDialog" title="批量操作" width="420px" destroy-on-close append-to-body>
        <el-tabs v-model="batchActive">
          <!-- 导入数据标签页 -->
          <el-tab-pane label="导入数据" name="import">
            <ul class="import-tip">
              <li>1.该功能适用于Excel导入单据数据。</li>
              <li>2.录入数据时，请勿修改首行数据标题以及排序。</li>
              <li>3.字段之间存在关联关系时，将自动进行关联运算。</li>
              <li>4.请查阅使用文档获取字段格式内容以及相关导入须知。</li>
              <li>5.点击下方上传模板，选择您编辑好的模板文件即可。</li>
            </ul>
            <el-divider />
            <el-row style="text-align: center">
              <el-col :span="12">
                <el-button @click="downloadTemplate" type="info">下载模板</el-button>
              </el-col>
              <el-col :span="12">
                <el-upload
                  action="#"
                  :show-file-list="false"
                  :on-success="onImportSuccess"
                  accept=".xlsx,.xls"
                >
                  <el-button type="primary">上传模板</el-button>
                </el-upload>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <!-- 导出数据标签页 -->
          <el-tab-pane label="导出数据" name="export">
            <div class="export-group">
              <div class="export-item" @click="exportReport('simple')">
                <el-icon><Download /></el-icon>
                <p>简易报表</p>
              </div>
              <div class="export-item" @click="exportReport('detailed')">
                <el-icon><Download /></el-icon>
                <p>详细报表</p>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-dialog>
  
      <!-- 全屏详情弹窗 -->
      <el-dialog 
        v-model="showDetailDialog" 
        title="单据详情" 
        fullscreen
        destroy-on-close
        append-to-body
        @close="handleDetailClose"
      >
        <!-- 
          关键修改：传递完整的行数据给详情组件
          - :id: 单据ID
          - :source: 完整的行数据（包含审核状态等所有信息）
          - @examined: 监听审核状态变化事件
        -->
        <WriteOffFormDetail 
          v-if="showDetailDialog && currentRowData" 
          :id="currentRowData.id"
          :source="currentRowData"
          @close="showDetailDialog = false"
          @saved="handleDetailSaved"
          @examined="handleDetailExamined"
        />
      </el-dialog>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, reactive, computed, onMounted } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { ArrowDown, Download, Search } from '@element-plus/icons-vue'
  import ReportTable from '@/components/report/reportButtonTable.vue'
  import WriteOffFormDetail from '@/views/finance/writeoff/writeoffFormDetail.vue'
  
  // ==================== 响应式数据定义 ====================
  
  // 详情弹窗控制
  const showDetailDialog = ref(false)
  const currentRowId = ref<number | null>(null)
  // 关键：存储当前选中行的完整数据，用于传递给详情组件
  const currentRowData = ref<any>(null)
  
  // 筛选表单接口定义
  interface SearchFormType {
    customerName: string
    customerId: string | number
    supplierName: string
    supplierId: string | number
    number: string
    dateRange: string[]
    peopleName: string
    peopleId: string | number
    examine: string
    writeOffType: string
    userName: string
    userId: string | number
    data: string
  }
  
  // 表格行数据接口定义
  interface TableRow {
    id: number
    frameData: { name: string }
    customerData: { name: string }
    supplierData: { name: string }
    time: string
    number: string
    writeOffAmount: number
    writeOffType: string
    extension: { 
      examine: string  // 报表中的审核状态是字符串格式：'已审核'/'未审核'
    }
    peopleData: { name: string }
    userData: { name: string }
    data: string
    recordData?: Array<{
      extension: { time: string }
      userData: { name: string }
      info: string
    }>
  }
  
  // 筛选表单数据
  const searchForm = reactive<SearchFormType>({
    customerName: '',
    customerId: '',
    supplierName: '',
    supplierId: '',
    number: '',
    dateRange: [],
    peopleName: '',
    peopleId: '',
    examine: '',
    writeOffType: '',
    userName: '',
    userId: '',
    data: ''
  })
  
  // 分页信息
  const page = reactive({
    current: 1,
    total: 0,
    size: 30
  })
  
  // 表格数据
  const rows = ref<TableRow[]>([])
  const loading = ref(false)
  
  // 选中的行数据
  const selection = ref<TableRow[]>([])
  
  // 弹窗控制
  const showFilterPanel = ref(false)
  const showCustomerDialog = ref(false)
  const showSupplierDialog = ref(false)
  const showPeopleDialog = ref(false)
  const showUserDialog = ref(false)
  const showBatchDialog = ref(false)
  const batchActive = ref('import')
  
  // 弹窗选择状态
  const customerSelection = ref<any[]>([])
  const supplierSelection = ref<any[]>([])
  const peopleSelection = ref<any[]>([])
  const userSelection = ref<any[]>([])
  
  // ==================== 表格列定义 ====================
  
  // 核销单表格列配置
  const columns = [
    { prop: 'frameData.name', label: '所属组织', width: '120', align: 'center' as const },
    { prop: 'customerData.name', label: '客户', width: '160', align: 'center' as const },
    { prop: 'supplierData.name', label: '供应商', width: '160', align: 'center' as const },
    { prop: 'time', label: '单据时间', width: '120', align: 'center' as const, sortable: true },
    { 
      prop: 'number', 
      label: '单据编号', 
      width: '180', 
      align: 'center' as const, 
      slot: 'billNo',  // 使用自定义插槽显示可点击的单据编号
      sortable: true 
    },
    { prop: 'writeOffType', label: '核销类型', width: '120', align: 'center' as const },
    { prop: 'writeOffAmount', label: '核销金额', width: '120', align: 'right' as const, sortable: true },
    { prop: 'peopleData.name', label: '关联人员', width: '120', align: 'center' as const },
    { prop: 'extension.examine', label: '审核状态', width: '100', align: 'center' as const },
    { prop: 'userData.name', label: '制单人', width: '120', align: 'center' as const },
    { prop: 'data', label: '备注信息', minWidth: '200' }
  ]
  
  // ==================== 演示数据 ====================
  
  // 客户列表数据
  const customerList = ref([
    { id: 1, name: '001', code: '0001', category: '常规类别', level: '常规等级', balance: 5680 },
    { id: 2, name: '理想', code: '005', category: '常规类别', level: '常规等级', balance: -218 },
    { id: 3, name: '123456789', code: '1', category: '常规类别', level: '常规等级', balance: -11 },
    { id: 4, name: '四川张三', code: '23232', category: '常规类别', level: '常规等级', balance: 643 },
    { id: 5, name: '00', code: '00001', category: '常规类别', level: '批发客户', balance: 304980 },
    { id: 6, name: '科瑞', code: '000088', category: '常规类别', level: '常规等级', balance: 11111 }
  ])
  
  // 供应商列表数据
  const supplierList = ref([
    { id: 1, name: '供应商A', code: 'GYSA001', category: '常规类别', level: '一级供应商', balance: 12000 },
    { id: 2, name: '供应商B', code: 'GYSB002', category: '常规类别', level: '二级供应商', balance: 8500 },
    { id: 3, name: '供应商C', code: 'GYSC003', category: '常规类别', level: '一级供应商', balance: 15600 },
    { id: 4, name: '供应商D', code: 'GYSD004', category: '常规类别', level: '三级供应商', balance: 3200 }
  ])
  
  // 人员列表数据
  const peopleList = ref([
    { id: 1, name: 'NTJK_test_DD001', code: 'DD001', organization: '总', gender: '男', phone: '', address: '', idCard: '' },
    { id: 2, name: '1', code: '', organization: '', gender: '', phone: '', address: '', idCard: '' },
    { id: 3, name: '崔瀚帅', code: '0023', organization: '默认组织', gender: '男', phone: '', address: '', idCard: '' },
    { id: 4, name: 'ruihi', code: '0098', organization: '默认组织', gender: '女', phone: '', address: '', idCard: '' },
    { id: 5, name: '123', code: '123', organization: '部门1', gender: '女', phone: '', address: '', idCard: '' }
  ])
  
  // 用户列表数据
  const userList = ref([
    { id: 1, name: 'csa', structure: '部门1', role: 'NTJK', account: '1414', phone: '17815712444' },
    { id: 2, name: 'test001', structure: '四川', role: 'NTJK', account: '18341755566', phone: '18341755566' },
    { id: 3, name: 'NTJK_test', structure: 'NTJK', role: 'NTJK', account: 'NTJK_test', phone: '11122233355' },
    { id: 4, name: 'test001a', structure: '默认组织', role: '', account: 'test001a', phone: '18800000001' },
    { id: 5, name: '001', structure: '公司1', role: 'role_01', account: '001', phone: '13388222223' },
    { id: 6, name: 'sd', structure: '默认组织', role: '', account: 'ok', phone: '13388222222' },
    { id: 7, name: '瀚', structure: '超级管理员', role: '四川', account: 'annin', phone: '13225412256' }
  ])
  
  // ==================== 模拟表格数据 ====================
  
  // 核销单演示数据
  const demoRows: TableRow[] = [
    {
      id: 1,
      frameData: { name: '默认组织' },
      customerData: { name: '001' },
      supplierData: { name: '' },
      time: '2024-10-15',
      number: 'HXD202410150001',
      writeOffAmount: 5000.00,
      writeOffType: '预收冲应收',
      extension: { examine: '已审核' },  // 字符串格式：'已审核'
      peopleData: { name: '崔瀚帅' },
      userData: { name: 'csa' },
      data: '预收冲应收核销',
      recordData: [
        { extension: { time: '2024-10-15 10:30:00' }, userData: { name: 'csa' }, info: '创建单据' },
        { extension: { time: '2024-10-15 14:20:00' }, userData: { name: 'admin' }, info: '审核通过' }
      ]
    },
    {
      id: 2,
      frameData: { name: '四川' },
      customerData: { name: '理想' },
      supplierData: { name: '供应商A' },
      time: '2024-10-16',
      number: 'HXD202410160001',
      writeOffAmount: 3000.00,
      writeOffType: '预付冲应付',
      extension: { examine: '未审核' },  // 字符串格式：'未审核'
      peopleData: { name: 'ruihi' },
      userData: { name: 'test001' },
      data: '预付冲应付核销',
      recordData: [
        { extension: { time: '2024-10-16 09:15:00' }, userData: { name: 'test001' }, info: '创建单据' }
      ]
    },
    {
      id: 3,
      frameData: { name: '默认组织' },
      customerData: { name: '四川张三' },
      supplierData: { name: '供应商B' },
      time: '2024-10-17',
      number: 'HXD202410170001',
      writeOffAmount: 8000.00,
      writeOffType: '应收冲应付',
      extension: { examine: '未审核' },  // 字符串格式：'未审核'
      peopleData: { name: '123' },
      userData: { name: 'NTJK_test' },
      data: '应收冲应付核销',
      recordData: [
        { extension: { time: '2024-10-17 16:45:00' }, userData: { name: 'NTJK_test' }, info: '创建单据' }
      ]
    },
    {
      id: 4,
      frameData: { name: '默认组织' },
      customerData: { name: '科瑞' },
      supplierData: { name: '' },
      time: '2024-10-18',
      number: 'HXD202410180001',
      writeOffAmount: 2500.00,
      writeOffType: '销退冲销售',
      extension: { examine: '已审核' },  // 字符串格式：'已审核'
      peopleData: { name: '崔瀚帅' },
      userData: { name: 'csa' },
      data: '销退冲销售核销',
      recordData: [
        { extension: { time: '2024-10-18 11:20:00' }, userData: { name: 'csa' }, info: '创建单据' },
        { extension: { time: '2024-10-18 15:30:00' }, userData: { name: 'admin' }, info: '审核通过' }
      ]
    }
  ]
  
  // ==================== 计算属性 ====================
  
  // 汇总数据计算
  const summaryData = computed(() => {
    const total = rows.value.reduce((s, r) => s + (parseFloat(r.writeOffAmount?.toString() || '0') || 0), 0)
    return [
      { label: '总核销金额', value: formatCurrency(total) }
    ]
  })
  
  // ==================== 工具函数 ====================
  
  /**
   * 获取行操作日志
   * @param row 表格行数据
   * @returns 操作日志数组
   */
  function getRowLogs(row: TableRow): Array<{ time: string; user: string; action: string }> {
    if (!row.recordData || !Array.isArray(row.recordData)) return []
    return row.recordData.map((item: any) => ({
      time: item.extension?.time || '',
      user: item.userData?.name || '',
      action: item.info || ''
    }))
  }
  
  /**
   * 格式化金额显示
   * @param value 金额数值
   * @returns 格式化后的金额字符串
   */
  const formatCurrency = (value: any) => {
    if (!value && value !== 0) return '-'
    return parseFloat(value).toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    })
  }
  
  // ==================== 筛选面板操作 ====================
  
  /**
   * 切换筛选面板显示状态
   */
  const toggleFilterPanel = () => {
    showFilterPanel.value = !showFilterPanel.value
  }
  
  /**
   * 执行搜索
   */
  const handleSearch = () => {
    page.current = 1
    fetchData()
    showFilterPanel.value = false
  }
  
  /**
   * 重置搜索条件
   */
  const resetSearch = () => {
    searchForm.customerName = ''
    searchForm.customerId = ''
    searchForm.supplierName = ''
    searchForm.supplierId = ''
    searchForm.number = ''
    searchForm.dateRange = []
    searchForm.peopleName = ''
    searchForm.peopleId = ''
    searchForm.examine = ''
    searchForm.writeOffType = ''
    searchForm.userName = ''
    searchForm.userId = ''
    searchForm.data = ''
    fetchData()
  }
  
  // ==================== 弹窗选择相关函数 ====================
  
  // 客户选择相关函数
  const handleCustomerSelection = (val: any[]) => {
    customerSelection.value = val
  }
  
  const handleCustomerRowClick = (row: any) => {
    const index = customerSelection.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      customerSelection.value.splice(index, 1)
    } else {
      customerSelection.value = [row]
    }
  }
  
  const confirmCustomerSelection = () => {
    if (customerSelection.value.length > 0) {
      const row = customerSelection.value[0]
      searchForm.customerName = row.name
      searchForm.customerId = row.id
      showCustomerDialog.value = false
    } else {
      ElMessage.warning('请选择一个客户')
    }
  }
  
  const resetCustomerSelection = () => {
    customerSelection.value = []
  }
  
  // 供应商选择相关函数
  const handleSupplierSelection = (val: any[]) => {
    supplierSelection.value = val
  }
  
  const handleSupplierRowClick = (row: any) => {
    const index = supplierSelection.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      supplierSelection.value.splice(index, 1)
    } else {
      supplierSelection.value = [row]
    }
  }
  
  const confirmSupplierSelection = () => {
    if (supplierSelection.value.length > 0) {
      const row = supplierSelection.value[0]
      searchForm.supplierName = row.name
      searchForm.supplierId = row.id
      showSupplierDialog.value = false
    } else {
      ElMessage.warning('请选择一个供应商')
    }
  }
  
  const resetSupplierSelection = () => {
    supplierSelection.value = []
  }
  
  // 关联人员选择相关函数
  const handlePeopleSelection = (val: any[]) => {
    peopleSelection.value = val
  }
  
  const handlePeopleRowClick = (row: any) => {
    const index = peopleSelection.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      peopleSelection.value.splice(index, 1)
    } else {
      peopleSelection.value = [row]
    }
  }
  
  const confirmPeopleSelection = () => {
    if (peopleSelection.value.length > 0) {
      const row = peopleSelection.value[0]
      searchForm.peopleName = row.name
      searchForm.peopleId = row.id
      showPeopleDialog.value = false
    } else {
      ElMessage.warning('请选择一个关联人员')
    }
  }
  
  const resetPeopleSelection = () => {
    peopleSelection.value = []
  }
  
  // 制单人选择相关函数
  const handleUserSelection = (val: any[]) => {
    userSelection.value = val
  }
  
  const handleUserRowClick = (row: any) => {
    const index = userSelection.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      userSelection.value.splice(index, 1)
    } else {
      userSelection.value = [row]
    }
  }
  
  const confirmUserSelection = () => {
    if (userSelection.value.length > 0) {
      const row = userSelection.value[0]
      searchForm.userName = row.name
      searchForm.userId = row.id
      showUserDialog.value = false
    } else {
      ElMessage.warning('请选择一个制单人')
    }
  }
  
  const resetUserSelection = () => {
    userSelection.value = []
  }
  
  // ==================== 表格操作 ====================
  
  /**
   * 表格选择变化事件
   * @param val 选中的行数据
   */
  const onSelectionChange = (val: TableRow[]) => {
    selection.value = val
  }
  
  /**
   * 分页变化事件
   * @param pageIndex 页码
   * @param pageSize 每页大小
   */
  const onPageChange = (pageIndex: number, pageSize: number) => {
    page.current = pageIndex
    page.size = pageSize
    fetchData()
  }
  
  /**
   * 查看详情 - 关键修改点
   * 将报表中的字符串审核状态转换为详情组件期望的数字格式
   * @param row 表格行数据
   */
  const onView = (row: TableRow) => {
    console.log('打开详情，行数据:', row)
    
    // 关键：将字符串审核状态转换为数字格式
    // 报表中的 '已审核' -> 1, '未审核' -> 0
    const examineStatus = row.extension.examine === '已审核' ? 1 : 0
    
    // 构建传递给详情组件的完整数据对象
    currentRowData.value = {
      ...row,
      examine: examineStatus  // 转换为数字格式
    }
    
    currentRowId.value = row.id
    showDetailDialog.value = true
    
    console.log('传递给详情的数据:', currentRowData.value)
  }
  
  /**
   * 详情弹窗关闭处理
   */
  const handleDetailClose = () => {
    currentRowId.value = null
    currentRowData.value = null  // 清空行数据
    showDetailDialog.value = false
  }
  
  /**
   * 详情保存后的处理
   */
  const handleDetailSaved = () => {
    showDetailDialog.value = false
    fetchData()  // 重新加载数据
    ElMessage.success('操作成功')
  }
  
  /**
   * 处理详情页面的审核状态变化
   * @param data 审核状态变化数据
   */
  const handleDetailExamined = (data: any) => {
    console.log('收到审核状态变化:', data)
    // 刷新数据以确保状态同步
    fetchData()
    ElMessage.success('审核状态已更新')
  }
  
  /**
   * 删除操作
   * @param rows 要删除的行数据
   */
  const onDelete = (rows: TableRow[]) => {
    if (!rows?.length) return
    ElMessageBox.confirm('您确定要删除选中数据吗?', '提示', { type: 'warning' })
      .then(() => {
        ElMessage.success('删除成功!')
        fetchData()
      })
      .catch(() => {})
  }
  
  /**
   * 批量删除
   */
  const batchDelete = () => {
    if (!selection.value.length) return
    onDelete(selection.value)
  }
  
  // ==================== 批量操作 ====================
  
  /**
   * 批量审核/反审核
   * @param type 操作类型：0-审核，1-反审核
   */
  const batchExamine = (type: number) => {
    if (!selection.value.length) {
      ElMessage.warning('无可操作单据!')
      return
    }
    const action = type === 0 ? '审核' : '反审核'
    ElMessage.success(`${action}成功!`)
    fetchData()
  }
  
  // ==================== 数据操作 ====================
  
  /**
   * 获取表格数据
   */
  const fetchData = async () => {
    loading.value = true
    
    try {
      // 模拟API调用延迟
      setTimeout(() => {
        rows.value = demoRows
        page.total = demoRows.length
        loading.value = false
        
        console.log('加载的数据:', demoRows)
      }, 500)
      
    } catch (error) {
      console.error('获取数据失败:', error)
      ElMessage.error('获取数据失败')
      loading.value = false
    }
  }
  
  /**
   * 刷新数据
   */
  const reload = () => {
    fetchData()
    ElMessage.success('页面刷新成功!')
  }
  
  // ==================== 批量操作相关 ====================
  
  /**
   * 下载模板
   */
  const downloadTemplate = () => {
    ElMessage.success('[ 数据模板 ] 下载请求中...')
  }
  
  /**
   * 上传模板成功回调
   */
  const onImportSuccess = () => {
    ElMessage.success('导入单据成功!')
    showBatchDialog.value = false
    fetchData()
  }
  
  /**
   * 导出报表
   * @param type 导出类型：simple-简易报表，detailed-详细报表
   */
  const exportReport = (type: string) => {
    if (!selection.value.length) {
      ElMessage.warning('未选择导出数据内容!')
      return
    }
    ElMessage.success(`[ ${type === 'simple' ? '简易报表' : '详细报表'} ] 导出请求中...`)
  }
  
  // ==================== 生命周期 ====================
  
  onMounted(() => {
    // 设置默认日期范围为最近7天
    const now = new Date()
    const end = new Date(now.getFullYear(), now.getMonth(), now.getDate())
    const start = new Date(now.getTime() - 6 * 24 * 3600 * 1000)
    
    searchForm.dateRange = [
      `${start.getFullYear()}-${(start.getMonth() + 1).toString().padStart(2, '0')}-${start.getDate().toString().padStart(2, '0')}`,
      `${end.getFullYear()}-${(end.getMonth() + 1).toString().padStart(2, '0')}-${end.getDate().toString().padStart(2, '0')}`
    ]
    
    // 初始加载数据
    fetchData()
  })
  </script>
  
  <style scoped>
  .page-container { 
    padding: 12px; 
  }
  
  .layout { 
    display: flex; 
    justify-content: space-between; 
    align-items: center;
    margin-bottom: 16px;
  }
  
  .left-actions {
    display: flex;
    align-items: center;
  }
  
  .right-actions {
    display: flex;
    align-items: center;
  }
  
  /* 可点击的链接样式 */
  .link { 
    color: #409eff; 
    cursor: pointer; 
  }
  
  .link:hover {
    text-decoration: underline;
  }
  
  /* 导入提示样式 */
  .import-tip { 
    margin: 0; 
    padding-left: 20px; 
  }
  
  .import-tip li { 
    margin-bottom: 8px; 
    font-size: 13px; 
    color: #606266; 
  }
  
  /* 导出组样式 */
  .export-group { 
    display: flex; 
    gap: 20px; 
    justify-content: center; 
    padding: 20px 0; 
  }
  
  .export-item { 
    display: flex; 
    flex-direction: column; 
    align-items: center; 
    gap: 8px; 
    cursor: pointer; 
    padding: 16px; 
    border: 1px solid #dcdfe6; 
    border-radius: 4px; 
    transition: all 0.3s; 
  }
  
  .export-item:hover { 
    border-color: #409eff; 
    color: #409eff; 
  }
  
  .export-item .el-icon { 
    font-size: 24px; 
  }
  
  .export-item p { 
    margin: 0; 
    font-size: 14px; 
  }
  
  /* 筛选面板样式 */
  .filter-panel {
    background: white;
    padding: 20px;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
  }
  
  .filter-section { 
    padding: 20px; 
  }
  
  .filter-row { 
    display: flex; 
    flex-wrap: wrap; 
    gap: 20px; 
    margin-bottom: 15px; 
  }
  
  .filter-item { 
    display: flex; 
    flex-direction: column; 
    min-width: 180px; 
  }
  
  .filter-item label { 
    margin-bottom: 5px; 
    font-size: 14px; 
    color: #606266; 
  }
  
  /* 操作菜单样式 */
  .operation-menu {
    padding: 8px 0;
  }
  
  .menu-item {
    padding: 8px 16px;
    cursor: pointer;
  }
  
  .menu-item:hover {
    background-color: #f5f7fa;
  }
  </style>