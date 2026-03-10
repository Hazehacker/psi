<template>
  <div class="good-search-demo">
    <el-card header="商品搜索组件演示 - GoodSearchForm">
      <div class="demo-container">
        <!-- 左侧 Tab 导航 -->
        <div class="tab-nav">
          <el-tabs v-model="activeTab" tab-position="left" class="demo-tabs">
            <el-tab-pane label="默认配置" name="demo1">
              <template #label>
                <span class="tab-label">默认配置</span>
              </template>
            </el-tab-pane>
            <el-tab-pane label="自定义字段" name="demo2">
              <template #label>
                <span class="tab-label">自定义字段</span>
              </template>
            </el-tab-pane>
            <el-tab-pane label="自定义配置" name="demo3">
              <template #label>
                <span class="tab-label">自定义配置</span>
              </template>
            </el-tab-pane>
            <el-tab-pane label="日期范围" name="demo4">
              <template #label>
                <span class="tab-label">日期范围</span>
              </template>
            </el-tab-pane>
            <el-tab-pane label="供应商搜索" name="demo5">
              <template #label>
                <span class="tab-label">供应商搜索</span>
              </template>
            </el-tab-pane>
            <el-tab-pane label="用户列表" name="demo6">
              <template #label>
                <span class="tab-label">用户列表</span>
              </template>
            </el-tab-pane>
            <el-tab-pane label="仓库搜索" name="demo8">
              <template #label>
                <span class="tab-label">仓库搜索</span>
              </template>
            </el-tab-pane>
            <el-tab-pane label="人员搜索" name="demo9">
              <template #label>
                <span class="tab-label">人员搜索</span>
              </template>
            </el-tab-pane>
            <el-tab-pane label="账户搜索" name="demo10">
              <template #label>
                <span class="tab-label">账户搜索</span>
              </template>
            </el-tab-pane>
            <el-tab-pane label="客户搜索" name="demo11">
              <template #label>
                <span class="tab-label">客户搜索</span>
              </template>
            </el-tab-pane>
            <el-tab-pane label="使用说明" name="demo7">
              <template #label>
                <span class="tab-label">使用说明</span>
              </template>
            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 右侧内容区域 -->
        <div class="tab-content">
          <!-- 演示1: 默认配置 -->
          <div v-show="activeTab === 'demo1'" class="demo-panel">
            <div class="panel-header">
              <h3>演示1：默认配置（显示所有字段）</h3>
            </div>
            <div class="panel-content">
              <GoodSearchForm v-model="searchData1" :config="config1" @search="handleSearch1" />
              <el-divider />
              <div class="result-box">
                <strong>搜索结果：</strong>
                <pre>{{ JSON.stringify(searchData1, null, 2) }}</pre>
              </div>
            </div>
          </div>

          <!-- 演示2: 自定义显示字段 -->
          <div v-show="activeTab === 'demo2'" class="demo-panel">
            <div class="panel-header">
              <h3>演示2：只显示部分字段（商品名称、单据编号、供应商、日期）</h3>
            </div>
            <div class="panel-content">
              <GoodSearchForm v-model="searchData2" :config="config2" @search="handleSearch2" />
              <el-divider />
              <div class="result-box">
                <strong>搜索结果：</strong>
                <pre>{{ JSON.stringify(searchData2, null, 2) }}</pre>
              </div>
            </div>
          </div>

          <!-- 演示3: 带自定义字段 -->
          <div v-show="activeTab === 'demo3'" class="demo-panel">
            <div class="panel-header">
              <h3>演示3：带自定义字段（商品类型、品牌、仓库等）</h3>
            </div>
            <div class="panel-content">
              <GoodSearchForm v-model="searchData3" :config="config3" @search="handleSearch3" />
              <el-divider />
              <div class="result-box">
                <strong>搜索结果：</strong>
                <pre>{{ JSON.stringify(searchData3, null, 2) }}</pre>
              </div>
            </div>
          </div>

          <!-- 演示4: 自定义默认日期范围 -->
          <div v-show="activeTab === 'demo4'" class="demo-panel">
            <div class="panel-header">
              <h3>演示4：自定义默认日期范围（最近7天）</h3>
            </div>
            <div class="panel-content">
              <GoodSearchForm
                v-model="searchData4"
                :config="config4"
                :defaultDays="7"
                @search="handleSearch4"
              />
              <el-divider />
              <div class="result-box">
                <strong>搜索结果：</strong>
                <pre>{{ JSON.stringify(searchData4, null, 2) }}</pre>
              </div>
            </div>
          </div>

          <!-- 演示5: 供应商搜索组件 -->
          <div v-show="activeTab === 'demo5'" class="demo-panel">
            <SupplierSample />
          </div>

          <!-- 演示6: 用户列表搜索组件（带分页） -->
          <div v-show="activeTab === 'demo6'" class="demo-panel">
            <UserListSample />
          </div>

          <!-- 演示8: 仓库搜索组件 -->
          <div v-show="activeTab === 'demo8'" class="demo-panel">
            <WarehouseSample />
          </div>

          <!-- 演示9: 人员搜索组件 -->
          <div v-show="activeTab === 'demo9'" class="demo-panel">
            <PeopleListSample />
          </div>

          <!-- 演示10: 账户搜索组件 -->
          <div v-show="activeTab === 'demo10'" class="demo-panel">
            <AccountSample />
          </div>

          <!-- 演示11: 客户搜索组件 -->
          <div v-show="activeTab === 'demo11'" class="demo-panel">
            <CustomerSample />
          </div>

          <!-- 组件说明 -->
          <div v-show="activeTab === 'demo7'" class="demo-panel">
            <div class="panel-header">
              <h3>📖 组件使用说明</h3>
            </div>
            <div class="panel-content usage-content">
              <h3>基本用法</h3>
              <div class="code-block">
                <pre><code>{{ basicUsage }}</code></pre>
              </div>

              <h3 style="margin-top: 20px">配置项说明</h3>
              <el-table :data="configTableData" border style="margin-top: 10px">
                <el-table-column prop="field" label="字段名" width="180" />
                <el-table-column prop="type" label="类型" width="120" />
                <el-table-column prop="default" label="默认值" width="100" />
                <el-table-column prop="desc" label="说明" />
              </el-table>

              <h3 style="margin-top: 20px">自定义字段配置</h3>
              <div class="code-block">
                <pre><code>{{ customFieldUsage }}</code></pre>
              </div>

              <h3 style="margin-top: 20px">供应商组件字段说明</h3>
              <el-table :data="supplierFieldTableData" border style="margin-top: 10px">
                <el-table-column prop="field" label="字段名" width="150" />
                <el-table-column prop="key" label="数据键名" width="150" />
                <el-table-column prop="type" label="类型" width="120" />
                <el-table-column prop="desc" label="说明" />
              </el-table>

              <h3 style="margin-top: 20px">用户列表组件字段说明</h3>
              <el-table :data="userListFieldTableData" border style="margin-top: 10px">
                <el-table-column prop="field" label="字段名" width="150" />
                <el-table-column prop="key" label="数据键名" width="150" />
                <el-table-column prop="type" label="类型" width="120" />
                <el-table-column prop="desc" label="说明" />
              </el-table>

              <h3 style="margin-top: 20px">仓库搜索组件字段说明</h3>
              <el-table :data="warehouseFieldTableData" border style="margin-top: 10px">
                <el-table-column prop="field" label="字段名" width="150" />
                <el-table-column prop="key" label="数据键名" width="150" />
                <el-table-column prop="type" label="类型" width="120" />
                <el-table-column prop="desc" label="说明" />
              </el-table>

              <h3 style="margin-top: 20px">人员搜索组件字段说明</h3>
              <el-table :data="peopleListFieldTableData" border style="margin-top: 10px">
                <el-table-column prop="field" label="字段名" width="150" />
                <el-table-column prop="key" label="数据键名" width="150" />
                <el-table-column prop="type" label="类型" width="120" />
                <el-table-column prop="desc" label="说明" />
              </el-table>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import GoodSearchForm from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import SupplierSample from './SupplierSample.vue'
