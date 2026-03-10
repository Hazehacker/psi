<template>
  <div class="writeoffbill">
    <el-form
      :model="form"
      :rules="rules"
      ref="formRef"
      :inline="true"
      :hide-required-asterisk="true"
      style="height: 100%"
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
          <el-button type="primary" @click="openCustomerDialog" style="margin-left: 8px">
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
        ></el-date-picker>
      </el-form-item>

      <el-form-item label="单据编号" prop="number">
        <el-input
          placeholder="请输入单据编号"
          v-model="form.number"
          clearable
        ></el-input>
      </el-form-item>

      <el-button type="primary" @click="save" style="margin-left: 8px">保存</el-button>
      <el-button type="info" @click="reload" style="margin-left: 8px">刷新</el-button>

      <el-divider class="fistDivider"></el-divider>

      <!-- AG Grid 表格区域 -->
      <div class="table-container">
        <div class="table-header">
          <div class="table-title">核销信息</div>
          <div class="table-actions">
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
            <!-- 序号列 -->
            <el-table-column type="index" width="60" align="center" />
            
            <!-- 操作列 - 点击选择单据 -->
            <el-table-column 
              label="操作" 
              width="80" 
              align="center"
            >
              <template #default="{ row, $index }">
                <el-button 
                  type="primary" 
                  size="small" 
                  @click="openDocumentSelection($index)"
                  class="select-btn"
                >
                  选择
                </el-button>
              </template>
            </el-table-column>
            
            <!-- 核销类型 -->
            <el-table-column 
              label="核销类型" 
              prop="writeOffType" 
              width="150"
            >
              <template #default="{ row }">
                <span>{{ row.writeOffType || '请选择核销类型' }}</span>
              </template>
            </el-table-column>
            
            <!-- 单据类型 -->
            <el-table-column 
              label="单据类型" 
              prop="documentType" 
              width="150"
            >
              <template #default="{ row }">
                <span>{{ row.documentType || '请选择单据类型' }}</span>
              </template>
            </el-table-column>
            
            <!-- 单据日期 -->
            <el-table-column 
              label="单据日期" 
              prop="documentDate" 
              width="120"
            >
              <template #default="{ row }">
                <span>{{ row.documentDate || '选择日期' }}</span>
              </template>
            </el-table-column>
            
            <!-- 单据编号 -->
            <el-table-column 
              label="单据编号" 
              prop="documentNumber" 
              width="180"
            >
              <template #default="{ row }">
                <span>{{ row.documentNumber || '请输入单据编号' }}</span>
              </template>
            </el-table-column>
            
            <!-- 单据金额 -->
            <el-table-column 
              label="单据金额" 
              prop="documentAmount" 
              width="120"
              align="right"
            >
              <template #default="{ row }">
                <span>{{ row.documentAmount || '0.00' }}</span>
              </template>
            </el-table-column>
            
            <!-- 已核销 -->
            <el-table-column 
              label="已核销" 
              prop="writtenOff" 
              width="120"
              align="right"
            >
              <template #default="{ row }">
                <span>{{ row.writtenOff || '0.00' }}</span>
              </template>
            </el-table-column>
            
            <!-- 未核销 -->
            <el-table-column 
              label="未核销" 
              prop="notWrittenOff" 
              width="120"
              align="right"
            >
              <template #default="{ row }">
                <span>{{ row.notWrittenOff || '0.00' }}</span>
              </template>
            </el-table-column>
            
            <!-- 核销金额 -->
            <el-table-column 
              label="核销金额" 
              prop="writeOffAmount" 
              width="120"
              align="right"
            >
              <template #default="{ row }">
                <span>{{ row.writeOffAmount || '0.00' }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="table-footer">
          <div class="footer-info">
            <span>总条数 {{ totalCount }} | 总核销金额 {{ totalWriteOffAmount }}</span>
          </div>
        </div>
      </div>

      <el-divider></el-divider>

      <!-- 底部表单区域 -->
      <LineFeed tag=".el-form-item" :rule="3">
        <el-form-item label="核销类型">
          <el-select
            v-model="form.writeOffType"
            placeholder="请选择核销类型"
            style="width: 200px"
          >
            <el-option
              v-for="type in writeOffTypes"
              :key="type.value"
              :label="type.label"
              :value="type.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="总核金额">
          <el-input
            placeholder="0.00"
            v-model="form.totalWriteOffAmount"
            :disabled="true"
          ></el-input>
        </el-form-item>

        <el-form-item label="总销金额">
          <el-input
            placeholder="0.00"
            v-model="form.totalSalesAmount"
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
            <el-button type="primary" @click="openPeopleDialog" style="margin-left: 8px">
              选择
            </el-button>
          </div>
        </el-form-item>

        <el-form-item label="单据附件">
          <NodUpload
            v-model="uploadFiles"
            :action="uploadAction"
          ></NodUpload>
        </el-form-item>

        <el-form-item label="备注信息">
          <el-input
            placeholder="请输入备注信息"
            v-model="form.data"
            clearable
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

    <!-- 单据选择对话框 -->
    <el-dialog
      v-model="documentDialogVisible"
      title="单据列表"
      width="80%"
      top="5vh"
    >
      <div class="document-dialog-content">
        <!-- 顶部操作栏 -->
        <div class="document-header">
          <div class="document-actions">
            <el-button type="primary" @click="openFilterDialog" size="small">
              筛选
            </el-button>
          </div>
        </div>

        <!-- 选项卡 -->
        <el-tabs v-model="activeTab" class="document-tabs">
          <el-tab-pane label="预收" name="preReceipt">
            <div class="document-type-buttons">
              <el-button 
                v-for="docType in preReceiptDocTypes" 
                :key="docType.value"
                :type="selectedDocType === docType.value ? 'primary' : ''"
                @click="selectDocType(docType.value)"
                size="small"
              >
                {{ docType.label }}
              </el-button>
            </div>
            <div class="tab-content">
              <el-table
                :data="filteredDocuments"
                style="width: 100%"
                @selection-change="handleDocumentSelection"
                ref="documentTableRef"
                size="small"
                border
              >
                <el-table-column type="selection" width="45" />
                <el-table-column prop="所属组织" label="所属组织" width="100" />
                <el-table-column prop="单据时间" label="单据时间" width="100" />
                <el-table-column prop="单据编号" label="单据编号" width="150" />
                <el-table-column prop="核销状态" label="核销状态" width="80" />
                <el-table-column prop="单据金额" label="单据金额" width="100" align="right">
                  <template #default="{ row }">
                    {{ formatCurrency(row.单据金额) }}
                  </template>
                </el-table-column>
                <el-table-column prop="已核销金额" label="已核销金额" width="100" align="right">
                  <template #default="{ row }">
                    {{ formatCurrency(row.已核销金额) }}
                  </template>
                </el-table-column>
                <el-table-column prop="未核销金额" label="未核销金额" width="100" align="right">
                  <template #default="{ row }">
                    {{ formatCurrency(row.未核销金额) }}
                  </template>
                </el-table-column>
                <el-table-column prop="关联人员" label="关联人员" width="100"/>
                <el-table-column prop="制单人" label="制单人" width="100"/>
                <el-table-column prop="备注信息" label="备注信息" width="120" />
              </el-table>
              <div class="pagination-container">
                <el-pagination
                  small
                  :current-page="currentPage"
                  :page-size="pageSize"
                  :total="totalDocuments"
                  :page-sizes="[10, 20, 30, 50]"
                  layout="total, sizes, prev, pager, next, jumper"
                  @size-change="handleSizeChange"
                  @current-change="handlePageChange"
                />
              </div>
            </div>
          </el-tab-pane>
          
          <el-tab-pane label="应收" name="receivable">
            <div class="document-type-buttons">
              <el-button 
                v-for="docType in receivableDocTypes" 
                :key="docType.value"
                :type="selectedDocType === docType.value ? 'primary' : ''"
                @click="selectDocType(docType.value)"
                size="small"
              >
                {{ docType.label }}
              </el-button>
            </div>
            <div class="tab-content">
              <el-table
                :data="filteredDocuments"
                style="width: 100%"
                @selection-change="handleDocumentSelection"
                size="small"
                border
              >
                <el-table-column type="selection" width="45" />
                <el-table-column prop="所属组织" label="所属组织" width="100" />
                <el-table-column prop="单据时间" label="单据时间" width="100" />
                <el-table-column prop="单据编号" label="单据编号" width="150" />
                <el-table-column prop="核销状态" label="核销状态" width="80" />
                <el-table-column prop="单据金额" label="单据金额" width="100" align="right">
                  <template #default="{ row }">
                    {{ formatCurrency(row.单据金额) }}
                  </template>
                </el-table-column>
                <el-table-column prop="已核销金额" label="已核销金额" width="100" align="right">
                  <template #default="{ row }">
                    {{ formatCurrency(row.已核销金额) }}
                  </template>
                </el-table-column>
                <el-table-column prop="未核销金额" label="未核销金额" width="100" align="right">
                  <template #default="{ row }">
                    {{ formatCurrency(row.未核销金额) }}
                  </template>
                </el-table-column>
                <el-table-column prop="关联人员" label="关联人员" width="100"/>
                <el-table-column prop="制单人" label="制单人" width="100"/>
                <el-table-column prop="备注信息" label="备注信息" width="120" />
              </el-table>
              <div class="pagination-container">
                <el-pagination
                  small
                  :current-page="currentPage"
                  :page-size="pageSize"
                  :total="totalDocuments"
                  :page-sizes="[10, 20, 30, 50]"
                  layout="total, sizes, prev, pager, next, jumper"
                  @size-change="handleSizeChange"
                  @current-change="handlePageChange"
                />
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="documentDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="confirmDocument">添加</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 筛选条件对话框 -->
    <el-dialog
      v-model="filterDialogVisible"
      title="筛选条件"
      width="600px"
      append-to-body
    >
      <div class="filter-dialog-content">
        <el-form :model="filterForm" label-width="100px" class="filter-form">
          <el-form-item label="单据编号">
            <el-input
              v-model="filterForm.documentNumber"
              placeholder="请输入单据编号"
              clearable
            />
          </el-form-item>
          
          <el-form-item label="核销状态">
            <el-select
              v-model="filterForm.writeOffStatus"
              placeholder="请选择核销状态"
              clearable
              style="width: 100%"
            >
              <el-option label="未核销" value="未核销" />
              <el-option label="部分核销" value="部分核销" />
              <el-option label="已核销" value="已核销" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="日期范围">
            <div class="date-range-container">
              <el-date-picker
                v-model="filterForm.startDate"
                type="date"
                placeholder="开始日期"
                value-format="YYYY-MM-DD"
                style="width: 48%; margin-right: 4%"
              />
              <el-date-picker
                v-model="filterForm.endDate"
                type="date"
                placeholder="结束日期"
                value-format="YYYY-MM-DD"
                style="width: 48%"
              />
            </div>
          </el-form-item>
          
          <el-form-item label="制单人">
            <div class="search-select-container">
              <el-input
                v-model="filterForm.creatorName"
                placeholder="请选择制单人"
                readonly
                style="width: calc(100% - 68px)"
              />
              <el-button 
                type="primary" 
                @click="openUserDialog" 
                style="margin-left: 8px"
              >
                选择
              </el-button>
            </div>
          </el-form-item>
          
          <el-form-item label="备注信息">
            <el-input
              v-model="filterForm.remark"
              placeholder="请输入备注信息"
              clearable
            />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <el-button @click="resetFilter">重置</el-button>
        <el-button @click="filterDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleFilter">查询</el-button>
      </template>
    </el-dialog>

    <!-- 用户选择对话框 -->
    <el-dialog
      v-model="userDialogVisible"
      title="选择制单人"
      width="60%"
      append-to-body
    >
      <div class="dialog-content">
        <el-table
          :data="userList"
          style="width: 100%"
          @selection-change="handleUserSelection"
          ref="userTableRef"
        >
          <el-table-column type="selection" width="45" />
          <el-table-column prop="userName" label="用户名称" />
          <el-table-column prop="userCode" label="用户编号" />
          <el-table-column prop="organization" label="所属组织" />
          <el-table-column prop="position" label="职位" />
        </el-table>
        <div class="pagination-container">
          <el-pagination
            small
            :current-page="userCurrentPage"
            :page-size="userPageSize"
            :total="totalUsers"
            layout="total, prev, pager, next, jumper"
            @current-change="handleUserPageChange"
          />
        </div>
      </div>
      <template #footer>
        <el-button @click="userDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmUser">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
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
  source: {
    default: null,
  }
})

