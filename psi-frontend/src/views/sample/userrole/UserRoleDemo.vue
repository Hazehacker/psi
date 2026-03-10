<!--
 * @Description: 用户角色管理演示页面
-->
<template>
  <div class="frame area user-role-demo">
    <!-- 顶部操作栏 -->
    <div class="layout">
      <!-- 筛选弹出框 -->
      <el-popover ref="searchPopover" popper-class="search-popover" placement="bottom-start" trigger="click" width="400">
        <div class="search-form-container">
          <el-form class="search-form" ref="searchFromRef" :model="searchParams">
            <div class="form-grid">
              <div class="form-item">
                <el-input placeholder="请输入角色名称" v-model="searchParams.name" clearable />
              </div>
              <div class="form-item">
                <el-input placeholder="请输入备注信息" v-model="searchParams.remark" clearable />
              </div>
            </div>
            <el-divider />
            <div class="form-actions">
              <el-button class="search-btn" type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
            </div>
          </el-form>
        </div>
        <template #reference>
          <el-button :icon="MoreFilled"></el-button>
        </template>
      </el-popover>

      <!-- 操作按钮组 -->
      <el-button-group>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增
        </el-button>
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </el-button-group>
    </div>

    <el-divider />

    <!-- 表格 -->
    <el-table :data="filteredTableRows" height="calc(100% - 90px)" border>
      <el-table-column prop="name" label="角色名称" align="center" width="200" />
      <el-table-column prop="remark" label="备注信息" align="center" />
      <el-table-column label="相关操作" align="center" width="200" fixed="right">
        <template #default="{ row }">
          <el-button-group>
            <el-button size="small" type="primary" @click="handleDetail(row)">详情</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      class="tablePagination"
      :current-page="page.current"
      :total="page.total"
      :page-size="page.size"
      :page-sizes="page.sizes"
      :pager-count="page.count"
      @size-change="onPageSizeChange"
      @current-change="onPageChange"
      layout="prev,pager,next,jumper,sizes,total"
    />

    <!-- 详情对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="720px" :close-on-click-modal="false" destroy-on-close>
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基础资料 -->
        <el-tab-pane label="基础资料" name="basic">
          <el-form :model="formData" label-width="100px">
            <el-form-item label="角色名称" required>
              <el-input v-model="formData.name" placeholder="请输入角色名称" />
            </el-form-item>
            <el-form-item label="备注信息">
              <el-input
                v-model="formData.remark"
                type="textarea"
                :rows="4"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 功能权限 -->
        <el-tab-pane label="功能权限" name="function">
          <div class="permission-tree-container">
            <el-tree
              ref="treeRef"
              :data="permissionTree"
              :props="{ label: 'name', children: 'children' }"
              show-checkbox
              node-key="id"
              :default-checked-keys="checkedKeys"
            />
          </div>
        </el-tab-pane>

        <!-- 数据权限 -->
        <el-tab-pane label="数据权限" name="data">
          <el-form :model="formData.dataPermissions" label-width="120px">
            <!-- 组织机构 -->
            <el-form-item label="组织机构">
              <el-popover
                placement="bottom-start"
                :width="300"
                trigger="click"
                popper-class="org-tree-popover"
              >
                <template #reference>
                  <el-input
                    v-model="orgDisplayText"
                    placeholder="请选择"
                    readonly
                    suffix-icon="ArrowDown"
                    style="width: 100%"
                  />
                </template>
                <div class="org-tree-container">
                  <el-tree
                    ref="orgTreeRef"
                    :data="orgTreeData"
                    :props="{ label: 'name', children: 'children' }"
                    show-checkbox
                    node-key="id"
                    :default-checked-keys="orgCheckedKeys"
                    @check="onOrgTreeCheck"
                  />
                </div>
              </el-popover>
            </el-form-item>

            <!-- 客户 -->
            <el-form-item label="客户">
              <el-select v-model="formData.dataPermissions.customer" placeholder="请选择" style="width: 100%">
                <el-option label="全部数据" value="all" />
                <el-option label="用户数据" value="custom" />
                <el-option label="组织数据" value="org" />
              </el-select>
            </el-form-item>

            <!-- 供应商 -->
            <el-form-item label="供应商">
              <el-select v-model="formData.dataPermissions.supplier" placeholder="请选择" style="width: 100%">
                <el-option label="全部数据" value="all" />
                <el-option label="用户数据" value="custom" />
                <el-option label="组织数据" value="org" />
              </el-select>
            </el-form-item>

            <!-- 仓库 -->
            <el-form-item label="仓库">
              <el-select v-model="formData.dataPermissions.warehouse" placeholder="请选择" style="width: 100%">
                <el-option label="全部数据" value="all" />
                <el-option label="用户数据" value="custom" />
                <el-option label="组织数据" value="org" />
              </el-select>
            </el-form-item>

            <!-- 资金账户 -->
            <el-form-item label="资金账户">
              <el-select v-model="formData.dataPermissions.account" placeholder="请选择" style="width: 100%">
                <el-option label="全部数据" value="all" />
                <el-option label="用户数据" value="custom" />
                <el-option label="组织数据" value="org" />
              </el-select>
            </el-form-item>

            <!-- 用户 -->
            <el-form-item label="用户">
              <el-select v-model="formData.dataPermissions.user" placeholder="请选择" style="width: 100%">
                <el-option label="全部数据" value="all" />
                <el-option label="用户数据" value="custom" />
                <el-option label="组织数据" value="org" />
              </el-select>
            </el-form-item>

            <!-- 人员 -->
            <el-form-item label="人员">
              <el-select v-model="formData.dataPermissions.personnel" placeholder="请选择" style="width: 100%">
                <el-option label="全部数据" value="all" />
                <el-option label="用户数据" value="custom" />
                <el-option label="组织数据" value="org" />
              </el-select>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, MoreFilled, Search } from '@element-plus/icons-vue'
