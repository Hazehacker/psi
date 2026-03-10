<template>
  <div class="bill detail-dialog">
    <div class="dialog-header">
      <h2 class="dialog-title">单据详情</h2>
      <div class="dialog-actions">
        <el-button-group>
          <template v-if="form.examine === 0">
            <el-button type="primary" @click="save">保存</el-button>
            <el-button type="success" @click="examine">审核</el-button>
          </template>
          <template v-else>
            <el-button type="warning" @click="examine">反审核</el-button>
          </template>
          <el-button @click="handleClose">关闭</el-button>
        </el-button-group>
      </div>
    </div>
    <!-- 已审核印章 -->
    <div v-if="form.examine === 1" class="examine-seal">
      <img src="/examine.png" alt="已审核" class="seal-image" />
    </div>

    <el-form
      :model="form"
      :rules="rules"
      ref="formRef"
      :inline="true"
      :hide-required-asterisk="true"
      style="height: 100%"
      :disabled="form.examine === 1" 
    >
      <!-- 客户选择 -->
      <el-form-item label="客户" prop="customer">
        <template #label>
          <span>客户</span>
        </template>
        <div class="search-select-container">
          <el-input
            v-model="form.customerName"
            placeholder="请选择客户"
            readonly
            style="width: 200px"
          ></el-input>
          <el-button 
            type="primary" 
            @click="openCustomerDialog" 
            style="margin-left: 8px"
            :disabled="form.examine === 1"
          >
            选择
          </el-button>
        </div>
      </el-form-item>

      <el-form-item label="单据日期" prop="time">
        <el-date-picker
          v-model="form.time"
          value-format="YYYY-MM-DD"
          type="date"
          placeholder="请选择单据日期"
          :disabled="form.examine === 1"
        ></el-date-picker>
      </el-form-item>

      <el-form-item label="单据编号" prop="number">
        <el-input
          placeholder="请输入单据编号"
          v-model="form.number"
          clearable
          :disabled="form.examine === 1"
        ></el-input>
      </el-form-item>

      <el-divider class="fistDivider"></el-divider>

      <!-- AG Grid 表格区域 -->
      <div class="table-container">
        <div class="table-header">
          <div class="table-title">结算信息</div>
          <div class="table-actions" v-if="form.examine === 0">
            <el-button type="primary" @click="addGridRow" size="small">
              新增
            </el-button>
          </div>
        </div>
        <div class="settlement-table-container">
          <el-table
            :data="tableData"
            :key="tableKey"
            style="width: 100%"
            :show-header="true"
            border
            size="small"
          >
            <!-- 序号列 + 齿轮配置按钮 -->
            <el-table-column type="index" width="60" align="center">
              <template #header>
                <span class="th-gear" @click="configVisible = true">⚙</span>
              </template>
            </el-table-column>
            <el-table-column 
              label="操作" 
              width="80" 
              align="center"
              v-if="form.examine === 0"
            >
              <template #default="{ row, $index }">
                <!-- 修改：只有第一行显示加号，其他行显示删除图标 -->
                <el-button 
                  v-if="$index === 0" 
                  type="primary" 
                  size="small" 
                  @click="addRowAt($index)" 
                  class="add-row-btn"
                >
                  +
                </el-button>
                <el-button 
                  v-else 
                  type="danger" 
                  size="small" 
                  @click="removeRow($index)" 
                  class="del-row-btn"
                >
                  🗑
                </el-button>
              </template>
            </el-table-column>
            
            <el-table-column 
              v-if="columnVisibles.account"
              label="结算账户" 
              prop="account" 
              min-width="200"
            >
              <template #default="{ row, $index }">
                <span 
                  v-if="!row.account || row.account === '点击选择'"
                  class="clickable-text"
                  @click="openAccountSelection($index)"
                  :class="{ 'disabled': form.examine === 1 }"
                >
                  ○ 点击选择
                </span>
                <span v-else>
                  {{ row.account }}
                </span>
              </template>
            </el-table-column>
            
            <el-table-column 
              v-if="columnVisibles.amount"
              label="结算金额" 
              prop="money" 
              width="150"
              align="right"
            >
              <template #default="{ row }">
                <el-input
                  v-model="row.money"
                  placeholder="0.00"
                  size="small"
                  @input="handleMoneyInput(row)"
                  @blur="formatMoney(row)"
                  :disabled="form.examine === 1"
                />
              </template>
            </el-table-column>
            
            <el-table-column 
              v-if="columnVisibles.number"
              label="结算号" 
              prop="settle" 
              width="150"
            >
              <template #default="{ row }">
                <el-input
                  v-model="row.settle"
                  placeholder=""
                  size="small"
                  :disabled="form.examine === 1"
                />
              </template>
            </el-table-column>
            
            <el-table-column 
              v-if="columnVisibles.remark"
              label="备注信息" 
              prop="data" 
              min-width="200"
            >
              <template #default="{ row }">
                <el-input
                  v-model="row.data"
                  placeholder=""
                  size="small"
                  :disabled="form.examine === 1"
                />
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="table-footer">
          <div class="footer-info">
            <span>总条数{{ totalCount }} | 总金额{{ totalAmount }}</span>
          </div>
        </div>
        <!-- 字段配置弹窗 -->
        <el-dialog v-model="configVisible" title="字段配置" width="520px">
          <el-table :data="fieldConfig" border>
            <el-table-column prop="label" label="名称" />
            <el-table-column label="显示" width="160" align="center">
              <template #default="{ row }">
                <el-switch :model-value="columnVisibles[row.key as keyof typeof columnVisibles]" @update:model-value="(val)=>setColumnVisible(row.key, val)" />
              </template>
            </el-table-column>
          </el-table>
        </el-dialog>
      </div>

      <el-divider></el-divider>

      <!-- 底部表单区域 -->
      <LineFeed tag=".el-form-item" :rule="3">
        <el-form-item label="单据金额">
          <el-input
            placeholder="请输入单据金额"
            v-model="form.total"
            :disabled="true"
          ></el-input>
        </el-form-item>

        <el-form-item label="关联人员">
          <div class="search-select-container">
            <el-input
              v-model="form.peopleName"
              placeholder="请选择关联人员"
              readonly
              style="width: 200px"
            ></el-input>
            <el-button 
              type="primary" 
              @click="openPeopleDialog" 
              style="margin-left: 8px"
              :disabled="form.examine === 1"
            >
              选择
            </el-button>
          </div>
        </el-form-item>

        <el-form-item label="单据附件">
          <NodUpload
            v-model="uploadFiles"
            :action="uploadAction"
            :disabled="form.examine === 1"
          ></NodUpload>
        </el-form-item>

        <FieldForm
          style="display: initial"
          v-model="form.more"
          rule="imy"
          prefix="more."
          :disabled="form.examine === 1"
        ></FieldForm>

        <el-form-item label="备注信息">
          <el-input
            placeholder="请输入备注信息"
            v-model="form.data"
            clearable
            :disabled="form.examine === 1"
          ></el-input>
        </el-form-item>
      </LineFeed>
    </el-form>

    <!-- 客户选择对话框 -->
    <el-dialog
      v-model="customerDialogVisible"
      title="选择客户"
      width="80%"
    >
      <div class="dialog-content">
        <el-table
          :data="customerList"
          style="width: 100%"
          @selection-change="handleCustomerSelection"
          ref="customerTableRef"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="客户名称" label="客户名称" />
          <el-table-column prop="客户编号" label="客户编号" />
          <el-table-column prop="客户类别" label="客户类别" />
          <el-table-column prop="客户等级" label="客户等级" />
          <el-table-column prop="所属组织" label="所属组织" />
          <el-table-column prop="备注信息" label="备注信息" />
        </el-table>
      </div>
      <template #footer>
        <el-button @click="customerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmCustomer">确定</el-button>
      </template>
    </el-dialog>

    <!-- 人员选择对话框 -->
    <el-dialog
      v-model="peopleDialogVisible"
      title="选择关联人员"
      width="80%"
    >
      <div class="dialog-content">
        <el-table
          :data="peopleList"
          style="width: 100%"
          @selection-change="handlePeopleSelection"
          ref="peopleTableRef"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="人员名称" label="人员名称" />
          <el-table-column prop="人员编号" label="人员编号" />
          <el-table-column prop="所属组织" label="所属组织" />
          <el-table-column prop="人员性别" label="性别" />
          <el-table-column prop="联系电话" label="联系电话" />
        </el-table>
      </div>
      <template #footer>
        <el-button @click="peopleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmPeople">确定</el-button>
      </template>
    </el-dialog>

    <!-- 结算账户选择对话框 -->
    <el-dialog
      v-model="accountSelectionVisible"
      title="选择结算账户"
      width="80%"
    >
      <div class="dialog-content">
        <el-table
          :data="accountList"
          style="width: 100%"
          @selection-change="handleAccountSelection"
          ref="accountTableRef"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="账户名称" label="账户名称" />
          <el-table-column prop="账户编号" label="账户编号" />
          <el-table-column prop="所属组织" label="所属组织" />
          <el-table-column prop="账户余额" label="账户余额">
            <template #default="{ row }">
              {{ formatCurrency(row.账户余额) }}
            </template>
          </el-table-column>
          <el-table-column prop="备注信息" label="备注信息" />
        </el-table>
      </div>
      <template #footer>
        <el-button @click="accountSelectionVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmAccount">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, computed, onMounted, nextTick, watch } from 'vue'
