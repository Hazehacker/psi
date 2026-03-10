<!--
 * @Description: 单据编号管理页面
-->
<template>
  <div class="frame area document-number-manage">
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
        </div>

        <div class="content-body">
          <!-- 获取单据编号 -->
          <div v-if="currentTab === 'generate'" class="generate-section">
            <el-card class="generate-card">
              <template #header>
                <div class="card-header">
                  <span>获取单据编号</span>
                </div>
              </template>
              
              <div class="generate-form">
                <el-form :model="generateForm" label-width="120px">
                  <el-form-item label="单据类型：" required>
                    <el-select 
                      v-model="generateForm.number_type" 
                      placeholder="请选择单据类型"
                      style="width: 100%"
                    >
                      <el-option-group
                        v-for="group in documentTypeOptions"
                        :key="group.label"
                        :label="group.label"
                      >
                        <el-option
                          v-for="item in group.children"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-option-group>
                    </el-select>
                  </el-form-item>
                  
                  <el-form-item>
                    <div class="default-tip">
                      （默认{{ getDefaultDocumentType() }}）
                    </div>
                  </el-form-item>
                  
                  <el-form-item>
                    <el-button 
                      type="primary" 
                      @click="handleGenerate" 
                      :loading="generating"
                      style="width: 100%"
                    >
                      确认生成
                    </el-button>
                  </el-form-item>
                </el-form>
              </div>
            </el-card>
          </div>

          <!-- 查看单据记录 -->
          <div v-else-if="currentTab === 'records'" class="records-section">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>单据记录查询</span>
                  <div class="search-form">
                    <el-form :model="searchForm" inline>
                      <el-form-item label="单据类型：">
                        <el-select 
                          v-model="searchForm.number_type" 
                          placeholder="请选择单据类型"
                          style="width: 200px"
                          clearable
                        >
                          <el-option-group
                            v-for="group in documentTypeOptions"
                            :key="group.label"
                            :label="group.label"
                          >
                            <el-option
                              v-for="item in group.children"
                              :key="item.value"
                              :label="item.label"
                              :value="item.value"
                            />
                          </el-option-group>
                        </el-select>
                      </el-form-item>
                      
                      <el-form-item>
                        <el-button type="primary" @click="handleSearch">确认查询</el-button>
                        <el-button @click="handleReset">重置</el-button>
                      </el-form-item>
                    </el-form>
                  </div>
                </div>
              </template>

              <!-- 单据记录表格 -->
              <el-table 
                :data="tableData" 
                border 
                stripe
                v-loading="loading"
                empty-text="请选择单据类型进行查询"
              >
                <el-table-column 
                  prop="number_type"
                  label="单据类型"
                  width="120"
                  align="center"
                />
                <el-table-column 
                  prop="number"
                  label="流水号"
                  width="100"
                  align="center"
                />
                <el-table-column 
                  prop="document_number"
                  label="完整单据编号"
                  width="200"
                  align="center"
                />
                <el-table-column 
                  prop="create_time"
                  label="创建时间"
                  width="180"
                  align="center"
                />
                
                <!-- 操作列 -->
                <el-table-column label="操作" width="100" align="center" fixed="right">
                  <template #default="{ row }">
                    <el-button 
                      type="danger" 
                      size="small" 
                      @click="handleDelete(row)"
                      :loading="row.deleting"
                    >
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页组件 - 只在有数据时显示 -->
              <div class="pagination-container" v-if="pagination.total > 0">
                <el-pagination
                  v-model:current-page="pagination.current"
                  v-model:page-size="pagination.size"
                  :page-sizes="[10, 20, 30, 50]"
                  :total="pagination.total"
                  layout="total, sizes, prev, pager, next, jumper"
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                />
              </div>
            </el-card>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 左侧标签页
const tabs = [
  { name: 'generate', label: '获取单据编号' },
  { name: 'records', label: '查看单据记录' }
]
const currentTab = ref('generate')

// 单据类型选项（树形结构）
const documentTypeOptions = reactive([
  {
    label: '采购',
    children: [
      { label: '采购订单', value: 'CGDD' },
      { label: '采购单', value: 'CGD' },
      { label: '采购退货单', value: 'CGTHD' }
    ]
  },
  {
    label: '销售',
    children: [
      { label: '销售订单', value: 'XSDD' },
      { label: '销售单', value: 'XSD' },
      { label: '销售退货单', value: 'XSTHD' }
    ]
  },
  {
    label: '仓库',
    children: [
      { label: '调拨单', value: 'DBD' },
      { label: '其它入库单', value: 'QTRKD' },
      { label: '其它出库单', value: 'QTCKD' }
    ]
  },
  {
    label: '资金',
    children: [
      { label: '收款单', value: 'SKD' },
      { label: '付款单', value: 'FKD' },
      { label: '核销单', value: 'HXD' },
      { label: '转账单', value: 'ZZD' },
      { label: '其它收入单', value: 'QTSRD' },
      { label: '其它支出单', value: 'QTZCD' }
    ]
  }
])

// 获取单据编号表单
const generateForm = reactive({
  number_type: 'CGDD' // 默认采购订单
})
const generating = ref(false)

// 查询表单
const searchForm = reactive({
  number_type: ''
})

// 表格数据
const tableData = ref<any[]>([])
const loading = ref(false)

// 分页配置
const pagination = reactive({
  current: 1,
  size: 30,
  total: 0
})