// 表单引用
const formRef = ref()
const customerTableRef = ref<InstanceType<typeof ElTable>>()
const peopleTableRef = ref<InstanceType<typeof ElTable>>()
const documentTableRef = ref<InstanceType<typeof ElTable>>()
const userTableRef = ref<InstanceType<typeof ElTable>>()

// 定义表单数据的接口
interface FormData {
  id: number;
  customer: string | null;
  customerName: string;
  time: string;
  number: string;
  totalWriteOffAmount: number;
  totalSalesAmount: number;
  writeOffType: string;
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
  totalWriteOffAmount: 0,
  totalSalesAmount: 0,
  writeOffType: '',
  people: null,
  peopleName: '',
  file: [],
  data: '',
  examine: 0,
  more: {},
})

// 附件上传
const uploadFiles = ref<any[]>([])

// 核销类型选项
const writeOffTypes = [
  { label: '预收冲应收', value: 'preReceiptToReceivable' },
  { label: '预付冲应付', value: 'prePaymentToPayable' },
  { label: '应收冲应付', value: 'receivableToPayable' },
  { label: '销退冲销售', value: 'salesReturnToSales' },
  { label: '购退冲采购', value: 'purchaseReturnToPurchase' }
]

// 操作按钮
const operations = [
  { label: '预收冲应收', value: 'preReceiptToReceivable' },
  { label: '预付冲应付', value: 'prePaymentToPayable' },
  { label: '应收冲应付', value: 'receivableToPayable' },
  { label: '销退冲销售', value: 'salesReturnToSales' },
  { label: '购退冲采购', value: 'purchaseReturnToPurchase' }
]

