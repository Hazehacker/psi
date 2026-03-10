<template>
    <div class="receipt-report page-container">
      <div class="layout">
        <!-- 左侧：筛选按钮 -->
        <div class="left-actions">
          <el-button type="primary" icon="Search" @click="toggleFilterPanel">筛选</el-button>
        </div>
        
        <!-- 右侧：批量操作和刷新按钮 -->
        <div class="right-actions">
          <el-button-group>
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
            <el-button @click="showBatchDialog = true">批量</el-button>
            <el-button @click="reload">刷新</el-button>
          </el-button-group>
        </div>
      </div>
  
      <!-- 筛选面板（替代弹窗） -->
      <div v-if="showFilterPanel" class="filter-panel">
        <div class="filter-section">
          <div class="filter-row">
            <div class="filter-item">
              <label>单据编号</label>
              <el-input v-model="searchForm.number" placeholder="请输入单据编号"></el-input>
            </div>
            <div class="filter-item">
              <label>单据日期</label>
              <el-date-picker
                v-model="searchForm.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="YYYY-MM-DD">
              </el-date-picker>
            </div>
            <div class="filter-item">
              <label>关联人员</label>
              <el-input 
                v-model="searchForm.peopleName" 
                placeholder="请选择关联人员" 
                readonly 
                @click="showPeopleDialog = true">
              </el-input>
            </div>
          </div>
          
          <div class="filter-row">
            <div class="filter-item">
              <label>审核状态</label>
              <el-select v-model="searchForm.examine" placeholder="请选择审核状态">
                <el-option label="已审核" value="1"></el-option>
                <el-option label="未审核" value="0"></el-option>
              </el-select>
            </div>
            <div class="filter-item">
              <label>结算账户</label>
              <el-input 
                v-model="searchForm.accountName" 
                placeholder="请选择结算账户" 
                readonly 
                @click="showAccountDialog = true">
              </el-input>
            </div>
            <div class="filter-item">
              <label>制单人</label>
              <el-input 
                v-model="searchForm.userName" 
                placeholder="请选择制单人" 
                readonly 
                @click="showUserDialog = true">
              </el-input>
            </div>
          </div>
          
          <div class="filter-row">
            <div class="filter-item">
              <label>备注信息</label>
              <el-input v-model="searchForm.data" placeholder="请输入备注信息"></el-input>
            </div>
            <div class="filter-item"></div>
            <div class="filter-item"></div>
          </div>
          
          <div class="filter-row">
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
            <el-button @click="showFilterPanel = false">关闭</el-button>
          </div>
        </div>
      </div>
  
      <el-divider />
  
      <!-- 使用 ReportTable 组件 -->
      <ReportTable
        title="转账单"
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
        <template #billNo="{ row }">
          <span class="link" @click="onView(row)">{{ row.number }}</span>
        </template>
      </ReportTable>
  
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
          <el-table-column prop="name" label="人员名称" width="120"></el-table-column>
          <el-table-column prop="code" label="人员编号" width="100"></el-table-column>
          <el-table-column prop="organization" label="所属组织" width="120"></el-table-column>
          <el-table-column prop="gender" label="人员性别" width="100"></el-table-column>
          <el-table-column prop="phone" label="联系电话" width="120"></el-table-column>
          <el-table-column prop="address" label="联系地址"></el-table-column>
          <el-table-column prop="idCard" label="身份证号" width="180"></el-table-column>
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
          <el-table-column prop="name" label="用户名称" width="120"></el-table-column>
          <el-table-column prop="structure" label="所属架构" width="120"></el-table-column>
          <el-table-column prop="role" label="用户角色" width="120"></el-table-column>
          <el-table-column prop="account" label="用户账号" width="120"></el-table-column>
          <el-table-column prop="phone" label="手机号码" width="120"></el-table-column>
        </el-table>
        <template #footer>
          <el-button @click="showUserDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmUserSelection">确定</el-button>
        </template>
      </el-dialog>
  
      <!-- 结算账户选择弹窗 -->
      <el-dialog 
        v-model="showAccountDialog" 
        title="选择结算账户" 
        width="80%" 
        @close="resetAccountSelection"
        append-to-body
      >
        <el-table 
          :data="accountList" 
          @selection-change="handleAccountSelection"
          @row-click="handleAccountRowClick"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="name" label="账户名称" width="180"></el-table-column>
          <el-table-column prop="code" label="账户编号" width="120"></el-table-column>
          <el-table-column prop="organization" label="所属组织" width="120"></el-table-column>
          <el-table-column prop="balance" label="账户余额" width="120" align="right">
            <template #default="scope">
              {{ formatCurrency(scope.row.balance) }}
            </template>
          </el-table-column>
        </el-table>
        <template #footer>
          <el-button @click="showAccountDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmAccountSelection">确定</el-button>
        </template>
      </el-dialog>
  
      <!-- 批量操作对话框 -->
      <el-dialog v-model="showBatchDialog" title="批量操作" width="420px" destroy-on-close append-to-body>
        <el-tabs v-model="batchActive">
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
        <TransferFormDetail 
          v-if="showDetailDialog && currentRowId" 
          :id="currentRowId"
          @close="showDetailDialog = false"
          @saved="handleDetailSaved"
        />
      </el-dialog>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, reactive, computed, onMounted } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { ArrowDown, Download, Search } from '@element-plus/icons-vue'
  import ReportTable from '@/components/report/reportButtonTable.vue'
  import TransferFormDetail from '@/views/finance/transfer/transferFormDetail.vue'
  
  // 详情弹窗控制
  const showDetailDialog = ref(false);
  const currentRowId = ref<number | null>(null);
  
  // 定义接口
  interface SearchFormType {
    number: string
    dateRange: string[]
    peopleName: string
    peopleId: string | number
    examine: string
    accountName: string
    accountId: string | number
    userName: string
    userId: string | number
    data: string
  }
  
  interface TableRow {
    id: number
    frameData: { name: string }
    time: string
    number: string
    total: number
    extension: { 
      examine: string
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
  
  // 筛选表单
  const searchForm = reactive<SearchFormType>({
    number: '',
    dateRange: [],
    peopleName: '',
    peopleId: '',
    examine: '',
    accountName: '',
    accountId: '',
    userName: '',
    userId: '',
    data: ''
  });
  
  // 分页信息
  const page = reactive({
    current: 1,
    total: 0,
    size: 30
  });
  
  // 表格数据
  const rows = ref<TableRow[]>([]);
  const loading = ref(false);
  
  // 选中的行
  const selection = ref<TableRow[]>([]);
  
  // 弹窗控制
  const showFilterPanel = ref(false);
  const showPeopleDialog = ref(false);
  const showUserDialog = ref(false);
  const showAccountDialog = ref(false);
  const showBatchDialog = ref(false);
  const batchActive = ref('import');
  
  // 弹窗选择状态
  const peopleSelection = ref<any[]>([]);
  const userSelection = ref<any[]>([]);
  const accountSelection = ref<any[]>([]);
  
  // 表格列定义 - 去掉客户列、核销金额、核销状态
  const columns = [
    { prop: 'frameData.name', label: '所属组织', width: '120', align: 'center' as const },
    { prop: 'time', label: '单据时间', width: '120', align: 'center' as const, sortable: true },
    { prop: 'number', label: '单据编号', width: '180', align: 'center' as const, slot: 'billNo', sortable: true },
    { prop: 'total', label: '单据金额', width: '120', align: 'right' as const, sortable: true },
    { prop: 'peopleData.name', label: '关联人员', width: '120', align: 'center' as const },
    { prop: 'extension.examine', label: '审核状态', width: '100', align: 'center' as const },
    { prop: 'userData.name', label: '制单人', width: '120', align: 'center' as const },
    { prop: 'data', label: '备注信息', minWidth: '200' }
  ]
  
  // 演示数据
  const peopleList = ref([
    { id: 1, name: 'NTJK_test_DD001', code: 'DD001', organization: '总', gender: '男', phone: '', address: '', idCard: '' },
    { id: 2, name: '1', code: '', organization: '', gender: '', phone: '', address: '', idCard: '' },
    { id: 3, name: '崔瀚帅', code: '0023', organization: '默认组织', gender: '男', phone: '', address: '', idCard: '' },
    { id: 4, name: 'ruihi', code: '0098', organization: '默认组织', gender: '女', phone: '', address: '', idCard: '' },
    { id: 5, name: '123', code: '123', organization: '部门1', gender: '女', phone: '', address: '', idCard: '' }
  ]);
  
  const userList = ref([
    { id: 1, name: 'csa', structure: '部门1', role: 'NTJK', account: '1414', phone: '17815712444' },
    { id: 2, name: 'test001', structure: '四川', role: 'NTJK', account: '18341755566', phone: '18341755566' },
    { id: 3, name: 'NTJK_test', structure: 'NTJK', role: 'NTJK', account: 'NTJK_test', phone: '11122233355' },
    { id: 4, name: 'test001a', structure: '默认组织', role: '', account: 'test001a', phone: '18800000001' },
    { id: 5, name: '001', structure: '公司1', role: 'role_01', account: '001', phone: '13388222223' },
    { id: 6, name: 'sd', structure: '默认组织', role: '', account: 'ok', phone: '13388222222' },
    { id: 7, name: '瀚', structure: '超级管理员', role: '四川', account: 'annin', phone: '13225412256' }
  ]);
  
  const accountList = ref([
    { id: 1, name: '四川账户', code: '423423', organization: '四川', balance: -1391489.02 },
    { id: 2, name: '让5mt', code: 'tyutu', organization: '默认组织', balance: 893601585 },
    { id: 3, name: 'dfg', code: 'dfg', organization: '法师贰', balance: 61375589 },
    { id: 4, name: '中创资金', code: '111', organization: '默认组织', balance: -4135634.64 },
    { id: 5, name: '1', code: '1', organization: '默认组织', balance: 11349309 }
  ]);
  
  // 模拟表格数据 - 修改为转账单数据，去掉客户和核销相关字段
  const demoRows: TableRow[] = [
    {
      id: 1,
      frameData: { name: '默认组织' },
      time: '2024-10-15',
      number: 'ZZD202410150001',
      total: 5000.00,
      extension: { examine: '已审核' },
      peopleData: { name: '崔瀚帅' },
      userData: { name: 'csa' },
      data: '测试备注信息',
      recordData: [
        { extension: { time: '2024-10-15 10:30:00' }, userData: { name: 'csa' }, info: '创建单据' },
        { extension: { time: '2024-10-15 14:20:00' }, userData: { name: 'admin' }, info: '审核通过' }
      ]
    },
    {
      id: 2,
      frameData: { name: '四川' },
      time: '2024-10-16',
      number: 'ZZD202410160001',
      total: 3000.00,
      extension: { examine: '未审核' },
      peopleData: { name: 'ruihi' },
      userData: { name: 'test001' },
      data: '',
      recordData: [
        { extension: { time: '2024-10-16 09:15:00' }, userData: { name: 'test001' }, info: '创建单据' }
      ]
    },
    {
      id: 3,
      frameData: { name: '默认组织' },
      time: '2024-10-17',
      number: 'ZZD202410170001',
      total: 8000.00,
      extension: { examine: '未审核' },
      peopleData: { name: '123' },
      userData: { name: 'NTJK_test' },
      data: '大额转账',
      recordData: [
        { extension: { time: '2024-10-17 16:45:00' }, userData: { name: 'NTJK_test' }, info: '创建单据' }
      ]
    }
  ];
  
  // 汇总数据计算 - 只保留单据金额汇总
  const summaryData = computed(() => {
    const total = rows.value.reduce((s, r) => s + (parseFloat(r.total?.toString() || '0') || 0), 0);
    return [
      { label: '总单据金额', value: formatCurrency(total) }
    ];
  });
  
  // 获取行操作日志
  function getRowLogs(row: TableRow): Array<{ time: string; user: string; action: string }> {
    if (!row.recordData || !Array.isArray(row.recordData)) return [];
    return row.recordData.map((item: any) => ({
      time: item.extension?.time || '',
      user: item.userData?.name || '',
      action: item.info || ''
    }));
  }
  
  // 切换筛选面板显示
  const toggleFilterPanel = () => {
    showFilterPanel.value = !showFilterPanel.value;
  };
  
  // 关联人员选择相关函数
  const handlePeopleSelection = (val: any[]) => {
    peopleSelection.value = val;
  };
  
  const handlePeopleRowClick = (row: any) => {
    const index = peopleSelection.value.findIndex(item => item.id === row.id);
    if (index > -1) {
      peopleSelection.value.splice(index, 1);
    } else {
      peopleSelection.value = [row];
    }
  };
  
  const confirmPeopleSelection = () => {
    if (peopleSelection.value.length > 0) {
      const row = peopleSelection.value[0];
      searchForm.peopleName = row.name;
      searchForm.peopleId = row.id;
      showPeopleDialog.value = false;
    } else {
      ElMessage.warning('请选择一个关联人员');
    }
  };
  
  const resetPeopleSelection = () => {
    peopleSelection.value = [];
  };
  
  // 制单人选择相关函数
  const handleUserSelection = (val: any[]) => {
    userSelection.value = val;
  };
  
  const handleUserRowClick = (row: any) => {
    const index = userSelection.value.findIndex(item => item.id === row.id);
    if (index > -1) {
      userSelection.value.splice(index, 1);
    } else {
      userSelection.value = [row];
    }
  };
  
  const confirmUserSelection = () => {
    if (userSelection.value.length > 0) {
      const row = userSelection.value[0];
      searchForm.userName = row.name;
      searchForm.userId = row.id;
      showUserDialog.value = false;
    } else {
      ElMessage.warning('请选择一个制单人');
    }
  };
  
  const resetUserSelection = () => {
    userSelection.value = [];
  };
  
  // 结算账户选择相关函数
  const handleAccountSelection = (val: any[]) => {
    accountSelection.value = val;
  };
  
  const handleAccountRowClick = (row: any) => {
    const index = accountSelection.value.findIndex(item => item.id === row.id);
    if (index > -1) {
      accountSelection.value.splice(index, 1);
    } else {
      accountSelection.value = [row];
    }
  };
  
  const confirmAccountSelection = () => {
    if (accountSelection.value.length > 0) {
      const row = accountSelection.value[0];
      searchForm.accountName = row.name;
      searchForm.accountId = row.id;
      showAccountDialog.value = false;
    } else {
      ElMessage.warning('请选择一个结算账户');
    }
  };
  
  const resetAccountSelection = () => {
    accountSelection.value = [];
  };
  
  // 表格选择变化
  const onSelectionChange = (val: TableRow[]) => {
    selection.value = val;
  };
  
  // 分页变化
  const onPageChange = (pageIndex: number, pageSize: number) => {
    page.current = pageIndex;
    page.size = pageSize;
    fetchData();
  };
  
  // 查看详情方法 - 改为打开全屏弹窗
  const onView = (row: TableRow) => {
    currentRowId.value = row.id;
    showDetailDialog.value = true;
  };
  
  // 详情弹窗关闭处理
  const handleDetailClose = () => {
    currentRowId.value = null;
    showDetailDialog.value = false;
  };
  
  // 详情保存后的处理
  const handleDetailSaved = () => {
    showDetailDialog.value = false;
    fetchData(); // 刷新列表数据
    ElMessage.success('操作成功');
  };
  
  // 删除
  const onDelete = (rows: TableRow[]) => {
    if (!rows?.length) return;
    ElMessageBox.confirm('您确定要删除选中数据吗?', '提示', { type: 'warning' })
      .then(() => {
        ElMessage.success('删除成功!');
        fetchData();
      })
      .catch(() => {});
  };
  
  // 批量删除
  const batchDelete = () => {
    if (!selection.value.length) return;
    onDelete(selection.value);
  };
  
  // 搜索
  const handleSearch = () => {
    page.current = 1;
    fetchData();
    showFilterPanel.value = false; // 搜索后关闭筛选面板
  };
  
  // 重置搜索 
  const resetSearch = () => {
    searchForm.number = '';
    searchForm.dateRange = [];
    searchForm.peopleName = '';
    searchForm.peopleId = '';
    searchForm.examine = '';
    searchForm.accountName = '';
    searchForm.accountId = '';
    searchForm.userName = '';
    searchForm.userId = '';
    searchForm.data = '';
    fetchData();
  };
  
  // 批量审核
  const batchExamine = (type: number) => {
    if (!selection.value.length) {
      ElMessage.warning('无可操作单据!');
      return;
    }
    const action = type === 0 ? '审核' : '反审核';
    ElMessage.success(`${action}成功!`);
    fetchData();
  };
  
  // 获取数据 - 支持 API 调用和演示数据
  const fetchData = async () => {
    loading.value = true;
    
    try {
      // 这里可以调用真实 API
      // const params = {
      //   page: page.current,
      //   limit: page.size,
      //   number: searchForm.number,
      //   startTime: searchForm.dateRange[0],
      //   endTime: searchForm.dateRange[1],
      //   people: searchForm.peopleId,
      //   user: searchForm.userId,
      //   examine: searchForm.examine,
      //   account: searchForm.accountId,
      //   data: searchForm.data
      // };
      // const res = await http.post('/transfer/record', params);
      // if (res?.code === 10000 || res?.state === 'success') {
      //   rows.value = res.data?.list || [];
      //   page.total = res.data?.total || 0;
      // } else {
      //   ElMessage.error(res?.message || res?.info || '获取数据失败');
      // }
      
      // 使用演示数据
      setTimeout(() => {
        rows.value = demoRows;
        page.total = demoRows.length;
        loading.value = false;
      }, 500);
      
    } catch (error) {
      console.error('获取数据失败:', error);
      ElMessage.error('获取数据失败');
      loading.value = false;
    }
  };
  
  // 刷新
  const reload = () => {
    fetchData();
    ElMessage.success('页面刷新成功!');
  };
  
  // 下载模板
  const downloadTemplate = () => {
    ElMessage.success('[ 数据模板 ] 下载请求中...');
  };
  
  // 上传模板回调
  const onImportSuccess = () => {
    ElMessage.success('导入单据成功!');
    showBatchDialog.value = false;
    fetchData();
  };
  
  // 导出报表
  const exportReport = (type: string) => {
    if (!selection.value.length) {
      ElMessage.warning('未选择导出数据内容!');
      return;
    }
    ElMessage.success(`[ ${type === 'simple' ? '简易报表' : '详细报表'} ] 导出请求中...`);
  };
  
  // 格式化金额
  const formatCurrency = (value: any) => {
    if (!value && value !== 0) return '-';
    return parseFloat(value).toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });
  };
  
  onMounted(() => {
    // 默认最近7天
    const now = new Date();
    const end = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const start = new Date(now.getTime() - 6 * 24 * 3600 * 1000);
    
    searchForm.dateRange = [
      `${start.getFullYear()}-${(start.getMonth() + 1).toString().padStart(2, '0')}-${start.getDate().toString().padStart(2, '0')}`,
      `${end.getFullYear()}-${(end.getMonth() + 1).toString().padStart(2, '0')}-${end.getDate().toString().padStart(2, '0')}`
    ];
    
    fetchData();
  });
  </script>
  
  <style scoped>
  .page-container { padding: 12px; }
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
  .link { color: #409eff; cursor: pointer; }
  .import-tip { margin: 0; padding-left: 20px; }
  .import-tip li { margin-bottom: 8px; font-size: 13px; color: #606266; }
  .export-group { display: flex; gap: 20px; justify-content: center; padding: 20px 0; }
  .export-item { display: flex; flex-direction: column; align-items: center; gap: 8px; cursor: pointer; padding: 16px; border: 1px solid #dcdfe6; border-radius: 4px; transition: all 0.3s; }
  .export-item:hover { border-color: #409eff; color: #409eff; }
  .export-item .el-icon { font-size: 24px; }
  .export-item p { margin: 0; font-size: 14px; }
  .filter-panel {
    background: white;
    padding: 20px;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
  }
  .filter-section { padding: 20px; }
  .filter-row { display: flex; flex-wrap: wrap; gap: 20px; margin-bottom: 15px; }
  .filter-item { display: flex; flex-direction: column; min-width: 180px; }
  .filter-item label { margin-bottom: 5px; font-size: 14px; color: #606266; }
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