import UserListSample from './UserListtSample.vue'
import WarehouseSample from './WarehouseSample.vue'
import PeopleListSample from './PeopleListSample.vue'
import AccountSample from './AccountSample.vue'
import CustomerSample from './CustomerSample.vue'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent/type'
import { ElMessage } from 'element-plus'

// 当前激活的 Tab
const activeTab = ref('demo1')

// 演示1：默认配置
// 演示1：默认配置（显示所有字段，包括客户和仓库）
const searchData1 = ref<SearchFormData>({})
const config1 = reactive<GoodSearchConfig>({
  showGoods: true,
  showNumber: true,
  showSupplier: true,
  showCustomer: true,     // 显示客户字段
  showWarehouse: true,    // 显示仓库字段
  showPeople: true,
  showBillDate: true,
  showArrivalDate: true,
  showUser: true,
  showExamine: true,
  showState: true,
  showRemark: true
})
const handleSearch1 = (data: SearchFormData) => {
  ElMessage.success('演示1 - 执行搜索')
  console.log('演示1搜索数据：', data)
}

// 演示2：自定义显示字段
const searchData2 = ref<SearchFormData>({})
const config2: GoodSearchConfig = {
  showGoods: true,
  showNumber: true,
  showSupplier: true,
  showBillDate: true,
  showPeople: false,
  showArrivalDate: false,
  showUser: false,
  showExamine: false,
  showState: false,
  showRemark: false
}
const handleSearch2 = (data: SearchFormData) => {
  ElMessage.success('演示2 - 执行搜索')
  console.log('演示2搜索数据：', data)
}

