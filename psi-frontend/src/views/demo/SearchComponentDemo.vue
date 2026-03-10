<!--
 * @Description: 搜索组件演示页面
-->
<template>
  <div class="search-component-demo">
    <el-card class="demo-card">
      <template #header>
        <div class="card-header">
          <span class="title">搜索组件演示</span>
          <el-tag type="info">GoodSearchComponent Demo</el-tag>
        </div>
      </template>

      <el-tabs v-model="activeTab" type="border-card">
        <!-- 供应商搜索演示 -->
        <el-tab-pane label="供应商搜索" name="supplier">
    <div class="demo-section">
            <div class="demo-title">
              <h3>供应商搜索组件 (Supplier)</h3>
              <p class="desc">包含供应商名称、编号、联系人、电话、地址等搜索条件</p>
      </div>
      
            <Supplier 
              ref="supplierRef"
              :pagination="supplierPagination" 
              @search="handleSupplierSearch" 
            />

            <div class="result-panel">
              <h4>搜索结果：</h4>
              <el-table 
                :data="supplierTableData" 
                border 
                style="width: 100%"
                v-loading="supplierLoading"
              >
                <el-table-column prop="name" label="供应商名称" width="150" />
                <el-table-column prop="number" label="供应商编号" width="150" />
                <el-table-column prop="contact" label="联系人" width="120" />
                <el-table-column prop="tel" label="联系电话" width="150" />
                <el-table-column prop="address" label="联系地址" show-overflow-tooltip />
              </el-table>
              
              <el-pagination
                v-model:current-page="supplierPagination.pageIndex"
                v-model:page-size="supplierPagination.pageSize"
                :page-sizes="[10, 20, 50, 100]"
                :total="supplierTotal"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSupplierSearch"
                @current-change="handleSupplierSearch"
                style="margin-top: 20px; justify-content: flex-end"
        />
      </div>
          </div>
        </el-tab-pane>

        <!-- 用户搜索演示 -->
        <el-tab-pane label="用户搜索" name="user">
          <div class="demo-section">
            <div class="demo-title">
              <h3>用户搜索组件 (UserList)</h3>
              <p class="desc">包含用户名称、编号、真实姓名、性别、联系电话等搜索条件</p>
    </div>

            <UserList 
              ref="userRef"
              :pagination="userPagination" 
              @search="handleUserSearch" 
            />

            <div class="result-panel">
              <h4>搜索结果：</h4>
              <el-table 
                :data="userTableData" 
                border 
                style="width: 100%"
                v-loading="userLoading"
              >
                <el-table-column prop="name" label="用户名称" width="150" />
                <el-table-column prop="number" label="用户编号" width="150" />
                <el-table-column prop="realName" label="真实姓名" width="120" />
                <el-table-column prop="gender" label="性别" width="80" />
                <el-table-column prop="tel" label="联系电话" width="150" />
                <el-table-column prop="address" label="联系地址" show-overflow-tooltip />
              </el-table>
              
              <el-pagination
                v-model:current-page="userPagination.pageIndex"
                v-model:page-size="userPagination.pageSize"
                :page-sizes="[10, 20, 50, 100]"
                :total="userTotal"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleUserSearch"
                @current-change="handleUserSearch"
                style="margin-top: 20px; justify-content: flex-end"
        />
      </div>
          </div>
        </el-tab-pane>

        <!-- 仓库搜索演示 -->
        <el-tab-pane label="仓库搜索" name="warehouse">
          <div class="demo-section">
            <div class="demo-title">
              <h3>仓库搜索组件 (Warehouse)</h3>
              <p class="desc">包含仓库名称、编号等搜索条件</p>
    </div>

            <Warehouse 
              ref="warehouseRef"
              :pagination="warehousePagination" 
              @search="handleWarehouseSearch" 
            />

            <div class="result-panel">
              <h4>搜索结果：</h4>
        <el-table
                :data="warehouseTableData" 
          border
          style="width: 100%"
                v-loading="warehouseLoading"
              >
                <el-table-column prop="name" label="仓库名称" width="200" />
                <el-table-column prop="number" label="仓库编号" width="200" />
                <el-table-column prop="manager" label="负责人" width="150" />
                <el-table-column prop="location" label="仓库位置" show-overflow-tooltip />
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="{ row }">
                    <el-tag :type="row.status === '启用' ? 'success' : 'info'">
                      {{ row.status }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
        
        <el-pagination
                v-model:current-page="warehousePagination.pageIndex"
                v-model:page-size="warehousePagination.pageSize"
                :page-sizes="[10, 20, 50, 100]"
                :total="warehouseTotal"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleWarehouseSearch"
                @current-change="handleWarehouseSearch"
                style="margin-top: 20px; justify-content: flex-end"
              />
            </div>
          </div>
        </el-tab-pane>

        <!-- 人员搜索演示 -->
        <el-tab-pane label="人员搜索" name="people">
          <div class="demo-section">
            <div class="demo-title">
              <h3>人员搜索组件 (PeopleList)</h3>
              <p class="desc">包含人员名称、编号、性别、联系电话、联系地址、身份证号、备注等搜索条件</p>
            </div>
            
            <PeopleList 
              ref="peopleRef"
              :pagination="peoplePagination" 
              @search="handlePeopleSearch" 
            />

            <div class="result-panel">
              <h4>搜索结果：</h4>
              <el-table 
                :data="peopleTableData" 
                border 
                style="width: 100%"
                v-loading="peopleLoading"
              >
                <el-table-column prop="name" label="人员名称" width="120" />
                <el-table-column prop="number" label="人员编号" width="150" />
                <el-table-column prop="gender" label="性别" width="80" />
                <el-table-column prop="tel" label="联系电话" width="150" />
                <el-table-column prop="address" label="联系地址" width="200" show-overflow-tooltip />
                <el-table-column prop="idCard" label="身份证号" width="180" />
                <el-table-column prop="remark" label="备注" show-overflow-tooltip />
              </el-table>
              
              <el-pagination
                v-model:current-page="peoplePagination.pageIndex"
                v-model:page-size="peoplePagination.pageSize"
                :page-sizes="[10, 20, 50, 100]"
                :total="peopleTotal"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handlePeopleSearch"
                @current-change="handlePeopleSearch"
                style="margin-top: 20px; justify-content: flex-end"
              />
      </div>
    </div>
        </el-tab-pane>
      </el-tabs>

      <!-- 操作按钮区域 -->
      <div class="action-bar">
        <el-button type="primary" @click="resetCurrentSearch">重置当前搜索</el-button>
        <el-button type="success" @click="triggerCurrentSearch">手动触发搜索</el-button>
        <el-button type="info" @click="getFormData">获取表单数据</el-button>
    </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Supplier, UserList, Warehouse, PeopleList } from '@/components/goodSearchConpoent'

