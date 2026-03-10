<!--
 * @Description: 系统设置页面
-->
<template>
  <div class="system-settings-demo">
    <div class="settings-container">
      <!-- 左侧导航菜单 -->
      <div class="settings-nav">
        <div
          v-for="tab in tabs"
          :key="tab.name"
          :class="['nav-item', { active: currentTab === tab.name }]"
          @click="currentTab = tab.name"
        >
          {{ tab.label }}
        </div>
      </div>

      <!-- 右侧内容区 -->
      <div class="settings-content">
        <div class="content-header">
          <h2>{{ getCurrentTabLabel() }}</h2>
          <div>
            <el-button type="primary" @click="handleSave">保存设置</el-button>
          </div>
        </div>

        <div class="content-body">
          <!-- 基础信息 -->
          <div v-if="currentTab === 'basic'" class="basic-info-section">
            <el-form :model="formData.basic" label-width="100px">
              <el-form-item label="系统名称" required>
                <el-input v-model="formData.basic.systemName" placeholder="请输入系统名称" />
              </el-form-item>
              <el-form-item label="公司名称" required>
                <el-input v-model="formData.basic.companyName" placeholder="请输入公司名称" />
              </el-form-item>
              <el-form-item label="备案信息">
                <el-input v-model="formData.basic.filingInfo" placeholder="请输入备案信息" />
              </el-form-item>
              <el-form-item label="公告信息">
                <el-input
                  v-model="formData.basic.notice"
                  type="textarea"
                  :rows="5"
                  placeholder="请输入公告信息"
                  resize="vertical"
                />
              </el-form-item>
            </el-form>
          </div>

          <!-- 功能参数 -->
          <div v-else-if="currentTab === 'function'" class="function-params-section">
            <el-table :data="functionTableDataArr" border stripe>
              <el-table-column prop="name" label="功能名称" width="200" />
              <el-table-column label="功能配置" width="300">
                <template #default="{ row }">
                  <el-switch 
                    v-if="row.key === 'autoAudit'" 
                    v-model="row.value" 
                    @change="() => handleFunctionChange(row)" 
                  />
                  <div v-else-if="row.key === 'enableTax'" class="tax-config">
                    <el-switch 
                      v-model="row.value" 
                      @change="() => handleFunctionChange(row)" 
                    />
                    <span class="tax-label">增值税税率</span>
                    <el-input 
                      v-model="row.extra" 
                      type="number" 
                      :min="0" 
                      :max="100" 
                      class="tax-input" 
                      @change="() => handleFunctionChange(row)" 
                      @input="() => handleFunctionChange(row)"
                    />
                    <span>%</span>
                  </div>
                  <el-switch 
                    v-else-if="row.key === 'allowNegativeInventory'" 
                    v-model="row.value" 
                    @change="() => handleFunctionChange(row)" 
                  />
                  <el-select 
                    v-else-if="row.key === 'pricingMethod'" 
                    v-model="row.value" 
                    @change="() => handleFunctionChange(row)"
                  >
                    <el-option label="先进先出" value="fifo" />
                    <el-option label="基础计价" value="base" />
                    <el-option label="移动平均" value="ma" />
                  </el-select>
                  <el-select 
                    v-else-if="row.key === 'accountingType'" 
                    v-model="row.value" 
                    @change="() => handleFunctionChange(row)"
                  >
                    <el-option label="分仓核算" value="warehouse" />
                    <el-option label="总仓核算" value="headquarters" />
                  </el-select>
                  <el-select 
                    v-else-if="row.key === 'costRule'" 
                    v-model="row.value" 
                    @change="() => handleFunctionChange(row)"
                  >
                    <el-option label="结存结余" value="balance" />
                    <el-option label="辅助属性" value="attr" />
                    <el-option label="批次日期" value="batch" />
                    <el-option label="属性批次" value="aab" />
                  </el-select>
                  <el-select 
                    v-else-if="row.key === 'quantityDecimal'" 
                    v-model="row.value" 
                    @change="() => handleFunctionChange(row)"
                  >
                    <el-option label="0位小数" :value="0" />
                    <el-option label="1位小数" :value="1" />
                    <el-option label="2位小数" :value="2" />
                    <el-option label="3位小数" :value="3" />
                    <el-option label="4位小数" :value="4" />
                  </el-select>
                  <el-select 
                    v-else-if="row.key === 'amountDecimal'" 
                    v-model="row.value" 
                    @change="() => handleFunctionChange(row)"
                  >
                    <el-option label="2位小数" :value="2" />
                    <el-option label="3位小数" :value="3" />
                    <el-option label="4位小数" :value="4" />
                  </el-select>
                  <el-input-number 
                    v-else-if="row.key === 'reportDays'" 
                    v-model="row.value" 
                    :min="0" 
                    :precision="0" 
                    style="width: 120px" 
                    @change="() => handleFunctionChange(row)"
                  />
                </template>
              </el-table-column>
              <el-table-column prop="description" label="功能说明" />
            </el-table>
          </div>

          <!-- 物流配置 -->
          <div v-else-if="currentTab === 'logistics'" class="logistics-section">
            <el-table :data="formData.logistics" border stripe>
              <el-table-column prop="key" label="物流标识" width="150" />
              <el-table-column prop="name" label="物流名称" width="150" />
              <el-table-column label="启用状态" width="150">
                <template #default="{ row }">
                  <el-switch v-model="row.enabled" :disabled="row.key === 'auto'" @change="handleLogisticsChange" />
                </template>
              </el-table-column>
              <el-table-column label="相关操作" width="120">
                <template #header>
                  <el-icon :size="20" style="cursor: pointer" @click="handleAddLogistics">
                    <Plus />
                  </el-icon>
                </template>
                <template #default="{ row }">
                  <el-icon v-if="row.key !== 'auto'" :size="18" style="cursor: pointer; color: #f56c6c" @click="handleDeleteLogistics(row)">
                    <Delete />
                  </el-icon>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 列表配置 -->
          <InputListConfig v-else-if="currentTab === 'brand'" v-model="formData.brand" label="商品品牌" placeholder="请输入品牌名称" />
          <InputListConfig v-else-if="currentTab === 'unit'" v-model="formData.unit" label="计量单位" placeholder="请输入计量单位名称" :reserved-items="['多单位', '-1']" />
          <InputListConfig v-else-if="currentTab === 'customer-category'" v-model="formData.customerCategory" label="客户类别" placeholder="请输入客户类别名称" :protected-items="['常规类别']" :reserved-items="['常规类别']" />
          <InputListConfig v-else-if="currentTab === 'customer-grade'" v-model="formData.customerGrade" label="客户等级" placeholder="请输入客户等级名称" :protected-items="['常规等级']" :reserved-items="['常规等级']" />
          <InputListConfig v-else-if="currentTab === 'supplier-category'" v-model="formData.supplierCategory" label="供应商类别" placeholder="请输入供应商类别名称" :protected-items="['常规类别']" :reserved-items="['常规类别']" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import MyForm from '@/components/myform/MyForm.vue'