// 表格数据
const tableData = ref<any[]>([])
const tableKey = ref(0)

// 对话框控制
const customerDialogVisible = ref(false)
const peopleDialogVisible = ref(false)
const documentDialogVisible = ref(false)
const userDialogVisible = ref(false)
const filterDialogVisible = ref(false)

// 当前选中的项
const currentCustomer = ref<any[]>([])
const currentPeople = ref<any[]>([])
const selectedDocuments = ref<any[]>([])
const selectedUsers = ref<any[]>([])
const currentEditingRowIndex = ref(-1)

// 单据选择相关
const activeTab = ref('preReceipt')
const selectedDocType = ref('收款单')
const currentPage = ref(1)
const pageSize = ref(30)
const totalDocuments = ref(1)

// 筛选表单数据
const filterForm = reactive({
  documentNumber: '',
  writeOffStatus: '',
  startDate: '',
  endDate: '',
  creatorName: '',
  creatorId: '',
  remark: ''
})

// 用户选择相关
const userList = ref<any[]>([])
const userCurrentPage = ref(1)
const userPageSize = ref(10)
const totalUsers = ref(0)

// 单据类型按钮
const preReceiptDocTypes = [
  { label: '收款单', value: '收款单' }
]

const receivableDocTypes = [
  { label: '销售单', value: '销售单' },
  { label: '销售退货单', value: '销售退货单' },
  { label: '其它收入单', value: '其它收入单' }
]