import { id } from 'element-plus/es/locales.mjs'

// 分页配置
const page = reactive({ 
  current: 1, 
  total: 0, 
  size: 30, 
  sizes: [30, 60, 90, 150, 300], 
  count: 5 
})

// 演示数据
const demoData = [
  { id: '1', name: '111', remark: '' },
  { id: '2', name: 'NTJK', remark: '' },
  { id: '3', name: '四川', remark: '' },
  { id: '4', name: '0000', remark: '' },
  { id: '5', name: 'role_01', remark: '' },
  { id: '6', name: '123', remark: '' }
]

// 搜索参数
const searchParams = reactive({
  name: '',
  remark: ''
})

// 表格数据
const tableRows = ref<any[]>([])

// 计算属性：根据搜索条件筛选表格数据
const filteredTableRows = computed(() => {
  if (!searchParams.name && !searchParams.remark) {
    return tableRows.value
  }
  
  return tableRows.value.filter(row => {
    const nameMatch = !searchParams.name || row.name?.includes(searchParams.name)
    const remarkMatch = !searchParams.remark || (row.remark && row.remark.includes(searchParams.remark))
    
    return nameMatch && remarkMatch
  })
})

// 对话框显示状态
const dialogVisible = ref(false)

// 对话框标题
const dialogTitle = ref('新增角色')

// 当前活跃标签页
const activeTab = ref('basic')

// 表单数据
const formData = reactive<any>({
  id: '',
  name: '',
  remark: '',
  functionPermissions: {},
  dataPermissions: {
    customer: 'all',
    supplier: 'all',
    warehouse: 'all',
    account: 'all',
    user: 'all',
    personnel: 'all',
    org: []
  }
})

// 组织机构树数据
const orgTreeData = ref<any[]>([])

// 组织机构树选中节点
const orgCheckedKeys = ref<string[]>([])