import MyTable from '@/components/mytable/MyTable.vue'
import InputListConfig from './components/InputListConfig.vue'
import { getSystemSettings, saveSystemSettings, SystemConfigTransformer } from '@/apis/advancedSettings/systemSettings'
import type { MyFormProps } from '@/components/myform/type'
import type { MyTableColumn, PageDTO } from '@/components/mytable/type'

// 定义表单数据类型
interface BasicConfig {
  systemName: string
  companyName: string
  filingInfo: string
  notice: string
}

interface FunctionConfig {
  autoAudit: boolean
  enableTax: boolean
  taxRate: number
  allowNegativeInventory: boolean
  pricingMethod: string
  accountingType: string
  costRule: string
  quantityDecimal: number
  amountDecimal: number
  reportDays: number
}

interface LogisticsConfig {
  key: string
  name: string
  enabled: boolean
}

interface SystemSettingsFormData {
  basic: BasicConfig
  function: FunctionConfig
  logistics: LogisticsConfig[]
  brand: string[]
  unit: string[]
  customerCategory: string[]
  customerGrade: string[]
  supplierCategory: string[]
}

// 表单数据
const formData = reactive<SystemSettingsFormData>({
  basic: {
    systemName: '',
    companyName: '',
    filingInfo: '',
    notice: ''
  },
  function: {
    autoAudit: false,
    enableTax: false,
    taxRate: 13,
    allowNegativeInventory: false,
    pricingMethod: 'fifo',
    accountingType: 'warehouse',
    costRule: 'balance',
    quantityDecimal: 2,
    amountDecimal: 2,
    reportDays: 365
  },
  logistics: [
    { key: 'auto', name: '自动物流', enabled: true }
  ],
  brand: [],
  unit: [],
  customerCategory: [],
  customerGrade: [],
  supplierCategory: []
})

// 左侧标签页
const tabs = [
  { name: 'basic', label: '基础信息' },
  { name: 'function', label: '功能参数' },
  { name: 'logistics', label: '物流配置' },
  { name: 'brand', label: '商品品牌' },
  { name: 'unit', label: '计量单位' },
  { name: 'customer-category', label: '客户类别' },
  { name: 'customer-grade', label: '客户等级' },
  { name: 'supplier-category', label: '供应商类别' }
]
const currentTab = ref('basic')