// 当前激活的标签页
const activeTab = ref('supplier')

// ===== 供应商相关 =====
const supplierRef = ref()
const supplierPagination = reactive({ pageIndex: 1, pageSize: 20 })
const supplierTableData = ref<Array<{name: string; number: string; contact: string; tel: string; address: string}>>([])
const supplierTotal = ref(0)
const supplierLoading = ref(false)

// ===== 用户相关 =====
const userRef = ref()
const userPagination = reactive({ pageIndex: 1, pageSize: 20 })
const userTableData = ref<Array<{name: string; number: string; realName: string; gender: string; tel: string; address: string}>>([])
const userTotal = ref(0)
const userLoading = ref(false)

// ===== 仓库相关 =====
const warehouseRef = ref()
const warehousePagination = reactive({ pageIndex: 1, pageSize: 20 })
const warehouseTableData = ref<Array<{name: string; number: string; manager: string; location: string; status: string}>>([])
const warehouseTotal = ref(0)
const warehouseLoading = ref(false)

// ===== 人员相关 =====
const peopleRef = ref()
const peoplePagination = reactive({ pageIndex: 1, pageSize: 20 })
const peopleTableData = ref<Array<{name: string; number: string; gender: string; tel: string; address: string; idCard: string; remark: string}>>([])
const peopleTotal = ref(0)
const peopleLoading = ref(false)

/**
 * 处理供应商搜索
 */
function handleSupplierSearch(params?: any) {
  console.log('供应商搜索参数:', params)
  supplierLoading.value = true
  
  // 模拟API请求
  setTimeout(() => {
    // 生成模拟数据
    supplierTableData.value = Array.from({ length: params?.pageSize || 20 }, (_, i) => ({
      name: `供应商${i + 1}`,
      number: `SUP${String(i + 1).padStart(4, '0')}`,
      contact: `联系人${i + 1}`,
      tel: `138${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`,
      address: `广东省深圳市南山区科技园${i + 1}号`
    }))
    supplierTotal.value = 100
    supplierLoading.value = false
    
    ElMessage.success('供应商数据加载成功')
  }, 500)
}

/**
 * 处理用户搜索
 */
function handleUserSearch(params?: any) {
  console.log('用户搜索参数:', params)
  userLoading.value = true
  
  // 模拟API请求
  setTimeout(() => {
    // 生成模拟数据
    userTableData.value = Array.from({ length: params?.pageSize || 20 }, (_, i) => ({
      name: `user${i + 1}`,
      number: `USR${String(i + 1).padStart(4, '0')}`,
      realName: `张三${i + 1}`,
      gender: i % 2 === 0 ? '男' : '女',
      tel: `138${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`,
      address: `广东省广州市天河区${i + 1}号`
    }))
    userTotal.value = 85
    userLoading.value = false
    
    ElMessage.success('用户数据加载成功')
  }, 500)
}