// 组织机构树引用
const orgTreeRef = ref()

// 权限树数据
const permissionTree = ref<any[]>([])

// 当前选中节点
const checkedKeys = ref<string[]>([])

// 树形引用
const treeRef = ref()

// 计算组织机构显示文本
const orgDisplayText = computed(() => {
  if (orgCheckedKeys.value.length === 0) {
    return '请选择'
  }
  
  const selectedNames: string[] = []
  
  const findNodeName = (nodes: any[], id: string): string | null => {
    for (const node of nodes) {
      if (node.id === id) {
        return node.name
      }
      if (node.children) {
        const found = findNodeName(node.children, id)
        if (found) return found
      }
    }
    return null
  }
  
  orgCheckedKeys.value.forEach(id => {
    const name = findNodeName(orgTreeData.value, id)
    if (name) {
      selectedNames.push(name)
    }
  })
  
  return selectedNames.join('，')
})

/**
 * 初始化权限树
 */
const initPermissionTree = () => {
  permissionTree.value = [
    {
      id: 'purchase',
      name: '采购管理',
      children: [
        {
          id: 'purchase_order',
          name: '采购订单',
          children: [
            { id: 'add', name: '新增' },
            { id: 'delete', name: '删除采购订单' },
            { id: 'modify', name: '修改采购订单' },
            { id: 'view', name: '查看采购订单列表' },
            { id: 'batch', name: '批量' },
            { id: 'approve', name: '审核|反审核' }
          ]
        },
        {
          id: 'purchase_invoice',
          name: '采购单',
          children: [
            { id: 'add', name: '新增' },
            { id: 'delete', name: '删除' },
            { id: 'modify', name: '修改' },
            { id: 'view', name: '查看' },
            { id: 'batch', name: '批量' },
            { id: 'approve', name: '审核|反审核' }
          ]
        },
        {
          id: 'purchase_return',
          name: '采购退货单',
          children: [
            { id: 'add', name: '新增' },
            { id: 'delete', name: '删除' },
            { id: 'modify', name: '修改' },
            { id: 'view', name: '查看' },
            { id: 'batch', name: '批量' },
            { id: 'approve', name: '审核|反审核' }
          ]
        }
      ]
    },
    {
      id: 'sales',
      name: '销售管理',
      children: [
        {
          id: 'sales_order',
          name: '销售订单',
          children: [
            { id: 'add', name: '新增' },
            { id: 'delete', name: '删除' },
            { id: 'modify', name: '修改' },
            { id: 'view', name: '查看' },
            { id: 'batch', name: '批量' },
            { id: 'approve', name: '审核|反审核' }
          ]
        },
        {
          id: 'sales_invoice',
          name: '销售单',
          children: [
            { id: 'add', name: '新增' },
            { id: 'delete', name: '删除' },
            { id: 'modify', name: '修改' },
            { id: 'view', name: '查看' },
            { id: 'batch', name: '批量' },
            { id: 'approve', name: '审核|反审核' }
          ]
        },
        {
          id: 'sales_return',
          name: '销售退货单',
          children: [
            { id: 'add', name: '新增' },
            { id: 'delete', name: '删除' },
            { id: 'modify', name: '修改' },
            { id: 'view', name: '查看' },
            { id: 'batch', name: '批量' },
            { id: 'approve', name: '审核|反审核' }
          ]
        }
      ]
    },
    {
      id: 'inventory',
      name: '库存模块',
      children: [
        {
          id: 'inventory_query',
          name: '库存查询',
          children: [{ id: 'view', name: '查看' }]
        },
        {
          id: 'batch_query',
          name: '批次查询',
          children: [{ id: 'view', name: '查看' }]
        },
        {
          id: 'serial_query',
          name: '序列查询',
          children: [{ id: 'view', name: '查看' }]
        },
        {
          id: 'inventory_count',
          name: '库存盘点',
          children: [{ id: 'view', name: '查看' }]
        },
        {
          id: 'transfer',
          name: '调拨单',
          children: [
            { id: 'add', name: '新增' },
            { id: 'delete', name: '删除' },
            { id: 'modify', name: '修改' },
            { id: 'view', name: '查看' },
            { id: 'batch', name: '批量' },
            { id: 'approve', name: '审核|反审核' }
          ]
        },
        {
          id: 'other_inbound',
          name: '其它入库单',
          children: [
            { id: 'add', name: '新增' },
            { id: 'delete', name: '删除' },
            { id: 'modify', name: '修改' },
            { id: 'view', name: '查看' },
            { id: 'batch', name: '批量' },
            { id: 'approve', name: '审核|反审核' }
          ]
        },
        {
          id: 'other_outbound',
          name: '其它出库单',
          children: [
            { id: 'add', name: '新增' },
            { id: 'delete', name: '删除' },
            { id: 'modify', name: '修改' },
            { id: 'view', name: '查看' },
            { id: 'batch', name: '批量' },
            { id: 'approve', name: '审核|反审核' }
          ]
        }
      ]
    }
  ]
}