// 演示数据 
const customerList = ref([
  { 客户名称: 'OPE', 客户编号: 'OPE1', 客户类别: '常规类别', 客户等级: '常规级别', 所属组织: '默认组织', 备注信息: '这是一个测试客户' },
  { 客户名称: '测试客户5', 客户编号: 'BH0005', 客户类别: '常规类别', 客户等级: '常规级别', 所属组织: '默认组织', 备注信息: 'VIP客户' }
])

const peopleList = ref([
  { 人员名称: 'NTJK_test_DD00', 人员编号: 'DD001', 所属组织: '总', 人员性别: '男', 联系电话: '', 联系地址: '', 身份证号: '', 备注信息: '' },
  { 人员名称: '崔瀚帅', 人员编号: '0023', 所属组织: '默认组织', 人员性别: '男', 联系电话: '', 联系地址: '', 身份证号: '', 备注信息: '' }
])

// 单据数据
const allDocuments = ref([
  { 
    所属组织: '默认组织', 
    单据时间: '2025-10-14', 
    单据编号: 'SKD2510142122160', 
    核销状态: '未核销', 
    单据金额: 123,
    已核销金额: 0,
    未核销金额: 123,
    关联人员: '',
    制单人: '张三',
    备注信息: '',
    单据类型: '收款单',
    选项卡: 'preReceipt'
  },
  { 
    所属组织: '默认组织', 
    单据时间: '2025-07-11', 
    单据编号: 'SKD2507111537078', 
    核销状态: '未核销', 
    单据金额: 105,
    已核销金额: 0,
    未核销金额: 105,
    关联人员: '',
    制单人: '李四',
    备注信息: '',
    单据类型: '收款单',
    选项卡: 'preReceipt'
  }
])