// 演示3：带自定义字段
const searchData3 = ref<SearchFormData>({})
const config3: GoodSearchConfig = {
  showGoods: true,
  showNumber: true,
  showSupplier: true,
  showBillDate: true,
  customFields: [
    {
      key: 'goodsType',
      label: '商品类型',
      type: 'select',
      options: [
        { label: '成品', value: 1 },
        { label: '原材料', value: 2 },
        { label: '半成品', value: 3 }
      ]
    },
    {
      key: 'brand',
      label: '品牌',
      type: 'input'
    },
    {
      key: 'warehouse',
      label: '仓库',
      type: 'nodList',
      nodListConfig: {
        action: 'service/warehouseRecord',
        scene: 'warehouse'
      }
    }
  ]
}
const handleSearch3 = (data: SearchFormData) => {
  ElMessage.success('演示3 - 执行搜索')
  console.log('演示3搜索数据：', data)
}

// 演示4：自定义默认日期范围
const searchData4 = ref<SearchFormData>({})
const config4: GoodSearchConfig = {
  showGoods: true,
  showBillDate: true,
  showExamine: true,
  showState: true
}
const handleSearch4 = (data: SearchFormData) => {
  ElMessage.success('演示4 - 执行搜索')
  console.log('演示4搜索数据：', data)
}

// 基本用法代码
const basicUsage = `<template>
  <GoodSearchForm
    v-model="searchData"
    :config="config"
    :defaultDays="30"
    @search="handleSearch"
  />
</template>

<script setup lang="ts">
import GoodSearchForm from '@/components/goodSearchConpoent/GoodSearchForm.vue'
import type { SearchFormData, GoodSearchConfig } from '@/components/goodSearchConpoent/type'

const searchData = ref<SearchFormData>({})
const config: GoodSearchConfig = {
  showGoods: true,
  showNumber: true,
  showSupplier: true
}

const handleSearch = (data: SearchFormData) => {
  console.log('搜索数据：', data)
  // 执行搜索逻辑
}
<\/script>`

// 自定义字段用法代码
const customFieldUsage = `const config: GoodSearchConfig = {
  customFields: [
    {
      key: 'goodsType',
      label: '商品类型',
      type: 'select',
      options: [
        { label: '成品', value: 1 },
        { label: '原材料', value: 2 }
      ]
    },
    {
      key: 'brand',
      label: '品牌',
      type: 'input'
    },
    {
      key: 'warehouse',
      label: '仓库',
      type: 'nodList',
      nodListConfig: {
        action: 'service/warehouseRecord',
        scene: 'warehouse'
      }
    }
  ]
}`

// 配置项表格数据
const configTableData = [
  { field: 'showGoods', type: 'boolean', default: 'true', desc: '是否显示商品名称搜索' },
  { field: 'showNumber', type: 'boolean', default: 'true', desc: '是否显示单据编号搜索' },
  { field: 'showSupplier', type: 'boolean', default: 'true', desc: '是否显示供应商选择' },
  { field: 'showPeople', type: 'boolean', default: 'true', desc: '是否显示关联人员选择' },
  { field: 'showBillDate', type: 'boolean', default: 'true', desc: '是否显示单据日期范围' },
  { field: 'showArrivalDate', type: 'boolean', default: 'true', desc: '是否显示到货日期范围' },
  { field: 'showUser', type: 'boolean', default: 'true', desc: '是否显示制单人选择' },
  { field: 'showExamine', type: 'boolean', default: 'true', desc: '是否显示审核状态选择' },
  { field: 'showState', type: 'boolean', default: 'true', desc: '是否显示入库状态选择' },
  { field: 'showRemark', type: 'boolean', default: 'true', desc: '是否显示备注信息搜索' },
  { field: 'customFields', type: 'array', default: '[]', desc: '自定义字段配置数组' },
  { field: 'defaultDays', type: 'number', default: '30', desc: '默认日期范围（天数）' }
]