// 获取当前标签页标题
const getCurrentTabLabel = () => {
  const tab = tabs.find((t) => t.name === currentTab.value)
  return tab ? tab.label : '单据编号管理'
}

// 获取默认单据类型显示
const getDefaultDocumentType = () => {
  const defaultType = (documentTypeOptions as any[])
    .reduce((acc: any[], group: any) => acc.concat(group.children || []), [])
    .find((item: any) => item.value === generateForm.number_type)
  return defaultType ? defaultType.label : '采购订单'
}

// 生成单据编号
const handleGenerate = async () => {
  if (!generateForm.number_type) {
    ElMessage.warning('请选择单据类型')
    return
  }

  generating.value = true
  try {
    // TODO: 调用生成单据编号的API
    // const response = await generateDocumentNumber({
    //   number_type: generateForm.number_type
    // })
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 模拟返回数据 - 根据数据库表结构
    const documentNumber = `${generateForm.number_type}-${new Date().getTime()}`
    
    ElMessage.success(`单据编号生成成功：${documentNumber}`)
    
    // 重置表单
    generateForm.number_type = 'CGDD'
    
  } catch (error) {
    console.error('生成单据编号失败:', error)
    ElMessage.error('生成单据编号失败')
  } finally {
    generating.value = false
  }
}

// 查询单据记录
const handleSearch = async () => {
  if (!searchForm.number_type) {
    ElMessage.warning('请选择单据类型')
    return
  }

  loading.value = true
  try {
    // TODO: 调用查询单据记录的API
    // const response = await getDocumentRecords({
    //   number_type: searchForm.number_type,
    //   page: pagination.current,
    //   size: pagination.size
    // })
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 800))
    
    // 模拟返回数据 
    const selectedType = (documentTypeOptions as any[])
      .reduce((acc: any[], group: any) => acc.concat(group.children || []), [])
      .find((item: any) => item.value === searchForm.number_type)
    
    const mockData = {
      total: 85,
      rows: Array.from({ length: Math.min(pagination.size, 85) }, (_, index) => {
        // 生成模拟数据
        const id = index + 1 + (pagination.current - 1) * pagination.size
        const number = Math.floor(Math.random() * 1000) + 1
        const create_time = new Date(Date.now() - Math.random() * 10000000000).toLocaleString()
        const document_number = `${searchForm.number_type}-${new Date().toISOString().slice(0, 10).replace(/-/g, '')}-${String(number).padStart(3, '0')}`
        
        return {
          id, // 内部使用，不显示在表格中
          number_type: selectedType?.label || searchForm.number_type,
          number,
          create_time,
          document_number
        }
      })
    }
    
    tableData.value = mockData.rows
    pagination.total = mockData.total
    
  } catch (error) {
    console.error('查询单据记录失败:', error)
    ElMessage.error('查询单据记录失败')
  } finally {
    loading.value = false
  }
}

// 重置查询
const handleReset = () => {
  searchForm.number_type = ''
  tableData.value = []
  pagination.total = 0
  pagination.current = 1
}

// 删除单据记录
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除单据编号为 "${row.document_number}" 的记录吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 标记删除中状态
    row.deleting = true
    
    // TODO: 调用删除API
    // await deleteDocumentRecord(row.id)
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 从表格中移除
    const index = tableData.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      tableData.value.splice(index, 1)
      pagination.total -= 1
      
      // 如果当前页没有数据了，且不是第一页，则跳转到前一页
      if (tableData.value.length === 0 && pagination.current > 1) {
        pagination.current -= 1
        handleSearch()
      }
    }
    
    ElMessage.success('删除成功')
    
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除单据记录失败:', error)
      ElMessage.error('删除失败')
    }
  } finally {
    row.deleting = false
  }
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  handleSearch()
}

// 当前页变化
const handleCurrentChange = (page: number) => {
  pagination.current = page
  handleSearch()
}

// 页面加载时初始化数据
onMounted(() => {
  // 记录页面初始时不加载数据，等待用户查询
})
</script>

<style scoped lang="scss">
.document-number-manage {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 60px);

  .settings-container {
    display: flex;
    background-color: #fff;
    border-radius: 4px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    min-height: 600px;

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

// 生成单据编号区域样式
.generate-section {
  max-width: 500px;
  margin: 0 auto;
  
  .generate-card {
    .card-header {
      font-size: 16px;
      font-weight: 500;
      color: #303133;
    }
    
    .generate-form {
      .default-tip {
        color: #909399;
        font-size: 14px;
        margin-left: 120px;
        margin-top: -16px;
        margin-bottom: 16px;
      }
    }
  }
}

// 单据记录区域样式
.records-section {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 16px;
    font-weight: 500;
    color: #303133;
    
    .search-form {
      margin-right: 20px;
    }
  }
  
  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .document-number-manage {
    padding: 10px;
    
    .settings-container {
      flex-direction: column;
      
      .settings-nav {
        width: 100%;
        display: flex;
        overflow-x: auto;
        padding: 10px 0;
        
        .nav-item {
          white-space: nowrap;
          border-left: none;
          border-bottom: 3px solid transparent;
          
          &.active {
            border-left: none;
            border-bottom-color: #409eff;
          }
        }
      }
    }
    
    .records-section {
      .card-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 16px;
        
        .search-form {
          margin-right: 0;
          width: 100%;
        }
      }
    }
  }
}
</style>