import { ElMessage, type ElTable } from 'element-plus'
import { useHttp } from '@/plugins/http'

// 导入组件
import LineFeed from '@/components/lib/LineFeed.vue'
import NodUpload from '@/components/lib/NodUpload.vue'
import FieldForm from '@/components/lib/FieldForm.vue'

// 工具函数
const generateUniqid = () => {
  return Date.now().toString(36) + Math.random().toString(36).substr(2)
}

// 组件属性
const props = defineProps({
  id: {  // 改为接收 id
    type: [Number, String],
    default: 0
  }
})

// 定义 emits
const emit = defineEmits<{
  (e: 'close'): void
  (e: 'saved'): void
  (e: 'examined'): void
}>()

// 表单引用
const formRef = ref()
const customerTableRef = ref<InstanceType<typeof ElTable>>()
const peopleTableRef = ref<InstanceType<typeof ElTable>>()
const accountTableRef = ref<InstanceType<typeof ElTable>>()

// 定义表单数据的接口
interface FormData {
  id: number;
  customer: string | null;
  customerName: string;
  time: string;
  number: string;
  total: number;
  people: string | null;
  peopleName: string;
  file: any[];
  data: string;
  examine: number;
  more: Record<string, any>;
}

// 表单数据 
const form = reactive<FormData>({
  id: 0,
  customer: null,
  customerName: '',
  time: '',
  number: '',
  total: 0,
  people: null,
  peopleName: '',
  file: [],
  data: '',
  examine: 0,
  more: {},
})