// 过滤后的单据数据
const filteredDocuments = computed(() => {
  return allDocuments.value.filter(doc => 
    doc.选项卡 === activeTab.value && doc.单据类型 === selectedDocType.value
  )
})

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

const totalWriteOffAmount = computed(() => {
  const total = tableData.value.reduce((sum, row) => {
    const amount = parseFloat(row.writeOffAmount) || 0
    return sum + amount
  }, 0)
  form.totalWriteOffAmount = total
  return total.toFixed(2)
})

// 添加初始行
const addInitialRows = () => {
  const initialRows = [
    {
      uniqid: generateUniqid(),
      writeOffType: '',
      documentType: '',
      documentDate: '',
      documentNumber: '',
      documentAmount: '',
      writtenOff: '0.00',
      notWrittenOff: '0.00',
      writeOffAmount: '',
    }
  ]
  tableData.value = [...initialRows]
  runHandleGrid()
}

const addGridRow = () => {
  const newRow = {
    uniqid: generateUniqid(),
    writeOffType: '',
    documentType: '',
    documentDate: '',
    documentNumber: '',
    documentAmount: '',
    writtenOff: '0.00',
    notWrittenOff: '0.00',
    writeOffAmount: '',
  }
  tableData.value = [...tableData.value, newRow]
  runHandleGrid()
}

// 打开单据选择对话框
const openDocumentSelection = (index: number) => {
  currentEditingRowIndex.value = index
  documentDialogVisible.value = true
  loadDocuments() // 加载单据数据
  nextTick(() => {
    if (documentTableRef.value) {
      documentTableRef.value.clearSelection()
    }
  })
}

// 打开筛选对话框
const openFilterDialog = () => {
  filterDialogVisible.value = true
}

const runHandleGrid = () => {
  summary()
}

const summary = () => {
  let totalWriteOff = 0
  tableData.value.forEach((item) => {
    totalWriteOff += parseFloat(item.writeOffAmount) || 0
    
    // 自动计算未核销金额
    const documentAmount = parseFloat(item.documentAmount) || 0
    const writtenOff = parseFloat(item.writtenOff) || 0
    const writeOffAmount = parseFloat(item.writeOffAmount) || 0
    item.notWrittenOff = (documentAmount - writtenOff - writeOffAmount).toFixed(2)
  })
  form.totalWriteOffAmount = totalWriteOff
}

// 处理操作按钮点击
const handleOperationClick = (type: string) => {
  form.writeOffType = type
}

