<!--
 * @Description: 系统设置演示页面（带演示数据）
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
          <el-button type="primary" @click="handleSave">保存设置</el-button>
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
            <el-table :data="functionTableData" border stripe>
              <el-table-column prop="name" label="功能名称" width="200" />
              <el-table-column label="功能配置" width="300">
                <template #default="{ row }">
                  <!-- 自动审核功能 - 使用开关控件 -->
                  <el-switch
                    v-if="row.key === 'autoAudit'"
                    v-model="row.value"
                    @change="handleFunctionChange(row)"
                  />
                  <!-- 启用税金功能 - 复合控件：开关 + 税率输入框 -->
                  <div v-else-if="row.key === 'enableTax'" class="tax-config">
                    <el-switch v-model="row.value" @change="handleFunctionChange(row)" />
                    <span class="tax-label">增值税税率</span>
                    <el-input
                      v-model="row.extra"
                      type="number"
                      :min="0"
                      :max="100"
                      class="tax-input"
                      @change="handleFunctionChange(row)"
                    />
                    <span>%</span>
                  </div>
                  <!-- 允许负库存功能 - 使用开关控件 -->
                  <el-switch
                    v-else-if="row.key === 'allowNegativeInventory'"
                    v-model="row.value"
                    @change="handleFunctionChange(row)"
                  />
                  <!-- 计价方法下拉框 -->
                  <el-select
                    v-else-if="row.key === 'pricingMethod'"
                    v-model="row.value"
                    @change="handleFunctionChange(row)"
                  >
                    <el-option label="先进先出" value="fifo" />
                    <el-option label="基础计价" value="base" />
                    <el-option label="移动平均" value="ma" />
                  </el-select>
                  <!-- 核算类型下拉框 -->
                  <el-select
                    v-else-if="row.key === 'accountingType'"
                    v-model="row.value"
                    @change="handleFunctionChange(row)"
                  >
                    <el-option label="分仓核算" value="warehouse" />
                    <el-option label="总仓核算" value="headquarters" />
                  </el-select>
                  <!-- 成本规则下拉框 -->
                  <el-select
                    v-else-if="row.key === 'costRule'"
                    v-model="row.value"
                    @change="handleFunctionChange(row)"
                  >
                    <el-option label="结存结余" value="balance" />
                    <el-option label="辅助属性" value="attr" />
                    <el-option label="批次日期" value="batch" />
                    <el-option label="属性批次" value="aab" />
                  </el-select>
                  <!-- 数量位数下拉框 -->
                  <el-select
                    v-else-if="row.key === 'quantityDecimal'"
                    v-model="row.value"
                    @change="handleFunctionChange(row)"
                  >
                    <el-option label="0位小数" :value="0" />
                    <el-option label="1位小数" :value="1" />
                    <el-option label="2位小数" :value="2" />
                    <el-option label="3位小数" :value="3" />
                    <el-option label="4位小数" :value="4" />
                  </el-select>
                  <!-- 金额位数下拉框 -->
                  <el-select
                    v-else-if="row.key === 'amountDecimal'"
                    v-model="row.value"
                    @change="handleFunctionChange(row)"
                  >
                    <el-option label="2位小数" :value="2" />
                    <el-option label="3位小数" :value="3" />
                    <el-option label="4位小数" :value="4" />
                  </el-select>
                  <!-- 报表天数数字输入 -->
                  <el-input-number
                    v-else-if="row.key === 'reportDays'"
                    v-model="row.value"
                    :min="0"
                    :precision="0"
                    style="width: 120px"
                    @change="handleFunctionChange(row)"
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
                  <el-switch
                    v-model="row.enabled"
                    :disabled="row.key === 'auto'"
                    @change="handleLogisticsChange"
                  />
                </template>
              </el-table-column>
              <el-table-column label="相关操作" width="120">
                <!-- 表头操作：添加按钮 -->
                <template #header>
                  <el-icon :size="20" style="cursor: pointer" @click="handleAddLogistics">
                    <Plus />
                  </el-icon>
                </template>
                <!-- 行操作：删除按钮 -->
                <template #default="{ row }">
                  <el-icon
                    v-if="row.key !== 'auto'"
                    :size="18"
                    style="cursor: pointer; color: #f56c6c"
                    @click="handleDeleteLogistics(row)"
                  >
                    <Delete />
                  </el-icon>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 商品品牌 -->
          <InputListConfig
            v-if="currentTab === 'brand'"
            v-model="formData.brand"
            label="商品品牌"
            placeholder="请输入品牌名称"
          />

          <!-- 计量单位 -->
          <InputListConfig
            v-if="currentTab === 'unit'"
            v-model="formData.unit"
            label="计量单位"
            placeholder="请输入计量单位名称"
            :reserved-items="['多单位', '-1']"
          />

          <!-- 客户类别 -->
          <InputListConfig
            v-if="currentTab === 'customer-category'"
            v-model="formData.customerCategory"
            label="客户类别"
            placeholder="请输入客户类别名称"
            :protected-items="['常规类别']"
            :reserved-items="['常规类别']"
          />

          <!-- 客户等级 -->
          <InputListConfig
            v-if="currentTab === 'customer-grade'"
            v-model="formData.customerGrade"
            label="客户等级"
            placeholder="请输入客户等级名称"
            :protected-items="['常规等级']"
            :reserved-items="['常规等级']"
          />

          <!-- 供应商类别 -->
          <InputListConfig
            v-if="currentTab === 'supplier-category'"
            v-model="formData.supplierCategory"
            label="供应商类别"
            placeholder="请输入供应商类别名称"
            :protected-items="['常规类别']"
            :reserved-items="['常规类别']"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import InputListConfig from '@/views/advancedSettings/sysmanage/components/InputListConfig.vue'