// 附件上传
const uploadFiles = ref<any[]>([])
  
// 表格数据
const tableData = ref<any[]>([])
const tableKey = ref(0)
// 字段配置
const configVisible = ref(false)
const columnVisibles = reactive({
  account: true,
  amount: true,
  number: true,
  remark: true
})
const fieldConfig = [
  { key: 'account', label: '结算账户' },
  { key: 'amount', label: '结算金额' },
  { key: 'number', label: '结算号' },
  { key: 'remark', label: '备注信息' }
]

const setColumnVisible = (key: string, val: any) => {
  (columnVisibles as any)[key] = Boolean(val)
  tableKey.value++
}
  
// 对话框控制
const customerDialogVisible = ref(false)
const peopleDialogVisible = ref(false)
const accountSelectionVisible = ref(false)
  
// 当前选中的项
const currentCustomer = ref<any[]>([])
const currentPeople = ref<any[]>([])
const selectedAccountIndex = ref(-1)
const selectedAccounts = ref<any[]>([])
  
// 演示数据 
const customerList = ref([
  { 客户名称: 'OPE', 客户编号: 'OPE1', 客户类别: '常规类别', 客户等级: '常规级别', 所属组织: '默认组织', 备注信息: '这是一个测试客户' },
  { 客户名称: '测试客户5', 客户编号: 'BH0005', 客户类别: '常规类别', 客户等级: '常规级别', 所属组织: '默认组织', 备注信息: 'VIP客户' },
  { 客户名称: '测试客户3', 客户编号: 'BH0003', 客户类别: '常规类别', 客户等级: '常规级别', 所属组织: '默认组织', 备注信息: '普通客户' },
  { 客户名称: '测试客户4', 客户编号: 'BH0004', 客户类别: '常规类别', 客户等级: '常规级别', 所属组织: '默认组织', 备注信息: '新客户' },
  { 客户名称: '测试客户2', 客户编号: 'BH0002', 客户类别: '常规类别', 客户等级: '常规级别', 所属组织: '默认组织', 备注信息: '优质客户' }
])
  