// 功能参数表格数据
const functionRows = ref([
  {
    key: 'autoAudit',
    name: '自动审核',
    value: formData.function.autoAudit,
    type: 'switch',
    description: '启用后单据模块将自动审核'
  },
  {
    key: 'enableTax',
    name: '启用税金',
    value: formData.function.enableTax,
    extra: formData.function.taxRate,
    description: '增值税税率'
  },
  {
    key: 'allowNegativeInventory',
    name: '允许负库存',
    value: formData.function.allowNegativeInventory,
    type: 'switch',
    description: '启用后商品库存将允许为负数'
  },
  {
    key: 'pricingMethod',
    name: '计价方法',
    value: formData.function.pricingMethod,
    type: 'select',
    options: [
      { label: '先进先出', value: 'fifo' },
      { label: '基础计价', value: 'base' },
      { label: '移动平均', value: 'ma' }
    ],
    description: '根据实际情况选择相应计价方法'
  },
  {
    key: 'accountingType',
    name: '核算类型',
    value: formData.function.accountingType,
    type: 'select',
    options: [
      { label: '分仓核算', value: 'warehouse' },
      { label: '总仓核算', value: 'headquarters' }
    ],
    description: '根据实际情况选择相应核算类型'
  },
  {
    key: 'costRule',
    name: '成本规则',
    value: formData.function.costRule,
    type: 'select',
    options: [
      { label: '结存结余', value: 'balance' },
      { label: '辅助属性', value: 'attr' },
      { label: '批次日期', value: 'batch' },
      { label: '属性批次', value: 'aab' }
    ],
    description: '根据实际情况选择相应成本规则'
  },
  {
    key: 'quantityDecimal',
    name: '数量位数',
    value: formData.function.quantityDecimal,
    type: 'select',
    options: [
      { label: '0位小数', value: 0 },
      { label: '1位小数', value: 1 },
      { label: '2位小数', value: 2 },
      { label: '3位小数', value: 3 },
      { label: '4位小数', value: 4 }
    ],
    description: '全局数量小数位数范围'
  },
  {
    key: 'amountDecimal',
    name: '金额位数',
    value: formData.function.amountDecimal,
    type: 'select',
    options: [
      { label: '2位小数', value: 2 },
      { label: '3位小数', value: 3 },
      { label: '4位小数', value: 4 }
    ],
    description: '全局金额小数位数范围'
  },
  {
    key: 'reportDays',
    name: '报表天数',
    value: formData.function.reportDays,
    type: 'number',
    description: '全局报表统计天数范围'
  }
])

// 功能参数表格数据计算属性
const functionTableDataArr = computed(() => {
  // 每次计算时同步最新的表单数据到表格数据
  const rows = functionRows.value.map(row => {
    const newRow = { ...row }
    switch (row.key) {
      case 'autoAudit':
        newRow.value = formData.function.autoAudit
        break
      case 'enableTax':
        newRow.value = formData.function.enableTax
        newRow.extra = formData.function.taxRate
        break
      case 'allowNegativeInventory':
        newRow.value = formData.function.allowNegativeInventory
        break
      case 'pricingMethod':
        newRow.value = formData.function.pricingMethod
        break
      case 'accountingType':
        newRow.value = formData.function.accountingType
        break
      case 'costRule':
        newRow.value = formData.function.costRule
        break
      case 'quantityDecimal':
        newRow.value = formData.function.quantityDecimal
        break
      case 'amountDecimal':
        newRow.value = formData.function.amountDecimal
        break
      case 'reportDays':
        newRow.value = formData.function.reportDays
        break
    }
    return newRow
  })
  return rows
})

// 功能参数表格列配置
const functionColumns: MyTableColumn[] = [
  { prop: 'name', label: '功能名称', width: '200' },
  { prop: 'config', label: '功能配置', width: '300' },
  { prop: 'description', label: '功能说明' }
]

// 物流配置表格数据
const logisticsTableData = computed<PageDTO<any>>(() => ({
  total: formData.logistics.length,
  pageIndex: 1,
  pageSize: formData.logistics.length,
  rows: formData.logistics
}))

// 物流配置表格列配置
const logisticsColumns: MyTableColumn[] = [
  { prop: 'key', label: '物流标识', width: '150' },
  { prop: 'name', label: '物流名称', width: '150' },
  { prop: 'enabled', label: '启用状态', width: '150' },
  { prop: 'operate', label: '操作', width: '120' }
]