// 辅助函数
const getPricingMethodLabel = (value: string) => {
  const labels: Record<string, string> = {
    fifo: '先进先出',
    base: '基础计价',
    ma: '移动平均'
  }
  return labels[value] || value
}

const getAccountingTypeLabel = (value: string) => {
  const labels: Record<string, string> = {
    warehouse: '分仓核算',
    headquarters: '总仓核算'
  }
  return labels[value] || value
}

const getCostRuleLabel = (value: string) => {
  const labels: Record<string, string> = {
    balance: '结存结余',
    attr: '辅助属性',
    batch: '批次日期',
    aab: '属性批次'
  }
  return labels[value] || value
}

// 定义标签页
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

// 演示数据（仅用于演示页面，实际系统中从后端获取）
const formData = reactive({
  basic: {
    systemName: '点可云进销存软件',
    companyName: '山西点可云科技有限公司',
    filingInfo: '晋备000000-0',
    notice: '欢迎使用点可云进销存系统\n官网地址: www.nodcloud.com\n反馈邮箱: ceo@nodcloud.com'
  },
  function: {
    autoAudit: true,
    enableTax: true,
    taxRate: 13,
    allowNegativeInventory: true,
    pricingMethod: 'fifo',
    accountingType: 'warehouse',
    costRule: 'balance',
    quantityDecimal: 2,
    amountDecimal: 2,
    reportDays: 365
  } as any,
  logistics: [
    { key: 'auto', name: '自动识别', enabled: true },
    { key: 'debangwuliu', name: '德邦物流', enabled: false },
    { key: 'ems', name: '邮政快递', enabled: false },
    { key: 'kuaijiesudi', name: '快捷速递', enabled: false },
    { key: 'quanfengkuaidi', name: '全峰快递', enabled: false },
    { key: 'shentong', name: '申通速递', enabled: false },
    { key: 'shunfeng', name: '顺丰速递', enabled: false },
    { key: 'tiantian', name: '天天快递', enabled: false },
    { key: 'youshuwuliu', name: '优速物流', enabled: false },
    { key: 'yuantong', name: '圆通速递', enabled: false },
    { key: 'yunda', name: '韵达快运', enabled: false },
    { key: 'zhongtong', name: '中通速递', enabled: false },
    { key: 'htky', name: '百世快递', enabled: false }
  ],
  brand: ['tml', 'haigelis', 'bdm', '点可云', '图灵机'],
  unit: ['个', '箱', '包'],
  customerCategory: ['常规类别', 'VIP客户'],
  customerGrade: ['常规等级', '金牌客户'],
  supplierCategory: ['常规类别', '一级供应商']
})