/**
 * 初始化组织机构树
 */
const initOrgTree = () => {
  orgTreeData.value = [
    {id:'all',name:'全部数据'},
    {id:'custom',name:'用户数据'},
    { 
      id: 'default_org', 
      name: '默认组织',
      children: [
        { 
          id: 'company1', 
          name: '公司1',
          children: [
            { id: 'department1', name: '部门1' }
          ]
        },
        { id: '111', name: '111' },
        { id: 'NTJK', name: 'NTJK' },
        { id: 'super_admin', name: '超级管理员' },
        { id: 'master', name: '法师' }
      ]
    }
  ]
}

/**
 * 组织机构树选中事件
 */
const onOrgTreeCheck = (data: any, checkedInfo: any) => {
  formData.dataPermissions.org = orgTreeRef.value.getCheckedKeys()
}

/**
 * 处理搜索
 */
const handleSearch = () => {
  // 更新分页总数
  page.total = filteredTableRows.value.length
  page.current = 1 // 重置到第一页
  ElMessage.success(`找到 ${filteredTableRows.value.length} 条数据`)
}

/**
 * 重置搜索条件
 */
const handleResetSearch = () => {
  searchParams.name = ''
  searchParams.remark = ''
  page.total = tableRows.value.length
  page.current = 1
  ElMessage.success('搜索条件已重置')
}

/**
 * 处理新增
 */
const handleAdd = () => {
  dialogTitle.value = '新增角色'
  activeTab.value = 'basic'
  Object.assign(formData, {
    id: '',
    name: '',
    remark: '',
    functionPermissions: {},
    dataPermissions: {
      customer: 'all',
      supplier: 'all',
      warehouse: 'all',
      account: 'all',
      user: 'all',
      personnel: 'all',
      org: []
    }
  })
  checkedKeys.value = []
  orgCheckedKeys.value = []
  dialogVisible.value = true
  
  // 重置组织机构树选中状态
  nextTick(() => {
    if (orgTreeRef.value) {
      orgTreeRef.value.setCheckedKeys([])
    }
  })
}

/**
 * 处理详情
 */
const handleDetail = (row: any) => {
  dialogTitle.value = '详情'
  activeTab.value = 'basic'
  
  Object.assign(formData, {
    id: row.id,
    name: row.name,
    remark: row.remark || '',
    functionPermissions: row.functionPermissions || {},
    dataPermissions: row.dataPermissions || {
      customer: 'all',
      supplier: 'all',
      warehouse: 'all',
      account: 'all',
      user: 'all',
      personnel: 'all',
      org: []
    }
  })
  
  checkedKeys.value = []
  orgCheckedKeys.value = formData.dataPermissions.org || []
  
  dialogVisible.value = true
  
  // 设置组织机构树选中状态
  nextTick(() => {
    if (orgTreeRef.value) {
      orgTreeRef.value.setCheckedKeys(orgCheckedKeys.value)
    }
  })
}