// 选择单据类型
const selectDocType = (type: string) => {
  selectedDocType.value = type
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  loadDocuments()
}

// 筛选方法
const handleFilter = () => {
  currentPage.value = 1
  loadDocuments()
  filterDialogVisible.value = false
}

// 重置筛选
const resetFilter = () => {
  Object.assign(filterForm, {
    documentNumber: '',
    writeOffStatus: '',
    startDate: '',
    endDate: '',
    creatorName: '',
    creatorId: '',
    remark: ''
  })
  currentPage.value = 1
  loadDocuments()
}

// 打开用户选择对话框
const openUserDialog = async () => {
  userDialogVisible.value = true
  await loadUserList()
  nextTick(() => {
    if (userTableRef.value) {
      userTableRef.value.clearSelection()
    }
  })
}

// 加载用户列表
const loadUserList = async () => {
  try {
    // 调用公共接口获取用户列表
    // const result = await useHttp().post('service/user/list', {
    //   page: userCurrentPage.value,
    //   size: userPageSize.value
    // })
    
    // 模拟数据
    userList.value = [
      { userName: '张三', userCode: 'ZS001', organization: '默认组织', position: '销售经理' },
      { userName: '李四', userCode: 'LS002', organization: '默认组织', position: '财务专员' },
      { userName: '王五', userCode: 'WW003', organization: '默认组织', position: '客服主管' }
    ]
    totalUsers.value = 3
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  }
}

// 处理用户选择
const handleUserSelection = (selection: any[]) => {
  selectedUsers.value = selection
}

// 确认用户选择
const confirmUser = () => {
  if (selectedUsers.value.length > 0) {
    const user = selectedUsers.value[0]
    filterForm.creatorName = user.userName
    filterForm.creatorId = user.userCode
    userDialogVisible.value = false
  } else {
    ElMessage.warning('请选择制单人')
  }
}

// 用户分页变化
const handleUserPageChange = (page: number) => {
  userCurrentPage.value = page
  loadUserList()
}

// 加载单据数据（模拟）
const loadDocuments = () => {
  // 在实际应用中，这里应该调用接口，根据筛选条件获取数据
  // 这里使用模拟数据
  allDocuments.value = [
    { 
      所属组织: '默认组织', 
      单据时间: '2025-10-14', 
      单据编号: 'SKD2510142122160', 
      核销状态: '未核销', 
      单据金额: 123,
      已核销金额: 0,
      未核销金额: 123,
      关联人员: '',
      制单人: '张三',
      备注信息: '',
      单据类型: '收款单',
      选项卡: 'preReceipt'
    },
    { 
      所属组织: '默认组织', 
      单据时间: '2025-07-11', 
      单据编号: 'SKD2507111537078', 
      核销状态: '未核销', 
      单据金额: 105,
      已核销金额: 0,
      未核销金额: 105,
      关联人员: '',
      制单人: '李四',
      备注信息: '',
      单据类型: '收款单',
      选项卡: 'preReceipt'
    }
  ]
  totalDocuments.value = allDocuments.value.length
}

// 初始化
const init = () => {
  if (props.source == null) {
    form.time = new Date().toISOString().split('T')[0]
    form.number = 'HXD' + new Date().toISOString().replace(/[-:T.Z]/g, '').slice(2, 15)
    // 初始化表格数据
    addInitialRows()
  } else {
    loadExistingData()
  }
}