const peopleList = ref([
  { 人员名称: 'NTJK_test_DD00', 人员编号: 'DD001', 所属组织: '总', 人员性别: '男', 联系电话: '', 联系地址: '', 身份证号: '', 备注信息: '' },
  { 人员名称: '崔瀚帅', 人员编号: '0023', 所属组织: '默认组织', 人员性别: '男', 联系电话: '', 联系地址: '', 身份证号: '', 备注信息: '' },
  { 人员名称: 'ruini', 人员编号: '0098', 所属组织: '默认组织', 人员性别: '女', 联系电话: '', 联系地址: '', 身份证号: '', 备注信息: '' },
  { 人员名称: '123', 人员编号: '123', 所属组织: '部门1', 人员性别: '女', 联系电话: '', 联系地址: '', 身份证号: '', 备注信息: '' },
  { 人员名称: 'jack', 人员编号: '0001', 所属组织: '默认组织', 人员性别: '男', 联系电话: '', 联系地址: '', 身份证号: '', 备注信息: '' },
  { 人员名称: '张三', 人员编号: '1', 所属组织: '默认组织', 人员性别: '女', 联系电话: '', 联系地址: '', 身份证号: '', 备注信息: '' }
])
  
const accountList = ref([
  { 账户名称: '四川账户', 账户编号: '423423', 所属组织: '四川', 账户余额: -1391344.02, 备注信息: '' },
  { 账户名称: '让5mt', 账户编号: 'tyutu', 所属组织: '默认组织', 账户余额: 898601585, 备注信息: '' },
  { 账户名称: 'dfg', 账户编号: 'dfg', 所属组织: '法师贰', 账户余额: 61376111, 备注信息: '' },
  { 账户名称: '中创资金', 账户编号: '111', 所属组织: '默认组织', 账户余额: -4135634.64, 备注信息: '' },
  { 账户名称: '1', 账户编号: '1', 所属组织: '默认组织', 账户余额: 11349309, 备注信息: '' }
])
  
// 表单验证规则
const rules = {
  customer: {
    required: true,
    message: '请选择客户',
    trigger: 'change',
  },
  time: {
    required: true,
    message: '请选择单据日期',
    trigger: 'change',
  },
  number: {
    required: true,
    message: '请输入单据编号',
    trigger: 'blur',
  },
}
  
// 计算属性
const uploadAction = computed(() => {
  const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
  return `${baseUrl}/imy/upload`
})
  
const hasEditPermission = computed(() => {
  return true
})
  
const hasExaminePermission = computed(() => {
  return true
})
  
// 状态信息
const totalCount = computed(() => {
  return tableData.value.length
})
  
const totalAmount = computed(() => {
  const total = tableData.value.reduce((sum, row) => {
    const amount = parseFloat(row.money) || 0
    return sum + amount
  }, 0)
  return total.toFixed(2)
})
  
// 添加初始行
const addInitialRows = () => {
  const initialRows = [
    {
      uniqid: generateUniqid(),
      accountId: null,
      account: "点击选择",
      money: "",
      settle: "",
      data: "",
    }
  ]
  tableData.value = [...initialRows]
  runHandleGrid()
}
  
const addGridRow = () => {
  const newRow = {
    uniqid: generateUniqid(),
    accountId: null,
    account: "点击选择",
    money: "",
    settle: "",
    data: "",
  }
  tableData.value = [...tableData.value, newRow]
  runHandleGrid()
}
  
const addRowAt = (index: number) => {
  const newRow = {
    uniqid: generateUniqid(),
    accountId: null,
    account: "点击选择",
    money: "",
    settle: "",
    data: "",
  }
  const newData = [...tableData.value]
  newData.splice(index + 1, 0, newRow)
  tableData.value = newData
  runHandleGrid()
}

const removeRow = (index: number) => {
  const newData = [...tableData.value]
  newData.splice(index, 1)
  tableData.value = newData
  runHandleGrid()
}
  
const runHandleGrid = () => {
  // 汇总信息
  summary()
}
  
const summary = () => {
  let total = 0
  tableData.value.forEach((item) => {
    total += parseFloat(item.money) || 0
  })
  form.total = total
}
  
// 处理金额输入
const handleMoneyInput = (row: any) => {
  // 可以添加输入验证逻辑
  runHandleGrid()
}
  
// 格式化金额显示
const formatMoney = (row: any) => {
  if (row.money) {
    const num = parseFloat(row.money)
    if (!isNaN(num)) {
      row.money = num.toFixed(2)
    } else {
      row.money = ""
    }
  }
  runHandleGrid()
}
  
// 初始化
const init = () => {
  if (props.id === 0) {
    // 新增模式
    form.time = new Date().toISOString().split('T')[0]
    form.number = 'SKD' + new Date().toISOString().replace(/[-:T.Z]/g, '').slice(2, 15)
    // 初始化表格数据
    addInitialRows()
  } else {
    // 编辑模式，加载已有数据
    loadExistingData()
  }
}
  