/**
 * 处理删除
 */
const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该角色吗？(演示：不实际删除)', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = tableRows.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      tableRows.value.splice(index, 1)
      page.total = tableRows.value.length
      ElMessage.success('删除成功(演示)')
    }
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

/**
 * 处理刷新
 */
const handleRefresh = () => {
  handleResetSearch()
  ElMessage.success('刷新成功')
}

/**
 * 处理保存
 */
const handleSave = () => {
  // 获取勾选的权限
  const checked = treeRef.value.getCheckedKeys()
  formData.functionPermissions = checked.reduce((acc: any, key: string) => {
    acc[key] = true
    return acc
  }, {})
  
  // 获取组织机构树选中的节点
  if (orgTreeRef.value) {
    formData.dataPermissions.org = orgTreeRef.value.getCheckedKeys()
  }
  
  console.log('保存的数据:', formData)
  
  if (formData.id) {
    // 修改
    const index = tableRows.value.findIndex(item => item.id === formData.id)
    if (index > -1) {
      tableRows.value[index] = { ...tableRows.value[index], ...formData }
      page.total = tableRows.value.length
      ElMessage.success('修改成功(演示)')
    }
  } else {
    // 新增
    const newItem = {
      id: Date.now().toString(),
      ...formData
    }
    tableRows.value.push(newItem)
    page.total = tableRows.value.length
    ElMessage.success('新增成功(演示)')
  }
  
  dialogVisible.value = false
}

/**
 * 分页大小改变
 */
const onPageSizeChange = (size: number) => { 
  page.size = size 
}

/**
 * 页码改变
 */
const onPageChange = (p: number) => { 
  page.current = p 
}

// 页面加载时初始化
onMounted(() => {
  initPermissionTree()
  initOrgTree()
  // 初始化表格数据
  tableRows.value = [...demoData]
  page.total = tableRows.value.length
})
</script>

<style scoped lang="scss">
.user-role-demo {
  .layout { 
    display: flex; 
    justify-content: space-between; 
    
    .el-button-group {
      display: flex;
    }
  }
}

.permission-tree-container {
  max-height: 400px;
  overflow-y: auto;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 10px;
}

.org-tree-container {
  max-height: 300px;
  overflow-y: auto;
  padding: 10px;
}

// 搜索弹窗样式
:deep(.search-popover) {
  width: 400px !important;
  max-width: 400px !important;
  padding: 16px;
  
  .search-form-container {
    width: 100%;
    
    .search-form {
      width: 100%;
      
      .form-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 12px;
        width: 100%;
        
        .form-item {
          width: 100%;
          
          .el-input {
            width: 100%;
            
            .el-input__wrapper {
              width: 100%;
              box-sizing: border-box;
            }
          }
        }
      }
      
      .el-divider {
        margin: 16px 0;
      }
      
      .form-actions {
        width: 100%;
        display: flex;
        justify-content: center;
        
        .search-btn {
          width: 100%;
        }
      }
    }
  }
}

:deep(.org-tree-popover) {
  padding: 0;
}

// 重置 Element Plus 默认样式
:deep(.el-popper) {
  &.search-popover {
    .el-popper__arrow {
      display: none;
    }
    
    .el-input {
      .el-input__wrapper {
        width: 100%;
        box-sizing: border-box;
      }
    }
  }
}

/* 标签页样式优化 */
:deep(.el-tabs__content) {
  padding: 16px 0;
}

/* 对话框样式优化 */
:deep(.el-dialog__body) {
  padding: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .layout {
    flex-direction: column;
    gap: 10px;
    
    .el-button-group {
      justify-content: flex-end;
    }
  }
  
  :deep(.search-popover) {
    width: 90vw !important;
  }
  
  .search-form .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>