/**
 * 处理仓库搜索
 */
function handleWarehouseSearch(params?: any) {
  console.log('仓库搜索参数:', params)
  warehouseLoading.value = true
  
  // 模拟API请求
  setTimeout(() => {
    // 生成模拟数据
    warehouseTableData.value = Array.from({ length: params?.pageSize || 20 }, (_, i) => ({
      name: `${i % 3 === 0 ? '主' : i % 3 === 1 ? '副' : '分'}仓库${i + 1}`,
      number: `WH${String(i + 1).padStart(4, '0')}`,
      manager: `管理员${i + 1}`,
      location: `仓库区域${String.fromCharCode(65 + (i % 5))}栋${i + 1}号`,
      status: i % 3 === 0 ? '启用' : '停用'
    }))
    warehouseTotal.value = 60
    warehouseLoading.value = false
    
    ElMessage.success('仓库数据加载成功')
  }, 500)
}

/**
 * 处理人员搜索
 */
function handlePeopleSearch(params?: any) {
  console.log('人员搜索参数:', params)
  peopleLoading.value = true
  
  // 模拟API请求
  setTimeout(() => {
    const names = ['张三', '李四', '王五', '赵六', '钱七', '孙八', '周九', '吴十']
    // 生成模拟数据
    peopleTableData.value = Array.from({ length: params?.pageSize || 20 }, (_, i) => ({
      name: names[i % names.length] + (i > 7 ? i : ''),
      number: `PEO${String(i + 1).padStart(4, '0')}`,
      gender: i % 2 === 0 ? '男' : '女',
      tel: `138${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`,
      address: `广东省深圳市福田区莲花街道${i + 1}号`,
      idCard: `4403${String(Math.floor(Math.random() * 10000000000000)).padStart(14, '0')}`,
      remark: i % 3 === 0 ? `重要人员-备注${i + 1}` : ''
    }))
    peopleTotal.value = 120
    peopleLoading.value = false
    
    ElMessage.success('人员数据加载成功')
  }, 500)
}

/**
 * 重置当前搜索
 */
function resetCurrentSearch() {
  const refMap = {
    supplier: supplierRef,
    user: userRef,
    warehouse: warehouseRef,
    people: peopleRef
  }
  
  const currentRef = refMap[activeTab.value as keyof typeof refMap]
  if (currentRef?.value?.resetSearch) {
    currentRef.value.resetSearch()
    ElMessage.success('搜索条件已重置')
  }
}

/**
 * 手动触发搜索
 */
function triggerCurrentSearch() {
  const refMap = {
    supplier: supplierRef,
    user: userRef,
    warehouse: warehouseRef,
    people: peopleRef
  }
  
  const currentRef = refMap[activeTab.value as keyof typeof refMap]
  if (currentRef?.value?.triggerSearch) {
    currentRef.value.triggerSearch()
    ElMessage.info('已手动触发搜索')
  }
}

/**
 * 获取表单数据
 */
function getFormData() {
  const refMap = {
    supplier: supplierRef,
    user: userRef,
    warehouse: warehouseRef,
    people: peopleRef
  }
  
  const currentRef = refMap[activeTab.value as keyof typeof refMap]
  if (currentRef?.value?.getFormData) {
    const formData = currentRef.value.getFormData()
    console.log('当前表单数据:', formData)
    ElMessage({
      message: '表单数据已输出到控制台',
      type: 'info',
      duration: 2000
    })
  }
}
</script>

<style scoped lang="scss">
.search-component-demo {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 40px);

  .demo-card {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .title {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
      }
    }
}

.demo-section {
    .demo-title {
      margin-bottom: 20px;
      padding-bottom: 15px;
      border-bottom: 2px solid #e4e7ed;

      h3 {
        margin: 0 0 8px 0;
        font-size: 18px;
        color: #303133;
        font-weight: 600;
      }

      .desc {
        margin: 0;
  font-size: 14px;
        color: #909399;
      }
}

    .result-panel {
      margin-top: 30px;
  padding: 20px;
      background-color: #f9fafc;
  border-radius: 4px;

      h4 {
        margin: 0 0 15px 0;
  font-size: 16px;
        color: #606266;
        font-weight: 600;
      }
    }
  }

  .action-bar {
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #e4e7ed;
  display: flex;
    gap: 12px;
    justify-content: center;
  }

  :deep(.el-tabs) {
    .el-tabs__content {
      padding: 20px;
    }
  }

  :deep(.el-table) {
    font-size: 14px;

    .el-table__header th {
      background-color: #f5f7fa;
      color: #606266;
      font-weight: 600;
    }
  }

  :deep(.el-pagination) {
    display: flex;
    justify-content: flex-end;
  }
}
</style>