// 打开账户选择
const openAccountSelection = (index: number) => {
  if (form.examine === 1) return // 已审核时不允许选择
  selectedAccountIndex.value = index
  accountSelectionVisible.value = true
  nextTick(() => {
    if (accountTableRef.value) {
      accountTableRef.value.clearSelection()
    }
  })
}
  
// 客户选择相关方法
const openCustomerDialog = () => {
  if (form.examine === 1) return // 已审核时不允许选择
  customerDialogVisible.value = true
  nextTick(() => {
    if (customerTableRef.value) {
      customerTableRef.value.clearSelection()
    }
  })
}
  
const handleCustomerSelection = (selection: any[]) => {
  currentCustomer.value = selection
}
  
const confirmCustomer = () => {
  if (currentCustomer.value.length > 0) {
    const names = currentCustomer.value.map(item => item.客户名称).join(', ')
    form.customerName = names
    form.customer = currentCustomer.value.map(item => item.客户编号).join(',')
    customerDialogVisible.value = false
  } else {
    ElMessage.warning('请选择客户')
  }
}
  
// 人员选择相关方法
const openPeopleDialog = () => {
  if (form.examine === 1) return // 已审核时不允许选择
  peopleDialogVisible.value = true
  nextTick(() => {
    if (peopleTableRef.value) {
      peopleTableRef.value.clearSelection()
    }
  })
}
  
const handlePeopleSelection = (selection: any[]) => {
  currentPeople.value = selection
}
  
const confirmPeople = () => {
  if (currentPeople.value.length > 0) {
    const names = currentPeople.value.map(item => item.人员名称).join(', ')
    form.peopleName = names
    form.people = currentPeople.value.map(item => item.人员编号).join(',')
    peopleDialogVisible.value = false
  } else {
    ElMessage.warning('请选择人员')
  }
}
  
// 账户选择相关方法
const handleAccountSelection = (selection: any[]) => {
  selectedAccounts.value = selection
}
  
const confirmAccount = () => {
  if (selectedAccounts.value.length > 0 && selectedAccountIndex.value !== -1) {
    const account = selectedAccounts.value[0]
    const newData = [...tableData.value]
    newData[selectedAccountIndex.value].account = `${account.账户名称} (${account.账户编号})`
    newData[selectedAccountIndex.value].accountId = account.账户编号
    tableData.value = newData
    accountSelectionVisible.value = false
    selectedAccountIndex.value = -1
  } else {
    ElMessage.warning('请选择账户')
  }
}
  
// 格式化货币
const formatCurrency = (value: number) => {
  return new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: 'CNY'
  }).format(value)
}
  
// 保存单据
const save = async () => {
  try {
    // 表单验证
    await formRef.value?.validate()
    
    // 准备提交数据
    const submitData = {
      class: {
        ...form,
        customerName: undefined,
        peopleName: undefined,
        file: uploadFiles.value
      },
      info: tableData.value.filter(row => row.accountId).map(item => ({
        account: item.accountId,
        money: item.money,
        settle: item.settle,
        data: item.data,
      }))
    }

    // 实际接口调用
    // const result = await useHttp().post('service/receiptRecord', submitData)
    // form.id = result.data.id
    
    ElMessage.success('单据保存成功!')
    emit('saved')
    console.log('提交数据:', submitData)
  } catch (error) {
    ElMessage.error('[ ERROR ] 服务器响应超时!')
  }
}
  
// 审核/反审核
const examine = async () => {
  try {
    if (form.id === 0) {
      ElMessage.warning('请先保存单据再进行审核操作')
      return
    }

    // 实际接口调用
    // const result = await useHttp().post<any>('service/receiptRecord/examine', {
    //   parm: [form.id]
    // })
    
    form.examine = form.examine === 0 ? 1 : 0
    const action = form.examine === 1 ? '审核' : '反审核'
    ElMessage.success(`${action}单据成功!`)
    emit('examined')
  } catch (error) {
    ElMessage.error('[ ERROR ] 服务器响应超时!')
  }
}

// 关闭弹窗
const handleClose = () => {
  emit('close')
}
  
