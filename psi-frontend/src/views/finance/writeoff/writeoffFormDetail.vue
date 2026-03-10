<template>
    <div class="writeoff-detail-fullscreen">
      <!-- 审核印章 - 在已审核状态下显示 -->
      <div v-if="form.examine === 1" class="examine-seal">
        <img src="/examine.png" alt="已审核" class="seal-image" />
      </div>
      
      <!-- 对话框头部 - 包含标题和操作按钮 -->
      <div class="dialog-header">
        <h2 class="dialog-title">核销单据详情</h2>
        <div class="dialog-actions">
          <el-button-group>
            <!-- 未审核状态显示保存和审核按钮 -->
            <template v-if="form.examine === 0">
              <el-button type="primary" @click="save">保存</el-button>
              <el-button type="success" @click="examine">审核</el-button>
            </template>
            <!-- 已审核状态只显示反审核按钮 -->
            <template v-else>
              <el-button type="warning" @click="examine">反审核</el-button>
            </template>
            <el-button @click="handleClose">关闭</el-button>
          </el-button-group>
        </div>
      </div>
  
      <!-- 主表单区域 -->
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
              :disabled="form.examine === 1"
            />
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
  
        <!-- 单据日期 -->
        <el-form-item label="单据日期" prop="time">
          <el-date-picker
            v-model="form.time"
            value-format="YYYY-MM-DD"
            type="date"
            placeholder="请选择单据日期"
            :disabled="form.examine === 1"
          />
        </el-form-item>
  
        <!-- 单据编号 -->
        <el-form-item label="单据编号" prop="number">
          <el-input
            placeholder="请输入单据编号"
            v-model="form.number"
            clearable
            :disabled="form.examine === 1"
          />
        </el-form-item>
  
        <el-divider class="fistDivider" />
  
        <!-- 核销信息表格区域 -->
        <div class="table-container">
          <div class="table-header">
            <div class="table-title">核销信息</div>
            <!-- 未审核状态显示新增按钮 -->
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
              <!-- 序号列 -->
              <el-table-column type="index" width="60" align="center" />
              
              <!-- 操作列 - 未审核状态显示选择按钮 -->
              <el-table-column 
                label="操作" 
                width="80" 
                align="center"
                v-if="form.examine === 0"
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
              
              <!-- 核销类型列 -->
              <el-table-column 
                label="核销类型" 
                prop="writeOffType" 
                width="150"
              >
                <template #default="{ row }">
                  <span>{{ row.writeOffType || '请选择核销类型' }}</span>
                </template>
              </el-table-column>
              
              <!-- 单据类型列 -->
              <el-table-column 
                label="单据类型" 
                prop="documentType" 
                width="150"
              >
                <template #default="{ row }">
                  <span>{{ row.documentType || '请选择单据类型' }}</span>
                </template>
              </el-table-column>
              
              <!-- 单据日期列 -->
              <el-table-column 
                label="单据日期" 
                prop="documentDate" 
                width="120"
              >
                <template #default="{ row }">
                  <span>{{ row.documentDate || '选择日期' }}</span>
                </template>
              </el-table-column>
              
              <!-- 单据编号列 -->
              <el-table-column 
                label="单据编号" 
                prop="documentNumber" 
                width="180"
              >
                <template #default="{ row }">
                  <span>{{ row.documentNumber || '请输入单据编号' }}</span>
                </template>
              </el-table-column>
              
              <!-- 单据金额列 -->
              <el-table-column 
                label="单据金额" 
                prop="documentAmount" 
                width="120"
                align="right"
              >
                <template #default="{ row }">
                  <span>{{ formatCurrency(row.documentAmount) || '0.00' }}</span>
                </template>
              </el-table-column>
              
              <!-- 已核销金额列 -->
              <el-table-column 
                label="已核销" 
                prop="writtenOff" 
                width="120"
                align="right"
              >
                <template #default="{ row }">
                  <span>{{ formatCurrency(row.writtenOff) || '0.00' }}</span>
                </template>
              </el-table-column>
              
              <!-- 未核销金额列 -->
              <el-table-column 
                label="未核销" 
                prop="notWrittenOff" 
                width="120"
                align="right"
              >
                <template #default="{ row }">
                  <span>{{ formatCurrency(row.notWrittenOff) || '0.00' }}</span>
                </template>
              </el-table-column>
              
              <!-- 核销金额列 -->
              <el-table-column 
                label="核销金额" 
                prop="writeOffAmount" 
                width="120"
                align="right"
              >
                <template #default="{ row }">
                  <span>{{ formatCurrency(row.writeOffAmount) || '0.00' }}</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
  
          <!-- 表格底部信息 -->
          <div class="table-footer">
            <div class="footer-info">
              <span>总条数 {{ totalCount }} | 总核销金额 {{ totalWriteOffAmount }}</span>
            </div>
          </div>
        </div>
  
        <el-divider />
  
        <!-- 底部表单区域 - 使用LineFeed组件实现自动换行 -->
        <LineFeed tag=".el-form-item" :rule="3">
          <!-- 核销类型选择 -->
          <el-form-item label="核销类型">
            <el-select
              v-model="form.writeOffType"
              placeholder="请选择核销类型"
              style="width: 200px"
              :disabled="form.examine === 1"
            >
              <el-option
                v-for="type in writeOffTypes"
                :key="type.value"
                :label="type.label"
                :value="type.value"
              />
            </el-select>
          </el-form-item>
  
          <!-- 总核金额显示 -->
          <el-form-item label="总核金额">
            <el-input
              placeholder="0.00"
              v-model="form.totalWriteOffAmount"
              :disabled="true"
            />
          </el-form-item>
  
          <!-- 总销金额显示 -->
          <el-form-item label="总销金额">
            <el-input
              placeholder="0.00"
              v-model="form.totalSalesAmount"
              :disabled="true"
            />
          </el-form-item>
  
          <!-- 关联人员选择 -->
          <el-form-item label="关联人员">
            <div class="search-select-container">
              <el-input
                v-model="form.peopleName"
                placeholder="请选择关联人员"
                readonly
                style="width: 200px"
                :disabled="form.examine === 1"
              />
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
  
          <!-- 单据附件上传 -->
          <el-form-item label="单据附件">
            <NodUpload
              v-model="uploadFiles"
              :action="uploadAction"
              :disabled="form.examine === 1"
            />
          </el-form-item>
  
          <!-- 备注信息 -->
          <el-form-item label="备注信息">
            <el-input
              placeholder="请输入备注信息"
              v-model="form.data"
              clearable
              :disabled="form.examine === 1"
            />
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

          <!-- 选项卡：预收和应收 -->
          <el-tabs v-model="activeTab" class="document-tabs">
            <!-- 预收选项卡 -->
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
            
            <!-- 应收选项卡 -->
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
  import { ref, reactive, computed, onMounted, nextTick, watch } from 'vue'
  import { ElMessage, ElMessageBox, type ElTable } from 'element-plus'
  import { useHttp } from '@/plugins/http'
  
  // 导入组件
  import LineFeed from '@/components/lib/LineFeed.vue'
  import NodUpload from '@/components/lib/NodUpload.vue'
  import FieldForm from '@/components/lib/FieldForm.vue'
  
  // ==================== 组件接口定义 ====================
  
  // 定义组件接收的props
  const props = defineProps({
    id: {
      type: [Number, String],
      default: 0
    },
    source: {
      type: Object,
      default: null
    }
  })
  
  // 定义组件发出的事件
  const emit = defineEmits(['close', 'saved', 'examined'])
  
  // ==================== 工具函数 ====================
  
  /**
   * 生成唯一ID，用于表格行的key
   * @returns 唯一ID字符串
   */
  const generateUniqid = () => {
    return Date.now().toString(36) + Math.random().toString(36).substr(2)
  }
  
  // ==================== 表单引用和数据定义 ====================
  
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
    examine: number;  // 审核状态：0-未审核，1-已审核
    more: Record<string, any>;
  }
  
  // 表单数据 - 使用reactive创建响应式对象
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
    examine: 0,  // 默认未审核
    more: {},
  })
  
  // 附件上传数据
  const uploadFiles = ref<any[]>([])
  
  // ==================== 业务数据定义 ====================
  
  // 核销类型选项
  const writeOffTypes = [
    { label: '预收冲应收', value: 'preReceiptToReceivable' },
    { label: '预付冲应付', value: 'prePaymentToPayable' },
    { label: '应收冲应付', value: 'receivableToPayable' },
    { label: '销退冲销售', value: 'salesReturnToSales' },
    { label: '购退冲采购', value: 'purchaseReturnToPurchase' }
  ]
  
  // 表格数据
  const tableData = ref<any[]>([])
  const tableKey = ref(0)  // 用于强制表格重新渲染
  
  // ==================== 对话框控制 ====================
  
  // 对话框显示状态
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
  const currentEditingRowIndex = ref(-1)  // 当前编辑的行索引
  
  // ==================== 单据选择相关 ====================
  
  // 单据选择状态
  const activeTab = ref('preReceipt')  // 当前激活的选项卡
  const selectedDocType = ref('收款单')  // 当前选中的单据类型
  const currentPage = ref(1)  // 当前页码
  const pageSize = ref(30)  // 每页大小
  const totalDocuments = ref(1)  // 总单据数

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
  
  // 加载状态
  const loading = ref(false)
  
  // 单据类型按钮配置
  const preReceiptDocTypes = [
    { label: '收款单', value: '收款单' }
  ]
  
  const receivableDocTypes = [
    { label: '销售单', value: '销售单' },
    { label: '销售退货单', value: '销售退货单' },
    { label: '其它收入单', value: '其它收入单' }
  ]
  
  // ==================== 演示数据 ====================
  
  // 客户列表数据
  const customerList = ref([
    { 客户名称: 'OPE', 客户编号: 'OPE1', 客户类别: '常规类别', 客户等级: '常规级别', 所属组织: '默认组织', 备注信息: '这是一个测试客户' },
    { 客户名称: '测试客户5', 客户编号: 'BH0005', 客户类别: '常规类别', 客户等级: '常规级别', 所属组织: '默认组织', 备注信息: 'VIP客户' }
  ])
  
  // 人员列表数据
  const peopleList = ref([
    { 人员名称: 'NTJK_test_DD00', 人员编号: 'DD001', 所属组织: '总', 人员性别: '男', 联系电话: '', 联系地址: '', 身份证号: '', 备注信息: '' },
    { 人员名称: '崔瀚帅', 人员编号: '0023', 所属组织: '默认组织', 人员性别: '男', 联系电话: '', 联系地址: '', 身份证号: '', 备注信息: '' }
  ])
  
  // 单据数据
  const allDocuments = ref([
    { 
      所属组织: '默认组织', 
      单据时间: '2025-08-04', 
      单据编号: 'SKD2508041750258', 
      核销状态: '未核销', 
      单据金额: 100,
      已核销金额: 0,
      未核销金额: 100,
      关联人员: '',
      制单人: '管理员',
      备注信息: '',
      单据类型: '收款单',
      选项卡: 'preReceipt'
    },
    { 
      所属组织: '默认组织', 
      单据时间: '2025-08-05', 
      单据编号: 'XSD2508051750258', 
      核销状态: '未核销', 
      单据金额: 200,
      已核销金额: 50,
      未核销金额: 150,
      关联人员: '张三',
      制单人: '管理员',
      备注信息: '销售订单',
      单据类型: '销售单',
      选项卡: 'receivable'
    },
    { 
      所属组织: '默认组织', 
      单据时间: '2025-08-06', 
      单据编号: 'XSTHD2508061750258', 
      核销状态: '未核销', 
      单据金额: 150,
      已核销金额: 0,
      未核销金额: 150,
      关联人员: '',
      制单人: '李四',
      备注信息: '退货商品',
      单据类型: '销售退货单',
      选项卡: 'receivable'
    }
  ])
  
  // ==================== 计算属性 ====================
  
  /**
   * 过滤后的单据数据 - 根据当前选项卡和单据类型过滤
   */
  const filteredDocuments = computed(() => {
    return allDocuments.value.filter(doc => 
      doc.选项卡 === activeTab.value && doc.单据类型 === selectedDocType.value
    )
  })
  
  /**
   * 上传地址计算属性
   */
  const uploadAction = computed(() => {
    const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
    return `${baseUrl}/imy/upload`
  })
  
  /**
   * 编辑权限计算属性
   */
  const hasEditPermission = computed(() => {
    return true
  })
  
  /**
   * 审核权限计算属性
   */
  const hasExaminePermission = computed(() => {
    return true
  })
  
  // ==================== 状态信息计算 ====================
  
  /**
   * 总条数计算
   */
  const totalCount = computed(() => {
    return tableData.value.length
  })
  
  /**
   * 总核销金额计算
   */
  const totalWriteOffAmount = computed(() => {
    const total = tableData.value.reduce((sum, row) => {
      const amount = parseFloat(row.writeOffAmount) || 0
      return sum + amount
    }, 0)
    form.totalWriteOffAmount = total  // 同步更新表单数据
    return formatCurrency(total)
  })
  
  // ==================== 表单验证规则 ====================
  
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
  
  // ==================== 表格操作方法 ====================
  
  /**
   * 添加初始行
   */
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
  
  /**
   * 新增表格行
   */
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
  
  /**
   * 打开单据选择对话框
   * @param index 行索引
   */
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

  /**
   * 打开筛选对话框
   */
  const openFilterDialog = () => {
    filterDialogVisible.value = true
  }
  
  /**
   * 运行表格处理逻辑
   */
  const runHandleGrid = () => {
    summary()
  }
  
  /**
   * 汇总计算
   */
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
  
  // ==================== 单据选择相关方法 ====================
  
  /**
   * 选择单据类型
   * @param type 单据类型
   */
  const selectDocType = (type: string) => {
    selectedDocType.value = type
  }
  
  /**
   * 处理单据选择
   * @param selection 选中的单据
   */
  const handleDocumentSelection = (selection: any[]) => {
    selectedDocuments.value = selection
  }
  
  /**
   * 确认单据选择
   */
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
      
      // 更新当前编辑行的数据
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
  
  /**
   * 处理分页变化
   * @param page 页码
   */
  const handlePageChange = (page: number) => {
    currentPage.value = page
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
  
  // ==================== 客户选择相关方法 ====================
  
  /**
   * 打开客户选择对话框
   */
  const openCustomerDialog = () => {
    customerDialogVisible.value = true
    nextTick(() => {
      if (customerTableRef.value) {
        customerTableRef.value.clearSelection()
      }
    })
  }
  
  /**
   * 处理客户选择
   * @param selection 选中的客户
   */
  const handleCustomerSelection = (selection: any[]) => {
    currentCustomer.value = selection
  }
  
  /**
   * 确认客户选择
   */
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
  
  // ==================== 人员选择相关方法 ====================
  
  /**
   * 打开人员选择对话框
   */
  const openPeopleDialog = () => {
    peopleDialogVisible.value = true
    nextTick(() => {
      if (peopleTableRef.value) {
        peopleTableRef.value.clearSelection()
      }
    })
  }
  
  /**
   * 处理人员选择
   * @param selection 选中的人员
   */
  const handlePeopleSelection = (selection: any[]) => {
    currentPeople.value = selection
  }
  
  /**
   * 确认人员选择
   */
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
  
  // ==================== 工具函数 ====================
  
  /**
   * 格式化货币显示
   * @param value 金额数值
   * @returns 格式化后的金额字符串
   */
  const formatCurrency = (value: any) => {
    if (!value && value !== 0) return '0.00';
    const num = parseFloat(value);
    if (isNaN(num)) return '0.00';
    return num.toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });
  }
  
  // ==================== 核心业务方法 ====================
  
  /**
   * 保存单据
   */
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
  
      // 实际接口调用
      // const result = await useHttp().post('service/writeOffRecord', submitData)
      // form.id = result.data.id
      
      ElMessage.success('核销单据保存成功!')
      emit('saved')
      console.log('提交数据:', submitData)
    } catch (error) {
      ElMessage.error('[ ERROR ] 服务器响应超时!')
    }
  }
  
  /**
   * 审核/反审核单据
   */
  const examine = async () => {
    try {
      // 检查单据是否已保存
      if (form.id === 0) {
        ElMessage.warning('请先保存单据再进行审核操作')
        return
      }
  
      const action = form.examine === 0 ? '审核' : '反审核'
      const confirmMessage = form.examine === 0 
        ? '确定要审核该单据吗?' 
        : '确定要反审核该单据吗?'
  
      // 确认对话框
      await ElMessageBox.confirm(confirmMessage, '提示', { type: 'warning' })
      
      // 实际接口调用
      // const result = await useHttp().post<any>('service/writeOffRecord/examine', {
      //   parm: [form.id]
      // })
      
      // 更新审核状态
      form.examine = form.examine === 0 ? 1 : 0
      ElMessage.success(`${action}单据成功!`)
      
      // 发出审核状态变化事件，通知父组件
      emit('examined', { 
        id: form.id, 
        examine: form.examine 
      })
    } catch (error) {
      // 用户取消操作不报错
      if (error !== 'cancel') {
        ElMessage.error('[ ERROR ] 服务器响应超时!')
      }
    }
  }
  
  /**
   * 关闭弹窗
   */
  const handleClose = () => {
    emit('close')
  }
  
  // ==================== 数据加载方法 ====================
  
  /**
   * 加载已有数据
   */
  const loadExistingData = async () => {
    try {
      loading.value = true
      
      // 关键修改：优先使用来源数据
      if (props.source) {
        console.log('使用来源数据初始化:', props.source)
        // 使用来源数据初始化表单
        Object.assign(form, {
          id: props.source.id || props.id,
          customer: props.source.customer || null,
          customerName: props.source.customerName || '',
          time: props.source.time || '',
          number: props.source.number || '',
          totalWriteOffAmount: props.source.totalWriteOffAmount || 0,
          totalSalesAmount: props.source.totalSalesAmount || 0,
          writeOffType: props.source.writeOffType || '',
          people: props.source.people || null,
          peopleName: props.source.peopleName || '',
          data: props.source.data || '',
          examine: props.source.examine || 0,  // 关键：使用传递的审核状态
        })
        
        // 如果有表格数据也一并初始化
        if (props.source.tableData) {
          tableData.value = props.source.tableData.map((item: any) => ({
            uniqid: generateUniqid(),
            ...item
          }))
        } else {
          // 使用默认表格数据
          tableData.value = [{
            uniqid: generateUniqid(),
            writeOffType: '预收',
            documentType: '收款单',
            documentDate: '2024-01-10',
            documentNumber: 'SKD20240110001',
            documentAmount: '5000.00',
            writtenOff: '0.00',
            notWrittenOff: '5000.00',
            writeOffAmount: '5000.00',
          }]
        }
      } else {
        // 没有来源数据时使用模拟数据
        console.log('使用模拟数据初始化')
        Object.assign(form, {
          id: props.id,
          customer: '001',
          customerName: '测试客户',
          time: '2024-01-15',
          number: 'HXD202401150001',
          totalWriteOffAmount: 5000,
          totalSalesAmount: 5000,
          writeOffType: 'preReceiptToReceivable',
          people: '001',
          peopleName: '张三',
          data: '测试备注',
          examine: 0
        })
        
        tableData.value = [{
          uniqid: generateUniqid(),
          writeOffType: '预收',
          documentType: '收款单',
          documentDate: '2024-01-10',
          documentNumber: 'SKD20240110001',
          documentAmount: '5000.00',
          writtenOff: '0.00',
          notWrittenOff: '5000.00',
          writeOffAmount: '5000.00',
        }]
      }
      
      loading.value = false
      runHandleGrid()
      
      console.log('初始化完成，当前审核状态:', form.examine)
      
    } catch (error) {
      ElMessage.error('[ ERROR ] 服务器响应超时!')
      loading.value = false
    }
  }
  
  // ==================== 生命周期和监听器 ====================
  
  /**
   * 监听ID变化
   */
  watch(() => props.id, (newId) => {
    if (newId !== undefined) {
      init()
    }
  })
  
  /**
   * 监听来源数据变化
   */
  watch(() => props.source, (newSource) => {
    console.log('来源数据变化:', newSource)
    if (newSource) {
      // 立即更新审核状态
      form.examine = newSource.examine || 0
      console.log('从来源数据更新审核状态:', form.examine)
    }
  }, { deep: true })
  
  /**
   * 初始化函数
   */
  const init = () => {
    console.log('初始化详情页面，ID:', props.id, '来源数据:', props.source)
    
    // 关键修改：如果有来源数据，立即设置审核状态
    if (props.source && props.source.examine !== undefined) {
      form.examine = props.source.examine
      console.log('从来源数据设置审核状态:', form.examine)
    }
    
    if (props.id && props.id !== 0) {
      // 加载已有数据
      loadExistingData()
    } else {
      // 新建单据
      form.time = new Date().toISOString().split('T')[0]
      form.number = 'HXD' + new Date().toISOString().replace(/[-:T.Z]/g, '').slice(2, 15)
      // 初始化表格数据
      addInitialRows()
    }
  }
  
  // 组件挂载时初始化
  onMounted(() => {
    init()
  })
  </script>
  
  <style scoped>
  .writeoff-detail-fullscreen {
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
  
  /* 对话框头部样式 */
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
  
  /* 表单样式 */
  :deep(.el-form) {
    flex: 1;
    padding: 24px;
    overflow-y: auto;
  }
  
  .search-select-container {
    display: flex;
    align-items: center;
  }
  
  /* 表格容器样式 */
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
  
  .fistDivider {
    margin: 16px 0;
  }
  
  /* 对话框内容样式 */
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
  
  :deep(.settlement-table-container .el-table .el-input.is-disabled .el-input__inner) {
    background-color: #f5f7fa;
    border-color: #e4e7ed;
    color: #c0c4cc;
    cursor: not-allowed;
  }
  </style>