// 客户选择相关方法
const openCustomerDialog = () => {
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

// 单据选择相关方法
const handleDocumentSelection = (selection: any[]) => {
  selectedDocuments.value = selection
}

const confirmDocument = () => {
  if (selectedDocuments.value.length > 0 && currentEditingRowIndex.value !== -1) {
    const document = selectedDocuments.value[0]
    const newData = [...tableData.value]
    
    // 根据选中的单据类型设置核销类型
    let writeOffType = ''
    if (activeTab.value === 'preReceipt') {
      writeOffType = '预收'
    } else if (activeTab.value === 'receivable') {
      writeOffType = '应收'
    }
    
    newData[currentEditingRowIndex.value] = {
      ...newData[currentEditingRowIndex.value],
      writeOffType: writeOffType,
      documentType: document.单据类型,
      documentDate: document.单据时间,
      documentNumber: document.单据编号,
      documentAmount: document.单据金额.toString(),
      writtenOff: document.已核销金额.toString(),
      notWrittenOff: document.未核销金额.toString(),
      writeOffAmount: document.未核销金额.toString()
    }
    
    tableData.value = newData
    documentDialogVisible.value = false
    currentEditingRowIndex.value = -1
    runHandleGrid()
  } else {
    ElMessage.warning('请选择单据')
  }
}

const handlePageChange = (page: number) => {
  currentPage.value = page
}

// 格式化货币
const formatCurrency = (value: number) => {
  if (!value && value !== 0) return '0.00';
  const num = parseFloat(value.toString());
  if (isNaN(num)) return '0.00';
  return num.toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });
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
      info: tableData.value.map(item => ({
        writeOffType: item.writeOffType,
        documentType: item.documentType,
        documentDate: item.documentDate,
        documentNumber: item.documentNumber,
        documentAmount: item.documentAmount,
        writtenOff: item.writtenOff,
        notWrittenOff: item.notWrittenOff,
        writeOffAmount: item.writeOffAmount,
      }))
    }

    ElMessage.success('核销单据保存成功!')
    console.log('提交数据:', submitData)
  } catch (error) {
    ElMessage.error('[ ERROR ] 服务器响应超时!')
  }
}

// 审核/反审核
const examine = async () => {
  try {
    form.examine = form.examine == 0 ? 1 : 0
    ElMessage.success((form.examine == 0 ? '反审核' : '审核') + '单据成功!')
  } catch (error) {
    ElMessage.error('[ ERROR ] 服务器响应超时!')
  }
}

// 页面刷新
const reload = () => {
  init()
  ElMessage.success('页面刷新成功!')
}

// 加载已有数据
const loadExistingData = async () => {
  try {
    ElMessage.info('加载已有数据')
    runHandleGrid()
  } catch (error) {
    ElMessage.error('[ ERROR ] 服务器响应超时!')
  }
}

// 初始化
onMounted(() => {
  init()
})
</script>

<style scoped>
.writeoffbill {
  height: 100%;
  padding: 16px;
  display: flex;
  flex-direction: column;
}

.search-select-container {
  display: flex;
  align-items: center;
}

.operation-buttons {
  margin: 16px 0;
  display: flex;
  gap: 8px;
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

.document-dialog-content {
  height: 70vh;
  display: flex;
  flex-direction: column;
}

/* 单据列表头部 */
.document-header {
  padding: 12px 16px;
  background: #f8f9fa;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  justify-content: flex-start;
}

.document-actions {
  display: flex;
  gap: 8px;
}

.document-tabs {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.document-type-buttons {
  padding: 12px 16px;
  background: #f8f9fa;
  border-bottom: 1px solid #e8e8e8;
}

.document-type-buttons .el-button {
  margin-right: 8px;
  margin-bottom: 4px;
}

.document-tabs :deep(.el-tabs__content) {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.document-tabs :deep(.el-tab-pane) {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.tab-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 12px 16px;
  background: #f8f9fa;
  border-top: 1px solid #e8e8e8;
}

.page-info {
  font-size: 14px;
  color: #606266;
}

.select-btn {
  width: 60px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 筛选对话框样式 */
.filter-dialog-content {
  padding: 20px 0;
}

.filter-form .el-form-item {
  margin-bottom: 20px;
}

.date-range-container {
  display: flex;
  justify-content: space-between;
}

/* 调整表格样式为紧凑型 */
.document-tabs :deep(.el-table) {
  font-size: 12px;
}

.document-tabs :deep(.el-table th) {
  padding: 8px 4px;
  background-color: #f5f7fa;
}

.document-tabs :deep(.el-table td) {
  padding: 8px 4px;
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
</style>