// 加载已有数据
const loadExistingData = async () => {
  try {
    // 实际接口调用代码
    // const result = await useHttp().get<any>('service/receiptRecord', { parm: props.id })
    // Object.assign(form, result.data.class)
    // if (result.data.info && Array.isArray(result.data.info)) {
    //   const gridData = result.data.info.map((item: any) => ({
    //     uniqid: generateUniqid(),
    //     accountId: item.account,
    //     account: item.accountData?.name || '',
    //     money: item.money || "",
    //     settle: item.settle || "",
    //     data: item.data || "",
    //   }))
    //   tableData.value = gridData
    // }
    
    // 模拟数据加载
    if (props.id) {
      // 模拟已审核状态的数据
      if (props.id === 1) {
        Object.assign(form, {
          id: 1,
          customerName: '理想',
          customer: 'BH0001',
          time: '2025-11-03',
          number: 'SKD2511031328334',
          total: 566,
          peopleName: '张三',
          people: '0001',
          data: '测试备注信息',
          examine: 1
        })
        tableData.value = [
          {
            uniqid: generateUniqid(),
            accountId: '423423',
            account: '四川账户 (423423)',
            money: '145.00',
            settle: '1',
            data: '1',
          },
          {
            uniqid: generateUniqid(),
            accountId: 'dfg',
            account: 'dfg (dfg)',
            money: '411.00',
            settle: '1',
            data: '1',
          }
        ]
      } else {
        // 模拟未审核状态的数据
        Object.assign(form, {
          id: props.id,
          customerName: '测试客户',
          customer: 'BH0002',
          time: new Date().toISOString().split('T')[0],
          number: 'SKD' + new Date().toISOString().replace(/[-:T.Z]/g, '').slice(2, 15),
          total: 0,
          peopleName: '',
          people: null,
          data: '',
          examine: 0
        })
        addInitialRows()
      }
    }
    
    ElMessage.info('加载数据成功')
    runHandleGrid()
  } catch (error) {
    ElMessage.error('[ ERROR ] 服务器响应超时!')
  }
}

// 监听 id 变化
watch(() => props.id, (newId) => {
  if (newId !== undefined) {
    init()
  }
})
  
// 初始化
onMounted(() => {
  init()
})
</script>
<style scoped>
.imy.bill.detail-dialog {
  height: 100%;
  padding: 0;
  display: flex;
  flex-direction: column;
  background: white;
  position: relative; /* 为印章定位提供参考 */
}

/* 已审核印章样式 */
.examine-seal {
  position: absolute;
  bottom: 20px; /* 距离底部20px */
  right: 20px; /* 距离右侧20px */
  z-index: 1000;
  pointer-events: none;
  opacity: 0.8;
}

.seal-image {
  width: 120px; /* 调整大小 */
  height: 120px;
  filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.3));
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #ebeef5;
  background: #f5f7fa;
}

.dialog-title {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.dialog-actions {
  display: flex;
  gap: 8px;
}

:deep(.el-form) {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}

.search-select-container {
  display: flex;
  align-items: center;
}

.table-container {
  flex: 1;
  margin: 16px 0;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
  min-height: 300px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
}

.table-title {
  font-weight: bold;
  color: #303133;
}

.table-actions {
  display: flex;
  gap: 8px;
}

.settlement-table-container {
  padding: 0;
}

.table-footer {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 12px 16px;
  background-color: #f5f7fa;
  border-top: 1px solid #ebeef5;
  font-size: 14px;
  color: #606266;
}

.footer-info {
  display: flex;
  align-items: center;
}

.clickable-text {
  color: #409eff;
  cursor: pointer;
}

.clickable-text:hover {
  text-decoration: underline;
}

.clickable-text.disabled {
  color: #c0c4cc;
  cursor: not-allowed;
}

.clickable-text.disabled:hover {
  text-decoration: none;
}

.th-gear { 
  cursor: pointer; 
}

.billExamine {
  height: 24px;
}

.fistDivider {
  margin: 16px 0;
}

.dialog-content {
  max-height: 60vh;
  overflow: auto;
}

.add-row-btn, .del-row-btn {
  width: 24px;
  height: 24px;
  min-width: 24px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

/* 调整表格样式 */
:deep(.settlement-table-container .el-table) {
  border: none;
}

:deep(.settlement-table-container .el-table th) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
}

:deep(.settlement-table-container .el-table .el-input__inner) {
  border: none;
  background: transparent;
}

:deep(.settlement-table-container .el-table .el-input__inner:focus) {
  background: #fff;
  border: 1px solid #409eff;
}

:deep(.settlement-table-container .el-table .el-input.is-disabled .el-input__inner) {
  background-color: #f5f7fa;
  border-color: #e4e7ed;
  color: #c0c4cc;
  cursor: not-allowed;
}
</style>