// 供应商组件字段说明
const supplierFieldTableData = [
  { field: '供应商名称', key: 'supplier', type: 'nodList', desc: '输入供应商名称进行搜索' },
  { field: '供应商编号', key: 'supplierCode', type: 'input', desc: '输入供应商编号进行搜索' },
  {
    field: '供应商类别',
    key: 'supplierCategory',
    type: 'select',
    desc: '选择供应商类别：常规类别、本地、电商、成都'
  },
  { field: '联系人员', key: 'people', type: 'nodList', desc: '通过 nodList 组件选择联系人员' },
  { field: '联系电话', key: 'contactPhone', type: 'input', desc: '输入联系电话进行搜索' },
  { field: '所属用户', key: 'user', type: 'nodList', desc: '通过 nodList 组件选择所属用户' },
  { field: '备注信息', key: 'data', type: 'input', desc: '输入备注信息进行搜索' }
]

// 用户列表组件字段说明
const userListFieldTableData = [
  { field: '用户名称', key: 'name', type: 'input', desc: '输入用户名称进行搜索' },
  { field: '用户账号', key: 'user', type: 'input', desc: '输入用户账号进行搜索' },
  { field: '手机号码', key: 'tel', type: 'input', desc: '输入手机号码进行搜索' },
  { field: '备注信息', key: 'data', type: 'input', desc: '输入备注信息进行搜索' }
]

// 仓库搜索组件字段说明
const warehouseFieldTableData = [
  { field: '仓库唯一标识', key: 'id', type: 'input', desc: '输入仓库唯一标识进行搜索' },
  { field: '仓库名称', key: 'name', type: 'input', desc: '输入仓库名称进行搜索' },
  { field: '仓库编号', key: 'number', type: 'input', desc: '输入仓库编号进行搜索' }
]

// 人员搜索组件字段说明
const peopleListFieldTableData = [
  { field: '人员名称', key: 'name', type: 'input', desc: '输入人员名称进行搜索' },
  { field: '人员编号', key: 'number', type: 'input', desc: '输入人员编号进行搜索' },
  { field: '性别', key: 'gender', type: 'select', desc: '选择性别：男/女' },
  { field: '联系电话', key: 'tel', type: 'input', desc: '输入联系电话进行搜索' },
  { field: '联系地址', key: 'address', type: 'input', desc: '输入联系地址进行搜索' },
  { field: '身份证号', key: 'idCard', type: 'input', desc: '输入身份证号进行搜索' },
  { field: '备注信息', key: 'data', type: 'input', desc: '输入备注信息进行搜索' }
]
</script>

<style scoped>
.good-search-demo {
  padding: 20px;
}

.demo-container {
  display: flex;
  min-height: 600px;
}

.tab-nav {
  width: 200px;
  flex-shrink: 0;
}

.demo-tabs {
  height: 100%;
}

.tab-label {
  font-size: 14px;
  font-weight: 500;
}

.tab-content {
  flex: 1;
  padding: 0 20px;
  min-height: 500px;
}

.demo-panel {
  height: 100%;
}

.panel-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e4e7ed;
}

.panel-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.panel-content {
  height: calc(100% - 60px);
  overflow-y: auto;
}

.result-box {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  margin-top: 10px;
}

.result-box pre {
  margin: 10px 0 0 0;
  font-family: 'Courier New', Courier, monospace;
  font-size: 13px;
  color: #606266;
}

.usage-content {
  padding: 10px;
}

.usage-content h3 {
  color: #409eff;
  font-size: 16px;
  margin-bottom: 10px;
}

.code-block {
  background-color: #f5f7fa;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  overflow-x: auto;
}

.code-block pre {
  margin: 0;
  padding: 15px;
  overflow-x: auto;
}

.code-block code {
  font-family: 'Courier New', Courier, monospace;
  font-size: 13px;
  line-height: 1.6;
  color: #303133;
  white-space: pre;
}

/* 供应商演示特定样式 */
.supplier-demo {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.supplier-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 140px;
}

.supplier-info {
  margin-top: 16px;
}

/* 用户列表演示特定样式 */
.userlist-demo {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.userlist-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 140px;
}

.userlist-info {
  margin-top: 16px;
}

/* 分页演示样式 */
.pagination-demo {
  margin-top: 20px;
}

.pagination-controls {
  display: flex;
  flex-direction: column;
  gap: 16px;
  align-items: center;
}

.pagination-info {
  display: flex;
  gap: 8px;
  justify-content: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .demo-container {
    flex-direction: column;
  }

  .tab-nav {
    width: 100%;
    margin-bottom: 20px;
  }

  .tab-content {
    padding: 0;
  }

  .supplier-demo,
  .userlist-demo {
    flex-direction: column;
  }

  .supplier-actions,
  .userlist-actions {
    flex-direction: row;
    min-width: auto;
    margin-top: 16px;
  }
}
</style>