// 工具函数
const isListConfig = (prop: string) => {
  const listConfigs = ['brand', 'unit', 'customerCategory', 'customerGrade', 'supplierCategory']
  return listConfigs.indexOf(prop) !== -1
}

const getListConfigTitle = (prop: string) => {
  const titles: Record<string, string> = {
    brand: '商品品牌',
    unit: '计量单位',
    customerCategory: '客户类别',
    customerGrade: '客户等级',
    supplierCategory: '供应商类别'
  }
  return titles[prop] || prop
}

const getProtectedItems = (prop: string) => {
  const protectedItems: Record<string, string[]> = {
    customerCategory: ['常规类别'],
    customerGrade: ['常规等级'],
    supplierCategory: ['常规类别']
  }
  return protectedItems[prop] || []
}

const getReservedItems = (prop: string) => {
  const reservedItems: Record<string, string[]> = {
    unit: ['多单位', '-1'],
    customerCategory: ['常规类别'],
    customerGrade: ['常规等级'],
    supplierCategory: ['常规类别']
  }
  return reservedItems[prop] || []
}

// 事件处理函数
const getCurrentTabLabel = () => {
  const tab = tabs.find((t) => t.name === currentTab.value)
  return tab ? tab.label : '系统设置'
}

// 测试当前数据状态
const handleTestData = () => {
  console.log('🧪 当前表单数据:', JSON.parse(JSON.stringify(formData)))
  console.log('🧪 当前功能参数表格数据:', functionTableDataArr.value)
}

// 功能参数变更处理
const handleFunctionChange = (row: any) => {
  console.log('功能参数变更:', row.key, '值:', row.value, '额外值:', row.extra)
  
  switch (row.key) {
    case 'autoAudit':
      formData.function.autoAudit = Boolean(row.value)
      break
    case 'enableTax':
      formData.function.enableTax = Boolean(row.value)
      if (row.extra !== undefined && row.extra !== null) {
        formData.function.taxRate = Number(row.extra) || 13
      }
      break
    case 'allowNegativeInventory':
      formData.function.allowNegativeInventory = Boolean(row.value)
      break
    case 'pricingMethod':
      formData.function.pricingMethod = String(row.value || 'fifo')
      break
    case 'accountingType':
      formData.function.accountingType = String(row.value || 'warehouse')
      break
    case 'costRule':
      formData.function.costRule = String(row.value || 'balance')
      break
    case 'quantityDecimal':
      formData.function.quantityDecimal = Number(row.value) || 2
      break
    case 'amountDecimal':
      formData.function.amountDecimal = Number(row.value) || 2
      break
    case 'reportDays':
      formData.function.reportDays = Number(row.value) || 365
      break
  }
  
  // 立即更新表格数据
  updateFunctionRows()
}

// 添加更新函数行数据的函数
const updateFunctionRows = () => {
  functionRows.value.forEach(row => {
    switch (row.key) {
      case 'autoAudit':
        row.value = formData.function.autoAudit
        break
      case 'enableTax':
        row.value = formData.function.enableTax
        row.extra = formData.function.taxRate
        break
      case 'allowNegativeInventory':
        row.value = formData.function.allowNegativeInventory
        break
      case 'pricingMethod':
        row.value = formData.function.pricingMethod
        break
      case 'accountingType':
        row.value = formData.function.accountingType
        break
      case 'costRule':
        row.value = formData.function.costRule
        break
      case 'quantityDecimal':
        row.value = formData.function.quantityDecimal
        break
      case 'amountDecimal':
        row.value = formData.function.amountDecimal
        break
      case 'reportDays':
        row.value = formData.function.reportDays
        break
    }
  })
}

const handleLogisticsChange = () => {
  // 物流配置变化处理
}

const handleAddLogistics = () => {
  const newItem = {
    key: `custom_${Date.now()}`,
    name: '自定义物流',
    enabled: false
  }
  formData.logistics.push(newItem)
}

const handleDeleteLogistics = (row: any) => {
  const index = formData.logistics.findIndex(item => item.key === row.key)
  if (index > -1) {
    formData.logistics.splice(index, 1)
    ElMessage.success('删除成功')
  }
}