// 功能参数表格数据
const functionTableData = reactive([
  {
    key: 'autoAudit',
    name: '自动审核',
    value: computed({
      get: () => formData.function.autoAudit,
      set: (val) => { formData.function.autoAudit = val }
    }),
    description: '启用后单据模块将自动审核'
  },
  {
    key: 'enableTax',
    name: '启用税金',
    value: computed({
      get: () => formData.function.enableTax,
      set: (val) => { formData.function.enableTax = val }
    }),
    extra: computed({
      get: () => formData.function.taxRate,
      set: (val) => { formData.function.taxRate = val }
    }),
    description: '增值税税率'
  },
  {
    key: 'allowNegativeInventory',
    name: '允许负库存',
    value: computed({
      get: () => formData.function.allowNegativeInventory,
      set: (val) => { formData.function.allowNegativeInventory = val }
    }),
    description: '启用后商品库存将允许为负数'
  },
  {
    key: 'pricingMethod',
    name: '计价方法',
    value: computed({
      get: () => formData.function.pricingMethod,
      set: (val) => { formData.function.pricingMethod = val }
    }),
    description: '根据实际情况选择相应计价方法'
  },
  {
    key: 'accountingType',
    name: '核算类型',
    value: computed({
      get: () => formData.function.accountingType,
      set: (val) => { formData.function.accountingType = val }
    }),
    description: '根据实际情况选择相应核算类型'
  },
  {
    key: 'costRule',
    name: '成本规则',
    value: computed({
      get: () => formData.function.costRule,
      set: (val) => { formData.function.costRule = val }
    }),
    description: '根据实际情况选择相应成本规则'
  },
  {
    key: 'quantityDecimal',
    name: '数量位数',
    value: computed({
      get: () => formData.function.quantityDecimal,
      set: (val) => { formData.function.quantityDecimal = val }
    }),
    description: '全局数量小数位数范围'
  },
  {
    key: 'amountDecimal',
    name: '金额位数',
    value: computed({
      get: () => formData.function.amountDecimal,
      set: (val) => { formData.function.amountDecimal = val }
    }),
    description: '全局金额小数位数范围'
  },
  {
    key: 'reportDays',
    name: '报表天数',
    value: computed({
      get: () => formData.function.reportDays,
      set: (val) => { formData.function.reportDays = val }
    }),
    description: '全局报表统计天数范围'
  }
])

// 事件处理函数
const handleFunctionChange = (row: any) => {
  // 数据已经通过computed双向绑定自动更新
  console.log('功能参数已更新:', row.key, row.value)
}

const handleLogisticsChange = () => {
  console.log('物流配置已更新')
}

const handleAddLogistics = () => {
  const newItem = {
    key: `custom_${Date.now()}`,
    name: '自定义物流',
    enabled: false
  }
  formData.logistics.push(newItem)
  ElMessage.success('添加物流成功')
}

const handleDeleteLogistics = (row: any) => {
  const index = formData.logistics.findIndex((item) => item.key === row.key)
  if (index > -1) {
    formData.logistics.splice(index, 1)
    ElMessage.success('删除物流成功')
  }
}

// 获取当前标签页的标题
const getCurrentTabLabel = () => {
  for (let i = 0; i < tabs.length; i++) {
    if (tabs[i].name === currentTab.value) {
      return tabs[i].label
    }
  }
  return '系统设置'
}

// 保存设置（演示页面只打印数据，不调用真实接口）
const handleSave = () => {
  ElMessageBox.confirm('确定要保存系统设置吗？', '确认保存', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 演示页面只打印数据
    console.log('保存的数据:', JSON.stringify(formData, null, 2))
    ElMessage.success('系统设置保存成功！(演示数据)')
  }).catch(() => {
    ElMessage.info('已取消保存')
  })
}
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