// 获取系统设置
const fetchSystemSettings = (): Promise<void> => {
  return getSystemSettings().then(res => {
    console.log('获取系统设置接口完整响应:', res)
    console.log('响应数据 data 字段:', res.data)
    
    if (res.code === 10000) {
      // 检查数据是否存在
      if (!res.data || res.data.length === 0) {
        console.warn('后端返回空数据数组，使用默认值')
        ElMessage.info('暂无系统配置数据，使用默认值')
        return
      }
      
      // 使用数据转换器将后端格式转换为前端格式
      const transformedData = SystemConfigTransformer.transformFromApiFormat(res.data || [])
      console.log('转换后的数据:', transformedData)
      
      // 详细检查每个字段
      console.log('🔍 基础信息:', transformedData.basic)
      console.log('🔍 功能参数:', transformedData.function)
      console.log('🔍 物流配置:', transformedData.logistics)
      console.log('🔍 品牌列表:', transformedData.brand)
      
      // 手动合并数据，确保每个字段都被正确处理
      if (transformedData.basic) {
        // 修复类型错误：使用类型安全的属性访问
        const basicKeys = Object.keys(transformedData.basic) as Array<keyof BasicConfig>
        basicKeys.forEach(key => {
          if (transformedData.basic[key] !== undefined) {
            formData.basic[key] = transformedData.basic[key]
          }
        })
      }
      
      if (transformedData.function) {
        // 修复类型错误：使用类型安全的属性访问
        const functionKeys = Object.keys(transformedData.function) as Array<keyof FunctionConfig>
        functionKeys.forEach(key => {
          if (transformedData.function[key] !== undefined) {
              formData.function[key] = transformedData.function[key] as never
          }
        })
      }
      
      if (transformedData.logistics && Array.isArray(transformedData.logistics)) {
        formData.logistics = [...transformedData.logistics]
      }
      
      // 列表配置 - 修复类型错误
      const listConfigs = ['brand', 'unit', 'customerCategory', 'customerGrade', 'supplierCategory'] as const
      listConfigs.forEach(key => {
        if (transformedData[key] && Array.isArray(transformedData[key])) {
          // 使用类型断言确保类型安全
          formData[key] = [...transformedData[key] as string[]]
        }
      })
      
      // 数据加载完成后，更新功能参数表格数据
      updateFunctionRows()
      
      console.log('最终表单数据:', JSON.parse(JSON.stringify(formData)))
      ElMessage.success('配置信息加载成功')
    } else {
      console.error('接口返回错误:', res.message)
      ElMessage.error(res.message || '获取配置信息失败')
    }
  }).catch(error => {
    console.error('获取系统设置失败:', error)
    ElMessage.error('获取配置信息失败，请稍后重试')
  })
}

// 保存设置
const handleSave = () => {
  ElMessageBox.confirm('确定要保存系统设置吗？', '确认保存', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 使用数据转换器将前端格式转换为后端格式
    const apiData = SystemConfigTransformer.transformToApiFormat(formData)
    return saveSystemSettings(apiData)
  }).then(res => {
    if (res.code === 10000) {
      ElMessage.success('系统设置保存成功！')
      // 保存成功后重新加载配置，确保数据同步
      fetchSystemSettings()
    } else {
      ElMessage.error(res.message || '保存失败，请稍后重试')
    }
  }).catch(error => {
    if (error !== 'cancel') {
      console.error('保存系统设置失败:', error)
      ElMessage.error('保存失败，请稍后重试')
    } else {
      ElMessage.info('已取消保存')
    }
  })
}

// 页面加载时获取数据
onMounted(() => {
  fetchSystemSettings().finally(() => {
    // 确保表单有默认值
    console.log('当前表单数据:', JSON.parse(JSON.stringify(formData)))
  })
})
</script>

<style scoped lang="scss">
.system-settings-demo {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 60px);

  .settings-container {
    display: flex;
    background-color: #fff;
    border-radius: 4px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

    .settings-nav {
      width: 180px;
      background-color: #fafafa;
      border-right: 1px solid #e4e7ed;
      padding: 20px 0;

      .nav-item {
        padding: 12px 24px;
        cursor: pointer;
        color: #606266;
        transition: all 0.3s;
        border-left: 3px solid transparent;
        position: relative;

        &:hover {
          background-color: #ecf5ff;
          color: #409eff;
        }

        &.active {
          background-color: #fff;
          color: #409eff;
          border-left-color: #409eff;
          font-weight: 500;
        }
      }
    }

    .settings-content {
      flex: 1;
      padding: 20px;

      .content-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 24px;
        padding-bottom: 16px;
        border-bottom: 1px solid #e4e7ed;

        h2 {
          margin: 0;
          font-size: 18px;
          font-weight: 500;
          color: #303133;
        }
      }

      .content-body {
        min-height: 400px;
      }
    }
  }
}

// 税率配置样式
.tax-config {
  display: flex;
  align-items: center;
  gap: 8px;
  
  .tax-label {
    white-space: nowrap;
    margin-right: 8px;
  }
  
  .tax-input {
    width: 80px;
  }
}
</style>